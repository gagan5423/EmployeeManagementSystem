package com.example.ems.service;

import java.util.List;
import java.util.Map;

import com.example.ems.entity.Employee;


public interface EmpService {

	public Employee saveEmp(Employee emp);

	public List<Employee> getAllEmp();

	public Employee getEmpById(int id);

	public boolean deleteEmp(int id);
	
	Map<String, Object> generateReport();
	
    List<Employee> searchByDepartment(String department);
    
    List<Employee> searchByJobTitle(String jobTitle);
    

}
