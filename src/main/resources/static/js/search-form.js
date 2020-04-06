$(function(){
	var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
      xhr.setRequestHeader(header, token);
    });
	// 親カテゴリのIDに紐づく子カテゴリ一覧を検索フォームに設置する
	$("#parent").on("change", function(){
		var hostUrl = "http://localhost:8080/search_form_api/refine_category";
		var parentIdParam = $("#parent").val();
		$.ajax({
			url : hostUrl,
			type : 'POST',
			dataType : 'json',
			data : {
				parentId : parentIdParam
			},
			async : true
		}).done(function(data) {
			console.log(data);
			console.dir(JSON.stringify(data));
			//valueが0以外のカテゴリ一覧を検索フォームから除外する
			$('#child > option[value!=0]').remove();
			var childOption = data.childCategoryList;
			for(var i = 0; i < childOption.length; i++){
				$('#child').append($('<option>').html(childOption[i].name).val(childOption[i].id));
			}
		}).fail(function(XMLHttpRequest, textStatus, errorThrown) {
			alert("エラーが発生しました！");
			console.log("XMLHttpRequest : " + XMLHttpRequest.status);
			console.log("textStatus     : " + textStatus);
			console.log("errorThrown    : " + errorThrown.message);
		});
	});
	
	// 子カテゴリに連動して孫カテゴリを設定する
	$("#child").on("change", function(){
		var hostUrl = "http://localhost:8080/search_form_api/refine_grand_child_category";
		var childIdParam = $("#child").val();
		$.ajax({
			url : hostUrl,
			type : 'POST',
			dataType : 'json',
			data : {
				childId : childIdParam
			},
			async : true
		}).done(function(data) {
			console.log(data);
			console.dir(JSON.stringify(data));
			$('#grandChild > option[value!=0]').remove();
			var grandChildOption = data.grandChildCategoryList;
			for(var i = 0; i < grandChildOption.length; i++){
				$('#grandChild').append($('<option>').html(grandChildOption[i].name).val(grandChildOption[i].id));
			}				
		}).fail(function(XMLHttpRequest, textStatus, errorThrown) {
			alert("エラーが発生しました！");
			console.log("XMLHttpRequest : " + XMLHttpRequest.status);
			console.log("textStatus     : " + textStatus);
			console.log("errorThrown    : " + errorThrown.message);
		});
	});
	
});