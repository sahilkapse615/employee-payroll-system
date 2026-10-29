package com.sahil.payroll;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class PayrollController {
   
    private final List<Employee> employees = new ArrayList<>();

    @GetMapping("/employees")
    public List<Map<String, Object>> getEmployees() {
        synchronized (employees) {
            List<Map<String, Object>> result = new ArrayList<>();
            for (Employee employee : employees) {
                result.add(toMap(employee));
            }
            return result;
        }
    }

    @PostMapping("/employees")
    public Map<String, Object> addEmployee(@RequestBody EmployeeRequest request) {
        if (request.id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee ID must be greater than zero.");
        }
        if (request.name == null || request.name.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee name is required.");
        }
        if (request.department == null || request.department.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department is required.");
        }
        if (request.salary < 0 || !Double.isFinite(request.salary)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Salary must be a valid non-negative amount.");
        }

        Address address = new Address(clean(request.city, "Not Set"), clean(request.state, "Not Set"));
        Employee employee;

        try {
            if ("PART_TIME".equalsIgnoreCase(request.type)) {
                if (request.hoursWorked < 0 || request.hourlyRate < 0 ||
                    !Double.isFinite(request.hourlyRate)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hours and hourly rate cannot be negative.");
                }
                employee = new PartTimeEmployee(
                    request.id, request.name, request.department, request.salary,
                    address, request.hoursWorked, request.hourlyRate
                );
            } else {
                double bonus = request.bonus;
                if (bonus < 0 || !Double.isFinite(bonus)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bonus cannot be negative.");
                }
                employee = new FullTimeEmployee(
                    request.id, request.name, request.department, request.salary,
                    address, bonus
                );
            }
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        synchronized (employees) {
            for (Employee existing : employees) {
                if (existing.getEmployeeId() == request.id) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "This employee ID already exists.");
                }
            }
            employees.add(employee);
        }
        return toMap(employee);
    }

    @PutMapping("/employees/{id}")
    public Map<String, Object> updateEmployee(@PathVariable int id, @RequestBody EmployeeRequest request) {
        if (request.name == null || request.name.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee name is required.");
        }
        if (request.department == null || request.department.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department is required.");
        }
        if (request.salary < 0 || !Double.isFinite(request.salary)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Salary must be a valid non-negative amount.");
        }
        Address address = new Address(clean(request.city, "Not Set"), clean(request.state, "Not Set"));
        Employee updated;
        try {
            if ("PART_TIME".equalsIgnoreCase(request.type)) {
                if (request.hoursWorked < 0 || request.hourlyRate < 0 || !Double.isFinite(request.hourlyRate)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hours and hourly rate cannot be negative.");
                }
                updated = new PartTimeEmployee(id, request.name, request.department, request.salary,
                    address, request.hoursWorked, request.hourlyRate);
            } else {
                if (request.bonus < 0 || !Double.isFinite(request.bonus)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bonus cannot be negative.");
                }
                updated = new FullTimeEmployee(id, request.name, request.department, request.salary,
                    address, request.bonus);
            }
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        synchronized (employees) {
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getEmployeeId() == id) {
                    employees.set(i, updated);
                    return toMap(updated);
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found.");
    }

    @DeleteMapping("/employees/{id}")
    public Map<String, Object> deleteEmployee(@PathVariable int id) {
        synchronized (employees) {
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getEmployeeId() == id) {
                    Employee removed = employees.remove(i);
                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("message", "Employee deleted successfully.");
                    result.put("id", removed.getEmployeeId());
                    return result;
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found.");
    }

    @GetMapping("/employees/{id}")
    public Map<String, Object> getEmployee(@PathVariable int id) {
        Employee employee = findEmployee(id);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found.");
        }
        return toMap(employee);
    }

    @GetMapping("/employees/{id}/salary-slip")
    public Map<String, Object> getSalarySlip(@PathVariable int id) {
        Employee employee = findEmployee(id);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found.");
        }
        Map<String, Object> result = toMap(employee);
        result.put("hra", employee.calculateHRA());
        result.put("da", employee.calculateDA());
        result.put("pf", employee.calculatePF());
        result.put("monthlyBonus", employee instanceof FullTimeEmployee
                ? ((FullTimeEmployee) employee).getMonthlyBonus() : 0.0);
        if (employee instanceof PartTimeEmployee) {
            PartTimeEmployee partTime = (PartTimeEmployee) employee;
            result.put("hoursWorked", partTime.getHoursWorked());
            result.put("hourlyRate", partTime.getHourlyRate());
            result.put("hourlyPay", partTime.getHoursWorked() * partTime.getHourlyRate());
        }
        return result;
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        synchronized (employees) {
            double totalPayroll = 0;
            for (Employee employee : employees) {
                totalPayroll += employee.calculateNetSalary();
            }
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("companyName", Employee.getCompanyName());
            result.put("employeeCount", employees.size());
            result.put("totalPayroll", totalPayroll);
            return result;
        }
    }

    private Employee findEmployee(int id) {
        synchronized (employees) {
            for (Employee employee : employees) {
                if (employee.getEmployeeId() == id) return employee;
            }
        }
        return null;
    }

    private String clean(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }

    private Map<String, Object> toMap(Employee employee) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", employee.getEmployeeId());
        map.put("name", employee.getEmployeeName());
        map.put("department", employee.getDepartment());
        map.put("type", employee.getEmployeeType());
        map.put("city", employee.getAddress().getCity());
        map.put("state", employee.getAddress().getState());
        map.put("basicSalary", employee.getBasicSalary());
        map.put("grossSalary", employee.calculateGrossSalary());
        map.put("netSalary", employee.calculateNetSalary());
        return map;
    }

    public static class EmployeeRequest {
        public int id;
        public String name;
        public String department;
        public double salary;
        public String city;
        public String state;
        public String type = "FULL_TIME";
        public double bonus;
        public int hoursWorked;
        public double hourlyRate;
    }
}
