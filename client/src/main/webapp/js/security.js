var getHost = function() {
	var port = '8080';
	var hname = window.location.hostname;
	
	if(hname.indexOf("rhcloud.com") == -1){
		return 'http://' + hname + ":" + port + "/todo";	
	} else {
		return 'http://' + hname + "/todo";	
	}
};

function storeToken(token) {
	try {
		localStorage.setItem("AUTH_TOKEN", token);
	} catch (e) {
		alert('Your browser does not support HTML5 localStorage. Try upgrading.');
	}
}

function getToken() {
	try {
		return localStorage.getItem("AUTH_TOKEN");
	} catch (e) {
		alert('Your browser does not support HTML5 localStorage. Try upgrading.');
	}
}

$.ajaxSetup({
	headers : {
		"Auth-Token" : getToken()
	},
	error : function(xhr, textStatus, errorThrown) {
		if (window.location.pathname.indexOf("login.html", 0) == -1) {
			if (window.location.pathname.indexOf("error.html", 0) == -1) {
				if (xhr.status == 500 || xhr.status == 403) {
					window.location = getHost() + "/error.html";
					return;
				}
			}

			window.location = getHost() + "/login.html";
		}
	}
});

$(document).ready(function() {
	if (!$('#login-btn')) {
		return;
	}
	
	$('#login-btn').click(function() {
		var jqxhr = $.ajax('/todo-server/signin', {
			contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({userId:$('#username').val(),password:$('#password').val()}),
            type:'POST', 
            success:function (data) {
                if (data.loggedIn) {
                	storeToken(data.token);
					window.location = getHost() + "/index.html";
                } else {
                	$('#login-msg').text("Authentication failed. Try again ...");
                }
            }
        });
		return false; // prevents submit of the form
	});
	
});

$(document).ready(function() {
	if (!$('#logout-btn')) {
		return;
	}
	
	$('#logout-btn').click(function() {
		var jqxhr = $.ajax('/todo-server/logout', {
            data:{},
            type:'GET', 
            success:function (data) {
            	window.location = getHost() + "/login.html";
            }
        });
		return false; // prevents submit of the form
	});
});

$(document).ready(function() {
	if (!$('#userinfo-msg')) {
		return;
	}

	if ($('#userinfo-msg')) {
		var jqxhr = $.ajax('/todo-server/userinfo', {
	        data:{},
	        type:'GET', 
	        success:function (data) { 
	        	$('#userinfo-msg').text("Welcome " + data.fullName + ". Your roles are: " + data.roles);
	        }
	    });
	}
});

var popup = null;

function sendMainPage() {
	popup.close();
	window.location = getHost() + "/index.html";
}

$(document).ready(function() {

	//Facebook signin
	$('#facebook-sign').click(function(e) {
        e.preventDefault();
        popup = window.open("/todo-server/facebook", "name", "height=512, width=512");
        popup.focus();
        popup.window.reload = function(){
        	if(popup.document.body.innerHTML.indexOf("true") > -1){
            	popup.close();
            	var jqxhr = $.ajax('/todo-server/facebook', {
        			contentType: "application/json",
                    dataType:'json',
                    type:'POST', 
                    success:function (data) {
                        if (data.loggedIn) {
                        	storeToken(data.token);
        					window.location = getHost() + "/index.html";
                        } else {
                        	$('#login-msg').text("Authentication failed. Try again ...");
                        }
                    }
                });
    			//window.location = getHost() + "/index.html";
        	}
        };
        
		return false; // prevents submit of the form
	});
});

$(document).ready(function() {
	$('#signup-btn').click(function() {
		window.location = getHost() + "/register.html";
		return false; // prevents submit of the form
	});
});

$(document).ready(function() {

	//Google signin
	$('#google-sign').click(function(e) {
        e.preventDefault();
        popup = window.open("/todo-server/google", "name", "height=512, width=512");
        popup.focus();
        popup.window.reload = function(){
        	if(popup.document.body.innerHTML.indexOf("true") > -1){
            	popup.close();
            	var jqxhr = $.ajax('/todo-server/google', {
        			contentType: "application/json",
                    dataType:'json',
                    type:'POST', 
                    success:function (data) {
                        if (data.loggedIn) {
                        	storeToken(data.token);
        					window.location = getHost() + "/index.html";
                        } else {
                        	$('#login-msg').text("Authentication failed. Try again ...");
                        }
                    }
                });
    			//window.location = getHost() + "/index.html";
        	}
        };
        
		return false; // prevents submit of the form
	});
});