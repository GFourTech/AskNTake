package Pojo;

/**
 * Created by on 05/05/2017.
 */

public class OneTimeRideServicePojo {
    String traveldate;
    String destination_from;
    String destination_to;
    String travel_time;
    String id;
//    String time_type;

    public OneTimeRideServicePojo() {

    }

    public OneTimeRideServicePojo(String traveldate, String destination_from, String destination_to, String travel_time, String id) {
        this.traveldate = traveldate;
        this.destination_from = destination_from;
        this.destination_to = destination_to;
        this.travel_time = travel_time;
        this.id = id;
    }



    public String getTraveldate() {
        return traveldate;
    }

    public void setTraveldate(String traveldate) {
        this.traveldate = traveldate;
    }

    public String getDestination_from() {
        return destination_from;
    }

    public void setDestination_from(String destination_from) {
        this.destination_from = destination_from;
    }

    public String getDestination_to() {
        return destination_to;
    }

    public void setDestination_to(String destination_to) {
        this.destination_to = destination_to;
    }

    public String getTravel_time() {
        return travel_time;
    }

    public void setTravel_time(String travel_time) {
        this.travel_time = travel_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
