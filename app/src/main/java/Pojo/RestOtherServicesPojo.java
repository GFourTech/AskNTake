package Pojo;

import java.util.ArrayList;

/**
 * Created by on 05/05/2017.
 */

public class RestOtherServicesPojo {

    ArrayList<TimingsPojo> timings;

    public RestOtherServicesPojo(ArrayList<TimingsPojo> timings) {
        this.timings = timings;
    }

    public RestOtherServicesPojo() {

    }

    public ArrayList<TimingsPojo> getTimings() {
        return timings;
    }

    public void setTimings(ArrayList<TimingsPojo> timings) {
        this.timings = timings;
    }
}
