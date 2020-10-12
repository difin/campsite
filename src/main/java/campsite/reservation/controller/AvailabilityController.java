package campsite.reservation.controller;

import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.model.in.RequestedDatesRange;
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
			params = {"startDate" , "endDate"},
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<AvailableDateModel> getAvailableDates(
			@RequestParam(name="startDate") @DateTimeFormat(pattern = "yyyy-MMM-dd")
					LocalDate startDate,
			@RequestParam(name="endDate") @DateTimeFormat(pattern = "yyyy-MMM-dd")
					LocalDate endDate) {

		return availabilityService
				.getAvailableDates(new RequestedDatesRange(startDate, endDate));
	}

	@GetMapping(
			path = "available-dates",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<AvailableDateModel> getAvailableDatesDefaultRange() {

		return availabilityService.getAvailableDates();
	}
}
