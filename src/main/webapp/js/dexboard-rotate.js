(function($)
{	
	var APP = {
		obtemParametroDeURL : function(parametro) {

	       	var Url = function(urlString) {
	       		var _params = {};

	       		urlString.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(match, key, value) {
	       			_params[key] = value;
				});

				this.param = function(paramName) {
					return _params[paramName];
				};
	         };

	         var url = new Url(window.location.href);
	         return url.param(parametro);
		}
	}

	var animateProjectsList = function(callback) {
		var container = $('.container-right');
		var last = $('.container-right ul li').last();
		var lastLiPx = last.offset().left + last.width();
		var limitLeft = container.offset().left + container.width();

		var scrollOffset = limitLeft - lastLiPx;
		var incrementoHorizontal = container.width() - last.width();

		container.animate({'scrollLeft' : scrollOffset > 0 ? 0 : '+=' + incrementoHorizontal}, 1, 'easeInOutCubic');
		if (typeof callback == 'function')
			callback(scrollOffset > 0 ? 0 : $('.container-right').scrollLeft() + incrementoHorizontal);
	}

	var animateProjectsBar = function(newProjectsListOffset) {
		var liWidth = $('#lista-projetos > li:first').width();
		var indiceDoProjetoExibido = Math.ceil(newProjectsListOffset / (liWidth + 25));
		var widthTh = $("#heatbar table tr th").width() + 2;
		var projectsBeenShown = 12;
		var totalProjects = $('#lista-projetos > li').length;

		$('#heatbar-slider').css('margin-left', (indiceDoProjetoExibido + projectsBeenShown > totalProjects ? totalProjects - projectsBeenShown : indiceDoProjetoExibido) * widthTh + "px" );
	}
	
    $(document).ready(function(){
    	var from = APP.obtemParametroDeURL('from');
        if (from == 'TV') {
        	$('body').addClass('tv');
        }

		if ( $('body').hasClass('tv') === true ) {
			var rotate = function()
			{
				if ( $('#dialog').data('opened') !== true )
				{
					animateProjectsList(animateProjectsBar);
				}
			}
			var rotationInterval = setInterval(rotate, 15000);
		}
    });
})(jQuery);