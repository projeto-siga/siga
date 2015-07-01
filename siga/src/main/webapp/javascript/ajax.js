/**
SAL - Simple Ajax Lib. 23-Sep-2005
by Nigel Liefrink
Email: leafrink@hotmail.com
*/

var debug = false;
var active = 0;

/**
<summary>
Browser Compatability function.
Returns the correct XMLHttpRequest 
  depending on the current browser.
</summary>
*/
function GetXmlHttp() {
  var xmlhttp = false;
  if (window.XMLHttpRequest)
  {
    xmlhttp = new XMLHttpRequest()
  }
  else if (window.ActiveXObject)// code for IE
  {
    try
    {
      xmlhttp = new ActiveXObject("Msxml2.XMLHTTP")
    } catch (e) {
      try
      {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP")
      } catch (E) {
        xmlhttp=false
      }
    }
  }
  return xmlhttp;
}

function mostraCarregando(){
	try {
		var style = document.getElementById("carregando").style;
		var posiciona = function(){
			style.top = document.body.scrollTop;
		};
		posiciona();
		window.onscroll = posiciona;
		style.display = 'block';
	} catch (err) {
	}
}

function ocultaCarregando(){
	try {
		document.getElementById("carregando").style.display = 'none';
		window.onscroll = null;
	} catch (err) {
	}
}

/**
<summary>
Gets the response stream from the passed url, and then calls
   the callbackFuntion passing the response and the div_ids.
</summary>
<param name="url">The url to make the request
            to get the response data.</param>
<param name="callbackFunction">The function to call after 
the response has been recieved.
The response <b>must</b> always 
be the first argument to the function.</param>
<param name="params"> (optional) Any other parameters 
you want to pass to the functions.
(Note: only constants/strings/globals can be passed
       as params, most variables will be out of scope.)
</param>
<example>
    <code>
PassAjaxResponseToFunction('?getsomehtml=1', 
              'FunctionToHandleTheResponse', 
              "\'div1\',\'div2\',\'div3\'');

function FunctionToHandleTheResponse(response, d1, d2, d3){
    var data = response.split(';');
    document.getElementById(d1).innerHTML = data[0];
    document.getElementById(d2).innerHTML = data[1];
    document.getElementById(d3).innerHTML = data[2];
}
    </code>
</example>
*/
function PassAjaxResponseToFunction(url, callbackFunction, params, omitirCarregando, postParams)
{
  if (!omitirCarregando)
  	mostraCarregando();
  var xmlhttp = new GetXmlHttp();
  //now we got the XmlHttpRequest object, send the request.
  if (xmlhttp)
  {
    xmlhttp.onreadystatechange = 
            function ()
            {
              if (xmlhttp && xmlhttp.readyState==4)
              {//we got something back..
                active--;
                if (xmlhttp.status==200)
                {
                  var response = xmlhttp.responseText;
                  var functionToCall = callbackFunction + 
                                 '(response,'+params+')';
                  if(debug)
                  {
                    alert(response);
                    alert(functionToCall);
                  }
                  eval(functionToCall);
                } else if(debug){
                  document.write(xmlhttp.responseText);
                }
                ocultaCarregando();
              }
            }
    var method;
    if (postParams == null)
  		method = "GET";
  	else method = "POST";
    xmlhttp.open(method,url,true);
    if (method == "POST"){
    	xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		//xmlhttp.setRequestHeader("Content-length", postParams.length);
		//xmlhttp.setRequestHeader("Connection", "close");
		xmlhttp.send(postParams);
    } else xmlhttp.send(null);
  }
  active++;
}


