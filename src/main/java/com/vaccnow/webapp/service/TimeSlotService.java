package com.vaccnow.webapp.service;

import java.time.LocalDate;
import java.time.LocalTime;

import com.vaccnow.webapp.dto.AvailabilityDetails;
import com.vaccnow.webapp.dto.AvailableTimeSlots;
import com.vaccnow.webapp.dto.ScheduleAppointmentRequest;

public interface TimeSlotService {

	void scheduleAppointment(ScheduleAppointmentRequest appointmentRequest);

	AvailabilityDetails isAvailable(int centreId, LocalDate date, LocalTime time);

	AvailableTimeSlots getAvailableTimeSlots(int centreId, LocalDate date);
}