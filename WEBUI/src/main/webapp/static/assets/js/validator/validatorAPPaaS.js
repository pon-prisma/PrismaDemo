$(document).ready(function() {

	/*var $validator = $("#pentahoForm").validate({
		rules: {
			serviceName: {
				minlength: 4,
				maxlength: 10,
				regex: "^([A-Za-z])[A-Za-z0-9-._-]"
			},
			consolePort: {
				range: [49152, 65535]
			},
			adminUsername: {
				minlength: 4,
				maxlength: 10,
				regex: "^([A-Za-z])[A-Za-z0-9-._-]"
			},
			adminPassword: {
				minlength: 8,
				maxlength: 20,
				regex: "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]))"
			},
			repeatAdminPassword: {
				equalTo: "#adminPassword"
			},
			consolePort: {
				range: [49152, 65535]
			},
			userPassword: {
				minlength: 8,
				maxlength: 20,
				regex: "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]))"
			},
			repeatUserPassword: {
				equalTo: "#userPassword"
			},
			portDB: {
				range: [49152, 65535]
			},
			dbSelected: {
				required: true
			}
		},
		errorPlacement: function(error, element) {
		},
		highlight: function(element, errorClass, validClass) {
			$(element).parent().removeClass('has-success').addClass('has-error');
			$('#' + $(element).attr('id') + '-error').removeClass('hidden');
		},
		unhighlight: function(element, errorClass, validClass) {
			$(element).parent().removeClass('has-error').addClass('has-success');
			$('#' + $(element).attr('id') + '-error').addClass('hidden');
		}
	});
*/

	$('#rootwizard').bootstrapWizard({
		'tabClass': 'nav nav-pills',
		'onTabClick': function(tab, navigation, index) {
			return false;
		},
		'onNext': function(tab, navigation, index) {


			var $total = navigation.find('li').length;
			var $current = index+1;
			

			if($current == $total){
				fillTable();
			}
			// If it's the last tab then hide the last button and show the finish instead
			if($current >= $total) {
				$('#rootwizard').find('.pager .next').hide();
				$('#rootwizard').find('.pager .finish').show();
				//$('#rootwizard').find('.pager .finish').removeClass('disabled');
			} else {
				$('#rootwizard').find('.pager .next').show();
				$('#rootwizard').find('.pager .finish').hide();
			}

			/*var $valid = $("#pentahoForm").valid();
			if(!$valid) {
				$validator.focusInvalid();
				return false;
			}*/
		},
		'onPrevious': function(tab, navigation, index) {
			var $total = navigation.find('li').length;
			var $current = index+1;

			if($current < $total) {
				$('#rootwizard').find('.pager .next').show();
				$('#rootwizard').find('.pager .finish').hide();
				//$('#rootwizard').find('.pager .finish').addClass('disabled');
			}
		}
	});	

	$('#rootwizard .finish').click(function() {
		//alert('Finished!, Starting over!');
		//$('#rootwizard').find("a[href*='tab1']").trigger('click');
		return true;
	});
});	


function fillTable(){
	$('#tableServiceName').html($('#serviceName').val());
	$('#tablePort').html($('#consolePort').val());
	$('#tableAdmin-username').html($('#adminUsername').val());
	$('#tableDbselected').html($('#dbSelected').val());
	$('#tablePortdb').html($('#portDB').val());
	$('#tableFlavor').html($('#flavor').val());
	$('#tableVolume').html($('#volume').val());
	$('#tableBackup').html($('#backup').val());
}

/*
$.validator.addMethod(
        "regex",
        function(value, element, regexp) {
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        },
        "regex error."
);*/