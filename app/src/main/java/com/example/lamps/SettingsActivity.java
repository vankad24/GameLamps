package com.example.lamps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    int[] RGB,RGB_back;
    SeekBar seekRadius,seekHeight,seekWidth,seekColor;
    int mode,radius,height,width,space,max_in_height,max_in_width,backcolor,color,progressHeight,progressWidth;
    TextView radius_progress,color_progress,field_size;
    boolean guaranteedwin,cheatmode;
    String sw,sh;
    ScrollView background;
    DrawExample drawExample;
    RadioGroup radioGroup;
    SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Устанавливаем цвета и начальные значения
        color = getIntent().getIntExtra("color",0);
        RGB=MenuActivity.convertToArrayColor(color);
        setContentView(R.layout.settings_activity);

        mSettings = getSharedPreferences(MenuActivity.APP_Settings, MODE_PRIVATE);

        field_size= (TextView) findViewById(R.id.field_size);
        color_progress = (TextView) findViewById(R.id.colorBar_progress);
        radius_progress = (TextView) findViewById(R.id.radiusBar_progress);
        seekRadius = (SeekBar) findViewById(R.id.seekBar_radius);
        seekHeight = (SeekBar) findViewById(R.id.seekBar_height);
        seekWidth = (SeekBar) findViewById(R.id.seekBar_width);
        seekColor = (SeekBar) findViewById(R.id.seekBar_color);
        background = (ScrollView) findViewById(R.id.settings);
        drawExample = (DrawExample) findViewById(R.id.example);

        radius=getIntent().getIntExtra("radius",50);
        mode=getIntent().getIntExtra("mode",3);
        space=getIntent().getIntExtra("space",50);
        backcolor=getIntent().getIntExtra("back_color",10);
        cheatmode=getIntent().getBooleanExtra("cheat",false);
        guaranteedwin=getIntent().getBooleanExtra("win",true);
        progressHeight = getIntent().getIntExtra("height",0);
        progressWidth = getIntent().getIntExtra("width",0);

        radioGroup = (RadioGroup)findViewById(R.id.rGroup);
        switch (mode){
            case 1:
                radioGroup.check(R.id.butmod1);
                break;
            case 2:
                radioGroup.check(R.id.butmod2);
                break;
            case 3:
                radioGroup.check(R.id.butmod3);
                break;
        }

        RGB_back=MenuActivity.convertToArrayColor(backcolor);
        background.setBackgroundColor(Color.rgb(RGB_back[0],RGB_back[1],RGB_back[2]));

        height=MenuActivity.height;
        width=MenuActivity.width;

        max_in_height =height/((2*radius+space)*MenuActivity.indexOfDisplay);
        max_in_width =width/((2*radius+space)*MenuActivity.indexOfDisplay);

        seekWidth.setOnSeekBarChangeListener(this);
        seekHeight.setOnSeekBarChangeListener(this);
        seekRadius.setOnSeekBarChangeListener(this);
        seekColor.setOnSeekBarChangeListener(this);

        //Передвигаем на нужные значения все seekbar
        seekColor.setProgress(color);
        seekHeight.setMax(max_in_height-1);
        seekWidth.setMax(max_in_width-1);
        seekRadius.setProgress(radius-30);
        seekHeight.setProgress(progressHeight-1);
        seekWidth.setProgress(progressWidth-1);

        radius_progress.setText(String.valueOf(radius));
        color_progress.setText(String.format("R:%03d G:%03d B:%03d",RGB[0],RGB[1],RGB[2]));
        field_size.setText((seekWidth.getProgress()+1)+"x"+(seekHeight.getProgress()+1));
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rGroup);
        Button save = (Button) findViewById(R.id.save_button);

        final Intent i = new Intent(this, MenuActivity.class);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.butmod1:
                        mode = 1;
                        break;
                    case R.id.butmod2:
                        mode = 2;
                        break;
                    case R.id.butmod3:
                        mode = 3;
                        break;
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Сохраняем настройки, уходим обратно
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt("radius", radius);
                editor.putInt("space",space);
                editor.putInt("back_color",backcolor);
                editor.putInt("color",seekColor.getProgress());
                editor.putBoolean("cheat",cheatmode);
                editor.putBoolean("win",guaranteedwin);
                editor.apply();

                finishActivity(1);
                i.putExtra("radius",radius);
                i.putExtra("mode",mode);
                i.putExtra("color",seekColor.getProgress());
                i.putExtra("back_color",backcolor);
                i.putExtra("win",guaranteedwin);
                i.putExtra("cheat",cheatmode);
                i.putExtra("space",space);
                i.putExtra("height",seekHeight.getProgress()+1);
                i.putExtra("width",seekWidth.getProgress()+1);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //Когда передвигаем какой-то из seekbar
        switch (seekBar.getId()) {
            case R.id.seekBar_radius:
                radius = seekBar.getProgress() + 30;
                radius_progress.setText(String.valueOf(radius));
                if (max_in_height!=height/((2*radius+space)*MenuActivity.indexOfDisplay)) max_in_height = height/((2*radius+space)*MenuActivity.indexOfDisplay);
                if (max_in_width!=width/((2*radius+space)*MenuActivity.indexOfDisplay)) max_in_width =width/((2*radius+space)*MenuActivity.indexOfDisplay);
                seekHeight.setMax(max_in_height-1);
                seekHeight.setProgress(max_in_height-1);
                seekWidth.setMax(max_in_width-1);
                seekWidth.setProgress(max_in_width-1);
                break;
            case R.id.seekBar_height:
                sh = Integer.toString(seekBar.getProgress()+1);
                field_size.setText(sw+"x"+sh);
                break;
            case R.id.seekBar_width:
                sw = Integer.toString(seekBar.getProgress()+1);
                field_size.setText(sw+"x"+sh);
                break;
            case R.id.seekBar_color:
                RGB=MenuActivity.convertToArrayColor(seekBar.getProgress());
                String s =String.format("R:%03d G:%03d B:%03d",RGB[0],RGB[1],RGB[2]);
                color_progress.setText(s);
                break;
        }
        //Перерисовываем наш пример
        drawExample.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    //Создаем меню(3 точки сверху)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.first_menu,menu);
        menu.findItem(R.id.about_menu_item).setIntent(new Intent(SettingsActivity.this, AboutActivity.class));
        menu.findItem(R.id.extra_settings).setIntent(new Intent(this, ExtraSettingsActivity.class));
        menu.findItem(R.id.reset_settings).setIntent(new Intent(this, MenuActivity.class));
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
            case R.id.extra_settings:
                //В доп настройки
                item.getIntent().putExtra("space",space);
                item.getIntent().putExtra("cheat",cheatmode);
                item.getIntent().putExtra("win",guaranteedwin);
                item.getIntent().putExtra("back_color",backcolor);
                startActivityForResult(item.getIntent(),1);
                break;
            case R.id.reset_settings:
                SharedPreferences.Editor editor = mSettings.edit();
                editor.clear();//Удаление настроек
                editor.apply();
                finishActivity(1);
                setResult(RESULT_OK,item.getIntent());
                finish();
                break;
        }
        return true;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Данные из доп настроек
        if (resultCode==RESULT_OK){
            space=data.getIntExtra("space",50);
            cheatmode=data.getBooleanExtra("cheat",false);
            backcolor=data.getIntExtra("back_color",0);
            guaranteedwin=data.getBooleanExtra("win",true);
            RGB_back=MenuActivity.convertToArrayColor(backcolor);
            background.setBackgroundColor(Color.rgb(RGB_back[0],RGB_back[1],RGB_back[2]));

        }
    }
}