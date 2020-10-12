package campsite.reservation.controller;

import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.model.in.BookingDates;
import campsite.reservation.service.AvailabilityService;
import campsite.reservation.validation.BookingDatesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Controller
public class AvailabilityController {

	private final AvailabilityService availabilityService;
	private final BookingDatesValidator bookingDatesValidator;

	@Autowired
	public AvailabilityController(AvailabilityService availabilityService,
								  BookingDatesValidator bookingDatesValidator) {
		this.availabilityService = availabilityService;
		this.bookingDatesValidator = bookingDatesValidator;
	}

	@GetMapping(
			path = "available-dates",
			params = {"arrival" , "departure"},
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<AvailableDateModel> getAvailableDates(
			@RequestParam(name="arrival") String arrival,
			@RequestParam(name="departure") String departure) {

		return availabilityService
				.getAvailableDates(
						bookingDatesValidator.validateDates(new BookingDates(arrival, departure)));
	}

	@GetMapping(
			path = "available-dates",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<AvailableDateModel> getAvailableDatesDefaultRange() {

		return availabilityService.getAvailableDates();
	}
}
