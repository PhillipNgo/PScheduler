/**
 * Functions for searching courses through the search bar
 */

jQuery(document).ready(function($){
	/**
	 * After every button pressed, searches the database for that term
	 * by what type they searching and their currently selected term
	 *//*
	$('.live-search-box').on('keyup focus', function(){
		var searchType = $('#searchtype').val(); 
		var searchTerm = $(this).val().toLowerCase().replaceAll("[\\s-]", "");
		var termYear = $('#term').val();
		if (searchTerm.length > 1) {
			$.get('LiveSearch', {search:searchTerm, type:searchType, term:termYear}, function(response) {
				$('#search').html(response);
			});
		}
		else { 
			hide();
		}
	});*/
	$('.live-search-box').on('keypress', function(e) {
		var code = e.keyCode || e.which;
		if (code == 13) {
			search();
		}
	})
	
	/**
	 * Changing the term clears the current schedule
	 */
	$('#term').on('change', function(){
	    if ($("#schedule > tbody:last-child").html().trim().length && confirm("This will clear your current schedule")) {
	    	$("#schedule > tbody:last-child").html('');
	    	hide();
	    }
	});
	
	/**
	 * set the parameters if the user comes from a modify schedule button
	 */
	setParameters();
});

function search() {
	var searchType = $('#searchtype').val(); 
	var searchTerm = $('.live-search-box').val().toLowerCase().replaceAll("[\\s-]", "");
	var termYear = $('#term').val();
	if (searchTerm.length > 1) {
		$.get('LiveSearch', {search:searchTerm, type:searchType, term:termYear}, function(response) {
			$('#search').html(response);
		});
	}
	else { 
		hide();
	}
}

/**
 * Replaces all occurrences of a regular expression with a new string
 * @param search the regex
 * @param replacement what to replace
 * @returns the string with the regex replaced
 */
String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
};

/**
 * hides the search terms
 */
function hide() {
	$('#search').html('');
}

/**
 * Reads URL for search parameters if there are any, it sets the schedule and search restrictions
 */
function setParameters() {
	var params = window.location.search.substr(1);
	if (params.length == 0) { //if there are no restrictions, stop
		return;
	}
	params = params.split('&'); //split the restrictions
	var i;
	
	for (i = 0; i < 8; i++) { //choose the time and day restrictions
		var val = params[i].split('=');
		$('[name="' + val[0] + '"]').val(val[1]); 
		$('[name="' + val[0] + '"]').selectpicker('refresh');
	}
	var days = []; //set freeDay restrictions
	for (; i < params.length - 1; i++) {
		days[i-8] = params[i].split('=')[1];
	}
	$('[name="free"]').val(days);
	$('[name="free"]').selectpicker('refresh');
	var classList = params[params.length-1].split('=')[1].split(/%7E|~/);
	
	if (classList[0].length != 0) { //check if there are any classes
		for (i = 0; i < classList.length; i++) { //for every class	
			var str = classList[i];
			var split = str.split('-');
			var index = 0;
			if ("HG".includes(split[0].charAt(split[0].length-1))) {
				index = 1;
			}
			var crns = [];
			var crnNum = [];
			var courses = [];
			var types = [];
			var profs = [];
			try {
				//get search table term and adds the schedule parameters to arrays
				if (split[0].length == 5 && !isNaN(parseInt(split[0], 10))) {
					$.ajax({
						async: false,
						type: 'GET',
						url: 'LiveSearch',
						data: {search:split[0], type:"course", term:$('#term').val()},
						success: function(response) {
							crns[crns.length] = $($($($.parseHTML(response)).get(0)));
							crnNum[crnNum.length] = split[0];
						}
					});
				}
				else {
					var search = (split[0].substring(1, split[0].length-4-index) + " " + 
								split[0].substring(split[0].length-4-index)).toLowerCase().replaceAll("[\\s-]", "");
					$.ajax({
						 async: false,
					     type: 'GET',
					     url: 'LiveSearch',
					     data: {search:search, type:"course", term:$('#term').val()},
					     success: function(response) {
					    	 var temp = $($($($.parseHTML(response)).get(0)));
					    	 if ((temp.children().get(0).name.endsWith('H') || temp.children().get(0).name.endsWith('G')) 
					    			 && index == 0) {
					    		 temp = $($($($.parseHTML(response)).get(1)));
					    	 }
					    	 courses[courses.length] = temp;
					     }
					});
					types[types.length] = split[0].substring(0, 1);
					profs[profs.length] = split[1];
				}
				//if there were CRNs, add them 
				for (var k = 0; k < crns.length; k++) {
					addClass($(crns[k]).children('button'));
					$($($('#schedule tbody tr:last').children('td').get(0)).children()[0]).val(crnNum[k]);
					crnCheck($($($('#schedule tbody tr:last').children('td').get(0)).children()[0]));
				}
				//if there were courses, add them
				for (var j = 0; j < courses.length; j++) {
					addClass($(courses[j]).children('button'));
					var cTypeSelect = $($($('#schedule tbody tr:last').children('td').get(3)).children()[0]);
					if (cTypeSelect.each(function(){
					    if (this.value == types[j]) {
					        return false;
					    }
					})) {
						cTypeSelect.val(types[j]);
					}

					var pSelect = $($($('#schedule tbody tr:last').children('td').get(4)).children()[0]);
					if (pSelect.each(function(){
					    if (this.value == profs[j]) {
					        return false;
					    }
					})) {
						pSelect.val(profs[j]);
					};
				}
				
			} catch(err) { //if there was an error, skips adding that class
				continue;
			}		
		}
	}
}
