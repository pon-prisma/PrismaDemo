/* *****************************************************************
	 ______  ______   __   ______   __    __   ______    
	/\  == \/\  == \ /\ \ /\  ___\ /\ "-./  \ /\  __ \   
	\ \  _-/\ \  __< \ \ \\ \___  \\ \ \-./\ \\ \  __ \  
 	 \ \_\   \ \_\ \_\\ \_\\/\_____\\ \_\ \ \_\\ \_\ \_\ 
  	  \/_/    \/_/ /_/ \/_/ \/_____/ \/_/  \/_/ \/_/\/_/  [2014]
                                                     
	PRISMA - PiattafoRme cloud Interoperabili per SMArt-Government 
	Custom JavaScripts                                           
	Version: 0.1.0                                                 
	      
 ***************************************************************** */

// Global pattern variable
//var urlPattern = new RegExp("(http|ftp|https)://[\w-]+(\.[\w-]+)+([\w.,@?^=%&amp;:/~+#-]*[\w@?^=%&amp;/~+#-])?");
var urlPattern = new RegExp("^(http[s]?:\\/\\/(www\\.)?|ftp:\\/\\/(www\\.)?|www\\.){1}([0-9A-Za-z-\\.@:%_\+~#=]+)+((\\.[a-zA-Z]{2,3})+)(/(.)*)?(\\?(.)*)?");


$(document).ready(
	function() {
		$("div.bhoechie-tab-menu>div.list-group>a").click(function(e) {
			e.preventDefault();
			$(this).siblings('a.active').removeClass("active");
			$(this).addClass("active");
			var index = $(this).index();
			$("div.bhoechie-tab>div.bhoechie-tab-content").removeClass("active");
			$("div.bhoechie-tab>div.bhoechie-tab-content").eq(index).addClass("active");
		});
	
	function checkUniqueName(value) {
		$.ajax({
			type : 'GET',
			dataType : 'json',
			url : "/async/" + value + "/checknameavailability",
			success : function(data) {
				if (data == true)
					return true;
				else
					return false;
			},
			error : function() {
				console.log("checkname service not working");
				return false;
			},
			async : false
		});
	}
});



/**
 * 
 * Funzione utilizzata per fare il rendering del livello di log nelle tabelle di
 * eventi
 * 
 * @param value
 * @param row
 * @returns {String}
 */
function levelFormatter(value, row) {
	var icon = "label label-primary";
	switch (value) {
	case 'INFO':
		icon = 'label label-primary';
		break;
	case 'WARNING':
		icon = 'label label-warning';
		break;
	case 'ERROR':
		icon = 'label label-danger';
		break;
	default:
		break;
	}
	return '<span style="display:block; width:70px; padding-bottom:5px; padding-top:5px;" class="'
			+ icon + '">' + value + '</span>';
}

function dateFormatter(value, row) {
	var m = moment.unix(value/1000);
	var date = m.format("YYYY-MM-DD HH:mm:ss");
	if(m.utcOffset() > 0)
		date = date + " (GMT +" + m.utcOffset()/60 + ")";
	else 
		date = date + " (GMT " + m.utcOffset()/60 + ")";
	
    return '<span>' + date + '</span>';
}

function appaasNameFormatter(value, row) {
	return '<a href="/paas/appaas/' + row["id"] + '">' + value + '</<a>';
}

function appNameFormatter(value, row) {
	return '<a href="/paas/appaas/' + row["appaasId"] + '/environments/'
			+ row["id"] + '">' + value + '</<a>';
}

function tokenRevokeButtonFormatter(value, row) {	
	return '<button type="button" data-toggle="modal" data-target="#modalDeleteToken" class="btn btn-sm btn-danger" '+ 
	'id="buttonDeleteAccount" onclick="javascript:openRevokeDialog(\'' + value + '\');">Revoca</button>';
}

