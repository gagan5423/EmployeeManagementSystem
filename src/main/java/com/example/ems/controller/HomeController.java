package com.example.ems.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ems.entity.Employee;
import com.example.ems.service.EmpService;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HomeController {

    @Autowired
    private EmpService empService;

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");

    @GetMapping("/")
    public String index(Model m) {
        List<Employee> list = empService.getAllEmp();
        m.addAttribute("empList", list);
        return "index";
    }

    @GetMapping("/loadEmpSave")
    public String loadEmpSave() {
        return "emp_save";
    }

    @GetMapping("/EditEmp/{id}")
    public String editEmp(@PathVariable int id, Model m) {
        Employee emp = empService.getEmpById(id);
        m.addAttribute("emp", emp);
        return "edit_emp";
    }

    @PostMapping("/saveEmp")
    public String saveEmp(@ModelAttribute Employee emp, HttpSession session) {
        Employee newEmp = empService.saveEmp(emp);

        if (newEmp != null) {
            session.setAttribute("msg", "Register successfully");
            auditLogger.info("Added Employee: {}", newEmp);
        } else {
            session.setAttribute("msg", "Something went wrong on server");
        }

        return "redirect:/loadEmpSave";
    }

    @PostMapping("/updateEmpDtls")
    public String updateEmp(@ModelAttribute Employee emp, HttpSession session) {
        Employee updateEmp = empService.saveEmp(emp);

        if (updateEmp != null) {
            session.setAttribute("msg", "Update successfully");
            auditLogger.info("Updated Employee: {}", updateEmp);
        } else {
            session.setAttribute("msg", "Something went wrong on server");
        }

        return "redirect:/";
    }

    @GetMapping("/report")
    public String report(Model m) {
        Map<String, Object> stats = empService.generateReport();
        m.addAttribute("report", stats);
        return "report";
    }

    @GetMapping("/search/department")
    public String searchByDepartment(@RequestParam String department, Model model) {
        List<Employee> employees = empService.searchByDepartment(department);
        model.addAttribute("empList", employees);
        return "index";
    }

    @GetMapping("/search/job")
    public String searchByJob(@RequestParam String jobTitle, Model model) {
        List<Employee> employees = empService.searchByJobTitle(jobTitle);
        model.addAttribute("empList", employees);
        return "index";
    }

    @GetMapping("/search/EditEmp/{id}")
    public String searchEditEmp(@PathVariable int id, Model m) {
        Employee emp = empService.getEmpById(id);
        m.addAttribute("emp", emp);
        return "edit_emp";
    }

    @GetMapping("/search/deleteEmp/{id}")
    public String searchDeleteEmp(@PathVariable int id, HttpSession session) {
        boolean f = empService.deleteEmp(id);
        if (f) {
            session.setAttribute("msg", "Delete successfully");
            auditLogger.info("Deleted Employee (via search) with ID {}", id);
        } else {
            session.setAttribute("msg", "Something went wrong on server");
        }
        return "redirect:/";
    }
    @PostMapping("/deleteEmp/{id}")
    public String deleteEmp(@PathVariable int id, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("ADMIN")) {
            session.setAttribute("msg", "You are not authorized to delete employees.");
            return "redirect:/";
        }

        boolean f = empService.deleteEmp(id);
        if (f) {
            session.setAttribute("msg", "Delete successfully");
            auditLogger.info("Deleted Employee with ID {}", id);
        } else {
            session.setAttribute("msg", "Something went wrong on server");
        }
        return "redirect:/";
    }
    @GetMapping("/confirmDelete/{id}")
    public String confirmDelete(@PathVariable int id, Model model, HttpSession session) {
        Employee emp = empService.getEmpById(id);

        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("ADMIN")) {
            session.setAttribute("msg", "You are not authorized to delete employees.");
            return "redirect:/";
        }

        model.addAttribute("emp", emp);
        return "delete"; // loads delete.html
    }

}
