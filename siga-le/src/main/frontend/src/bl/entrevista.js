export default {
    fix: function (s) {
        // remover scripts
        s = s.replace(/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, '')
        // trocar onclick por @change
        s = s.replace(/ onclick="/gi, '@change="')
        // substituir o ckeditor
        s = s.replace(/<textarea id="(?<id>[^"]+)" name="(?<name>[^"]+)" class="editor">(?<content>.*?)<\/textarea>/gis, '<ckeditor v-on:input="setHidden(\'$<name>\', $event)" value="$<content>"></ckeditor><input type="hidden" ref="hidden_$<name>" name="$<name>">');
        // console.log(s)
        return s;
    },

    getFormResults: function (formElement) {
        var setOrPush = function (target, val) {
            var result = val;
            if (target) {
                result = [target];
                result.push(val);
            }
            return result;
        }
        var formElements = formElement.elements;
        var formParams = {};
        var i = 0;
        var elem = null;
        for (i = 0; i < formElements.length; i += 1) {
            elem = formElements[i];
            switch (elem.type) {
                case 'submit':
                    break;
                case 'radio':
                    if (elem.checked) {
                        formParams[elem.name] = elem.value;
                    }
                    break;
                case 'checkbox':
                    if (elem.checked) {
                        formParams[elem.name] = setOrPush(formParams[elem.name], elem.value);
                    }
                    break;
                default:
                    formParams[elem.name] = setOrPush(formParams[elem.name], elem.value);
            }
        }
        return formParams;
    },

    encodeFormParams: function (formParams) {
        var s = "";
        for (var k in formParams) {
            if (s !== "") s += "&";
            s += k + "=" + encodeURIComponent(formParams[k]);
        }
        return s;
    }

}