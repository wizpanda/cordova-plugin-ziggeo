package com.ziggeo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.ziggeo.androidsdk.Ziggeo;
import com.ziggeo.androidsdk.callbacks.IRecorderCallback;
import com.ziggeo.androidsdk.callbacks.RecorderCallback;
import com.ziggeo.androidsdk.recorder.MicSoundLevel;
import com.ziggeo.androidsdk.recorder.RecorderConfig;
import com.ziggeo.androidsdk.ui.theming.CameraRecorderStyle;
import com.ziggeo.androidsdk.widgets.cameraview.CameraView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class which parses the options received from JS layer and launches the full screen Ziggeo
 * recorder.
 *
 * @author Shashank Agrawal
 */
public class CameraFullscreenRecorder {

    private static final String TAG = "ZiggeoCordovaPlugin";

    private static final String OPTION_TIME_LIMIT = "timeLimit";
    private static final String OPTION_AUTO_RECORD = "autoRecord";
    private static final String OPTION_CAMERA_FACING = "facing";
    private static final String OPTION_MANUAL_SUBMIT = "manualSubmit";
    private static final String OPTION_DISABLE_SWITCH = "disableCameraSwitch";
    private static final String OPTION_START_DELAY = "countdown";       // In seconds
    private static final String OPTION_CUSTOM_DATA = "customData";
    private static final String OPTION_DATA_TITLE = "title";
    private static final String OPTION_HIDE_CONTROL = "hideControl";

    // Keep a reference to these classes for any cleanup and destroying later.
    private static Ziggeo ziggeo;

    private CallbackContext callbackContext;
    private Context context;

    CameraFullscreenRecorder(Context context, CallbackContext callbackContext) {
        this.context = context;
        this.callbackContext = callbackContext;
    }

    public void start(String appToken, JSONObject options) throws JSONException {
        destroy(null);
        ziggeo = new Ziggeo(appToken, context);

        RecorderConfig config = getConfig(options);

        ziggeo.setRecorderConfig(config);

        Log.d(TAG, "About to launch Ziggeo camera recorder");
        ziggeo.startCameraRecorder();
    }

    /**
     * Cleanup and destroy any previous instance of {@link Ziggeo} to prevent memory leaks and
     * save resources.
     */
    public static void destroy(CallbackContext callbackContext) {
        if (ziggeo != null) {
            ziggeo.clearRecorded();
            ziggeo = null;
        }

        if (callbackContext != null) {
            String message = "Ziggeo Recorder Destroyed";

            PluginResult result = new PluginResult(PluginResult.Status.OK, message);
            result.setKeepCallback(false);
            callbackContext.sendPluginResult(result);
        }
    }

    private RecorderConfig getConfig(JSONObject options) throws JSONException {

        RecorderConfig.Builder builder = new RecorderConfig.Builder()
                .callback(prepareCallback())
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

        if (options.has(OPTION_MANUAL_SUBMIT)) {
            builder.sendImmediately(!options.getBoolean(OPTION_MANUAL_SUBMIT));
        } else {
            builder.sendImmediately(false);
        }

        if (options.has(OPTION_DISABLE_SWITCH)) {
            builder.disableCameraSwitch(options.getBoolean(OPTION_DISABLE_SWITCH));
        }

        if (options.has(OPTION_START_DELAY)) {
            builder.startDelay(options.getInt(OPTION_START_DELAY));
        } else {
            builder.startDelay(3);      // In seconds
        }

        if (options.has(OPTION_HIDE_CONTROL) && options.getBoolean(OPTION_HIDE_CONTROL)) {
            CameraRecorderStyle style = new CameraRecorderStyle.Builder()
                    .hideControls(true)
                    .build();

            builder.style(style);
        }

        HashMap<String, String> extra = new HashMap<>();

        if (options.has(OPTION_CUSTOM_DATA)) {
            JSONObject customData = (JSONObject) options.get(OPTION_CUSTOM_DATA);
            extra.put("data", customData.toString());
        }

        if (options.has(OPTION_DATA_TITLE)) {
            extra.put("title", options.getString(OPTION_DATA_TITLE));
        }

        if (extra.size() > 0) {
            builder.extraArgs(extra);
        }

        return builder.build();
    }

    /**
     * Send result to the JS layer.
     *
     * @param eventName Name of the Ziggeo event.
     * @param eventData Any data to pass on along with the event.
     */
    private void sendResult(String eventName, Map eventData) {
        if (eventData == null) {
            eventData = new HashMap<>();
        }

        JSONObject message = new JSONObject(eventData);

        try {
            message.put("ziggeoEvent", eventName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PluginResult result = new PluginResult(PluginResult.Status.OK, message);
        result.setKeepCallback(true);
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
                data.put("uploaded", uploaded);
                data.put("total", total);

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
