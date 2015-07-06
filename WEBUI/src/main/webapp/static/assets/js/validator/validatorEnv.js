var $validator;

$(document).ready(function() {

	$validator = $("#DeployForm").validate({
				rules : {
					serviceName : {
						minlength : 4,
						maxlength : 16,
						regex : "^[a-zA-Z0-9][-a-zA-Z0-9]+$",
						unique: 'name',
						onkeyup: false,
					},
					description : {
						minlength : 4,
						maxlength : 400
					},
					notificationEmail : {
						regex : "^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+.)+([a-zA-Z0-9]{2,4})+$",
					},
					source: {
						appSource: "env"
					},
					url: {
						regex : "^[a-z0-9][a-z0-9-]+[a-z0-9]$",
						unique: 'dns',
						onkeyup: false
					}
				},
				errorPlacement : function(error, element) {;},
				highlight : function(element, errorClass, validClass) {
					if(element.id != "sourceLabelPRISMA" && element.id != "urlStorage"){
						$(element).parent().removeClass('has-success').addClass('has-error');
						$('#' + $(element).attr('id') + '-error').removeClass('hidden');
					}
				},
				unhighlight : function(element,	errorClass, validClass) {
					if(element.id != "sourceLabelPRISMA" && element.id != "urlStorage"){
						$(element).parent().removeClass('has-error').addClass('has-success');
						$('#' + $(element).attr('id') + '-error').addClass('hidden');
					}
				}
			});
});

function fillTable() {
	
	$('#tableName').html($('#serviceName').val());
	$('#tableDescription').html($('#description').val());

	$('#tableZoneSelect').html($('#zoneSelect option:selected').text());
	$('#tableProfile').html($('#qosSelect option:selected').text());
	$('#tableFlavor').html($('#flavorSelect option:selected').text());
	$('#tableDisk').html($('#volume').val());
	if ($('#secureConnection').is(":checked")) {
		$('#tableSecureConnection').html('');
	} else {
		$('#tableSecureConnection').html('No');
	}

	$('#tableAutoscaling').html($('#scalingSelect option:selected').text());

	$('#tableEmail').html($('#notificationEmail').val());

	$('#tableServerType').html($('#serverType option:selected').text() + " - " + $('#serverName option:selected').text());
	$('#tableDomainName').html($('#url').val() + $('#urlName').val());

	if ($('#publicip').is(":checked")) {
		$('#tablePublicIP').html('Si');
	} else {
		$('#tablePublicIP').html('No');
	}
	var appName = "";
	switch ($('input[name=source]:checked').val()) {
	
	case 'localApp':
		appName = $('#localFile').text();
		break;
	case 'prismaApp':
		appName = $('#urlStorage').val();
		break;
	case 'privateApp':
		appName = $('#inputPrivateIdSpan').text();
		break;
	case 'publicApp':
			appName = $('#inputPublicIdSpan').text();
			break;

	default:
		break;
	}
	
	$('#tableApp').html(appName);
}