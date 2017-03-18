package yoursheep.jesus.com.yoursheep;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import model.Kid;

public class AddKid extends AppCompatActivity {
    @Nullable
    private Button addSheep;
    @Nullable
    private ImageView kidImage;
    @Nullable
    private EditText name;
    @Nullable
    private EditText mobileNumber;
    @Nullable
    private EditText homeNumber;
    @Nullable
    private EditText fatherNumber;
    @Nullable
    private EditText motherNumber;
    @Nullable
    private EditText grandNumber;
    @Nullable
    private EditText extraNumber1;
    @Nullable
    private EditText extraNumber2;
    @Nullable
    private EditText building;
    @Nullable
    private EditText street;
    @Nullable
    private EditText area;
    @Nullable
    private EditText floor;
    @Nullable
    private EditText apartment;
    @Nullable
    private EditText specialSign;
    @Nullable
    private EditText otherAddress;
    @Nullable
    private EditText confessionFather;
    @Nullable
    private EditText email;
    @Nullable
    private EditText birthday;
    @Nullable
    private EditText comments;
    @Nullable
    private EditText school;
    private Kid kid;
    private boolean editMode;
    private String editKidId;
    private ParseFile image;
    private ParseFile thumbNail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kid);

        //getting intent Extras
        editMode = getIntent().getBooleanExtra("editMode",false);
        editKidId = getIntent().getStringExtra("editKidId");

        //setting home button and renaming activity title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        if(editMode){actionBar.setTitle("Edit Sheep");}
        else{actionBar.setTitle("Add new Sheep");}
        actionBar.setDisplayHomeAsUpEnabled(true);


        //Wiring views
        addSheep = (Button) findViewById(R.id.addSheepButton);
        kidImage = (ImageView) findViewById(R.id.addKidImage);
        name = (EditText) findViewById(R.id.addNameText);
        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        homeNumber = (EditText) findViewById(R.id.homeNumber);
        fatherNumber = (EditText) findViewById(R.id.fatherNumber);
        motherNumber = (EditText) findViewById(R.id.motherNumber);
        grandNumber = (EditText) findViewById(R.id.grandNumber);
        extraNumber1 = (EditText) findViewById(R.id.extraNumber1);
        extraNumber2 = (EditText) findViewById(R.id.extraNumber2);
        building = (EditText) findViewById(R.id.buildingNumber);
        street = (EditText) findViewById(R.id.streetName);
        area = (EditText) findViewById(R.id.areaName);
        floor = (EditText) findViewById(R.id.floor);
        apartment = (EditText) findViewById(R.id.apartment);
        specialSign = (EditText) findViewById(R.id.specialSign);
        otherAddress = (EditText) findViewById(R.id.otherAddress);
        confessionFather = (EditText) findViewById(R.id.confessionFather);
        email = (EditText) findViewById(R.id.email);
        birthday = (EditText) findViewById(R.id.birthday);
        comments = (EditText) findViewById(R.id.comments);
        school = (EditText) findViewById(R.id.school);

        //check we are editing kid or adding new one
        if(editMode){
            ParseQuery<Kid> query = ParseQuery.getQuery(Kid.class).fromLocalDatastore();
            try {
                kid = query.get(editKidId);
                populateKidToViews();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else{
            kid = new Kid();
        }

        //on ImageView press
        kidImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClick();
            }
        });

        //on button press
        addSheep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(image != null){
                image.saveInBackground(new ProgressCallback() {
                   @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                   @Override
                   public void done(Integer integer) {
                       int [] progressArray = {1,25,40,70,95,100};

                       for (int i = 0; i<=progressArray.length;i++){
                           if (progressArray[i] == integer){
                               toaster("Uploading"+integer+"%","s");
                               //Snackbar.make(findViewById(R.id.activity_add_kid),"Uploading"+integer+"%",Snackbar.LENGTH_INDEFINITE).show();
                               kidSaveToServer();
                           }
                       }
                   }
               });}else{kidSaveToServer();} //make sure to save the kid even if there is no image

            }
    });}
