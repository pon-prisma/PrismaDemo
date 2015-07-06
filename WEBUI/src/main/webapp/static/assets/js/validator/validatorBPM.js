$(document)
		.ready(
				function() {

					var $validator = $("#BPMForm")
							.validate(
									{
										rules : {
											serviceName : {
												minlength : 4,
												maxlength : 16,
												regex : "^([A-Za-z])[A-Za-z0-9-._-]"
											},
											rootUsername : {
												minlength : 4,
												maxlength : 40,
												regex : "^([A-Za-z])[A-Za-z0-9-._-]"
											},
											rootPassword : {
												minlength : 8,
												maxlength : 40,
												regex : "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]))"
											},
											repeatRootPassword : {
												equalTo : "#rootPassword"
											},
											username : {
												minlength : 4,
												maxlength : 40,
												regex : "^([A-Za-z])[A-Za-z0-9-._-]"
											},
											usernamePassword : {
												minlength : 8,
												maxlength : 40,
												regex : "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]))"
											},
											repeatusernamePassword : {
												equalTo : "#usernamePassword"
											},

										},
										errorPlacement : function(error,
												element) {
										},
										highlight : function(element,
												errorClass, validClass) {
											$(element).parent().removeClass(
													'has-success').addClass(
													'has-error');
											$(
													'#' + $(element).attr('id')
															+ '-error')
													.removeClass('hidden');
										},
										unhighlight : function(element,
												errorClass, validClass) {
											$(element).parent().removeClass(
													'has-error').addClass(
													'has-success');
											$(
													'#' + $(element).attr('id')
															+ '-error')
													.addClass('hidden');
										}
									});

					$('#rootwizard').bootstrapWizard({
						'tabClass' : 'nav nav-pills',
						'onTabClick' : function(tab, navigation, index) {
							return false;
						},
						'onNext' : function(tab, navigation, index) {

							var $valid = $("#BPMForm").valid();
							if (!$valid) {
								$validator.focusInvalid();
								return false;
							}

							var $total = navigation.find('li').length;
							var $current = index + 1;

							if ($current == $total) {
								fillTable();
							}
							// If it's the last tab then hide the last button
							// and show the finish instead
							if ($current >= $total) {
								$('#rootwizard').find('.pager .next').hide();
								$('#rootwizard').find('.pager .finish').show();
								// $('#rootwizard').find('.pager
								// .finish').removeClass('disabled');
							} else {
								$('#rootwizard').find('.pager .next').show();
								$('#rootwizard').find('.pager .finish').hide();
							}

						},
						'onPrevious' : function(tab, navigation, index) {
							var $total = navigation.find('li').length;
							var $current = index + 1;

							if ($current < $total) {
								$('#rootwizard').find('.pager .next').show();
								$('#rootwizard').find('.pager .finish').hide();
								// $('#rootwizard').find('.pager
								// .finish').addClass('disabled');
							}
						}
					});

					$('#rootwizard .finish').click(function() {
						// alert('Finished!, Starting over!');
						// $('#rootwizard').find("a[href*='tab1']").trigger('click');
						return true;
					});
				});

function fillTable() {
	$('#tableZoneSelect').html($('#zoneSelect option:selected').text());
	$('#tableProfile').html($('#qosSelect option:selected').text());
	$('#tableFlavor').html($('#flavorSelect option:selected').text());
	$('#tableDisk').html($('#volume').val());
	if ($('#encryptedDisk').is(":checked")) {
		$('#tableEncryptedDisk').html('Si');
	} else {
		$('#tableEncryptedDisk').html('No');
	}
	if ($('#autoscaling').is(":checked")) {
		$('#tableAutoscaling').html('Max Flavor: ' + $('#maxFlavor option:selected').text() + '<br>' + 'Dimensione massima disco: ' + $('#maxVolume').val());

	} else {
		$('#tableAutoscaling').html('No');
	}
	$('#tableUServiceName').html($('#serviceName').val());
	$('#tableDBMS').html($('#dbmsSelect option:selected').text());
	$('#tablePort').html($('#port').val());
	$('#tableBackup').html($('#backup option:selected').text());
	if ($('#publicip').is(":checked")) {
		$('#tablePublicIP').html('Si');
	} else {
		$('#tablePublicIP').html('No');
	}	
	$('#tableRoot').html($('#rootUsername').val());
	if ($('#otherUser').is(":checked")) {
		$('#tableUsername').html('Nome: ' + $('#username').val() + '<br>' + 'Diritti di accesso: ' + $('#permission option:selected').text());
	} else {
		$('#tableUsername').html('No');
	}
}

$.validator.addMethod("regex", function(value, element, regexp) {
	var re = new RegExp(regexp);
	return this.optional(element) || re.test(value);
}, "regex error.");