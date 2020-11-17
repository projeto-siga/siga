$(document).on('ready', function(){

	var gsa = {
		settings: {
			proxy: "/siga/app/buscargsa",
			params:{
				site: "siga",
				client: "json",//"json_",
				proxystylesheet: "json",//xml_init",
				oe: "UTF-8",
				ie: "UTF-8",
				getfields: "*",
				filter: 0,
				output: "xml_no_dtd",
				lr:"lang_pt",
				sort: "D:L:d1",
				access: "a"
			}
		},
		RenderInit:function(json){
			var self = this;

			if(Object.keys(json).length > 0){
				self.dynamicNavigation.render(json);
				self.result.render(json);
			}else{
				console.log("Erro ao retornar resultados");
			}
		},
		//Função responsavel para trazer resultados
		result:{
			render:function(json){
				var self = this;
				var listhtml = [];

				var RES = json.GSP.hasOwnProperty("RES")?json.GSP.RES:{"R":[]};

				if(RES.R.length > 0){
					
					var htmlFirstLine = '<div class="row gsa-info">';
					htmlFirstLine += '<div class="col-md-7">' + self.search_info(json) + '</div>';
					htmlFirstLine += '<div class="col-md-5">' + self.sortBy(json.GSP.PARAM) +'</div>';
					htmlFirstLine += '</div>';
					
					listhtml.push(htmlFirstLine);
					
					for (var i = 0; i < RES.R.length; i++) {
						var MT = {};
						if( Object.keys(RES.R[i].MT).length > 0) {
							MT = RES.R[i].MT;

							var t = [];
							
							//t.push(MT.modelo?MT.modelo:"");
							t.push(MT.codigo?MT.codigo:"");
							t.push(MT.data?MT.data.replace(/(\d{4})-(\d{2})-(\d{2})/,"$3/$2/$1"):"");
							//t.push(RES.R[i].T);
							t.push(MT.subscritor?MT.subscritor:"");
							t.push(MT.subscritor?" ("+MT.subscritor_lotacao+")":"");
							t.push(MT.descricao?"- "+MT.descricao:"");

							MT.desc_titulo = t.join(" ");
						}
						else{
							MT.desc_titulo = RES.R[i].T;
						}
						

						var rowHtml ='<div class="panel panel-default">';
						// Titulo
						rowHtml += '<div class="panel-heading gsa-title"> <div class="row">';
						rowHtml += '<div class="col-md-10"><a class="gsa-permalink" href="'+RES.R[i].U+'">'+MT.desc_titulo+'</a></div>';
						//link cache
						rowHtml += '<div class="col-md-2"><a class="gsa-cache pull-right" target="_blank" href="'+self.cacheLink(RES.R[i],json.GSP.PARAM, gsa.settings.proxy)+'">'+RES.R[i].HAS.C.SZ+' Cache</a></div>';
						rowHtml += '</div></div>';

						//snippet
						rowHtml += '<div class="panel-body text-left">';
						rowHtml += '<span>'+RES.R[i].S+'</span>';

						// Metadados
//						if(Object.keys(RES.R[i].MT).length > 0){
//							rowHtml += '<p/><p>';
//							rowHtml += '<li><b>'+ Descrição +': </b>'+ MT.descricao +'</li>';
//							if( Object.keys(RES.R[i].MT).length > 0) {
//								for (var x in RES.R[i].MT) {
//									if(/descricao/.test(x)){
//										rowHtml += '<b>Descrição: </b>'+ RES.R[i].MT[x];
//									}
//								}
//							}
//							rowHtml += '</p>';
//						}

						//link cache
						// rowHtml += '<p>'+ RES.R[i].U + ' - ' + RES.R[i].HAS.C.SZ + ' - ' + RES.R[i].CRAWLDATE + ' - ';
						// rowHtml += '<a target="_blank" href="'+self.cacheLink(RES.R[i],json.GSP.PARAM, gsa.settings.path)+'"> Cache</a>';
						// rowHtml += '</p>';

						rowHtml += '</div></div>'

						listhtml.push(rowHtml);
					}

					var navHtml = '<div class="row">';

					var NB = RES.hasOwnProperty("NB")?RES.NB:{"NB":{}}
					var next = NB.hasOwnProperty("NU")?RES.NB.NU.replace(/\/search\?/,''):false;
					var prev = NB.hasOwnProperty("PU")?RES.NB.PU.replace(/\/search\?/,''):false;

					navHtml += self.navigation.render(json.GSP.PARAM, prev, next, RES.SN, RES.EN, RES.M, 'botton');
					navHtml += '</div>';
					listhtml.push(navHtml);
				}else{
					var rowHtml = '<div class="panel panel-default">';
					rowHtml += '<div class="panel-body">';
					rowHtml +=	self.no_results(json.GSP.PARAM);
					rowHtml += '</div></div>';

					listhtml.push(rowHtml);
				}
				

				// Adiciona conteúdo processado ao container de resultados
				document.getElementById("main").innerHTML = listhtml.join(" ");

				//self.navigation.eventClickPavigation();
				gsa.div_loader.hidePleaseWait();
			},
			sortBy:function(PARAM){
			    
			    var url = [];
			    var dnavs = {origen:'',value:''};
			    var q_origen = '';
			    
			    for (var x in PARAM) {
			        if(PARAM[x].name != "q" && !/dnavs|sort|start|epoch|epoch|ip|ip|metabased_/g.test(PARAM[x].name) ){
			            url.push(PARAM[x].name+"="+PARAM[x].value);
			        }

			        if(/dnavs/g.test(PARAM[x].name) ){
			        	dnavs.origen = PARAM[x].value;
			        	dnavs.value = decodeURI(dnavs.origen).replace(/\s|%20/g,'%2520').replace(/%2520inmeta:/g, ' inmeta:');
			        	url.push(PARAM[x].name+"="+ dnavs.value);
			        } 
			        if(PARAM[x].name == "q" ){
			        	q_origen = PARAM[x].value;
			        }  
			    }

			    q_origen = q_origen.replace(dnavs.origen,dnavs.value);
			    url.push("q="+ q_origen);

			    var location = window.location.href.split('?')[0];
			    url = location + "?" + url.join('&');

			    var sort_by_relevance_url = url + "&sort=date:D:L:d1";
			    var sort_by_date_url = url + "&sort=date:D:S:d1";
			    var sort_by_dataCrescente = url + "&sort=date:A:S:d1";

			    var html = "";
			    html += '<div class="info-sortBy pull-right">';

			    var sort = gsa.findParamValue('sort',PARAM);
			    if(/date:D:S/.test(sort)){
			        html += '<span class="font-size-12">Classificar por data / </span>';
			        html += '<a href="'+ sort_by_relevance_url +'" class="featured">Classificar por relevância</a>';
			    }else{
			        html += '<a href="'+ sort_by_date_url +'" class="featured">Classificar por data</a>';
			        html += '<span class="font-size-12"> / Classificar por relevância</span>';
			    }
			                
			    html += '</div>';

			    return html;

			},
			search_info:function(json){

			    var time = json.GSP.hasOwnProperty("TM")?json.GSP.TM:0;
			    
			    var html = '<div class="clearfix pull-left">';

			    var RES =  json.GSP.hasOwnProperty("RES")?json.GSP.RES:{R:[],SN:"",EN:""};
			       
			    if(RES.R.length > 0){
			    	var q = gsa.findParamValue('q',json.PARAM);
			    	var as_epq = gsa.findParamValue('as_epq',json.PARAM);
			    	var access = gsa.findParamValue('access',json.PARAM);
			    	var dnavs = gsa.findParamValue('dnavs',json.PARAM);
			    	var query_q = dnavs.length >0? q.replace(dnavs,'').trim():q;
			        if(/s|a/.test(access)){
			            html += 'Resultados ';
			            html += '<span class="bold">';
			            html += RES.SN;
			            html += '</span> - ';
			            html += '<span class="bold">';
			            html += RES.EN;
			            html += '</span>';
			            
			            if(q.length > 0){
			                html += ' para ';
			                html += '<span class="bold">';
			                html += q;
			                html += '</span>';
			            }else{
			                if(as_epq.length > 0){
			                    html += ' para ';
			                    html += '<span class="bold">';
			                    html += as_epq;
			                    html += '</span>';
			                }
			            }
			            html += '. ';
			        }else{
			            html += 'Resultados ';
			            html += '<span class="bold">';
			            html += RES.SN;
			            html += '</span> -';
			            html += '<span class="bold">';
			            html += RES.EN;
			            html += '</span>';
			            html += ' de aproximadamente ';
			            html += '<span class="bold">';
			            html += RES.M;
			            html += '</span>';
			            if(q.length > 0){
			                html += ' para';
			                html += '<span class="bold">';
			                html += query_q;
			                html += '</span> ';
			                   
			            }else{
			                if(as_epq.length > 0){
			                    html += ' para ';
			                    html += '<span class="bold">';
			                    html += as_epq;
			                    html += '</span> ';
			                }
			            }
			            html += '. ';
			        }
			    }
			    html += 'A pesquisa demorou ';
			    html += '<span class="bold">';
			    html += Math.round(time * 100.0) / 100.0;
			    html += '</span> ';
			    html += ' segundos.';
			    html += '</div>';

			    return html;

			},
			cacheLink:function(R, PARAM, gsaPath){
			    var escaped_url = R.U;

			    var docid = R.hasOwnProperty("CID")? R.CID: R.HAS.C.CID;
			    var cache_encoding = R.hasOwnProperty("ENC")? R.ENC:"UTF-8";
			    
			    var stripped_search_query = "";
			    var base_url = "";
			    for (var x in PARAM) {
			    	var value = PARAM[x].name + "=" + PARAM[x].value;
			    	if( /q|as_q|as_oq|as_epq/.test(PARAM[x].name) ){
			    		stripped_search_query += stripped_search_query.length > 0? "+"+value:value;
			    	}
			    	if( /client|site|num|output|access|lr|ie|oe/.test(PARAM[x].name) ){
			    		base_url += base_url.length > 0? "&" + value:value;
			    	}
				}

			   	var url = gsaPath;
			   	url += '?q=cache:';
			   	url += docid;
			   	url += ":";
			   	url += docid;
			   	url += escaped_url;
			   	url += '+';
			   	url += stripped_search_query;
			   	url += "&";
			   	url += base_url;
			   	url += "&proxystylesheet=default_frontend";

				return url;
			},
			//<!-- **********************************************************************
             //       Template Quando não tem resultados
        	//********************************************************************** -->
	        no_results:function(PARAM){
	        	var dnavs = gsa.findParamValue('dnavs',PARAM);
	        	var q = gsa.findParamValue('q',PARAM);
	        	var query_q = dnavs.length >0 ? q.replace(dnavs,'').trim():q;
	            var rowHtml = '<div id="gsa-no-results"><p>';
	                rowHtml += 'Sua pesquisa';
	                if(query_q.length > 0){
	                   
	                    rowHtml += '<b> - '+ query_q +' - </b>'; 
	                }else{
	                    rowHtml += '- <b> '+ query_q.trim() +' </b> -'; 
	                }

	                rowHtml += 'não encontrou nenhum documento correspondente.';
	                rowHtml += '</p><p>';
	                rowHtml += 'Nenhuma página';
	                    if(query_q.length > 0){
	                        rowHtml += ' contendo <b>';
	                        rowHtml += '"' + query_q + '"';
	                        rowHtml += '</b>';
	                    }else{
	                        rowHtml += ' contendo <b>"'+ query_q.trim() +'"</b>';
	                    }

	                rowHtml += ' foi encontrada.';
	                rowHtml += '</p>';
	                rowHtml += '<h4>Sugestões:</h4>';
	                rowHtml += '<ul>';
	                    rowHtml += '<li>';
	                        rowHtml += '<p>Certifique-se de que todas as palavras estejam escritas corretamente.</p>';
	                    rowHtml += '</li>';
	                    rowHtml += '<li>';
	                        rowHtml += '<p>Tente palavras-chave diferentes.</p>';
	                    rowHtml += '</li>';
	                    var access = gsa.findParamValue('access',PARAM);
	                    access = access?access:'';
	                    if(/a/.test(access)){
	                        rowHtml += '<li>';
	                            rowHtml += '<p>Verifique se as suas credenciais de segurança estão corretas.</p>';
	                        rowHtml += '</li>';
	                    }
	                    rowHtml += '<li>';
	                        rowHtml += '<p>Tente palavras-chave mais genéricas.</p>';
	                    rowHtml += '</li>';
	                rowHtml += '</ul>';
	            rowHtml += '</div>';

	            return rowHtml;
        	},
			navigation:{
				render:function(PARAM,prev,next,view_begin,view_end,guess,navigation_style){
					var self = this;
    				var html = "";

    				var num_results = gsa.findParamValue('num',PARAM);
    				num_results = num_results?num_results:10;
 
    				var location = window.location.href.split('?')[0];
    				var search_url_navigation = location + "?" + gsa.search_url_navigation(PARAM);
				    //<!-- *** Test to see if we should even show navigation *** -->
				    if(prev || next){

				        if(navigation_style == 'botton'){
				            html += '<nav id="gsa-pagination" ><ul class="pagination">';
				        }else{
				            html += '<div class="pagination-inner-wrapper">';
				        }
				        	
				        //<!-- *** Show firts navigation, if available *** --> 
				        if(prev){
				        	if(navigation_style == 'botton'){
				                html += '<li>';
				            }else{
				                html += '<span class="text-right">';
				            }

				            if(navigation_style == 'botton'){
				                html += '<a href="'+ search_url_navigation+'" aria-label="Previous">';
				                html += '<span aria-hidden="true"> &lt; &lt; </span>';
				                html += '</a>';
				            }

				            if(navigation_style == 'botton'){
				                html += '</li>';
				            }else{
				                html += '</span>';
				            }
				        }

				        //<!-- *** Show previous navigation, if available *** -->
				        if(prev){
				            if(navigation_style == 'botton'){
				                html += '<li>';
				            }else{
				                html += '<span class="text-right">';
				            }

				            if(navigation_style == 'botton'){

				                html += '<a href="'+ search_url_navigation+'&start='+ (view_begin - num_results - 1) +'" aria-label="Previous">';
				                html += '<span aria-hidden="true">&lt;</span>';
				                html += '</a>';
				            }else{
				                html += '<a href="'+ search_url_navigation+'&start='+(view_begin - num_results - 1)+'" class="pagination-link">';

				                if(navigation_style != 'botton'){
				                    html += '&lt;';
				                }else{
				                    html += 'Anterior';
				                }

				                html += '</a>';

				            }
				        	
				            if(navigation_style == 'botton'){
				                html += '</li>';
				            }else{
				                html += '</span>';
				            }
				        	
				        }

				        if( navigation_style == 'botton'){
				            
				            var mod_end = next?guess : view_end;

				            html += self.result_nav(0, mod_end, view_begin - 1, navigation_style, search_url_navigation, num_results);
				        }

				         //<!-- *** Show next navigation, if available *** -->
				         if(next){

				            if(navigation_style == 'botton'){
				                html += '<li>';
				        	}else{
				                html += '<span>';
				            }

				            if(navigation_style == 'botton'){
				                html += ' <a href="' + search_url_navigation + '&start='+ (view_begin + num_results - 1)+'" aria-label="Next">';
				                html += '<span aria-hidden="true">&gt;</span>';
								html += '</a>';
					        }else{

				                if(navigation_style != 'botton'){
				                    html += '<span class="pagination-link featured right-0"> | </span>';
				                }
				                
				                html += '<a href="' + search_url_navigation + '&start='+ (view_begin + num_results - 1)+'" class="pagination-link featured">';

				                html += 'Próximo';
						        if(navigation_style != 'botton'){
				                    html += '&gt;';
				                }
						         html += '</a>';
					        		
					        }

				            if(navigation_style == 'botton'){
				                html += '</li>';
				            }else{
				                html += '</span>';
				            }
				        }

				        //<!-- *** Show last navigation, if available *** -->
				        if(next){
				            if( navigation_style == 'botton'){
				                html += '<li>';
				            }else{
				                html += '<span class="text-right">';
				            }

					        defineLastPage = guess
					        while (defineLastPage % 10 != 0) {
							    defineLastPage--;
							}

				            var last_navigation = guess < 990? 990: defineLastPage;

				            if(navigation_style = 'botton'){
				                html += '<a href="' + search_url_navigation + '&start='+ last_navigation +'" aria-label="Next">';
				                html += '<span aria-hidden="true">&gt; &gt;</span>';
				                html += '</a>';
				            }

				            if(navigation_style == 'botton'){
				                html += '</li>';
				            }else{
				                html += '</span>';
				            }
				        }

				        //<!-- *** End Google result bar *** -->
				        if(navigation_style == 'botton'){
				            html += '</ul></nav>';
				        }else{
				            html += '</div>';
				        } 	
				    }

				    return html;
				},
				result_nav:function(start,end,current_view,navigation_style,search_url_navigation,num_results){
					var self = this;
				    var html = '';
				    //<!-- *** Choose how to show this result set *** -->
				    if( (current_view >= start) && (current_view < (start + num_results) ) ){
				        if(navigation_style == 'botton'){
				            html += '<li class="active">';
				            html += '<a href="#">';
				            html += (start / num_results )+1;
				            html += '<span class="sr-only">(current)</span>';
				            html += '</a></li>';
				        }else{
				            html += '<span class="pagination-control">';
				            html += '<span class="pagination-active">';
				            html += ($start / num_results) + 1;
				            html += '</span></span>';
				        }
				        
				    }else{
				        if(navigation_style == 'botton'){
				            if( (start / num_results) + 1 < 101){
				                html += '<li>';
				                html += '<a href="' + search_url_navigation + '&start='+start+'">';
				                html += (start / num_results) + 1;
				                html += '</a></li>';
				            }
				        }else{
				            if((start / num_results) + 1 < 101){
				                html += '<a ctype="nav.page" href="' + search_url_navigation + '&start='+start+'" class="pagination-link">';
				                html += (start / num_results) + 1;
				                html += '</a>';
				            }
				        }

				    }

				    //<!-- *** Recursively iterate through result sets to display *** -->
				    if( ((start + num_results) < end) && (start + num_results) < (current_view + 4 * num_results)){

				        html += self.result_nav(start+num_results,end,current_view,navigation_style,search_url_navigation,num_results);

				    }

				    return html;

				}
			}

		},
		dynamicNavigation:{
			render:function(json){
				var self = gsa;

				var PMT = json.GSP.hasOwnProperty("RES")?json.GSP.RES.hasOwnProperty("PARM")?json.GSP.RES.PARM.hasOwnProperty("PMT")?json.GSP.RES.PARM.PMT:{}:{}:{};
				var DNAVS = self.findParamValue("dnavs",json.GSP.PARAM);
				DNAVS = DNAVS.length > 0 ? decodeURI(DNAVS):"";
				
				// variavel para guardar os nomes, onde irá verificar a hierarquia
				var name_pmt_nm = [];
				var html = "";
				var inmetaHierarq = {};

				for (var i = 0; i < PMT.length; i++) {
					//variável inserida para verificar a exibição do próximo nivel de hierarquia
					PMT[i].visible = false;

					//variável para verificar a existencia no item anterior o parametro 'visible'
					var PMT_prev_Visible = i > 0?PMT[(i-1)].visible:false;

					// lista que contém na posição 1 o valor anterior do separador e na posição 2 o valor posterior ao separador 
					var pmt_name = self.matchLastWordSeparator(PMT[i].NM, "_");

					// teste para verificar se for de uma hierarquia para poder processar o item
					var teste_anterior = i > 0 && self.matchLastWordSeparator(PMT[(i-1)].NM, "_")[0] == pmt_name[0] && PMT_prev_Visible

					// Teste para permitir o processamento do item, verifica a exitencia do nome do metadado com o conteudo anteriorao separador 'pmt_name' na lista 'name_pmt_nm'
					if(name_pmt_nm.indexOf(pmt_name[0]) == -1 || teste_anterior){

						//adiciona em uma lista o nome sem separador para proxima verificação de hierarquia e exibição
						name_pmt_nm.push(pmt_name[0]);

						//montagem dos container para navegação dinamica
						var container = '<div class="gsa-dn-container text-left">';
						container += '<label>'+ PMT[i].DN +'</label>';
						container += '<div class="list-group">';

						var isElemtActive = false;
						for (var e = 0; e < PMT[i].PV.length; e++) {

							var value_interface = PMT[i].PV[e].V;

							// teste para processar item não filtrados e somente itens com resultados 
							if(!isElemtActive && PMT[i].PV[e].C != '0'){
								//verificação para monta a url de inmeta
								var inmeta = "";
								//teste para data
								if(PMT[i].IR === "1"){
									var L = PMT[i].PV[e].L;
									var H = PMT[i].PV[e].H;

									inmeta = "inmeta:"+PMT[i].NM+":";
									if(PMT[i].T === "3"){
										inmeta += L != ''?"$"+L:"";
										inmeta += ".."+ H != ''?"$":+H;
										inmeta += L + ".."+ H;

										value_interface = self.replaceDataResult(L) + " - "+ self.replaceDataResult(H);
									}else{
										inmeta += L + ".."+ H;
										value_interface = self.replaceDataResult(L) + " - "+ self.replaceDataResult(H);
									}
								}else{
									inmeta = "inmeta:"+ PMT[i].NM +"="+PMT[i].PV[e].V;
								}

								// variável com o valor do parametro dnav sem caracteres especiais
								var dnavss = DNAVS.length >0? decodeURI(DNAVS)+" ":"";

								// teste de verificação do item no dnavs
								isElemtActive = new RegExp(inmeta).test(DNAVS)
								PMT[i].visible = isElemtActive;

								var params = self.generateObjectParams(json.GSP.PARAM);
								var location = window.location.href.split('?')[0];

								var elementA = "";
								
								// Teste de veridicação para mostrar item filtrado e não filtrado
								if(isElemtActive){
									params.dnavs = params.dnavs?params.dnavs.replace(/%20/g,' '):'';
									params.dnavs = params.dnavs.replace(RegExp("(\\s|)"+inmeta,"g"),'');
									params.dnavs = params.dnavs.trim();
									params.dnavs = params.dnavs?params.dnavs.replace(/\s+/g,' '):'';
									params.dnavs = params.dnavs.replace(/\s|%20/g,'%2520').replace(/%2520inmeta:/g, ' inmeta:');
									params.q = params.q.replace(/\sinmeta:.*/g, '').trim();
									params.q += params.dnavs.length >0?" " + params.dnavs:"";
									params.q = params.q.replace(/\s+/g," ");
									params.q = params.q.trim();

									if(!inmetaHierarq.hasOwnProperty(pmt_name[0])){
										inmetaHierarq[pmt_name[0]] = {};
									}

									inmetaHierarq[pmt_name[0]][PMT[i].NM] = inmeta;
									
									var url_nav = self.search_url_dinamicNavigation(params);

									elementA = '<span class="list-group-item text-left active">';
									elementA += '<a href="'+ location+'?'+ url_nav +'" class="badge gsa-return-navi '+pmt_name[0]+'-dyn" data-NM="'+PMT[i].NM+'">X</a>';
									elementA += '<span class="badge">'+ PMT[i].PV[e].C +'</span>';
									elementA += value_interface;
									elementA += '</span>';
								}else{
									params.dnavs = params.dnavs?params.dnavs:'';
									params.dnavs = params.dnavs.length > 0?params.dnavs + " " + inmeta:inmeta;
									params.dnavs = params.dnavs?params.dnavs.replace(/%20/g,' '):'';
									params.dnavs = params.dnavs?params.dnavs.replace(/\s+/g,' '):'';
									params.dnavs = params.dnavs.trim();
									params.dnavs = params.dnavs.replace(/\s|%20/g,'%2520').replace(/%2520inmeta:/g, ' inmeta:');
									params.q = params.q.replace(/\sinmeta:.*/g, '').trim();
									params.q += params.dnavs.length >0?" " + params.dnavs:"";
									params.q = params.q.replace(/\s+/g," ");
									var url_nav = self.search_url_dinamicNavigation(params);

									var class_pmt = e > 5?' hidden':'';

									elementA = '<a href="'+ location+'?'+ url_nav +'" class="list-group-item text-left gsa-next-navi'+class_pmt+'">';
									elementA += '<span class="badge">'+ PMT[i].PV[e].C +'</span>';
									elementA += value_interface;
									elementA += '</a>';
								}
								
								
								container += elementA;
							}
						};
						if(e > 5){
							container += '<span class="list-group-item" style="height: 45px;">';
							container += '<a href="#" class="gsa-more-dynav pull-left"> <span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span class="gsa-number"> '+ (e - 6) +'</span> Mais</a>';
							container += '<a href="#" class="gsa-minus-dynav pull-right hidden"> <span class="glyphicon glyphicon-minus" aria-hidden="true"></span> Menos</a>';
							container += '</span>';		
						}
						container += '</div></div>';

						html += container;
					}

				};

				//class = PMT NM
				//console.log(inmetaHierarq);
				html =  $.parseHTML( html );
				for(var x in inmetaHierarq){
					if(inmetaHierarq[x].hasOwnProperty(x+"_1")){
						var active = $(html).find('.'+x+'-dyn');
						var inmeta = [];
						for (var i = active.length - 1; i >= 0; i--) {
							var href = active[i].href;
							var NM = $(active[i]).attr("data-NM");
							if(NM){inmeta.push(NM);}
							if(inmeta.length > 0){
								var q = href.match(/(q=)[^&]+/g)[0].replace(/q=/,"").replace(/inmeta:.*/,"").trim();
								q = q.replace(/%20/g," ");
								q = /\s/.test(q.charAt(q.length-1))?q.substr(0, q.length-1):q;
								var dnavs = href.match(/(dnavs=)[^&]+/g);
								dnavs = $.isArray(dnavs)?dnavs[0].replace(/dnavs=/,""):"";

								for (var ie = 0; ie < inmeta.length; ie++) {
									var regex = new RegExp("inmeta:"+inmeta[ie]+"[^inmeta:]+","g");
									dnavs = dnavs.replace(regex,"");
								};

								dnavs = dnavs.substring(0,3) == "%20"?dnavs.substring(3):dnavs;
								q += dnavs.length > 0 ?"%20"+dnavs:"";

								href = href.replace(/(dnavs=)[^&]+/g,"$1"+dnavs);
								href = href.replace(/(q=)[^&]+/g,"$1"+ q);
								active[i].href = href;
							}
						};
					}
				}

	

				// Adiciona conteúdo processado ao container da navegação dinamica
				$(html).appendTo("#gsa-dynamic-navigation");

				//Adiciona as ações aos link para listar e esconder mais elementos da navegação dinamica
				this.moreFilters();
				this.minusFilters();

			},
			// Ação para mostrar mais filtros da navegação dinamica
			moreFilters:function(){
				$(".gsa-dn-container")
				.off('click','a.gsa-more-dynav')
				.on('click','a.gsa-more-dynav',function(event){
					event.preventDefault();
					var aElements = $(this).parent().siblings('.list-group-item.hidden');
					for (var i = 0; i < aElements.length; i++) {
						if(i < 10){
							$(aElements[i]).removeClass("hidden");
						}
					};
					var indice = (i - 10) > 0?(i-10):'';
					$(this).find('.gsa-number').text(indice);
					$(this).siblings('.gsa-minus-dynav').removeClass("hidden");
				});
			},
			minusFilters:function(){
				$(".gsa-dn-container")
				.off('click','a.gsa-minus-dynav')
				.on('click','a.gsa-minus-dynav',function(event){
					event.preventDefault();
					var aElements = $(this).parent().siblings('.list-group-item:not(.hidden)');
					for (var i = aElements.length - 1; i >= 6; i--) {
						//if(i > (aElements.length - 10)){
							$(aElements[i]).addClass("hidden");
						//}
					};
					var aElements2 = $(this).parent().siblings('.list-group-item.hidden');
					$(this).siblings('.gsa-more-dynav').find('.gsa-number').text(aElements2.length);
					if(i <= 6){
						$(this).addClass("hidden");
					}
					
				});
			}
		},
		//*** get params in list object ***
		//*** @name = name string for key in object
		//*** @list = Object for search
		//************************************
		getPARAM:function(name,list){
			var value = false;
			for (var i = 0; i < list.length; i++) {
				if(list[i].name == name){
					value = list[i];
				}
			}
			return value;
		},
		//*** get params value in list object ***
		//*** @key = name string for key in object
		//*** @object = Object for search
		//************************************
		findParamValue:function(key, object){
			var result = '';
			for (var x in object) {
				if(key === object[x].name){
					result = object[x].value;
				}
			}
			return result;
		},
		//*** Função para retornar um split do separador informado
		//** @myString = valor a ser separado
		//** @separator = valor do separador utilizado
		//** posição 1 --> valor anterior do separador
		//** posição 2 --> valor posterior ao separador 
		matchLastWordSeparator:function(myString,separator){
			var str = [myString,""];
			var regex = new RegExp("("+separator+"\\d+$)");
			if(regex.test(myString)){
				str[0] = myString.replace(regex,"");
				str[1] = myString.replace(str[0]+separator,"");
			}
			return str;
		},
		
		getCookie:function(name) {
			  var value = "; " + document.cookie;
			  var parts = value.split("; " + name + "=");
			  if (parts.length == 2) return parts.pop().split(";").shift();
			},
		//*** Função para realizar Ajax ao GSA
		//*** Realiza metodo GET e espera resultado JSON em texto
		//*** Retira tag <xml> e realiza o parse do resultado para JSON 
		//*** Envia para função RenderInit para processar o resultado;
		//*** @params = parametros a serem enviados
		//***  
		ajaxGetGSA:function(params){
			var self = this;
			var idpSession = "";
			jQuery.ajax({
				 url: '/sigaidp/IDPServlet',
			       success: function (result) {
			            idpSession = result;
			            gsaSession = gsa.getCookie("GSA_SESSION_ID");
			            jQuery.ajax({
			            	url: gsa.settings.proxy, 
			            	data: params, 
			            	//headers: {'IDP-JSESSIONID': idpSession},
			            	headers: {'IDP-JSESSIONID':jQuery.trim(idpSession), 'IDP-DOMAIN': window.location.host, 'GSA_SESSION_ID': gsaSession},
			            	success: function(json){
							    gsa.RenderInit(json);
								self.div_loader.hidePleaseWait();
			            	},
			            	error: function(){
			            		self.div_loader.hidePleaseWait();
			            	}
			            	
			            });
			         
			        }
			 		
			    });
			
//			var idpSession = "EqlMM5f81jZJ7AI568n0YTOy.classee:siga-auth-server01";
//			
//			            gsaSession = gsa.getCookie("GSA_SESSION_ID");
//			            jQuery.ajax({
//			            	url: gsa.settings.proxy, 
//			            	data: params, 
//			            	//headers: {'IDP-JSESSIONID': idpSession},
//			            	headers: {'IDP-JSESSIONID':jQuery.trim(idpSession), 'GSA_SESSION_ID': gsaSession},
//			            	success: function(json){
//							    gsa.RenderInit(json);
//								self.div_loader.hidePleaseWait();
//			            	},
//			            	error: function(){
//			            		self.div_loader.hidePleaseWait();
//			            	}
//			            	
//			            });
			         
			       
		},
		// Função para formatar data
		replaceDataResult:function(string){
			return string.replace(/(\d{4})-(\d{2})-(\d{2})/,'$3/$2/$1');
		},
		// Função para montar url de paginação
		search_url_navigation:function(PARAM){
			var url = [];
			var dnavs = {origen:"",value:"",q:""};
		    for (var x in PARAM) {
		    	if(PARAM[x].name != "q" && !/dnavs|start|swrnum|epoch|proxycustom|exclude_apps|ip|metabased_/g.test(PARAM[x].name) ){
		    		url.push(PARAM[x].name+"="+PARAM[x].value);
		    	}
		    	if(PARAM[x].name == "dnavs"){
		    		dnavs.origen = PARAM[x].value;
			        dnavs.value = decodeURI(dnavs.origen).replace(/\s|%20/g,'%2520').replace(/%2520inmeta:/g, ' inmeta:');
			        url.push(PARAM[x].name+"="+ dnavs.value);
		    	}

		    	if(PARAM[x].name == "q"){
		    		dnavs.q = PARAM[x].value;
		    	}
		    }
		    dnavs.q = dnavs.q.replace(dnavs.origen,dnavs.value);
			url.push("q="+ dnavs.q);

		    return url.join('&');
		},
		// Função para montar url de Navegação dinamica
		search_url_dinamicNavigation:function(PARAM){
			var url = [];
		    for (var x in PARAM) {
		    	if( !/start|swrnum|epoch|proxycustom|exclude_apps|ip|metabased_/g.test(x) )
		    		url.push( x + "=" + PARAM[x] );
		    }
		    return url.join('&');
		},
		// Função para transformar array de objeto em objeto nome:valor 
		generateObjectParams:function(PARAM){
			var object = {};
		    for (var x in PARAM) {
		    	object[PARAM[x].name]= PARAM[x].value;
		    }
		    return object;
		},
		// Função para montar objeto dos parametros da URL;
		generateObjectUrl:function(){
			var url = window.location.href.split('?')[1];
			url = decodeURIComponent(url).split('&');
			var object = {};
			for (var i = 0; i < url.length; i++) {
				var split = url[i].split('=');
				object[split[0]] = url[i].replace(split[0]+"=","");
			};
    		for(var x in gsa.settings.params){
    			if (!object.hasOwnProperty(x))
    				object[x] = gsa.settings.params[x];
    		}
			return object;
		},
		// Função para montar modal de carregamento.
		div_loader:{
	        pleaseWaitDiv: $(	        		
				'<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true" style="padding-top:15%; overflow-y:visible;">' +
					'<div class="modal-dialog modal-m">' +
						'<div class="modal-content">' +
							'<div class="modal-header">'+
								(typeof uriLogoSiga !== 'undefined' ? '<div class="col-6  p-0"><img src="' + uriLogoSiga + '" class="siga-modal__logo" alt="logo siga"></div>' : '<h5 class="modal-title">Siga</h5>') +
								'<div class="col-6  p-0"><h4 class="modal-title  siga-modal__titulo  siga-modal__titulo--direita">Processando a busca</h4></div>' +
							'</div>' +
							'<div class="modal-body">' +
								'<div class="progress progress-striped active" style="margin-bottom:0;"><div class="progress-bar" style="width: 100%"></div></div>' +
							'</div>' +
						'</div>'+
					'</div>'+
				'</div>'
			),
	        showPleaseWait: function() {
            	this.pleaseWaitDiv.modal();
           	},
	        hidePleaseWait: function () {
	            this.pleaseWaitDiv.modal('hide');
	        }
    	},
    	//Função para validar parametros na URL e executar AJAX para retornar e montar os resultados
		hash_url_load:function(){
	        var self = this;
	        var hash =  window.location.href;
	        
            if(/(q|as_q|partialfields|requiredfields)=/.test(hash)){
                self.div_loader.showPleaseWait();

            	var params = gsa.generateObjectUrl();
            	self.ajaxGetGSA(params);
    // 	          $.get("./json/result.json",params,function(data,status,xhr){
				// 	var json = {};
				// 	if(status = "success"){
				// 		//data = data.replace(/<\?xml.*\?>/gi,"");
				// 		data = data.replace(/\\/g,'\\\\');
				// 		data = data.replace(/\s+/g,' ');
				// 		json = JSON.parse(data);
				// 	}
				// 	gsa.RenderInit(json);
				// },"text");
               
            }
	        
    	},
    	// Função para Adicionar ao Formulário informado os paramtros do Settings;
    	writeInputParamsForm:function(targerForm){
    		var html = [];
    		for(var x in gsa.settings.params){
    			if(!/q/.test(x)){
    				html.push( '<input type="hidden" name="'+ x +'" value="'+ gsa.settings.params[x] +'"/>');
    			}
    		}
    		var params = this.generateObjectUrl();
    		if(params.q){
    			$(targerForm).find("input[name='q']").val(params.q.replace(/inmeta:.*/,'').trim());
    		}
    			
    		$(targerForm).append(html.join(''));
    	},
    	autocomplete:function(target){
    		//https://twitter.github.io/typeahead.js/examples/
    		//https://www.google.com/support/enterprise/static/gsa/docs/admin/74/gsa_doc_set/xml_reference/query_suggestion.html#1079937
    		params = {
    			type: "suggest",
    			max_matches: 10,
    			site: gsa.settings.params.site,
    			client: gsa.settings.params.client,
    			token:"%QUERY"
    		}

    		var url = [];
			for(var x in params){
				url.push(x+"="+params[x]);
			}

			var suggest = new Bloodhound({
			  	datumTokenizer: Bloodhound.tokenizers.whitespace,
				queryTokenizer: Bloodhound.tokenizers.whitespace,
			  	//prefetch: '../data/films/post_1960.json',
			  	remote: {
			    	url: gsa.settings.proxy+"?"+url.join("&"),
			    	wildcard: '%QUERY'
			  	}
			});

			$(target).typeahead(null, {
			 	name: 'Suggest',
			  	display: '',
			  	source: suggest
			});
    		

    	},
    	//Função inicial responsavel por setar funções de verificação e processamento JS;
    	load:function(targerForm){
    		document.onload = this.hash_url_load();
    		this.writeInputParamsForm(targerForm);
    		this.autocomplete(".typeahead");
    	}
	}


	// Acionando a função do GSA passando a classe do formulário do topo
 	gsa.load('.topoForm');	

	

});