/*
*       Other Methods rather than onCreate
*             |
*            | |
*           |___|
*           |   |
*
 */
    public void toaster(String message, @NonNull String duration){
        int toastDuration;
        if (duration.equals("s")){toastDuration = Toast.LENGTH_SHORT;}
        else{toastDuration = Toast.LENGTH_LONG;}
        Toast.makeText(getApplicationContext(),message,toastDuration).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void kidSaveToServer(){
        kid.setName(name.getText().toString());
        kid.setMobileNumber(mobileNumber.getText().toString());
        kid.setHomeNumber(homeNumber.getText().toString());
        kid.setFatherNumber(fatherNumber.getText().toString());
        kid.setMotherNumber(motherNumber.getText().toString());
        kid.setGrandNumber(grandNumber.getText().toString());
        kid.setExtraNumber1(extraNumber1.getText().toString());
        kid.setExtraNumber2(extraNumber2.getText().toString());
        kid.setBuildingNumber(building.getText().toString());
        kid.setStreetName(street.getText().toString());
        kid.setAreaName(area.getText().toString());
        kid.setFloorNumber(floor.getText().toString());
        kid.setApartmentNumber(apartment.getText().toString());
        kid.setAddressSpecialSign(specialSign.getText().toString());
        kid.setOtherAddress(otherAddress.getText().toString());
        kid.setConfessionFather(confessionFather.getText().toString());
        kid.setEmail(email.getText().toString());
        kid.setBirthday(birthday.getText().toString());
        kid.setComment(comments.getText().toString());
        kid.setSchool(school.getText().toString());
        kid.setNumberOfVisits("0");
        if(image != null){
            kid.setImage(image);
        }
        if(thumbNail != null){
            kid.setThumbNail(thumbNail);
        }

        kid.saveInBackground(new SaveCallback() {
            @Override
            public void done(@Nullable ParseException e) {
                if(e == null){
                    toaster("Saved successfully", "l");
                    //boolean isLoggedIn = true;
                    MainActivity.isLoggedIn = true;
//                    Intent intent = new Intent(AddKid.this, MainActivity.class);
//                    intent.putExtra("isLoggedIn",isLoggedIn);
//                    startActivity(intent);
                    AddKid.this.finish();
                }else{
                    toaster("Error, please try again.","l");
                }
            }
        });
    }
    public void populateKidToViews(){
        name.setText(kid.getName());
        mobileNumber.setText(kid.getMobileNumber());
        homeNumber.setText(kid.getHomeNumber());
        fatherNumber.setText(kid.getFatherNumber());
        motherNumber.setText(kid.getMotherNumber());
        grandNumber.setText(kid.getGrandNumber());
        extraNumber1.setText(kid.getExtraNumber1());
        extraNumber2.setText(kid.getExtraNumber2());
        building.setText(kid.getBuildingNumber());;
        street.setText(kid.getStreetName());
        area.setText(kid.getAreaName());
        floor.setText(kid.getFloorNumber());
        apartment.setText(kid.getApartmentNumber());
        specialSign.setText(kid.getAddressSpecialSign());
        otherAddress.setText(kid.getOtherAddress());
        confessionFather.setText(kid.getConfessionFather());
        email.setText(kid.getEmail());;
        birthday.setText(kid.getBirthday());
        comments.setText(kid.getComment());
        school.setText(kid.getSchool());
        setImageViewFromKid();

    }
    public void onImageClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddKid.this);
        builder.setCancelable(true);
        builder.setTitle("Add image");
        String [] options = {"From gallery","Take picture"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, 0);
                    }
                }else if(which == 1){
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, 1);
                        }
                }

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setImageViewFromKid(){
            if (kid.getImage()!=null) {
                kid.getImage().getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(@NonNull byte[] bytes, @Nullable ParseException e) {
                        if (e == null) {
                            Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            kidImage.setImageBitmap(bitmapImage);
                        }
                    }
                });
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*
            I noticed that whenever the Image is coming from the gallery it will be very big
            resulting in slowing down the activity showing the image
            which is not the case if coming from camera, i don't know why
            so the following code will first compress the image in case of gallery before compressing it again to jpg
            then converting it to parseFile
            ****the above text is an old text*****
            * but when on actual experience, the image files of the kids are of low size so compressing them made them blurry
            * so I returned back their compression to 100%
            * i.e:
            * bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            options.inSampleSize = 1 (instead of 4 in previous testing versions)
             */
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            byte [] byteArray = outputStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1  ;//this is line is used to compress the sizeof the image
            //if 1: no compression, 2: 1/2 of size, 3: 1/3 etc..
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length,options);
            kidImage.setImageBitmap(bitmap1);
            bitmapToParse(bitmap1);
        }else if (requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            kidImage.setImageBitmap(bitmap);
            bitmapToParse(bitmap);
        }

    }
        public void bitmapToParse(@NonNull Bitmap bitmap){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            byte [] byteArray = outputStream.toByteArray();
            image = new ParseFile("image.jpg",byteArray);
            //code for thumbNail
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = byteArray.length/50000;
            Bitmap thumbBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
            ByteArrayOutputStream thumbStream = new ByteArrayOutputStream();
            thumbBitmap.compress(Bitmap.CompressFormat.JPEG,100,thumbStream);
            byte [] thumbByteArray = thumbStream.toByteArray();
            thumbNail = new ParseFile("thumbNail.jpg",thumbByteArray);

        }
}


