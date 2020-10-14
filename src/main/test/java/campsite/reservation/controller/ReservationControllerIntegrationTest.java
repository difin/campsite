package campsite.reservation.controller;

import campsite.reservation.model.in.BookingDates;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.BookingReference;
import campsite.reservation.service.reservation.CancellationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private final Supplier<BookingReference>
            successFunc =
                () -> webTestClient
                    .post()
                    .uri("http://localhost:" + port + "/api/reservation")
                    .body(BodyInserters.fromObject(payload))
                    .exchange()
                    .expectStatus().isOk()
                    .returnResult(BookingReference.class)
                    .getResponseBody()
                    .blockFirst();

    @DisplayName("When creating a new reservation and there are available dates then reservations are creates successfully")
    @Test
    public void makingReservationTest() {

        BookingReference result = successFunc.get();

        assertEquals(36, result.getBookingReference().length());
    }

    @DisplayName("A date can be booked only the specified maximum number of times")
    @Test
    public void reservingSameDatesTest() {

        IntStream.range(0, spots).forEach(i -> {
            BookingReference result = successFunc.get();
            assertEquals(36, result.getBookingReference().length());
        });

        String errorMessage =
            webTestClient
                .post()
                .uri("http://localhost:" + port + "/api/reservation")
                .body(BodyInserters.fromObject(payload))
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("{\"message\":\"Reservation couldn't proceed because campsite is at full capacity on one or more days, please choose other dates\"}",
                errorMessage);
    }
}