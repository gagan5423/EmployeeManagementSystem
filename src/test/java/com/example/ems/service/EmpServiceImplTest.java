package com.example.ems.service;

import com.example.ems.entity.Employee;
import com.example.ems.repository.EmpRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmpServiceImplTest {

    @Mock
    private EmpRepository empRepository; 

    @InjectMocks
    private EmpServiceImpl empService;

    private Employee emp1, emp2, emp3;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all EmpServiceImpl tests...");
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        injectPrivate(empService, "empRepo", empRepository);
        injectPrivate(empService, "empRepository", empRepository);

        emp1 = buildEmp(1, "John Doe", "john@x.com", "Addr1", "Male", "p", "EMP",
                50000.0, 30, 5, 1000.0, "IT", "Developer");
        emp2 = buildEmp(2, "Alice Smith", "alice@x.com", "Addr2", "Female", "p", "EMP",
                60000.0, 28, 3, 1200.0, "HR", "Manager");
        emp3 = buildEmp(3, "Bob Johnson", "bob@x.com", "Addr3", "Male", "p", "EMP",
                70000.0, 35, 8, 1500.0, "Finance", "Analyst");
    }

    @AfterEach
    void tearDown() {
        System.out.println("After each test...");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After all EmpServiceImpl tests...");
    }

    @Test
    void saveEmp() {
        when(empRepository.save(emp1)).thenReturn(emp1);

        Employee saved = empService.saveEmp(emp1);

        assertNotNull(saved);
        assertEquals("John Doe", saved.getName());
        verify(empRepository, times(1)).save(emp1);
    }

    @Test
    void getAllEmp() {
        when(empRepository.findAll()).thenReturn(Arrays.asList(emp1, emp2, emp3));

        List<Employee> list = empService.getAllEmp();

        assertEquals(3, list.size());
        assertTrue(list.contains(emp2));
        verify(empRepository, times(1)).findAll();
    }

    @Test
    void getEmpById_found() {
        when(empRepository.findById(1)).thenReturn(Optional.of(emp1));

        Employee found = empService.getEmpById(1);

        assertNotNull(found);
        assertEquals("John Doe", found.getName());
        verify(empRepository, times(1)).findById(1);
    }

    @Test
    void deleteEmp_success() {
        when(empRepository.findById(1)).thenReturn(Optional.of(emp1));

        boolean ok = empService.deleteEmp(1);

        assertTrue(ok);
        verify(empRepository, times(1)).delete(emp1);
    }

    @Test
    void deleteEmp_notFound_throws() {
        when(empRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> empService.deleteEmp(99));
    }

    @Test
    void generateReport() {
        when(empRepository.findAll()).thenReturn(Arrays.asList(emp1, emp2, emp3));

        Map<String, Object> r = empService.generateReport();

        assertEquals(3L, r.get("totalEmployees"));
        assertEquals(60000.0, (double) r.get("avgSalary"), 0.0001);
        assertEquals(70000.0, (double) r.get("maxSalary"), 0.0001);
        assertEquals(50000.0, (double) r.get("minSalary"), 0.0001);
        assertEquals(2L, r.get("maleCount"));
        assertEquals(1L, r.get("femaleCount"));
        verify(empRepository, times(1)).findAll();
    }

    @Test
    void searchByDepartment() {
        when(empRepository.findByDepartmentContainingIgnoreCase("IT"))
                .thenReturn(List.of(emp1));

        List<Employee> out = empService.searchByDepartment("IT");

        assertEquals(1, out.size());
        assertEquals("John Doe", out.get(0).getName());
        verify(empRepository, times(1)).findByDepartmentContainingIgnoreCase("IT");
    }

    @Test
    void searchByJobTitle() {
        when(empRepository.findByJobTitleContainingIgnoreCase("Manager"))
                .thenReturn(List.of(emp2));

        List<Employee> out = empService.searchByJobTitle("Manager");

        assertEquals(1, out.size());
        assertEquals("Alice Smith", out.get(0).getName());
        verify(empRepository, times(1)).findByJobTitleContainingIgnoreCase("Manager");
    }

    private static void injectPrivate(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    private static Employee buildEmp(int id, String name, String email, String address, String gender,
                                     String password, String role, double salary, int age, int exp,
                                     double bonus, String dept, String job) {
        Employee e = new Employee();
        e.setId(id);
        e.setName(name);
        e.setEmail(email);
        e.setAddress(address);
        e.setGender(gender);
        e.setPassword(password);
        e.setRole(role);
        e.setSalary(salary);
        e.setAge(age);
        e.setExperience(exp);
        e.setBonus(bonus);
        e.setDepartment(dept);
        e.setJobTitle(job);
        return e;
    }
}
