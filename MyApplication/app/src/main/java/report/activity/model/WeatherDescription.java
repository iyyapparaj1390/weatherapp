package report.activity.model;

import java.io.Serializable;

/**
 * Created by samsung on 5/20/2018.
 */

public class WeatherDescription implements Serializable {
    private String id;
    private  String main;
    private  String description;
    private  String icon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
