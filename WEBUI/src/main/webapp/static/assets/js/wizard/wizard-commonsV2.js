function deploy(serviceName) {
	var $form = $("#DeployForm");
	$.ajax({
		url : $form.attr("action"),
		data : $form.serialize(),
		type : "POST",
		statusCode : {
			404 : function() {
				console.log("404");
				window.location.href = '/404';
			},
			500 : function() {
				console.log("500");
				window.location.href = '/500';
			}
		},
		beforeSend : function() {
			$.blockUI({
				css : {
					border : 'none',
					padding : '15px',
					backgroundColor : '#000',
					'-webkit-border-radius' : '10px',
					'-moz-border-radius' : '10px',
					opacity : .5,
					color : '#fff'
				}
			});
		},
		success : function(data) {
			response = data;
			if (data.status) {
				if (data.success) {
					var loc;
					switch (serviceName) {
					case 'app':
						loc = '/paas/appaas/' + data.result.id;
						break;
					case 'env':
						loc = '/paas/appaas/' + data.result.appId + '/environments/' + data.result.envId;
						break;
					case 'biaas':
						loc ='/paas/' + serviceName  + '/' + data.result.id;
						break;
					case 'dbaas':
						loc ='/paas/' + serviceName  + '/' + data.result.id;
						break;
					case 'vmaas':
						loc = '/iaas/' + serviceName  + '/' + data.result.id;
						break;
					case 'mqaas':
						loc = '/paas/' + serviceName  + '/' + data.result.id;
						break;
					default:
						loc = '/prisma/error?description=Servizio non riconosciuto';					
					}
					//setTimeout(function(loc) { window.location.href=loc; }, 2000);
					window.location.href=loc;
					
				} else {
					console.log(data.error.errorCode);
					console.log(data.error.message);
					window.location.href = "/prisma/error?code=" + data.error.errorCode + "&message=" + data.error.message;
				}
			} else {
				window.location.href = "/500";
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log("Error during deploy request: " + textStatus);
			window.location.href = '/prisma/error?description=La creazione del servizio ha fallito.';
		},
		timeout: 60000
	});
	return false;
}

$(function() {

	$('#rootwizard').bootstrapWizard({
		'tabClass' : 'nav nav-pills',
		'onTabClick' : function(tab, navigation, index) {
			return false;
		},
		'onNext' : function(tab, navigation, index) {

			var $valid = $("#DeployForm").valid();
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
			}
		}
	});

	$('#rootwizard .finish').click(function() {
		return true;
	});

	$.validator.addMethod("regex", function(value, element, regexp) {
		var re = new RegExp(regexp);
		return this.optional(element) || re.test(value);
	}, "regex error.");

	$.validator.addMethod("unique", function(value, element, type) {
		if (value.length > 3) {
			switch (type) {
			case 'name':
				return checkUniqueName(value);

			case 'dns':
				return checkUniqueDns(value);
			default:
				return false;
			}
		} else {
			return true;
		}
	}, "unique error.");
	
	$.validator.addMethod("appSource", function(value, element, type) {	
		result = false;
		switch (value) {
			case 'localApp':
				console.log("localApp");
				if($('#fileuploadId').val() != "" && $('#fileuploadId').val() != -1){
					result = true;
				}
				break;
			case 'prismaApp':
				if(!urlPattern.test($('#urlStorage').val())){
					$('#urlStorage-error').removeClass("hidden");
				}
				if($('#sourceLabelPRISMA').val().length <= 3){
					$('#sourceLabelPRISMA-error').removeClass("hidden");
				}
				if(urlPattern.test($('#urlStorage').val()) && $('#sourceLabelPRISMA').val().length > 3 && $('#fileURLId').val() > 0){
					result = true;
					$('#urlStorage-error').addClass("hidden");
					$('#sourceLabelPRISMA-error').addClass("hidden");

				}
				break;
			case 'privateApp':
				if($('#inputPrivateIdSpan').text() != "Nessuna" && $('#inputPrivateId').val() != ""){
					result = true;
				}			
				break;

			case 'publicApp':
				console.log("publicApp");
				if($('#inputPublicIdSpan').text() != "Nessuna" && $('#inputPublicId').val() != ""){
					result = true;
				}		
				break;
		}
		return result;
	}, "appSource error.");


});

var nameRequest = null;
function checkUniqueName(value) {

	if (nameRequest != null) {
		nameRequest.abort();
	}

	var result = false;
	nameRequest = $.ajax({
		type : 'GET',
		dataType : 'json',
		url : "/async/name/available?name=" + value,
		success : function(data) {
			if (data == true)
				result = true;
		},
		error : function() {
			console.log("check name service not working");
		},
		async : false
	});
	return result;
}

var dnsRequest = null;
function checkUniqueDns(value) {

	if (dnsRequest != null) {
		dnsRequest.abort();
	}

	var result = false;
	dnsRequest = $.ajax({
		type : 'GET',
		dataType : 'json',
		url : "/async/dns/available?dn=" + value + $("#urlName").html(),
		success : function(data) {
			if (data == true)
				result = true;
		},
		error : function() {
			console.log("check dns service not working");
		},
		async : false
	});
	return result;
}
