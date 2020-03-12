[![npm version](https://badge.fury.io/js/cordova-plugin-ziggeo.svg)](https://badge.fury.io/js/cordova-plugin-ziggeo)

# Cordova plugin for [Ziggeo](https://ziggeo.com/) native SDKs (Unofficial)

Ziggeo API allows you to integrate video recording and playback with only two lines of code in your site, service or app. Ziggeo claims
 to have their [Cordova integration](https://ziggeo.com/docs/sdks/mobile/cordova#javascript-revision=stable&javascript-version=v2) which
 is not a true Cordova implementation as that does not directly uses the native SDKs but instead uses their client JS SDK with a magic
 (which is so nice) that triggers a few existing official Cordova plugins (like media capture, file or camera plugin) for recording
 & player.
 
This gives a flexibility to developers by using the same code in their web & mobile apps but the downside of this approach is that, all
 the features & functionality that are available in native SDK or JS SDK are not available when used with a Cordova based mobile app.
 
According to Google or Cordova, a cordova plugin is:

> A Cordova plugin is a bit of add-on code that provides JavaScript interface to native components. They allow your app to use native device
> capabilities beyond what is available to pure web apps.
>
> Plugins comprise a single JavaScript interface along with corresponding native code libraries for each supported platform. In essence
> this hides the various native code implementations behind a common JavaScript interface.

Based on the detailed discussion about this issue with Cordova, we decided to write this plugin so this plugin provides the integration of
 SDKs using Cordova.

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


| Name             | Type     | Required  | Default              | Description                                   |
|------------------|----------|-----------|----------------------|-----------------------------------------------|
| `facing`         | number   | No        | `0` - Back Camera          | Which camera to use, front `1` or back `0`.           |
| `timeLimit`      | number   | No        | Unlimited            | Maximum time allowed for recording.           |
| `autoRecord`     | boolean  | No        | `false`              | Should auto start the recording immediately.  |
| `hideControl`    | boolean  | No        | `true` i.e. Visible  | Whether to hide the controls.                 |
| `manualSubmit`   | boolean  | No        | `true`               | Automatically submits the recording as soon as it stops |
| `title`          | string   | No        | null                 | The title of the video to set                 |
| `quality`        | number   | No        | `0` - High quality    | The quality of the video. `1` for medium quality. `2` for low quality.                 |

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
00:20:41.565 {ziggeoEvent: "countdown"}
00:20:42.344 {ziggeoEvent: "recording_started"}
00:20:42.359 {level: "MODERATE", ziggeoEvent: "microphone_health"}
00:20:43.346 {ziggeoEvent: "recording_progress"}
00:20:43.369 {level: "BAD", ziggeoEvent: "microphone_health"}
00:20:59.382 {ziggeoEvent: "recording_progress"}
00:21:03.198 {ziggeoEvent: "recording_stopped"}
00:21:10.194 {ziggeoEvent: "manually_submitted"}
00:21:10.436 {ziggeoEvent: "ready_to_record"}
00:21:10.440 {ziggeoEvent: "countdown"}
00:21:10.505 {ziggeoEvent: "uploading_started"}
00:21:12.445 {ziggeoEvent: "countdown"}
00:21:12.869 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "upload_progress"}
00:21:13.455 {ziggeoEvent: "countdown"}
00:21:52.173 {ziggeoEvent: "uploaded"}
00:21:52.873 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "verified"}
00:21:52.889 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "processing"}
00:22:07.967 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "processing"}
00:22:11.167 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "processing"}
00:22:13.897 {videoToken: "510c6104201710e5a211ef3c276c3859", ziggeoEvent: "processed"}
```
