var app= angular.module("chatApp",[]);

app.controller("chatPart",function($scope,$rootScope,$window,$http){
	
	var webSocket;
	//$rootScope.logged = sessionStorage.getItem("username");
	
	var output;
	
	var offlineUsers = [];
	var activeUsers = [];
	
//	$( window ).unload(function() {
//		 	webSocket.close();
//		 	$scope.logout()
//		});
	
//	$scope.setScroll = function() {
//		document.getElementById('scroller').scr
//	}
	
	

	$scope.openSocket = function() {

		$rootScope.logged = sessionStorage.getItem("username");
		

		
//		$(".usersTables").on('click', function() {
//			console.log("entered on click");
//			$(".usersTables").css("background-color","white")
//			$(this).css("background-color","#66b3ff")
//		})
		
	
//		$(".userRow").click(function() {
//			console.log("usao u podesavanje pozadine")
//			$("#userTable").css("background-color", "white");
//			$(this).css("background-color", "gray")
//		})
		

		
		console.log(window.location.href);
		
		document.getElementById("subjectinput").disabled = true;
		document.getElementById("contentinput").disabled = true;
		document.getElementById("send").disabled = true;
		
		var loc = window.location.href;
		
		var ind = loc.indexOf("//");
		var location = loc.substr(ind+2);
		
		location = location.substr(0, location.indexOf("/"));
		
		console.log(location);
		
		// Ensures only one connection is open at a time
        if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
           writeResponse("WebSocket is already opened.");
            return;
        }
        // Create a new instance of the websocket
        webSocket = new WebSocket("ws://"+location+"/ChatApp/chat/"+$rootScope.logged);
         
        /**
         * Binds functions to the listeners for the websocket.
         */
        webSocket.onopen = function(event){
            // For reasons I can't determine, onopen gets called twice
            // and the first time event.data is undefined.
            // Leave a comment if you know the answer.
            if(event.data === undefined)
                return;

            writeResponse(event.data);
        };
        var response;
        webSocket.onmessage = function(event){
        	console.log("onMessage");
            response = event.data;

            if (response.substr(0, 8) == "messages") {
            	console.log(response.substr(8))
            	var jsonArray = JSON.parse(response.substr(8));
            	
//            	if (json.length != 0) {
//	            	console.log("From: "+json[0].from.username);
//				    console.log("To: "+json[0].to.username);
//	            	
//				    var from = json[0].from.username;
//				    var to =  json[0].to.username;
//				    
//	
//				    
//				    if (from == $rootScope.receiver || to == $rootScope.receiver) {
//				    	$rootScope.messages = JSON.parse(response.substr(8));
//				    } else {
//				    	console.log("nije otvoren chat");
//				    }
//			    
//            	} else {
//            		$rootScope.messages = JSON.parse("[]");  // check this
//            	}
            	if ($rootScope.receiver != "all") {
					var messages = [];
	            	for (var i in jsonArray) {
	            		var message = jsonArray[i];
	            		console.log(message)
	            		if (message.to != null) {
		        		  	if (message.from.username == $rootScope.logged && message.to.username == $rootScope.receiver) {
		        		  		messages.push(message);
		        		  	} else if (message.to.username == $rootScope.logged && message.from.username == $rootScope.receiver) {
		        		  		messages.push(message);
		      				}
	            		}
	            	}
	            	$rootScope.messages = messages;
            	} else {
            		console.log(JSON.parse(response.substr(8)))
            		var messages = [];
	            	for (var i in jsonArray) {
	            		var message = jsonArray[i];
	        		  	if (message.to == null ) {
	        		  		messages.push(message);
	        		  	}
	            	}
	            	$rootScope.messages = messages;
            	}
            	
            } 
            
            if (response.substr(0,12) == "offlineUsers" && response.substr(12) != "[]") {
            	var json = JSON.parse(response.substr(12));
            	if (json.length != 0) {
	            	$rootScope.offlineUsers = json
	            	console.log(response.substr(12))
            	} else {
            		$rootScope.offlineUsers = JSON.parse("[]")  // check this
            	}
            }
            console.log(response)
            if (response.substr(0,11) == "activeUsers" && response.substr(11) != "[]") {
            	var json = JSON.parse(response.substr(11));
            	if (json.length != 0) {
	            	$rootScope.activeUsers = json
	            	console.log(response.substr(11))
            	} else {
            		$rootScope.activeUsers = JSON.parse("[]")  // check this
            	}
            }
            
            $rootScope.$apply()
            
            $("#scroller").scrollTop($("#scroller")[0].scrollHeight);
           
            
            writeResponse(event.data);
        };

        webSocket.onclose = function(event){
            writeResponse("Connection closed");
        };
        //alert(sessionStorage.getItem("refreshed"))
       if (sessionStorage.getItem("refreshed") == null || sessionStorage.getItem("refreshed") == "no") {
    	   sessionStorage.setItem("refreshed", "yes")
       } else {
    	   sessionStorage.setItem("refreshed","no")
    	   $scope.logout();
    
       }
	}
	
	$scope.send = function(){
        var subject = document.getElementById("subjectinput").value;
        var content = document.getElementById("contentinput").value;      
        if (subject == "" || content == "") return;
               
        if (subject.length==0 || content.length == 0) return;
        
        
        //output = $rootScope.logged+";&,:&"+$rootScope.receiver+";&,:&"+"subject"+";&,:&"+text;
        
        if ($rootScope.receiver != "all") {
       
	        var object = {      
				     from : $rootScope.logged,
				     to : $rootScope.receiver,
				     subject : subject,
				     content : content
				    }
        } else {
        	var object = {      
				     from : $rootScope.logged,
				     to : "",
				     subject : subject,
				     content : content
				    }
        }
        webSocket.send(JSON.stringify(object));
        
        document.getElementById("subjectinput").value = "";
        document.getElementById("contentinput").value = "";
        
    }
   
	$scope.closeSocket = function() {
        webSocket.close();
    }

    function writeResponse(text){
        messages.innerHTML += "<br/>" + text;
    }
    
    $scope.selectedUser = function(un) {
    	
    	$(".usersTables").css("background-color","white")
    	$("#"+un).css("background-color","#66b3ff")
    	
    	console.log("Clicked on: "+un);
    	
    	document.getElementById("subjectinput").disabled = false;
    	document.getElementById("contentinput").disabled = false;
		document.getElementById("send").disabled = false;
    	
    	$rootScope.receiver = un;
    	console.log("entered selectedUser " + $rootScope.receiver)
    	//output = $rootScope.logged+";&,:&"+$rootScope.receiver+";&,:&"+""+";&,:&"+"";
    	var object = {      
			     from : $rootScope.logged,
			     to : $rootScope.receiver,
			     subject : "",
			     content : ""
			    }
        webSocket.send(JSON.stringify(object));
    }
    
    $scope.logout = function() {
		console.log("logout")
		//var username = sessionStorage.getItem("username");
		var username = $rootScope.logged;

		$http({
			  method: 'POST',
			  url: 'http://localhost:8080/UserApp/rest/users/logout',
			  data: username
			}).then(function successCallback(response) {
				if (response.data == true) {
					//alert("You have succeeded my child");
					sessionStorage.setItem("username","");
					$window.location.href = "index.html"
				} else {
					alert("Method returned false");
				}
			  }, function errorCallback(response) {
				  alert("failure");
			  });
		
	}
    
    $scope.getDate = function(dateLong) {
    	  var monthNames = [
    	    "January", "February", "March",
    	    "April", "May", "June", "July",
    	    "August", "September", "October",
    	    "November", "December"
    	  ];
    	  
    	  var date = new Date(dateLong);

    	  var day = date.getDate();
    	  var monthIndex = date.getMonth();
    	  var year = date.getFullYear();
    	  
    	  var hour = date.getHours();
    	  if (hour < 10){
    		  hour = '0'+hour;
    	  }
    	  var minute = date.getMinutes();
    	  if (minute < 10){
    		  minute = '0'+minute;
    	  }

    	  return hour+':'+minute+' '+day + '.' + monthNames[monthIndex] + '.' + year+'.';
    	}
    
});