(function($) {

	$(document).ready(function(){
		//$('.container-right').jScrollPane({ showArrows: true });		
		
	});	
	
    var CarregaDados = {
    		
            carregar : function() {
                $.getJSON('/query', function(resultado) {
                	if (resultado) {
	                    var todosProjetos = resultado;
	                    CarregaDados.carregarIndicadores(todosProjetos);
	                    var ulProjetos = $('<ul id="lista-projetos" />');
	                    CarregaDados.adicionaProjetos(ulProjetos, todosProjetos);
	                    $("#data").html(ulProjetos);
	                    CarregaDados.defineCliqueEmIndicador(todosProjetos);
	                    habilitarBotoesJustificativas();
	                    heatbarStart();
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
						$('#dialog-overlay').show();
						
                        CarregaDados.populaDiv(projeto, indicador);

						$("#dialog").find('textarea').focus();							
						

						$('#dialog-overlay').bind('click', function(){
							
							$("#dialog").dialog("close");
							
							$("#dialog-overlay").hide(null, function(){
								$(this).data("opened", false);
							});
							
							$(this).unbind('click');
							$(this).unbind('keydown');
						});
						$(document).keydown(function(e)	{
							
							if ( e.which === 27 ){
								$('#dialog-overlay').click();
							}
						});
						
						$('.dialog-close-button').on('click', function(e){
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
                	if( ! $(obj).hasClass('btn-sem-mudancas') ){
                		$(obj).attr('disabled', 'disabled');
            			$(obj).unbind('click');
                	}
                });
                
                $.each($('#divOptions input'), function(idx, obj) {    	
                	$(obj).click(function() { 
                		if(!$(obj).hasClass('btn-sem-mudancas') ){                		
                			CarregaDados.trocaIndicador(projeto.idPma, indicador, obj.name);
	                	} else {
	                		CarregaDados.mantemIndicador(projeto.idPma, indicador, obj.name);
	                	}
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

            mantemIndicador : function(idProjeto, indicador, classsificacao) {
            	alert("OI");
            },
            
            carregarIndicadores : function (todosProjetos) {

            	var indicadoresDoMenu = todosProjetos[0].indicadores;          	
                var ul = $("#menuIndicadores");
                ul.html('');                                         
                                 
    			$.each(indicadoresDoMenu, function(key, val){
                   var li = '<li>' + val.nome;
                   	   li += '<div id="'  + val.nome + '"class="heatbar-indicadores">';
                   	   li += '<table><tr>';                   	

                   	   var classificacaoPERIGO = [];
                   	   var classificacaoATENCAO = [];
                   	   var classificacaoOK = [];
                   	   var classificacaoOrdenada = [];
                   	   
                   for (var i = 0; i < todosProjetos.length; i++) {                    	                  	   
                	   for (var j = 0; j < todosProjetos[i].indicadores.length; j++) {                		   
                		   var indicadoresDeProjeto = todosProjetos[i].indicadores;                  		   
                           if (indicadoresDeProjeto[j].nome == val.nome) { 
                        	   var classificacao = indicadoresDeProjeto[j].classificacao;                			   
							   if (classificacao == "PERIGO") {
								   classificacaoPERIGO.push(classificacao);
							   } else if ( classificacao == "ATENCAO" ) {
								   classificacaoATENCAO.push(classificacao);
							   } else {
								   classificacaoOK.push(classificacao);                        		   
							   }
                            }
                        }
                   }
                   
             	   classificacaoOrdenada = classificacaoPERIGO.concat(classificacaoATENCAO).concat(classificacaoOK);
             	  
             	  for( var w = 0; w < classificacaoOrdenada.length; w++){
             		  li += '<th class="heatbar-indicadores-'+ classificacaoOrdenada[w] +'">&nbsp;</th>';
             	  }
             	   
             	  li += '</tr></table> ';
                  li += '</div>';
                  li += '</li>';
                  ul.append(li);
                  
                  // Carregar heatbar de todos os projetos
             	  var heatbar = $("#heatbar");
                  heatbar.html('');
                  var contentHeatBar = '<div id="heatbar-slider" class="heatbar-slider"></div> <table><tr>';
                  
                  for (var i = 0; i < todosProjetos.length; i++) {    
               	   contentHeatBar += '<th class="projeto-'+todosProjetos[i].classificacao+'">&nbsp;</th>';
                  }
                  contentHeatBar +='</tr></table>';
                  heatbar.append( contentHeatBar);  
                });                
            }           
    }
    
     function habilitarBotoesJustificativas () {    	
    	$("#edicaoIndicadorTxtDescricao").keyup(function(){
			  $('#divOptions input').removeAttr("disabled");
			  if ( $(this).val() === '' ){
				  $('#divOptions input').attr('disabled', 'disabled');
			  }else{
				  $('#divOptions .btn-sem-mudancas').attr('disabled', 'disabled');
			  }
		});
    }
    
    function heatbarStart(){    	
		var numberTotalProjects = $('#lista-projetos > li').length;
		var numberShowProjects = 12;		
		var showpages =  numberTotalProjects / numberShowProjects;		
		var barWidth = ( 100 / showpages ) ;  		
		
		$('#heatbar-slider').width( barWidth + '%');
	};

    $(window).ready(function() {
        $("#dialog").dialog({
            autoOpen : false,
            width : 1000
     });

        CarregaDados.carregar();
    });

})(jQuery);