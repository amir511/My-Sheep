package yoursheep.jesus.com.yoursheep;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;

import model.Kid;
import model.Visit;

public class KidDetail extends AppCompatActivity {
    @Nullable
    private ImageView kidImage;
    @Nullable
    private TextView nameText;
    @Nullable
    private TextView address;
    @Nullable
    private Button mobile;
    @Nullable
    private Button home;
    @Nullable
    private TextView otherInfo;
    @Nullable
    private Button fatherNumber;
    @Nullable
    private Button motherNumber;
    @Nullable
    private Button grandParentNumber;
    @Nullable
    private Button extra1Number;
    @Nullable
    private Button extra2Number;
    private Kid myKid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_detail);
        setTitle("Sheep Detail");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        kidImage = (ImageView) findViewById(R.id.KidDetailImage);
        nameText = (TextView) findViewById(R.id.KidDetailName);
        address = (TextView) findViewById(R.id.KidDetailAddress);
        mobile = (Button) findViewById(R.id.KidDetailMobileButton);
        home = (Button) findViewById(R.id.KidDetailHomeButton);
        otherInfo = (TextView) findViewById(R.id.KidDetailOtherInfo);
        fatherNumber = (Button) findViewById(R.id.KidDetailFatherButton);
        motherNumber = (Button) findViewById(R.id.KidDetailMotherButton);
        grandParentNumber = (Button) findViewById(R.id.KidDetailGrandButton);
        extra1Number = (Button) findViewById(R.id.KidDetailExtra1Button);
        extra2Number = (Button) findViewById(R.id.KidDetailExtra2Button);

        String id = getIntent().getStringExtra("id");
        ParseQuery<Kid> query = ParseQuery.getQuery(Kid.class).fromLocalDatastore();
        query.getInBackground(id, new GetCallback<Kid>() {
            @Override
            public void done(@NonNull Kid kid, ParseException e) {
                if(kid.getImage() != null){
                kid.getImage().getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(@NonNull byte[] bytes, @Nullable ParseException e) {
                    if(e == null){
                        Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        kidImage.setImageBitmap(bitmapImage);
                    }}
                });}

                nameText.setText(kid.getName());
                String addressString = kid.getBuildingNumber()+"ش "+kid.getStreetName()+"\n"
                                        +"الدور: "+kid.getFloorNumber()+" شقة: "+kid.getApartmentNumber()+"\n"
                                        +"المنطقة: "+kid.getAreaName()+"\n"+kid.getAddressSpecialSign();
                address.setText("Address: \n"+addressString);
                mobile.setText("Call mobile number: "+kid.getMobileNumber());
                home.setText("Call home number: "+kid.getHomeNumber());
                fatherNumber.setText("Call Father number: "+kid.getFatherNumber());
                motherNumber.setText("Call Mother number: "+kid.getMotherNumber());
                grandParentNumber.setText("Call GrandParent number: "+kid.getGrandNumber());
                extra1Number.setText("Extra number 1 : "+kid.getExtraNumber1());
                extra2Number.setText("Extra number 2 : "+kid.getExtraNumber2());

                String otherInfoString = "Other Info:\n "+" Confession Father: "+kid.getConfessionFather()+"\n"
                                        +"  Birthday: "+kid.getBirthday()+"\n"+
                                        "  Email: "+kid.getEmail()+"\n"+"  School: "+kid.getSchool()
                                        +"\n"+"  Comments: \n"+kid.getComment()
                                        +"\n"+"  Other Address: "+"\n"+kid.getOtherAddress();
                otherInfo.setText(otherInfoString);
                myKid = kid;
            }

        });

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(myKid.getMobileNumber());
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(myKid.getHomeNumber());
            }
        });
        fatherNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(myKid.getFatherNumber());
            }
        });
        motherNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(myKid.getMotherNumber());
            }
        });
        grandParentNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(myKid.getGrandNumber());
            }
        });
        extra1Number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(myKid.getExtraNumber1());
            }
        });
        extra2Number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(myKid.getExtraNumber2());
            }
        });
    }


    public void call(@Nullable String number){
        if (number != null && !number.equals("")){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number));
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"No number",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
        KidDetail.this.finish();
        }else if(item.getItemId() == R.id.edit){
            boolean isLoggedIn = getIntent().getBooleanExtra("isLoggedIn",false);
            if (isConnected() && isLoggedIn){
            Intent editIntent = new Intent(KidDetail.this,AddKid.class);
            editIntent.putExtra("editMode",true);
            editIntent.putExtra("editKidId",myKid.getObjectId());
            startActivity(editIntent);
            KidDetail.this.finish();}
            else{
                Snackbar.make(findViewById(R.id.activity_kid_detail),"Not allowed offline!",Snackbar.LENGTH_SHORT).show();}
                //Toast.makeText(getApplicationContext(),"Not allowed offline!",Toast.LENGTH_SHORT).show();}
        }else if(item.getItemId() == R.id.visit){
            if (isConnected()) {
                visitInterface();
            }else{
                Snackbar.make(findViewById(R.id.activity_kid_detail),"You are offline!",Snackbar.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"You are offline!",Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void visitInterface() {
        AlertDialog.Builder messageBoxBuilder = new AlertDialog.Builder(KidDetail.this);
        messageBoxBuilder.setCancelable(true);
        String[] options = {"Add a new visit","See visit history"};
        messageBoxBuilder.setTitle("Visits");
        messageBoxBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){newVisitDialog();}
                else if(which == 1){visitHistoryDialog();}
            }
        });
        AlertDialog messageBox = messageBoxBuilder.create();
        messageBox.show();
    }
    private void newVisitDialog(){
        AlertDialog.Builder newVisitBuilder = new AlertDialog.Builder(KidDetail.this);
        newVisitBuilder.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.new_visit,null);
        newVisitBuilder.setView(view);
        final EditText dateText = (EditText) view.findViewById(R.id.visitDate);
        final EditText servantsText = (EditText) view.findViewById(R.id.visitServants);
        final EditText summaryText = (EditText) view.findViewById(R.id.visitSummary);
        Button visitSaveBtn = (Button) view.findViewById(R.id.visitSaveBtn);
        final AlertDialog newVisitDialog = newVisitBuilder.create();

        visitSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Visit visit = new Visit();
                visit.setKidId(myKid.getObjectId());
                visit.setVisitDate(dateText.getText().toString());
                visit.setServants(servantsText.getText().toString());
                visit.setSummary(summaryText.getText().toString());
                visit.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(@Nullable ParseException e) {
                        if (e == null){
                            incrementNumberOfVisits();
                            newVisitDialog.dismiss();

                        }else {
                            Toast.makeText(getApplicationContext(),"Error!, Please try again",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
        newVisitDialog.show();

    }
    private void visitHistoryDialog(){
        AlertDialog.Builder visitHistoryBuilder = new AlertDialog.Builder(KidDetail.this);
        visitHistoryBuilder.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.visit_history,null);
        visitHistoryBuilder.setView(view);
        final TextView historyText = (TextView) view.findViewById(R.id.historyText);
        final StringBuilder historyString = new StringBuilder();
        ParseQuery<Visit> query = ParseQuery.getQuery(Visit.class);//.fromLocalDatastore();
        query.whereContains("kidId",myKid.getObjectId());
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<Visit>() {
            @Override
            public void done(@NonNull List<Visit> list, @Nullable ParseException e) {
                if (e == null){
                    if(list.size()!=0){
                    for (int i = 0; i < list.size() ; i++) {
                        Visit visit = list.get(i);
                        historyString.append("Visit No. : "+ (i+1)+"\n");
                        historyString.append("Visit Date: "+visit.getVisitDate()+"\n");
                        historyString.append("Servants:\n "+visit.getServants()+"\n");
                        historyString.append("Visit summary:\n "+visit.getSummary()+"\n\n\n");
                        }
                        historyText.setText(historyString);
                    }else{
                        historyText.setText("No visit history found!\nPlease pay a visit asap.");
                    }

                }else{Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_LONG).show();}
            }
        });

        AlertDialog historyDialog = visitHistoryBuilder.create();
        historyDialog.show();
    }
    private void incrementNumberOfVisits(){
        int i;
        if(myKid.getNumberOfVisits() == null){
            i = 0;
        }else{
            i = Integer.parseInt(myKid.getNumberOfVisits());}
        i++;
        myKid.setNumberOfVisits(String.valueOf(i));
        myKid.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            if (e ==  null){
                Toast.makeText(getApplicationContext(),"Saved successfully",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),"Error calculating the number of visits!",Toast.LENGTH_LONG).show();
            }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean  isConnected(){
        ConnectivityManager connection  = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connection.getActiveNetworkInfo();
        boolean check = activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
        return check;
    }
}
