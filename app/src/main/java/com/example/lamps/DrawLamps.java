package com.example.lamps;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.Random;

public class DrawLamps extends View {
    int lampX, lampY,n, m;
    long lastTime = 0, winTime = 0,startTime=0, nowTime;
    int r,mode, ro = 25, seconds;
    boolean start = true, win, cheatmode=false,cheat,guaranteedwin;
    byte lamps[][];
    double touchX, touchY;
    int[] RGB;
    public DrawLamps(Context context) {
        super(context);
    }
    public DrawLamps(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //Берем переменные из MainActivity
        MainActivity a = (MainActivity) context;
        mode=a.mode;
        r=a.radius;
        RGB=a.RGB;
        cheat=a.cheatmode;
        guaranteedwin=a.guaranteedwin;
        ro=a.space;
        n=a.max_in_height;
        m=a.max_in_width;
    }
    Paint paint1 = new Paint();
    Paint paint2 = new Paint();
    Paint stroke = new Paint();
    Bitmap background_win,image_win;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        stroke.setStyle(Paint.Style.STROKE);
        paint1.setStyle(Paint.Style.FILL);
        paint2.setStyle(Paint.Style.FILL);

        stroke.setAntiAlias(true);
        paint1.setAntiAlias(true);//Сглаживание
        stroke.setStrokeWidth(4);
        stroke.setColor(Color.rgb(RGB[0],RGB[1],RGB[2]));
        paint1.setColor(Color.rgb(RGB[0],RGB[1],RGB[2]));
        paint2.setColor(Color.argb(70,RGB[0],RGB[1],RGB[2]));
        
        if (start) start();

        lampX = r + ro;
        lampY = r + ro;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                    canvas.drawCircle(lampX, lampY, r, stroke);
                if (lamps[i][j] == 1)
                    canvas.drawCircle(lampX, lampY, r, paint1);
                else
                    canvas.drawCircle(lampX, lampY, r, paint2);
                lampX += 2 * r + ro;
            }
            lampY += 2 * r + ro;
            lampX = r + ro;
        }
        checkWin(canvas);
    }
    //Начальное значение лампочек. Генерация зависит от guaranteedwin(гарантированного выигрыша)
    public void start() {
        lamps = new byte[n][m];
        Random rand = new Random();
        if (guaranteedwin){
            for (int i = 0; i <lamps.length ; i++)
                for (int j = 0; j < lamps[0].length; j++)
                    lamps[i][j]=1;
        }
        for (int i = 0; i < lamps.length; i++) {
            for (int j = 0; j < lamps[i].length; j++) {
                int state = rand.nextInt(10000)%2;
                if (guaranteedwin){
                    if (state==1)
                        statusChange(i,j,true);
                    }
                else lamps[i][j] = (byte) (state);
            }
        }
        start = false;
        win = false;
        startTime=System.currentTimeMillis();
    }

    public void checkWin(Canvas canvas) {
        Resources res = this.getResources();
        //Проверяем выиграли ли мы
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
        if (win) {
            Random rand = new Random();
            //Берем случайную картинку
            switch (rand.nextInt(10000) % 3) {
                case 0:
                    image_win = BitmapFactory.decodeResource(res, R.drawable.win1);
                    break;
                case 1:
                    image_win = BitmapFactory.decodeResource(res, R.drawable.win2);
                    break;
                case 2:
                    image_win = BitmapFactory.decodeResource(res, R.drawable.win3);
                    break;
            }
            //Выводим изображения
            background_win = BitmapFactory.decodeResource(res, R.drawable.background_win);
            background_win = Bitmap.createScaledBitmap(background_win, 550, 800, false);
            image_win = Bitmap.createScaledBitmap(image_win, 500, 500, false);
            canvas.drawBitmap(background_win, (canvas.getWidth() - 550) / 2, 30, null);
            canvas.drawBitmap(image_win, (canvas.getWidth() - 550) / 2 + 25, 170, null);
            //Переводим и выводим время в мин и сек
            paint1.setTextSize(40);
            winTime = System.currentTimeMillis();
            seconds=(int) (winTime-startTime)/1000;
            canvas.drawText(seconds/60+" мин "+seconds%60+" сек",250,780,paint1);
            start();
        }

    }
    //Проверяем точку касания
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        nowTime = System.currentTimeMillis();
        if (event.getAction() == MotionEvent.ACTION_DOWN && nowTime - lastTime > 200) {//Задержка
            touchX = event.getX();
            touchY = event.getY();
            lastTime = nowTime;
            if (touchX > m * (2 * r + ro) || touchY > n * (2 * r + ro)) {
                if (cheat) cheatmode = !cheatmode;}
            else statusChange((int) (touchY / (2 * r + ro)), (int) (touchX / (2 * r + ro)), false);
            invalidate();
        }
        return true;
    }

    //Изменить состояние у нужной лампочки. imitate_click используется при гарантированном выигрыше
    void statusChange(int line,int stolb, boolean imitate_click) {
        double x0, y0;
        x0 = stolb * (2 * r + ro) + r + ro;
        y0 = line * (2 * r + ro) + r + ro;
        if (imitate_click||Math.pow(x0 - touchX, 2) + Math.pow(y0 - touchY, 2) <= r * r) {
            lamps[line][stolb] = (byte) Math.abs(lamps[line][stolb] - 1);
            if (!cheatmode) {
                switch (mode) {
                    case 1:
                        for (int i = 0; i < lamps.length; i++)
                            lamps[i][stolb] = (byte) Math.abs(lamps[i][stolb] - 1);
                        for (int i = 0; i < lamps[line].length; i++)
                            lamps[line][i] = (byte) Math.abs(lamps[line][i] - 1);
                        break;
                    case 2:
                        if (line > 0 && stolb < lamps[0].length - 1)
                            lamps[line - 1][stolb + 1] = (byte) Math.abs(lamps[line - 1][stolb + 1] - 1);
                        if (line < lamps.length - 1 && stolb < lamps[0].length - 1)
                            lamps[line + 1][stolb + 1] = (byte) Math.abs(lamps[line + 1][stolb + 1] - 1);
                        if (line > 0 && stolb > 0)
                            lamps[line - 1][stolb - 1] = (byte) Math.abs(lamps[line - 1][stolb - 1] - 1);
                        if (line < lamps.length - 1 && stolb > 0)
                            lamps[line + 1][stolb - 1] = (byte) Math.abs(lamps[line + 1][stolb - 1] - 1);
                        break;
                    case 3:
                        if (line < lamps.length - 1)
                            lamps[line + 1][stolb] = (byte) Math.abs(lamps[line + 1][stolb] - 1);
                        if (line > 0)
                            lamps[line - 1][stolb] = (byte) Math.abs(lamps[line - 1][stolb] - 1);
                        if (stolb < lamps[0].length - 1)
                            lamps[line][stolb + 1] = (byte) Math.abs(lamps[line][stolb + 1] - 1);
                        if (stolb > 0)
                            lamps[line][stolb - 1] = (byte) Math.abs(lamps[line][stolb - 1] - 1);
                        break;

                }
            }

        }
    }
}
