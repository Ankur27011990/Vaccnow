package com.vaccnow.webapp.service.impl;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vaccnow.webapp.dto.AvailabilityDetails;
import com.vaccnow.webapp.dto.AvailableTimeSlots;
import com.vaccnow.webapp.dto.ScheduleAppointmentRequest;
import com.vaccnow.webapp.dto.TimeSlotStatusDTO;
import com.vaccnow.webapp.exceptions.UnavailableTimeSlotException;
import com.vaccnow.webapp.exceptions.UnsupportedPaymentMethodException;
import com.vaccnow.webapp.exceptions.UnsupportedTimeSlotException;
import com.vaccnow.webapp.exceptions.UserNotFoundException;
import com.vaccnow.webapp.exceptions.VaccinationCentreNotFound;
import com.vaccnow.webapp.model.Appointment;
import com.vaccnow.webapp.model.TimeSlot;
import com.vaccnow.webapp.model.User;
import com.vaccnow.webapp.model.VaccinationCentre;
import com.vaccnow.webapp.repository.AppointmentRepository;
import com.vaccnow.webapp.repository.TimeSlotRepository;
import com.vaccnow.webapp.repository.UserRepository;
import com.vaccnow.webapp.repository.VaccinationCentreRepository;
import com.vaccnow.webapp.service.NotificationService;
import com.vaccnow.webapp.service.TimeSlotService;

import lombok.Setter;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

	@Value("#{'${payment-methods}'.split(',')}")
	@Setter
	private List<String> paymentMethods;

	@Value("#{'${time-slots}'.split(',')}")
	@Setter
	private List<Integer> timeSlots;

	@Autowired
	private TimeSlotRepository timeSlotRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VaccinationCentreRepository vaccinationCentreRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	@Setter
	List<LocalTime> allTimeSlots;

	@Override
	public AvailabilityDetails isAvailable(int centreId, LocalDate date, LocalTime time) {
		VaccinationCentre centre = vaccinationCentreRepository.findById(centreId)
				.orElseThrow(() -> new VaccinationCentreNotFound());
		TimeSlot timeSlot = timeSlotRepository.findTimeSlotByCentreIdAndDateAndStartTime(centreId, date, time);

		long maxCapacity = centre.getCapacity();

		if (timeSlot == null || timeSlot.getCapacity() < maxCapacity) {
			return new AvailabilityDetails(TimeSlotStatusDTO.AVAILABLE);
		}

		return new AvailabilityDetails(TimeSlotStatusDTO.UNAVAILABLE);
	}

	@Override
	@Transactional
	public void scheduleAppointment(ScheduleAppointmentRequest appointmentRequest) {

		if (!isValid(appointmentRequest.getPaymentMethod())) {
			throw new UnsupportedPaymentMethodException();
		}

		if (!isValid(appointmentRequest.getMinute())) {
			throw new UnsupportedTimeSlotException();
		}

		int centreId = appointmentRequest.getCentreId();
		LocalDate date = appointmentRequest.getDate();
		LocalTime time = LocalTime.of(appointmentRequest.getHour(), appointmentRequest.getMinute());

		// Check if the slot requested is available!
		TimeSlotStatusDTO availability = isAvailable(centreId, date, time).getStatus();
		if (!TimeSlotStatusDTO.AVAILABLE.equals(availability))
			throw new UnavailableTimeSlotException();

		TimeSlot timeSlot = timeSlotRepository.findTimeSlotByCentreIdAndDateAndStartTime(centreId, date, time);

		Optional<User> userOptional = userRepository.findById(appointmentRequest.getUserId());
		User user = userOptional.orElseThrow(() -> new UserNotFoundException());

		Optional<VaccinationCentre> centreOpt = vaccinationCentreRepository.findById(centreId);
		VaccinationCentre vaccinationCentre = centreOpt.get();

		if (timeSlot == null) {
			timeSlot = new TimeSlot();
			timeSlot.setCentre(vaccinationCentre);
			timeSlot.setDate(date);
			timeSlot.setStartTime(time);
			timeSlot.setEndTime(timeSlot.getStartTime().plusMinutes(15));
			timeSlot = timeSlotRepository.save(timeSlot);
		}

		// If Available, Increase capacity/people present currently of the timeslot
		timeSlot.incrementCapacity();

		// Save the newly made appointment
		appointmentRepository.save(new Appointment(user, appointmentRequest.getPaymentMethod(), timeSlot));

		// Send Notification
		notificationService.sendTimeSlotConfirmation(user, timeSlot);

	}

	@Override
	public AvailableTimeSlots getAvailableTimeSlots(int centreId, LocalDate date) {
		Optional<VaccinationCentre> centreOpt = vaccinationCentreRepository.findById(centreId);
		VaccinationCentre centre = centreOpt.orElseThrow(() -> new VaccinationCentreNotFound());

		// The idea is to get all the slots based on the interval provided throughout
		// the day
		// Then check for timeslot which are at max capacity and remove them from the
		// overall timeSlots.
		allTimeSlots = timeSlotRepository.findAllTimeSlotsStartTimeByCentreIdAndDate(centreId, date);
		
		List<LocalTime> timeSlots = new ArrayList<>();
		timeSlots.addAll(allTimeSlots);

		List<LocalTime> unavailableTimeSlots = timeSlotRepository
				.findTimeSlotsAtMaxCapacity(centreId, date, centre.getCapacity()).stream()
				.map(slot -> slot.getStartTime()).collect(toList());

		timeSlots.removeAll(unavailableTimeSlots);
		return new AvailableTimeSlots(timeSlots);
	}

	private boolean isValid(String paymentMethod) {
		return paymentMethods.contains(paymentMethod);
	}

	private boolean isValid(int minute) {
		return timeSlots.contains(minute);
	}
}
