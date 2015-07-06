var $validator;

$(document).ready(function() {

	$validator = $("#DeployForm").validate({
	
		rules : {
			serviceName : {
				minlength : 4,
				maxlength : 16,
				unique: 'name',
				onkeyup: false,
				regex : "^[a-zA-Z0-9][-a-zA-Z0-9]+$"
			},
			description : {
				minlength : 4,
				maxlength : 400
			},
			url: {
				onkeyup: false,
				regex : "^[a-z0-9][a-z0-9-]+[a-z0-9]$",
				unique: 'dns'
			},
			notificationEmail : {
				regex : "^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+.)+([a-zA-Z0-9]{2,4})+$",
			},
			rootPassword : {
				minlength : 8,
				maxlength : 40,
				regex : "^([A-Za-z0-9]+)*$"
			},
			repeatRootPassword : {
				equalTo : "#rootPassword"
			}
		},
		errorPlacement : function(error, element) {},
		highlight : function(element, errorClass, validClass) {
			var rules = $(element).rules();
		    var errors ={};
			var genericError = false;

		    for (var method in rules ) {
		        var rule = { method: method, parameters: rules[method] };
		        try {
		            var result = $.validator.methods[method].call( this, element.value.replace(/\r/g, ""), element, rule.parameters );
		            errors[rule.method] = result;
		            if(rule.method != "unique") {
			        	if(result == false){
			        		genericError = true;
			        	}
			        }
		        } catch(e) {
		            console.log(e);
		        }
		    }
		    
		    if("unique" in errors) {
		    	if(errors["unique"] == false){
		    		console.log("field must be unique");
		    		$('#' + $(element).attr('id') + '-errorUnique').removeClass('hidden');
		    	} else {
		    		$('#' + $(element).attr('id') + '-errorUnique').addClass('hidden');
		    	}
		    	if(genericError){
					$('#' + $(element).attr('id') + '-error').removeClass('hidden');
				} else {
					$('#' + $(element).attr('id') + '-error').addClass('hidden');
				}
		    } else{
				$('#' + $(element).attr('id') + '-error').removeClass('hidden');

		    }
			$(element).parent().removeClass('has-success').addClass('has-error');
		},
		unhighlight : function(element, errorClass, validClass) {
			$('#' + $(element).attr('id') + '-errorUnique').addClass('hidden');
			$('#' + $(element).attr('id') + '-error').addClass('hidden');
			$(element).parent().removeClass('has-error').addClass('has-success');
		}
	});
});	


function fillTable(){
	$('#tableServiceName').html($('#serviceName').val());
	$('#tableDescription').html($('#description').val());
	$('#tableDomainName').html($('#url').val() + $('#urlName').text());
	$('#tableEmail').html($('#notificationEmail').val());
	
	$('#tableZoneSelect').html($('#zoneSelect option:selected').text());
	$('#tableProfile').html($('#qosSelect option:selected').text());
	$('#tableFlavor').html($('#flavorSelect option:selected').text());
}