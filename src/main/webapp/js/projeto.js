var dexboard = window.dexboard || {};

dexboard.projeto = (function($, Handlebars) {
	
	Handlebars.registerHelper("lowercase", function(str) {
		return str.toLowerCase();
	});
	
	var model = {};
	var service = {};
	var template = null;
	var view = {};
	
	var isFullscreen = function() {
		return (!window.screenTop && !window.screenY);
	};
	
	var zoomVertical = function() {
		var container = (new view.Projeto()).container;
		var height = window.innerHeight;
		var tableHeight = container.height() + container.offset().top + 15;
		var scale = (height / tableHeight).toFixed(2);
		var width = Math.floor((1 / scale) * 100);
		
		console.info(tableHeight, scale, width);
		
		$("html")
			.css("transform", "scale(" + scale + ")")
			.css("width", width + "%");
	};
	
	var autoScroll = function() {
		var container = (new view.Projeto()).container.find("tbody");
		var lastProject = container.find("tr:last");
		
		// largura de cada coluna de projeto
		var projectWidth = lastProject.width();
		var columnWidth = projectWidth + 4;
		
		// comeco dos projetos em relacao a pagina
		var offset = container.offset().left;
		
		// largura ate o scroll
		var visibleWidth = container.width();
		
		// largura ate o final da pagina
		var totalWidth = lastProject.offset().left + projectWidth - offset;
		
		if (visibleWidth < totalWidth) {
			var currentScroll = container.scrollLeft();
			var deltaScroll = Math.floor((visibleWidth / columnWidth)) * columnWidth;
			var scrollTo = currentScroll + deltaScroll;
			
			container.animate({"scrollLeft" : scrollTo + "px"});
			return scrollTo;
			
		} else {
			container.animate({"scrollLeft" : "0"});
			return 0;
		}
	};
	
	model.Indicador = function(jsonIndicador) {
		
		var self = this;
		
		this.id = jsonIndicador.id;
		this.nome = jsonIndicador.nome;
		this.quantidadeAtraso = 0;
		this.quantidadePerigo = 0;
		this.quantidadeAtencao = 0;
		this.quantidadeOk = 0;
		
		this.addQuantidade = function(status) {
			if (status === "ATRASADO") {
				self.quantidadeAtraso = self.quantidadeAtraso + 1;
			} else if (status === "PERIGO") {
				self.quantidadePerigo++;
			} else if (status === "ATENCAO") {
				self.quantidadeAtencao++;
			} else {
				self.quantidadeOk++;
			}
		}
	};
	
	model.Indicador.fromProjetos = function(projetos) {
		if (!projetos[0]) return [];
		
		var map = {};
		var indicadores = projetos[0].indicadores.map(function(jsonIndicador) {
			var indicador = new model.Indicador(jsonIndicador);
			map[jsonIndicador.id] = indicador;
			return indicador;
		});
		
		projetos.forEach(function(projeto) {
			projeto.indicadores.forEach(function(json) {
				var status = json.atrasado ? "ATRASADO" : json.classificacao;
				map[json.id].addQuantidade(status);
			});
		});
		
		return indicadores;
	};
	
	model.QueryWrapper = function(projetos) {
		this.projetos = projetos || [];
		this.indicadores = model.Indicador.fromProjetos(this.projetos);
	};
	
	service.query = function() {
		var query = document.location.search.substr(1).split("=");
		var equipe = (query.length > 1 && query[0] === "equipe") ? query[1] : undefined;
		
		return $.getJSON("/query", {"equipe" : equipe}).done(function(projetos) {
			var queryWrapper = new model.QueryWrapper(projetos);
			(new view.Projeto()).init(queryWrapper);
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
		
		this.init = function(queryWrapper) {
			
			self.container.html(template(queryWrapper));
			
			if (isFullscreen()) {
				zoomVertical(); // TV Mode
				setInterval(autoScroll, 15000);
			}
			
			self.container.find(".indicador").click(function() {
				var indexProjeto = parseInt($(this).parent().data("index"));
				var indexIndicador = parseInt($(this).data("index"));
				
				var projeto = queryWrapper.projetos[indexProjeto];
				var indicador = projeto.indicadores[indexIndicador];
				
				var dialog = new dexboard.indicador.view.Dialog();
				dialog.open(projeto, indicador);
			});
			
			dexboard.indicador.view.init();
		}
	};
	
	return {
		"model" : model,
		"view" : view,
		"service" : service
	};
	
})(jQuery, Handlebars);
