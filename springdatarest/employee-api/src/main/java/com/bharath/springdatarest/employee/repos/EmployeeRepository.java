package com.bharath.springdatarest.employee.repos;

import org.springframework.data.repository.CrudRepository;

import com.bharath.springdatarest.employee.entities.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}
