package ru.wildleaf.liedetectorprank.app;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.io.IOException;

import ru.wildleaf.liedetectorprank.app.R;

/**
 * Created by Данил on 04.07.2014.
 */
public class VoiceIndicator extends FrameLayout {

    private Context context;
    private MediaRecorder mRecorder = null;
    private Boolean recordProcess = false;
    private ProgressBar voiceLevelIndicator;
    private Integer maxDiff = 1;

    public VoiceIndicator(Context context) {
        this(context, null);
    }

    public VoiceIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.addView(inflater.inflate(R.layout.voice_volume, null));

        voiceLevelIndicator = (ProgressBar) this.findViewById(R.id.voiceLevel);

    }

    public void start() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile("/dev/null");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("MIC", "prepare() failed");
        }

        mRecorder.start();

        recordProcess = true;

        if (mRecorder != null) {
            new ShowMicVolume().execute();
        }
    }

    public void stop() {
        recordProcess = false;
        mRecorder.stop();
    }

    public void release() {
        if (mRecorder != null) {
            recordProcess = false;
            mRecorder.release();
            mRecorder = null;
        }
    }

    private class ShowMicVolume extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            recordProcess = true;
            Integer curMicVolume = 0;
            Integer timeDiff = 10;
            Integer coefMic = 10;
            Integer iterMic = 0;
            Integer newMicVolume = 0;
            Integer diff = 0;
            while (recordProcess) {
                try {
                    Thread.sleep(timeDiff);
                    if (iterMic == coefMic) {
                        newMicVolume = new Integer(mRecorder.getMaxAmplitude());
                        diff = (newMicVolume - curMicVolume) / coefMic;
                        iterMic = 0;
                    }
                    curMicVolume += diff;
                    voiceLevelIndicator.setProgress(curMicVolume);
                    iterMic += 1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
