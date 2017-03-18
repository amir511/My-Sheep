package yoursheep.jesus.com.yoursheep;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import adapter.KidAdapter;
import core.VisitCalculator;
import model.Kid;

public class MainActivity extends AppCompatActivity {
    @Nullable
    private FloatingActionButton faButton;
    private KidAdapter adapter;
    private ArrayList<Kid> kidArrayList;
    @Nullable
    private ListView listView;
    public static boolean  isLoggedIn;
    private SharedPreferences prefs;
    @Nullable
    private ParseUser user = null;
    private VisitCalculator visitCalculator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLoggedIn = getIntent().getBooleanExtra("isLoggedIn",false);
        //code for the floating button
        faButton = (FloatingActionButton) findViewById(R.id.addButton);

        faButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection() && isLoggedIn){
                    startActivity(new Intent(MainActivity.this,AddKid.class));
                }else if (checkConnection() && !isLoggedIn){
                    toaster("Logging you In","l");
                    logIn();
                    startActivity(new Intent(MainActivity.this,AddKid.class));
                    isLoggedIn = true;
                }
                else{
                    Snackbar.make(findViewById(R.id.activity_main),
                            "Adding new sheep not allowed while offline!...Connect to internet and try again.",
                            Snackbar.LENGTH_LONG).show();
//                    toaster("Adding new sheep not allowed while offline!","s");
//                    toaster("Connect to internet and try again.","l");
                }
            }
        });

        kidArrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        adapter = new KidAdapter(MainActivity.this,kidArrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,KidDetail.class);
                intent.putExtra("id",kidArrayList.get(position).getObjectId());
                intent.putExtra("isLoggedIn",isLoggedIn);
                startActivity(intent);
            }
        });
        localQuery();
        if (checkConnection()) {
            serverQuery();
            }
        visitCalculator = new VisitCalculator();


    }
    private void logIn() {
        prefs = getSharedPreferences("prefFile", 0);
        String userName = prefs.getString("userName",null);
        String password = prefs.getString("password",null);
        user.logInInBackground(userName, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, @Nullable ParseException e) {
                if (e == null){
                    toaster("Login successful!","l");

                }else {
                    toaster("Failed to connect!","l");
                }
            }
        });
    }


    public boolean checkConnection(){   //method for checking network connection
            ConnectivityManager cm =
                    (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            return isConnected;
        }

    public void toaster(String message, @NonNull String duration){
        int toastDuration;
        if (duration.equals("s")){toastDuration = Toast.LENGTH_SHORT;}
        else{toastDuration = Toast.LENGTH_LONG;}
        Toast.makeText(getApplicationContext(),message,toastDuration).show();
    }


 private void localQuery(){
     ParseQuery<Kid> query = ParseQuery.getQuery(Kid.class).fromLocalDatastore();
     query.orderByAscending("createdAt");
     query.findInBackground(new FindCallback<Kid>() {
         @Override
         public void done(List<Kid> list, @Nullable ParseException e) {
             if (e == null && list.size()!=0) {
                 kidArrayList.clear();
                 kidArrayList.addAll(list);
                 applyAttendanceToKids();
                 adapter.notifyDataSetChanged();
                 listView.invalidate();
                    }
                     }
                 });
 }
    private void serverQuery() {
        ParseQuery<Kid> query = ParseQuery.getQuery(Kid.class);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<Kid>() {
            @Override
            public void done(List<Kid> list, @Nullable ParseException e) {
                if (e == null){
                    kidArrayList.clear();
                    kidArrayList.addAll(list);
                    Kid.pinAllInBackground(kidArrayList);
                    applyAttendanceToKids();
                    adapter.notifyDataSetChanged();
                    listView.invalidate();
                }else if(checkConnection()){
                    toaster("Error getting data from Server\nWorking offline!","l");
                }
            }
        });
                }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //toaster("Logging out of server..","s");----this line removed because of the below to do problem
        ParseUser.getCurrentUser().logOut();
        /*TODO there is a problem here that needs modification:
            when the layout is changed from portrait to landscape or vice versa, i.e.: screen rotation
                     the onDestroy method is called, thus logging the user out unintentionally
                     although this doesn't make a problem
                     as parse supports anonymous user modification, but if there is a solution it will be better
        */

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.attendance,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Splash.attendanceQuery();
        serverQuery();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.attendanceIcon){
            if(checkConnection() && isLoggedIn){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Attendances");
            String[] options = {"Add a new attendance","View previous attendances"};
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){
                        if(checkConnection() && isLoggedIn){
                        startActivity(new Intent(MainActivity.this,AddAttendance.class));}
                        else if (checkConnection() && !isLoggedIn) {
                            toaster("Logging you in...","l");
                            logIn();
                            startActivity(new Intent(MainActivity.this,AddAttendance.class));
                            isLoggedIn = true;
                        } else{
                            Snackbar.make(findViewById(R.id.activity_main),"Connect to Internet and try again",Snackbar.LENGTH_LONG).show();
                        }
                    }else if(which == 1){
                        startActivity(new Intent(MainActivity.this,AllAttendances.class));
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();}
            else{
                Snackbar.make(findViewById(R.id.activity_main),
                        "You have to be online and logged in..Restart application while online.",
                        Snackbar.LENGTH_LONG).show();
            }
        }
        if (item.getItemId() == R.id.refreshIcon){
            Snackbar.make(findViewById(R.id.activity_main),"Refreshing data...",Snackbar.LENGTH_SHORT).show();
            Splash.attendanceQuery();
            visitCalculator.updateKidsWithVisitsAndSave();
            serverQuery();
        }
        return super.onOptionsItemSelected(item);
    }
    private void applyAttendanceToKids(){
        if(Splash.lastAttendanceIds != null){
            for (int i = 0; i < kidArrayList.size() ; i++) {
                kidArrayList.get(i).setLastTimeAttendance(false);
            }
        for (int i = 0; i < Splash.lastAttendanceIds.size(); i++) {
            for (int j = 0; j < kidArrayList.size() ; j++) {
                if (Splash.lastAttendanceIds.get(i).equals(kidArrayList.get(j).getObjectId())){
                    kidArrayList.get(j).setLastTimeAttendance(true);
                }
            }

        }}
        if(Splash.attendancesStatistic != null){
        for (int i = 0; i < kidArrayList.size(); i++) {
            if(Splash.attendancesStatistic.containsKey(kidArrayList.get(i).getObjectId())){
                String numberOfAttendances = String.valueOf(Splash.attendancesStatistic.get(kidArrayList.get(i).getObjectId()));
                kidArrayList.get(i).setNumberOfAttendances(numberOfAttendances);
            }
        }}
    }
}