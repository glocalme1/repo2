sampleApp.controller('ActivationCtrl', function ($scope, $http,  $location, activationDetails) {
	$scope.login = activationDetails.data.result;
	console.log($scope.activationDetails);
	$scope.setpassword = {};
	
	$scope.doActivate = function() {
		$scope.activatestatus = "";
		console.log('doActivate');
		console.log($scope.login);
	 	$http({
	 		method: "POST",
	 		url: "/GlocalMe/personal/activate?imei="+$scope.imei+"&serialno="+$scope.serialno+"&login="+$scope.login,
			transformResponse: function(data, headersGetter) {
			      return {result: data};
			}            
        })
        .success(function(response, status, headers, config){
        	console.log(response);
        	if (response.result == 'SUCCESS') {
        		var newView = "/partner/changepassword/"+$scope.login;
			 	$location.path(newView);
			}
			else {
				$scope.activatestatus = "ERROR";
			}
        })
        .error(function(response, status, headers, config){
			console.log(response);
			$scope.activatestatus = "ERROR";
        });
	}
	
});

sampleApp.controller('AccountSummaryCtrl', function ($scope, $http,  $location, accountSummaryDetails) {
	$scope.summary = {};
	$scope.summary.status = "";
	if (accountSummaryDetails != null) {
		if (accountSummaryDetails.data.STATUS == 'SUCCESS') {
			if (accountSummaryDetails.data.RESPONSE_DATA != null) {
				$scope.latestPosition = accountSummaryDetails.data.RESPONSE_DATA.latestPosition;
				$scope.lastLoginTime = accountSummaryDetails.data.RESPONSE_DATA.lastLoginTime;
				$scope.lastLogoutTime = accountSummaryDetails.data.RESPONSE_DATA.lastLogoutTime;
				$scope.lastPosition = accountSummaryDetails.data.RESPONSE_DATA.lastPosition;
				$scope.lastTopupAmount = accountSummaryDetails.data.RESPONSE_DATA.lastTopupAmount;
			}
		}
		else {
			$scope.summary.status = "FAILED";
			$scope.summary.msg = accountSummaryDetails.data.MESSAGE;
			$scope.latestPosition = "";
			$scope.lastLoginTime = "";
			$scope.lastLogoutTime = "";
			$scope.lastPosition = "";
			$scope.lastTopupAmount = "";
		}
	}
});

sampleApp.controller('TopupCtrl', function ($scope, $http,  $location, topupPricingDetails) {
	$scope.summary = {};
	$scope.summary.status = "";
	
	$scope.countrylistForPackages = {};
	$scope.topupDetailsForPackages = {};
	
	$scope.packageListForCountry = {};
	$scope.packagecost = "";
	if (topupPricingDetails != null) {
		console.log(topupPricingDetails);
		if (topupPricingDetails.data.STATUS == 'SUCCESS') {
			$scope.countrylistForPackages = topupPricingDetails.data.COUNTRYWITHPACKAGES;
			$scope.topupDetailsForPackages = topupPricingDetails.data.COUNTRYPACKAGES;
			$scope.packagePricing = topupPricingDetails.data.PACKAGEPRICING;
		}
		else {
			/*
			$scope.summary.status = "FAILED";
			$scope.summary.msg = accountSummaryDetails.data.MESSAGE;
			$scope.latestPosition = "";
			$scope.lastLoginTime = "";
			$scope.lastLogoutTime = "";
			$scope.lastPosition = "";
			$scope.lastTopupAmount = "";
			*/
		}
	}
	
	
	$scope.selectCountry = function() {
		console.log("Selected Country is called");
		var selectedCountry = $scope.selectedCountry;
		//console.log($scope.selectedPackage);
		if (angular.isDefined($scope.selectedPackage)) {
			var selectedCountryPackage = selectedCountry + ":" + $scope.selectedPackage;
			$scope.packagecost = $scope.packagePricing[selectedCountryPackage];
		}
		$scope.packageListForCountry = $scope.topupDetailsForPackages[selectedCountry];
	}
	$scope.selectPackage = function() {
		var selectedCountry = $scope.selectedCountry;
		var selectedCountryPackage = selectedCountry + ":" + $scope.selectedPackage;
		$scope.packagecost = $scope.packagePricing[selectedCountryPackage];
	}	
	
	$scope.resetType = function() {
		console.log("Reset is called");
		$scope.selectedCountry = "";
		$scope.selectedPackage = "";
		$scope.packagecost = "";
	}
	
	$scope.doTopup = function() {
		console.log("Do Toup is called");
		$scope.topup = {};
		$scope.topup.status = "";
	 	$http({
	 		method: "POST",
	 		url: "/GlocalMe/personal/topup?type="+$scope.type+"&selectedCountry="+$scope.selectedCountry+"&selectedPackage="+$scope.selectedPackage+"&topupAmount="+$scope.topupAmount+"&packagecost="+$scope.packagecost
        })
        .success(function(response, status, headers, config){
        	console.log(response);
        	$scope.topup.status = response.STATUS;
        	$scope.topup.message = response.MESSAGE;	        	

        })
        .error(function(response, status, headers, config){
			console.log(response);
        	$scope.topup.status = 'FAILED';	
        	$scope.topup.message = response.MESSAGE;		
        });		
	}
});


