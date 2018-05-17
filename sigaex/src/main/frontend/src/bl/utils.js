export default {
  formatDDMMYYYY: function (s) {
    if (!s) return
    return (
      s.substring(8, 10) + '/' + s.substring(5, 7) + '/' + s.substring(0, 4)
    )
  },

  formatDDMMYYYYHHMM: function (s) {
    if (s === undefined) return

    var r =
      s.substring(6, 8) +
      '/' +
      s.substring(4, 6) +
      '/' +
      s.substring(0, 4) +
      ' ' +
      s.substring(8, 10) +
      ':' +
      s.substring(10, 12)
    r = r.replace(' ', '&nbsp;')
    return r
  },

  convertString2DateYYYYMMDDHHMM: function (s) {
    if (s === undefined) return

    var r =
      s.substring(0, 4) +
      '-' +
      s.substring(4, 6) +
      '-' +
      s.substring(6, 8) +
      'T' +
      s.substring(8, 10) +
      ':' +
      s.substring(10, 12)
    return new Date(r)
  },

  formatJSDDMMYYYYHHMM: function (s) {
    if (!s) return
    var r =
      s.substring(8, 10) +
      '/' +
      s.substring(5, 7) +
      '/' +
      s.substring(0, 4) +
      '&nbsp;' +
      s.substring(11, 13) +
      ':' +
      s.substring(14, 16)
    return r
  },

  trunc: function (s, n, useWordBoundary) {
    if (s.length <= n) {
      return s
    }
    var subString = s.substr(0, n - 1)
    return (
      (useWordBoundary
        ? subString.substr(0, subString.lastIndexOf(' '))
        : subString) + '&hellip;'
    )
  },
  arrayMove: function (a, oldIndex, newIndex) {
    if (newIndex >= a.length) {
      var k = newIndex - a.length
      while (k-- + 1) {
        a.push(undefined)
      }
    }
    a.splice(newIndex, 0, a.splice(oldIndex, 1)[0])
    return a // for testing purposes
  },
  formatMoney: function (n, c, d, t) {
    c = isNaN((c = Math.abs(c))) ? 2 : c
    d = d === undefined ? '.' : d
    t = t === undefined ? ',' : t
    var s = n < 0 ? '-' : ''
    var i = parseInt((n = Math.abs(+n || 0).toFixed(c))) + ''
    var j = (j = i.length) > 3 ? j % 3 : 0
    return (
      s +
      (j ? i.substr(0, j) + t : '') +
      i.substr(j).replace(/(\d{3})(?=\d)/g, '$1' + t) +
      (c
        ? d +
          Math.abs(n - i)
            .toFixed(c)
            .slice(2)
        : '')
    )
  },
  logEvento: function (categoria, acao, rotulo, valor) {
    try {
      /* global ga */
      /* eslint no-undef: "error" */
      ga('send', 'event', categoria, acao, rotulo, valor)
    } catch (ex) {}
  },
  camelCaseToDash: function (s) {
    return s.replace(/([a-zA-Z])(?=[A-Z])/g, '$1-').toLowerCase()
  },

  filtrarPorSubstring: function (a, s) {
    var re = new RegExp(s, 'i')
    return a.filter(function filterItem (item) {
      for (var key in item) {
        if (!item.hasOwnProperty(key)) continue
        if (typeof item[key] === 'string' && re.test(item[key])) {
          return true
        }
        if (typeof item[key] === 'object' && filterItem(item[key])) {
          return true
        }
      }
      return false
    })
  },

  absoluteUrl: function (base, relative) {
    var stack = base.split('/')
    var parts = relative.split('/')
    stack.pop() // remove current file name (or empty string) (omit if "base" is the current folder without trailing slash)
    for (var i = 0; i < parts.length; i++) {
      if (parts[i] === '.') continue
      if (parts[i] === '..') stack.pop()
      else stack.push(parts[i])
    }
    return stack.join('/')
  },

  applyDefauts: function (obj, defaults) {
    for (var k in defaults) {
      if (!defaults.hasOwnProperty(k)) continue
      if (obj.hasOwnProperty(k)) continue
      obj[k] = defaults[k]
    }
  },

  overrideProperties: function (obj, source) {
    for (var k in source) {
      if (!source.hasOwnProperty(k)) continue
      obj[k] = source[k]
    }
  },

  errormsg: function (error, component) {
    component.errormsg = error.data.errormsg
    if (
      component.errormsg === undefined &&
      error.statusText &&
      error.statusText !== ''
    ) {
      component.errormsg = error.statusText
    }
    if (component.errormsg === undefined) {
      component.errormsg = 'Erro desconhecido!'
    }
  },

  quietBatch: function (arr, callbackNext, callbackEnd, index) {
    if (index === undefined) index = 0
    if (index >= arr.length) {
      if (callbackEnd) callbackEnd(arr)
      return
    }
    callbackNext(arr[index], () => {
      this.quietBatch(arr, callbackNext, callbackEnd, index + 1)
    })
  },

  getUTF8Length: function (string) {
    var utf8length = 0
    for (var n = 0; n < string.length; n++) {
      var c = string.charCodeAt(n)
      if (c < 128) {
        utf8length++
      } else if (c > 127 && c < 2048) {
        utf8length = utf8length + 2
      } else {
        utf8length = utf8length + 3
      }
    }
    return utf8length
  },

  clipUTF8Length: function (string, length) {
    var utf8length = 0
    for (var n = 0; n < string.length; n++) {
      var c = string.charCodeAt(n)
      if (c < 128) {
        utf8length++
      } else if (c > 127 && c < 2048) {
        utf8length = utf8length + 2
      } else {
        utf8length = utf8length + 3
      }
      if (utf8length > length) return string.substring(0, n)
    }
    return string
  }
}
