package yoursheep.jesus.com.yoursheep;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Attendance;

public class Splash extends AppCompatActivity {
    private boolean isFirstTime;
    private SharedPreferences prefs;
    @Nullable
    private ParseUser user = null;
    private static boolean isLoggedIn = false;
    public static double  totalAttendances;
    public static List<String> lastAttendanceIds;
    public static HashMap<String,Integer> attendancesStatistic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs = getSharedPreferences("prefFile", 0);
        isFirstTime = prefs.getBoolean("firstTime", true);
        if (isFirstTime){
            firstTimeLogIn();
        }else{
            autoLogin();
        }




    }
    public void firstTimeLogIn(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View custom = inflater.inflate(R.layout.login_dialog, null);
        builder.setView(custom);
        final EditText userNameEdit = (EditText) custom.findViewById(R.id.userName);
        final EditText passwordEdit = (EditText) custom.findViewById(R.id.password);
        final Button loginButton = (Button) custom.findViewById(R.id.loginButton);
        final Button cancelButton = (Button) custom.findViewById(R.id.CancelButton);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = userNameEdit.getText().toString();
                final String password = passwordEdit.getText().toString();

                user.logInInBackground(userName, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, @Nullable ParseException e) {
                        if (e == null) {
                            Snackbar.make(findViewById(R.id.activity_splash),"Login successful..",Snackbar.LENGTH_LONG).show();
                            isLoggedIn = true;
                            splash(1500);
                            dialog.dismiss();
                            prefs.edit().putBoolean("firstTime",false).apply();
                            prefs.edit().putString("userName",userName).apply();
                            prefs.edit().putString("password",password).apply();

                        } else {
                            userNameEdit.setText("");
                            passwordEdit.setText("");
                            Toast.makeText(getApplicationContext(),"Please try again!",Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Splash.this.finish();
            }
        });

        dialog.show();
    }

    public void splash(int delay){
        Handler handler = new Handler();
        attendanceQuery();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this,MainActivity.class);
                intent.putExtra("isLoggedIn", isLoggedIn);
                startActivity(intent);
                Splash.this.finish();
            }
        },delay);
    }
    public void autoLogin(){
        if (checkConnection()){
        String userName = prefs.getString("userName",null);
        String password = prefs.getString("password",null);
        user.logInInBackground(userName, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, @Nullable ParseException e) {
                if (e == null){
                    isLoggedIn = true;
                    splash(1500);
                    Snackbar.make(findViewById(R.id.activity_splash),"Login successful..",Snackbar.LENGTH_LONG).show();
                }
            }
            });
        }else {
            isLoggedIn = false;
            splash(4000);
            Snackbar.make(findViewById(R.id.activity_splash),"You are offline, Working on local data",Snackbar.LENGTH_LONG).show();}
    }
    public boolean checkConnection(){   //method for checking network connection
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    public static void attendanceQuery(){
        ParseQuery<Attendance> localQuery = new ParseQuery<>(Attendance.class).fromLocalDatastore();
        localQuery.orderByDescending("createdAt");

        localQuery.findInBackground(new FindCallback<Attendance>() {
            @Override
            public void done(List<Attendance> list, ParseException e) {
                if (e == null){
                    totalAttendances = list.size();
                    if (list.size() != 0){
                    lastAttendanceIds = list.get(0).getKidsIds();
                    attendancesStatistic = calculations(list);}
                }
            }
        });
        if(isLoggedIn){
            ParseQuery<Attendance> serverQuery = new ParseQuery<>(Attendance.class);
            serverQuery.orderByDescending("createdAt");
            serverQuery.findInBackground(new FindCallback<Attendance>() {
                @Override
                public void done(List<Attendance> list, ParseException e) {
                    if (e == null){
                        totalAttendances = list.size();
                        if (list.size() != 0){
                        lastAttendanceIds = list.get(0).getKidsIds();
                        Attendance.pinAllInBackground(list);
                        attendancesStatistic = calculations(list);}
                    }
                }
            });
        }
    }
    private static HashMap<String,Integer> calculations (List<Attendance> attendanceList){
        HashMap<String,Integer> output = new HashMap<>();
        List<String> idStrings= new ArrayList<>(); //we will store all the ids in this array
        for (int i = 0; i < attendanceList.size(); i++) {
            List<String> tempList = attendanceList.get(i).getKidsIds();

            for (int j = 0; j < tempList.size(); j++) {

                idStrings.add(tempList.get(j));}
        }
//        List<String> alreadyChecked = new ArrayList<>();
        String checking ;
        int count ;
        for (int i = 0; i < idStrings.size(); i++) {
            checking = idStrings.get(i);
            count = 0;
            for (int j = 0; j < idStrings.size(); j++) {
                if(checking.equals(idStrings.get(j)) && !output.containsKey(checking)){
                    count +=1;
                }
            }
            if(!output.containsKey(checking)){
            output.put(checking,count);}

//                alreadyChecked.add(checking);
        }
        return output;
    }
}

