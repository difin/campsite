package campsite.reservation.controller;

import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.ActionResult;
import campsite.reservation.model.out.BookingReference;
import campsite.reservation.service.ReservationFacade;
import campsite.reservation.validation.MethodParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Controller
public class ReservationController {

	private final ReservationFacade reservationFacade;
	private final MethodParamValidator methodParamValidator;

	@Autowired
	public ReservationController(ReservationFacade reservationFacade,
                                 MethodParamValidator methodParamValidator) {
		this.reservationFacade = reservationFacade;
		this.methodParamValidator = methodParamValidator;
	}

	@PostMapping(
			path = "reservation",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public @ResponseBody Mono<BookingReference> reserve(@Valid @RequestBody ReservationPayload payload) {

		return reservationFacade.reserve(payload);
	}

	@DeleteMapping(
			path = "reservation/{bookingReference}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public @ResponseBody Mono<ActionResult> cancelReservation(@PathVariable BookingReferencePayload bookingReference) {

		return reservationFacade.cancelReservation(
				methodParamValidator.validateBookingReference(bookingReference));
	}

	@PutMapping(
			path = "reservation/{bookingReference}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public @ResponseBody Mono<ActionResult> updateReservation(@PathVariable BookingReferencePayload bookingReference,
															  @Valid @RequestBody ReservationPayload payload) {

		return reservationFacade.updateReservation(
				methodParamValidator.validateBookingReference(bookingReference), payload);
	}
}
