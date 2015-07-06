//variabile globale, contiene l'email selezionata
var serialNumberDelete = "";

$(function() {

	$('#commonName').focus(function() {
		$('#commonName').parent().removeClass("has-error");
	});


	$("#OkErrorDownloadButton").click(function() {
		$('#modalDownloadFailedCertificate').modal('hide');
	});


	$("#createButton").click(function() {
		if ($('#commonName').val().length > 0){
			$("#createButton").addClass("hidden");
			$('#loadingIcon').removeClass("hidden");
			$('#commonName').parent()
			.removeClass("has-error");
			$.ajax({
				type : "POST",
				url : "/async/paas/caaas/" + $('#commonName').val(),
				success : function(data) {
					if (data.status){
						// tutto ok
						if(data.success){
							$('#loadingIcon').addClass("hidden");
							$('#createCertificateInfo').text("Certificato creato correttamente, clicca su ok per scaricare la chiave privata. Conservala con cura perchè non sarà più recuperabile");
							$('#commonNameField').addClass("hidden");
							$("#okButton").removeClass("hidden");
							//il risultato sta in data.result
						} else {
							$('#loadingIcon').addClass("hidden");
							$('#createCertificateInfo').text("Errore nella creazione del certificato");
							$('#commonNameField').addClass("hidden");
							$('#okErrorButton').removeClass("hidden");
						}
					} else {
						$('#loadingIcon').addClass("hidden");
						$("#okErrorButton").removeClass("hidden");
						$('#commonNameField').addClass("hidden");
						$('#createCertificateInfo').text(data.error.message);
						//window.location.href = "/prisma/error?code="+data.error.errorCode+"&message="+data.error.message;
					}
				},
				error : function(data) {
					window.location.href = "/500";
				}
			});
		}else {
			$('#createCertificateInfo').text("Inserire un Common Name valido");
			$('#commonName').parent()
			.addClass("has-error");
		}
	});


	$("#okButton").click(function() {
		$.fileDownload("/async/paas/caaas/download", {
			successCallback: function (url) {
				$('#tableCa').bootstrapTable('refresh');				
				$("#modalCreateCertificate").modal("hide");
				$('#okButton').addClass("hidden");
			},
			failCallback: function (HttpServletResponse, url) {
				$("#createCertificateInfo").text("Errore nel download della chiave privata, contatta il supporto per annullare l'acquisto (fornisci la data e l'ora di creazione)");
				$('#okButton').addClass("hidden");
				$('#okErrorButton').removeClass("hidden");
			}
		});
		return false; //this is critical to stop the click event which will trigger a normal file download!
	});


	$("#buttonCreateAccount").click(function() {
		$('#commonNameField').removeClass("hidden");
		$("#createButton").removeClass("hidden");
		$('#createCertificateInfo').text("Inserisci il COMMON NAME del certificato");
		$('#commonName').parent().removeClass("has-error");
		$('#okErrorButton').addClass("hidden");
		$('#commonName').val("");
	});
	
	$("#okErrorButton").click(function() {
		$("#modalCreateCertificate").modal("hide");
		$('#okErrorButton').addClass("hidden");
	});



	$("#backRevokeButton").click(function() {
		$('#modalRevokeCertificate').modal('hide');

	});
	
	
	$("#confirmRevokeButton").click(function() {
		var reason =$( "#reasonSelect" ).val();
		$('#loadingRevokeIcon').removeClass("hidden");
		$('#confirmRevokeButton').addClass("hidden");
		$('#backRevokeButton').addClass("hidden");
		$.ajax({
			type : "PUT",
			url : "/async/paas/caaas/"+ serialNumberDelete + "/" + reason,
			success : function(data) {
				if (data.status){
					// tutto ok
					if(data.success){
						$('#loadingRevokeIcon').addClass("hidden");
						$("#reasonDiv").addClass("hidden");
						$('#revokeCertificateInfo').text("Certificato correttamente revocato");
						$('#tableCa').bootstrapTable('refresh');	
						setTimeout( "jQuery('#modalRevokeCertificate').modal('hide');",1000 );
						//$("#modalRevokeCertificate").modal("hide");
						//il risultato sta in data.result
					} else {
						$('#loadingRevokeIcon').addClass("hidden");
						$("#reasonDiv").addClass("hidden");
						$('#revokeCertificateInfo').text("Impossibile revocare il certificato");
						$('#backRevokeButton').removeClass("hidden");
					}
				} else {
					$('#loadingRevokeIcon').addClass("hidden");
					$("#reasonDiv").addClass("hidden");
					$('#revokeCertificateInfo').text(data.error.message);
					$('#backRevokeButton').removeClass("hidden");
					//window.location.href = "/prisma/error?code="+data.error.errorCode+"&message="+data.error.message;
				}
			},
			error : function(data) {
				window.location.href = "/500";
			}
		});


	});

});




function openViewDialog(serialNumber) {
	$('#viewCertificateInfo').html("Caricamento in corso...");
	$.ajax({
		type : "GET",
		url : "/async/paas/caaas/"+ serialNumber,
		success : function(data) {
			if (data.status){
				// tutto ok
				if(data.success){
					$('#viewCertificateInfo').html(data.result);
					//il risultato sta in data.result
				} else {

				}
			} else {
				$('#viewCertificateInfo').html(data.error.message);	
				//window.location.href = "/prisma/error?code="+data.error.errorCode+"&message="+data.error.message;
			}
		},
		error : function(data) {
			window.location.href = "/500";
		}
	});
}

function openDownloadDialog(serialNumber) {
	$("#"+ serialNumber).attr("disabled", true);
//	document.getElementById("buttonDownloadCertificate").innerHTML= "Downloading...";
	$.fileDownload("/async/paas/caaas/downloadCertificate/"+serialNumber, {
		successCallback: function (url) {
			$("#"+ serialNumber).attr("disabled", false);
		},
		failCallback: function (HttpServletResponse, url) {
			$("#"+ serialNumber).attr("disabled", false);
			$("#modalDownloadFailedCertificate").modal("show");
		}
	});
	return false; //this is critical to stop the click event which will trigger a normal file download!
}



function openRevokeDialog(data,name) {
	serialNumberDelete=data;
	$("#reasonDiv").removeClass("hidden");
	$('#revokeCertificateInfo').html("Sei sicuro di voler revocare il certificato con SN: <b>" + serialNumberDelete
			+ "</b>?");
	$('#confirmRevokeButton').removeClass("hidden");
	$('#backRevokeButton').removeClass("hidden");
}