/**
<summary>
Sets the innerHTML property of obj_id with the response from the passed url.
</summary>
<param name="url">The url to make the request 
to get the response data.</param>
<param name="obj_id">The object or the id of 
the object to set the innerHTML for.</param>
*/
function SetInnerHTMLFromAjaxResponse(url, obj_id)
{
  mostraCarregando();
  active++;
  
  var jqxhr = $.get(url, function(s) {
	if(typeof obj_id == 'object') {
    	obj_id.innerHTML = s;
	} else {
    	document.getElementById(obj_id).innerHTML = s;
	}
  
	// Caso seja necessario acrescentar algum script na pagina, ficou convencionado que
	// o script deverá ser marcado com <script type="text/javascript" > e < /script>
	if (s.indexOf('<script type="text/javascript">') != -1) {
		var j = 0;
		var len = 0;
		do {
			j = s.indexOf('<script type="text/javascript">', j);
			if (j<=0)
				break;
			j = j + 31;
			len = s.indexOf('</script>', j);
			eval(s.substring(j, len));
		} while (true)
	}
  })
  .always(function() {
		ocultaCarregando();
  });
  
  return;

  
  var xmlhttp = new GetXmlHttp();
  //now we got the XmlHttpRequest object, send the request.
  if (xmlhttp)
  {
    xmlhttp.onreadystatechange = 
            function ()
            {
              if (xmlhttp && xmlhttp.readyState==4)
              {//we got something back..
                active--;
                if (xmlhttp.status==200)
                {
                	var s = xmlhttp.responseText;
                	
                	var splitted = s.split('###');
                	if (splitted[0] == 'TRYAGAIN'){
                		//alert('SetInnerHTMLFromAjaxResponse(\''+url+'\',\''+obj_id.id+'\')');
                		var timer=setTimeout('SetInnerHTMLFromAjaxResponse(\''+url+'\',\''+obj_id.id+'\')',1500);
                	} else {
                    	if(debug) {
	                    	alert(s);
                  		}
                  		if(typeof obj_id == 'object') {
	                    	obj_id.innerHTML = s;
                  		} else {
	                    	document.getElementById(obj_id).innerHTML = s;
                  		}
	                  
						// Caso seja necessario acrescentar algum script na pagina, ficou convencionado que
		            	// o script deverá ser marcado com <script type="text/javascript" > e < /script>
	               		if (s.indexOf('<script type="text/javascript">') != -1) {
							var j = 0;
							var len = 0;
							do {
								j = s.indexOf('<script type="text/javascript">', j);
								if (j<=0)
									break;
								j = j + 31;
								len = s.indexOf('</script>', j);
								eval(s.substring(j, len));
							} while (true)
						}
						ocultaCarregando();
					}
                } else if(debug){
                  document.Write(xmlhttp.responseText);
                }
              }
            }
    xmlhttp.open("GET",url,true);
    xmlhttp.send(null);
    active++;
  }
}

function $(e){if(typeof e=='string')e=document.getElementById(e);return e};
function collect(a,f){var n=[];for(var i=0;i<a.length;i++){var v=f(a[i]);if(v!=null)n.push(v)}return n};

ajax={};
ajax.x=function(){try{return new ActiveXObject('Msxml2.XMLHTTP')}catch(e){try{return new ActiveXObject('Microsoft.XMLHTTP')}catch(e){return new XMLHttpRequest()}}};
ajax.serialize=function(f){var g=function(n){return f.getElementsByTagName(n)};

var nv=function(e){
if(e.name)
	return encodeURIComponent(e.name)+'='+encodeURIComponent(e.value);
else
	return ''};
	
var nvs=function(e){
if(e.name) {
	if (e.options[e.selectedIndex].value != "")
		return encodeURIComponent(e.name)+'='+encodeURIComponent(e.options[e.selectedIndex].value);
	else
		return encodeURIComponent(e.name)+'='+encodeURIComponent(e.options[e.selectedIndex].text);
} else
	return ''};
	
var i=collect(g('input'),function(i){if((i.type!='radio'&&i.type!='checkbox')||i.checked)return nv(i)});
var s=collect(g('select'),nvs);
var t=collect(g('textarea'),nv);
return i.concat(s).concat(t).join('&');};
ajax.send=function(u,f,m,a){var x=ajax.x();x.open(m,u,true);x.onreadystatechange=function(){if(x.readyState==4)f(x.responseText)};if(m=='POST')x.setRequestHeader('Content-type','application/x-www-form-urlencoded');x.send(a)};
ajax.get=function(url,func){ajax.send(url,func,'GET')};
ajax.gets=function(url){var x=ajax.x();x.open('GET',url,false);x.send(null);return x.responseText};
ajax.post=function(url,func,args){ajax.send(url,func,'POST',args)};
ajax.update=function(url,elm){var e=$(elm);var f=function(r){e.innerHTML=r};ajax.get(url,f)};
ajax.submit=function(url,elm,frm){var e=$(elm);var f=function(r){e.innerHTML=r};ajax.post(url,f,ajax.serialize(frm))};


