/**
 * 
 */

$(document).ready(function () {
   $("#dropdown-link-violator").click(function(event){
		addRemoveClass( "dropdown-caret-violator", "fa-rotate-180" );
	});
	
	$("#dropdown-link-vessel").click(function(event){
		addRemoveClass( "dropdown-caret-vessel", "fa-rotate-180" );
	});
	
});

function addRemoveClass( ident, className ) {
	var element = $("#"+ident);
	if( element.hasClass(className) ) {
		element.removeClass(className);
	}else{
		element.addClass(className);
	}
}