(function($) 
{		
	$(document).ready(function() 
	{
		var container = $('.container-right');
		var rotate = function()
		{
			if ( $('#dialog').data('opened') !== true )
			{				
				// 50 é o margin-right de cada <li/> + .container-right somadas.
				var scrollOffset = $(document).width() - $('.container-right ul li').last().offset().left - $('.container-right ul li').outerWidth() - 50;
				container.animate({'scrollLeft' : scrollOffset > 0 ? 0 : '+=1200px'}, 4000, 'easeInOutCubic');
			}
		}
		var rotationInterval = setInterval(rotate, 15000);
	});	
})(jQuery);