function emailModifyButtonFormatter(value, row) {
	return '<button type="button" data-toggle="modal" data-target="#modalChangePassword" class="btn btn-sm btn-primary" '+ 
	'id="buttonChangePassword" onclick="javascript:openChangeDialog(\'' + value + '\');">Modifica password</button>';
	//return '<a class="btn btn-primary" role="button" data-toggle="modal" id="buttonChangePassword" data-target="#modalChangePassword"' +
		// 'onclick="javascript:openChangeDialog(\"' + value + '");">Modifica password</a>';
}

function emailDeleteButtonFormatter(value, row) {	
	return '<button type="button" data-toggle="modal" data-target="#modalDeleteAccount" class="btn btn-sm btn-danger" '+ 
	'id="buttonDeleteAccount" onclick="javascript:openDeleteDialog(\'' + value + '\');">Elimina</button>';
	//return '<a class="btn btn-danger" role="button" data-toggle="modal" id="buttonDeleteAccount" data-target="#modalDeleteAccount"' +
		// 'onclick="javascript:openDeleteDialog(\"' + value + '");">Elimina</a>';
}


function deleteKeyFormatter(value, row) {
	return '<button type="button" data-toggle="modal" data-target="#modalDeleteKeypair" class="btn btn-sm btn-danger" '+ 
		'id="button'+value+'" onclick="javascript:selectKeypair(\'' + value + '\');">Elimina</button>';
}



function workgroupUserActionsButtonFormatter(value, row) {
	
	return '<div class="dropdown">' +
		'<button class="btn btn-danger btn-xs dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">' +
			'Gestisci utente<span class="caret"></span>' +
		'</button>' +
		'<ul class="dropdown-menu pull-right" role="menu" aria-labelledby="dropdownMenu1">' +
			'<li role="presentation"><a role="menuitem" tabindex="-1" href="#">Elimina dal workgroup</a></li>' +
			'<li role="presentation"><a role="menuitem" tabindex="-1" href="#">Sospendi dal workgroup</a></li>' +
			'<li role="presentation"><a role="menuitem" tabindex="-1" href="#">Nomina come utente ospite</a></li>' +
			'<li role="presentation"><a role="menuitem" tabindex="-1" href="#">Nomina come utente standard</a></li>' +
			'<li role="presentation"><a role="menuitem" tabindex="-1" href="#">Nomina come utente amministratore</a></li>' +
			'</ul> </div>';
}


function updateSuccess(data) {
	updateGUI(data['status'], '#statusIcon');
}

