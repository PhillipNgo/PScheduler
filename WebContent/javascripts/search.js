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

