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
				// posicao ultimo pixel do ultimo li = posicao x do ultimo li + largura de um li - 990
				// PORQUE 990?
				// se largura da tela - posicao ultimo pixel do ultimo li > 0, então o ultimo li está visível na tela

				var scrollOffset = $(document).width() - $('.container-right ul li').last().offset().left - $('.container-right ul li').outerWidth() + 990;
				console.info($(document).width(), $(document).width() - $('.container-right ul li').last().offset().left - $('.container-right ul li').outerWidth() + 990);
				container.animate({'scrollLeft' : scrollOffset > 0 ? 0 : '+=' + ($(document).width() - $('.container-right').offset().left)}, 2000, 'easeInOutCubic');
			}
		}
		var rotationInterval = setInterval(rotate, 3000);
	});
})(jQuery);