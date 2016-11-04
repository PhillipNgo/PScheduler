jQuery(document).ready(function($){

	$('.live-search-list li').each(function(){
		$(this).attr('data-search-term', $(this).text().toLowerCase());
	});

	$('.live-search-box').on('keyup focus', function(){
		var searchType = $('#searchtype').val();
		var searchTerm = $(this).val().toLowerCase();
		if (searchTerm.length > 1) {
			var count = 0;
			$('.live-search-list li').each(function(){
				if ((($(this).filter('[data-search-term *= ' + searchTerm + ']').length > 0 || searchTerm.length < 1) &&
									$(this).attr('id') === searchType && count < 10) || $(this).attr('id') === 'hide') {
					$(this).show();
					count++;
				} 
				else {
					$(this).hide();
				}
			});
		}
		else {
			hide();
		}
	});
	
	$('#hideb').on('click', function(){
		hide();
	});

	$('.crnb').each(function(){
		$(this).on('click', function() {
				addCRN($(this));
				hide();
		});
	});

	$('.courseb').each(function(){
		$(this).on('click', function(){
			addClass($(this));
			hide();
		});
	});
	

});

function hide() {
	$('.live-search-list li').each(function(){
		$(this).hide();
	});
}