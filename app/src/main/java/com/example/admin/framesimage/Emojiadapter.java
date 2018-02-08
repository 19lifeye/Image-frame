package com.example.admin.framesimage;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Created by ADMIN on 9/8/2017.
 */

public class Emojiadapter extends ArrayAdapter<String> {

    private final Activity context;
    private final Integer[] emojis;


    public  Emojiadapter (Activity context, Integer[] emoji) {
        super(context, R.layout.list_image);
        this.context=context;
        this.emojis=emoji;



    }

    @Override
    public int getCount() {
        return emojis.length;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.e("Your in getView","Welcome");
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_image, null);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_img);
        imageView.setImageResource(emojis[position]);

        return rowView;
    }
}

