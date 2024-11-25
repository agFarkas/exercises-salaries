package hu.epam.exercise.model;

import java.util.Objects;

public class Employee {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final int salary;
    private final Integer managerId;

    private Employee(
            int id,
            String firstName,
            String lastName,
            int salary,
            Integer managerId
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    public static Employee of(int id, String firstName, String lastName, int salary, Integer managerId) {
        return new Employee(
                id,
                firstName,
                lastName,
                salary,
                managerId
        );
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getSalary() {
        return salary;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public boolean isCEO() {
        return Objects.isNull(managerId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
