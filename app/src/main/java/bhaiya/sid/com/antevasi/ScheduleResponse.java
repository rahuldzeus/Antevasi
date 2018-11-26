package bhaiya.sid.com.antevasi;

import java.io.Serializable;

public class ScheduleResponse implements Serializable {
    private String date,place,reason;
    private static final long serialVersionUID = 1L;
    ScheduleResponse(String date, String place, String reason){
        this.date=date;
        this.place=place;
        this.reason=reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
