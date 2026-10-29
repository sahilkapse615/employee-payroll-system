package com.sahil.payroll;

public class Employee {
    private int employeeId;
    private String employeeName;
    private String department;
    private double basicSalary;
    private Address address;

    private static String companyName = "Smart Tech Enterprises.";
    private static int employeeCount = 0;

  
    public Employee() {
        this(0, "Not Set", "Not Set", 0.0, new Address());
    }


    public Employee(int employeeId, String employeeName, String department,
                    double basicSalary, Address address) {
        if (employeeId < 0) {
            throw new IllegalArgumentException("Employee ID cannot be negative.");
        }
        if (employeeName == null || employeeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be empty.");
        }
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }
        if (basicSalary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }

        this.employeeId = employeeId;
        this.employeeName = employeeName.trim();
        this.department = department.trim();
        this.basicSalary = basicSalary;
        this.address = (address == null) ? new Address() : new Address(address);
        employeeCount++;
    }

   
    public Employee(Employee other) {
        this(other.employeeId, other.employeeName, other.department,
             other.basicSalary, new Address(other.address));
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getDepartment() {
        return department;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public Address getAddress() {
        return new Address(address);
    }

    public void setEmployeeName(String employeeName) {
        if (employeeName == null || employeeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be empty.");
        }
        this.employeeName = employeeName.trim();
    }

    public void setDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }
        this.department = department.trim();
    }

    public void setBasicSalary(double basicSalary) {
        if (basicSalary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }
        this.basicSalary = basicSalary;
    }

    public void setAddress(Address address) {
        if (address != null) {
            this.address = new Address(address);
        }
    }

    public static String getCompanyName() {
        return companyName;
    }

    public static void setCompanyName(String companyName) {
        if (companyName != null && !companyName.trim().isEmpty()) {
            Employee.companyName = companyName.trim();
        }
    }

    public static int getEmployeeCount() {
        return employeeCount;
    }

    public static void displayTotalEmployees() {
        System.out.println("Total employee objects created: " + employeeCount);
    }

    public double calculateHRA() {
        return basicSalary * 0.20;
    }

    public double calculateDA() {
        return basicSalary * 0.10;
    }

    public double calculatePF() {
        return basicSalary * 0.12;
    }

    public double calculateGrossSalary() {
        return basicSalary + calculateHRA() + calculateDA();
    }

    public double calculateNetSalary() {
        return calculateGrossSalary() - calculatePF();
    }

    // Method overloading
    public double calculateNetSalary(double bonus) {
        if (bonus < 0) {
            throw new IllegalArgumentException("Bonus cannot be negative.");
        }
        return calculateNetSalary() + bonus;
    }

    public double calculateNetSalary(double bonus, double deduction) {
        if (bonus < 0 || deduction < 0) {
            throw new IllegalArgumentException("Bonus and deduction cannot be negative.");
        }
        return calculateNetSalary() + bonus - deduction;
    }

    public String getEmployeeType() {
        return "General Employee";
    }

    public void displayDetails() {
        System.out.println("----------------------------------------");
        System.out.println("Employee ID   : " + employeeId);
        System.out.println("Employee Name : " + employeeName);
        System.out.println("Department    : " + department);
        System.out.println("Employee Type : " + getEmployeeType());
        System.out.println("Address       : " + address);
        System.out.printf("Basic Salary  : Rs. %.2f%n", basicSalary);
        System.out.printf("Gross Salary  : Rs. %.2f%n", calculateGrossSalary());
        System.out.printf("Net Salary    : Rs. %.2f%n", calculateNetSalary());
    }

    public void displaySalarySlip() {
        System.out.println("========================================");
        System.out.println("             SALARY SLIP");
        System.out.println("========================================");
        System.out.println("Company       : " + companyName);
        System.out.println("Employee ID   : " + employeeId);
        System.out.println("Employee Name : " + employeeName);
        System.out.println("Department    : " + department);
        System.out.println("Employee Type : " + getEmployeeType());
        System.out.println("Address       : " + address);
        System.out.printf("Basic Salary  : Rs. %.2f%n", basicSalary);
        System.out.printf("HRA (20%%)     : Rs. %.2f%n", calculateHRA());
        System.out.printf("DA (10%%)      : Rs. %.2f%n", calculateDA());
        System.out.printf("PF (12%%)      : Rs. %.2f%n", calculatePF());
        System.out.println("----------------------------------------");
        System.out.printf("Gross Salary  : Rs. %.2f%n", calculateGrossSalary());
        System.out.printf("Net Salary    : Rs. %.2f%n", calculateNetSalary());
        System.out.println("========================================");
    }
}
