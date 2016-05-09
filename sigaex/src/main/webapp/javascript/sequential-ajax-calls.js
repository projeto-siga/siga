//
// Processamento em lote, com progress bar
//

var process = {
	steps : [],
	index : 0,
	title : "Batch Operation",
	initializingmsg : "Initializing...",
	errormsg : "Não foi possível completar a operação",
	urlRedirect : null,
	reset : function() {
		this.steps = [];
		this.index = 0;
	},
	push : function(x) {
		this.steps.push(x);
	},
	run : function() {
		window.scrollTo(0, 0);
		this.dialogo = $(
				'<div id="dialog-ad" title="' + this.title
						+ '"><p id="vbslog">' + this.starting
						+ '</p><div id="progressbar-ad"></div></div>').dialog({
			title : this.title,
			width : '50%',
			height : 'auto',
			resizable : false,
			autoOpen : true,
			position : {
				my : "center top+25%",
				at : "center top",
				of : window
			},
			modal : true,
			closeText : "hide"
		});
		this.progressbar = $('#progressbar-ad').progressbar();
		this.nextStep();
	},
	finalize : function() {
		this.dialogo.dialog('destroy');
	},
	nextStep : function() {
		if (typeof this.steps[this.index] == 'string')
			eval(this.steps[this.index++]);
		else {
			var ret = this.steps[this.index++]();
			if ((typeof ret == 'string') && ret != "OK") {
				this.finalize();
				alert(ret, 0, this.errormsg);
				return;
			}
		}

		this.progressbar.progressbar("value",
				100 * (this.index / this.steps.length));

		if (this.index != this.steps.length) {
			var me = this;
			window.setTimeout(function() {
				me.nextStep();
			}, 100);
		} else {
			this.finalize();
		}
	},

	identifyOperations : function() {
		this.operations = [];

		var NodeList = document.getElementsByTagName("INPUT");
		for (var i = 0, len = NodeList.length; i < len; i++) {
			var Elem = NodeList[i];
			if (Elem.name.substr(0, 14) == "process_descr_") {
				var operation = {};

				operation.code = Elem.name.substr(14);
				operation.descr = document.getElementsByName("process_descr_"
						+ operation.code)[0].value;
				operation.url = document.getElementsByName("process_url_post_"
						+ operation.code)[0].value;

				var oChk = document.getElementsByName("process_chk_"
						+ operation.code)[0];
				if (oChk == null) {
					operation.enabled = true;
				} else {
					operation.enabled = oChk.checked;
				}

				this.operations.push(operation);
			}
		}
	},

	runTest : function() {
		process.reset();

		process.push(function() {
			Log("Iniciando operação em lote")
		});

		process.push(function() {
			return "OK";
		});

		process.push(function() {
			Log("Concluído, redirecionando...");
		});

		process.push(function() {
			if (this.urlRedirect != null)
				location.href = this.urlRedirect.value;
		});

		process.run();
	}

};

function Log(Text) {
	var oLog;
	oLog = document.getElementById("vbslog");
	if (oLog != null) {
		oLog.innerHTML = Text;
	}
}