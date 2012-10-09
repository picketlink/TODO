$(document).ready(function() {
	if (!$('#qrcode-div')) {
		return;
	}

	if ($('#qrcode-div')) {
		var jqxhr = $.ajax('/todo-server/otpserial', {
	        data:{},
	        type:'GET', 
	        success:function (data) {
	        	$('#qrcode-div').qrcode("otpauth://totp/"+data.userId + "?secret="+data.b32);
	        	$('#b32').text("Base32 Value="+data.b32);
	        	$('#val').text("Straight Value="+data.serial);
	        }
	    });
	}
});