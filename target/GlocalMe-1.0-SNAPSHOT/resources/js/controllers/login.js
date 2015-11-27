sampleApp.controller('LoginCtrl', function ($scope,$http, $location, $rootScope, AuthService) {
	
	$scope.login = {};
	$scope.login.status = "";
	
	$scope.setpassword = {};
	$scope.setpassword.status = "";
	if (AuthService.isLoggedIn()) {
		$scope.setpassword.login = AuthService.getLoggedInUser();
	}
	$scope.doLogin = function() {
			$scope.login.status = "";
		 	$http({
		 		method: "POST",
		 		url: "/GlocalMe/login/doLogin?passwd="+$scope.password+"&login="+$scope.username
	        })
	        .success(function(response, status, headers, config){
	        	console.log("Login Response >>");
	        	console.log(response.authenticated);
	        	if (response.authenticated == true) {
	        		console.log("Login Successful");
	        		AuthService.setCurrentUser(response);
	        		$scope.$parent.isloggedin = AuthService.isLoggedIn();
	        		$scope.$parent.role = AuthService.getCurrentRole();
	        		$scope.login.status = "SUCCESS";
	        		$location.path('/personal/getinfo');
	        	} else {
	        		console.log("Login Failed");
	        		$scope.login.status = "FAILED";
	        	}
	        	console.log(response);
	        })
	        .error(function(response, status, headers, config){
	        	$scope.login.status = "FAILED";
				console.log(response);
	        });
	}
	
	$scope.doSetPassword = function() {
	 	$http({
	 		method: "POST",
	 		url: "/GlocalMe/personal/setpassword?login="+$scope.setpassword.login+"&passwd="+$scope.setpassword.passwd,
        })
        .success(function(response, status, headers, config){
        	console.log(response);
        	if (response.STATUS == 'SUCCESS') {
        		$scope.setpassword.status = 'SUCCESS';
        		console.log(response);
        		var newview = "/personal/getinfo";
				$location.path(newview);
			} 
			else {
				$scope.setpassword.status = 'FAILED';
			}
				
        })
        .error(function(response, status, headers, config){
			$scope.setpassword.status = 'FAILED';
        });
	}	
	
});