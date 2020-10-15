package campsite.reservation.controller;

import campsite.reservation.model.in.BookingDates;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.service.reservation.CancellationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasLength;

@TestPropertySource(
        properties = {
                "spring.datasource.url=jdbc:h2:file:~/h2/campsite_test"
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

    private final String siteAtFullCapacityMessage = "{\"message\":\"Reservation couldn't proceed because campsite " +
            "is at full capacity on one or more days, please choose other dates\"}";

    @DisplayName("When creating a new reservation and there are available dates then reservations are creates successfully")
    @Test
    public void makingReservationTest() {

        // For future reference - getting result and closing stream
        //
        // BookingReference result =
        //     webTestClient
        //         .post()
        //         .uri("http://localhost:" + port + "/api/reservation")
        //         .body(Mono.just(payload), ReservationPayload.class)
        //         .exchange()
        //         .expectStatus().isOk()
        //         .expectBody(BookingReference.class)
        //         .returnResult()
        //         .getResponseBody();

        webTestClient
                .post()
                .uri("http://localhost:" + port + "/api/reservation")
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
                        .uri("http://localhost:" + port + "/api/reservation")
                        .body(Mono.just(payload), ReservationPayload.class)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                            .jsonPath("$.bookingReference").exists()
                            .jsonPath("$.bookingReference").value(hasLength(36));

        IntStream.range(0, spots).forEach(i -> successFunc.run());

        webTestClient
            .post()
            .uri("http://localhost:" + port + "/api/reservation")
            .body(Mono.just(payload), ReservationPayload.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
                .isEqualTo(siteAtFullCapacityMessage);
    }
}