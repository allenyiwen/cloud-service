function checkPermission() {
	var pers = [];
	$.ajax({
		type : 'get',
		url : domainName + '/api-u/users/current',
		contentType : "application/json; charset=utf-8",
		async : false,
		success : function(data) {
			pers = data.permissions;
			$("[permission]").each(function() {
				var per = $(this).attr("permission");
				if ($.inArray(per, pers) < 0) {
					$(this).hide();
				}
			});
		}
	});
	
	return pers;
}