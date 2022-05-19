
function checkFolderAuth(prj_id,folder_id){
	
	let auth = {};
	
		$.ajax({
    	    url: '../checkFolderAuth.do',
    	    type: 'POST',
    	    async:false,
    	    data:{
    	    	prj_id: prj_id,
    	    	folder_id : folder_id
    	    },
    	    success: function onData (data) {
    	    	if(data == "<script>alert('There is no Read Permission');</script>"){
    	    		auth.r = "N";
    	    		auth.w = "N";
    	    		auth.d = "N";
    	    		
    	    		console.log("intercepter",auth);
    	    		return auth;
    	    	}
    	    	
    	    	if(!data.model){
    	    		auth.r = "N";
    	    		auth.w = "N";
    	    		auth.d = "N";
    	    		
    	    		console.log("not mapping",auth);
    	    		return auth;
    	    	}
    	    	
    	    	auth.r = data.model.auth_check_r;
    	    	auth.w = data.model.auth_check_w;
    	    	auth.d = data.model.auth_check_d;
    	    	
    	    	
    	    },
    	    error: function onError (error) {
    	        console.error(error);
    	    }
    	});
		
	console.log(auth);
	return auth;
		
}