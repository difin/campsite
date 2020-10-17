package org.difin.volcanic_getaways.reservation.controller;

import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.response.BookingReferenceModel;
import org.difin.volcanic_getaways.reservation.model.response.ReservationModel;
import org.difin.volcanic_getaways.reservation.service.ReservationFacade;
import org.difin.volcanic_getaways.reservation.validation.MethodParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping(path = "reservations")
public class ReservationController {

	private ReservationFacade reservationFacade;
	private MethodParamValidator methodParamValidator;

	@Autowired
	public ReservationController(ReservationFacade reservationFacade,
                                 MethodParamValidator methodParamValidator) {
		this.reservationFacade = reservationFacade;
		this.methodParamValidator = methodParamValidator;
	}

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<BookingReferenceModel> makeReservation(@Valid @RequestBody ReservationPayload payload) {

		return reservationFacade.makeReservation(payload);
	}

	@DeleteMapping(
			path = "/{bookingReference}"
	)
	public Mono<Void> cancelReservation(@PathVariable BookingReferencePayload bookingReference) {

		return reservationFacade.cancelReservation(
				methodParamValidator.validateBookingReference(bookingReference));
	}

	@PutMapping(
			path = "/{bookingReference}",
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<Void> updateReservation(@PathVariable BookingReferencePayload bookingReference,
										@Valid @RequestBody ReservationPayload payload) {

		return reservationFacade.updateReservation(
				methodParamValidator.validateBookingReference(bookingReference), payload);
	}

	@GetMapping(
			params = {"arrival" , "departure"},
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<ReservationModel> getReservations(@RequestParam @DateTimeFormat(pattern = "uuuu-MMMM-dd")
												  LocalDate arrival,
												  @RequestParam @DateTimeFormat(pattern = "uuuu-MMMM-dd")
												  LocalDate departure) {
		return reservationFacade
				.getReservations(
						Optional.of(
							methodParamValidator.validateRequestDates(
									new RequestDates(arrival, departure))));
	}

	@GetMapping(
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<ReservationModel> getReservations() {

		return reservationFacade
				.getReservations(
						Optional.empty());
	}
}
