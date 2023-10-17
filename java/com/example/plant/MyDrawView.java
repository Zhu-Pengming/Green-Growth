package com.example.plant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyDrawView extends View {

    Paint paint;
    private RectF rect;
    private boolean isDrawing;
    private Bitmap bitmap;
    Path path;

    public MyDrawView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        rect = new RectF();
        isDrawing = false;
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(rect, paint);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                rect.left = x;
                rect.top = y;
                isDrawing = true;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isDrawing) {
                    rect.right = x;
                    rect.bottom = y;
                    path.reset();
                    path.addRect(rect, Path.Direction.CW);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                break;
            default:
                return false;
        }
        return true;
    }
    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }


}
