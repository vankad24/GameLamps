package com.example.lamps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DrawMenu extends View {

    int r = 50, ro = 25, width, height;
    double touchX = -100, touchY = -100;
    byte[][] lamps = new byte[4][3];
    long lastTime = 0, nowTime;
    boolean start=true,win=false;

    public DrawMenu(Context context) {
        super(context);
    }
    MenuActivity a;
    public DrawMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        a=(MenuActivity) context;
    }
    Paint paint1 = new Paint();
    Paint paint2 = new Paint();
    Paint stroke = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        height = canvas.getHeight();
        width = canvas.getWidth();
        MenuActivity.height=height-166; //-166 т.к. еще ActionBar сверху
        MenuActivity.width=width;
        stroke.setStrokeWidth(4);


        stroke.setStyle(Paint.Style.STROKE);
        paint1.setStyle(Paint.Style.FILL);
        paint2.setStyle(Paint.Style.FILL);
        paint1.setAntiAlias(true);   //Сглаживание
        paint2.setAntiAlias(true);
        stroke.setAntiAlias(true);

        if (start)start();
        //Рисуем кружки по углам
        for (int i = 0; i <lamps.length ; i++) {
            int first_circle=r + ro, second_circle=3*r+2*ro,shiftX=0,shiftY=0;
            switch (i){
                case 0:
                    stroke.setColor(Color.RED);
                    paint1.setColor(Color.RED);
                    paint2.setColor(Color.RED);
                break;
                case 1:
                    stroke.setColor(Color.YELLOW);
                    paint1.setColor(Color.YELLOW);
                    paint2.setColor(Color.YELLOW);
                    shiftX=width;
                break;
                case 2:
                    stroke.setColor(Color.BLUE);
                    paint1.setColor(Color.BLUE);
                    paint2.setColor(Color.BLUE);
                    shiftY=height;
                break;
                case 3:
                    stroke.setColor(Color.GREEN);
                    paint1.setColor(Color.GREEN);
                    paint2.setColor(Color.GREEN);
                    shiftX=width;
                    shiftY=height;
                break;
            }
            paint1.setAlpha(70);
            canvas.drawCircle(Math.abs(shiftX-first_circle),Math.abs(shiftY-second_circle), r, stroke);
            if (lamps[i][0]==0) canvas.drawCircle(Math.abs(shiftX-first_circle),Math.abs(shiftY-second_circle), r, paint1);
            else canvas.drawCircle(Math.abs(shiftX-first_circle),Math.abs(shiftY-second_circle), r, paint2);
            canvas.drawCircle(Math.abs(shiftX-first_circle),Math.abs(shiftY-first_circle), r, stroke);
            if (lamps[i][1]==0) canvas.drawCircle(Math.abs(shiftX-first_circle),Math.abs(shiftY-first_circle), r, paint1);
            else canvas.drawCircle(Math.abs(shiftX-first_circle),Math.abs(shiftY-first_circle), r, paint2);
            canvas.drawCircle(Math.abs(shiftX-second_circle),Math.abs(shiftY-first_circle), r, stroke);
            if (lamps[i][2]==0) canvas.drawCircle(Math.abs(shiftX-second_circle),Math.abs(shiftY-first_circle), r, paint1);
            else canvas.drawCircle(Math.abs(shiftX-second_circle),Math.abs(shiftY-first_circle), r, paint2);
        }

        checkWin();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        nowTime = System.currentTimeMillis();
        if (event.getAction() == MotionEvent.ACTION_DOWN && nowTime - lastTime > 200) {//задержка на 200 милисек
            touchX = event.getX();
            touchY = event.getY();
            lastTime = nowTime;
            if ((touchX < 2 * (2 * r + ro) || touchX > width - 2 * (2 * r + ro)) && (touchY < 2 * (2 * r + ro) || touchY > height - 2 * (2 * r + ro)))
                statusChange();
            invalidate();
        }
        return true;
    }
    //Изменить состояние на противоположное
    void statusChange() {
        int corner;
        if (touchX < 2 * (2 * r + ro))
            if (touchY < 2 * (2 * r + ro))
                corner = 0;
            else corner = 2;
        else if (touchY < 2 * (2 * r + ro))
            corner = 1;
        else corner = 3;

        int line = (int) (corner == 0 || corner == 1 ? touchY / (2 * r + ro) : (height - touchY) / (2 * r + ro));
        int stolb = (int) (corner == 0 || corner == 2 ? touchX / (2 * r + ro) : (width - touchX) / (2 * r + ro));

        int index_circle;
        if (line != 1 || stolb != 1) {
            int x0= (corner == 0 || corner == 2? stolb * (2 * r + ro) + r + ro:width -(stolb * (2 * r + ro) + r + ro));
            int y0= (corner == 0 || corner == 1?line * (2 * r + ro) + r + ro:height-(line * (2 * r + ro) + r + ro));

            if (line == 1 && stolb == 0)index_circle=0;
            else  if (line == 0 && stolb == 1)index_circle=2;
            else index_circle=1;
            if (Math.pow(x0 - touchX, 2) + Math.pow(y0 - touchY, 2) <= r * r) {
                lamps[corner][index_circle] = (byte) Math.abs(lamps[corner][index_circle] - 1);
                if(index_circle<lamps[0].length-1)
                    lamps[corner][index_circle+1] = (byte) Math.abs(lamps[corner][index_circle+1] - 1);
                if(index_circle>0)
                    lamps[corner][index_circle-1] = (byte) Math.abs(lamps[corner][index_circle-1] - 1);
            }
        }
    }
    //Проверить выигрыш
    public void checkWin() {
          win = true;
        for (int i = 0; i < lamps.length; i++) {
            for (int j = 0; j < lamps[i].length; j++) {
                if (lamps[i][j] == 0) {
                    win = false;
                    break;
                }
            }
            if (!win) break;
        }
        if (win) Toast.makeText(a,"Ах ты чертов гений!",Toast.LENGTH_SHORT).show();
    }
    //Начальное состояние лампочек
    void start(){
        for (int i = 0; i <lamps.length ; i++) {
            for (int j = 0; j <lamps[0].length ; j++) {
                if (j!=1)
                    lamps[i][j]=1;
            }
        }
        start=false;
    }
}
