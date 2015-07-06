
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



//iaasHeal is a model variable set in GlobalControllerAdvice

$(document).ready(function() {
	if(window['iaasHealt'] != undefined){
		updateIaaSStatus(iaasHealt);
	} else {

		$.ajax({
			type : 'GET',
			dataType : 'json',
			url : '/async/monitoring/iaasStatus',
			success : function(data) {
				retry = 0;
				timerMultiplier = 5;
				updateIaaSStatus(data);
				iaasHealt = data;
			},
			error : function(){
				if(retry > 3){
					timerMultiplier = 20;
				} else {
					timerMultiplier = 10;
					retry++;
				}
				errorIaaSStatus();
			}
		});

	}

	var timerMultiplier = 1;
	var retry = 0;
	(function poll() {
		setTimeout(function() {
			$.ajax({
				type : 'GET',
				dataType : 'json',
				url : '/async/monitoring/iaasStatus',
				success : function(data) {
					retry = 0;
					timerMultiplier = 5;
					updateIaaSStatus(data);
					iaasHealt = data;
				},
				error : function(){
					if(retry > 3){
						timerMultiplier = 20;
					} else {
						timerMultiplier = 10;
						retry++;
					}
					errorIaaSStatus();
				},
				complete : poll
			});
		}, (timerMultiplier*6000));
	})();

	function updateIaaSStatus(data) {

		switch (data['compute'].toLowerCase()) {

		case 'ok':
			$("#iaasComputeStatus").removeClass();
			$("#iaasComputeStatus").addClass('fa fa-check-circle');
			$("#iaasComputeStatus").css('color', '#43ac6a');
			break;
		case 'warning':
			$("#iaasComputeStatus").removeClass();
			$("#iaasComputeStatus").addClass('fa fa-exclamation-circle');
			$("#iaasComputeStatus").css('color', '#FFFF00');
			break;
		case 'problem':
			$("#iaasComputeStatus").removeClass();
			$("#iaasComputeStatus").addClass('fa fa-times-circle');
			$("#iaasComputeStatus").css('color', '#FF0000');
			break;
		default:
			break;

		}

		switch (data['network'].toLowerCase()) {

		case 'ok':
			$("#iaasNetworkStatus").removeClass();
			$("#iaasNetworkStatus").addClass('fa fa-check-circle');
			$("#iaasNetworkStatus").css('color', '#43ac6a');
			break;

		default:
			$("#iaasNetworkStatus").removeClass();
		$("#iaasNetworkStatus").addClass('fa fa-times-circle');
		$("#iaasNetworkStatus").css('color', '#FF0000');
		break;

		}

		switch (data['storage'].toLowerCase()) {

		case 'ok':
			$("#iaasStorageStatus").removeClass();
			$("#iaasStorageStatus").addClass('fa fa-check-circle');
			$("#iaasStorageStatus").css('color', '#43ac6a');
			break;
		default:
			$("#iaasStorageStatus").removeClass();
		$("#iaasStorageStatus").addClass('fa fa-times-circle');
		$("#iaasStorageStatus").css('color', '#FF0000');
		break;
		}
	}

	function errorIaaSStatus(data) {

		console.log('Monitoring: unable to retrieve iaas status');

		$("#iaasComputeStatus").removeClass();
		$("#iaasComputeStatus").addClass('fa fa-times-circle');
		$("#iaasComputeStatus").css('color', '#FF0000');

		$("#iaasNetworkStatus").removeClass();
		$("#iaasNetworkStatus").addClass('fa fa-times-circle');
		$("#iaasNetworkStatus").css('color', '#FF0000');

		$("#iaasStorageStatus").removeClass();
		$("#iaasStorageStatus").addClass('fa fa-times-circle');
		$("#iaasStorageStatus").css('color', '#FF0000');
	}


	$(document).ready(function() {
		if(window['iaasHealt'] != undefined){
			updateIaaSStatus(iaasHealt);
		} else {
			$.ajax({
				type : 'GET',
				dataType : 'json',
				url : '/async/monitoring/metrics-vm',
				success : function(data) {
					retry = 0;
					timerMultiplier = 5;
					dataRetrievedFromMonit(data);
					iaasHealt = data;
				},
				error : function(){
					if(retry > 3){
						timerMultiplier = 20;
					} else {
						timerMultiplier = 10;
						retry++;
					}
					errorIaaSStatus();
				}
			});
		}
	})

//	<!-- These are useful to create LINE charts -->
//	<link rel="stylesheet"
//	th:href="@{/static/assets/css/morris-0.5.1.min.css}" />
//	<script th:src="@{/static/assets/js/raphael-2.1.0.min.js}"></script>
//	<script th:src="@{/static/assets/js/morris-0.5.1.min.js}"></script>

//	<!-- Line Chart -->
//	<script type="text/javascript">

	function dataRetrievedFromMonit(data) {
		"use strict"
		//ToDO:verify the following returned parameters are the correct ones
		var arrayValues = data['value'].toLowerCase();
		var arrayDates = data['time'].toLowerCase();
		//toDo: for each to build the result:
		var iVal, iTime, len;

		for (iVal = 0, len = arrayValues.length; iVal < len; ++iVal) {
			for (iTime = 0, len = arrayValues.length; iTime < len; ++iTime) {
				var map = new Object(); // or var map = {};
				map[arrayValues] = arrayDates;
			}
		}
		var arrayResult = result[map];


		new Morris.Line({
			// ID of the element in which to draw the chart.
			element : 'lineChartCPU', 
			// Chart data records -- each entry in this array corresponds to a point on
			// the chart.
			 
			//ToDo: Capire perchè non accetta arrayResult
//			arrayResult,
			
			data : [ {
				date : '2014-10-16',
				value : 20
			},{
				date : '2014-10-17',
				value : 20
			},{
				date : '2014-10-18',
				value : 20
			}, {
				date : '2014-10-19',
				value : 10
			}, {
				date : '2014-10-20',
				value : 5
			}, {
				date : '2014-10-21',
				value : 5
			}, {
				date : '2014-10-22',
				value : 20
			} ],
			// A list of names of data record attributes that contain y-values.
			ykeys : [ 'value' ],
			// Labels for the ykeys -- will be displayed when you hover over the
			// chart.
			labels : [ 'Valore' ],

			// The name of the data record attribute that contains x-values.
			xkey : 'date',

			xLabelFormat: function(date) {
				return date.getDate()+'/'+(date.getMonth()+1)+'/'+date.getFullYear(); 
			},
			//xLabels : 'day',

			resize : true
		});
	};
//	);
//	</script>

});