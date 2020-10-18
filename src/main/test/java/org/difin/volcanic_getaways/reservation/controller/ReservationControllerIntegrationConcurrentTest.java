package org.difin.volcanic_getaways.reservation.controller;

import org.difin.volcanic_getaways.reservation.data.entity.Reservation;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.response.BookingReferenceModel;
import org.difin.volcanic_getaways.reservation.service.reservation.CancellationService;
import org.difin.volcanic_getaways.reservation.service.reservation.ReservationService;
import org.difin.volcanic_getaways.reservation.utils.TestUtils;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestPropertySource(
        properties = {
                "spring.datasource.url=jdbc:h2:file:~/volcanic_getaways/h2_test",
                "spring.liquibase.url=jdbc:h2:file:~/volcanic_getaways/h2_test",
                "app.spots-num=10"
        }
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "10000")
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

    private final Random random = new Random();
    private final TestUtils testUtils = new TestUtils();

    @BeforeEach
    public void beforeEach() {
        cancellationService.deleteAllReservations();
    }

    @DisplayName("When trying to reserve the same range of dates concurrently then only #spotsOnSite reservations succeeds " +
            "and others fail site with site full capacity message")
    @Test
    public void concurrentReservationTest() {

        Vector<CompletableFuture<Void>> threads = new Vector<>();

        IntStream.range(1, 1000).forEach(i -> {
            CompletableFuture<Void> thread =
                CompletableFuture.runAsync(() -> {
                webTestClient
                    .post()
                    .uri("http://localhost:" + port + "/api/reservations")
                    .body(Mono.just(testUtils.generateReservationPayload(3, 3)), ReservationPayload.class)
                    .exchange()
                    .expectStatus().value(oneOf(HttpStatus.OK.value(), HttpStatus.CONFLICT.value()))
                    .expectBody(String.class)
                    .consumeWith(t -> {
                        Map<String, String> map = testUtils.jsonToMap(t.getResponseBody());
                        assertTrue(((map.containsKey("bookingReference") && map.get("bookingReference").length() == 36) ||
                                   (map.containsKey("errors"))));
                        })
                    .returnResult();
                } );

            threads.add(thread);
        });

        threads.forEach(CompletableFuture::join);

        List<Reservation> reservations = reservationService.getReservationsBlocking(Optional.empty());

        assertEquals(spotsOnSite, reservations.size());
    }

    @DisplayName("When trying to update the same reservation concurrently then only expected statuses are returned and " +
            "at the end the reservation exists only in 1 variant")
    @Test
    public void concurrentUpdateTest() {

        ReservationPayload reservationPayload = testUtils.generateReservationPayload(1, 3);

         BookingReferenceModel bookingRef =
             webTestClient
                 .post()
                 .uri("http://localhost:" + port + "/api/reservations")
                 .body(Mono.just(reservationPayload), ReservationPayload.class)
                 .exchange()
                 .expectStatus().isOk()
                 .expectBody(BookingReferenceModel.class)
                 .returnResult()
                 .getResponseBody();

        Vector<CompletableFuture<Void>> threads = new Vector<>();

        IntStream.range(1, 1000).forEach(i -> {
            CompletableFuture<Void> thread =
                CompletableFuture.runAsync(() -> {
                    webTestClient
                        .put()
                        .uri("http://localhost:" + port + "/api/reservations/" + bookingRef.getBookingReference())
                        .body(Mono.just(testUtils.generateReservationPayload(20, 3)), ReservationPayload.class)
                        .exchange()
                        .expectStatus().value(oneOf(
                            HttpStatus.OK.value(),
                            HttpStatus.CONFLICT.value(),
                            HttpStatus.NOT_FOUND.value()))
                        .expectBody()
                        .returnResult();
                } );

            threads.add(thread);
        });

        threads.forEach(CompletableFuture::join);

        assertEquals(reservationService.getReservationsBlocking(Optional.empty())
                .stream()
                .filter(r -> r.getBookingRef().equals(bookingRef.getBookingReference()))
                .count(), 1);
    }

    @DisplayName("When performing massive concurrent updates then no indefinite deadlock or other unexpect errors occur")
    @Test
    public void concurrentMultipleUpdatesTest() {

        Vector<CompletableFuture<Void>> threads = new Vector<>();

        // Creating up to 100 random reservations to semi-fill the database
        // 10 spots per date, if stay length is 3 then max 100 reservations, but here spots num is random and some will conflict
        IntStream.range(1, 200).forEach(i -> {
            CompletableFuture<Void> thread =
                CompletableFuture.runAsync(() -> {
                    webTestClient
                        .post()
                        .uri("http://localhost:" + port + "/api/reservations")
                        .body(Mono.just(testUtils.generateReservationPayload(27)), ReservationPayload.class)
                        .exchange()
                        .expectStatus().value(oneOf(HttpStatus.OK.value(), HttpStatus.CONFLICT.value()))
                        .expectBody(String.class)
                        .consumeWith(t -> {
                            Map<String, String> map = testUtils.jsonToMap(t.getResponseBody());
                            assertTrue(((map.containsKey("bookingReference") && map.get("bookingReference").length() == 36) ||
                                    (map.containsKey("errors"))));
                        })
                        .returnResult();
                } );

            threads.add(thread);
        });

        threads.forEach(CompletableFuture::join);

        // Getting all booking references
        List<String> bookingRefs =
            reservationService.getReservationsBlocking(Optional.empty())
                .stream()
                .map(r -> r.getBookingRef())
                .collect(Collectors.toList());

        threads.clear();

        // Updating reservations to random date ranges
        // Verifying that there are no exceptions or deadlocks
        IntStream.range(1, 2000).forEach(i -> {
            CompletableFuture<Void> thread =
                CompletableFuture.runAsync(() -> {
                    webTestClient
                        .put()
                        .uri("http://localhost:" + port + "/api/reservations" + bookingRefs.get(random.ints(1,4).findFirst().getAsInt()))
                        .body(Mono.just(testUtils.generateReservationPayload(27)), ReservationPayload.class)
                        .exchange()
                        .expectStatus().value(oneOf(
                            HttpStatus.OK.value(),
                            HttpStatus.CONFLICT.value(),
                            HttpStatus.NOT_FOUND.value()))
                        .expectBody()
                        .returnResult();
                    } );

            threads.add(thread);
        });

        threads.forEach(CompletableFuture::join);

        List<String> bookingRefsAfterUpdates =
                reservationService.getReservationsBlocking(Optional.empty())
                        .stream()
                        .map(r -> r.getBookingRef())
                        .collect(Collectors.toList());

        // Checking that number of reservations remained the same
        assertTrue(bookingRefs.size() == bookingRefsAfterUpdates.size());
    }
}