
function ConfigSetting(url, data, method){
	this.url = url;
	this.data = data;
	this.type = method;
}

ConfigSetting.prototype.ajaxCall = function(successCallback, errorCallBack){
	$.ajax({
        url: this.url,
        type: this.method,
        data : this.data,
        success: successCallback,
        error: errorCallBack
    });
}