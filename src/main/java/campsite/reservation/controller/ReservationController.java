package campsite.reservation.controller;

import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.BookingReference;
import campsite.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Controller
public class ReservationController {

	private final ReservationService reservationService;

	@Autowired
	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@PostMapping(
			path = "reservation",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public @ResponseBody Mono<BookingReference> reserve(@Valid @RequestBody ReservationPayload payload) {

		return reservationService.reserve(payload);
	}
}
