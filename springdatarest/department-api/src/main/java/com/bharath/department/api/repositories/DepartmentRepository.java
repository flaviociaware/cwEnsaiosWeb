package com.bharath.department.api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bharath.department.api.entities.Department;

public interface DepartmentRepository extends CrudRepository<Department, Long> {

}
