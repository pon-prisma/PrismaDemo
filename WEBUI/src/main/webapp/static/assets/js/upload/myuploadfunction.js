$(function() {

	$('#fileupload').fileupload({
		// add: function(e, data){
		// data.formData = $("#label").val();
		// },
		dataType : 'json',
		url : '/async/apprepo/upload',
		done : function(e, data) {
			$.each(data.result.files, function(index, file) {
				$("#localFile").html(file.name);
			});
		},

		progressall : function(e, data) {
			var progress = parseInt(data.loaded / data.total * 100, 10);
			console.log(progress);
			$("#localFile").html("Caricamento in corso: " + progress + "%");
			if (progress == 100) {
				$("#localFile").html("Salvataggio in corso...");
			}
		},
		fail : function(e, data) {
			$("#localFile").html("Errore durante l'upload");
		}
	});

	$('#fileupload').bind('fileuploadsubmit', function(e, data) {
		data.formData = {
			label : $('#sourceLabel').val(),
			visibility : $('input[name=appVisibility]:checked').val()
		};
	});
});