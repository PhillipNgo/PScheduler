jQuery(document).ready(function($){

	$('.live-search-list li').each(function(){
		$(this).attr('data-search-term', $(this).text().toLowerCase());
	});

	$('.live-search-box').on('keyup', function(){
		var searchTerm = $(this).val().toLowerCase();
		if (searchTerm.length > 0) {
			var count = 0;
			$('.live-search-list li').each(function(){
				if (($(this).filter('[data-search-term *= ' + searchTerm + ']').length > 0 || searchTerm.length < 1) && count < 10) {
					$(this).show();
					count++;
				} else {
					$(this).hide();
				}
			});
		}
		else {
			$('.live-search-list li').each(function(){
				$(this).hide();
			});
		}
	});

});