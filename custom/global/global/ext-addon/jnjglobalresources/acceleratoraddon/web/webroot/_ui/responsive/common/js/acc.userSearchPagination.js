ACC.userpaginationsort = {

	bindAll : function() {
		ACC.userpaginationsort.showMoreUsers();
	},

	showMoreUsers : function(e) {
		$(".showMoreUsers").click(
				function() {
					var searcUserform = $('#searchUserForm');
					$('<input>').attr({
						type : 'hidden',
						id : 'showMore',
						name : 'showMore',
						value : true
					}).appendTo(searcUserform);
					$("#searchUserForm").attr("action",
							ACC.config.contextPath + "/resources/usermanagement");
					var showMoreCounterUser = $('#showMoreCounterUserM').val();
					var counterIntegerUser = parseInt(showMoreCounterUser);
					counterIntegerUser = counterIntegerUser + 1;
					$('#showMoreCounterUserM').val(counterIntegerUser);
					searcUserform.submit();
				})

		$("#userManagementSubmit").click(function() {
			$("#searchFlagUserSearch").val('true');
			$("#showMoreCounterUserM").val('1');
			$("#searchUserForm").submit();
		})

		$("#createUserProfileUserMangement").click(function() {
			$("#searchUserForm").attr("action",
					ACC.config.contextPath + "/resources/usermanagement/create");
			$("#searchUserForm").submit();
		})
		
		/*$(".editUserProfileUserMangement").click(function() {
			$("#searchUserForm").attr("action",$(this).attr("data"));
			$("#searchUserForm").submit();
		})*/
		

		$("#showUserM").change(function() {
			var showMore = $('#showMoreFlag').val();
			if(showMore == null || showMore == '')
			{
				$('#showMoreFlag').val(false);
			}
			
			$("#searchUserForm").submit();
		})
		
		$("#userManagementReset").click(function() {
			$("#searchFlagUserSearch").val('false');
			$("#showMoreCounterUserM").val('1');
			$("#searchUserForm").submit();
		})

		$("#sortby").change(function() {
			$("#searchUserForm").submit();
		})
	}

};
$(document).ready(function() {
	ACC.userpaginationsort.bindAll();
});
