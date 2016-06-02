var dexboard = window.dexboard || {};

dexboard.slides = (function($, Handlebars) {
	
	var view = {};
	
	var presentationMode = "presentation-mode";
	
	view.init = function() {
		var projetos = new dexboard.projeto.view.Projeto();
		projetos.container.find("tr td:first-child").click(function() {
			var isOpen = $("body").hasClass(presentationMode);
			var main = new view.Main($(this).parent());
			main.toggle(!isOpen);
		});
	};
	
	view.Main = function(column) {
		
		var self = this;
		
		var projetos = new dexboard.projeto.view.Projeto();
		var body = $("body");
		
		var openSlides = function() {
			column.addClass("chosen");
			body.addClass(presentationMode);
			
			// TODO recalcular no redimensionamento
			var containerOffset = projetos.container.find("tbody").offset().left;
			var columnOffset = column.offset().left;
			var offset = columnOffset - containerOffset;
			
			column.css("transition", "1s");
			column.css("transition-delay", "1s");
			column.css("transform", "translateX(-" + offset + "px)");
			
			window.Reveal.initialize({
				"controls" : false,
				"progress" : false,
				"overview": false,
				"embedded" : true,
				"help" : false
			});
		};
		
		var closeSlides = function() {
			body.removeClass(presentationMode);
			
			column.css("transition", "");
			column.css("transition-delay", "");
			column.css("transform", "");
			column.removeClass("chosen");
		};
		
		this.toggle = function(on) {
			if (on) {
				openSlides();
			} else {
				closeSlides();
			}
		};
		
	};
	
	return {
		"view" : view
	};
	
})(jQuery, Handlebars);
