package com.vaccnow.webapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.vaccnow.webapp.dto.CompletedVaccinationDTO;
import com.vaccnow.webapp.dto.PageResponse;
import com.vaccnow.webapp.dto.ScheduledVaccinationDTO;
import com.vaccnow.webapp.model.Appointment;
import com.vaccnow.webapp.model.TimeSlot;
import com.vaccnow.webapp.model.User;
import com.vaccnow.webapp.model.VaccinationCentre;
import com.vaccnow.webapp.model.Vaccine;
import com.vaccnow.webapp.repository.AppointmentRepository;
import com.vaccnow.webapp.service.impl.ReportingServiceImpl;

@ExtendWith(SpringExtension.class)
public class ReportingServiceTest {

	private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);
	private static final LocalDate DATE = LocalDate.now();
	private static final LocalTime START_TIME = LocalTime.now();
	private static final LocalTime END_TIME = LocalTime.now().plusMinutes(15);

	@Spy
	@InjectMocks
	private ReportingService reportingService = new ReportingServiceImpl();

	@Mock
	private AppointmentRepository repository;

	private Page<Appointment> appointments;

	private Vaccine vaccine;

	@BeforeEach
	public void setUp() {
		vaccine = new Vaccine(1, "A", "B");
		appointments = getAppointments();
	}

	@Test
	public void testGetAppointmentsForCentre() {
		when(repository.getAppointmentsForCentre(eq(1), eq(PAGE_REQUEST))).thenReturn(appointments);

		PageResponse<ScheduledVaccinationDTO> response = reportingService.getAppointmentsForCentre(1, 0, 10);

		ScheduledVaccinationDTO actual = response.getData().get(0);

		Appointment expected = appointments.getContent().get(0);

		assertNotNull(actual);
		assertNotNull(actual.getDate());
		assertNotNull(actual.getTime());
		assertNotNull(actual.getUserName());
		assertNotNull(actual.getVaccinationCentre());
		assertEquals(expected.getSlot().getDate(), actual.getDate());
		assertEquals(expected.getSlot().getStartTime(), actual.getTime());
		assertEquals(expected.getUser().getName(), actual.getUserName());
		assertEquals(expected.getSlot().getCentre().getName(), actual.getVaccinationCentre());

		verify(repository, Mockito.times(1)).getAppointmentsForCentre(eq(1), eq(PAGE_REQUEST));

	}

	@Test
	public void testGetAppointmentsForPeriod() {
		when(repository.getAppointmentsForPeriod(eq(DATE), eq(DATE), eq(PAGE_REQUEST))).thenReturn(appointments);

		PageResponse<ScheduledVaccinationDTO> response = reportingService.getAppointmentsForPeriod(DATE, DATE, 0, 10);

		ScheduledVaccinationDTO actual = response.getData().get(0);

		Appointment expected = appointments.getContent().get(0);

		assertNotNull(actual);
		assertNotNull(actual.getDate());
		assertNotNull(actual.getTime());
		assertNotNull(actual.getUserName());
		assertNotNull(actual.getVaccinationCentre());
		assertEquals(expected.getSlot().getDate(), actual.getDate());
		assertEquals(expected.getSlot().getStartTime(), actual.getTime());
		assertEquals(expected.getUser().getName(), actual.getUserName());
		assertEquals(expected.getSlot().getCentre().getName(), actual.getVaccinationCentre());

		verify(repository, Mockito.times(1)).getAppointmentsForPeriod(eq(DATE), eq(DATE), eq(PAGE_REQUEST));

	}

	@Test
	public void testGetAllCompletedVaccinationsForPeriod() {
		Appointment expected = appointments.getContent().get(0);
		expected.setVaccine(vaccine);

		when(repository.getCompletedVaccinationsForPeriod(eq(DATE), eq(DATE), eq(PAGE_REQUEST)))
				.thenReturn(appointments);

		PageResponse<CompletedVaccinationDTO> response = reportingService.getCompletedVaccinationsForPeriod(DATE, DATE,
				0, 10);

		CompletedVaccinationDTO actual = response.getData().get(0);

		assertNotNull(actual);
		assertNotNull(actual.getDate());
		assertNotNull(actual.getTime());
		assertNotNull(actual.getUserName());
		assertNotNull(actual.getVaccinationCentre());
		assertEquals(expected.getSlot().getDate(), actual.getDate());
		assertEquals(expected.getSlot().getStartTime(), actual.getTime());
		assertEquals(expected.getUser().getName(), actual.getUserName());
		assertEquals(expected.getSlot().getCentre().getName(), actual.getVaccinationCentre());
		assertEquals(expected.getVaccine().getName(), actual.getVaccine());

		verify(repository, Mockito.times(1)).getCompletedVaccinationsForPeriod(eq(DATE), eq(DATE), eq(PAGE_REQUEST));

	}

	private PageImpl<Appointment> getAppointments() {
		VaccinationCentre centre1 = new VaccinationCentre();
		centre1.setName("Test Centre");

		TimeSlot ts1 = new TimeSlot(DATE, START_TIME, END_TIME, centre1, 5);

		User user1 = new User("A", "A@gmail.com");

		List<Appointment> appointmentList = new ArrayList<>();
		appointmentList.add(new Appointment(user1, "CASH", ts1));
		return new PageImpl<Appointment>(appointmentList);
	}
}
