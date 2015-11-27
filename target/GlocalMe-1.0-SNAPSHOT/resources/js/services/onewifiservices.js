sampleApp.factory( 'AuthService1', function($rootScope) {

  var currentUser;
  var currentprofile;
  
  return {
  
    login: function() {
    	$rootScope.$broadcast("login");
    },
    logout: function() {
    	currentUser = {};
    	$rootScope.$broadcast("logout");
    },
    isLoggedIn: function() {
    	if (typeof currentUser != 'undefined') {
    		if (currentUser.authenticated == true) {
    			return true;
    		} else {
    			return false;
    		}		
	   	} else {
    		return false;
    	}
    },
    getCurrentRole: function() {
    	if (typeof currentUser != 'undefined') {
    		return currentUser.role;
    	} else {
    		return "";
    	}
    },
    setCurrentUser: function(user) {
    	currentUser = user;
    },
    getLoggedInUser: function() {
    	return currentUser.userName;
    },
    getCurrentUser: function() { 
    	return currentUser; 
    },
    setProfile: function(profile) {
    	currentprofile = profile;
    },
    getProfile: function() {
    	return currentprofile;
    }
    
  };
});