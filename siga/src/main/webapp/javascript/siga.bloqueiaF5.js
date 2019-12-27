/*
* Impede o uso do F5
*/
bloqueiaF5 = function bloquearF5(ev) {
	var oEvent = (window.event) ? window.event : ev;
	if (oEvent.keyCode == 116) {
		//alert("F5 Pressionado !"); 
		if(document.all) {
			oEvent.keyCode = 0;
			oEvent.cancelBubble = true;
		} else {
			oEvent.preventDefault();
			oEvent.stopPropagation();
		}
		return false;
	}
};
 	
window.addEventListener('keydown', bloqueiaF5); 