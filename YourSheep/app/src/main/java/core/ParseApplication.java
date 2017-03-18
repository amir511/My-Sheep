package core;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import model.Attendance;
import model.Kid;
import model.Visit;

/**
 * Created by aanwer on 11/12/2016.
 */

public class ParseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Kid.class);
        ParseObject.registerSubclass(Visit.class);
        ParseObject.registerSubclass(Attendance.class);
        Parse.initialize(new  Parse.Configuration.Builder(this)
                .applicationId("PARSE applicationId to be added here")
                .clientKey("Parse Client Id to be added here")
                .server("Parse server address to be added here").enableLocalDataStore().build());

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }


}
