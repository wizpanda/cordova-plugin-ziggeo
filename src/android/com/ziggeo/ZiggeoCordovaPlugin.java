package com.ziggeo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Plugin's main file to provide bridge between Ziggeo's Android SDK & JavaScript.
 *
 * @author Shashank Agrawal
 */
public class ZiggeoCordovaPlugin extends CordovaPlugin {

    private Context context;
    private CallbackContext callbackContext;
    private static final String TAG = "ZiggeoCordovaPlugin";

    public ZiggeoCordovaPlugin() {
        Log.d(TAG, "ZiggeoCordovaPlugin initialize");
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.context = this.cordova.getContext();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        Log.i(TAG, "Execute called");
        this.callbackContext = callbackContext;

        if (action.equals("startFullScreenRecorder")) {
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startFullScreenRecorder(args);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return true;
        }

        return false;
    }

    private void startFullScreenRecorder(JSONArray args) throws JSONException {
        String apiToken = args.getString(0);
        JSONObject options = args.getJSONObject(1);

        HashMap optionsMap = toMap(options);
        Intent intent = new Intent(this.cordova.getActivity(), CameraFullscreenRecorderActivity.class);
        intent.putExtra(CameraFullscreenRecorderActivity.INTENT_OPTIONS, optionsMap);
        intent.putExtra(CameraFullscreenRecorderActivity.INTENT_API_TOKEN, apiToken);

        cordova.startActivityForResult(this, intent, 0);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (callbackContext == null || intent == null) {
            return;
        }

        if (intent.getAction().equals(CameraFullscreenRecorderActivity.ACTION_EXIT)) {
            HashMap result = (HashMap) intent.getSerializableExtra(CameraFullscreenRecorderActivity.EXTRA_RESULT);

            if (intent.getBooleanExtra(CameraFullscreenRecorderActivity.EXTRA_RESULT_SUCCESS, false)) {
                this.callbackContext.success(new JSONObject(result));
            } else {
                this.callbackContext.error(new JSONObject(result));
            }
        }
    }

    /**
     * Utility method which converts JSONObject to Map because {@link JSONObject} is not
     * {@link java.io.Serializable}.
     *
     * @param object Data which need to convert to Map
     * @return Converted Map
     * @throws JSONException if key-value from the data can not be read.
     */
    private static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
        HashMap<String, Object> map = new HashMap<>();

        if (object == null) {
            return map;
        }

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            map.put(key, value);
        }
        return map;
    }
}
