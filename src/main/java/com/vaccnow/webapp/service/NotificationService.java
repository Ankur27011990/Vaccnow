package com.vaccnow.webapp.service;

import java.time.LocalDate;

import com.vaccnow.webapp.model.TimeSlot;
import com.vaccnow.webapp.model.User;
import com.vaccnow.webapp.model.VaccinationCentre;

public interface NotificationService {

	public void sendTimeSlotConfirmation(User user, TimeSlot timeSlot);
	
	public void sendVaccinationCertificate(User user, VaccinationCentre vaccinationCentre, LocalDate date);

}
