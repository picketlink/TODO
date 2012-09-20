var getHost = function() {
	var port = '8080';
	return 'http://' + window.location.hostname + ":" + port + "/todo";
};

function storeToken(token) {
	try {
		sessionStorage.setItem("AUTH_TOKEN", token);
	} catch (e) {
		alert('Your browser does not support HTML5 sessionStorage. Try upgrading.');
	}
}

function getToken() {
	try {
		return sessionStorage.getItem("AUTH_TOKEN");
	} catch (e) {
		alert('Your browser does not support HTML5 sessionStorage. Try upgrading.');
	}
}

function removeToken() {
	try {
		return sessionStorage.clear();
        } catch (e) {
		alert('Your browser does not support HTML5 sessionStorage. Try upgrading.');
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
	if (!$('#register-btn')) {
		return;
	}
	
	$('#register-btn').click(function() {
		var jqxhr = $.ajax('/todo-server/auth/register', {
			contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({
                firstName:$('#firstname').val(),
                lastName:$('#lastname').val(),
                email:$('#email').val(),
                userId:$('#username').val(),
                password:$('#password').val()
            }),
            type:'POST', 
            success:function (data) {
                if (data.loggedIn) {
                	storeToken(data.token);
					window.location = getHost() + "/index.html";
                } else {
                	$('#register-msg').text("Registration failed. Try again ...");
                }
            }
        });
		return false; // prevents submit of the form
	});
});

$(document).ready(function() {
	if (!$('#login-btn')) {
		return;
	}
	
	$('#login-btn').click(function() {
		var jqxhr = $.ajax('/todo-server/auth/login', {
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
    alert("Logout");
		var jqxhr = $.ajax('/todo-server/auth/logout', {
            data:{},
            type:'GET', 
            success:function (data) {
                removeToken();
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

	var jqxhr = $.ajax('/todo-server/userinfo', {
        data:{},
        type:'GET', 
        success:function (data) { 
        	$('#userinfo-msg').text("Welcome " + data.fullName + ". Your roles are: " + data.roles);
        }
    });
});
