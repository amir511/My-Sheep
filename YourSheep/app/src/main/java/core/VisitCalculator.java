package core;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import model.Kid;
import model.Visit;

/**
 * Created by aanwer on 2/6/2017.
 */

public class VisitCalculator {

    private ArrayList<Visit> visitList = new ArrayList<>();
    private ArrayList<Kid> kidList =new ArrayList<>();

    public VisitCalculator(){
        getAllVisitsFromServer();
        getAllKidsFromServer();
    }
    private void getAllVisitsFromServer()  {
        ParseQuery<Visit> visitParseQuery = new ParseQuery<Visit>("Visit");
        visitParseQuery.findInBackground(new FindCallback<Visit>() {
            @Override
            public void done(List<Visit> list, ParseException e) {
                if(e == null){
                    visitList.addAll(list);
                }
            }
        });
    }
    private void getAllKidsFromServer(){
        ParseQuery<Kid> kidParseQuery = new ParseQuery<Kid>("Kid");
        kidParseQuery.findInBackground(new FindCallback<Kid>() {
            @Override
            public void done(List<Kid> list, ParseException e) {
                if (e == null){
                    kidList.addAll(list);
                }
            }
        });
    }

    public void updateKidsWithVisitsAndSave(){
        for (int i = 0; i < kidList.size() ; i++) {
            int numberOfVisits = 0; //initial value
            Kid kid = kidList.get(i);
            for (int j = 0; j < visitList.size(); j++) {
                Visit visit = visitList.get(j);
                if (kid.getObjectId().equals(visit.getKidId())){
                    numberOfVisits++;
                }
            }
            String numberOfVisitsString = String.valueOf(numberOfVisits);
            kid.setNumberOfVisits(numberOfVisitsString);
            kid.saveInBackground();
        }
    }
}
