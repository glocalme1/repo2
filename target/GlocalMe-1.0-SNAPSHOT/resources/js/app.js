var sampleApp = angular.module('glocalMeApp', 
				['ngRoute', 'pascalprecht.translate','ui.bootstrap','ui.grid','countrySelect','ui.grid.resizeColumns']);

sampleApp.factory('activationService', ['$http', function($http) {
  var sdo = {
    getDetails: function(hashValue) {
      var promise = $http({ 
        method: 'POST', 
        url: '/GlocalMe/retailer/getHash?hashValue='+hashValue,
        transformResponse: function(data, headersGetter) {
		      return {result: data};
		}    
      });
      promise.success(function(data, status, headers, conf) {
        return data;
      });
      return promise;
    },
    getAccountSummaryDetails: function() {
      	var promise = $http({ 
    		method: 'POST', 
    		url: '/GlocalMe/personal/accountsummary'
  		});
  		promise.success(function(data, status, headers, conf) {
    		return data;
  		});
  		return promise;	
    },
    getTopupDetails: function() {
      	var promise = $http({ 
    		method: 'POST', 
    		url: '/GlocalMe/personal/gettopup'
  		});
  		promise.success(function(data, status, headers, conf) {
    		return data;
  		});
  		return promise;	
    },    
  }
  return sdo;
}]);

sampleApp.factory('personDetailsService', ['$http', function($http) {
  var sdo = {
    getDetails: function(login) {
      var promise = $http({ 
        method: 'POST', 
        url: '/GlocalMe/personal/getinfo?login='+login
      });
      promise.success(function(data, status, headers, conf) {
      	console.log(data);
        return data;
      });
      return promise;
    }
  }
  return sdo;
}]);		
			
sampleApp.factory( 'AuthService', function($rootScope,$http) {

  var currentUser;
  var currentprofile;
  
  return {
  
    login: function() {
    	$rootScope.$broadcast("login");
    },
    logout: function() {
    	currentUser = {};
    	$rootScope.$broadcast("logout");
	    var promise = $http({ 
	        method: 'POST', 
	        url: '/GlocalMe/login/logout'
	    });
	    promise.success(function(data, status, headers, conf) {
	    });
	    return promise;    	
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
    },
    checkLogin: function() {
    	var promise = $http({ 
	        method: 'POST', 
	        url: '/GlocalMe/login/loginstatus'
	    });
	    promise.success(function(data, status, headers, conf) {
	    });
	    return promise; 
    }
    
  };
});			
				
