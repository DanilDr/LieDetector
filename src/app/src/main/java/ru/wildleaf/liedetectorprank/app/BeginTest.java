package ru.wildleaf.liedetectorprank.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;

import ru.wildleaf.liedetectorprank.app.R;

public class BeginTest extends FullScreenActivity {

    private CameraPreview cameraPreview = null;
    private Boolean startTest = false;
    private VoiceIndicator voiceIndicator;
    private Boolean process = true;
    private View mainTestLayout;
    private Integer testTypeButton;
    private AdRequest adRequest;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        testTypeButton = getIntent().getIntExtra("buttonid", 0);

        switch (testTypeButton) {
            case R.id.main_menu_voice:
                mainTestLayout = (View) getLayoutInflater().inflate(R.layout.begin_test_voice, null);
                setContentView(mainTestLayout);

                final RelativeLayout startstop_voicerecord = (RelativeLayout) findViewById(R.id.startstop_voicerecord);
                final ACLineText startstop_voicerecordtext = (ACLineText) findViewById(R.id.startstop_voicerecord_text);
                voiceIndicator = (VoiceIndicator) findViewById(R.id.voiceLevelIndicator);

                startstop_voicerecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (startTest == false) {
                            startTest = true;
                            startstop_voicerecordtext.setText(R.string.finish);
                            voiceIndicator.start();
                        } else {

                            startstop_voicerecordtext.setText(R.string.start);
                            startTest = false;
                            voiceIndicator.stop();
                            // переход на экран обработки
                            startProcessing(1);
                        }
                    }
                });

                break;
            case R.id.main_menu_photo:
                mainTestLayout = (View) getLayoutInflater().inflate(R.layout.begin_test_photo, null);
                setContentView(mainTestLayout);
                RelativeLayout buttonFotoTest = (RelativeLayout) findViewById(R.id.buttonFotoTest);
                buttonFotoTest.setOnClickListener(new ClickStartProcessing(2));
                break;
            case R.id.main_menu_handprint:
                mainTestLayout = (View) getLayoutInflater().inflate(R.layout.begin_test_fingerwrite, null);
                setContentView(mainTestLayout);
                RelativeLayout buttonFingewriteTest = (RelativeLayout) findViewById(R.id.buttonFingewriteTest);
                buttonFingewriteTest.setOnClickListener(new ClickStartProcessing(3));
                break;
            case R.id.main_menu_fingerprint:
                mainTestLayout = (View) getLayoutInflater().inflate(R.layout.begin_test_fingerprint, null);
                setContentView(mainTestLayout);
                final ProgressBar fingetprintProgress = (ProgressBar) findViewById(R.id.fingetprintProgress);
                final RelativeLayout fingerprintArea = (RelativeLayout) findViewById(R.id.fingerprintArea);

                new FingerprintProcessing(fingetprintProgress, fingerprintArea).execute();

                break;
        }

        // Поиск AdView как ресурса и отправка запроса.
        adView = (AdView) this.findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")
                .addTestDevice("E19CEB37DD1B2F8B58041434E321385C")
                .addTestDevice("752839EE1FAE4643FC3BFEAE3575CBCD")
                .addTestDevice("67C892B846DC43B8BA0784B330C66476").build();
        adView.loadAd(adRequest);

    }

    private class FingerprintProcessing extends AsyncTask<String, Integer, String> {

        private ProgressBar fingetprintProgress;
        private RelativeLayout fingerprintArea;
        private Integer progress = 0;
        private Boolean progressStart = false;

        public FingerprintProcessing(ProgressBar fingetprintProgress, RelativeLayout fingerprintArea) {
            this.fingetprintProgress = fingetprintProgress;
            this.fingerprintArea = fingerprintArea;
            progress = 0;
            setProgress();
            fingerprintArea.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        progressStart = true;
                        playSound(R.raw.scanner);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        progressStart = false;
                        progress = 0;
                        setProgress();
                        stopSound();
                    }
                    return true;
                }
            });

        }

        @Override
        protected String doInBackground(String... params) {
            while (progress <= 100) {
                if (progressStart == true) {
                    try {
                        Thread.sleep(50);
                        setProgress();
                        progress += 1;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        private void setProgress() {
            fingetprintProgress.setProgress(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            startProcessing(4);
        }
    }

    private class ClickStartProcessing implements View.OnClickListener {

        private Integer typeTest;

        public ClickStartProcessing(Integer typeTest) {
            super();
            this.typeTest = typeTest;
        }

        @Override
        public void onClick(View v) {
            startProcessing(typeTest);
        }
    }


    /* входной параметр typeTest
        1 - тест по голосу
        2 - тест по фото
        3 - тест по рукописному тексту
        4 - тест по отпечатку пальца
     */
    private void startProcessing(final Integer typeTest) {
        if (typeTest == 2) {
            playSound(R.raw.fotospusk);
        }

        cameraPreview = (CameraPreview) findViewById(R.id.cameraPreview);
        if (cameraPreview != null) {
            cameraPreview.release();
        }

        final View processingView = (View) getLayoutInflater().inflate(R.layout.processing, null);
        processingView.setAlpha(0.3f);
        mainTestLayout.animate().alpha(0.2f).setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime)).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setContentView(processingView);
                processingView.animate().alpha(1f).setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime)).setListener(null);
                ImageView processingImage = (ImageView) findViewById(R.id.processingImage);
                AnimationDrawable frameAnimation = (AnimationDrawable) processingImage.getDrawable();
                frameAnimation.start();
            }
        });

        Handler mHandler = new Handler();

        Runnable runmhandler = new Runnable() {

            @Override
            public void run() {
                processingView.animate().alpha(0.2f).setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime)).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Random rand = new Random();
                        super.onAnimationEnd(animation);
                        final View completeProcessingView = (View) getLayoutInflater().inflate(R.layout.complete_processing, null);
                        completeProcessingView.setAlpha(0.3f);
                        setContentView(completeProcessingView);
                        completeProcessingView.animate().alpha(1f).setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime)).setListener(null);
                        // текст - заголовок
                        ACLineText completeTitleText = (ACLineText) findViewById(R.id.completeTitleText);
                        switch (typeTest) {
                            case 1:
                                completeTitleText.setText(R.string.the_man_whose_voice_recorded_tells);
                                break;
                            case 2:
                                completeTitleText.setText(R.string.human_on_this_picture_tells);
                                break;
                            case 3:
                                completeTitleText.setText(R.string.the_man_who_wrote_the_text_tells);
                                break;
                            case 4:
                                completeTitleText.setText(R.string.the_man_who_scanned_fingerprint_tells);
                                break;
                        }

                        // правда / ложь

                        Boolean trueLieResult = rand.nextBoolean();
                        ACLineText completeTrueOrLie = (ACLineText) findViewById(R.id.completeTrueOrLie);
                        if (trueLieResult == true) {
                            completeTrueOrLie.setText(R.string.test_result_true);
                        } else {
                            completeTrueOrLie.setText(R.string.test_result_lie);
                        }
                        // процент
                        Integer completePercentageRandom = new Integer(rand.nextInt(100));
                        ACLineText completePercentageNum = (ACLineText) findViewById(R.id.completePercentageNum);
                        ProgressBar trueLieProgressBar = (ProgressBar) findViewById(R.id.trueLieProgressBar);
                        completePercentageNum.setText(completePercentageRandom.toString() + '%');
                        trueLieProgressBar.setProgress(completePercentageRandom);

                        // кнопка возврата в главное меню
                        RelativeLayout buttonTryAgain = (RelativeLayout) findViewById(R.id.buttonTryAgain);
                        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent startBeginTest = new Intent(BeginTest.this, BeginTest.class);
                                startBeginTest.putExtra("buttonid", testTypeButton);
                                BeginTest.this.startActivity(startBeginTest);
                            }
                        });
                        // кнопка начала теста заново
                        RelativeLayout buttonBackToMenu = (RelativeLayout) findViewById(R.id.buttonBackToMenu);
                        buttonBackToMenu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent startMainMenu = new Intent(BeginTest.this, MainMenu.class);
                                BeginTest.this.startActivity(startMainMenu);
                            }
                        });

                        // Поиск AdView как ресурса и отправка запроса.
                        adView = (AdView) findViewById(R.id.adView);
                        adView.loadAd(adRequest);

                    }
                });
            }
        };

        mHandler.postDelayed(runmhandler, 5000);

    }

    @Override
    protected void onPause() {
        adView.pause();
        super.onPause();
        if (cameraPreview != null) {
            cameraPreview.release();
        }
        if (voiceIndicator != null) {
            voiceIndicator.release();
            voiceIndicator = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cameraPreview != null) {
            cameraPreview.release();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    public void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent startMainMenu = new Intent(BeginTest.this, MainMenu.class);
        BeginTest.this.startActivity(startMainMenu);
    }

}
