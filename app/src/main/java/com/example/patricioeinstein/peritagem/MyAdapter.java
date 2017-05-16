package com.example.patricioeinstein.peritagem;

/**
 * Created by Hawkingg on 16/05/2017.
 */

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<ImageView> {

    ArrayList<ImageView> animalList = new ArrayList<>();

    public MyAdapter(Context context, int textViewResourceId, ArrayList<ImageView> objects) {
        super(context, textViewResourceId, objects);
        animalList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.grid_view_items, null);
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) animalList.get(position).getDrawable());
        ImageView imageView = (ImageView) v.findViewById(R.id.imageViewf);
        imageView.setBackground(bitmapDrawable);
        return v;

    }

}