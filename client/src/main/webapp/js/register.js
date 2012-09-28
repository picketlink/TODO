var getHost = function() {
	var port = '8080';
	var hname = window.location.hostname;
	
	if(hname.indexOf("rhcloud.com") == -1){
		return 'http://' + hname + ":" + port + "/todo";	
	} else {
		return 'http://' + hname + "/todo";	
	}
};

$(document).ready(function() {

	if (!$('#register-btn')) {
		return;
	}
	
	//Register
	$('#register-btn').click(function(e) {
		var jqxhr = $.ajax('/todo-server/register', {
			contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({userName:$('#username').val(),password:$('#password').val(),address:$('#address').val(),
            	firstName:$('#firstName').val(),lastName:$('#lastName').val(),email:$('#email1').val(),
            	postalCode:$('#postalCode').val(),city:$('#city').val(),state:$('#state').val(),country:$('#country').val()}),
            type:'POST', 
            success:function (data) {
                if (data.status.indexOf('Success') > -1) {
                	alert("Registration Successful. Please login..");
                	window.location = getHost() + "/login.html";
                } else {
                	$('#register-msg').text("Registration failed. Try again ...");
                }
            }
        });
        
		return false; // prevents submit of the form
	});
});