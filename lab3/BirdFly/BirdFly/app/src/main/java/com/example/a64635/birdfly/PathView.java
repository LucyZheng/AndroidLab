package com.example.a64635.birdfly;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class PathView extends View {
    private Paint paint = new Paint();
    private Path path = new Path();
    private Context context;

    public PathView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }
    public void set(Pos pos1, Pos pos2, int width, int height) {
        path.moveTo(pos1.getx() + width / 2, pos1.gety() + height / 2);
        path.lineTo(pos2.getx() + width / 2, pos2.gety() + height / 2);
        invalidate();
    }

    public void clear() {
        path.reset();
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        paint.setAlpha(100);
        canvas.drawPath(path, paint);
    }
}
