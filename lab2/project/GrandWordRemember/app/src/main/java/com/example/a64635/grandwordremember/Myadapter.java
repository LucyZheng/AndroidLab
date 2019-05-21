package com.example.a64635.grandwordremember;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.checked;

/**
 * Created by 64635 on 2019/5/20.
 */

public class Myadapter extends BaseAdapter {
    private ArrayList<WordRec> list;
    private Context mContext;
    private SparseIntArray ifcheck=new SparseIntArray();
    LayoutInflater inflater;
    Map<Object, ArrayList<Object>> map = new HashMap<>();
    Map<String, String> sel = new HashMap<>();



    public Myadapter(ArrayList<WordRec> list, Context mContext) {
        super();
        this.list = list;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    public String trans(String tmp2){
        String tmp="";
        int flag=0;
        for (int i=0;i<tmp2.length();++i){
            if (tmp2.substring(i,i+1).equals("[")) flag=1;
            if (tmp2.substring(i,i+1).equals("]")) {
                flag=0;
                continue;
            }
            if (flag==0){
                tmp+=tmp2.substring(i,i+1);
            }
        }
        return tmp;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.rg = (RadioGroup) convertView.findViewById(R.id.ex);
            holder.rb1 = (RadioButton) convertView.findViewById(R.id.ex1);
            holder.rb2 = (RadioButton) convertView.findViewById(R.id.ex2);
            holder.rb3 = (RadioButton) convertView.findViewById(R.id.ex3);
            holder.rb4 = (RadioButton) convertView.findViewById(R.id.ex4);
            holder.tv = (TextView) convertView.findViewById(R.id.word);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ArrayList<Object> list2 = new ArrayList<>();
        if (!map.containsKey(position)) {
            list2.add(position);
            for (int i = 0; i < 3; ++i) {
                int num = position;
                while (list2.contains(num)) num = (int) (Math.random() * 10);
                list2.add(num);
            }
            Collections.shuffle(list2);
            map.put(position, list2);
        }
        else
            list2 = map.get(position);
        holder.tv.setText(list.get(position).getWord());
        holder.rb1.setText(trans(list.get((int)list2.get(0)).getExplanation()));
        holder.rb2.setText(trans(list.get((int)list2.get(1)).getExplanation()));
        holder.rb3.setText(trans(list.get((int)list2.get(2)).getExplanation()));
        holder.rb4.setText(trans(list.get((int)list2.get(3)).getExplanation()));
        holder.rg.setOnCheckedChangeListener(null);
        holder.rg.clearCheck();
        if(ifcheck.indexOfKey(position)!=-1)  holder.rg.check(ifcheck.get(position));
        else holder.rg.clearCheck();
        holder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup rg,int index){
                if (index>-1){
                    ifcheck.put(position,index);
                }
                else if(ifcheck.indexOfKey(position) > -1) ifcheck.removeAt(ifcheck.indexOfKey(position));
            }
        });

        return convertView;
    }

    public class ViewHolder{
        RadioGroup rg;
        RadioButton rb1;
        RadioButton rb2;
        RadioButton rb3;
        RadioButton rb4;
        TextView tv;
    }
}
