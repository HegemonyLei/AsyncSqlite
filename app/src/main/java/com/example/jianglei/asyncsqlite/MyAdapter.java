package com.example.jianglei.asyncsqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jianglei.ormsqlitedemo.R;

import java.util.List;

/**
 * Created by jianglei on 2016/4/29.
 */
public class MyAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater inflater;

    private List<Info> infos;

    public MyAdapter(Context context, List<Info> infos) {
        this.context = context;
        this.infos = infos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);
            holder.nameText = (TextView) convertView.findViewById(R.id.name_text);
            holder.ageText = (TextView) convertView.findViewById(R.id.age_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameText.setText(infos.get(position).getName());
        holder.ageText.setText(infos.get(position).getAge());
        return convertView;
    }

    class ViewHolder {
        TextView nameText;
        TextView ageText;
    }
}
