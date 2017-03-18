package yoursheep.jesus.com.yoursheep;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import model.Attendance;

public class AllAttendances extends AppCompatActivity {
    private ListView attendanceList;
    private ArrayList<Attendance> attendances;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_attendances);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Previous Attendances");
        actionBar.setDisplayHomeAsUpEnabled(true);

        attendanceList = (ListView) findViewById(R.id.attendanceList);
        attendances = new ArrayList<>();
        ParseQuery<Attendance> lQuery = new ParseQuery<>(Attendance.class).fromLocalDatastore();
        lQuery.orderByAscending("createdAt");
        lQuery.findInBackground(new FindCallback<Attendance>() {
            @Override
            public void done(List<Attendance> list, ParseException e) {
                if (e == null){
                    attendances.clear();
                    attendances.addAll(list);
                    String [] dates = new String [list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        dates[i] = "Attendance on: "+list.get(i).getDate();
                    }
                    adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.dates_row,R.id.rowDate,dates);
                    attendanceList.setAdapter(adapter);
                    attendanceList.invalidate();
                    adapter.notifyDataSetChanged();
                    
                }
            }
        });
        ParseQuery<Attendance> sQuery = new ParseQuery<>(Attendance.class);
        sQuery.orderByAscending("createdAt");
        sQuery.findInBackground(new FindCallback<Attendance>() {
            @Override
            public void done(List<Attendance> list, ParseException e) {
                if (e == null){
                    attendances.clear();
                    attendances.addAll(list);
                    Attendance.pinAllInBackground(attendances);
                    String [] dates = new String [list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        dates[i] = "Attendance on: "+list.get(i).getDate();
                    }
                    adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.dates_row,R.id.rowDate,dates);
                    attendanceList.setAdapter(adapter);
                    attendanceList.invalidate();
                    adapter.notifyDataSetChanged();
                }else{
                    Snackbar.make(findViewById(R.id.activity_all_attendances),"Error getting data from server!",Snackbar.LENGTH_LONG);
                }
            }
        });
        attendanceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<String> kidsAttendedList = attendances.get(position).getKidsIds();
                String[] intentAttendance = new String[kidsAttendedList.size()];
                for (int i = 0; i < kidsAttendedList.size(); i++) {
                    intentAttendance[i] = kidsAttendedList.get(i);
                }
                Intent intent = new Intent(AllAttendances.this,AddAttendance.class);
                intent.putExtra("isEditing",true);
                intent.putExtra("intentAttendance",intentAttendance);
                intent.putExtra("intentDate",attendances.get(position).getDate());
                intent.putExtra("intentAttendanceId",attendances.get(position).getObjectId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();}
        return super.onOptionsItemSelected(item);
    }
}


