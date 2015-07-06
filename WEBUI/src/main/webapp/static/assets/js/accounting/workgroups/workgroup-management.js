$(function() {



	$(".editUsersOfTheWorkgroupButton").click(function() {

		var workgroupId = $(this).data('workgroup-id');

		$('#workgroupUsersTable').bootstrapTable('refresh', {
		    url: '/async/accounting/workgroup/' + workgroupId + '/users'
		});

		
	});

});