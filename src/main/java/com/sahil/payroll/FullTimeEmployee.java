package com.sahil.payroll;

public class FullTimeEmployee extends Employee {
    private double monthlyBonus;

    public FullTimeEmployee() {
        this(0, "Not Set", "Not Set", 0.0, new Address(), 0.0);
    }

    public FullTimeEmployee(int id, String name, String department,
                            double salary, Address address, double monthlyBonus) {
        super(id, name, department, salary, address);
        setMonthlyBonus(monthlyBonus);
    }

    public double getMonthlyBonus() {
        return monthlyBonus;
    }

    public void setMonthlyBonus(double monthlyBonus) {
        if (monthlyBonus < 0) {
            throw new IllegalArgumentException("Bonus cannot be negative.");
        }
        this.monthlyBonus = monthlyBonus;
    }

    @Override
    public String getEmployeeType() {
        return "Full-Time Employee";
    }

    @Override
    public double calculateNetSalary() {
        return super.calculateNetSalary() + monthlyBonus;
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.printf("Monthly Bonus : Rs. %.2f%n", monthlyBonus);
    }

    @Override
    public void displaySalarySlip() {
        super.displaySalarySlip();
        System.out.printf("Monthly Bonus : Rs. %.2f%n", monthlyBonus);
        System.out.printf("Final Net Pay : Rs. %.2f%n", calculateNetSalary());
        System.out.println("========================================");
    }
}
