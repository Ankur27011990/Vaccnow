package com.vaccnow.webapp.service;

import java.time.LocalDate;

import com.vaccnow.webapp.dto.CompletedVaccinationDTO;
import com.vaccnow.webapp.dto.PageResponse;
import com.vaccnow.webapp.dto.ScheduledVaccinationDTO;

public interface ReportingService {

	public PageResponse<ScheduledVaccinationDTO> getAppointmentsForCentre(int centreId, int pageNumber, int pageSize);

	PageResponse<ScheduledVaccinationDTO> getAppointmentsForPeriod(LocalDate startDate, LocalDate endDate,
			int pageNumber, int pageSize);

	PageResponse<CompletedVaccinationDTO> getCompletedVaccinationsForPeriod(LocalDate startDate, LocalDate endDate,
			int pageNumber, int pageSize);

}
