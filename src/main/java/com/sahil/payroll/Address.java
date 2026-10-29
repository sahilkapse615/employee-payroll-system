package com.sahil.payroll;

public class Address {
    private String city;
    private String state;

    public Address() {
        this("Not Set", "Not Set");
    }

    public Address(String city, String state) {
        this.city = city;
        this.state = state;
    }

    public Address(Address other) {
        this(other.city, other.state);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city != null && !city.trim().isEmpty()) {
            this.city = city.trim();
        }
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if (state != null && !state.trim().isEmpty()) {
            this.state = state.trim();
        }
    }

    @Override
    public String toString() {
        return city + ", " + state;
    }
}
