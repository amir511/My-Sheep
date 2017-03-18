package model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by aanwer on 11/25/2016.
 */
@ParseClassName("Attendance")
public class Attendance extends ParseObject {
    public void setDate(String date){
        put("date",date);
    }
    public String getDate(){
        return getString("date");
    }
    public void setKidsIds (List<String> KidsIds){
        put("KidsIds",KidsIds);
    }
    public List<String> getKidsIds(){
        return getList("KidsIds");
    }
}
