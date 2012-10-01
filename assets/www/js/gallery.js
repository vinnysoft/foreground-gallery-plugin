/**
 * This class provides access to the device camera.
 *
 * @constructor
 */
var Gallery = function() {
	this.successCallback = null;
	this.errorCallback = null;
	this.options = null;
};

/**
 * Gets a picture from source defined by "options.sourceType", and returns the
 * image as defined by the "options.destinationType" option.

 * The defaults are sourceType=CAMERA and destinationType=DATA_URL.
 *
 * @param {Function} successCallback
 * @param {Function} errorCallback
 * @param {Object} options
 */
Gallery.prototype.getPicture = function(successCallback, errorCallback,
		options) {

	// successCallback required
	if (typeof successCallback !== "function") {
		console.log("Galeria Error: successCallback is not a function");
		return;
	}

	// errorCallback optional
	if (errorCallback && (typeof errorCallback !== "function")) {
		console.log("Galeria  Error: errorCallback is not a function");
		return;
	}

	if (options === null || typeof options === "undefined") {
		options = {};
	}
	if (options.quality === null || typeof options.quality === "undefined") {
		options.quality = 80;
	}
	if (options.maxResolution === null
			|| typeof options.maxResolution === "undefined") {
		options.maxResolution = 0;
	}

	if (options.targetWidth === null
			|| typeof options.targetWidth === "undefined") {
		options.targetWidth = -1;
	} else if (typeof options.targetWidth === "string") {
		var width = new Number(options.targetWidth);
		if (isNaN(width) === false) {
			options.targetWidth = width.valueOf();
		}
	}
	if (options.targetHeight === null
			|| typeof options.targetHeight === "undefined") {
		options.targetHeight = -1;
	} else if (typeof options.targetHeight === "string") {
		var height = new Number(options.targetHeight);
		if (isNaN(height) === false) {
			options.targetHeight = height.valueOf();
		}
	}

	cordova.exec(successCallback, errorCallback, "Gallery", "getPicture", [options]);
};

cordova.addConstructor(function() {
	if (typeof navigator.gallery === "undefined") {
		navigator.gallery = new Gallery();
	}
});