# Change Log

## 1.0.3

1. Fix: Trying to fix the crash on a few devices https://github.com/Ziggeo/Android-Client-SDK/issues/26.

## 1.0.2

1. Fix: Fixed the crash on various Android devices https://github.com/Ziggeo/Android-Client-SDK/issues/26.
2. Fix: Fixed the crash while switching the camera https://github.com/Ziggeo/Android-Client-SDK/issues/31.
3. Improvement: Stop & close button not needed with `sendImmediate` is set to true https://github.com/Ziggeo/Android-Client-SDK/issues/32.

## 1.0.1

1. Fix: Fixed plugin crash after recording is stopped if `autoRecord` is set to `true` https://github.com/Ziggeo/Android-Client-SDK/issues/33
2. Improvement: Not creating instance of `Ziggeo` if already created to save resources and speed-up performance.

## 1.0.0

1. Feature: Added option to show a confirmation dialogue before stopping the recording.
2. Breaking Change: Renamed option from `manualSubmit` to `sendImmediately`.
3. Breaking Change: Renamed option from `timeLimit` to `maxDuration`.

## 0.0.5

1. Added feature to set quality of the recording.
2. Preventing screen lock/sleep when the recorder is running https://github.com/Ziggeo/Android-Client-SDK/issues/28
3. Added confirmation dialog with Android Back button https://github.com/Ziggeo/Android-Client-SDK/issues/30
4. Wrong video orientation when front camera is being used https://github.com/Ziggeo/Android-Client-SDK/issues/17

## 0.0.4

1. Fixed passing the `custom-data` in recorded video. https://github.com/Ziggeo/Android-Client-SDK/issues/29
2. Added option to pass `title` as extra info to recorded video.
3. Using the stable version of Ziggeo Android SDK.
4. Passing the uploaded/total count in `upload_progress` event.

## 0.0.3

1. Added a new option to hide the control.
2. Added info about ZiggeoTheme

## 0.0.1, 0.0.2

1. Full screen Ziggeo recorder with basic options.
