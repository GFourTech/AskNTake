package Pojo;

/**
 * Created by on 24/05/2017.
 */

public class TimingsPojo {

    String week_day;
    String daycount;
    String fromTime;
    String toTime;
    String id;

    public TimingsPojo(String week_day, String daycount, String fromTime, String toTime, String id) {
        this.week_day = week_day;
        this.daycount = daycount;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.id = id;
    }



    public TimingsPojo() {

    }


    public String getWeek_day() {
        return week_day;
    }

    public void setWeek_day(String week_day) {
        this.week_day = week_day;
    }

    public String getDaycount() {
        return daycount;
    }

    public void setDaycount(String daycount) {
        this.daycount = daycount;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
