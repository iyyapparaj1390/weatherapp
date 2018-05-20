package report.activity.model;

import java.io.Serializable;

/**
 * Created by samsung on 5/20/2018.
 */

public class Settinginfo implements Serializable {
   private boolean check;
   private boolean type;
   private String settingName;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }
}
