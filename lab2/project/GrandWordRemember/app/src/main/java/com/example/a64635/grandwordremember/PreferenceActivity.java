package com.example.a64635.grandwordremember;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import static com.example.a64635.grandwordremember.R.id.toolbar;

public class PreferenceActivity extends AppCompatActivity {

    // private SettingFragment fg;  https://blog.csdn.net/lincyang/article/details/20609673
    // private FragmentManager fManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Bundle bundle;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setTitle("学霸背单词");
        toolbar.setSubtitle("系统设置");
        FragmentManager fManager = getFragmentManager();

        FragmentTransaction fTransaction = fManager.beginTransaction();
        //if(fg == null){
            SettingFragment fg = new SettingFragment();
            bundle=new Bundle();
            bundle.putString("data", "第一个Fragment");
            fg.setArguments(bundle);
            fTransaction.add(R.id.ly_content,fg);
       // }else{
         //   fTransaction.show(fg);
        //}
        fTransaction.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analysis, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
        }
        else super.onOptionsItemSelected(item);
        return true;
    }
}
