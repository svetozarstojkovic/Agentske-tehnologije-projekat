var app= angular.module("chatApp",[]);

app.controller("chatCtrl",function($scope,$rootScope,$window,$http){
	
	var webSocket;
	
	$rootScope.logged = sessionStorage.getItem("username");
	
	$scope.login = function(username,pass) {
		
//		 var username = $("#username").val();
//	     var password = $("#password").val();
	     
	     console.log("Username: "+username+" password: "+password)
		
		console.log(window.location.href);
		var loc = window.location.href;
		var host = JSON.stringify({address:loc, alias:""});
		var object = {      
			     username : username,
			     password : pass,
			     host : {address : loc,
			    	 	 alias : ""}
			     }
		$http({
			  method: 'POST',
			  url: 'http://localhost:8080/UserApp/rest/users/login',
			  data: JSON.stringify(object),
			}).then(function successCallback(response) {
				console.log(response);
				if (response.data == true) {
					console.log("You have succeeded my child");
					sessionStorage.setItem("username", username);
					$rootScope.logged = username;
					$window.location.href = "chat.html"
				} else {
					alert("You have failed this city");
				}
			  }, function errorCallback(response) {
				  alert("failure");
			  });
		
	}
	
	$scope.logout = function() {
		
		var username = sessionStorage.getItem("username");

		$http({
			  method: 'POST',
			  url: 'http://localhost:8080/UserApp/rest/users/logout',
			  data: username
			}).then(function successCallback(response) {
				if (response.data == true) {
					alert("You have succeeded my child");
					sessionStorage.setItem("username","");
					$window.location.href = "index.html"
				} else {
					alert("You have failed this city");
				}
			  }, function errorCallback(response) {
				  alert("failure");
			  });
		
	}
	
	$scope.register = function(username,pass) {
		
		console.log(username+" "+pass);
		$http({
			  method: 'POST',
			  url: 'http://localhost:8080/UserApp/rest/users/register',
			  data: JSON.stringify({username:username, password:pass, host:null})
			}).then(function successCallback(response) {
				if (response.data == true) {
					alert("You have succeeded my child");
				} else {
					alert("You have failed this city");
				}
			  }, function errorCallback(response) {
				  alert("failure");
			  });
	}
	
	
	
	
	
	
	
});