/**
<summary>
Sets the innerHTML property of obj_id with the response from the passed url.
</summary>
<param name="url">The url to make the request 
to get the response data.</param>
<param name="obj_id">The object or the id of 
the object to set the innerHTML for.</param>
*/
function MixWithNewPage(aElements, sPage, sIdObj) {
	var e;
	var depende;
//	var a = [document.getElementsByTagName('div'),document.getElementsByTagName('span')];
//	var d = document.getElementsByTagName('div');
//  for (var d in a) {
    for (e in aElements) {
    
	    if (e != null && e != 'length' && e != 'item' && e != 'namedItem') {
	    
//			if (window.XMLHttpRequest) {
				// Firefox
//				alert('e: ' + e + aElements[e]);
				e = aElements[e];
				try {
					depende = e.attributes.depende.nodeValue;
				} catch (ex) {
					depende = null;
				}
//			} else {
				// InternetExplorer
//				e = document.getElementById(e);
//				depende = e.depende;
//			}
			
	     	if(e != null && typeof depende == 'string') {
//				e = document.getElementById(e);
//alert('depende: ' + e.depende);
				if (depende.indexOf(';'+sIdObj+';') != -1) {
					var i = 0;
					i = sPage.indexOf('<!--ajax:'+e.id+'-->', i);
//alert('<!--ajax:'+e.id+'-->', i);
					if (i > 0) {
						var s = sPage.substring(i,sPage.indexOf('<!--/ajax:'+e.id+'-->', i));
						
						// Insere o HTML
						e.innerHTML = s;
//alert(s);							
		              	// Caso seja necessario acrescentar algum script na pagina, ficou convencionado que
		              	// o script deverá ser marcado com <script type="text/javascript" > e < /script>
		               	if (s.indexOf('<script type="text/javascript">') != -1) {
							var j = 0;
							var len = 0;
							do {
								j = s.indexOf('<script type="text/javascript">', j);
								if (j<=0)
									break;
								j = j + 31;
								len = s.indexOf('</script>', j);
//	alert(j);
//	alert(len);
//alert(s.substring(j, len));
								eval(s.substring(j, len));
							} while (true)
						}
					}
				}
			}
		}
	}					
}

function ReplaceInnerHTMLFromAjaxResponse(url, frm, obj_id) {
	var res = new RespostaAjax();
	mostraCarregando();
	var xmlhttp = new GetXmlHttp();
	//now we got the XmlHttpRequest object, send the request.
	if (xmlhttp) {
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp && xmlhttp.readyState == 4) {//we got something back..
				active--;
				if (xmlhttp.status == 200) {
					if (debug) {
						alert(xmlhttp.responseText);
					}
					if (typeof obj_id == 'object') {
						obj_id.innerHTML = xmlhttp.responseText;
					} else {
						var r = xmlhttp.responseText;
						MixWithNewPage(document.getElementsByTagName('span'), r, obj_id);
						MixWithNewPage(document.getElementsByTagName('div'), r, obj_id);
					}
					res.invokeSuccess(xmlhttp.responseText);
				} else if (debug) {
					document.Write(xmlhttp.responseText);
					res.invokeError(xmlhttp.responseText);
				}
				ocultaCarregando();
			}
		}
		//	alert(ajax.serialize(frm));
		if (frm == null) {
			xmlhttp.open("GET", url, true);
			xmlhttp.send(null);
		} else {
			xmlhttp.open("POST", url, true);
			xmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
			xmlhttp.send(ajax.serialize(frm));
		}
		active++;
	}
	return res;
}

function IsRunningAjaxRequest() {
	//window.alert(document.getElementById("carregando").style.display != 'none');
	return document.getElementById("carregando").style.display != 'none';
}

function RespostaAjax() {
	var successCallback = errorCallback = null;
	
	this.success = function(successCallback0) {
		successCallback = successCallback0;
	}
	
	this.error = function(errorCallback0) {
		errorCallback = errorCallback0;
	}
	
	this.invokeSuccess = function(responseText) {
		if(successCallback) 
			successCallback(responseText);
	}
	
	this.invokeError = function(responseText) {
		if(errorCallback)
			errorCallback(responseText);
	}
}