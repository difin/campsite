package org.difin.volcanic_getaways.reservation.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

	@ApiOperation(value = "Getting dates available for reservation")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<AvailableDateModel> getAvailableDates(
			  @RequestParam(required = false) @DateTimeFormat(pattern = "uuuu-MMMM-dd")
			  @ApiParam(name="arrival", value = "starting date; if not provided, tomorrow's date will be used", defaultValue="2020-November-07", required=false)
			  LocalDate arrival,
			  @RequestParam(required = false) @DateTimeFormat(pattern = "uuuu-MMMM-dd")
			  @ApiParam(name="departure", value = "ending date, if not provided, 1 month from now + 1 day's (end date is exclusive) date will be used", defaultValue="2020-November-10", required=false)
			  LocalDate departure) {

		return managedDatesFacade
			.getAvailableDatesReactive(
					methodParamValidator.validateRequestDates(
							new RequestDates(
									arrival == null ? LocalDate.now().plusDays(1) : arrival,
									departure == null ? LocalDate.now().plusMonths(1).plusDays(1) : departure
							)), false);
	}
}
