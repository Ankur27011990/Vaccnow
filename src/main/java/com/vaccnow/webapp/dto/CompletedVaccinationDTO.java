package com.vaccnow.webapp.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompletedVaccinationDTO extends ScheduledVaccinationDTO {

	private String vaccine;
}