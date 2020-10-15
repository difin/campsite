package campsite.reservation.controller;

import campsite.reservation.model.in.BookingDates;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.service.reservation.CancellationService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestPropertySource(
        properties = {
                "spring.datasource.url=jdbc:h2:file:~/h2/campsite_test",
                "app.spots-num=1"
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

    @Value("${app.spots-num}")
    int spots;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    @BeforeEach
    public void beforeEach() {
        cancellationService.deleteAllReservations();
    }

    private final String siteAtFullCapacityMsg = "{\"message\":\"Reservation couldn't proceed because campsite " +
            "is at full capacity on one or more days, please choose other dates\"}";

    // please note that departure is not included : [arrival, departure)
    private final BookingDates bookingDates1 = new BookingDates("2020-Nov-02", "2020-Nov-05");
    private final BookingDates bookingDates2 = new BookingDates("2020-Nov-03", "2020-Nov-06");
    private final BookingDates bookingDates3 = new BookingDates("2020-Nov-04", "2020-Nov-07");

    private final List<BookingDates> bookingDatesList = Arrays.asList(bookingDates1, bookingDates2, bookingDates3);

    private ReservationPayload generateReservationPayload() {

        String name = faker.name().fullName();
        String email = faker.name().firstName() + faker.name().lastName() + "@somewhere.com";

        BookingDates bookingDates = bookingDatesList.get(random.ints(0,2).findFirst().getAsInt());
        ReservationPayload payload = ReservationPayload.builder().name(name).email(email).bookingDates(bookingDates).build();

        return payload;
    }

    @DisplayName("concurrent")
    @Test
    public void reservingSameDatesTest() {

        List<CompletableFuture<Void>> threads = new ArrayList<>();

        IntStream.range(1, 1000).forEach(i -> {
            CompletableFuture<Void> thread =
                    CompletableFuture.runAsync(() -> {
                        webTestClient
                                .post()
                                .uri("http://localhost:" + port + "/api/reservation")
                                .body(Mono.just(generateReservationPayload()), ReservationPayload.class)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(String.class)
                                .consumeWith(t -> {
                                    String result = t.getResponseBody();
                                    assertTrue(result.startsWith("{\"bookingReference\":\"") || result.equals(siteAtFullCapacityMsg));
                                });
                    } );

            threads.add(thread);
        });

        threads.forEach(CompletableFuture::join);
    }
}