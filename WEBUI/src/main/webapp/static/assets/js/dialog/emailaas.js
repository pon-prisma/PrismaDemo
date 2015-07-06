/**
 * 
 */

//variabile globale, contiene l'email selezionata
var email = "";

$(function() {

//	$(window).on("load resize",function() {
//		winHeight = $(window).height();
//		$('#scroller').css({
//			'max-height' : (winHeight)-100 + "px",
//			'overflow-y' : "auto",
//			'overflow-x' : "hidden",
//		});
//	});

	$('#confirmNewPassword').focus(function() {
		$('#newPassword').parent().removeClass("has-error");
		$('#confirmNewPassword').parent().removeClass("has-error");
	});

	$('#password').focus(function() {
		$('#password').parent().removeClass("has-error");
	});

	$('#accountName').focus(function() {
		$('#accountName').parent().removeClass("has-error");
	});
	$('#emailPassword').focus(function() {
		$('#emailPassword').parent().removeClass("has-error");
		$('#emailConfirmPassword').parent().removeClass("has-error");
	});
	$('#emailConfirmPassword').focus(function() {
		$('#emailPassword').parent().removeClass("has-error");
		$('#emailConfirmPassword').parent().removeClass("has-error");
	});

	$('#actualPassword').focus(function() {
		$('#actualPassword').parent().removeClass("has-error");
	});

	$('#newPassword').focus(function() {
		$('#newPassword').parent().removeClass("has-error");
		$('#confirmNewPassword').parent().removeClass("has-error");
	});

	$("#buttonCreateAccount").click(function() {
		$('#loadingCreateIcon').addClass("hidden");
		$('#accountName').parent().removeClass("has-error");
		$('#emailPassword').parent().removeClass("has-error");
		$('#emailConfirmPassword').parent().removeClass("has-error");
		$('#accountName').val('');
		$('#emailPassword').val('');
		$('#emailConfirmPassword').val('');
		$('#createButton').removeClass("hidden");
		$('#createAccountInfo').text("");
		
	});

	$("#createButton").click(
			function() {
				$('#createButton').addClass("hidden");
				var form = $("#emailCreateForm");
				$('#loadingCreateIcon').removeClass("hidden");
				$('#accountName').parent().removeClass("has-error");
				$('#emailPassword').parent().removeClass("has-error");
				$('#emailConfirmPassword').parent().removeClass("has-error");			
				$.ajax({
					type : "POST",
					url : form.attr("action"),
					data : form.serialize(),
					success : function(data) {
						//nessun errore del server
						if (data.status){
							// tutto ok
							if(data.success){
								//il risultato sta in data.result
								//window.location.reload(true);
								$('#tableEmail').bootstrapTable('refresh');
								$('#modalCreateAccount').modal("hide");

							} else {
							//	$('#loadingCreateIcon').addClass("hidden");
								$('#createAccountInfo').text("");
								for (i=0;i<data.result.length;i++){
									$('#'+data.result[i].field).parent()
									.addClass("has-error");
									if (data.result[i].field=="emailPassword")
										$('#emailConfirmPassword').parent()
										.addClass("has-error");
									$('#createAccountInfo').append(data.result[i].message+"<br/>");
								}
								$('#createButton').removeClass("hidden");	
								$('#loadingCreateIcon').addClass("hidden");
							}

						} else {
							window.location.href = "/prisma/error?code="+data.error.errorCode+"&message="+data.error.message;
						}
					},
					error : function(data) {
						window.location.href = "/500";
					}
				});
			});

	$("#okDeleteButton").click(function() {
		$('#okDeleteButton').addClass("hidden");
		$('#loadingDeleteIcon').removeClass("hidden");
		$.ajax({
			type : "DELETE",
			url : "/async/paas/emailaas",
			data : JSON.stringify({
				email : email,
				password : $('#password').val()
			}),
			contentType : 'application/json',
			success : function(data) {
				if (data.status){
					// tutto ok
					if(data.success){
						//il risultato sta in data.result
						$('#tableEmail').bootstrapTable('refresh');
						$('#modalDeleteAccount').modal("hide");
					} else {
						$('#loadingDeleteIcon').addClass("hidden");
						$('#'+data.result.field).parent()
						.addClass("has-error");
						$('#deleteAccountInfo').text(data.result.message);
						$('#okDeleteButton').removeClass("hidden");		
					}

				} else if (data.error!=null) {
					window.location.href = "/prisma/error?code="+data.error.errorCode+"&message="+data.error.message;
				}else 
					window.location.href = "/500";
			},
			error : function(data) {
				window.location.href = "/500";
			}
		});
	});

	$("#saveChangeButton")
	.click(
			function() {
				$(this).addClass("hidden");
				$('#loadingSaveIcon').removeClass("hidden");
				$('#accountName').parent().removeClass("has-error");
				$('#emailPassword').parent().removeClass("has-error");
				$('#emailConfirmPassword').parent().removeClass("has-error");
				$.ajax({
					type : "PUT",
					url : "/async/paas/emailaas",
					data : JSON.stringify({
						email : email,
						password : $('#actualPassword')
						.val(),
						newPassword : $('#newPassword')
						.val(),
						confirmNewPassword : $('#confirmNewPassword')
						.val()				
					}),
					contentType : 'application/json',
					success : function(data) {
						//nessun errore del server
						if (data.status){
							// tutto ok
							if(data.success){
								$('#loadingSaveIcon').addClass("hidden");
								$('#changePasswordInfo').text("Password cambiata con successo!");

								$('#actualPassword').prop("disabled", true);
								$('#confirmNewPassword').prop("disabled", true);
								$('#newPassword').prop("disabled", true);
								// hide save button
								$('#saveChangeButton').addClass("hidden");
								// show ok button
								$('#okChangeButton').removeClass("hidden");

							} else {
								$('#loadingSaveIcon').addClass("hidden");
								$('#changePasswordInfo').text("");
								for (i=0;i<data.result.length;i++){
									$('#'+data.result[i].field).parent()
									.addClass("has-error");
									if (data.result[i].field=="newPassword")
										$('#confirmNewPassword').parent()
										.addClass("has-error");
									$('#changePasswordInfo').append(data.result[i].message+"<br/>");
								}
								$('#saveChangeButton').removeClass("hidden");		
							}
						} else {
							window.location.href = "/prisma/error?code="+data.error.errorCode+"&message="+data.error.message;
						}
					},
					error : function(data) {
						window.location.href = "/500";
					}
				});
			});


	$("#okChangeButton").click(function() {
		$('#modalChangePassword').modal('hide');
	});

});

function openDeleteDialog(data) {
	email = data;
	$('#loadingDeleteIcon').addClass("hidden");
	$('#okDeleteButton').removeClass("hidden");
	$('#password').parent().removeClass("has-error");
	$('#password').val("");
	$('#deleteAccountInfo').html(
			"Sei sicuro di voler eliminare l'account email <b>" + email
			+ "</b>?");
}

function openChangeDialog(data) {
	email = data;
	$('#actualPassword').val("");
	$('#newPassword').val("");
	$('#confirmNewPassword').val("");
	$('#saveChangeButton').prop("disabled", false);
	$('#confirmNewPassword').parent().removeClass("has-error");
	$('#actualPassword').parent().removeClass("has-error");
	$('#newPassword').parent().removeClass("has-error");
	$('#changePasswordInfo').text(
	"Inserisci la password attuale e quella desiderata");
	$('#okChangeButton').addClass("hidden");
	$('#saveChangeButton').removeClass("hidden");
	$('#actualPassword').prop("disabled", false);
	$('#confirmNewPassword').prop("disabled", false);
	$('#newPassword').prop("disabled", false);

}
