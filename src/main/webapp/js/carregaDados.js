(function($) {

	CarregaDados = {
	        carregar : function() {
	            $.getJSON('/query', function(resultado) {
	                var ulProjetos = $('<ul id="projetos" />');
	                CarregaDados.adicionaProjetos(ulProjetos, resultado.value);
	                $("#data").append(ulProjetos);
	            });
	        },
	        
	        adicionaProjetos : function (container, value) {
                var projetos = value;
                $.each(projetos, function(key, val) {
                    var liProjeto = $('<li id="' + val.idPma + '" class="' + val.classificacao + '"/>');
                    liProjeto.html(val.nome);
                    container.append(liProjeto);
                    CarregaDados.adicionaIndicadores(liProjeto, val.indicadores, val.idPma, val.cpi);
                });
            },
            
            adicionaIndicadores : function (container, value, idPma, cpi){
                var ulIndicadores = $('<ul id="' + idPma + '_indicadores" class="indicadores" />');
                var indicadores = value;
                $.each(indicadores, function(key, val) {
                    var id = "indicador_" + val.id + "_" + idPma;
                    var liIndicador = $('<li id="' + id + '" class="' + val.classificacao + '"></li>');
                    if (val.nome == "CPI") {
                        liIndicador.html(cpi);
                    }
                    ulIndicadores.append(liIndicador);
                });
                container.append(ulIndicadores);
            }
	}
    	
})(jQuery);