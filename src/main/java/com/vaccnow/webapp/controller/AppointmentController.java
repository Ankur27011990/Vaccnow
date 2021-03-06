package com.vaccnow.webapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaccnow.webapp.dto.ScheduleAppointmentRequest;
import com.vaccnow.webapp.service.TimeSlotService;

@RestController
@RequestMapping(path = "/v1/appointments")
@Validated
public class AppointmentController {

	@Autowired
	private TimeSlotService appointmentService;

	@SuppressWarnings("rawtypes")
	@PostMapping
	public ResponseEntity scheduleAppointment(@Valid @RequestBody ScheduleAppointmentRequest appointmentRequest) {
		appointmentService.scheduleAppointment(appointmentRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}