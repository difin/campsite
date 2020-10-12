package campsite.reservation.controller;

import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.model.in.BookingDates;
import campsite.reservation.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Controller
public class AvailabilityController {

	private final AvailabilityService availabilityService;

	@Autowired
	public AvailabilityController(AvailabilityService availabilityService) {
		this.availabilityService = availabilityService;
	}

	@GetMapping(
			path = "available-dates",
			params = {"arrival" , "departure"},
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<AvailableDateModel> getAvailableDates(
			@RequestParam(name="arrival") @DateTimeFormat(pattern = "yyyy-MMM-dd") LocalDate arrival,
			@RequestParam(name="departure") @DateTimeFormat(pattern = "yyyy-MMM-dd") LocalDate departure) {

		return availabilityService
				.getAvailableDates(new BookingDates(arrival, departure));
	}

	@GetMapping(
			path = "available-dates",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<AvailableDateModel> getAvailableDatesDefaultRange() {

		return availabilityService.getAvailableDates();
	}
}
