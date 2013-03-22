(function($) {

    CarregaDados = {
            carregar : function() {
                $.getJSON('/query', function(resultado) {
                    var ulProjetos = $('<ul id="lista-projetos" />');
                    var todosProjetos = resultado.value;
                    CarregaDados.adicionaProjetos(ulProjetos, todosProjetos);
                    $("#data").append(ulProjetos);
                    CarregaDados.defineCliqueEmIndicador(todosProjetos);
                
                
                });
            },
            
            defineCliqueEmIndicador : function (todosProjetos) {
                
                 $(".opener").each(function() {
                    $(this).click(function() { 
                        
                        var split = $(this).data("idpma").split("_");
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
                        $("#dialog").dialog("open");
                        CarregaDados.populaDiv(projeto, indicador);
                    });
                });
                
            },
            
            adicionaProjetos : function (container, value) {
                var projetos = value;
                $.each(projetos, function(key, val) {
                    if (val.nome.length >= 14){
                        var nome = val.nome.substring(0,10)+"...";                        
                    }
                    else{
                        var nome = val.nome;
                    }
                    var liProjeto = $('<li id="' + val.idPma + '" />');
                    liProjeto.html('<h4 class="projeto-' + val.classificacao + '">'+nome+'</h4>');
                    container.append(liProjeto);
                    CarregaDados.adicionaIndicadores(liProjeto, val);
                });
            },
            
            adicionaIndicadores : function (container, projeto) {
                
                var ulIndicadores = $('<ul id="' + projeto.idPma + '_indicadores" class="indicadores" />');
                var indicadores = projeto.indicadores;
                
                $.each(indicadores, function(key, val) {
                    var id = "indicador_" + val.id + "_" + projeto.idPma;
                    var liIndicador = $('<li data-idpma=' + id + ' class="' + val.classificacao + ' permiteAlteracao opener" id="'+ id +'">&nbsp;</li>');
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