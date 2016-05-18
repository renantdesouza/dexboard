(function($) {

    $(document).ready(function(){
        //$('.container-right').jScrollPane({ showArrows: true });

    });

    var CarregaDados = {

            getURLParameter : function(name) {
                return decodeURIComponent((new RegExp('[?|&]' + name + '='
                        + '([^&;]+?)(&|#|;|$)').exec(location.search) || [ , "" ])[1]
                        .replace(/\+/g, '%20'))
                        || null;
            },

            carregar : function() {

                var path = '/query';

                var equipe = CarregaDados.getURLParameter("equipe");
                if(equipe) {
                    path += '?equipe='+equipe;
                }

                $.getJSON(path, function(resultado) {
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
                        $(document).keydown(function(e) {

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
                    liProjeto.html('<h4 class="' + CarregaDados.getClassProjeto(val) + '">'+nome+'</h4>');
                    container.append(liProjeto);
                    CarregaDados.adicionaIndicadores(liProjeto, val);
                });
            },

            getClassProjeto : function(projeto) {
                var classProjeto =  'projeto-' + projeto.classificacao;
                if(projeto.atrasado) {
                    classProjeto += ' projeto-DESATUALIZADO';
                }
                return classProjeto;
            },

            getClassIndicador : function(indicador) {
                var classIndicador =  indicador.classificacao;
                if(indicador.atrasado) {
                    classIndicador += ' DESATUALIZADO';
                }
                return classIndicador;
            },

            adicionaIndicadores : function (container, projeto) {

                var ulIndicadores = $('<ul id="' + projeto.idPma + '_indicadores" class="indicadores" />');
                var indicadores = projeto.indicadores;

                $.each(indicadores, function(key, val) {
                    var id = "indicador_" + val.id + "_" + projeto.idPma;
                    var liIndicador = $('<li data-idpma=' + id + ' class="' + CarregaDados.getClassIndicador(val) + ' permiteAlteracao opener" id="'+ id +'">&nbsp;</li>');
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

                var  explicacao ="";

                if( indicador.nome == "CPI" ){

                         explicacao ="CPI : lorem orbi nec nibh lacus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut gravida nunc sed condimentum tempus. Duis hendrerit arcu tristique porttitor cursus. Nunc suscipit diam vitae est malesuada, id vestibulum sem imperdiet. Nunc nec aliquam mauris, vel rutrum magna. Ut feugiat sollicitudin odio. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer et viverra diam. ";

                }else if(indicador.nome =="Foco em entrega do valor" ){

                     explicacao ="Foco em entrega do valor : lorem orbi nec nibh lacus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut gravida nunc sed condimentum tempus. Duis hendrerit arcu tristique porttitor cursus. Nunc suscipit diam vitae est malesuada, id vestibulum sem imperdiet. Nunc nec aliquam mauris, vel rutrum magna. Ut feugiat sollicitudin odio. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer et viverra diam.";

                }else if(indicador.nome =="Qualidade do c\u00F3digo" )  {

                     explicacao = "Qualidade do c\u00F3digo : lorem orbi nec nibh lacus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut gravida nunc sed condimentum tempus. Duis hendrerit arcu tristique porttitor cursus. Nunc suscipit diam vitae est malesuada, id vestibulum sem imperdiet. Nunc nec aliquam mauris, vel rutrum magna. Ut feugiat sollicitudin odio. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer et viverra diam.    ";

                }else if(indicador.nome =="Qualidade funcional" ){

                     explicacao = "Qualidade funcional : lorem orbi nec nibh lacus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut gravida nunc sed condimentum tempus. Duis hendrerit arcu tristique porttitor cursus. Nunc suscipit diam vitae est malesuada, id vestibulum sem imperdiet. Nunc nec aliquam mauris, vel rutrum magna. Ut feugiat sollicitudin odio. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer et viverra diam.";

                }else if (indicador.nome =="Satisfa\u00E7\u00E3o da equipe" ){

                     explicacao = "Satisfa\u00E7\u00E3o da equipe : lorem orbi nec nibh lacus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut gravida nunc sed condimentum tempus. Duis hendrerit arcu tristique porttitor cursus. Nunc suscipit diam vitae est malesuada, id vestibulum sem imperdiet. Nunc nec aliquam mauris, vel rutrum magna. Ut feugiat sollicitudin odio. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer et viverra diam. ";
                } else if (indicador.nome =="Satisfa\u00E7\u00E3o do cliente" ){

                    explicacao = "Satisfa\u00E7\u00E3o do cliente : lorem orbi nec nibh lacus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut gravida nunc sed condimentum tempus. Duis hendrerit arcu tristique porttitor cursus. Nunc suscipit diam vitae est malesuada, id vestibulum sem imperdiet. Nunc nec aliquam mauris, vel rutrum magna. Ut feugiat sollicitudin odio. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer et viverra diam. ";
                }

                $("#explicacao-indicador").html( explicacao );


                // carregarExplicacaoIndicador(indicador.nome);


                var ulHistorico = $("#historico");
                ulHistorico.html('');

                var registros = indicador.registros;
                if (registros.length > 0) {
                    $.each(registros, function(idx, obj) {
                        ulHistorico.append('<li class="historico-' + obj.classificacao + '" data-classificacao="' + obj.classificacao + '">Alterado em <b>' +
                                obj.ultimaAlteracaoString + '</b> por <b>' + obj.usuario + '</b><br/><span class="comentario">' + obj.comentario + '</span></li>');
                    });

                    $("#edicaoIndicadorUltimaAlteracaoDesc").html(indicador.descricao);
                    $("#edicaoIndicadorUltimaAlteracao").html(indicador.usuarioUltimaAlteracao + " em " + indicador.ultimaAlteracaoString);
                    $('#fieldsetUltimaAlteracao').show();
                } else {
                    $('#fieldsetUltimaAlteracao').hide();
                }

                $("#edicaoIndicadorTxtDescricao").val("");

                $('#divOptions input').each(function() {
                    if( ! $(this).hasClass('btn-sem-mudancas') ){
                        $(this).attr('disabled', 'disabled');
                    } else {
                        if ($('#historico').children().length == 0) {
                            $(this).attr('disabled', 'disabled');
                        } else {
                            $(this).removeAttr('disabled');
                        }
                    }

                    $(this).unbind('click');
                });

                $.each($('#divOptions input'), function(idx, obj) {
                    $(obj).click(function() {
                        if(!$(obj).hasClass('btn-sem-mudancas') ){
                            CarregaDados.trocaIndicador(projeto.idPma, indicador, obj.name);
                        } else {
                            $('#edicaoIndicadorTxtDescricao').val($('#historico li:first-child span.comentario').text());
                            var name = $('#historico li:first-child').data('classificacao');
                            CarregaDados.trocaIndicador(projeto.idPma, indicador, name);
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

            carregarIndicadores : function(todosProjetos) {

                var temProjetos = todosProjetos !== null && todosProjetos.length > 0;
                var indicadoresDoMenu = temProjetos ? todosProjetos[0].indicadores : [];
                var ul = $("#menuIndicadores");
                ul.html('');

                $.each(indicadoresDoMenu, function(key, val){
                   var li = '<li>' + val.nome;
                          li += '<div id="'  + val.nome + '"class="heatbar-indicadores">';
                          li += '<table><tr>';

                          var classificacaoPERIGO = [];
                          var classificacaoATENCAO = [];
                          var classificacaoOK = [];

                          var classificacaoPERIGOATRASADO = [];
                       var classificacaoATENCAOATRASADO = [];
                       var classificacaoOKATRASADO = [];

                          var classificacaoOrdenada = [];


                   for (var i = 0; i < todosProjetos.length; i++) {
                       for (var j = 0; j < todosProjetos[i].indicadores.length; j++) {

                           var indicadoresDeProjeto = todosProjetos[i].indicadores;

                           if (indicadoresDeProjeto[j].nome == val.nome) {

                               var classificacao = indicadoresDeProjeto[j].classificacao;
                               var atrasado = indicadoresDeProjeto[j].atrasado;

                               if (classificacao == "PERIGO") {
                                   if(atrasado ){
                                       classificacaoPERIGOATRASADO.push(classificacao+"atrasado");
                                   }else {
                                       classificacaoPERIGO.push(classificacao);
                                   }

                               } else if ( classificacao == "ATENCAO" ) {
                                   if(atrasado ){
                                       classificacaoATENCAOATRASADO.push(classificacao+"atrasado");
                                   }else{
                                       classificacaoATENCAO.push(classificacao);
                                   }
                               } else {
                                   if(atrasado ){
                                       classificacaoOKATRASADO.push(classificacao+"atrasado");
                                   }else{
                                       classificacaoOK.push(classificacao);
                                   }
                               }
                            }
                        }
                   }

                    classificacaoOrdenada =  classificacaoPERIGOATRASADO.concat(classificacaoATENCAOATRASADO).concat(classificacaoOKATRASADO).concat(classificacaoPERIGO).concat(classificacaoATENCAO).concat(classificacaoOK);

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
                      var desatualizado = "";
                      if(todosProjetos[i].atrasado){
                          var desatualizado = "heatbar-DESATUALIZADO";
                      }
                      contentHeatBar += '<th class="projeto-'+todosProjetos[i].classificacao+' '+ desatualizado +'">&nbsp;</th>';
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