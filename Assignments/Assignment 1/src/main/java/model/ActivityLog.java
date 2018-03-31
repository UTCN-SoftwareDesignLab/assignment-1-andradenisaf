package model;

import java.io.Serializable;

public class ActivityLog implements IModel, Serializable {

    private int id;
    private int employeeId;
    private String description;

    public ActivityLog(int employeeId, String description) {
        this.employeeId = employeeId;
        this.description = description;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ActivityLog{" +
                "employeeId=" + employeeId +
                ", description='" + description + '\'' +
                '}';
    }
}
