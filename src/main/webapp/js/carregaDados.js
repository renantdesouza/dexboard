(function($) {

    CarregaDados = {
            carregar : function() {
                $.getJSON('/query', function(resultado) {
                    var ulProjetos = $('<ul id="lista-projetos" />');
                    var todosProjetos = resultado.value;
                    CarregaDados.adicionaProjetos(ulProjetos, todosProjetos);
                    $("#data").append(ulProjetos);
                    CarregaDados.defineComportamentoDosBotoes(todosProjetos);
                
                });
            },
            
            defineComportamentoDosBotoes : function (todosProjetos) {
                
                $(".classeX").each(function() {
                    
                    $(this).click(function() { 
                        
                        var split = $(this).attr("id").split("_");
                        idIndicador = split[1]; 
                        idProjeto = split[2];
                        
                        for (var i = 0; i < todosProjetos.length; i++) {
                            var projeto = todosProjetos[i];
                            if (idProjeto == projeto.idPma) {
                                break;
                            }
                        }

                        for (i = 0; i < projeto.indicadores.length; i++) {
                            var indicador = projeto.indicadores[i];
                            if (indicador.id == idIndicador) {
                                break;
                            }
                        }
                        
                        CarregaDados.populaDiv(projeto, indicador);
                    });
                });
                
            },
            
            adicionaProjetos : function (container, value) {
                var projetos = value;
                $.each(projetos, function(key, val) {
                    var liProjeto = $('<li id="' + val.idPma + '" class="' + val.classificacao + '"/>');
                    liProjeto.html('<h4 class="projeto-ok">'+val.nome+'</h4>');
                    container.append(liProjeto);
                    CarregaDados.adicionaIndicadores(liProjeto, val);
                });
            },
            
            adicionaIndicadores : function (container, projeto) {
                
                var ulIndicadores = $('<ul id="' + projeto.idPma + '_indicadores" class="indicadores" />');
                var indicadores = projeto.indicadores;
                
                $.each(indicadores, function(key, val) {
                    var id = "indicador_" + val.id + "_" + projeto.idPma;
                    var botao = '<input type="button" value="Trocar Indicador" id=' + id + ' class="classeX"/>';
                    var liIndicador = $('<li class="' + val.classificacao + '">' + botao + '</li>');
                    if (val.nome == "CPI") {
                        liIndicador.html(projeto.cpi);
                    }
                    ulIndicadores.append(liIndicador);
                });

                container.append(ulIndicadores);
            },
            
            populaDiv : function (projeto, indicador){
                
                $("#edicaoIndicadorNomeProjeto").html(projeto.nome);
                $("#edicaoIndicadorNomeIndicador").html(indicador.nome);
                
                $("#botaoDeTroca").unbind('click');
                $("#botaoDeTroca").click(function() {
                    CarregaDados.trocaIndicador(projeto.idPma, indicador);
                });
                
                document.getElementById("edicaoIndicador").style.display = "block";
            },
            
            trocaIndicador : function(idProjeto, indicador){
  
                console.info(indicador);
                indicador.classificacao = $("#classificacaoIndicador").val();
                console.info(JSON.stringify(indicador));
                    
                $.ajax({    
                    type: "post",    
                    url: "/indicador",    
                    data: {  
                        projeto: idProjeto,  
                        indicador: JSON.stringify(indicador)  
                    },  
                  
                });  
                
            }               
    }
        
})(jQuery);