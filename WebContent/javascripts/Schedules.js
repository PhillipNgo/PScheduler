jQuery(document).ready(function($){

	$('.hidetb').on('click', function(){
		if ($(this).text() === 'Hide Table') {
			$('#textschedules').hide();
			$(this).text('Show Table');
		}
		else {
			$('#textschedules').show();
			$(this).text('Hide Table');
		}
	});
	
	$("#changet").on('keyup', function(e) {
	    if (e.keyCode == 13) {
	    	var num = parseInt($(this).val()) - 1;
	        if (!isNaN(num)) {
	        	changeSchedule(num - parseInt($('#tableschedules').attr('name')));
	        }
	    }
	});
	
	$(document).keydown(function(e) {
	    switch(e.which) {
	        case 37: changeSchedule(-1);
	        		 break; 
	        case 39: changeSchedule(1);
	        		 break;
	        default: return; 
	    }
	    e.preventDefault();
	});
});



function changeSchedule(inc) {
	var list = document.getElementById("tableschedules");
	var currNum = parseInt(list.attributes["name"].value);
	var listItem = list.getElementsByTagName("table");
	var list2 = document.getElementById("textschedules");
	var listItem2 = list2.getElementsByTagName("table");
	if (currNum + inc >= 0 && currNum + inc < listItem.length) {
		listItem[currNum].style.display = "none";
		listItem[currNum + inc].style.display = "table";
		list.attributes["name"].value = listItem[currNum + inc].id;
		listItem2[currNum].style.display = "none";
		listItem2[currNum + inc].style.display = "table";
		list2.attributes["name"].value = listItem[currNum + inc].id;
		$('#changet').val(currNum + inc + 1);
	}
}