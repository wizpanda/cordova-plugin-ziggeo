[![npm version](https://badge.fury.io/js/cordova-plugin-ziggeo.svg)](https://badge.fury.io/js/cordova-plugin-ziggeo)

# Cordova Plugin For [Ziggeo](https://ziggeo.com/) SDKs (Unofficial)

Ziggeo API allows you to integrate video recording and playback with only two lines of code in your site, service or app.

## Installation

```bash
cordova plugin add cordova-plugin-ziggeo
```

## Platforms

- Android
- iOS (TODO)

## Fullscreen Recorder

### Using AppCompat theme

Ziggeo Android SDK requires `Theme.AppCompat` or its descendants as an application theme. So you need to use if you are not adding other
 theme. For this, add the following lines in your `config.xml`:

```xml
<edit-config file="app/src/main/AndroidManifest.xml" mode="merge" target="/manifest/application" xmlns:android="http://schemas.android.com/apk/res/android">
    <application android:theme="@style/ZiggeoTheme" />
</edit-config>
```

You can also customize theme if you want. For that, refer the `styles.xml` file here.

### Options

We have tried to match the options with JavaScript SDK https://ziggeo.com/docs/sdks/javascript/browser-integration/parameters#javascript-revision=stable&javascript-version=v2
so that developers can pretty much use the same options in their web-apps.


| Name             | Type             | Required         | Default              | Description                                   |
|------------------|------------------|------------------|----------------------|-----------------------------------------------|
| `facing`         | number             | No             | Back Camera          | Which camera to use, front or back.           |
| `timeLimit`      | number             | No             | Unlimited            | Maximum time allowed for recording.           |
| `autoRecord`     | boolean            | No             | `false`              | Should auto start the recording immediately.  |
| `hideControl`    | boolean            | No             | `true` i.e. Visible  | Whether to hide the controls.                 |

TODO Complete this

### Usage

```javascript
const options = {
    facing: 1,
    timeLimit: 20,
    autoRecord: true,
    manualSubmit: false,
    disableCameraSwitch: true,
    countdown: 5,
    customData: {
        foo: 'bar'
    }
};

// Make sure the below code is invoked after Cordova's `deviceready` event is fired.

cordova.plugins.Ziggeo.startFullScreenRecorder("my-ziggeo-token", options, (data) => {
    console.log(data);
}, (error) => {
    console.error(error);
});
```

This will log the events like:

```
00:20:38.365 {ziggeoEvent: "loaded"}
00:20:38.368 {ziggeoEvent: "access_granted"}
00:20:38.368 {ziggeoEvent: "has_microphone"}
00:20:38.369 {ziggeoEvent: "has_camera"}
00:20:38.561 {ziggeoEvent: "ready_to_record"}
00:20:38.563 {ziggeoEvent: "countdown"}
00:20:39.564 {ziggeoEvent: "countdown"}
00:20:40.565 {ziggeoEvent: "countdown"}
00:20:41.565 {ziggeoEvent: "countdown"}
00:20:42.344 {ziggeoEvent: "recording_started"}
00:20:42.359 {level: "MODERATE", ziggeoEvent: "microphone_health"}
00:20:43.346 {ziggeoEvent: "recording_progress"}
00:20:43.369 {level: "BAD", ziggeoEvent: "microphone_health"}
00:20:44.352 {ziggeoEvent: "recording_progress"}
00:20:44.373 {level: "MODERATE", ziggeoEvent: "microphone_health"}
00:20:45.354 {ziggeoEvent: "recording_progress"}
00:20:45.375 {level: "BAD", ziggeoEvent: "microphone_health"}
00:20:46.354 {ziggeoEvent: "recording_progress"}
00:20:46.380 {level: "MODERATE", ziggeoEvent: "microphone_health"}
00:20:47.356 {ziggeoEvent: "recording_progress"}
00:20:47.381 {level: "BAD", ziggeoEvent: "microphone_health"}
00:20:48.358 {ziggeoEvent: "recording_progress"}
00:20:48.382 {level: "GOOD", ziggeoEvent: "microphone_health"}
00:20:49.360 {ziggeoEvent: "recording_progress"}
00:20:49.388 {level: "GOOD", ziggeoEvent: "microphone_health"}
00:20:50.362 {ziggeoEvent: "recording_progress"}
00:20:50.390 {level: "MODERATE", ziggeoEvent: "microphone_health"}
00:20:51.363 {ziggeoEvent: "recording_progress"}
00:20:51.391 {level: "BAD", ziggeoEvent: "microphone_health"}
00:20:52.366 {ziggeoEvent: "recording_progress"}
00:20:52.392 {level: "MODERATE", ziggeoEvent: "microphone_health"}
00:20:53.367 {ziggeoEvent: "recording_progress"}
00:20:53.394 {level: "BAD", ziggeoEvent: "microphone_health"}
00:20:54.370 {ziggeoEvent: "recording_progress"}
00:20:54.397 {level: "MODERATE", ziggeoEvent: "microphone_health"}
00:20:55.371 {ziggeoEvent: "recording_progress"}
00:20:55.397 {level: "BAD", ziggeoEvent: "microphone_health"}
00:20:56.373 {ziggeoEvent: "recording_progress"}
00:20:56.397 {level: "MODERATE", ziggeoEvent: "microphone_health"}
00:20:57.377 {ziggeoEvent: "recording_progress"}
00:20:57.398 {level: "BAD", ziggeoEvent: "microphone_health"}
00:20:58.380 {ziggeoEvent: "recording_progress"}
00:20:58.400 {level: "MODERATE", ziggeoEvent: "microphone_health"}
00:20:59.382 {ziggeoEvent: "recording_progress"}
00:20:59.401 {level: "BAD", ziggeoEvent: "microphone_health"}
00:21:00.384 {ziggeoEvent: "recording_progress"}
00:21:00.402 {level: "MODERATE", ziggeoEvent: "microphone_health"}
00:21:01.385 {ziggeoEvent: "recording_progress"}
00:21:01.403 {level: "BAD", ziggeoEvent: "microphone_health"}
00:21:02.387 {ziggeoEvent: "recording_progress"}
00:21:02.405 {level: "MODERATE", ziggeoEvent: "microphone_health"}
00:21:03.198 {ziggeoEvent: "recording_stopped"}
00:21:10.194 {ziggeoEvent: "manually_submitted"}
00:21:10.436 {ziggeoEvent: "ready_to_record"}
00:21:10.440 {ziggeoEvent: "countdown"}
00:21:10.505 {ziggeoEvent: "uploading_started"}
00:21:11.445 {ziggeoEvent: "countdown"}
00:21:12.445 {ziggeoEvent: "countdown"}
00:21:12.869 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "upload_progress"}
00:21:13.455 {ziggeoEvent: "countdown"}
00:21:52.173 {ziggeoEvent: "uploaded"}
00:21:52.873 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "verified"}
00:21:52.889 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "processing"}
00:21:55.946 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "processing"}
00:21:58.947 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "processing"}
00:22:01.941 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "processing"}
00:22:04.889 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "processing"}
00:22:07.967 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "processing"}
00:22:11.167 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "processing"}
00:22:13.897 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "processed"}
```
