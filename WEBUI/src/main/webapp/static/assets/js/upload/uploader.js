$(function() {
	'use strict';
	// Change this to the location of your server-side upload handler:
	var url = window.location.hostname === 'http://localhost:8080/prisma-webui/paas/appaas/upload/file';
	$('#file').fileupload({

		add : function(e, data) {
			var uploadErrors = [];
			// var acceptFileTypes = /^image\/(gif|jpe?g|png)$/i;
			// if(data.originalFiles[0]['type'].length &&
			// !acceptFileTypes.test(data.originalFiles[0]['type'])) {
			// uploadErrors.push('Not an accepted file type');
			// }
			// if(data.originalFiles[0]['size'].length &&
			// data.originalFiles[0]['size'] > 50) {
			if (data.originalFiles[0]['size'] > 10000000) {
				uploadErrors.push('Filesize is too big');
			}
			if (uploadErrors.length > 0) {
				alert(uploadErrors.join("\n"));
			} else {
				data.submit();
			}
		},

		url : url,
		dataType : 'json',
		done : function(e, data) {
			$.each(data.result.files, function(index, file) {
				// $('<p/>').text(file.name).appendTo('#files');
				$('#files').html(file.name);
			});
			$('#deleteCertificato').removeClass("hide");
			$('#addCertificato').addClass("hide");
		},
		progressall : function(e, data) {
			var progress = parseInt(data.loaded / data.total * 100, 10);
			$('#files').html(progress + '%');
		}
	}).prop('disabled', !$.support.fileInput).parent().addClass(
			$.support.fileInput ? undefined : 'disabled');
	
	
	
	
	
	$('#secureConnection').change(function() {
		var isChecked = $('#secureConnection').is(":checked");
		//alert(isChecked);
		if(isChecked){
			$('#secureDiv').removeClass('hide');
		} else{
			$('#secureDiv').addClass('hide');
		}
	});

	
	$("#serverType").change(function() {
        var val = $(this).val();
        if (val == "web") {
            $("#serverName").html("<option value='apache'>Apache HTTP Server</option><option value='ibm'>IBM HTTP Server</option>");
        } else if (val == "as") {
            $("#serverName").html("<option value='iis'>IIS</option><option value='tomcat'>Tomcat</option>><option value='tomcat'>JBoss</option>");
        }
    });
});

function deleteCert() {
	$.ajax({
		type : "POST",
		url : "/prisma-webui/paas/appaas/delete/file",
		data : "filename=" + $("#files").text(),
		dataType : "json",
		success : function(data) {
			alert(data.success);
			if (data.success == true) {
				$("#files").text("Nessun certificato caricato");

			} else {
				$("#files").text("Errore. Ricarica il certificato");
			}
			$('#addCertificato').removeClass("hide");
			$('#deleteCertificato').addClass("hide");
		},
		error : function() {
			alert("Chiamata fallita, si prega di riprovare...");
		}
	});
	$('#addCertificato').removeClass("hide");
	$('#deleteCertificato').addClass("hide");
}
