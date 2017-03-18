package model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by aanwer on 11/22/2016.
 */
@ParseClassName("Visit")
public class Visit extends ParseObject{
    
    public void setKidId(String kidId){
        
        put("kidId",kidId);
    }
    public String getKidId(){
        return  getString("kidId");
    }
    public void setVisitDate(String visitDate){
        put("visitDate",visitDate);
    }
    public String getVisitDate(){
        return getString("visitDate");
    }
    public void setServants(String servants){
        put("servants",servants);
    }
    public String getServants(){
        return getString("servants");
    }
    public void setSummary(String summary){
        put("summary",summary);
    }
    public String getSummary(){
        return getString("summary");
    }
}
