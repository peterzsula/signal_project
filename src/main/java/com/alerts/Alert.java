package com.alerts;

// Represents an alert
public class Alert {
    private String patientId;
    private String condition;
    private long timestamp;

    public Alert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getCondition() {
        return condition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "patientId='" + patientId + '\'' +
                ", condition='" + condition + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Alert alert = (Alert) obj;
        return timestamp == alert.timestamp && patientId.equals(alert.patientId) && condition.equals(alert.condition);
    }
}
