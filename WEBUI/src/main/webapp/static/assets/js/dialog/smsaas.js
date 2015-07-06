$(document).ready(function() {

	$('#dailyNot').change(function() {
		var isChecked = $('#dailyNot').is(":checked");
		if(isChecked){
			$('#divDaily').removeClass('hide');
		} else{
			$('#divDaily').addClass('hide');
		}
		checkDailySms();
		checkDailyEmail();
		checkDailyThreshold();
	});

						
	$('#dailySms').change(function() {
		checkDailySms();
		checkDailyThreshold();
	}); 

	$("#numberDaily").focus(function() {
		$('#numberDaily').parent().removeClass("has-error");
	});


	$("#emailAddressDaily").focus(function() {
		$('#emailAddressDaily').parent().removeClass("has-error");
	});

	$("#smsThresholdDaily").focus(function() {
		$('#smsThresholdDaily').parent().removeClass("has-error");
	});

	
	
	
	$('#dailyEmail').change(function() {
		checkDailyEmail();
		checkDailyThreshold();
	}); 	
		

	/* Funzioni per notifiche  */
	
	$("#saveDailyNotification").click(function() {

		if(checkDailyField()){
			saveDailyNotification("#dailyNotificationsForm", "#saveDailyNotification");
		}
		else {
			console.log("error");
		}
	});

});

function checkDailyField(){
	var ok = true;
	var isSmsChecked = $('#dailySms').is(":checked");
	if(isSmsChecked){
		var isnum = /^\d+$/.test($('#numberDaily').val());
		if(!isnum){ 
			$('#numberDaily').parent().addClass("has-error");
			ok = false; 
		}else {
			$('#numberDaily').parent().removeClass("has-error");
		}
		var threshold = parseInt($('#smsThresholdDaily').val());
		if(threshold < 1 || isNaN(threshold)){
			$('#smsThresholdDaily').parent().addClass("has-error");
			ok = false;
		}else{
			$('#smsThresholdDaily').parent().removeClass("has-error");
		}
		
	}

	if($('#dailyEmail').is(":checked")){
		
		if(!validateEmail($('#emailAddressDaily').val())){
			$('#emailAddressDaily').parent().addClass("has-error");
			ok = false;
		} else{
			$('#emailAddressDaily').parent().removeClass("has-error");
		}
		var threshold = parseInt($('#smsThresholdDaily').val());
		if(threshold < 1 || isNaN(threshold)){
			$('#smsThresholdDaily').parent().addClass("has-error");
			ok = false;
		}else{
			$('#smsThresholdDaily').parent().removeClass("has-error");
		}
	}

	return ok;

}

