package com.example.ems.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import com.example.ems.entity.Employee;
import com.example.ems.repository.EmpRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class EmpServiceImpl implements EmpService {

	@Autowired
	private EmpRepository empRepo;

	@Override
	public Employee saveEmp(Employee emp) {
		Employee newEmp = empRepo.save(emp);
		return newEmp;
	}

	@Override
	public List<Employee> getAllEmp() {

		return empRepo.findAll();
	}

	@Override
	public Employee getEmpById(int id) {
		return empRepo.findById(id).get();
	}

	@Override
	public boolean deleteEmp(int id) {
		Employee emp = empRepo.findById(id).get();
		if (emp != null) {
			empRepo.delete(emp);
			return true;
		}
		return false;
	}
	@Override
	public Map<String, Object> generateReport() {
	    List<Employee> all = empRepo.findAll();

	    long totalEmployees = all.size();
	    
	    double avgSalary = all.stream().mapToDouble(Employee::getSalary).average().orElse(0);
	    double maxSalary = all.stream().mapToDouble(Employee::getSalary).max().orElse(0);
	    double minSalary = all.stream().mapToDouble(Employee::getSalary).min().orElse(0);

	    double avgAge = all.stream().mapToInt(Employee::getAge).average().orElse(0);
	    int maxAge = all.stream().mapToInt(Employee::getAge).max().orElse(0);
	    int minAge = all.stream().mapToInt(Employee::getAge).min().orElse(0);

	    double avgExp = all.stream().mapToInt(Employee::getExperience).average().orElse(0);
	    int maxExp = all.stream().mapToInt(Employee::getExperience).max().orElse(0);
	    int minExp = all.stream().mapToInt(Employee::getExperience).min().orElse(0);

	    long maleCount = all.stream().filter(e -> "Male".equalsIgnoreCase(e.getGender())).count();
	    long femaleCount = all.stream().filter(e -> "Female".equalsIgnoreCase(e.getGender())).count();

	    Map<String, Object> report = new HashMap<>();
	    report.put("totalEmployees", totalEmployees);
	    report.put("avgSalary", avgSalary);
	    report.put("maxSalary", maxSalary);
	    report.put("minSalary", minSalary);
	    report.put("avgAge", avgAge);
	    report.put("maxAge", maxAge);
	    report.put("minAge", minAge);
	    report.put("avgExp", avgExp);
	    report.put("maxExp", maxExp);
	    report.put("minExp", minExp);
	    report.put("maleCount", maleCount);
	    report.put("femaleCount", femaleCount);

	    return report;
	}



	public void removeSessionMessage() {
		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
				.getSession();

		session.removeAttribute("msg");

	}
    @Autowired
    private EmpRepository empRepository;

    @Override
    public List<Employee> searchByDepartment(String department) {
        return empRepository.findByDepartmentContainingIgnoreCase(department);
    }

    @Override
    public List<Employee> searchByJobTitle(String jobTitle) {
        return empRepository.findByJobTitleContainingIgnoreCase(jobTitle);
    }


}
