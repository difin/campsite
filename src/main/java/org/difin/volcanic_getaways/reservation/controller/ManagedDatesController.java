package org.difin.volcanic_getaways.reservation.controller;

import org.difin.volcanic_getaways.reservation.model.response.AvailableDateModel;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.service.ManagedDatesFacade;
import org.difin.volcanic_getaways.reservation.validation.MethodParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping(path = "available-dates")
public class ManagedDatesController {

	private ManagedDatesFacade managedDatesFacade;
	private MethodParamValidator methodParamValidator;

	@Autowired
	public ManagedDatesController(ManagedDatesFacade managedDatesFacade,
								  MethodParamValidator methodParamValidator) {

		this.managedDatesFacade = managedDatesFacade;
		this.methodParamValidator = methodParamValidator;
	}

	@GetMapping(
			params = {"arrival" , "departure"},
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<AvailableDateModel> getAvailableDates(@RequestParam @DateTimeFormat(pattern = "uuuu-MMMM-dd")
													  LocalDate arrival,
													  @RequestParam @DateTimeFormat(pattern = "uuuu-MMMM-dd")
													  LocalDate departure) {

		return managedDatesFacade
			.getAvailableDates(
					Optional.of(
							methodParamValidator.validateRequestDates(
									new RequestDates(arrival, departure))));
	}

	@GetMapping(
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<AvailableDateModel> getAvailableDatesDefaultRange() {

		return managedDatesFacade.getAvailableDates(Optional.empty());
	}
}
