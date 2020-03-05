package com.ziggeo;

import android.content.Context;
import android.support.annotation.NonNull;
import com.ziggeo.androidsdk.Ziggeo;
import com.ziggeo.androidsdk.callbacks.IRecorderCallback;
import com.ziggeo.androidsdk.callbacks.RecorderCallback;
import com.ziggeo.androidsdk.recorder.MicSoundLevel;
import com.ziggeo.androidsdk.recorder.RecorderConfig;
import com.ziggeo.androidsdk.widgets.cameraview.CameraView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraFullscreenRecorder {

    private static final String OPTION_TIME_LIMIT = "timelimit";
    private static final String OPTION_AUTO_RECORD = "autorecord";
    private static final String OPTION_CAMERA_FACING = "facing";

    private Ziggeo ziggeo;
    private Context context;
    private CallbackContext callbackContext;

    CameraFullscreenRecorder(Context context, CallbackContext callbackContext) {
        this.context = context;
        this.callbackContext = callbackContext;
    }

    public void start(String appToken, JSONObject options) throws JSONException {
        ziggeo = new Ziggeo(appToken, context);

        RecorderConfig config = getConfig(options);

        ziggeo.setRecorderConfig(config);
        ziggeo.startCameraRecorder();
    }

    private RecorderConfig getConfig(JSONObject options) throws JSONException {

        RecorderConfig.Builder builder = new RecorderConfig.Builder()
                .callback(prepareCallback())
                .sendImmediately(false)
                .enableCoverShot(false);

        if (options.has(OPTION_CAMERA_FACING)) {
            int facing = options.getInt(OPTION_CAMERA_FACING);

            if (facing == 1) {
                builder.facing(CameraView.FACING_FRONT);
            } else if (facing == 0) {
                builder.facing(CameraView.FACING_BACK);
            }
        }

        if (options.has(OPTION_AUTO_RECORD)) {
            builder.autostartRecording(options.getBoolean(OPTION_AUTO_RECORD));
        }

        if (options.has(OPTION_TIME_LIMIT)) {
            builder.maxDuration(options.getInt(OPTION_TIME_LIMIT) * 1000);
        }

        return builder.build();
    }

    private void sendResult(String eventName, Map eventData) {
        JSONObject message = new JSONObject();

        try {
            message.put("eventName", eventName);
            if (eventData != null) {
                message.put("eventData", eventData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PluginResult result = new PluginResult(PluginResult.Status.OK, message);
        this.callbackContext.sendPluginResult(result);
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
