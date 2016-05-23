var dexboard = window.dexboard || {};

dexboard.projeto = (function($, Handlebars) {
	
	Handlebars.registerHelper("lowercase", function(str) {
		return str.toLowerCase();
	});
	
	var service = {};
	var template = null;
	var view = {};
	
	service.query = function() {
		var query = document.location.search.substr(1).split("=");
		var equipe = (query.length > 1 && query[0] === "equipe") ? query[1] : undefined;
		
		return $.getJSON("/query", {"equipe" : equipe}).done(function(projetos) {
			if (projetos && projetos.length > 0) {
				(new view.Projeto()).init(projetos);
			}
		});
	};
	
	view.init = function() {
		var source = $("#dexboard-template").html();
		template = Handlebars.compile(source);
		
		service.query();
	};
	
	view.Projeto = function() {
		
		var self = this;
		
		this.container = $("#tabela-principal");
		
		this.init = function(projetos) {
			
			var indicadores = projetos[0].indicadores;
			
			self.container.html(template({
				"projetos" : projetos,
				"indicadores" : indicadores
			}));
			
			self.container.find(".indicador").click(function() {
				var indexProjeto = parseInt($(this).parent().data("index"));
				var indexIndicador = parseInt($(this).data("index"));
				
				var projeto = projetos[indexProjeto];
				var indicador = projeto.indicadores[indexIndicador];
				
				var dialog = new dexboard.indicador.view.Dialog();
				dialog.open(projeto, indicador);
			});
			
			dexboard.indicador.view.init();
		}
	};
	
	return {
		"view" : view,
		"service" : service
	};
	
})(jQuery, Handlebars);
