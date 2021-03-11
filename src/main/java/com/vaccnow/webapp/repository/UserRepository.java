package com.vaccnow.webapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vaccnow.webapp.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
