/**
 * Button functions to be used on the schedule generated page
 */

jQuery(document).ready(function($){
	
	/**
	 * Hides the text table of schedules
	 */
	$('.hidetb').on('click', function(){
		$('#hidetb-but').toggleClass('glyphicon-triangle-top glyphicon-triangle-bottom');	
		$('.table-condensed').each(function() {
		    $(this).toggle();
		});
	});
	
	/**
	 * Changes Schedule based on number input
	 */
	$("#changet").on('keyup', function(e) {
	    if (e.keyCode == 13) {
	    	var num = parseInt($(this).val()) - 1;
	    	if (!isNaN(num)) {
	    		changeSchedule(num);
	    	}
	    }
	});
	
	/**
	 * Hotkeys for advancing/decrementing schedules
	 * Left arrow: decrement
	 * Right arrow: advance
	 */
	$(document).keydown(function(e) {
	    switch(e.which) {
	        case 37: changeSchedule('prev');
	        		 break; 
	        case 39: changeSchedule('next');
	        		 break;
	        default: return; 
	    }
	    e.preventDefault();
	});
	
	/**
	 * Disable auto carousel slide
	 */
	$('#schedlist').carousel({
	    interval: false
	}); 
	
	setGenHref();
});

/**
 * Creates the printer friendly page by hiding unneeded info
 */
function printerFriendly() {
	$('#header').hide();
	$('#schedules').hide();
	$('body').append("<div id='printerFriendly'>" +
						"<span class='no-print'>" +
					 		"<button class='btn btn-default' onclick='exitPrinter()'><span class='glyphicon glyphicon-circle-arrow-left' style='vertical-align:top'> Return</span></button>" +
					 		"<button class='btn btn-default' onclick='togglePrint(1)'>Toggle Text</button>" +
					 		"<button class='btn btn-default' onclick='togglePrint(3)'>Toggle Visual</button>" +
					 		"<button class='btn btn-default' onclick='window.print()'><span class='glyphicon glyphicon-print' style='vertical-align:top'></span></button>" +
					    "</span>" +
					    $('.item.active').html() + 
					 "</div>");
	window.print();
}

/**
 * Toggles the printer friendly page
 * @param index
 */
function togglePrint(index) {
	$($('#printerFriendly').children()[2]).toggle();
	$($('#printerFriendly').children()[index]).toggle();
}

/**
 * Unhides the printer friendly page
 */
function exitPrinter() {
	$('#header').show();
	$('#schedules').show();
	$('#printerFriendly').remove();
}

/**
 * Excel Download of the schedules
 */
function dlHref() {
	var a = document.getElementById('download');
	a.href += "?" + window.location.search.substr(1);
}

/**
 * Toggles the search data and schedule display
 */
function togglePanel() {
	$('#schedule-panel').collapse('toggle');
	$('#data-panel').collapse('toggle');
}

/**
 * Changes Plus drop down to Minus dropdown and viceversa
 * @param button the button pressed
 */
function changeIcon(button) {
	$(button).toggleClass('glyphicon-plus glyphicon-minus');	
}

function setGenHref() {
	$($('#maingentab').children().get(0)).attr('href', $($('.current').children().get(0)).attr('href'));
}

/**
 * Changes the tab and content based on the secondary menu
 * @param button the tab pressed
 */
function changeSecondaryTab(button) {
	$('#generator').hide();
	$('#gentab').attr('class', '');
	
	$('#results').hide();
	$('#restab').attr('class', '');
	
	$('#schedules').hide();
	$('#schedtab').attr('class', '');
	
	$($(button).attr('href')).show();
	$(button).parent().attr('class', 'current');
	setGenHref();
}

/**
 * Changes the displayed schedule by the increment
 * @param inc the increment
 */
function changeSchedule(index) {
	if (!$('#schedlist').length) {
		return
	}
	$('#schedlist').carousel(index);
	var title = document.getElementById('title');
	var split = title.innerText.split(' ');
	var newIndex = $('#schedlist .active').index('#schedlist .item') + 1
	title.innerHTML = "<b>" + split[0] + " " + newIndex + " " + split[2] + " " + split[3] + "</b>";
	$('#changet').val(newIndex);
}