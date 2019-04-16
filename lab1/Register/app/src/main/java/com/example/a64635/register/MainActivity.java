package com.example.a64635.register;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editText,editText2;
    Switch switch1;
    RadioButton radiogroup[];
    CheckBox cb[];RadioButton rButton;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=(Button) findViewById(R.id.button);
        editText=(EditText) findViewById(R.id.edit_text);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        editText2=(EditText) findViewById(R.id.edit_text2);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        spinner=(Spinner) findViewById(R.id.spinner1);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.colleges, R.layout.personal_spinner);
        spinner.setAdapter(adapter);
        button.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){

        switch (v.getId()){

            case R.id.button:
                String inputText ="用户名："+editText.getText().toString()+"\n";
                inputText += "密码："+editText2.getText().toString();
                inputText+="\n爱好：";
                cb = new CheckBox[3];
                cb[0]=(CheckBox) findViewById(R.id.box1);
                cb[1]=(CheckBox) findViewById(R.id.box2);
                cb[2]=(CheckBox) findViewById(R.id.box3);
                if (cb[0].isChecked()) inputText+="体育 ";
                if (cb[1].isChecked()) inputText+="音乐 ";
                if (cb[2].isChecked()) inputText+="绘画 ";
                inputText+="\n年级：";
                RadioButton rab1=(RadioButton) findViewById(R.id.rb1);
                RadioButton rab2 =(RadioButton) findViewById(R.id.rb2);
                if (rab1.isChecked()) inputText+="低年级";
                else if (rab2.isChecked()) inputText+="高年级";
                inputText+="\n学院：";
                Spinner spinner=(Spinner)findViewById(R.id.spinner1);
                inputText+=spinner.getSelectedItem().toString();
                inputText+="\n全日制学生：";
                switch1=(Switch)findViewById(R.id.switch1);
                if(switch1.isChecked()) inputText+="是" ;else inputText+="否";
                Toast.makeText(MainActivity.this,inputText,Toast.LENGTH_SHORT).show();
                break;
            default:break;
        }
    }

}

