var token="";

$(function() {

	$("#dismissButton").click(function() {
		$('#modalDeleteToken').modal('hide');
	});

	$("#createdButton").click(function(){
		$('#modalCreateToken').modal('hide');
	});
	
	
	$("#buttonCreateToken").click(function() {
		$('#loadingCreateIcon').removeClass("hidden");
		$('#createTokenInfo').html("Creazione token in corso...");
		$.ajax({
			type : "PUT",
			url : "/async/token/",
			success : function(data) {
				//nessun errore del server
				if (data.status){
					// tutto ok
					$('#loadingCreateIcon').addClass("hidden");
					$('#createdButton').removeClass("hidden");
					if(data.success){
						$('#createTokenInfo').html("Token <b>" + data.result.tokenId + "</b> creato con successo!");	
						$('#tableToken').bootstrapTable('refresh');
					} else {
						$('#createTokenInfo').html("Impossibile creare un token!");
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
	
	
	$("#okRevokeButton").click(function() {
		$('#loadingDeleteIcon').removeClass("hidden");
		$('#okRevokeButton').addClass("hidden");
		$.ajax({
			type : "DELETE",
			url : "/async/token/" + token,
			success : function(data) {
				//nessun errore del server
				if (data.status){
					// tutto ok
					if(data.success){
						$('#loadingDeleteIcon').addClass("hidden");
						$('#deleteTokenInfo').text("Token revocato con successo!");					
						$('#dismissButton').removeClass("hidden");
						$('#tableToken').bootstrapTable('refresh');
					} else {
						$('#deleteTokenInfo').text("Impossibile revocare il token!");
						$('#loadingDeleteIcon').addClass("hidden");
						$('#dismissButton').removeClass("hidden");
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
});


function openRevokeDialog(data) {
	token = data;
	$('#loadingDeleteIcon').addClass("hidden");
	$('#okRevokeButton').removeClass("hidden");
	$('#dismissButton').addClass("hidden");
	$('#deleteTokenInfo').html(
			"Sei sicuro di voler revocare il token <b>" + token
			+ "</b>?");
} 



function dateFormatter(value,row){
	var date = new Date(value);
	return date.toLocaleString();
} 