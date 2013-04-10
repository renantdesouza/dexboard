(function($) {

    var CarregaDados = {
            carregar : function() {
                $.getJSON('/query', function(resultado) {
                	if (resultado) {
	                    var todosProjetos = resultado;
	                    CarregaDados.carregarIndicadores(todosProjetos[0].indicadores);
	                    var ulProjetos = $('<ul id="lista-projetos" />');
	                    CarregaDados.adicionaProjetos(ulProjetos, todosProjetos);
	                    $("#data").html(ulProjetos);
	                    CarregaDados.defineCliqueEmIndicador(todosProjetos);
                	}
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
						$("#dialog").data("opened", true);
						$('#dialog-overlay').fadeIn('fast');
                        CarregaDados.populaDiv(projeto, indicador);

						$("#dialog").find('textarea').focus();

						$('#dialog-overlay').bind('click', function()
						{
							$("#dialog").dialog("close");
							$("#dialog-overlay").fadeOut('fast', function()
							{
								$(this).data("opened", false);
							});
							$(this).unbind('click');
							$(this).unbind('keydown');
						});
						$(document).keydown(function(e)
						{
							if ( e.which === 27 )
							{
								$('#dialog-overlay').click();
							}
						});
						$('.dialog-close-button').on('click', function(e)
						{
							e.preventDefault();
							$('#dialog-overlay').click();
							$(this).off('click');
						});


                    });
                });

            },

            adicionaProjetos : function (container, value) {
                var projetos = value;
                $.each(projetos, function(key, val) {
                    var nome = val.nome;
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
            	var ulHistorico = $("#historico");
            	ulHistorico.html('');
            	var registros = indicador.registros;
                if (registros.length > 0) {
                    $.each(registros, function(idx, obj) {
                		ulHistorico.append('<li class="historico-' + obj.classificacao + '">Alterado em <b>' +
                				obj.ultimaAlteracaoString + '</b> por <b>' + obj.usuario + '</b><br/>' + obj.comentario + '</li>');
                    });

                    $("#edicaoIndicadorUltimaAlteracaoDesc").html(indicador.descricao);
                	$("#edicaoIndicadorUltimaAlteracao").html(indicador.usuarioUltimaAlteracao + " em " + indicador.ultimaAlteracaoString);
                	$('#fieldsetUltimaAlteracao').show();
                } else {
                	$('#fieldsetUltimaAlteracao').hide();
                }

                $("#edicaoIndicadorTxtDescricao").val("");

                $.each($('#divOptions input'), function(idx, obj) {
            		$(obj).unbind('click');
                });
                $.each($('#divOptions input'), function(idx, obj) {
                	$(obj).click(function() {
                		CarregaDados.trocaIndicador(projeto.idPma, indicador, obj.name);
                	});
                });

            },

            trocaIndicador : function(idProjeto, indicador, classsificacao){

            	var registro = {
            			classificacao : classsificacao,
            			comentario : $("#edicaoIndicadorTxtDescricao").val()
            	};

                $.ajax({
                    type: "POST",
                    url: "/indicador",
                    data: {
                        projeto: idProjeto,
                        indicador: indicador.id,
                        registro: JSON.stringify(registro)
                    },
                    complete : function() {
                        CarregaDados.carregar();
                    }
                });
				$('#dialog-overlay').fadeOut('fast',function(){
					$("#dialog").dialog("close");
					$("#dialog").data("opened", false);
				});
            },

            carregarIndicadores : function (indicadores) {
                var ul = $("#menuIndicadores");
                ul.html('');
                $.each(indicadores, function(key, val){
                   var li = '<li>' + val.nome +'</li>';
                   ul.append(li);

                });
            }
    }

    $(window).ready(function() {
        $("#dialog").dialog({
            autoOpen : false,
            show : {
                effect : "fade",
                duration : 400
            },
            hide : {
                effect : "fade",
                duration : 400
            },
            width : 1000,
            height : 425
        });


        CarregaDados.carregar();

    });

})(jQuery);