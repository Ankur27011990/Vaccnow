package com.vaccnow.webapp.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.vaccnow.webapp.dto.CompletedVaccinationDTO;
import com.vaccnow.webapp.dto.PageResponse;
import com.vaccnow.webapp.dto.ScheduledVaccinationDTO;
import com.vaccnow.webapp.model.Appointment;
import com.vaccnow.webapp.repository.AppointmentRepository;
import com.vaccnow.webapp.service.ReportingService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportingServiceImpl implements ReportingService {

	@Autowired
	private AppointmentRepository repository;

	@Override
	public PageResponse<ScheduledVaccinationDTO> getAppointmentsForCentre(int centreId, int pageNumber, int pageSize) {
		log.debug("Retrieving all the appointments for Centre {}", centreId);
		Page<Appointment> appointmentsPage = repository.getAppointmentsForCentre(centreId,
				PageRequest.of(pageNumber, pageSize));

		List<ScheduledVaccinationDTO> appointments = appointmentsPage.getContent().stream()
				.map(app -> mapScheduledAppointment(app))
				.collect(Collectors.toList());

		return new PageResponse<ScheduledVaccinationDTO>(appointments, pageNumber, pageSize,
				appointmentsPage.getTotalPages(), appointmentsPage.getTotalElements());
	}

	@Override
	public PageResponse<ScheduledVaccinationDTO> getAppointmentsForPeriod(LocalDate startDate, LocalDate endDate,
			int pageNumber, int pageSize) {
		log.debug("Retrieving all the appointments for period with start date: {} and end date: {}", startDate, endDate);
		Page<Appointment> appointmentsPage = repository.getAppointmentsForPeriod(startDate, endDate,
				PageRequest.of(pageNumber, pageSize));

		List<ScheduledVaccinationDTO> appointments = appointmentsPage.getContent().stream()
				.map(app -> mapScheduledAppointment(app))
				.collect(Collectors.toList());

		return new PageResponse<ScheduledVaccinationDTO>(appointments, pageNumber, pageSize,
				appointmentsPage.getTotalPages(), appointmentsPage.getTotalElements());
	}

	@Override
	public PageResponse<CompletedVaccinationDTO> getCompletedVaccinationsForPeriod(LocalDate startDate,
			LocalDate endDate, int pageNumber, int pageSize) {
		log.debug("Retrieving all the completed appointments for period with start date: {} and end date: {}", startDate, endDate);
		Page<Appointment> appointmentsPage = repository.getCompletedVaccinationsForPeriod(startDate, endDate,
				PageRequest.of(pageNumber, pageSize));

		List<CompletedVaccinationDTO> appointments = appointmentsPage.getContent().stream()
				.map(app -> mapCompletedAppointment(app))
				.collect(Collectors.toList());

		return new PageResponse<CompletedVaccinationDTO>(appointments, pageNumber, pageSize,
				appointmentsPage.getTotalPages(), appointmentsPage.getTotalElements());
	}

	private ScheduledVaccinationDTO mapScheduledAppointment(Appointment appointment) {
		ScheduledVaccinationDTO response = new ScheduledVaccinationDTO();
		response.setDate(appointment.getSlot().getDate());
		response.setTime(appointment.getSlot().getStartTime());
		response.setUserName(appointment.getUser().getName());
		response.setVaccinationCentre(appointment.getSlot().getCentre().getName());
		return response;
	}
	
	private CompletedVaccinationDTO mapCompletedAppointment(Appointment appointment) {
		CompletedVaccinationDTO response = new CompletedVaccinationDTO();
		response.setDate(appointment.getSlot().getDate());
		response.setTime(appointment.getSlot().getStartTime());
		response.setUserName(appointment.getUser().getName());
		response.setVaccinationCentre(appointment.getSlot().getCentre().getName());
		response.setVaccine(appointment.getVaccine().getName());
		return response;
	}

}