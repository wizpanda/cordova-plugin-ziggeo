package com.ziggeo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.ziggeo.androidsdk.Ziggeo;
import com.ziggeo.androidsdk.callbacks.IRecorderCallback;
import com.ziggeo.androidsdk.callbacks.RecorderCallback;
import com.ziggeo.androidsdk.recorder.MicSoundLevel;
import com.ziggeo.androidsdk.recorder.RecorderConfig;
import com.ziggeo.androidsdk.widgets.cameraview.CameraView;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class CameraFullscreenRecorderActivity extends AppCompatActivity {

    private static final String OPTION_TIME_LIMIT = "timelimit";
    private static final String OPTION_AUTO_RECORD = "autorecord";
    private static final String OPTION_CAMERA_FACING = "facing";

    static final String ACTION_EXIT = "ACTION_JOURNEY_EXIT";
    static final String EXTRA_RESULT = "EXTRA_RESULT";
    static final String EXTRA_RESULT_SUCCESS = "EXTRA_RESULT_SUCCESS";

    public static final String INTENT_API_TOKEN = "ziggeo-api-token";
    public static final String INTENT_OPTIONS = "ziggeo-options";

    private Ziggeo ziggeo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String appToken = intent.getStringExtra(INTENT_API_TOKEN);
        HashMap options = (HashMap) intent.getSerializableExtra(INTENT_OPTIONS);

        start(appToken, options);
    }

    public void start(String appToken, HashMap options) {
        ziggeo = new Ziggeo(appToken, this);

        RecorderConfig config = getConfig(options);

        ziggeo.setRecorderConfig(config);
        ziggeo.startCameraRecorder();
    }

    private RecorderConfig getConfig(HashMap options) {

        RecorderConfig.Builder builder = new RecorderConfig.Builder()
                .callback(prepareCallback())
                .sendImmediately(false)
                .enableCoverShot(false);

        if (options.containsKey(OPTION_CAMERA_FACING)) {
            int facing = (int) options.get(OPTION_CAMERA_FACING);

            if (facing == 1) {
                builder.facing(CameraView.FACING_FRONT);
            } else if (facing == 0) {
                builder.facing(CameraView.FACING_BACK);
            }
        }

        if (options.containsKey(OPTION_AUTO_RECORD)) {
            builder.autostartRecording((boolean) options.get(OPTION_AUTO_RECORD));
        }

        if (options.containsKey(OPTION_TIME_LIMIT)) {
            int seconds = (int) options.get(OPTION_TIME_LIMIT);
            builder.maxDuration(seconds * 1000);
        }

        return builder.build();
    }

    private void sendResult(String eventName, HashMap<String, Object> extra) {
        if (extra == null) {
            extra = new HashMap<>();
        }

        extra.put("eventName", eventName);

        Intent intent = new Intent();

        intent.putExtra(EXTRA_RESULT, extra);
        intent.putExtra(EXTRA_RESULT_SUCCESS, true);
        intent.setAction(ACTION_EXIT);

        setResult(RESULT_OK, intent);
        //finish();
    }

    private IRecorderCallback prepareCallback() {
        return new RecorderCallback() {

            @Override
            public void loaded() {
                super.loaded();
                sendResult("loaded", null);
            }

            @Override
            public void manuallySubmitted() {
                super.manuallySubmitted();
                sendResult("manually_submitted", null);
            }

            @Override
            public void uploaded(@NonNull String path, @NonNull String token) {
                super.uploaded(path, token);
                sendResult("uploaded", null);
            }

            @Override
            public void recordingStarted() {
                super.recordingStarted();
                sendResult("recording_started", null);
            }

            @Override
            public void recordingStopped(@NonNull String path) {
                super.recordingStopped(path);
                sendResult("recording_stopped", null);
            }

            @Override
            public void uploadingStarted(@NonNull String path) {
                super.uploadingStarted(path);
                sendResult("uploading_started", null);
            }

            @Override
            public void countdown(int timeLeft) {
                super.countdown(timeLeft);
                sendResult("countdown", null);
            }

            @Override
            public void recordingProgress(long time) {
                super.recordingProgress(time);
                sendResult("recording_progress", null);
            }

            @Override
            public void uploadProgress(@NonNull String videoToken, @NonNull File file, long uploaded, long total) {
                super.uploadProgress(videoToken, file, uploaded, total);

                HashMap<String, Object> data = new HashMap<>();
                data.put("videoToken", videoToken);

                sendResult("upload_progress", data);
            }

            @Override
            public void readyToRecord() {
                super.readyToRecord();
                sendResult("ready_to_record", null);
            }

            @Override
            public void accessForbidden(@NonNull List<String> permissions) {
                super.accessForbidden(permissions);
                sendResult("access_forbidden", null);
            }

            @Override
            public void accessGranted() {
                super.accessGranted();
                sendResult("access_granted", null);
            }

            @Override
            public void processing(@NonNull String token) {
                super.processing(token);

                HashMap<String, Object> data = new HashMap<>();
                data.put("videoToken", token);

                sendResult("processing", data);
            }

            @Override
            public void processed(@NonNull String token) {
                super.processed(token);

                HashMap<String, Object> data = new HashMap<>();
                data.put("videoToken", token);

                sendResult("processed", data);
            }

            @Override
            public void verified(@NonNull String token) {
                super.verified(token);

                HashMap<String, Object> data = new HashMap<>();
                data.put("videoToken", token);

                sendResult("verified", data);
            }

            @Override
            public void noCamera() {
                super.noCamera();
                sendResult("no_camera", null);
            }

            @Override
            public void noMicrophone() {
                super.noMicrophone();
                sendResult("no_microphone", null);
            }

            @Override
            public void hasCamera() {
                super.hasCamera();
                sendResult("has_camera", null);
            }

            @Override
            public void hasMicrophone() {
                super.hasMicrophone();
                sendResult("has_microphone", null);
            }

            @Override
            public void microphoneHealth(@NonNull MicSoundLevel level) {
                super.microphoneHealth(level);

                HashMap<String, Object> data = new HashMap<>();
                data.put("level", level.name());

                sendResult("microphone_health", data);
            }

            @Override
            public void canceledByUser() {
                super.canceledByUser();
                sendResult("canceled_by_user", null);
            }

            @Override
            public void error(@NonNull Throwable throwable) {
                super.error(throwable);
                sendResult("error", null);
            }

            @Override
            public void onPictureTaken(@NonNull String path) {
                super.onPictureTaken(path);
                sendResult("on_picture_taken", null);
            }
        };
    }
}
