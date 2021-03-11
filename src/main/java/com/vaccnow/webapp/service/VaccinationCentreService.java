package com.vaccnow.webapp.service;

import com.vaccnow.webapp.dto.PageResponse;
import com.vaccnow.webapp.dto.VaccinationCentreDTO;
import com.vaccnow.webapp.dto.VaccineDTO;

public interface VaccinationCentreService {

	PageResponse<VaccinationCentreDTO> getAllVaccinationCentres(int pageNumber, int pageSize);

	PageResponse<VaccineDTO> getAllVaccinesForCentre(int id, int pageNumber, int pageSize);
}
