package com.example.a64635.expandable;

import android.support.v7.app.AppCompatActivity;

import java.util.*;

import android.os.Bundle;
import android.app.Activity;
import android.app.ExpandableListActivity;
import android.view.Menu;
import android.widget.SimpleExpandableListAdapter;

public class MainActivity extends ExpandableListActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] keysOfGroup={"groupName"};
        String[] valuesOfGroup={"惠州候鸟","深圳光明"};
        ArrayList<HashMap<String,String>> listOfGroup=getDataList(keysOfGroup, valuesOfGroup);
        String[] keysOfChild={"childName"};
        String[] valuesOfChild1={"迎奥卡","候鸟都市精英卡"};
        String[] valuesOfChild2={"十年卡","二十年卡"};
        ArrayList<HashMap<String,String>> listOfChild1=getDataList(keysOfChild, valuesOfChild1);
        ArrayList<HashMap<String,String>> listOfChild2=getDataList(keysOfChild, valuesOfChild2);
        ArrayList<ArrayList<HashMap<String,String>>> childs=new ArrayList<ArrayList<HashMap<String,String>>> ();
        childs.add(listOfChild1);
        childs.add(listOfChild2);
        SimpleExpandableListAdapter adapter=new SimpleExpandableListAdapter(this, listOfGroup,
                R.layout.group_layout, keysOfGroup,new int[]{R.id.myGroup},
                childs,R.layout.child_layout,keysOfChild,new int[]{R.id.myChild});

        setListAdapter(adapter);


    }
    private ArrayList<HashMap<String,String>> getDataList(String[] keys, String[]values){
        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
        for(int i=0; i<values.length/keys.length; i++){
            HashMap<String,String> map=new HashMap<String,String>();
            for(int j=0;j<keys.length; j++ ){
                map.put(keys[j], values[i*keys.length+j]);
            }
            list.add(map);
        }

        return list;
    }






}
