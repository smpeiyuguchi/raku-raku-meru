/**
 * 検索フォームを動的に操作する 
 */

$(function(){
	$("#parent").on("change", function(){
		var hostUrl = "http://localhost:8080/search_form_api/refine_child_category";
		var parentIdParam = $("#parent").val();
		var childSelect = $("#child").children();
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
	
	
});