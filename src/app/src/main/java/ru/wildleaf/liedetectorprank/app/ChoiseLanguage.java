package ru.wildleaf.liedetectorprank.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import ru.wildleaf.liedetectorprank.app.R;


public class ChoiseLanguage extends FullScreenActivity {

    private Map<Integer, String> choiseLanguageImage;
    private Handler mHandler = new Handler();
    private NotificationControl notificationControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedlang == null) {
            setContentLayout(R.layout.choise_lang, false);
            choiseLanguageImage = new HashMap<Integer, String>();

            choiseLanguageImage.put(R.id.choiseLangRus, "ru");
            choiseLanguageImage.put(R.id.choiseLangEng, "en");
            choiseLanguageImage.put(R.id.choiseLangEsp, "es");
            choiseLanguageImage.put(R.id.choiseLangKor, "ko");

            for (final Map.Entry<Integer, String> entry : choiseLanguageImage.entrySet()) {
                ImageView curFlag = (ImageView) findViewById(entry.getKey());

                curFlag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String langStr = entry.getValue();
                        setLanguage(langStr);
                        saveLanguage(langStr);
                        setContentLayout(R.layout.start_screen, true);
                    }
                });
            }
        } else {
            setContentLayout(R.layout.start_screen, true);
        }

        notificationControl = new NotificationControl(this);

        if (settings.getBoolean("notific", true) == true) {
            notificationControl.switchOn();
        }
    }

    private void setContentLayout(int layoutId, Boolean showMenu) {
        super.setContentView(layoutId);

        if (showMenu == true) {
            final Runnable runmhandler;

            runmhandler = new Runnable() {

                @Override
                public void run() {
                    Intent choiseLang = new Intent(ChoiseLanguage.this, MainMenu.class);
                    ChoiseLanguage.this.startActivity(choiseLang);
                }
            };

            mHandler.postDelayed(runmhandler, 3000);
        }


    }
}
