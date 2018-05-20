package report.activity.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by samsung on 5/20/2018.
 */

//serializable used to save state of object
public class WeatherReport implements Serializable {

    private float temp;
    private float minTemp;
    private float maxTemp;
    private float seaLevel;
    private String humidty;
    private float pressure;
    private float grndLevel;
    private  String dateStr;
    private  WeatherDescription desc;
    private  Wind windRep;
    private  Cloud cloudRep;
    private  LocationInfo loc;
    private String reportDate;

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public void setSeaLevel(float seaLevel) {
        this.seaLevel = seaLevel;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public void setGrndLevel(float grndLevel) {
        this.grndLevel = grndLevel;
    }

    public float getTemp() {
        return temp;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public float getSeaLevel() {
        return seaLevel;
    }

    public float getPressure() {
        return pressure;
    }

    public float getGrndLevel() {
        return grndLevel;
    }

    public String getHumidty() {
        return humidty;
    }

    public void setHumidty(String humidty) {
        this.humidty = humidty;
    }



    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public WeatherDescription getDesc() {
        return desc;
    }

    public void setDesc(WeatherDescription desc) {
        this.desc = desc;
    }

    public Wind getWindRep() {
        return windRep;
    }

    public void setWindRep(Wind windRep) {
        this.windRep = windRep;
    }

    public Cloud getCloudRep() {
        return cloudRep;
    }

    public void setCloudRep(Cloud cloudRep) {
        this.cloudRep = cloudRep;
    }

    public LocationInfo getLoc() {
        return loc;
    }

    public void setLoc(LocationInfo loc) {
        this.loc = loc;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }
}
