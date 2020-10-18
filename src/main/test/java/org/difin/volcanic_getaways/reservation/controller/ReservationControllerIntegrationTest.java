package org.difin.volcanic_getaways.reservation.controller;

import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.service.reservation.CancellationService;
import org.difin.volcanic_getaways.reservation.utils.TestUtils;
import org.junit.jupiter.api.*;
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

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasLength;

@TestPropertySource(
        properties = {
                "spring.datasource.url=jdbc:h2:file:~/volcanic_getaways/h2_test",
                "spring.liquibase.url=jdbc:h2:file:~/volcanic_getaways/h2_test",
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

    private TestUtils testUtils = new TestUtils();
    private final ReservationPayload payload = testUtils.generateReservationPayload(1, 3);

    @BeforeEach
    public void beforeEach() {
        cancellationService.deleteAllReservations();
    }

    @DisplayName("When creating a new reservation and there are available dates then reservations are creates successfully")
    @Test
    public void makingReservationTest() {

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
            .expectStatus().value(equalTo(HttpStatus.CONFLICT.value()))
            .expectBody()
                .jsonPath("$.errors").exists();
    }
}