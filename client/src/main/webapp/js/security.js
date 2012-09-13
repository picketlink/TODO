var getHost = function() {
	var port = '8080';
	return 'http://' + window.location.hostname + ":" + port + "/todo-www";
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
                }
            }
        });
		return false; // prevents submit of the form
	});
});