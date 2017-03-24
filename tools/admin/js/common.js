
	function postRequest(url, param, callback){
		$.ajax( {    
			url:url,
			data: param,    
			type:'post',    
			cache:false,    
			dataType:'json',    
			success:function(data) {
				
				if (typeof callback != 'undefined') {
					callback(data);
				}
			 },    
			 error : function() {    
				  alert("网络异常");   
			 }    
		}); 
	};
