var dexboard = window.dexboard || {};

dexboard.slides = (function($, Handlebars) {
	
	var view = {};
	
	var presentationMode = "presentation-mode";
	
	$(document).keydown(function(e) {
		if (e.which === 27) {
			var projetos = new dexboard.projeto.view.Projeto();
			var column = projetos.container.find("tr.chosen");
			if (column.length === 1) {
				(new view.Main(column)).toggle(false);
			}
		}
	});
	
	view.init = function() {
		var projetos = new dexboard.projeto.view.Projeto();
		projetos.container.find("tr.slides td:first-child").click(function() {
			var isOpen = $("body").hasClass(presentationMode);
			var main = new view.Main($(this).parent());
			main.toggle(!isOpen);
		});
	};
	
	view.Main = function(column) {
		
		var self = this;
		
		var projetos = new dexboard.projeto.view.Projeto();
		var body = $("body");
		var indexes = [-1, 6, 5, 2, 4, 3, 1];
		
		var highlightIndicador = function(event) {
			var slide = event.currentSlide;
			var indicadorId = $(slide).data("indicador");
			var index = indexes[indicadorId] + 1;
			
			$(".chosen-indicador").removeClass("chosen-indicador");
			$(".unchosen-indicador").removeClass("unchosen-indicador");
			projetos.container.find("th:nth-child(" + index + ")").addClass("chosen-indicador");
			projetos.container.find("th:not(:nth-child(" + index + "))").addClass("unchosen-indicador");
			projetos.container.find("tr.chosen td:nth-child(" + index + ")").addClass("chosen-indicador");
			projetos.container.find("tr.chosen td:not(:nth-child(" + index + "))").addClass("unchosen-indicador");
		};
		
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
			
			Reveal.addEventListener("ready", highlightIndicador);
			Reveal.addEventListener("slidechanged", highlightIndicador);
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