sampleApp.config(['$routeProvider' , '$translateProvider', 

	  function($routeProvider, $translateProvider) {
	    $routeProvider.
			  when('/personal/getinfo', {
			    templateUrl: 'partials/personal.html',
			    controller: 'PersonalCtrl',
			    resolve: {
        			personalDetails: function(personDetailsService, AuthService, $route, $location) {
						var loginFlg = AuthService.isLoggedIn();
						if (loginFlg == true) {
        					console.log("login::"+AuthService.getLoggedInUser());
        					var login = AuthService.getLoggedInUser();
        					return personDetailsService.getDetails(login);
        				}
    					else {
    						$location.path("/login");
    					}
					}
			
        		}
			  }).
			  when('/login', {
			    templateUrl: 'partials/login.html',
			    controller: 'LoginCtrl'
			  }).
			  when('/topuphistory', {
			    templateUrl: 'partials/topuphistory.html',
			    controller: 'TopupHistoryCtrl',
			    resolve: {
			    	logincheck: function(AuthService, $location) {
						var loginFlg = AuthService.isLoggedIn();
						if (loginFlg == true) {
        					var login = AuthService.getLoggedInUser();
        					return AuthService.checkLogin();
        				}
    					else {
    						$location.path("/login");
    					}			    		
			    	}
			    }
			  }).      
			  when('/coverage', {
			    templateUrl: 'partials/coverage.html',
			    controller: 'CoverageCtrl'
			  }).        
			  when('/home', {
			    templateUrl: 'partials/home.html',
			    controller: 'HomePageCtrl'
			  }).
			  when('/partner/deviceupload', {
			    templateUrl: 'partials/deviceuploads.html',
			    controller: 'PartnerCtrl',
			    resolve: {
			    	logincheck: function(AuthService, $location) {
						var loginFlg = AuthService.isLoggedIn();
						if (loginFlg == true) {
        					var login = AuthService.getLoggedInUser();
        					return AuthService.checkLogin();
        				}
    					else {
    						$location.path("/login");
    					}			    		
			    	}
			    }			          
			  }).
			  when('/partner/retailerupload', {
			    templateUrl: 'partials/retailersuploads.html',
			    controller: 'PartnerCtrl',
			    resolve: {
			    	logincheck: function(AuthService, $location) {
						var loginFlg = AuthService.isLoggedIn();
						if (loginFlg == true) {
        					var login = AuthService.getLoggedInUser();
        					return AuthService.checkLogin();
        				}
    					else {
    						$location.path("/login");
    					}			    		
			    	}
			    }	      
			  }).		
			  when('/partner/retailerdevicesupload', {
			    templateUrl: 'partials/retailerdevicesuploads.html',
			    controller: 'PartnerCtrl',
			    resolve: {
			    	logincheck: function(AuthService, $location) {
						var loginFlg = AuthService.isLoggedIn();
						if (loginFlg == true) {
        					var login = AuthService.getLoggedInUser();
        					return AuthService.checkLogin();
        				}
    					else {
    						$location.path("/login");
    					}			    		
			    	}
			    }	      
			  }).	
			  when('/partner/pricingupload', {
			    templateUrl: 'partials/pricinguploads.html',
			    controller: 'PartnerCtrl',
			    resolve: {
			    	logincheck: function(AuthService, $location) {
						var loginFlg = AuthService.isLoggedIn();
						if (loginFlg == true) {
        					var login = AuthService.getLoggedInUser();
        					return AuthService.checkLogin();
        				}
    					else {
    						$location.path("/login");
    					}			    		
			    	}
			    }	      
			  }).
			  when('/partner/retailerbuy', {
			    templateUrl: 'partials/buy.html',
			    controller: 'RetailerCtrl'      
			  }).	
			  when('/personal/traffichistory', {
			    templateUrl: 'partials/traffichistory.html',
			    controller: 'RetailerCtrl'      
			  }).	
			  when('/personal/topuphistory', {
			    templateUrl: 'partials/topuphistory.html',
			    controller: 'TopupHistoryCtrl'      
			  }).	
			  when('/personal/activate/:hashval', {
			  		templateUrl: 'partials/activate.html',
			    	controller: 'ActivationCtrl',
			    	resolve: {
            			activationDetails: function(activationService, $route) {
							var hashValue = $route.current.params.hashval;
            				console.log("HashValue::"+hashValue);
      						return activationService.getDetails(hashValue);    		
    					}
        			}
			  }).
			  when('/partner/changepassword/:login', {
			  		templateUrl: 'partials/changepassword.html',
			    	controller: 'RetailerCtrl',
			    	resolve: {
     				   test: function ($route) { 
     				   		$route.current.params.loginValue = $route.current.params.login;
     				   }
    				}			  
			  }).
			  when('/personal/changepassword', {
			    templateUrl: 'partials/password.html',
			    controller: 'LoginCtrl'      
			  }).
			  when('/personal/accountsummary', {
			    	templateUrl: 'partials/accountsummary.html',
			    	controller: 'AccountSummaryCtrl',
		    		resolve: {
        				accountSummaryDetails: function(activationService) {
				      		return activationService.getAccountSummaryDetails();
						}
    				}			          
			  }).		
			  when('/personal/topup', {
			    	templateUrl: 'partials/topup.html',
			    	controller: 'TopupCtrl',
		    		resolve: {
        				topupPricingDetails: function(activationService) {
				      		return activationService.getTopupDetails();
						}
    				}			          
			  }).				  	  
			  when('/personal/logout', {
			    templateUrl: 'partials/login.html',
			    controller: 'LoginCtrl',
		    	resolve: {
        			activationDetails: function(AuthService) {
  						return AuthService.logout();    		
					}
    			}			          
			  }).				  	
			  otherwise({
			    redirectTo: '/home'
			  });

			$translateProvider.translations('en', {
				FIRST_NAME: 'First Name',
				BUY_IDENTITY_TYPE: 'Identity Type',
				BUY_PASSPORT: 'Passport',
				BUY_OTHERS: 'Others',
				BUY_UPLOAD_IMAGE: 'Upload Image',
				BUY_EMAIL: 'Email',
				NAV_HOME: 'Home',
				NAV_LOGIN: 'Login',
				NAV_ENGLISH: 'English',
				NAV_CHINESE: 'Chinese',
				
				MENU_CHANGEPASSWORD: 'Change Password',
				MENU_LOGOUT: 'Logout',
				MENU_TRAFFICHISTORY: 'Traffic History',
				MENU_PERSONALINFO: 'Personal Information',
				MENU_BUYDEVICE: 'Buy Device',
				MENU_ACCOUNTSUMMARY: 'Account Summary',
				MENU_TOPUP: 'Top up',
				MENU_TOPUPHISTORY: 'Top-up History',
				MENU_RETAILERINFO: 'Retailer Information',
				MENU_PARTNERINFO: 'Partner Information',
				MENU_USERINFO: 'Customer Information',
				
				PERSONAL_PARTNER_ID: 'Partner Id',
				PERSONAL_PARTNER_HEADER: 'Partner Information',
				PERSONAL_PARTNER_FIRST_NAME: 'First Name',
				PERSONAL_PARTNER_LAST_NAME: 'Last Name',
				PERSONAL_PARTNER_EMAIL: 'Email',
				PERSONAL_PARTNER_ADDRESS: 'Address',
				PERSONAL_PARTNER_SUBMIT: 'Submit',
				PERSONAL_PARTNER_CANCEL: 'Cancel'				
				
			});
			$translateProvider.translations('ch', {
				FIRST_NAME: '名字',
				BUY_IDENTITY_TYPE: 'Identity Type (CH)',
				BUY_PASSPORT: 'Passport (CH)',
				BUY_OTHERS: 'Others (CH)',
				BUY_UPLOAD_IMAGE: 'Upload Image (CH)',
				BUY_EMAIL: 'Email (CH)',
				NAV_HOME: '文字不来',
				NAV_LOGIN: '登录',
				NAV_ENGLISH: '英文',
				NAV_CHINESE: '中文',
				
				MENU_CHANGEPASSWORD: '更换登录密码',
				MENU_LOGOUT: '登出',
				MENU_TRAFFICHISTORY: '网络使用量记录',
				MENU_PERSONALINFO: '个人资料',
				MENU_BUYDEVICE: '购买设备',
				MENU_ACCOUNTSUMMARY: '用户资料简介',
				MENU_TOPUP: '充值',
				MENU_TOPUPHISTORY: '充值记录'	,
				MENU_RETAILERINFO: 'Retailer Information',
				MENU_PARTNERINFO: 'Partner Information',
				MENU_USERINFO: 'Customer Information',
				
				PERSONAL_PARTNER_ID: 'Partner Id',
				PERSONAL_PARTNER_HEADER: 'Partner Information',
				PERSONAL_PARTNER_FIRST_NAME: '姓',
				PERSONAL_PARTNER_LAST_NAME: '名',
				PERSONAL_PARTNER_EMAIL: '电邮地址',
				PERSONAL_PARTNER_ADDRESS: '地址',
				PERSONAL_PARTNER_SUBMIT: '呈交',
				PERSONAL_PARTNER_CANCEL: '取消'																			
			});

			$translateProvider.preferredLanguage('en');
	  }

 ]);
 
