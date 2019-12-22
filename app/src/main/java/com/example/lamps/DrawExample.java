package com.example.lamps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawExample extends View {

    long nowTime,lastTime;
    int heignt,width,x0,y0,radius;
    int[] RGB;
    boolean circle_status=true;
    SettingsActivity a;
    public DrawExample(Context context) {
        super(context);
    }
    public DrawExample(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        a =(SettingsActivity) context;

    }
    Paint paint1=new Paint();
    Paint paint2=new Paint();
    Paint stroke=new Paint();
    Paint rect=new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RGB = a.RGB;
        radius=a.radius;
        heignt = canvas.getHeight();
        width = canvas.getWidth();
        x0=width/2;
        y0=heignt/2;
        rect.setStrokeWidth(4);

        RectF rectF = new RectF(3,3,width-3,heignt-3);
        rect.setAntiAlias(true);
        rect.setColor(Color.WHITE);
        rect.setStyle(Paint.Style.FILL);
        //Рисуем задний фон
        canvas.drawRoundRect(rectF,50,50,rect);
        rect.setColor(Color.GRAY);
        rect.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF,50,50,rect);

        stroke.setStyle(Paint.Style.STROKE);
        paint1.setStyle(Paint.Style.FILL);
        paint2.setStyle(Paint.Style.FILL);

        stroke.setStrokeWidth(4);
        stroke.setAntiAlias(true);
        stroke.setColor(Color.rgb(RGB[0],RGB[1],RGB[2]));
        paint1.setColor(Color.rgb(RGB[0],RGB[1],RGB[2]));
        paint2.setColor(Color.argb(70,RGB[0],RGB[1],RGB[2]));
        //Рисуем круг
        canvas.drawCircle(x0,y0,radius,stroke);
        if (circle_status) canvas.drawCircle(x0,y0,radius,paint1);
        else canvas.drawCircle(x0,y0,radius,paint2);
    }
    //Проверяем точку касания
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        nowTime = System.currentTimeMillis();
        if (event.getAction() == MotionEvent.ACTION_DOWN && nowTime - lastTime > 200) {
            lastTime=nowTime;
            float touchX = event.getX();
            float touchY = event.getY();

            if (Math.pow(x0 - touchX, 2) + Math.pow(y0 - touchY, 2) <= radius * radius)
                circle_status = !circle_status;

            invalidate();
        }
        return true;
    }
}

