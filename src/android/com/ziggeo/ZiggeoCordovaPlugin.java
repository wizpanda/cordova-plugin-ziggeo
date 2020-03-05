package com.ziggeo;

import android.content.Context;
import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
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

        CameraFullscreenRecorder recorder = new CameraFullscreenRecorder(this.context, callbackContext);
        recorder.start(apiToken, options);
    }
}
