jQuery(document).ready(function($){

	$('.live-search-box').on('keyup focus', function(){
		var searchType = $('#searchtype').val();
		var searchTerm = $(this).val().toLowerCase();
		if (searchTerm.length > 1) {
			$.get('LiveSearch', {search:searchTerm, type:searchType}, function(response) {
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
			if (split[0].length == 5 && !isNaN(parseInt(split[0], 10))) {
				$.get('LiveSearch', {search:split[0], type:"crn"}, function(response) {
					addCRN($($($($.parseHTML(response)).get(1)).children('button')));
				});
			}
			else {
				var search = (split[0].substring(1, split[0].length-4-index) + " " + split[0].substring(split[0].length-4-index)).toLowerCase();
				$.ajax({
				     async: false,
				     type: 'GET',
				     url: 'LiveSearch',
				     data: {search:search, type:"course"},
				     success: function(response) {
				    	 addClass($($($($.parseHTML(response)).get(1)).children('button')));
				     }
				});
				$($($('#schedule tbody tr:last').children('td').get(3)).children()[0]).val(split[0].substring(0, 1));
				$($($('#schedule tbody tr:last').children('td').get(4)).children()[0]).val(split[1]);
			}			
		}
	}
}
