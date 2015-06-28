package ru.wildleaf.liedetectorprank.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import ru.wildleaf.liedetectorprank.app.R;

public class MainMenu extends FullScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        // кнопка включения,выключения звука
        final ToggleButton soundButton = (ToggleButton) findViewById(R.id.main_menu_sound);
        soundButton.setChecked(getSoundStatus());
        soundButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    soundOn();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sound_on), Toast.LENGTH_SHORT).show();
                } else {
                    soundOff();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sound_off), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // написать письмо
        ImageButton main_menu_mail = (ImageButton) findViewById(R.id.main_menu_mail);
        main_menu_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", getResources().getString(R.string.supportemail), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.write)));
            }
        });

        // ссылка на маркет
        ImageButton main_menu_like = (ImageButton) findViewById(R.id.main_menu_like);
        main_menu_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.linktomarket) + getPackageName()));
                startActivity(intent);
            }
        });

        // кнопка настроек
        ImageButton main_menu_settings = (ImageButton) findViewById(R.id.main_menu_settings);
        main_menu_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainMenu.this, Settings.class);
                MainMenu.this.startActivity(settingsIntent);
            }
        });

        // Поиск AdView как ресурса и отправка запроса.
        AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")
                .addTestDevice("E19CEB37DD1B2F8B58041434E321385C")
                .addTestDevice("752839EE1FAE4643FC3BFEAE3575CBCD")
                .addTestDevice("67C892B846DC43B8BA0784B330C66476").build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public void startTest(View view) {
        playSound(R.raw.button);
        Integer viewId = view.getId();
        Intent menuButton = new Intent(MainMenu.this, BeginTest.class);
        menuButton.putExtra("buttonid", viewId);
        MainMenu.this.startActivity(menuButton);
    }
}
