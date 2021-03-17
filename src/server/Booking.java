package server;

import java.time.DayOfWeek;

public class Booking {
    public String ID;
    public DayOfWeek date;
    public int startTime;
    public int endTime;
    public String username;

    public Booking(String id,DayOfWeek date, int startTime, int endTime,String username) {
        this.ID= id;
        this.date = date;
        this.startTime=startTime;
        this.endTime = endTime;
        this.username = username;
    }

    public String getStartTime(){
        return makeReadable(this.startTime/4) + makeReadable(15*(this.startTime%4)) + "h";
    }

    public String getEndTime(){
        int endTime = this.endTime + 1;
        return makeReadable(endTime/4) + makeReadable(15*(endTime%4)) + "h";
    }

    private String makeReadable(int time){
        if (time >= 10)
            return String.valueOf(time);
        else
            return "0" + time;
    }
}
