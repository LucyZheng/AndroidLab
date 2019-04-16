package com.example.a64635.calculator;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    int [] buttons;
    double result0,result1,result;
    Button buttonC,buttonplus,buttonmin,buttondiv,buttonmul,buttonequal,buttonpoint,buttonCE,buttondelete,buttonpm;
    String oldstr,newstr;
    int flag=0;
    boolean flag2=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttons=new int[]{R.id.b0,R.id.b1,R.id.b2,R.id.b3,R.id.b4,R.id.b5,R.id.b6,R.id.b7,R.id.b8,R.id.b9};
        textView=(TextView) this.findViewById(R.id.inputtext);
        oldstr=String.valueOf(textView.getText());
        newstr="";
        buttonC=(Button)findViewById(R.id.c);
        buttondiv=(Button)findViewById(R.id.div);
        buttonmin=(Button) findViewById(R.id.min);
        buttonmul=(Button) findViewById(R.id.mul);
        buttonplus=(Button) findViewById(R.id.plus);
        buttonequal=(Button) findViewById(R.id.equal);
        buttonpoint=(Button) findViewById(R.id.point);
        buttonpoint.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (flag2==true){
                    if (flag!=0){
                        newstr+=String.valueOf(((Button) v).getText());
                        textView.setText(newstr);
                    }
                    else{
                        oldstr+=String.valueOf(((Button) v).getText());
                        textView.setText(oldstr);
                    }
                }
                flag2=false;

            }
        });
        for (int i=0;i<buttons.length;++i){
            Button buttontmp=(Button) findViewById(buttons[i]);
            buttontmp.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    if(flag!=0) {
                        newstr+=String.valueOf(((Button) v).getText());
                        textView.setText(newstr);
                    }
                    else {
                        oldstr=textView.getText().toString().trim();
                        if (oldstr.equals("0")) oldstr="";
                        oldstr+=String.valueOf(((Button) v).getText());
                        textView.setText(oldstr);
                    }

                }
            });
        }
        buttonpm=(Button) findViewById(R.id.plusminus);
        buttonpm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (Double.parseDouble(textView.getText().toString().trim())>0){
                    String tmp="-"+textView.getText().toString().trim();
                    textView.setText(tmp);
                }
                else{
                    String tmp=textView.getText().toString().trim().substring(1);
                    textView.setText(tmp);
                }
            }
        });
        buttondelete =(Button) findViewById(R.id.delete);
        buttondelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int len=textView.getText().toString().length();
                String tmp=textView.getText().toString().trim().substring(0,len-1);
                textView.setText(tmp);
            }
        });

        buttonCE=(Button) findViewById(R.id.ce);
        buttonCE.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(flag!=0){
                    newstr="";
                    textView.setText("0");
                }
                else{
                    oldstr="";
                    textView.setText("0");
                }
            }
        });

        buttonC.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                textView.setText("0");
                flag2=true;
                result=0;
                result1=0;
                result0=0;
                buttonCE.setEnabled(true);
                buttonpoint.setEnabled(true);
                buttonplus.setEnabled(true);
                buttonmul.setEnabled(true);
                buttonmin.setEnabled(true);
                buttondiv.setEnabled(true);
                buttondelete.setEnabled(true);
                buttonpm.setEnabled(true);
                flag=0;
                oldstr="";
                newstr="";
                for(int j=0;j<buttons.length;++j){
                    Button buttontmp=(Button) findViewById(buttons[j]);
                    buttontmp.setEnabled(true);
                }


            }
        });
        buttontoop(buttonplus,1);
        buttontoop(buttonmin,2);
        buttontoop(buttonmul,3);
        buttontoop(buttondiv,4);
        buttonequal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                for(int j=0;j<buttons.length;++j){
                    Button buttontmp=(Button) findViewById(buttons[j]);
                    buttontmp.setEnabled(false);
                }
                buttonCE.setEnabled(false);
                buttonpoint.setEnabled(false);
                buttonplus.setEnabled(false);
                buttonmul.setEnabled(false);
                buttonmin.setEnabled(false);
                buttondiv.setEnabled(false);
                buttondelete.setEnabled(false);
                buttonpm.setEnabled(false);
                result1=Double.parseDouble(newstr);
                switch (flag){
                    case 0:
                        result=result1;
                        break;
                    case 1:
                        result=result0+result1;
                        break;
                    case 2:
                        result=result0-result1;
                        break;
                    case 3:
                        result=result0*result1;
                        break;
                    case 4:
                        if (result1!=0){
                            result=result0/result1;
                            break;
                        }
                        else{
                            Toast.makeText(MainActivity.this,"算式错误",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                String tmp=(result+"").trim();
                textView.setText(tmp);

            }

        });

    }
    public void buttontoop(Button button,final int id){
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                flag2=true;
                result0=Double.parseDouble(textView.getText().toString().trim());
                flag=id;
            }
        });
    }
}
