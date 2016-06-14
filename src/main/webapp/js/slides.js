var dexboard = window.dexboard || {};

dexboard.slides = (function($, Handlebars) {
	
	var view = {};
	
	var presentationMode = "presentation-mode";
	var template;
	
	var initialized = false;
	var fixOverlappingSlides = function() {
		// sem isso os slides ficam sobrepostos ao abrir a apresentacao
		// de dois projetos diferentes sem navegar pelos slides 
		if (initialized) {
			window.Reveal.slide(2);
		} else {
			initialized = true;
		}
	};
	
	$(document).keydown(function(e) {
		if (e.which === 27) {
			var projetos = new dexboard.projeto.view.Projeto();
			var column = projetos.container.find("tr.chosen");
			if (column.length === 1) {
				(new view.Main(column)).toggle(false);
			}
		}
	});
	
	view.init = function(projetos) {
		var projetosView = new dexboard.projeto.view.Projeto();
		
		if (!template) {
			var source = $("#slides-apresentacao").html();
			template = Handlebars.compile(source);
		}
		
		projetosView.container.find("tr td:first-child").click(function() {
			var column = $(this).parent();
			var index = column.data("index");
			var isOpen = $("body").hasClass(presentationMode);
			var main = new view.Main(column, projetos[index].indicadores);
			main.toggle(!isOpen);
		});
	};
	
	view.Main = function(column, indicadores) {

		var self = this;
		
		var projetos = new dexboard.projeto.view.Projeto();
		var body = $("body");
		
		var highlightIndicador = function(event) {
			var slide = event.currentSlide;
			var index = $(slide).data("indicador") + 2;
			
			$(".chosen-indicador").removeClass("chosen-indicador");
			$(".unchosen-indicador").removeClass("unchosen-indicador");
			projetos.container.find("th:nth-child(" + index + ")").addClass("chosen-indicador");
			projetos.container.find("th:not(:nth-child(" + index + "))").addClass("unchosen-indicador");
			projetos.container.find("tr.chosen td:nth-child(" + index + ")").addClass("chosen-indicador");
			projetos.container.find("tr.chosen td:not(:nth-child(" + index + "))").addClass("unchosen-indicador");
		};
		
		var openSlides = function() {
			fixOverlappingSlides();
			
			column.addClass("chosen");
			body.addClass(presentationMode);
			
			$("#presentation-overlay").html(template({"indicadores" : indicadores}));
			
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
			window.Reveal.sync();
			
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
