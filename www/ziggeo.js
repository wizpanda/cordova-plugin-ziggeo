const exec = require('cordova/exec');

const Ziggeo = {};

Ziggeo.startFullScreenRecorder = function (token, options, success, failure) {
    exec(success, failure, 'ZiggeoCordovaPlugin', "startFullScreenRecorder", [token, options])
};

module.exports = Ziggeo;