function getStatusData(status) {
	var returnValues = new Object();
	switch (status) {
	case STATUS_STARTING:
	case STATUS_START_IN_PROGRESS:
		returnValues.status={text:"Avvio in corso"};
		returnValues.dropdownMenu={
				tagClass:"btn btn-primary",
				disabled: true,
				text:"Avvio in corso"
					};
		returnValues.startService={hidden: true};
		returnValues.stopService={hidden: true};
		returnValues.restartService={hidden: true};
		returnValues.deleteService={hidden: true};
		break;

		
	case STATUS_RUNNING:
		returnValues.status={text:"Avviato"};
		returnValues.dropdownMenu={
				tagClass:"btn btn-success",
				disabled: false,
				text:"Avviato"
					};
		returnValues.startService={hidden: true};
		returnValues.stopService={hidden: false};
		returnValues.restartService={hidden: false};
		returnValues.deleteService={hidden: false};

		break;

		
	case STATUS_RESTARTING:
	case STATUS_RESTART_IN_PROGRESS:
		returnValues.status={text:"Riavvio in corso"};
		returnValues.dropdownMenu={
				tagClass:"btn btn-primary",
				disabled: true,
				text:"Riavvio in corso"
					};
		returnValues.startService={hidden: true};
		returnValues.stopService={hidden: true};
		returnValues.restartService={hidden: true};
		returnValues.deleteService={hidden: true};
		break;

	case STATUS_DEPLOY_PENDING:	
	case STATUS_DEPLOY_IN_PROGRESS:
		returnValues.status={text:"Creazione in corso"};
		returnValues.dropdownMenu={
				tagClass:"btn btn-default",
				disabled: true,
				text:"Creazione in corso"
					};
		returnValues.startService={hidden: true};
		returnValues.stopService={hidden: true};
		returnValues.restartService={hidden: true};
		returnValues.deleteService={hidden: true};
		break;
		
	case STATUS_UNDEPLOY_PENDING:	
	case STATUS_UNDEPLOY_IN_PROGRESS:
		returnValues.status={text:"Cancellazione in corso"};
		returnValues.dropdownMenu={
				tagClass:"btn btn-default",
				disabled: true,
				text:"Cancellazione in corso"
					};
		returnValues.startService={hidden: true};
		returnValues.stopService={hidden: true};
		returnValues.restartService={hidden: true};
		returnValues.deleteService={hidden: true};
		break;
		
	case STATUS_STOPPING:
	case STATUS_STOP_IN_PROGRESS:
		returnValues.status={text:"Arresto in corso"};
		returnValues.dropdownMenu={
				tagClass:"btn btn-primary",
				disabled: true,
				text:"Arresto in corso"
					};
		returnValues.startService={hidden: true};
		returnValues.stopService={hidden: true};
		returnValues.restartService={hidden: true};
		returnValues.deleteService={hidden: true};
		break;

		
	case STATUS_STOPPED:
		returnValues.status={text:"Arrestato"};
		returnValues.dropdownMenu={
				tagClass:"btn btn-warning",
				disabled: false,
				text:"Arrestato"
					};
		returnValues.startService={hidden: false};
		returnValues.stopService={hidden: true};
		returnValues.restartService={hidden: true};
		returnValues.deleteService={hidden: false};
		break;


	case STATUS_DELETED:
		returnValues.status={text:"Cancellato"};
		returnValues.dropdownMenu={
				tagClass:"btn btn-default",
				disabled: true,
				text:"Cancellato"
					};
		returnValues.startService={hidden: true};
		returnValues.stopService={hidden: true};
		returnValues.restartService={hidden: true};
		returnValues.deleteService={hidden: true};
		break;

	case STATUS_ERROR:
		returnValues.status={text:"Errore"};
		returnValues.dropdownMenu={
				tagClass:"btn btn-danger",
				disabled: false,
				text:"Errore"
					};
		returnValues.startService={hidden: false};
		returnValues.stopService={hidden: false};
		returnValues.restartService={hidden: false};
		returnValues.deleteService={hidden: false};
		break;

	case STATUS_OPERATION_ERROR:
		returnValues.status={text:"Impossibile eseguire l'operazione richiesta"};
		returnValues.dropdownMenu={
				tagClass:"btn btn-danger",
				disabled: true,
				text:"Errore"
					};
		returnValues.startService={hidden: true};
		returnValues.stopService={hidden: true};
		returnValues.restartService={hidden: true};
		returnValues.deleteService={hidden: true};
		break;
		
	default:
		break;
	}
	return returnValues;
}

function updateGUI(status, iconid){

	switch (status) {
	case STATUS_STARTING:
	case STATUS_START_IN_PROGRESS:
		$(iconid).removeClass();
		$(iconid).addClass('fa fa-refresh fa-spin');
		$(iconid).css('color', '#0C090A');
		break;
	case STATUS_RUNNING:
		$(iconid).removeClass();
		$(iconid).addClass('fa fa-check-circle');
		$(iconid).css('color', '#43ac6a');
		break;
	case STATUS_RESTARTING:
	case STATUS_RESTART_IN_PROGRESS:
		$(iconid).removeClass();
		$(iconid).addClass('fa fa-refresh fa-spin');
		$(iconid).css('color', '#0C090A');
		break;

	case STATUS_DEPLOY_PENDING:	
	case STATUS_DEPLOY_IN_PROGRESS:
		$(iconid).removeClass();
		$(iconid).addClass('fa fa-refresh fa-spin');
		$(iconid).css('color', '#0C090A');
		break;
		
	case STATUS_UNDEPLOY_PENDING:	
	case STATUS_UNDEPLOY_IN_PROGRESS:
		$(iconid).removeClass();
		$(iconid).addClass('fa fa-refresh fa-spin');
		$(iconid).css('color', '#0C090A');
		break;
		
	case STATUS_STOPPING:
	case STATUS_STOP_IN_PROGRESS:
		$(iconid).removeClass();
		$(iconid).addClass('fa fa-refresh fa-spin');
		$(iconid).css('color', '#0C090A');
		break;

		
	case STATUS_STOPPED:
		$(iconid).removeClass();
		$(iconid).addClass('fa fa-minus-circle');
		$(iconid).css('color', '#e99002');
		break;


	case STATUS_DELETED:
		$(iconid).removeClass();
		$(iconid).addClass('fa fa-trash');
		$(iconid).css('color', '#0C090A');
		clearTimeout(timeoutStatus);
		break;

	case STATUS_ERROR:
		$(iconid).removeClass();
		$(iconid).addClass('fa fa-times-circle');
		$(iconid).css('color', '#FF0000');
		break;

	case STATUS_OPERATION_ERROR:
		$(iconid).removeClass();
		$(iconid).addClass('fa fa-times-circle');
		$(iconid).css('color', '#FF0000');
		break;
		
	default:
		break;
	}
	var statusData = getStatusData(status);
	$('#status').text(statusData.status.text);
	updateStatusMenu(status);
}

