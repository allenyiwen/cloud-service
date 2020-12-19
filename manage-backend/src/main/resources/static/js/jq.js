$.ajaxSetup({
	cache : false,
	headers : {
		"Authorization" : "Bearer " + localStorage.getItem("access_token")
	},
	error : function(xhr, textStatus, errorThrown) {
		var status = xhr.status; // http status
		var msg = xhr.responseText;
		var message = "";
		if(msg != undefined && msg != ""){
			console.log(msg)
			var response = JSON.parse(msg);
			var exception = response.exception;
			if(exception){
				message = exception;
			}else{
				message = response.message;
			}
		}
		
		var flag = typeof(layer)=="undefined";
		
		if (status == 400) {
			if (flag) {
				alert(message);
			} else {
				layer.msg(message);
			}
		} else if (status == 401) {
			console.log('access_token过期');
			localStorage.removeItem("access_token");
			localStorage.removeItem("refresh_token");
			location.href = loginPage;
		} else if (status == 403) {
			message = "未授权";
			if (flag) {
				alert(message);
			} else {
				layer.msg(message);
			}
		} else if (status == 500) {
			message = '系统错误：' +  message + '，请刷新页面，或者联系管理员';
			if (flag) {
				alert(message);
			} else {
				layer.msg(message);
			}
		}
	}
});

function buttonDel(data, permission, pers){
	if(permission != ""){
		if ($.inArray(permission, pers) < 0) {
			return "";
		}
	}
	
	var btn = $("<button class='layui-btn layui-btn-xs' title='删除' onclick='del(\"" + data +"\")'><i class='layui-icon'>&#xe640;</i></button>");
	return btn.prop("outerHTML");
}

function buttonEdit(href, permission, pers){
	if(permission != ""){
		if ($.inArray(permission, pers) < 0) {
			return "";
		}
	}
	
	var btn = $("<button class='layui-btn layui-btn-xs' title='编辑' onclick='window.location=\"" + href +"\"'><i class='layui-icon'>&#xe642;</i></button>");
	return btn.prop("outerHTML");
}


function deleteCurrentTab(){
	var lay_id = $(parent.document).find("ul.layui-tab-title").children("li.layui-this").attr("lay-id");
	parent.active.tabDelete(lay_id);
}

function refresh_token(){
	$.ajax({
		type : 'post',
		url : domainName + '/sys/refresh_token',
		async:false,
		data : {refresh_token : localStorage.getItem("refresh_token")},
		success : function(data) {
			localStorage.setItem("access_token", data.access_token);
			localStorage.setItem("refresh_token", data.refresh_token);
		}
	});
}
