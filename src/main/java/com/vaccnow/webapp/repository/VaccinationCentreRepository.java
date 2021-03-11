package com.vaccnow.webapp.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.vaccnow.webapp.model.VaccinationCentre;

@Repository
public interface VaccinationCentreRepository extends PagingAndSortingRepository<VaccinationCentre, Integer> {

}