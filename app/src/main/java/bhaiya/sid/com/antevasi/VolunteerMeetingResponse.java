package bhaiya.sid.com.antevasi;

import java.io.Serializable;

public class VolunteerMeetingResponse implements Serializable {
    private String date,time,meeting_with,meeting_for;
    private static final long serialVersionUID = 1L;
    VolunteerMeetingResponse(String date, String time, String meeting_with, String meeting_for){
        this.date=date;
        this.time=time;
        this.meeting_with=meeting_with;
        this.meeting_for=meeting_for;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMeeting_with() {
        return meeting_with;
    }

    public void setMeeting_with(String meeting_with) {
        this.meeting_with = meeting_with;
    }

    public String getMeeting_for() {
        return meeting_for;
    }

    public void setMeeting_for(String meeting_for) {
        this.meeting_for = meeting_for;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