sampleApp.controller('RetailerCtrl', function ($scope, $http, $location, $timeout, $rootScope, $routeParams, AuthService, logincheck) {

	if (logincheck != null) {
		console.log(logincheck.data);
		var login = logincheck.data;
		if (angular.isDefined(login)) {
			if (login.authenticated == false) {
				$location.path("/login");
			}
		}
	}
	$scope.retailer = {};
	$scope.retailer.idType = "NRIC";

	$scope.setpassword = {};
	$scope.login = $routeParams.loginValue;
	
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
		var dataObj = {
			fromDate : startDateTime,
			toDate: endDateTime,
			requestType: "DateRange",
			perPageCountStr: 20,
			currentPage: 1
		};	
		var res = $http({
			method: "POST",
			url: TRAFFICHISTORY_DATERANGE+'&fromDate='+startDateTime+'&toDate='+endDateTime+'&perPageCountStr='+20+'&currentPage=1'
		});
		res.success(function(data, status, headers, config) {
			console.log("doEnquire Click Status");
			console.log(data);
			if (data.STATUS=='SUCCESS') {
				if (data.TOTALRECORDCOUNT == '0' || data.TOTALRECORDCOUNT == 0) {
					alert('No Traffic History Records Found');
				} else {
					if (angular.isDefined(data.RESPONSE_DATA) && data.RESPONSE_DATA != null) {
						$scope.gridOptions.data = data.RESPONSE_DATA;
					}				
				}
			}
			$scope.enquire.status = "SUCCESS";			
		});
		res.error(function(data, status, headers, config) {
			console.log("doEnquire Click Status");
			console.log(data);
			$scope.enquire.status = "FAILED";
		});	
	}
	
	$scope.doToday = function() {
		$scope.enquire = {};
		var res = $http({
			method: "POST",
			url: '/GlocalMe/personal/traffichistory?requestType=Today'+'&perPageCountStr='+20+'&currentPage=1'
		});
		res.success(function(data, status, headers, config) {
			console.log("doToday Click Status");
			console.log(data);
			if (data.STATUS=='SUCCESS') {
				if (data.TOTALRECORDCOUNT == '0' || data.TOTALRECORDCOUNT == 0) {
					alert('No Traffic History Records Found');
				} else {			
					if (angular.isDefined(data.RESPONSE_DATA) && data.RESPONSE_DATA != null) {
						$scope.gridOptions.data = data.RESPONSE_DATA;
					}
				}
			}
			$scope.enquire.status = "SUCCESS";			
		});
		res.error(function(data, status, headers, config) {
			console.log("doToday Click Status");
			console.log(data);
			$scope.enquire.status = "FAILED";
		});		
	}
	
	$scope.doWeek = function() {
		$scope.enquire = {};
		var res = $http({
			method: "POST",
			url: '/GlocalMe/personal/traffichistory?requestType=Week'+'&perPageCountStr='+20+'&currentPage=1'
		});
		res.success(function(data, status, headers, config) {
			console.log("doWeek Click Status");
			console.log(data);
			if (data.STATUS=='SUCCESS') {
				if (data.TOTALRECORDCOUNT == '0' || data.TOTALRECORDCOUNT == 0) {
					alert('No Traffic History Records Found');
				} else {			
					if (angular.isDefined(data.RESPONSE_DATA) && data.RESPONSE_DATA != null) {
						$scope.gridOptions.data = data.RESPONSE_DATA;
					}
				}
			}
			$scope.enquire.status = "SUCCESS";				
		});
		res.error(function(data, status, headers, config) {
			console.log("doWeek Click Status");
			console.log(data);
			$scope.enquire.status = "FAILED";
		});		
	}
		
	$scope.doMonth = function() {
		$scope.enquire = {};
		var res = $http({
			method: "POST",
			url: '/GlocalMe/personal/traffichistory?requestType=Month'+'&perPageCountStr='+20+'&currentPage=1'
		});
		res.success(function(data, status, headers, config) {
			console.log("doMonth Click Status");
			console.log(data);
			if (data.STATUS=='SUCCESS') {
				if (data.TOTALRECORDCOUNT == '0' || data.TOTALRECORDCOUNT == 0) {
					alert('No Traffic History Records Found');
				} else {			
					if (angular.isDefined(data.RESPONSE_DATA) && data.RESPONSE_DATA != null) {
						$scope.gridOptions.data = data.RESPONSE_DATA;
					}
				}
			}
			$scope.enquire.status = "SUCCESS";			
		});
		res.error(function(data, status, headers, config) {
			console.log("doMonth Click Status");
			console.log(data);
			$scope.buy.status = "FAILED";
		});		
	}		
	
	
	$scope.gridOptions = {
	    enableSorting: true,
	    enableColumnResizing: true,
	    columnDefs: [
	      { displayName: 'Start Time' , name: 'loginTime' ,type: 'date', cellFilter: 'date:\'yyyy-MM-dd HH:mm:ss.sssZ\''},
	      { displayName: 'End Time' , name: 'logoutTime',type: 'date', cellFilter: 'date:\'yyyy-MM-dd HH:mm:ss.sssZ\'' },
	      { displayName: 'Country' , name: 'countryCode' },
	      { displayName: 'Volume (KB)' , name: 'totalFlows'  }
	    ],
	    onRegisterApi: function( gridApi ) {
	      $scope.grid1Api = gridApi;
	    }
  	};
	
	
	//$scope.login = AuthService.getLoggedInUser();
	if (AuthService.getProfile() != null) {
		$scope.roc = AuthService.getProfile().retailerId;
	}

	$scope.doSetPassword = function() {
		console.log($scope.login);
		console.log($scope.setpassword.passwd);
		$scope.setpassword = {};
		$scope.setpassword.status = "";
	 	$http({
	 		method: "POST",
	 		url: "/GlocalMe/personal/setpassword?login="+$scope.login+"&passwd="+$scope.passwd
	 		/*
			transformResponse: function(data, headersGetter) {
			      return {result: data};
			} 
			*/           
        })
        .success(function(response, status, headers, config){
        	console.log(response);
        	if (response.STATUS == 'SUCCESS') {
        	    /*
	        	var User = {"authenticated":true, "userName": $scope.login};
	        	AuthService.setCurrentUser(User);
	        	//var newview = "/personal/getinfo/"+$scope.login;
	        	var newview = "/personal/getinfo";
				$location.path(newview);
				*/
				$scope.setpassword.status = "SUCCESS";
			}
        })
        .error(function(response, status, headers, config){
        	// var newview = "/personal/getinfo/"+$scope.login;        
			// console.log(response);
			// $location.path(newview);
			$scope.setpassword.status = "FAILED";			
        });
	}	

	
	$scope.doScan = function() {
		console.log("do Scan is called");
	    var post_data = new FormData();    
        post_data.append( 'file', $('input[type=file]')[0].files[0]);
        
        var obj = {
        	fullName:'Test',
        	addressLine1:'Address1'
        };
		post_data.append('obj',obj);
		$http.post("/GlocalMe/retailer/scanid", post_data, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(){
        })
        .error(function(){
        });		
	}
	
	function openSaveAsDialog(filename, content, mediaType) {
		var blob = new Blob([content], {type: mediaType});
		saveAs(blob, filename);
 	}
	
	$scope.print = function() {
		console.log("Print is called");
		var dataObj = {
				fullName : $scope.retailer.fullName,
				addressLine1: $scope.retailer.addressLine1,
				addressLine2: $scope.retailer.addressLine2,
				addressLine3: $scope.retailer.addressLine3,
				idNumber : $scope.retailer.id,
				email: $scope.retailer.email,
				imei: $scope.retailer.imei,
				mobileNumber: $scope.retailer.mobileNumber,
				idType: $scope.retailer.idType,
				roc: $scope.roc
		};
	
		var res = $http({
			method: "POST",
			url: '/GlocalMe/retailer/print', 
			data: dataObj,
			responseType: 'arraybuffer'
		});
				
		res.success(function(data, status, headers, config) {
			console.log(data);
 			openSaveAsDialog("Registration_output.pdf", data, "application/pdf");

		});
		res.error(function(data, status, headers, config) {
			console.log("OOPS!!");
		});		
	}
	
	
	$scope.saveRetailer = function() {
		console.log("Save Retailer is called");
				
	}

	$scope.saveCustomer = function() {
		$scope.buy = {};
		$scope.buy.status = "";
		$scope.buy.msg = "";
		
		var dataObj = {
				fullName : $scope.retailer.fullName,
				addressLine1: $scope.retailer.addressLine1,
				addressLine2: $scope.retailer.addressLine2,
				addressLine3: $scope.retailer.addressLine3,
				idNumber : $scope.retailer.id,
				email: $scope.retailer.email,
				imei: $scope.retailer.imei,
				mobileNumber: $scope.retailer.mobileNumber,
				idType: $scope.retailer.idType,
				state: $scope.retailer.state,
				zipCode: $scope.retailer.zipCode,
				city: $scope.retailer.city,
				country: $scope.retailer.country				
		};
		
		var res = $http({
			method: "POST",
			url: '/GlocalMe/retailer/save', 
			headers: { 'Content-Type': undefined },			
			// for which we'll need to encapsulate the model data in 'FormData'
            transformRequest: function (data) {
                var formData = new FormData();
                //need to convert our json object to a string version of json otherwise
                // the browser will do a 'toString()' on the object which will result 
                // in the value '[Object object]' on the server.
                formData.append("obj", angular.toJson(data.model));
				formData.append("file", $('input[type=file]')[0].files[0]);
				/*
                //now add all of the assigned files
                for (var i = 0; i < data.files; i++) {
                    //add each file to the form data and iteratively name them
                    formData.append("file" + i, data.files[i]);
                }
				*/
                return formData;
            },
            //Create an object that contains the model and files which will be transformed
            // in the above transformRequest method
            data: { model: dataObj}
		});
		res.success(function(data, status, headers, config) {
			console.log("Save Click Status");
			console.log(data);
			$scope.buy.status = data.STATUS;		
			$scope.buy.msg = data.MESSAGE;				
		});
		res.error(function(data, status, headers, config) {
			console.log("Save Click Status");
			console.log(data);
			$scope.buy.status = data.STATUS;
			$scope.buy.msg = data.MESSAGE;
		});		
	}	
	
	$scope.cancel = function() {
		console.log("Cancel is called");
	}
	
});