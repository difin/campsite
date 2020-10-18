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

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<ReservationModel> getReservations(
				@RequestParam(required = false) @DateTimeFormat(pattern = "uuuu-MMMM-dd")
				@ApiParam(name="arrival", value = "arrival date, default: tomorrow", defaultValue="2020-November-07", required=false)
				LocalDate arrival,
				@RequestParam(required = false) @DateTimeFormat(pattern = "uuuu-MMMM-dd")
				@ApiParam(name="departure", value = "departure date, default: one month from now", defaultValue="2020-November-10", required=false)
				LocalDate departure) {

		return reservationFacade
				.getReservations(
						methodParamValidator.validateRequestDates(
								new RequestDates(
										arrival == null ? LocalDate.now().plusDays(1) : arrival,
										departure == null ? LocalDate.now().plusMonths(1) : departure
								)));
	}
}
