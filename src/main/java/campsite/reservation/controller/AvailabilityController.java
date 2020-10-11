package campsite.reservation.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.Month;

@Controller
public class AvailabilityController {

	@GetMapping(
			path = "available-dates",
			params = {"startDate" , "endDate"},
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<LocalDate> getAvailableDates(
			@RequestParam(name="startDate") @DateTimeFormat(pattern = "yyyy-MMM-dd")
					LocalDate startDate,
			@RequestParam(name="endDate") @DateTimeFormat(pattern = "yyyy-MMM-dd")
					LocalDate endDate) {

		return Flux.fromArray(new LocalDate[]{
				LocalDate.of(2020, Month.JANUARY, 8),
				LocalDate.of(2020, Month.JANUARY, 9),
				LocalDate.of(2020, Month.JANUARY, 10),
		});
	}

	@GetMapping(
			path = "available-dates",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<LocalDate> getAvailableDates2() {

		return Flux.fromArray(new LocalDate[]{
				LocalDate.of(2020, Month.JANUARY, 8),
				LocalDate.of(2020, Month.JANUARY, 8),
				LocalDate.of(2020, Month.JANUARY, 9),
				LocalDate.of(2020, Month.JANUARY, 9),
				LocalDate.of(2020, Month.JANUARY, 10),
		});
	}
}
