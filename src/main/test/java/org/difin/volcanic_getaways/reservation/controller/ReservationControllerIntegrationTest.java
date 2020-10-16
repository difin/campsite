package org.difin.volcanic_getaways.reservation.controller;

import org.difin.volcanic_getaways.reservation.model.in.BookingDates;
import org.difin.volcanic_getaways.reservation.model.in.ReservationPayload;
import org.difin.volcanic_getaways.reservation.service.reservation.CancellationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.MessageSource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasLength;

@TestPropertySource(
        properties = {
                "spring.datasource.url=jdbc:h2:file:~/h2/volcanic_getaways_test"
        }
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ReservationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    CancellationService cancellationService;

    @Value("${app.spots-num}")
    int spots;

    @BeforeEach
    public void beforeEach() {
        cancellationService.deleteAllReservations();
    }

    private final String arrival = "2020-Nov-01";
    private final String departure = "2020-Nov-04";
    private final String name = "some name";
    private final String email = "someone@somewhere.com";

    private final BookingDates bookingDates = new BookingDates(arrival, departure);
    private final ReservationPayload payload = ReservationPayload.builder().name(name).email(email).bookingDates(bookingDates).build();

    @DisplayName("When creating a new reservation and there are available dates then reservations are creates successfully")
    @Test
    public void makingReservationTest() {

        // For future reference - getting result and closing stream
        //
        // BookingReference result =
        //     webTestClient
        //         .post()
        //         .uri("http://localhost:" + port + "/api/reservations")
        //         .body(Mono.just(payload), ReservationPayload.class)
        //         .exchange()
        //         .expectStatus().isOk()
        //         .expectBody(BookingReference.class)
        //         .returnResult()
        //         .getResponseBody();

        webTestClient
                .post()
                .uri("http://localhost:" + port + "/api/reservations")
                .body(Mono.just(payload), ReservationPayload.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$.bookingReference").exists()
                    .jsonPath("$.bookingReference").value(hasLength(36));

    }

    @DisplayName("A date can be booked only the specified maximum number of times and then error message it returned")
    @Test
    public void reservingSameDatesTest() {

        Runnable successFunc =
                () -> webTestClient
                        .post()
                        .uri("http://localhost:" + port + "/api/reservations")
                        .body(Mono.just(payload), ReservationPayload.class)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                            .jsonPath("$.bookingReference").exists()
                            .jsonPath("$.bookingReference").value(hasLength(36));

        IntStream.range(0, spots).forEach(i -> successFunc.run());

        webTestClient
            .post()
            .uri("http://localhost:" + port + "/api/reservations")
            .body(Mono.just(payload), ReservationPayload.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$.message").isEqualTo(messageSource.getMessage("volcanic_getaways.exception.reservation.full.capacity", null, null, Locale.getDefault()));
    }
}