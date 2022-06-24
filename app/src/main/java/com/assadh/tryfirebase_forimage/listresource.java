package com.assadh.tryfirebase_forimage;

import static java.sql.Types.TIMESTAMP;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

//import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class listresource extends ArrayAdapter<Postdata> {

    public listresource(@NonNull Context context, List<Postdata> list) {
        super(context, 0,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View convertView1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageitems,null);
        ImageView imgpost= convertView1.findViewById(R.id.imageView);
        TextView txtdescription= convertView1.findViewById(R.id.textdescription);
        TextView txtusername=convertView1.findViewById(R.id.textusername);
        TextView txtDate=convertView1.findViewById(R.id.textDate);
        ImageView imgprofile=convertView1.findViewById(R.id.imageViewprofile);
       Postdata data=getItem(position);

//        if(data.getUri().equals("#"))
//          imgpost.setImageBitmap(data.getImagePost());
//        else
        Glide.with(parent.getContext()).load(data.getUri()).into(imgpost);
//        Glide.with(getContext()).load(data.getUriimageprofile()).into(imgprofile);
       txtdescription.setText(data.getDescription());
       txtusername.setText(data.getUsername());
        SimpleDateFormat formatter = new SimpleDateFormat("EEE,MMM, hh:mm a");
        String dateString = formatter.format(new Date(data.getDate().getSeconds()));
       txtDate.setText(dateString);
       return convertView1;
    }
}
