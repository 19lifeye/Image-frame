package com.example.admin.framesimage;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by admin on 09-Sep-17.
 */

public class TextAdapter extends ArrayAdapter<String>
{
    String [] fontresss;
    Context context;

    public TextAdapter(Context context, String []frontres) {
        super(context,R.layout.textview);
        this.context=context;
        this.fontresss=frontres;
    }

    @Override
    public int getCount() {
        return fontresss.length;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent)
    {
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.textview, null);
        TextView textView=(TextView)rowView.findViewById(R.id.Textdisplay);
       textView.setText("ABC");
        Typeface faceee=Typeface.createFromAsset(getContext().getAssets(),fontresss[position]);
        textView.setTypeface(faceee);
        return rowView;

    }
}
