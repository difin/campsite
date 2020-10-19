package org.difin.volcanic_getaways.reservation.controller;

import io.swagger.annotations.ApiParam;
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

		return reservationFacade.makeReservationReactive(payload);
	}

	@DeleteMapping(
			path = "/{bookingReference}"
	)
	public Mono<Void> cancelReservation(
				@PathVariable
				@ApiParam(name="bookingReference", value="36 characters booking reference", defaultValue="1fdc99b7-e74a-4d99-bf8b-a49f45f3e367", required=true)
				BookingReferencePayload bookingReference) {

		return reservationFacade.cancelReservationReactive(
				methodParamValidator.validateBookingReference(bookingReference));
	}

	@PutMapping(
			path = "/{bookingReference}",
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<Void> updateReservation(
				@ApiParam(name="bookingReference", value="36 characters booking reference", defaultValue="1fdc99b7-e74a-4d99-bf8b-a49f45f3e367", required=true)
				@PathVariable BookingReferencePayload bookingReference,
				@Valid @RequestBody ReservationPayload payload) {

		return reservationFacade.updateReservationReactive(
				methodParamValidator.validateBookingReference(bookingReference), payload);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<ReservationModel> getReservations(
				@RequestParam(required = false) @DateTimeFormat(pattern = "uuuu-MMMM-dd")
				@ApiParam(name="arrival", value = "starting date; if not provided, tomorrow's date will be used", defaultValue="2020-November-07", required=false)
				LocalDate arrival,
				@RequestParam(required = false) @DateTimeFormat(pattern = "uuuu-MMMM-dd")
				@ApiParam(name="departure", value = "ending date, if not provided, 1 month from now + 1 day's (end date is exclusive) date will be used", defaultValue="2020-November-10", required=false)
				LocalDate departure) {

		return reservationFacade
				.getReservationsReactive(
						methodParamValidator.validateRequestDates(
								new RequestDates(
										arrival == null ? LocalDate.now().plusDays(1) : arrival,
										departure == null ? LocalDate.now().plusMonths(1).plusDays(1) : departure
								)));
	}
}