function validateEmail(email){
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function saveDailyNotification(formReference, button){
	var $form = $(formReference);
	$.ajax({
		url : "/async/paas/smsaas/daily",
		data : $form.serialize(),
		type : 'POST',
		statusCode : {
			404 : function() {
				console.log("404");
				window.location.href = path + '404';
			},
			500 : function() {
				console.log("500");
				window.location.href = path + '500';
			}
		},
		beforeSend : function (){
			$(button).attr("disabled", true);

		},
		success : function(data) {
			fillNotifications(data);
			//abilito botton
			$('#textNotifyDaily').text("");
			$('#textNotifyDaily').text("Notifiche salvate!");
			$('#notifySaved').removeClass("invisible");
			setTimeout( "jQuery('#notifySaved').addClass('invisible');",1000 );
			$(button).attr("disabled", false);
		},
		error : function(data){
			console.log("AJAX ERROR: " + status);
			$('#textNotifyDaily').text("");
			$('#textNotifyDaily').text("Errore, riprova!");
			$('#notifySaved').removeClass("invisible");
			setTimeout( "jQuery('#notifySaved').addClass('invisible');",1000 );
			$(button).attr("disabled", false);
		}
	});
}

function fillNotifications(data){
	console.log(data);
	if(data.isDaily == true){
		$('#dailyNot').prop('checked', true);

	} else {
		$('#dailyNot').prop('checked', false);
		$('#divDaily').addClass('hide');
	}
	
	if(data.isDailySms == true){
		$('#dailySms').prop('checked', true);
	} else {
		$('#dailySms').prop('checked', false);
	}
	
	if(data.isDailyEmail == true){
		$('#dailyEmail').prop('checked', true);
	} else {
		$('#dailyEmail').prop('checked', false);
	}

	$('#numberDaily').val(data.dailySms);
	$('#emailAddressDaily').val(data.dailyEmail);
	$('#smsThresholdDaily').val(data.thresholdDay);
}

function checkDailyThreshold(){

	var isCheckedEmail = $('#dailyEmail').is(":checked");
	var isCheckedSms = $('#dailySms').is(":checked");
	if(isCheckedEmail || isCheckedSms){
		$('#rowDailyThreshold').removeClass('hide');
	} else{
		$('#rowDailyThreshold').addClass('hide');
	}
}


function checkDailySms(){
	var isChecked = $('#dailySms').is(":checked");
	if(isChecked){
		$('#rowDailyNumber').removeClass('hide');
		$('#rowDailyNumber').val(1);
	} else{
		$('#rowDailyNumber').addClass('hide');
	}	
}

function checkDailyEmail(){
	var isChecked = $('#dailyEmail').is(":checked");
	if(isChecked){
		$('#rowDailyEmail').removeClass('hide');
		$('#rowDailyNumber').val(1);
	} else{
		$('#rowDailyEmail').addClass('hide');
	}
}


//Monthly notifications
$(document).ready(function() {
	
	$('#monthlyNot').change(function() {
		var isChecked = $('#monthlyNot').is(":checked");
		if(isChecked){
			$('#divMonthly').removeClass('hide');
		} else{
			$('#divMonthly').addClass('hide');
		}
		checkMonthlySms();
		checkMonthlyEmail();
		checkMonthlyThreshold();

	}); 
							
	$('#monthlySms').change(function() {
		checkMonthlySms();
		checkMonthlyThreshold();
	}); 

	$("#numberMonthly").focus(function() {
		$('#numberMonthly').parent().removeClass("has-error");
	});


	$("#emailAddressMonthly").focus(function() {
		$('#emailAddressMonthly').parent().removeClass("has-error");
	});

	$("#smsThresholdMonthly").focus(function() {
		$('#smsThresholdMonthly').parent().removeClass("has-error");
	});	
	
	$('#monthlyEmail').change(function() {
		checkMonthlyEmail();
		checkMonthlyThreshold();
	}); 		

	/* Funzioni per notifiche  */
	

	$("#saveMonthlyNotification").click(function() {
		if(checkMonthlyField()){
		saveMonthlyNotification("#monthlyNotificationsForm", "#saveMonthlyNotification");
		}
		else {
			console.log("error");
		}
	});

});

function checkMonthlyField(){
	var ok = true;
	var isSmsChecked = $('#monthlySms').is(":checked");
	if(isSmsChecked){
		var isnum = /^\d+$/.test($('#numberMonthly').val());
		if(!isnum){ 
			$('#numberMonthly').parent().addClass("has-error");
			ok = false; 
		}else {
			$('#numberMonthly').parent().removeClass("has-error");
		}
		var threshold = parseInt($('#smsThresholdMonthly').val());
		if(threshold < 1 || isNaN(threshold)){
			$('#smsThresholdMonthly').parent().addClass("has-error");
			ok = false;
		}else{
			$('#smsThresholdMonthly').parent().removeClass("has-error");
		}
		
	}

	if($('#monthlyEmail').is(":checked")){
		
		if(!validateEmail($('#emailAddressMonthly').val())){
			$('#emailAddressMonthly').parent().addClass("has-error");
			ok = false;
		} else{
			$('#emailAddressMonthly').parent().removeClass("has-error");
		}
		var threshold = parseInt($('#smsThresholdMonthly').val());
		if(threshold < 1 || isNaN(threshold)){
			$('#smsThresholdMonthly').parent().addClass("has-error");
			ok = false;
		}else{
			$('#smsThresholdMonthly').parent().removeClass("has-error");
		}
	}

	return ok;

}

function validateEmail(email){
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function saveMonthlyNotification(formReference, button){
	var $form = $(formReference);
	$.ajax({
		url : "/async/paas/smsaas/monthly",
		data : $form.serialize(),
		type : 'POST',
		statusCode : {
			404 : function() {
				console.log("404");
				window.location.href = path + '404';
			},
			500 : function() {
				console.log("500");
				window.location.href = path + '500';
			}
		},
		beforeSend : function (){
			$(button).attr("disabled", true);

		},
		success : function(data) {
			fillNotifications(data);
			//abilito botton
			$('#textNotifyMonthly').text("");
			$('#textNotifyMonthly').text("Notifiche salvate!");
			$('#notifyMonthlySaved').removeClass("invisible");
			setTimeout( "jQuery('#notifyMonthlySaved').addClass('invisible');",1000 );
			$(button).attr("disabled", false);
		},
		error : function(data){
			console.log("AJAX ERROR: " + status);
			$('#textNotifyMonthly').text("");
			$('#textNotifyMonthly').text("Errore, riprova!");
			$('#notifyMonthlySaved').removeClass("invisible");
			setTimeout( "jQuery('#notifyMonthlySaved').addClass('invisible');",1000 );
			$(button).attr("disabled", false);
		}
	});
}

function fillNotifications(data){
	console.log(data);
	if(data.isMonth == true){
		$('#monthlyNot').prop('checked', true);

	} else {
		$('#monthlyNot').prop('checked', false);
		$('#divMonthly').addClass('hide');
	}
	
	if(data.isMonthSms == true){
		$('#monthlySms').prop('checked', true);
	} else {
		$('#monthlySms').prop('checked', false);
	}
	
	if(data.isMonthEmail == true){
		$('#monthlyEmail').prop('checked', true);
	} else {
		$('#monthlyEmail').prop('checked', false);
	}

	$('#numberMonthly').val(data.monthSms);
	$('#emailAddressMonthly').val(data.monthEmail);
	$('#smsThresholdMonthly').val(data.thresholdMonth);
}

function checkMonthlyThreshold(){

	var isCheckedEmail = $('#monthlyEmail').is(":checked");
	var isCheckedSms = $('#monthlySms').is(":checked");
	if(isCheckedEmail || isCheckedSms){
		$('#rowMonthlyThreshold').removeClass('hide');
	} else{
		$('#rowMonthlyThreshold').addClass('hide');
	}
}

function checkMonthlySms(){
	var isChecked = $('#monthlySms').is(":checked");
	if(isChecked){
		$('#rowMonthlyNumber').removeClass('hide');
		$('#rowMonthlyNumber').val(1);
	} else{
		$('#rowMonthlyNumber').addClass('hide');
	}	
}

function checkMonthlyEmail(){
	var isChecked = $('#monthlyEmail').is(":checked");
	if(isChecked){
		$('#rowMonthlyEmail').removeClass('hide');
		$('#rowMonthlyNumber').val(1);
	} else{
		$('#rowMonthlyEmail').addClass('hide');
	}
}
//END monthly notification
		           
	           
	$(document).ready(function() {

		(function($) {

			$('#filter').keyup(function() {
				var rex = new RegExp($(this).val(), 'i');
				$('.searchable tr').hide();
				$('.searchable tr').filter(function() {
					console.log("here");
					return rex.test($(this).text());
				}).show();
			});

		}(jQuery));

	});
	

$(function() {
	
$("#buttonActiveSMS").click(function() {
		
		$.ajax({
			type : "POST",
			url : "/async/paas/smsaas/deploy",
			//data : "html",
			success : function(data) {
				if (data.status){
					// tutto ok
					if(data.success){
						$("#smsCreateSuccess").removeClass("hidden");
					} else {
						$("#smsCreateError").removeClass("hidden");
					}
					$("#smsCreateLoader").addClass("hidden");
					$("#okButton").removeClass("hidden");
				} else {
					$("#smsCreateError").removeClass("hidden");
					$("#smsCreateLoader").addClass("hidden");
					$("#okButton").removeClass("hidden");
				}
			},
			error : function(data) {
				window.location.href = "/500";
			}			
		});	
	});	
				
				
//				if(data == "Utente attivo"){
//					$("#smsCreateSuccess").removeClass("hidden");
//				} else{
//					$("#smsCreateError").removeClass("hidden");
//				}
//				$("#smsCreateLoader").addClass("hidden");
//				$("#okButton").removeClass("hidden");
//			},
//			error : function(data) {
//				$("#smsCreateLoader").addClass("hidden");
//				$("#smsServiceError").removeClass("hidden");
//				$("#okButton").addClass("hidden");
//				
//			}
//		});

	
	
	$("#okButton").click(function() {
		window.location.reload(true);
	});
	
	$("#regenerateToken").click(function() {

		$.ajax({
			type : "POST",
			url : "/async/paas/smsaas/regenerateToken",
			beforeSend : function(){
				$("#regenerateToken").addClass("disabled");
			},
			success : function(data) {
				if (data.status){
					// tutto ok
					if(data.success){
						//il risultato sta in data.result
						$("#token").text(data.result);
					} else {
					}
				} else {
					$("#token").text("Errore nel recuperare il token!");
				}
			},
			error : function(data) {
				window.location.href = "/500";
			},
			complete : function(){
				$("#regenerateToken").removeClass("disabled");
			}
		});
	});
	
	$("#okRefreshButton").click(function() {
		window.location.reload(true);
	});
	
	$("#okDisableButton").click(function() {
		$.ajax({
			type : "POST",
			url : "/async/paas/smsaas/disable",
			success : function(data) {
				if (data.status){
					// tutto ok
					if(data.success){
					window.location.reload(true);
					}
					else{
						$('#textModalDisable').text("Impossibile disabilitare l'utente, riprova in un secondo momento!!");
						$('#okDisableButton').addClass("hidden");
						$('#okRefreshButton').removeClass("hidden");
					}
				}
				else{
					$('#textModalDisable').text("Impossibile disabilitare l'utente, riprova in un secondo momento!!");
					$('#okDisableButton').addClass("hidden");
					$('#okRefreshButton').removeClass("hidden");
				}
			},
			error : function(data) {
				window.location.href = "/500";
			}
		});
	});

});