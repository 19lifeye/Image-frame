package com.example.admin.framesimage;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


public class Adapter extends ArrayAdapter<String>  {

    private final Activity context;
    private final  Integer[] images;



    public Adapter(Activity context, Integer[] imgid) {
        super(context, R.layout.list_image);
        this.context=context;
        this.images=imgid;
    }

    @Override
    public int getCount() {
        return images.length;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.e("Your in getView","Welcome");
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_image, null);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_img);
        imageView.setImageResource(images[position]);

        return rowView;
    }
}
