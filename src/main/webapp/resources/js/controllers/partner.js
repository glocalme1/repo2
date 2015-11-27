 sampleApp.controller('PartnerCtrl', ['$scope', '$http',  function($scope, $http, logincheck){

	if (logincheck != null) {
		console.log(logincheck.data);
		var login = logincheck.data;
		if (angular.isDefined(login)) {
			if (login.authenticated == false) {
				$location.path("/login");
			}
		}
	}

	$scope.upload = {}; 
 
 	$scope.doDevicesUpload = function() {
		console.log("do Devices Upload is called");
		$scope.upload.message = '';
		$scope.upload.status = '';
	    var post_data = new FormData();    
        post_data.append( 'file', $('input[type=file]')[0].files[0]);
        $http.post("/GlocalMe/partner/upload/devices", post_data, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
            /*
			transformResponse: function(data, headersGetter) {
			      return {result: data};
			}
			*/            
        })
        .success(function(response, status, headers, config){
        	$scope.upload.message=response.MESSAGE;
        	$scope.upload.status=response.STATUS;        	
        })
        .error(function(response, status, headers, config){
        	console.log(response);
        	$scope.upload.message=response.MESSAGE;
        	$scope.upload.status=response.STATUS;
        });
    }		
    
     $scope.doRetailersUpload = function() {
		console.log("do Retailers Upload is called");

		$scope.upload.message = '';
		$scope.upload.status = '';
				
	    var post_data = new FormData();    
        post_data.append( 'file', $('input[type=file]')[0].files[0]);
        $http.post("/GlocalMe/partner/upload/retailers", post_data, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(response, status, headers, config){
            $scope.upload.message=response.MESSAGE;
        	$scope.upload.status=response.STATUS;        
        })
        .error(function(response, status, headers, config){
            $scope.upload.message=response.MESSAGE;
        	$scope.upload.status=response.STATUS;
        });
    }
    
     $scope.doRetailerDevicesUpload = function() {
		console.log("do Retailer Devices Upload is called");
		$scope.upload.message = '';
		$scope.upload.status = '';
	
	    var post_data = new FormData();    
        post_data.append( 'file', $('input[type=file]')[0].files[0]);
        $http.post("/GlocalMe/partner/upload/retailerdevices", post_data, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(response, status, headers, config){
            $scope.upload.message=response.MESSAGE;
        	$scope.upload.status=response.STATUS;                
        })
        .error(function(response, status, headers, config){
            $scope.upload.message=response.MESSAGE;
        	$scope.upload.status=response.STATUS;        
        });
    }    
     $scope.doPricingUpload = function() {
		console.log("do Pricing Upload is called");
		$scope.upload.message = '';
		$scope.upload.status = '';		
	    var post_data = new FormData();    
        post_data.append( 'file', $('input[type=file]')[0].files[0]);
        $http.post("/GlocalMe/partner/upload/pricing", post_data, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(response, status, headers, config){
            $scope.upload.message=response.MESSAGE;
        	$scope.upload.status=response.STATUS;          
        })
        .error(function(response, status, headers, config){
            $scope.upload.message=response.MESSAGE;
        	$scope.upload.status=response.STATUS;          
        });
    }    
        
    
    
 }]);
 
