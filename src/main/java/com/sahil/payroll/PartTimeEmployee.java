package com.sahil.payroll;

public class PartTimeEmployee extends Employee {
    private int hoursWorked;
    private double hourlyRate;

    public PartTimeEmployee() {
        this(0, "Not Set", "Not Set", 0.0, new Address(), 0, 0.0);
    }

    public PartTimeEmployee(int id, String name, String department,
                            double basicSalary, Address address,
                            int hoursWorked, double hourlyRate) {
        super(id, name, department, basicSalary, address);
        setHoursWorked(hoursWorked);
        setHourlyRate(hourlyRate);
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHoursWorked(int hoursWorked) {
        if (hoursWorked < 0) {
            throw new IllegalArgumentException("Hours cannot be negative.");
        }
        this.hoursWorked = hoursWorked;
    }

    public void setHourlyRate(double hourlyRate) {
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative.");
        }
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String getEmployeeType() {
        return "Part-Time Employee";
    }

    @Override
    public double calculateNetSalary() {
        
        double hourlyPay = hoursWorked * hourlyRate;
        double totalBasic = getBasicSalary() + hourlyPay;
        double hra = totalBasic * 0.20;
        double da = totalBasic * 0.10;
        double pf = totalBasic * 0.12;
        return totalBasic + hra + da - pf;
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Hours Worked  : " + hoursWorked);
        System.out.printf("Hourly Rate   : Rs. %.2f%n", hourlyRate);
        System.out.printf("Hourly Pay    : Rs. %.2f%n", hoursWorked * hourlyRate);
        System.out.printf("Adjusted Net  : Rs. %.2f%n", calculateNetSalary());
    }

    @Override
    public void displaySalarySlip() {
        super.displaySalarySlip();
        System.out.println("Hours Worked  : " + hoursWorked);
        System.out.printf("Hourly Rate   : Rs. %.2f%n", hourlyRate);
        System.out.printf("Hourly Pay    : Rs. %.2f%n", hoursWorked * hourlyRate);
        System.out.printf("Final Net Pay : Rs. %.2f%n", calculateNetSalary());
        System.out.println("========================================");
    }
}
