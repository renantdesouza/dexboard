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

    $(document).ready(function()
    {

        var from = APP.obtemParametroDeURL('from');
        if (from == 'TV') {
        	$('body').addClass('tv');
        }

		if ( $('body').hasClass('tv') === true ) {
			var rotate = function()
			{
				if ( $('#dialog').data('opened') !== true )
				{
					var container = $('.container-right');
					var last = $('.container-right ul li').last();
					var lastLiPx = last.offset().left + last.width();
					var limitLeft = container.offset().left + container.width();

					var scrollOffset = limitLeft - lastLiPx;

					container.animate({'scrollLeft' : scrollOffset > 0 ? 0 : '+=' + (container.width() - last.width())}, 1, 'easeInOutCubic');
				}
			}
			var rotationInterval = setInterval(rotate, 15000);
		}
    });
})(jQuery);