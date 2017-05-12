var app= angular.module("chatApp",[]);

app.controller("loginPart",function($scope,$rootScope,$window,$http){
	
	
	var webSocket;
	$rootScope.logged = sessionStorage.getItem("username");
	
	var output;
	


	
	$scope.openSocket = function() {
		
		var loc = window.location.href;
		
		var ind = loc.indexOf("//");
		var location = loc.substr(ind+2);
		
		location = location.substr(0, location.indexOf("/"));
		
		console.log(location);

		// Ensures only one connection is open at a time
        if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
        	console.log("already opened");
            return;
        }
        // Create a new instance of the websocket
        webSocket = new WebSocket("ws://"+location+"/ChatApp/login");
         
        /**
         * Binds functions to the listeners for the websocket.
         */
        webSocket.onopen = function(event){
        	console.log("Connection opened");
            // For reasons I can't determine, onopen gets called twice
            // and the first time event.data is undefined.
            // Leave a comment if you know the answer.
            if(event.data === undefined)
                return;
        };
        
        webSocket.onmessage = function(event){
        	
        	console.log("onMessage: "+event.data);
        	var msg = JSON.parse(event.data);
        	if (msg == true) {
        		console.log("entered in if")
        		sessionStorage.setItem("username", $("#username").val());
        		$rootScope.logged = $("#username").val();
        		$window.location.href = "chat.html"
        	} else if (msg == false){
        		alert("You failed this city");
        	}
            
        };

        webSocket.onclose = function(event){
        	console.log("onClose")
        };
	}
	
	$scope.send = function(){
       var username = $("#username").val();
       var password = $("#password").val();
       
       console.log("Username: "+username+" password: "+password);
       
       var loc = window.location.href;
		var host = JSON.stringify({address:loc, alias:""});
		var object = {      
			     username : username,
			     password : password,
			     host : {address : loc,
			    	 	 alias : username+"s computer"}
		     }
      webSocket.send(JSON.stringify(object));
        
    }
	
	
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
			  crossDomain: true,
			  data: JSON.stringify(object)
			}).then(function successCallback(response) {
				console.log(response);
				if (response.data == true) {
					console.log("You have succeeded my child");
					sessionStorage.setItem("username", username);
					$rootScope.logged = username;
					$window.location.href = "chat.html"
				} else {
					alert("Method returned false");
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
	
	
});