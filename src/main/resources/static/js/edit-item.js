$(function(){
	$("#parent").on("change", function(){
		var hostUrl = "http://localhost:8080/search_form_api/refine_category";
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
		}).done(function(data){
			console.log(data);
			console.dir(JSON.stringify(data));
			$('#child > option').remove();
			$('#child').append($('<option>').html("- childCategory -").val(0));
			$('#grandChild > option').remove();
			$('#grandChild').append($('<option>').html("- grandChildCategory -").val(0));
			var childOption = data.childCategoryList;
			for(var i = 0; i < childOption.length; i++){
				$('#child').append($('<option>').html(childOption[i].name).val(childOption[i].id));
			}
		}).fail(function(XMLHttpRequest, textStatus, errorThrown){
			alert("エラーが発生しました！");
			console.log("XMLHttpRequest : " + XMLHttpRequest.status);
			console.log("textStatus     : " + textStatus);
			console.log("errorThrown    : " + errorThrown.message);
		});
	});
	
	$("#child").on("change", function(){
		var hostUrl = "http://localhost:8080/search_form_api/refine_grand_child_category";
		var childIdParam = $("#child").val();
		var childSelect = $("#grandChild").children();
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
			$('#grandChild > option').remove();
			$('#grandChild').append($('<option>').html("- grandChildCategory -").val(0));
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