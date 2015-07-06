var prevRadio = null;

function radioDivShow(radioValue){
		
	switch(radioValue){

		case 'localApp':
	 			
			$('#sourceLabel').prop('required', true);
			$('#sourceFile').prop('required', true);
	 			
			//$('#urlStorage').prop('required', false);
	 		//$('#urlStorage').parent().removeClass('has-error');
	 		//$('#sourceLabelPRISMA').prop('required', false);
	 		//$('#sourceLabelPRISMA').parent().removeClass('has-error');
	 		
	 		//$('#urlStorage-error').addClass('hidden');
	 		//$('#sourceLabelPRISMA-error').addClass('hidden');
	 			
	 		$('#source-localApp').removeClass("hidden");		
	 		break;

		case 'prismaApp':
			
	 		//$('#urlStorage').prop('required', true);
	 		//$('#sourceLabelPRISMA').prop('required', true);
	 			
	 		$('#sourceFile').prop('required', false);
	 		$('#sourceLabel').prop('required', false);
	 		$('#sourceLabel').parent().removeClass('has-error');
	 		$('#sourceFile-error').addClass('hidden');
 			$('#sourceLabel-error').addClass('hidden'); 			
	 		$('#source-prismaApp').removeClass("hidden");	
	 		break;
		case 'privateApp':

			$('#source-privateApp').removeClass("hidden");
			$("#inputPrivateIdSpan").text("Nessuna");
	 		$("#inputPrivateId").val("");
	 		break;
	 
		case 'publicApp':
	 		$('#source-publicApp').removeClass("hidden");
	 		$("#inputPublicIdSpan").text("Nessuna");
	 		$("#inputPublicId").val("");
			break;
	 }
	 
	if(prevRadio != null){
		var id = '#source-' + prevRadio;
	 	$(id).addClass("hidden");
	 	$(id).children('input').each(function () {
	 	    //alert(this.value); // "this" is the current element in the loop
	 	});
	 }
	prevRadio = radioValue;
}
