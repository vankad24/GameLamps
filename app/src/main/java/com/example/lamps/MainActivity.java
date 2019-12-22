package com.example.lamps;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    int mode,radius,backcolor,space,color,max_in_height,max_in_width;
    int[] RGB,RGB_back;
    boolean cheatmode,guaranteedwin;
    DrawLamps drawLamps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Устанавливаем цвета и начальные значения
        color=getIntent().getIntExtra("color",0);
        RGB=MenuActivity.convertToArrayColor(color);

        max_in_height=getIntent().getIntExtra("height",0);
        max_in_width=getIntent().getIntExtra("width",0);

        mode = getIntent().getIntExtra("mode",3);
        radius = getIntent().getIntExtra("radius",50);
        space= getIntent().getIntExtra("space",25);
        RGB_back=MenuActivity.convertToArrayColor(backcolor);
        cheatmode=getIntent().getBooleanExtra("cheat",false);
        guaranteedwin=getIntent().getBooleanExtra("win",true);
        setContentView(R.layout.activity_main);
        backcolor=getIntent().getIntExtra("back_color",0);
        RGB_back=MenuActivity.convertToArrayColor(backcolor);
        LinearLayout background = (LinearLayout) findViewById(R.id.backgroundgame);
        background.setBackgroundColor(Color.rgb(RGB_back[0],RGB_back[1],RGB_back[2]));
        drawLamps = (DrawLamps)findViewById(R.id.draw_game) ;
    }
    //Создаем меню(3 точки сверху)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.second_menu,menu);
        menu.findItem(R.id.about_menu_item).setIntent(new Intent(MainActivity.this, AboutActivity.class));
        return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.about_menu_item:
                startActivity(item.getIntent());
                break;
            case R.id.back:
                finish();
                break;
            case R.id.replay:
                drawLamps.start(); //Перезапускаем DrawLamps
                drawLamps.invalidate();
                break;
        }
        return true;
    }
}


