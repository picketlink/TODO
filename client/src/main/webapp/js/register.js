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
		var jqxhr = $.ajax('/todo-server/accregister', {
			contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({userName:$('#username').val(),password:$('#password').val(),address:$('#address').val(),
            	firstName:$('#firstName').val(),lastName:$('#lastName').val(),email:$('#email1').val(),
            	postalCode:$('#postalCode').val(),city:$('#city').val(),state:$('#state').val(),country:$('#country').val()}),
            type:'POST', 
            success:function (data) {
                if (data.registered) {
                	alert("Registration Successful. Please login..");
                	window.location = getHost() + "/login.html";
                } else if (data.status.length == 0){
                	$('#register-msg').text("Registration failed. Try again ...");
                } else {
                	$('#register-msg').text(data.status);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if (xhr.status == 403) {
                	alert('You have no permissions for this operation.');
                }
              }
        });
        
		return false; // prevents submit of the form
	});
	
	//Register
	$('#username').on('focusout', (function(e) {
		var jqxhr = $.ajax('/todo-server/alreadyExists', {
			contentType: "application/json",
            dataType:'json',
            data:'userName=' + $('#username').val(),
            type:'GET', 
            success:function (data) {
                if (data.registered){
                	$('#register-msg').text('Username is already in use. Choose another one.');
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                	alert(thrownError);
              }
        });
	}));
});