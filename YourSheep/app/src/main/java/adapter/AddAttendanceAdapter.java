package adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import model.Kid;
import yoursheep.jesus.com.yoursheep.R;

public class AddAttendanceAdapter extends ArrayAdapter<Kid>{
    private Activity activityContext;
    private ArrayList<Kid> adapterKidList;
    private ViewHolder holder;
    public HashMap<Kid,Boolean> map;

    public AddAttendanceAdapter(Activity context, ArrayList<Kid> objects, HashMap<Kid,Boolean> editingMap) {
        super(context, 0, objects);
        activityContext = context;
        adapterKidList = objects;
        map = editingMap;

    }

    @Override
    public int getCount() {
        return adapterKidList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(activityContext);
            convertView = inflater.inflate(R.layout.attendance_row,parent,false);
            holder = new ViewHolder();
            holder.nameText = (TextView) convertView.findViewById(R.id.atRowNameText);
            holder.kidImage = (ImageView) convertView.findViewById(R.id.atRowKidImage);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.atRowChecBox);
            holder.checkBox.setChecked(map.get(adapterKidList.get(position)));
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //code for Image loading:
        if (adapterKidList.get(position).getThumbNail() != null) {
            byte[] bytes = new byte[0];
            try {
                bytes = adapterKidList.get(position).getThumbNail().getData();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            holder.kidImage.setImageBitmap(bitmapImage);
        } else {
            holder.kidImage.setImageResource(R.drawable.sheep_void);}

        //code for textView:
        holder.nameText.setText(adapterKidList.get(position).getName());

        //code for checkBox:
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                map.put(adapterKidList.get(position),isChecked);
            }
        });
        holder.checkBox.setChecked(map.get(adapterKidList.get(position)));
        return convertView;
    }
    private class ViewHolder{
        TextView nameText;
        ImageView kidImage;
        CheckBox checkBox;
    }
}