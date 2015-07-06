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
			databaseName : {
				minlength : 4,
				maxlength : 40,
				regex : "^([A-Za-z])[A-Za-z]"
			},
			rootPassword : {
				minlength : 8,
				maxlength : 40,
				regex : "^([A-Za-z0-9]+)*$"
			},
			repeatRootPassword : {
				equalTo : "#rootPassword"
			},
			username : {
				minlength : 4,
				maxlength : 40,
				regex : "^([A-Za-z])[A-Za-z0-9]"
			},
			usernamePassword : {
				minlength : 8,
				maxlength : 40,
				regex : "^([A-Za-z0-9]+)*$"
			},
			repeatusernamePassword : {
				equalTo : "#usernamePassword"
			},
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
	
	// Mostro o oscuro il div del certificato verificando lo
	// stato del checkbox
	$('#autoscaling').change(function() {
		var isChecked = $('#autoscaling').is(":checked");
		if (isChecked) {
			$('#autoscalingDiv').removeClass('hide');
			$('#maxFlavor').prop('required', true);
			$('#maxVolume').prop('required', true);

		} else {
			$('#autoscalingDiv').addClass('hide');
			$('#maxFlavor').prop('required', false);
			$('#maxVolume').prop('required', false);
		}
	});

	// Mostro o oscuro il div del certificato verificando lo
	// stato del checkbox
	$('#otherUser').change(function() {
		var isChecked = $('#otherUser').is(":checked");
		if (isChecked) {
			$('#otherUserDiv').removeClass('hide');
			$('#username').prop('required', true);
			$('#usernamePassword').prop('required', true);
			$('#repeatUsernamePassword').prop('required', true);
		} else {
			$('#otherUserDiv').addClass('hide');
			$('#username').prop('required', false);
			$('#usernamePassword').prop('required', false);
			$('#repeatUsernamePassword').prop('required', false);
		}
	});

	$('#volume').change(function() {
		$('#maxVolume').attr('value', $('#volume').val());
	});

});



function fillTable() {
	
	$('#tableServiceName').html($('#serviceName').val());
	$('#tableDescription').html($('#description').val());
	$('#tableDomainName').html($('#url').val() + $('#urlName').text());
	$('#tableEmail').html($('#notificationEmail').val());
	
	$('#tableZoneSelect').html($('#zoneSelect option:selected').text());
	$('#tableProfile').html($('#qosSelect option:selected').text());
	$('#tableFlavor').html($('#flavorSelect option:selected').text());
	
	
	
	$('#tableDisk').html($('#volume').val());
//	if ($('#encryptedDisk').is(":checked")) {
//		$('#tableEncryptedDisk').html('Si');
//	} else {
//		$('#tableEncryptedDisk').html('No');
//	}
//	if ($('#autoscaling').is(":checked")) {
//		$('#tableAutoscaling').html(
//				'Max Flavor: ' + $('#maxFlavor option:selected').text()
//				+ '<br>' + 'Dimensione massima disco: '
//				+ $('#maxVolume').val());
//
//	} else {
//		$('#tableAutoscaling').html('No');
//	}

	$('#tableDBMS').html($('#dbmsSelect option:selected').text());
	$('#tableDatabaseName').html($('#databaseName').val());

//	$('#tableBackup').html($('#backup option:selected').text());
//	if ($('#publicip').is(":checked")) {
//		$('#tablePublicIP').html('Si');
//	} else {
//		$('#tablePublicIP').html('No');
//	}
	// $('#tableRoot').html($('#rootUsername').val());
	
	
	var dbms = $('#dbmsSelect option:selected').text()
	if(dbms.toLowerCase().indexOf("mysql") > -1){
		$('#tableAdminUsername').html("root");
	} else {
		$('#tableAdminUsername').html("postgres");
	}
	
	
	if ($('#otherUser').is(":checked")) {
		$('#tableUsername').html(
				'Nome: ' + $('#username').val() + '<br>'
				+ 'Diritti di accesso: '
				+ $('#permission option:selected').text());
	} else {
		$('#tableUsername').html('No');
	}
}
