package org.difin.volcanic_getaways.reservation.controller;

import org.difin.volcanic_getaways.reservation.data.entity.Reservation;
import org.difin.volcanic_getaways.reservation.model.request.BookingDates;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.service.reservation.CancellationService;
import org.difin.volcanic_getaways.reservation.service.reservation.ReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestPropertySource(
        properties = {
                "spring.datasource.url=jdbc:h2:file:~/h2/volcanic_getaways_test",
                "app.spots-num=10"
        }
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ReservationControllerIntegrationConcurrentTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    CancellationService cancellationService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    private MessageSource messageSource;

    @Value("${app.spots-num}")
    int spotsOnSite;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    @BeforeEach
    public void beforeEach() {
        cancellationService.deleteAllReservations();
    }

    // please note that departure is not included : [arrival, departure)
    private final BookingDates bookingDates1 = new BookingDates("2020-November-02", "2020-November-05");
    private final BookingDates bookingDates2 = new BookingDates("2020-November-03", "2020-November-06");
    private final BookingDates bookingDates3 = new BookingDates("2020-November-04", "2020-November-07");

    private final List<BookingDates> bookingDatesList = Arrays.asList(bookingDates1, bookingDates2, bookingDates3);

    private ReservationPayload generateReservationPayload() {

        String name = faker.name().fullName();
        String email = faker.name().firstName() + faker.name().lastName() + "@somewhere.com";

        BookingDates bookingDates = bookingDatesList.get(random.ints(0,2).findFirst().getAsInt());
        ReservationPayload payload = ReservationPayload.builder().name(name).email(email).bookingDates(bookingDates).build();

        return payload;
    }

    @DisplayName("When trying to reserve the same range of dates concurrently then only #spotsOnSite reservations succeeds and others fail site at full capacity message")
    @Test
    public void reservingSameDatesTest() {

        List<CompletableFuture<Void>> threads = new ArrayList<>();

        IntStream.range(1, 1000).forEach(i -> {
            CompletableFuture<Void> thread =
                CompletableFuture.runAsync(() -> {
                webTestClient
                    .post()
                    .uri("http://localhost:" + port + "/api/reservations")
                    .body(Mono.just(generateReservationPayload()), ReservationPayload.class)
                    .exchange()
                    .expectStatus().value(oneOf(HttpStatus.OK.value(), HttpStatus.CONFLICT.value()))
                    .expectBody(String.class)
                    .consumeWith(t -> {
                        Map<String, String> map = parse(t.getResponseBody());
                        assertTrue(((map.containsKey("bookingReference") && map.get("bookingReference").length() == 36) ||
                                   (map.containsKey("errors"))));
                    });
                } );

            threads.add(thread);
        });

        threads.forEach(CompletableFuture::join);

        List<Reservation> reservations = reservationService.getReservationsBlocking(Optional.empty());

        assertEquals(spotsOnSite, reservations.size());
    }

    private Map<String, String> parse(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(jsonString, Map.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}