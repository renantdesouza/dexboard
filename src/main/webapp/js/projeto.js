var dexboard = window.dexboard || {};

dexboard.projeto = (function($, Handlebars) {
	
	Handlebars.registerHelper("lowercase", function(str) {
		return str.toLowerCase();
	});
	
	var model = {};
	var service = {};
	var template = null;
	var view = {};
	
	var isTvMode = function() {
		var search = document.location.search;
		return search.indexOf("from") > 0 && search.indexOf("TV") > 0;
	};
	
	var queryEquipe = function() {
		var query = document.location.search.substr(1).split("=");
		return (query.length > 1 && query[0] === "equipe") ? query[1] : undefined;
	};
	
	model.StatusHistogram = function() {
		
		var self = this;

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
		};

	};
	
	model.Indicador = function(jsonIndicador) {
		
		var self = this;
		
		this.id = jsonIndicador.id;
		this.nome = jsonIndicador.nome;
		this.status = new model.StatusHistogram();
		
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
				map[json.id].status.addQuantidade(status);
			});
		});
		
		return indicadores;
	};
	
	model.QueryWrapper = function(projetos) {
		
		var self = this;
		
		this.projetos = projetos || [];
		this.indicadores = model.Indicador.fromProjetos(this.projetos);
		this.tvMode = isTvMode();
		
		this.status = new model.StatusHistogram();
		
		projetos.forEach(function(projeto) {
			var status = projeto.atrasado ? "ATRASADO" : projeto.classificacao;
			self.status.addQuantidade(status);
		});
		
	};
	
	service.query = function() {
		var equipe = queryEquipe();
		
		return $.getJSON("/query", {"equipe" : equipe}).done(function(projetos) {
			var queryWrapper = new model.QueryWrapper(projetos);
			(new view.Projeto()).init(queryWrapper);
		});
	};
	
	service.reloadPlanilhas = function() {
		return $.getJSON("/reload/projetos").done(service.query);
	};
	
	view.init = function() {
		var source = $("#dexboard-template").html();
		template = Handlebars.compile(source);
		
		service.query();
		
		var reload = $("#reload-projects");
		if (isTvMode()) {
			$("#reload-projects").hide();
		} else {
			$("#reload-projects").click(service.reloadPlanilhas);
		}
	};
	
	view.HeatBar = function() {
		
		var self = this;
		
		this.bar = $(".heatbar-global .heatbar");
		this.slider = $(".heatbar-global .slider");
		
		var container = (new view.Projeto()).container.find("tbody");
		var fistProject = container.find("tr:first");
		var lastProject = container.find("tr:last");
		
		var heatbarWidth = this.bar.width() + 18;
		
		// largura de cada coluna de projeto
		var projectWidth = lastProject.innerWidth();
		
		// comeco dos projetos em relacao a pagina
		var offset = container.offset().left;
		
		// largura ate o scroll
		var visibleWidth = container.innerWidth();
		
		// largura ate o final da pagina
		var totalWidth = lastProject.offset().left - fistProject.offset().left + projectWidth;
		
		// maximo que pode ser rolado sem quebrar a visao de uma coluna no meio
		var deltaScroll = Math.floor((visibleWidth / projectWidth)) * projectWidth;
		
		// ponto mais distante que pode ser rolado
		var limitScroll = totalWidth - visibleWidth;
		
		var indicadorScrollWidth = (visibleWidth / totalWidth) * heatbarWidth;
		
		var displayScrollPosition = function(scrollTo) {
			var left = (scrollTo / totalWidth) * heatbarWidth;
			self.slider.css("left", left + "px");
		};
		
		var reset = false;
		
		var nextScrollPosition = function() {
			
			//largura do scroll ate o final da pagina
			var offsetLastElement = lastProject.offset().left + projectWidth - offset;
			
			if (visibleWidth < Math.floor(offsetLastElement)) {
				var scrollTo = container.scrollLeft() + deltaScroll;
				
				if (scrollTo > limitScroll) {
					if (reset) {
						reset = false;
						return 0;
					}
					reset = true;
					return limitScroll;
				}
				
				return scrollTo;
				
			} else {
				return 0;
			}
			
		};
		
		this.autoScroll = function() {
			var scrollTo = nextScrollPosition();
			container.scrollLeft(scrollTo);
			displayScrollPosition(scrollTo);
		};
		
		this.init = function() {
			self.slider
				.css("display", "block")
				.css("width", indicadorScrollWidth + "px");
			displayScrollPosition(0);
			return self;
		};

	};
	
	view.Projeto = function() {
		
		var self = this;
		
		this.container = $("#tabela-principal");
		
		var getScroll = function() {
			return self.container.find("tbody").scrollLeft();
		};
		
		var setScroll = function(scrollTo) {
			self.container.find("tbody").scrollLeft(scrollTo);
		};
		
		this.init = function(queryWrapper, scrollTo) {
			
			self.container.html(template(queryWrapper));
			
			if (isTvMode()) {
				var heatbar = (new view.HeatBar()).init();
				setInterval(heatbar.autoScroll, 15000);
			}
			
			if (scrollTo) {
				setScroll(scrollTo);
			}
			
			self.container.find(".indicador").click(function() {
				var indexProjeto = parseInt($(this).parent().data("index"));
				var indexIndicador = parseInt($(this).data("index"));
				
				var projeto = queryWrapper.projetos[indexProjeto];
				var indicador = projeto.indicadores[indexIndicador];
				
				var dialog = new dexboard.indicador.view.Dialog();
				dialog.open(projeto, indicador);
			});
			
			if (view.Projeto.updateIndicador) {
				self.container[0].removeEventListener("update-indicador", view.Projeto.updateIndicador);
			}
			
			view.Projeto.updateIndicador = function() {
				var updatedQuery = new model.QueryWrapper(queryWrapper.projetos);
				(new view.Projeto()).init(updatedQuery, getScroll());
			};
			
			self.container[0].addEventListener("update-indicador", view.Projeto.updateIndicador);
			
			dexboard.slides.view.init(queryWrapper.projetos);
			dexboard.indicador.view.init();
			
			return self;
		}
	};
	
	return {
		"model" : model,
		"view" : view,
		"service" : service
	};
	
})(jQuery, Handlebars);
