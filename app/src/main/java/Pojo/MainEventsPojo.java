package Pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 7/20/2017.
 */

public class MainEventsPojo {

    String id;
    ArrayList<EventsServicePojo> eventsServicePojoList=null;

    public MainEventsPojo(String id, ArrayList<EventsServicePojo> eventsServicePojoList) {
        this.id = id;
        this.eventsServicePojoList = eventsServicePojoList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<EventsServicePojo> getEventsServicePojoList() {
        return eventsServicePojoList;
    }

    public void setEventsServicePojoList(ArrayList<EventsServicePojo> eventsServicePojoList) {
        this.eventsServicePojoList = eventsServicePojoList;
    }
}