sampleApp.controller('PersonalCtrl', function ($scope, $http, personalDetails, AuthService ) {

		$scope.partnerupdate = {};
		var originalPartner;		
	 	if (personalDetails != null) {
	 		if (personalDetails.data == "") {
				personalDetails.data = AuthService.getProfile();
		 	} else {
	 			AuthService.setProfile(personalDetails.data);	 				
		 	}
		}
	 	if (personalDetails != null) {
	 		
	 		if (AuthService.getCurrentRole() == 'Partner') {
	 			$scope.partner = {};
	 			$scope.partneroriginal = {};
		 		$scope.partner.fullName = personalDetails.data.fullName;
		 		$scope.$parent.loggedInUserName = personalDetails.data.fullName;
				$scope.partner.firstName = personalDetails.data.firstName;
				$scope.partner.lastName = personalDetails.data.lastName;
				$scope.partner.email = personalDetails.data.email;
				$scope.partner.fullAddress = personalDetails.data.fullAddress;
				$scope.partner.partnerId = personalDetails.data.partnerId;
				originalPartner = angular.copy($scope.partner);
			}
	 		if (AuthService.getCurrentRole() == 'Retailer') {
	 			$scope.retailer = {};
		 		$scope.retailer.fullName = personalDetails.data.fullName;
		 		$scope.$parent.loggedInUserName = personalDetails.data.fullName;
				$scope.retailer.email = personalDetails.data.email;
				$scope.retailer.fullAddress = personalDetails.data.fullAddress;
				$scope.retailer.retailerId = personalDetails.data.retailerId;	 		
	 		}
	 		if (AuthService.getCurrentRole() == 'User') {
	 			$scope.customer = {};
	 			$scope.customeroriginal = {};
		 		$scope.customer.fullName = personalDetails.data.fullName;
		 		$scope.$parent.loggedInUserName = personalDetails.data.fullName;
				$scope.customer.email = personalDetails.data.email;
				$scope.customer.addressLine1 = personalDetails.data.addressLine1;
				$scope.customer.addressLine2 = personalDetails.data.addressLine2;				
				$scope.customer.addressLine3 = personalDetails.data.addressLine3;				
				$scope.customer.idNumber = personalDetails.data.idNumber;
				$scope.customer.imei = personalDetails.data.imei;
				$scope.customer.idType = personalDetails.data.idType;
				$scope.country = personalDetails.data.country;
				$scope.customer.state = personalDetails.data.state;
				$scope.customer.state = personalDetails.data.city;
				$scope.customer.mobileNumber = personalDetails.data.mobileNumber;
				$scope.customer.zipCode  = personalDetails.data.zipCode;
				$scope.customeroriginal = $scope.customer;
	 		}	 					
	 	}
	 	$scope.doPartnerUpdateCancel = function() {
			$scope.partner = angular.copy(originalPartner);
	 	}
	 	
		$scope.doRetailerUpdate = function() {
                $http({
                        method : 'POST',
                        url : 'personal/save'
                }).success(function(data, status, headers, config) {
                        $scope.person = data;
                }).error(function(data, status, headers, config) {
                });
        };
		$scope.doPartnerUpdate = function() {
				var dataObj = {
					firstName : $scope.partner.firstName,
					fullAddress: $scope.partner.fullAddress,
					lastName: $scope.partner.lastName,
					email: $scope.partner.email,
					partnerId: $scope.partner.partnerId
				};				
                $http({
                        method : 'POST',
                        url : '/GlocalMe/partner/update',
                        data: dataObj
                }).success(function(data, status, headers, config) {
                	console.log(data);
                	if (data.STATUS=='SUCCESS') {
                		$scope.partnerupdate.status = 'SUCCESS';
                		$scope.partnerupdate.msg = data.MESSAGE;
                	} else {
                		$scope.partnerupdate.status = 'FAILED';
                		$scope.partnerupdate.msg = data.MESSAGE;                	
                	}
                }).error(function(data, status, headers, config) {
                		$scope.partnerupdate.status = 'FAILED';                
                });
        };
		$scope.doCustomerUpdate = function() {
                $http({
                        method : 'POST',
                        url : 'personal/save'
                }).success(function(data, status, headers, config) {
                        $scope.person = data;
                }).error(function(data, status, headers, config) {
                });
        };                
});


