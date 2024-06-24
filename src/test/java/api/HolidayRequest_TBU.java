package api;

import java.util.List;
import java.util.Objects;

public class HolidayRequest_TBU {
    private String description;
    private String date;
    private int length;
    private int operational_country_id;
    private List<Integer> location;
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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getOperational_country_id() {
        return operational_country_id;
    }

    public void setOperational_country_id(int operational_country_id) {
        this.operational_country_id = operational_country_id;
    }

    public List<Integer> getLocation() {
        return location;
    }

    public void setLocation(List<Integer> location) {
        this.location = location;
    }

    public boolean isAdjustLeave() {
        return adjustLeave;
    }

    public void setAdjustLeave(boolean adjustLeave) {
        this.adjustLeave = adjustLeave;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HolidayRequest_TBU that = (HolidayRequest_TBU) o;
        return length == that.length &&
                operational_country_id == that.operational_country_id &&
                adjustLeave == that.adjustLeave &&
                Objects.equals(description, that.description) &&
                Objects.equals(date, that.date) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, date, length, operational_country_id, location, adjustLeave);
    }

    @Override
    public String toString() {
        return "HolidayRequest_TBU{" +
                "description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", length=" + length +
                ", operational_country_id=" + operational_country_id +
                ", location=" + location +
                ", adjustLeave=" + adjustLeave +
                '}';
    }
}