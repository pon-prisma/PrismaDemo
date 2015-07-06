$(document)
		.ready(
				function() {

					var $validator = $("#CMSForm")
							.validate(
									{
										rules : {
											serviceName : {
												minlength : 4,
												maxlength : 20,
												regex : "^([A-Za-z])[A-Za-z0-9-._-]"
											},
											dbName : {
												minlength : 4,
												maxlength : 20,
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
											}
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

							var $valid = $("#CMSForm").valid();
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
	$('#tableServiceName').html($('#serviceName').val());
	$('#tableBDName').html($('#dbName').val());
	$('#tableRoot').html($('#rootUsername').val());
}

$.validator.addMethod("regex", function(value, element, regexp) {
	var re = new RegExp(regexp);	
	return this.optional(element) || re.test(value);
}, "regex error.");