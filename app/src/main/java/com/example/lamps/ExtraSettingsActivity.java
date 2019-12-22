package com.example.lamps;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ExtraSettingsActivity extends AppCompatActivity implements  SeekBar.OnSeekBarChangeListener, Switch.OnCheckedChangeListener  {
    Switch switchCheat,switchGuaranteedWin;
    SeekBar seekSpase,seekBackcolor;
    TextView space_progress,color_progress;
    ScrollView background;
    Button save;
    Intent i;
    int space,backcolor;
    int[] RGB_background;
    boolean cheatmode,guaranteedwin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extrasettings_activity);
        //Устанавливаем цвета и начальные значения
        space=getIntent().getIntExtra("space",50);
        backcolor=getIntent().getIntExtra("back_color",10);
        cheatmode=getIntent().getBooleanExtra("cheat",false);
        guaranteedwin=getIntent().getBooleanExtra("win",true);
        switchCheat=(Switch)findViewById(R.id.cheatmode);
        switchGuaranteedWin =(Switch)findViewById(R.id.guaranteed);
        seekSpase= (SeekBar)findViewById(R.id.seekSpace);
        seekBackcolor=(SeekBar)findViewById(R.id.seekBackcolor);
        space_progress = (TextView) findViewById(R.id.space);
        color_progress = (TextView) findViewById(R.id.backcolor);
        background = (ScrollView) findViewById(R.id.color_extra_settings);
        save=(Button) findViewById(R.id.button_save);

        seekBackcolor.setOnSeekBarChangeListener(this);
        seekSpase.setOnSeekBarChangeListener(this);
        switchGuaranteedWin.setOnCheckedChangeListener(this);
        switchCheat.setOnCheckedChangeListener(this);
        //Передвигаем на нужные значения все seekbar и switch
        seekSpase.setProgress(space-5);
        seekBackcolor.setProgress(backcolor);
        switchCheat.setChecked(cheatmode);
        switchGuaranteedWin.setChecked(guaranteedwin);


        i = new Intent(this, SettingsActivity.class);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishActivity(1);
                i.putExtra("space",space);
                i.putExtra("cheat",cheatmode);
                i.putExtra("win",guaranteedwin);
                i.putExtra("back_color",seekBackcolor.getProgress());

                setResult(RESULT_OK,i);
                finish();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cheatmode:
                cheatmode=isChecked;
                break;
            case R.id.guaranteed:
                guaranteedwin=isChecked;
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.seekSpace:
                space=seekBar.getProgress()+5;
                space_progress.setText(String.valueOf(space));
                break;
            case R.id.seekBackcolor:
                RGB_background=MenuActivity.convertToArrayColor(seekBar.getProgress());
                String s =String.format("R:%03d G:%03d B:%03d",RGB_background[0],RGB_background[1],RGB_background[2]);
                color_progress.setText(s);
                background.setBackgroundColor(Color.rgb(RGB_background[0],RGB_background[1],RGB_background[2]));
                break;
        }
    }



    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
