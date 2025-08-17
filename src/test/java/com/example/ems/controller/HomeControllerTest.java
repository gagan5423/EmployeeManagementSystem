package com.example.ems.controller;

import com.example.ems.entity.Employee;
import com.example.ems.repository.EmpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)

class HomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpRepository empRepository;

    private Employee emp;

    @BeforeEach
    void setup() {
        empRepository.deleteAll(); // Clean DB before each test
        emp = new Employee();
        emp.setName("Jane Doe");
        emp.setDepartment("HR");
        emp.setJobTitle("Manager");
        emp.setSalary(70000);
        emp.setAge(35);
        emp.setExperience(10);
        emp.setGender("Female");
        empRepository.save(emp);
    }

    @Test
    void testIndexPageLoads() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("empList"));
    }

    @Test
    void testAddEmployee() throws Exception {
        mockMvc.perform(post("/saveEmp")
                        .param("name", "Mark Smith")
                        .param("department", "Finance")
                        .param("jobTitle", "Analyst")
                        .param("salary", "50000")
                        .param("age", "28")
                        .param("experience", "3")
                        .param("gender", "Male"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/loadEmpSave"));
    }

    @Test
    void testEditEmployee() throws Exception {
        mockMvc.perform(get("/EditEmp/" + emp.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_emp"))
                .andExpect(model().attributeExists("emp"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        mockMvc.perform(get("/deleteEmp/" + emp.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testSearchByDepartment() throws Exception {
        mockMvc.perform(get("/search/department").param("department", "HR"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("empList"));
    }
}
