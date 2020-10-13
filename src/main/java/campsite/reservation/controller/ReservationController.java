package campsite.reservation.controller;

import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.ActionResult;
import campsite.reservation.model.out.BookingReference;
import campsite.reservation.service.ReservationService;
import campsite.reservation.validation.MethodParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Controller
public class ReservationController {

	private final ReservationService reservationService;
	private final MethodParamValidator methodParamValidator;

	@Autowired
	public ReservationController(ReservationService reservationService,
								 MethodParamValidator methodParamValidator) {
		this.reservationService = reservationService;
		this.methodParamValidator = methodParamValidator;
	}

	@PostMapping(
			path = "reservation",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public @ResponseBody Mono<BookingReference> reserve(@Valid @RequestBody ReservationPayload payload) {

		return reservationService.reserve(payload);
	}

	@DeleteMapping(
			path = "reservation/{bookingReference}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public @ResponseBody Mono<ActionResult> cancelReservation(@PathVariable BookingReferencePayload bookingReference) {

		return reservationService.cancelReservation(
				methodParamValidator.validateBookingReference(bookingReference));
	}

	@PutMapping(
			path = "reservation/{bookingReference}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public @ResponseBody Mono<ActionResult> updateReservation(@PathVariable BookingReferencePayload bookingReference,
															  @Valid @RequestBody ReservationPayload payload) {

		return reservationService.updateReservation(
				methodParamValidator.validateBookingReference(bookingReference), payload);
	}
}
