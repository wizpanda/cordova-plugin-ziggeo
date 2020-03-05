const exec = require('cordova/exec');

const Ziggeo = {};

Ziggeo.startFullScreenRecorder = function (token, options, success, failure) {
    exec(success, failure, 'ZiggeoCordovaPlugin', "startFullScreenRecorder", [token, options])
};

Ziggeo.destroyRecorder = function (success, failure) {
    exec(success, failure, 'ZiggeoCordovaPlugin', "destroyRecorder", [])
};

module.exports = Ziggeo;
