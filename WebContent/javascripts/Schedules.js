jQuery(document).ready(function($){

	$('.hidetb').on('click', function(){
		$('#textschedules').collapse('toggle');
		if ($(this).text() === 'Hide Table') {
			$(this).text('Show Table');
		}
		else {
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

function printerFriendly() {
	$('#header').hide();
	$('#content').hide();
	$('#footer').hide();
	$('body').append("<div id='printerFriendly'>" +
						"<span class='no-print'>" +
					 		"<button class='btn btn-default' onclick='exitPrinter()'><span class='glyphicon glyphicon-circle-arrow-left' style='vertical-align:top'> Return</span></button>" +
					 		"<button class='btn btn-default' onclick='togglePrint(1)'>Toggle Text</button>" +
					 		"<button class='btn btn-default' onclick='togglePrint(2)'>Toggle Visual</button>" +
					 		"<button class='btn btn-default' onclick='window.print()'><span class='glyphicon glyphicon-print' style='vertical-align:top'> Print</span></button>" +
					    "</span>" +
					    $('#tablebody').html() + 
					 "</div>");
	window.print();
}

function togglePrint(index) {
	$($('#printerFriendly').children()[index]).toggle();
}

function exitPrinter() {
	$('#header').show();
	$('#content').show();
	$('#footer').show();
	$('#printerFriendly').remove();
}

function dlHref() {
	var a = document.getElementById('download');
	a.href += "?" + window.location.search.substr(1);
}

function togglePanel() {
	$('#schedule-panel').collapse('toggle');
	$('#data-panel').collapse('toggle');
}

function changeIcon(button) {
	$(button).toggleClass('glyphicon-plus glyphicon-minus');	
}

function changeSchedule(inc) {
	var list = document.getElementById("tableschedules");
	var currNum = parseInt(list.attributes["name"].value);
	var listItem = list.getElementsByTagName("table");
	var list2 = document.getElementById("textschedules");
	var listItem2 = list2.getElementsByTagName("table");
	if (currNum + inc >= 0 && currNum + inc < Math.min(listItem.length, 500)) {
		listItem[currNum].style.display = "none";
		listItem[currNum + inc].style.display = "table";
		list.attributes["name"].value = listItem[currNum + inc].id;
		listItem2[currNum].style.display = "none";
		listItem2[currNum + inc].style.display = "table";
		list2.attributes["name"].value = listItem[currNum + inc].id;
		$('#changet').val(currNum + inc + 1);
		var title = document.getElementById('title');
		var split = title.innerText.split(' ');
		split[1] = (parseInt(split[1]) + inc) + "";
		title.innerHTML = "<b>" + split[0] + " " + split[1] + " " + split[2] + " " + split[3] + "</b>";
	}
}