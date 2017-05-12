var app= angular.module("chatApp",[]);

app.controller("registerPart",function($scope,$rootScope,$window,$http){
	
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
        webSocket = new WebSocket("ws://"+location+"/ChatApp/register");
         
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
        		$window.location.href = "index.html"
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
			     host : null
		     }
      webSocket.send(JSON.stringify(object));
        
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
