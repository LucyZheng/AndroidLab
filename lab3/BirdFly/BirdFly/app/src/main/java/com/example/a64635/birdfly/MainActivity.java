package com.example.a64635.birdfly;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.delay;
import static android.R.attr.fingerprintAuthDrawable;
import static android.R.attr.revisionCode;
import static java.lang.StrictMath.abs;

public class MainActivity extends AppCompatActivity {
    private boolean isright = true, record = false, play = false, respond = true;
    private int imagex,imagey;
    private float nowx,nowy;
    private ImageView imageView,imageView2;
    private AnimationDrawable recording, birdfly;
    private PathView pathView;
    private ArrayList<Pos> list = new ArrayList<>();
    private Pos last_position;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.birdimg);
        imageView2=(ImageView) findViewById(R.id.recordimg);
        pathView = (PathView) findViewById(R.id.path_view);
        relativeLayout=(RelativeLayout) findViewById(R.id.content_main);
        birdfly = (AnimationDrawable) imageView.getBackground();
        recording = (AnimationDrawable) imageView2.getBackground();
        birdfly.start();
        recording.start();
    }
    public boolean onTouchEvent(MotionEvent event) {
        final int maxheight=relativeLayout.getHeight(),maxwidth=relativeLayout.getWidth();
        imagex=imageView.getWidth();
        imagey=imageView.getHeight();
        
        // 在这里判断一下如果是按下操作就获取坐标然后执行方法
        if (event.getAction() == MotionEvent.ACTION_DOWN && !play && respond) {
            nowx=imageView.getX();
            nowy=imageView.getY();
            respond = false;
            float oldx,oldy;
            final float X,Y,newx,newy;
            if (event.getX() + imagex/2 > maxwidth) oldx=maxwidth - imagex/2;else oldx=event.getX();
            if (event.getY() - 100 + imagey/2 > maxheight) oldy=maxheight - imagey/2;else oldy=event.getY() - 100;
            if (oldx < imagex/2) X=imagex; else X=oldx;
            if (oldy < imagex/2) Y=imagey; else Y=oldy;
            newx = X - nowx - imagex/2;
            newy = Y - nowy - imagey/2;
            if (newx < 0 && isright) {
                isright = false;
                ObjectAnimator rotateY = ObjectAnimator.ofFloat(imageView, "rotationY", 0, 180);
                rotateY.setDuration(500);
                rotateY.start();
            }
            else if (newx > 0 && !isright) {
                isright = true;
                ObjectAnimator rotateY = ObjectAnimator.ofFloat(imageView, "rotationY", 180, 0);
                rotateY.setDuration(500);
                rotateY.start();
            }
            TranslateAnimation animation = new TranslateAnimation(0, newx, 0, newy);
            animation.setStartOffset(500);
            double maxLength = Math.sqrt(Math.pow(maxheight - imagey, 2) + Math.pow(maxwidth - imagex, 2));
            animation.setDuration(Double.valueOf(5000 * (Math.sqrt(Math.pow(newx, 2) + Math.pow(newy, 2)) / maxLength)).longValue());
            imageView.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    imageView.clearAnimation();
                    imageView.setX(X - imagex/2);
                    imageView.setY(Y - imagey/2);
                    nowx=imageView.getX();
                    nowy=imageView.getY();
                    if (record) {
                        Pos pos = new Pos(nowx, nowy, isright);
                        list.add(pos);
                    }
                    respond = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        return super.onTouchEvent(event);
    }

    private void play(Pos firstPos, final int inoldx) {
        final int maxheight=relativeLayout.getHeight(),maxwidth=relativeLayout.getWidth();
        if (inoldx >= list.size()) {
            return;
        }
        final Pos nextPos = list.get(inoldx);
        pathView.set(firstPos, nextPos, imagex, imagey);
        float newx = nextPos.getx() - firstPos.getx();
        float newy = nextPos.gety() - firstPos.gety();
        if (newx < 0 && isright) {
            isright = false;
            ObjectAnimator rotateY = ObjectAnimator.ofFloat(imageView, "rotationY", 0, 180);
            rotateY.setDuration(500);
            rotateY.start();
        }
        else if (newx > 0 && !isright) {
            isright = true;
            ObjectAnimator rotateY = ObjectAnimator.ofFloat(imageView, "rotationY", 180, 0);
            rotateY.setDuration(500);
            rotateY.start();
        }
        TranslateAnimation animation = new TranslateAnimation(0, newx, 0, newy);
        animation.setStartOffset(500);
        double maxLength = Math.sqrt(Math.pow(maxheight - imagey, 2) + Math.pow(maxwidth - imagex, 2));
        double speed = Math.sqrt(Math.pow(newx, 2) + Math.pow(newy, 2)) / maxLength;
        animation.setDuration(Double.valueOf(5000 * speed).longValue());
        imageView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.clearAnimation();
                imageView.setX(nextPos.getx());
                imageView.setY(nextPos.gety());
                play(nextPos, inoldx + 1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.start) {
            imageView2.setVisibility(View.VISIBLE);
            record = true;
            list.clear();
            list.add(new Pos(nowx, nowy, isright));
        }
        if (id == R.id.end) {
            imageView2.setVisibility(View.GONE);
            record = false;
        }
        if (id == R.id.replay && !record) {
            pathView.clear();
            play = true;
            last_position = new Pos(nowx, nowy, isright);
            if (list.size() != 0) {
                Pos firstPos = list.get(0);
                imageView.setX(firstPos.getx());
                imageView.setY(firstPos.gety());
                if (isright != firstPos.isisRight())
                    if (firstPos.isisRight()) imageView.setRotationY(0);else imageView.setRotationY(180);
                isright = firstPos.isisRight();
                play(firstPos, 1);
            }
        }
        if (id == R.id.end_replay) {
            pathView.clear();
            imageView.clearAnimation();
            imageView.setX(last_position.getx());
            imageView.setY(last_position.gety());
            if (isright != last_position.isisRight())
                if (last_position.isisRight())
                    imageView.setRotationY(0);
                else
                    imageView.setRotationY(180);
            isright = last_position.isisRight();
            play = false;
        }
        return super.onOptionsItemSelected(item);
    }


}
