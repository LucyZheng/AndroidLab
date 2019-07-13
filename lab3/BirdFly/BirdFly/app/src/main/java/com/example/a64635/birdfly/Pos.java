package com.example.a64635.birdfly;

/**
 * Created by 64635 on 2019/7/13.
 */

public class Pos {
    private float x, y;
    private boolean isright;
    public boolean isisRight() {
        return isright;
    }
    public float getx() {
        return x;
    }
    public float gety() {
        return y;
    }
    public Pos(float x, float y, boolean isright) {
        this.x = x;
        this.y = y;
        this.isright = isright;
    }

}
