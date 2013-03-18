(function($) {

	CarregaDados = {
			
        carregar : function() {
        	$.ajax({  
		        dataType: 'json',  
		        type: 'get',  
		        url: '/query',  
		        success:
		        	function (resultado) {
		        	console.info(resultado);},
		        }  
		);  
    	}
	}
    	
})(jQuery);