function updateStatusMenu(status, id) {
	var statusData = getStatusData(status);
	var isListView;
	if (typeof(id)==='undefined') {
		isListView = false;
	} else {
		isListView = true;
	}
	if (isListView) {
		$('#status-'+id).html(statusData.dropdownMenu.text.toUpperCase());
		$('#status-'+id).removeClass();
		$('#status-'+id).addClass(statusData.dropdownMenu.tagClass + " btn-sm");
		$('#statusDropdown-'+id).removeClass();
		$('#statusDropdown-'+id).addClass(statusData.dropdownMenu.tagClass + " btn-sm dropdown-toggle");
		$('#statusDropdown-'+id).prop("disabled", statusData.dropdownMenu.disabled);
	} else {
		$('#dropdownMenuText').text(statusData.dropdownMenu.text);
		$('#dropdownMenu').removeClass();
		$('#dropdownMenu').prop("disabled", statusData.dropdownMenu.disabled);
		$('#dropdownMenu').addClass(statusData.dropdownMenu.tagClass + " dropdown-toggle btn-xs");
	}
	var enabledOperations = 0;
	var divId = '#startService'+(isListView?'-'+id:'');
	if ($(divId).length) {
		if(statusData.startService.hidden) {
			$(divId).addClass("hidden"); 
		} else {
			$(divId).removeClass("hidden");
			++enabledOperations;
		}
	}
	divId = '#stopService'+(isListView?'-'+id:'');
	if ($(divId).length) {
		if(statusData.stopService.hidden) {
			$(divId).addClass("hidden"); 
		} else {
			$(divId).removeClass("hidden");
			++enabledOperations;
		}
	}
	divId = '#restartService'+(isListView?'-'+id:'');
	if ($(divId).length) {
		if(statusData.restartService.hidden) {
			$(divId).addClass("hidden"); 
		} else {
			$(divId).removeClass("hidden");
			++enabledOperations;
		}
	}
	divId = '#dropdownMenuDivider'+(isListView?'-'+id:'');
	if ($(divId).length) {
		if (enabledOperations == 0) {
			$(divId).addClass("hidden");
		} else {
			$(divId).removeClass("hidden");
		}
	}
	divId = '#deleteService'+(isListView?'-'+id:'');
	if ($(divId).length) {
		if(statusData.deleteService.hidden) {
			$(divId).addClass("hidden"); 
		} else {
			$(divId).removeClass("hidden");
		}
	}
}

