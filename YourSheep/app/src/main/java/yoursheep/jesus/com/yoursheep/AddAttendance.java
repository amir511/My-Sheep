package yoursheep.jesus.com.yoursheep;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.AddAttendanceAdapter;
import model.Attendance;
import model.Kid;

public class AddAttendance extends AppCompatActivity {
    private EditText dateText;
    private ListView listView;
    private AddAttendanceAdapter adapter;
    private boolean isEditing;
    private String[] intentAttendance;
    private String intentDate;
    private String intentAttendanceId;
    private HashMap<Kid,Boolean> activityMap;
    private ArrayList<Kid> kidArrayList;
    private Attendance attendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendance);
       //code to get intent
        isEditing = getIntent().getBooleanExtra("isEditing",false);
        intentAttendance = getIntent().getStringArrayExtra("intentAttendance");
        intentDate = getIntent().getStringExtra("intentDate");
        intentAttendanceId = getIntent().getStringExtra("intentAttendanceId");

        //code for adding the home arrow to actionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(isEditing){
            actionBar.setTitle("Edit attendance");
        }else{
            actionBar.setTitle("Add attendance");
        }
        //code for connecting views
        dateText = (EditText) findViewById(R.id.attendanceDate);
        listView = (ListView) findViewById(R.id.kidsAttendanceList);

        //if editing attendance we should find the date existing
        if(isEditing){
            dateText.setText(intentDate);
            //also getting the attendance with the specified id from intent
            ParseQuery<Attendance> singleAttendanceQuery = ParseQuery.getQuery(Attendance.class);
            try {
                attendance = singleAttendanceQuery.get(intentAttendanceId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{ // but if we are not editing we should initialize an attendance object anyway for later saving
            attendance = new Attendance();
        }

        //Initializing the kidArrayList and updating data in it
        kidArrayList = new ArrayList<>();
        localQuery();









    }
    private void localQuery(){
        ParseQuery<Kid> lQuery = ParseQuery.getQuery(Kid.class).fromLocalDatastore();
        lQuery.orderByAscending("createdAt");
        lQuery.findInBackground(new FindCallback<Kid>() {
            @Override
            public void done(List<Kid> list, ParseException e) {
                if (e == null) {

                    kidArrayList.clear();
                    kidArrayList.addAll(list);
                    fillHashMap();
                    adapter = new AddAttendanceAdapter(AddAttendance.this,kidArrayList,activityMap);
                    listView.setAdapter(adapter);
                    listView.invalidate();
                }

            }
        });
    }


private void fillHashMap(){
    activityMap = new HashMap<>(kidArrayList.size());
    for (int i = 0; i < kidArrayList.size(); i++) {
        activityMap.put(kidArrayList.get(i),false);
    }
    if(isEditing){
        for (int i = 0; i < intentAttendance.length; i++) {
            String leftComparison = intentAttendance[i];
            for (int j = 0; j < kidArrayList.size(); j++) {
                String rightComparison = kidArrayList.get(j).getObjectId();
                if(leftComparison.equals(rightComparison)){
                    activityMap.put(kidArrayList.get(j),true);}
                }
            }
        }
    }
private void saveMethod(){
        //will save the data
        if(dateText.getText().toString().equals("") || dateText.getText().toString().equals(" ") || dateText.getText() == null){  //but before saving we will make sure that date is there
            Toast.makeText(getApplicationContext(), "Attendance date is required",Toast.LENGTH_SHORT).show();
           // Snackbar.make(findViewById(R.id.activity_add_attendance),
                  //  "Attendance date is required", Snackbar.LENGTH_SHORT).show();
        }else{
            List<String> kidsIds = new ArrayList<>();
            for (int i = 0; i < adapter.map.size() ; i++) {
                if (adapter.map.get(kidArrayList.get(i))){
                    kidsIds.add(kidArrayList.get(i).getObjectId());
                }
            }
            attendance.setDate(dateText.getText().toString());
            attendance.setKidsIds(kidsIds);
            attendance.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        Toast.makeText(getApplicationContext(),"Saved successfully.",Toast.LENGTH_LONG).show();
                        end();
                    }
                }
            });
        }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_attendance,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            end();
        }else if (item.getItemId() == R.id.saveAttendance){
            saveMethod();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkConnection(){   //method for checking network connection
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    private void end(){
        activityMap.clear();
        adapter.map.clear();
        AddAttendance.this.finish();
    }
}