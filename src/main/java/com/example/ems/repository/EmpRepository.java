package com.example.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ems.entity.Employee;

public interface EmpRepository extends JpaRepository<Employee, Integer> {

	 List<Employee> findByDepartmentContainingIgnoreCase(String department);

	 List<Employee> findByJobTitleContainingIgnoreCase(String jobTitle);

	 List<Employee> findByDepartmentAndJobTitle(String department, String jobTitle);
}