sampleApp.controller('CoverageCtrl', function ($scope) {

});

sampleApp.controller('HomePageCtrl', function ($scope,$rootScope) {

});

sampleApp.controller('TopupHistoryCtrl', function ($scope, $http,  $location, logincheck) {
	
	if (logincheck != null) {
		console.log(logincheck.data);
		var login = logincheck.data;
		if (angular.isDefined(login)) {
			if (login.authenticated == false) {
				$location.path("/login");
			}
		}
	}

	$scope.status = {
    	opened: false,
    	opened1: false
  	};
	$scope.dateOptions = {
		formatYear: 'yy',
		startingDay: 1
	};
	$scope.open = function($event) {
		$scope.status.opened = true;
	};	
	$scope.open1 = function($event) {
		$scope.status.opened1 = true;
	};
	
	$scope.topupgridOptions = {
	    enableSorting: true,
	    columnDefs: [
	      { displayName: 'Topup Type', name: 'topupType' },
	      { displayName: 'Package Country', name: 'packageCountry'  },
	      { displayName: 'Selected Package', name: 'selectedPackage' },
	      { displayName: 'Price List Date', name: 'priceListDate' ,type: 'date', cellFilter: 'date:\'yyyy-MM-dd HH:mm:ss.sssZ\'' },	    	      	      
	      { displayName: 'Currency', name: 'currency' },
	      { displayName: 'Topup Amount', name: 'topupAmount' }     	      
	    ],
	    onRegisterApi: function( gridApi ) {
	      $scope.grid1Api = gridApi;
	    }
  	};
  	
	$scope.doEnquire = function() {
		$scope.enquire = {};
		if (angular.isUndefined($scope.startDt)) {
			alert('Please enter a valid Start Date');
			return;
		}

		if (angular.isUndefined($scope.endDt)) {
			alert('Please enter a valid End Date');
			return;
		}

		if ($scope.startDt === undefined || $scope.startDt === null) {
			alert('Please enter a valid Start Date');
			return;		
		}
		
		if ($scope.endDt === undefined || $scope.endDt === null) {
			alert('Please enter a valid End Date');
			return;		
		}

		var startDateObj = new Date($scope.startDt);
		var endDateObj = new Date($scope.endDt);
		var startDateTime = startDateObj.getTime();
		var endDateTime = endDateObj.getTime();

		var res = $http({
			method: "POST",
			url: '/GlocalMe/personal/topuphistory?fromDate='+startDateTime+'&toDate='+endDateTime
		});
		res.success(function(data, status, headers, config) {
			console.log("doEnquire - Topup Click Status");
			console.log(data);
			if (data.STATUS=='SUCCESS') {
				if (data.TOTALRECORDCOUNT == '0' || data.TOTALRECORDCOUNT == 0) {
					alert('No Topup History Records Found');
				} else {
					if (angular.isDefined(data.RESPONSE_DATA) && data.RESPONSE_DATA != null) {
						$scope.topupgridOptions.data = data.RESPONSE_DATA;
					}				
				}
			} else {		
				$scope.enquire.status = "FAILED";
			}			
		});
		res.error(function(data, status, headers, config) {
			console.log("doEnquire - Topup Click Status");
			console.log(data);
			$scope.enquire.status = "FAILED";
		});	
		
	}
	  	
});
sampleApp.controller('MainCtrl', function ($scope, $translate,AuthService) {
	$scope.$watch(AuthService.isLoggedIn, function(newValue,oldValue){
		console.log("New Value::"+newValue);
		console.log("old Value::"+oldValue);
		if (oldValue==true && newValue==false) {
			$scope.isloggedin=false;
		}
	});
	$scope.changeLanguage = function (langKey) {
		$translate.use(langKey);
	};	
});
