package api;

import java.util.List;

public class HolidayRequest {

    private String description;

    private String date;

    private String length;

    private String operational_country_id;

    private List<String> location;

    private boolean adjustLeave;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getOperational_country_id() {
        return operational_country_id;
    }

    public void setOperational_country_id(String operational_country_id) {
        this.operational_country_id = operational_country_id;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public boolean isAdjustLeave() {
        return adjustLeave;
    }

    public void setAdjustLeave(boolean adjustLeave) {
        this.adjustLeave = adjustLeave;
    }

    @Override
    public String toString() {
        return "HolidayRequest{" +
                "description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", length='" + length + '\'' +
                ", operational_country_id='" + operational_country_id + '\'' +
                ", location=" + location +
                ", adjustLeave=" + adjustLeave +
                '}';
    }
}
