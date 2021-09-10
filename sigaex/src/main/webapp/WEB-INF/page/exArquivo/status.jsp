<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://localhost/functiontag" prefix="fg"%>

<siga:pagina titulo="Aguarde..." popup="${true}">

	<div id="app" class="container content mt-5">
		<p class="mb-1">{{ progressbarCaption }}</p>
		<div class="progress">
			<div id="progressbar-ad" class="progress-bar" role="progressbar"
				aria-valuenow="{progressbarWidth}" aria-valuemin="0"
				aria-valuemax="100" :style="{ width: progressbarWidth + '%' }">
				<span class="sr-only"></span>
			</div>
		</div>
		<div v-if="bytes">
			<span class="float-right mt-1">{{ bytes }}</span>
		</div>
	</div>

	<script type="text/javascript" src="/sigaex/javascript/vue.min.js"></script>

	<script type="text/javascript" language="Javascript1.1">
	
	var httpGet = function(url, success, error) {  
	  var xhr = XMLHttpRequest ? new XMLHttpRequest() : 
	                             new ActiveXObject("Microsoft.XMLHTTP"); 
	  xhr.open("GET", url, true); 
	  xhr.onreadystatechange = function(){ 
	    if ( xhr.readyState == 4 ) { 
	      if ( xhr.status == 200 ) { 
	    success(xhr.responseText); 
	      } else { 
	    error(xhr, xhr.status); 
	      } 
	    } 
	  }; 
	  xhr.onerror = function () { 
	    error(xhr, xhr.status); 
	  }; 
	  xhr.send(); 
	} 

	var demo = new Vue({
		  el: "#app",
		  
		  mounted() {
			  this.start('${sigla}', '${uuid}', this.end);
		  },

		  data() {
		    return {
		      i: 0,
		      title: undefined,
		      caption: undefined,
		      callbackEnd: function() {},
		      progressbarWidth: 0,
		      progressbarCaption: undefined,
		      bytes: undefined,
		      errormsg: undefined
		    };
		  },
		
		  methods: {
		    start: function(title, key, callbackEnd) {
		      this.title = title;
		      this.key = key;
		      this.callbackEnd = callbackEnd;
		      this.refresh();
		    },
		
		    cancel: function() {
		    },
		
		    refresh: function() {
		      httpGet("/sigaex/api/v1/status/" + this.key,
		        text => {
		          var r = JSON.parse(text);
		          this.progressbarCaption = r.mensagem;
		          this.progressbarWidth = 100 * (r.indice / r.contador);
		          this.bytes = r.bytes ? this.formatBytes(r.bytes) : undefined;
		          if (r.indice === r.contador) {
		            this.showModal = false;
		            this.progressbarWidth = 0;
		            this.callbackEnd(this.i);
		          } else {
		            setTimeout(() => {
		              this.refresh();
		            }, 2000);
		          }
		        },
		        error => {
		          this.errormsg =
		            error.data || "Erro obtendo informações de status";
		        }
		      );
		    },
		    
		    end: function() {
		    	if("${filename}".includes(".html")){
		    		window.location = "/sigaex/api/v1/download/${jwt}/${filename}";
		    	} else {
		    		window.location = montarUrlDocPDF("/sigaex/api/v1/download/${jwt}/${filename}", "${fg:resource('/sigaex.pdf.visualizador')}");
		    	}
		    },
		    
		    formatBytes: function(bytes, decimals = 2) {
		        if (bytes === 0) return "0 Bytes";
		        const k = 1024;
		        const dm = decimals < 0 ? 0 : decimals;
		        const sizes = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
		        const i = Math.floor(Math.log(bytes) / Math.log(k));
		        const f = parseFloat((bytes / Math.pow(k, i)).toFixed(dm));
		        const r = ("" + f).replace(".", ",");
		        return r + " " + sizes[i];
		    }
		  }
		});
</script>
</siga:pagina>
