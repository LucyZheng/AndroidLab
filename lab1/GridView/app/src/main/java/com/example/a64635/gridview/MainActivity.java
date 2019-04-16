package com.example.a64635.gridview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.grid_photo);
        int image[] = { R.drawable.cwt, R.drawable.dlrb, R.drawable.lh, R.drawable.wjk, R.drawable.zly, R.drawable.ym, R.drawable.zyx,};
        //图标下的文字
        String name[]={"陈伟霆","迪丽热巴","鹿晗","王俊凯","赵丽颖","杨幂","张艺兴"};
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <image.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", image[i]);
            map.put("text", name[i]);
            dataList.add(map);
            String[] str1 = {"img", "text"};
            int[] str2 = {R.id.img, R.id.text};
            adapter = new SimpleAdapter(this, dataList, R.layout.gridview_item, str1, str2);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Toast.makeText(MainActivity.this,"Short Click:"+dataList.get(arg2).get("text").toString(),Toast.LENGTH_SHORT).show();
                }
            });
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Toast.makeText(MainActivity.this,"Long Click:"+dataList.get(arg2).get("text").toString(),Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
    }
}

