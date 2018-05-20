package report.activity.model;

import java.io.Serializable;

/**
 * Created by samsung on 5/20/2018.
 */

public class Wind implements Serializable {

    String speed;
    String degree;

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
