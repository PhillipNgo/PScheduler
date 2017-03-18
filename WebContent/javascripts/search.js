jQuery(document).ready(function($){

	$('.live-search-box').on('keyup focus', function(){
		var searchType = $('#searchtype').val();
		var searchTerm = $(this).val().toLowerCase();
		var termYear = $('#term').val();
		if (searchTerm.length > 1) {
			$.get('LiveSearch', {search:searchTerm, type:searchType, term:termYear}, function(response) {
				$('#search').html(response);
			});
		}
		else { 
			hide();
		}
	});
	
	setParameters();
});

function hide() {
	$('#search').html('');
}

function setParameters() {
	var params = window.location.search.substr(1);
	if (params.length == 0) {
		return;
	}
	params = params.split('&');
	var i;
	for (i = 1; i < 8; i++) { 
		var val = params[i].split('=');
		$('[name="' + val[0] + '"]').val(val[1]); 
		$('[name="' + val[0] + '"]').selectpicker('refresh');
	}
	var days = [];
	for (i; i < params.length; i++) {
		days[i-8] = params[i].split('=')[1];
	}
	$('[name="free"]').val(days);
	$('[name="free"]').selectpicker('refresh');
	var classList = params[0].split('=')[1].split('%7E');
	if (classList[0].length != 0) {
		for (i = 0; i < classList.length; i++) {
			var str = classList[i];
			var split = str.split('-');
			var index = 0;
			if (split[0].charAt(split[0].length-1) == 'H') {
				index = 1;
			}
			var crns = [];
			var crnNum = [];
			var courses = [];
			var types = [];
			var profs = [];
			try {
				if (split[0].length == 5 && !isNaN(parseInt(split[0], 10))) {
					$.ajax({
						async: false,
						type: 'GET',
						url: 'LiveSearch',
						data: {search:split[0], type:"course", term:$('#term').val()},
						success: function(response) {
							crns[crns.length] = $($($($.parseHTML(response)).get(1)));
							crnNum[crnNum.length] = split[0];
						}
					});
				}
				else {
					var search = (split[0].substring(1, split[0].length-4-index) + " " + split[0].substring(split[0].length-4-index)).toLowerCase();
					$.ajax({
						 async: false,
					     type: 'GET',
					     url: 'LiveSearch',
					     data: {search:search, type:"course", term:$('#term').val()},
					     success: function(response) {
					    	 var temp = $($($($.parseHTML(response)).get(1)));
					    	 if (temp.children().get(0).name.endsWith('H') && index == 0) {
					    		 temp = $($($($.parseHTML(response)).get(2)));
					    	 }
					    	 courses[courses.length] = temp;
					     }
					});
					types[types.length] = split[0].substring(0, 1);
					profs[profs.length] = split[1];
				}
				for (var k = 0; k < crns.length; k++) {
					addClass($(crns[k]).children('button'));
					$($($('#schedule tbody tr:last').children('td').get(0)).children()[0]).val(crnNum[k]);
					crnCheck($($($('#schedule tbody tr:last').children('td').get(0)).children()[0]));
				}
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
				
			} catch(err) {
				continue;
			}		
		}
	}
}
