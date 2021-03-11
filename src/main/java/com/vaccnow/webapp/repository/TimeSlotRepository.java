package com.vaccnow.webapp.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vaccnow.webapp.model.TimeSlot;

@Repository
public interface TimeSlotRepository extends CrudRepository<TimeSlot, Long> {

	@Query(value = "select startTime from TIME_SLOTS ts where ts.centre_id = ?1 and ts.date = ?2", nativeQuery = true)
	List<LocalTime> findAllTimeSlotsStartTimeByCentreIdAndDate(Integer centreId, LocalDate date);
	
	TimeSlot findTimeSlotByCentreIdAndDateAndStartTime(Integer centreId, LocalDate date, LocalTime time);
	
	@Query(value = "select * from TIME_SLOTS ts where ts.centre_id = ?1 and ts.date = ?2 and ts.capacity = ?3", nativeQuery = true)
	List<TimeSlot> findTimeSlotsAtMaxCapacity(Integer centreId, LocalDate date, long capacity);

}