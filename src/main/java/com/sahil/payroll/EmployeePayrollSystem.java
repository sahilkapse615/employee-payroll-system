package com.sahil.payroll;

import java.util.Scanner;

public class EmployeePayrollSystem {
    private static final int MAX_EMPLOYEES = 100;
    private static final Employee[] employees = new Employee[MAX_EMPLOYEES];
    private static int employeeCount = 0;
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("       EMPLOYEE PAYROLL SYSTEM");
        System.out.println("========================================");
        System.out.println("Company: " + Employee.getCompanyName());

        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    displayAllEmployees();
                    break;
                case 3:
                    searchEmployee();
                    break;
                case 4:
                    calculateSalary();
                    break;
                case 5:
                    displaySalarySlip();
                    break;
                case 6:
                    Employee.displayTotalEmployees();
                    System.out.println("Employees currently in list: " + employeeCount);
                    break;
                case 7:
                    displayTotalPayroll();
                    break;
                case 8:
                    demonstrateCopyConstructor();
                    break;
                case 9:
                    changeCompanyName();
                    break;
                case 0:
                    System.out.println("Thank you for using Employee Payroll System!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select from the menu.");
            }
        }
        sc.close();
    }

    private static void showMenu() {
        System.out.println("--------------- MAIN MENU ---------------");
        System.out.println("1. Add Employee");
        System.out.println("2. Display All Employees");
        System.out.println("3. Search Employee by ID or Name");
        System.out.println("4. Calculate Salary");
        System.out.println("5. Display Salary Slip");
        System.out.println("6. Show Employee Count");
        System.out.println("7. Show Total Payroll");
        System.out.println("8. Demonstrate Copy Constructor");
        System.out.println("9. Change Company Name (Static)");
        System.out.println("0. Exit");
        System.out.println("-----------------------------------------");
    }

    private static void addEmployee() {
        if (employeeCount >= MAX_EMPLOYEES) {
            System.out.println("Employee array is full. Cannot add more employees.");
            return;
        }

        int id = readInt("Enter Employee ID: ");
        if (id <= 0) {
            System.out.println("Employee ID must be greater than zero.");
            return;
        }
        if (findById(id) != null) {
            System.out.println("This Employee ID already exists.");
            return;
        }

        String name = readNonEmpty("Enter Employee Name: ");
        String department = readNonEmpty("Enter Department: ");
        String city = readNonEmpty("Enter City: ");
        String state = readNonEmpty("Enter State: ");
        double salary = readNonNegativeDouble("Enter Basic Salary (Rs.): ");

        System.out.println("Employee Type:");
        System.out.println("1. Full-Time");
        System.out.println("2. Part-Time");
        int type = readInt("Select type: ");

        try {
            Address address = new Address(city, state);
            Employee employee;

            if (type == 1) {
                double bonus = readNonNegativeDouble("Enter Monthly Bonus (Rs.): ");
                employee = new FullTimeEmployee(
                    id, name, department, salary, address, bonus
                );
            } else if (type == 2) {
                int hours = readNonNegativeInt("Enter Hours Worked: ");
                double rate = readNonNegativeDouble("Enter Hourly Rate (Rs.): ");
                employee = new PartTimeEmployee(
                    id, name, department, salary, address, hours, rate
                );
            } else {
                System.out.println("Invalid employee type. Employee not added.");
                return;
            }

            employees[employeeCount] = employee;
            employeeCount++;
            System.out.println("Employee added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add employee: " + e.getMessage());
        }
    }

    private static void displayAllEmployees() {
        if (employeeCount == 0) {
            System.out.println("No employees found. Add an employee first.");
            return;
        }

        System.out.println("============== ALL EMPLOYEES ==============");
        for (int i = 0; i < employeeCount; i++) {
            employees[i].displayDetails();
        }
        System.out.println("Employees in list: " + employeeCount);
    }

    private static void searchEmployee() {
        if (employeeCount == 0) {
            System.out.println("No employees available to search.");
            return;
        }

        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        int option = readInt("Choose search option: ");

        if (option == 1) {
            int id = readInt("Enter Employee ID: ");
            Employee employee = findById(id);
            if (employee != null) {
                employee.displayDetails();
            } else {
                System.out.println("Employee not found.");
            }
        } else if (option == 2) {
            String name = readNonEmpty("Enter name or part of name: ");
            boolean found = false;
            for (int i = 0; i < employeeCount; i++) {
                if (employees[i].getEmployeeName().toLowerCase()
                        .contains(name.toLowerCase())) {
                    employees[i].displayDetails();
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Employee not found.");
            }
        } else {
            System.out.println("Invalid search option.");
        }
    }

    private static void calculateSalary() {
        Employee employee = askEmployeeById();
        if (employee == null) {
            return;
        }

        System.out.println("Employee: " + employee.getEmployeeName());
        System.out.printf("Gross Salary: Rs. %.2f%n", employee.calculateGrossSalary());
        System.out.printf("Net Salary  : Rs. %.2f%n", employee.calculateNetSalary());

        System.out.println("Optional overloaded calculation:");
        double bonus = readNonNegativeDouble("Enter additional bonus (0 if none): ");
        double deduction = readNonNegativeDouble("Enter additional deduction (0 if none): ");
        double adjustedNet = employee.calculateNetSalary(bonus, deduction);
        System.out.printf("Adjusted Net Salary: Rs. %.2f%n", adjustedNet);
    }

    private static void displaySalarySlip() {
        Employee employee = askEmployeeById();
        if (employee != null) {
            employee.displaySalarySlip();
        }
    }

    private static Employee askEmployeeById() {
        if (employeeCount == 0) {
            System.out.println("No employees available.");
            return null;
        }
        int id = readInt("Enter Employee ID: ");
        Employee employee = findById(id);
        if (employee == null) {
            System.out.println("Employee not found.");
        }
        return employee;
    }

    private static Employee findById(int id) {
        for (int i = 0; i < employeeCount; i++) {
            if (employees[i].getEmployeeId() == id) {
                return employees[i];
            }
        }
        return null;
    }

    private static void displayTotalPayroll() {
        if (employeeCount == 0) {
            System.out.println("No employees. Total payroll: Rs. 0.00");
            return;
        }

        double total = 0;
        for (int i = 0; i < employeeCount; i++) {
            total += employees[i].calculateNetSalary();
        }
        System.out.println("============== PAYROLL SUMMARY ==============");
        System.out.println("Company              : " + Employee.getCompanyName());
        System.out.println("Employees in list    : " + employeeCount);
        System.out.printf("Total Monthly Payroll: Rs. %.2f%n", total);
        System.out.println("=============================================");
    }

    private static void demonstrateCopyConstructor() {
        Employee original = askEmployeeById();
        if (original == null) {
            return;
        }

        Employee copy = new Employee(original);
        System.out.println("Original employee:");
        original.displayDetails();
        System.out.println("Copied employee:");
        copy.displayDetails();
        System.out.println("A separate object was created using the copy constructor.");
        System.out.println("Note: copy is only a demonstration and was not added to the employee list.");
    }

    private static void changeCompanyName() {
        String name = readNonEmpty("Enter new company name: ");
        Employee.setCompanyName(name);
        System.out.println("Company name changed to: " + Employee.getCompanyName());
        System.out.println("The static company name is shared by all employees.");
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static int readNonNegativeInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("Value cannot be negative.");
        }
    }

    private static double readNonNegativeDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();
            try {
                double number = Double.parseDouble(value);
                if (Double.isFinite(number) && number >= 0) {
                    return number;
                }
                System.out.println("Enter a finite, non-negative number.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount, e.g. 20000 or 20000.50.");
            }
        }
    }
}
