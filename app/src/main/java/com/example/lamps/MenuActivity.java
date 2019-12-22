package com.example.lamps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    int mode=3,radius,backcolor,space,color, max_in_height,max_in_width;
    int[] RGB_back;
    boolean cheatmode=false, guaranteedwin=true;
    static int height,width;
    RelativeLayout menuLayout;
    public static final String APP_Settings = "settings";
    private SharedPreferences mSettings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        //Устанавливаем сохраненные настройки
        mSettings = getSharedPreferences(APP_Settings, MODE_PRIVATE);
        setSettings();
        //Меняем задний фон
        menuLayout = (RelativeLayout) findViewById(R.id.menu_layout);
        RGB_back=convertToArrayColor(backcolor);
        menuLayout.setBackgroundColor(Color.rgb(RGB_back[0],RGB_back[1],RGB_back[2]));

        max_in_height =height/(2*radius+space);
        max_in_width =width/(2*radius+space);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            //Когда мы вернулись из настроек:
            radius=data.getIntExtra("radius",50);
            mode=data.getIntExtra("mode",3);
            color=data.getIntExtra("color",0);
            space=data.getIntExtra("space",50);
            backcolor=data.getIntExtra("back_color",0);
            cheatmode=data.getBooleanExtra("cheat",false);
            guaranteedwin=data.getBooleanExtra("win",true);
            max_in_height=data.getIntExtra("height",0);
            max_in_width=data.getIntExtra("width",0);
            setSettings();
            RGB_back=convertToArrayColor(backcolor);
            menuLayout.setBackgroundColor(Color.rgb(RGB_back[0],RGB_back[1],RGB_back[2]));
        }
    }
    //Обработчик кнопок
        public void menuClick(View v) {
            Intent i;
            switch (v.getId()) {
                case R.id.play_button:
                    //Играть
                    i = new Intent(MenuActivity.this, MainActivity.class);
                    i.putExtra("radius",radius);
                    i.putExtra("mode",mode);
                    i.putExtra("color",color);
                    i.putExtra("back_color",backcolor);
                    i.putExtra("win",guaranteedwin);
                    i.putExtra("cheat",cheatmode);
                    i.putExtra("space",space);
                    if (max_in_height==0)max_in_height =height/(2*radius+space);
                    if (max_in_width==0)max_in_width =width/(2*radius+space);
                    i.putExtra("height",max_in_height);
                    i.putExtra("width",max_in_width);
                    startActivity(i);
                    break;
                case R.id.settings:
                    //В настройки
                    i = new Intent(MenuActivity.this, SettingsActivity.class);
                    i.putExtra("radius",radius);
                    i.putExtra("mode",mode);
                    i.putExtra("color",color);
                    i.putExtra("back_color",backcolor);
                    i.putExtra("win",guaranteedwin);
                    i.putExtra("cheat",cheatmode);
                    i.putExtra("space",space);
                    if (max_in_height==0)max_in_height =height/(2*radius+space);
                    if (max_in_width==0)max_in_width =width/(2*radius+space);
                    i.putExtra("height",max_in_height);
                    i.putExtra("width",max_in_width);
                    startActivityForResult(i,1);
                    break;
                case R.id.out:
                    //Выход
                    Toast.makeText(this,"Пока!",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case R.id.about:
                    //Об игре
                    i = new Intent(MenuActivity.this, AboutActivity.class);
                    startActivity(i);
                    break;
            }
        }
        //Конвертер числа в массив с цветми. Всего цветов 306, а дальше повторяются
        public static int[] convertToArrayColor(int progress){
            int[] RGB=new int[3];
            boolean plus = true;
            int m = 2;
            RGB[0] = 255;

            for (int i = 0; i < progress /51; i++) {
                if (plus) RGB[m] = 255;
                else RGB[m] = 0;
                m = (m + 1) % 3;
                plus = !plus;
            }
            if (plus) RGB[m] = (progress%51)*5;
            else RGB[m] = 255 - (progress%51)*5;
            return RGB;
        }
        public void setSettings(){
            //Установка из настроек
            radius=mSettings.getInt("radius",50);
            space=mSettings.getInt("space",25);
            backcolor=mSettings.getInt("back_color",153);
            color=mSettings.getInt("color",102);
            cheatmode=mSettings.getBoolean("cheat",false);
            guaranteedwin=mSettings.getBoolean("win",true);
        }
}
