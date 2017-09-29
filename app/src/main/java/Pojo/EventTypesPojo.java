package Pojo;

/**
 * Created by on 05/05/2017.
 */

public class EventTypesPojo {

    String event_date;
    String fromTime;
    //    String fromTimeType;
    String toTime;
//    String toTimeType;
    String id;


    public EventTypesPojo(String event_date, String fromTime, String toTime, String id) {
        this.event_date = event_date;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.id = id;
    }

    public EventTypesPojo() {

    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

//    public String getFromTimeType() {
//        return fromTimeType;
//    }
//
//    public void setFromTimeType(String fromTimeType) {
//        this.fromTimeType = fromTimeType;
//    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

//    public String getToTimeType() {
//        return toTimeType;
//    }
//
//    public void setToTimeType(String toTimeType) {
//        this.toTimeType = toTimeType;
//    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