function startAndStopService(path, status, id){
	var request;
	var isListView;
	if (typeof(id)==='undefined') {
		isListView = false;
	} else {
		isListView = true;
	}
	request = $.ajax({
		type : 'POST',
		url : path,
		beforeSend : function (){
			$('#loadingManageServiceIcon').removeClass("hidden");
			$('#confirmManageServiceButton').addClass("hidden");
			if (isListView) {
				updateStatusMenu(status, id);
			} else {
				updateGUI(status, '#statusIcon');
				clearTimeout(timeoutStatus);
			}
		},
		success : function(data) {
			$('#loadingManageServiceIcon').addClass("hidden");
			$('#dismissButton').html("OK");
			if(data == true){
				$('#mymodalManageService').html("Successo");
				$('#manageServiceInfo').html("Richiesta inoltrata correttamente");
				console.log(status + ": " + data);
				if (isListView) {
					updateStatusMenu(status, id);
				} else {
					updateGUI(status, '#statusIcon');
				}
			} else {
				$('#mymodalManageService').html("Errore");
				$('#manageServiceInfo').html("Si è verificato un errore durante l'inoltro della richiesta");
				console.log("Error " + status);
				if (isListView) {
					updateStatusMenu(STATUS_OPERATION_ERROR, id);
				} else {
					updateGUI(STATUS_OPERATION_ERROR, '#statusIcon');
				}
			}
		},
		error : function(data){
			$('#mymodalManageService').html("Errore");
			$('#manageServiceInfo').html("Si è verificato un errore durante l'inoltro della richiesta");
			$('#loadingManageServiceIcon').addClass("hidden");
			$('#dismissButton').html("OK");
			console.log("AJAX ERROR: " + status);
			if (isListView) {
				updateStatusMenu(STATUS_OPERATION_ERROR, id);
			} else {
				updateGUI(STATUS_OPERATION_ERROR, '#statusIcon');
			}
		},
		complete: function(){
			if (!isListView) {
				clearTimeout(timeoutStatus);
				poll();
			}
		}
	});
	return request;
}

function deleteService(path, id){
	var request;
	var isListView;
	if (typeof(id)==='undefined') {
		isListView = false;
	} else {
		isListView = true;
	}
	request = $.ajax({
		type : 'DELETE',
		url : path,
		beforeSend : function (){
			$('#loadingManageServiceIcon').removeClass("hidden");
			$('#confirmManageServiceButton').addClass("hidden");
			if (isListView) {
				updateStatusMenu(STATUS_UNDEPLOY_IN_PROGRESS, id);
			} else {
				updateGUI(STATUS_UNDEPLOY_IN_PROGRESS, '#statusIcon');
				clearTimeout(timeoutStatus);
			}
		},
		success : function(data) {
			$('#loadingManageServiceIcon').addClass("hidden");
			$('#dismissButton').html("OK");
			if(data.success == true){
				$('#mymodalManageService').html("Successo");
				$('#manageServiceInfo').html("Richiesta inoltrata correttamente");
				console.log(STATUS_UNDEPLOY_IN_PROGRESS + ": " + data);
				if (isListView) {
					updateStatusMenu(STATUS_UNDEPLOY_IN_PROGRESS, id);
				} else {
					updateGUI(STATUS_UNDEPLOY_IN_PROGRESS, '#statusIcon');
				}
			} else {
				$('#mymodalManageService').html("Errore");
				$('#manageServiceInfo').html("Si è verificato un errore durante l'inoltro della richiesta");
				console.log("Error " + STATUS_UNDEPLOY_IN_PROGRESS);
				if (isListView) {
					updateStatusMenu(STATUS_OPERATION_ERROR, id);
				} else {
					updateGUI(STATUS_OPERATION_ERROR, '#statusIcon');
				}
			}
		},
		error : function(data){
			$('#mymodalManageService').html("Errore");
			$('#manageServiceInfo').html("Si è verificato un errore durante l'inoltro della richiesta");
			$('#loadingManageServiceIcon').addClass("hidden");
			$('#dismissButton').html("OK");
			console.log("AJAX ERROR: " + STATUS_UNDEPLOY_IN_PROGRESS);
			if (isListView) {
				updateStatusMenu(STATUS_OPERATION_ERROR, id);
			} else {
				updateGUI(STATUS_OPERATION_ERROR, '#statusIcon');
			}
		},
		complete: function(){
			if (!isListView) {
				clearTimeout(timeoutStatus);
				poll();
			}
		}
	});
	return request;
}
