package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import model.Kid;
import yoursheep.jesus.com.yoursheep.R;
import yoursheep.jesus.com.yoursheep.Splash;

/**
 * Created by aanwer on 11/12/2016.
 */

public class KidAdapter extends ArrayAdapter<Kid> {
    private Activity activityContext;
//    private int layoutResourceId = R.layout.list_row;
    //private LayoutInflater inflater;
    private ArrayList<Kid> kidList;
    private LayoutInflater inflater;
    private Bitmap bitmapImage;
    public KidAdapter(@NonNull Activity context, @NonNull ArrayList<Kid> objects) {
        super(context, 0, objects);
        activityContext = context;
        kidList = objects;
    }

    @Override
    public int getCount() {
        return kidList.size();
    }

    @Nullable
    @Override
    public Kid getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(Kid item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Kid kid = kidList.get(position);
        final int totalAttendances = (int) Splash.totalAttendances;

        if (convertView == null){
            inflater = LayoutInflater.from(activityContext);
            convertView = inflater.inflate(R.layout.list_row,parent,false);

            holder  = new ViewHolder();
            holder.statusFrame = (ImageView) convertView.findViewById(R.id.imageFrame);
            holder.kidThumb = (ImageView) convertView.findViewById(R.id.rowKidImage);
            holder.kidName = (TextView) convertView.findViewById(R.id.rowNameText);
            holder.callButton = (Button) convertView.findViewById(R.id.callButton);
            holder.address = (TextView) convertView.findViewById(R.id.rowAddressText);
            holder.numberOfVisits = (TextView) convertView.findViewById(R.id.rowVisitText);
            holder.lastTimeText = (TextView) convertView.findViewById(R.id.rowLastTime);
            holder.percentage = (TextView) convertView.findViewById(R.id.percentText);
            holder.attReport = (TextView) convertView.findViewById(R.id.attendanceReport);

                convertView.setTag(holder);

            }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //convert Parse file to bitmap and pass it to the image view:
        if (kid.getThumbNail()!=null){
            byte [] bytes = new byte[0];
            try {
                bytes = kid.getThumbNail().getData();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            holder.kidThumb.setImageBitmap(bitmapImage);
        }else{
            holder.kidThumb.setImageResource(R.drawable.sheep_void);
        }
        holder.kidName.setText(kid.getName());
        String addressString = kid.getBuildingNumber()+"ش "+kid.getStreetName()+" "
                +"الدور "+kid.getFloorNumber()+" شقة: "+kid.getApartmentNumber()+"\n"
                +kid.getAreaName()+"\n"+kid.getAddressSpecialSign();
        holder.address.setText(addressString);
        holder.numberOfVisits.setText(kid.getNumberOfVisits());
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context currentContext = getContext();
                if(kid.getMobileNumber()!=null && !kid.getMobileNumber().equals("")){
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+kid.getMobileNumber()));
                currentContext.startActivity(intent);}
                else{
                    Toast.makeText(currentContext,"Sheep has no mobile number saved, " +
                            "check his details for more numbers",Toast.LENGTH_LONG).show();
                }
            }
        });
        if(kid.getLastTimeAttendance()){
            holder.lastTimeText.setText("Yes");
            holder.lastTimeText.setTextColor(activityContext.getResources().getColor(R.color.greenAttendance));
        }else{
            holder.lastTimeText.setText("No");
            holder.lastTimeText.setTextColor(activityContext.getResources().getColor(R.color.redAttendance));
        }
        String attReportString;
        if (kid.getNumberOfAttendances() != null){
            double kidTotalAttendance = Integer.parseInt(kid.getNumberOfAttendances());
            int percentage =(int) Math.round((kidTotalAttendance/ totalAttendances)*100);
            holder.percentage.setText(String.valueOf(percentage) + "%");
            holder.statusFrame.setImageResource(colorSelector(percentage));
            attReportString = "Attendance \n    ("+kid.getNumberOfAttendances()+"/"+totalAttendances+")";}
           else{
            attReportString = "Attendance \n    ("+"0"+"/"+totalAttendances+")";
            holder.percentage.setText("0%");
            holder.statusFrame.setImageResource(R.drawable.red);
        }
            holder.attReport.setText(attReportString);
        return convertView;
        }
    private int colorSelector(int percentage){
      int resource = 0;

        if(percentage <= 75 && percentage > 50){resource = R.drawable.yellow;}
        else if(percentage <= 50){ resource = R.drawable.red;}
        else if(percentage > 75){ resource = R.drawable.green;}
        return resource;
    }
    private class ViewHolder{
        ImageView statusFrame;
        ImageView kidThumb;
        TextView kidName;
        Button callButton;
        TextView address;
        TextView numberOfVisits;
        TextView lastTimeText;
        TextView percentage;
        TextView attReport;
    }
}



