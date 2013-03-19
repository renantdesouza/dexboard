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
                    CarregaDados.adicionaIndicadores(liProjeto, val);
                });
            },
            
            adicionaIndicadores : function (container, projeto ){
                
                var ulIndicadores = $('<ul id="' + projeto.idPma + '_indicadores" class="indicadores" />');
                var indicadores = projeto.indicadores;
                $.each(indicadores, function(key, val) {
                    var id = "indicador_" + val.id + "_" + projeto.idPma;
                    var botao = '<input type="button" value="Trocar Indicador" id=' + id + '/>';
                    var liIndicador = $('<li id="' + id + '" class="' + val.classificacao + '">' + botao + '</li>');
                    if (val.nome == "CPI") {
                        liIndicador.html(projeto.cpi);
                    }
                    ulIndicadores.append(liIndicador);
                    
                    $("#"+id).click(CarregaDados.populaDiv(id ,projeto ,val));
                });
                container.append(ulIndicadores);
            },
            
            
            populaDiv : function (container, projeto, indicador){
                
                var botao = $('<input type="button" value="Trocar!" id="botaoDeTroca"/>');
                $("#edicaoIndicadorNomeProjeto").html(projeto.nome);
                $("#edicaoIndicadorNomeIndicador").html(indicador.nome);
                $("#botaoDeTroca").click(CarregaDados.trocaIndicador(projeto.id, indicador));
                document.getElementById("edicaoIndicador").style.display = "block";
                
            },
            
            trocaIndicador : function(idProjeto, indicador){
               
                
                
            }
               
    }
        
})(jQuery);