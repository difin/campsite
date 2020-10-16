package org.difin.volcanic_getaways.reservation.controller;

import org.difin.volcanic_getaways.reservation.model.out.AvailableDateModel;
import org.difin.volcanic_getaways.reservation.model.in.RequestDates;
import org.difin.volcanic_getaways.reservation.service.ManagedDatesFacade;
import org.difin.volcanic_getaways.reservation.validation.MethodParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Controller
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
			path = "available-dates",
			params = {"arrival" , "departure"},
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<AvailableDateModel> getAvailableDates(
			@RequestParam(name="arrival") String arrival,
			@RequestParam(name="departure") String departure) {

		return managedDatesFacade
			.getAvailableDates(
					Optional.of(
							methodParamValidator.validateRequestDates(
									new RequestDates(arrival, departure))));
	}

	@GetMapping(
			path = "available-dates",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<AvailableDateModel> getAvailableDatesDefaultRange() {

		return managedDatesFacade.getAvailableDates(Optional.empty());
	}
}
