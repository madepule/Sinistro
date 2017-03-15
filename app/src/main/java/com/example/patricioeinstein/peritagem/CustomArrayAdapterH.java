package com.example.patricioeinstein.peritagem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hawkingg on 05/09/2016.
 */
public class CustomArrayAdapterH extends ArrayAdapter<Teste> {

    private static  class ViewHolder
    {
        TextView txtInfo;
    }

    public CustomArrayAdapterH(Context context, int resource, List<Teste> objects) {
        super(context, resource, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Teste info = getItem(position);

        ViewHolder viewHolder;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_arrayadapter,parent,false);
            viewHolder.txtInfo = (TextView) convertView.findViewById(R.id.txtInfo);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtInfo.setText(info.toString());
        if (position % 2 == 0)
        {
            viewHolder.txtInfo.setBackgroundColor(Color.parseColor("#ff6666"));
        }
        else
        {
            viewHolder.txtInfo.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        return convertView;
    }
}
