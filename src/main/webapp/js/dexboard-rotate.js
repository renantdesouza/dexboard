(function($)
{
    $(document).ready(function()
    {
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

					container.animate({'scrollLeft' : scrollOffset > 0 ? 0 : '+=' + container.width()}, 5000, 'easeInOutCubic');
				}
			}
			var rotationInterval = setInterval(rotate, 15000);
		}
    });
})(jQuery);