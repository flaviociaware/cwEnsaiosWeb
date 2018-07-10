package com.bharath.eventmanagement.repos;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.bharath.eventmanagement.entities.Organizer;

public interface OrganizerRepository extends PagingAndSortingRepository<Organizer, Long> {

}
