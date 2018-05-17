import UtilsBL from './utils.js'
import CnjClasseBL from './cnj-classe.js'

export default {
  fixProc: function (p) {
    var i, j, k
    var fixed = {}
    p.dadosBasicos.numero = this.formatarProcesso(p.dadosBasicos.numero)

    for (i = 0; i < p.dadosBasicos.polo.length; i++) {
      if (p.dadosBasicos.polo[i].polo === 'AT') {
        fixed.partesAtivas = p.dadosBasicos.polo[i].parte
      }
      if (p.dadosBasicos.polo[i].polo === 'PA') {
        fixed.partesPassivas = p.dadosBasicos.polo[i].parte
      }
    }

    if (!p.dadosBasicos.outroParametro) p.dadosBasicos.outroParametro = {}
    fixed.nomeMagistrado = p.dadosBasicos.outroParametro.nomeMagistrado

    if (p.documento) {
      for (i = 0; i < p.documento.length; i++) {
        var f = false
        var doc = p.documento[i]

        // Formatar os textos
        if (doc.outroParametro && doc.outroParametro.textoMovimento) {
          doc.outroParametro.textoMovimento = this.formatarTexto(
            doc.outroParametro.textoMovimento
          )
        }

        if (p.movimento) {
          for (j = 0; j < p.movimento.length; j++) {
            var mov = p.movimento[j]
            if (
              mov.hasOwnProperty('idDocumentoVinculado') &&
              mov.idDocumentoVinculado.includes(doc.idDocumento)
            ) {
              if (!mov.documento) mov.documento = []
              mov.documento.unshift(doc)
              f = true
              break
            }
          }
        }
        if (f) continue
        if (!p.movimento) p.movimento = []
        p.movimento.push({
          dataHora: doc.dataHora,
          documento: [doc]
        })
      }
    }

    if (p.movimento) {
      // p.movimento = p.movimento.sort(function(a, b) {
      // if (a.dataHora < b.dataHora)
      // return 1;
      // if (a.dataHora > b.dataHora)
      // return -1;
      // return 0;
      // })

      fixed.movdoc = []
      for (j = 0; j < p.movimento.length; j++) {
        mov = p.movimento[j]
        if (
          mov.complemento &&
          mov.movimentoLocal &&
          mov.movimentoLocal.descricao
        ) {
          for (k = 0; k < mov.complemento.length; k++) {
            if (mov.movimentoLocal.descricao !== mov.complemento[k]) {
              if (
                mov.complemento[k].indexOf(mov.movimentoLocal.descricao) === 0
              ) {
                mov.movimentoLocal.descricao = mov.complemento[k]
              } else mov.movimentoLocal.descricao += ' - ' + mov.complemento[k]
            }
          }
        }
        fixed.movdoc.push({
          dataHora: mov.dataHora,
          mov: mov,
          doc: (mov.documento || [{}])[0],
          rowspan: 1,
          marca: []
        })
        if (mov.documento && mov.documento.length > 0) {
          for (i = 1; i < mov.documento.length; i++) {
            fixed.movdoc.push({
              dataHora: mov.dataHora,
              mov: mov,
              doc: mov.documento[i],
              rowspan: 1,
              marca: []
            })
          }
        }
      }

      // Ativar a visualização da primeira decisão
      if (fixed.movdoc) {
        var fDecisao = true
        for (i = 0; i < fixed.movdoc.length; i++) {
          var movdoc = fixed.movdoc[i]
          if (movdoc.doc) {
            doc = movdoc.doc
            if (doc.outroParametro && doc.outroParametro.textoMovimento) {
              if (fDecisao) {
                this.mostrarTexto(fixed.movdoc, doc, true)
                fDecisao = false
              } else doc.exibirTexto = false
            }
          }
        }
      }

      this.fixMovDoc(fixed.movdoc)

      if (
        typeof p.dadosBasicos.valorCausa === 'number' &&
        p.dadosBasicos.valorCausa > 0
      ) {
        fixed.valorCausa =
          'R$ ' + UtilsBL.formatMoney(p.dadosBasicos.valorCausa, 2, ',', '.')
      }
      fixed.dataAjuizamento = UtilsBL.formatDDMMYYYYHHMM(
        p.dadosBasicos.dataAjuizamento
      )

      var op = p.dadosBasicos.outroParametro
      if (op.processoVinculado) {
        fixed.processoVinculado = this.arrayOfStringsToObjects(
          op.processoVinculado,
          ['numero', 'cnjClasse', 'descrClasse', 'digital']
        )
        for (i = 0; i < fixed.processoVinculado.length; i++) {
          fixed.processoVinculado[i].link = this.colocarLink(
            fixed.processoVinculado[i].numero
          )
          fixed.processoVinculado[i].nomeClasse = CnjClasseBL.nome(
            fixed.processoVinculado[i].cnjClasse
          )
          fixed.processoVinculado[i].suporte =
            fixed.processoVinculado[i].digital === 'E'
              ? 'Digital'
              : fixed.processoVinculado[i].digital === 'F' ? 'Físico' : '?'
        }
      }
      if (op.recurso) {
        fixed.recurso = this.arrayOfStringsToObjects(op.recurso, [
          'numero',
          'cnjClasse',
          'descrClasse',
          'digital'
        ])
        for (i = 0; i < fixed.recurso.length; i++) {
          fixed.recurso[i].link = this.colocarLink(fixed.recurso[i].numero)
          fixed.recurso[i].nomeClasse = CnjClasseBL.nome(
            fixed.recurso[i].cnjClasse
          )
          fixed.recurso[i].suporte =
            fixed.recurso[i].digital === 'E'
              ? 'Digital'
              : fixed.recurso[i].digital === 'F' ? 'Físico' : '?'
        }
      }
      if (op.processoOriginario) {
        op.processoOriginario = this.colocarLink(op.processoOriginario)
      }
      if (op.peticaoPendenteJuntada) {
        op.peticaoPendenteJuntada = this.arrayToString(
          op.peticaoPendenteJuntada
        )
      }

      if (op.numProcAdm) op.numProcAdm = this.arrayToString(op.numProcAdm)
      if (op.numCDA) {
        if (typeof op.numCDA === 'string') op.numCDA = [op.numCDA]
        fixed.numCDAs = this.arrayToString(op.numCDA)
      }
      if (op.informacoesParte) {
        var map = {}
        var inf = this.arrayOfStringsToObjects(op.informacoesParte, [
          'nome',
          'tipoAtuacao',
          'documento'
        ])
        for (i = 0; i < inf.length; i++) map[inf[i].nome] = inf[i]
        for (i = 0; i < p.dadosBasicos.polo.length; i++) {
          for (j = 0; j < p.dadosBasicos.polo[i].parte.length; j++) {
            p.dadosBasicos.polo[i].parte[j].tipoAtuacao =
              map[p.dadosBasicos.polo[i].parte[j].pessoa.nome].tipoAtuacao
            p.dadosBasicos.polo[i].parte[j].documento =
              map[p.dadosBasicos.polo[i].parte[j].pessoa.nome].documento
          }
        }
      }
    }
    return fixed
  },

  // Corrige ordenação de peças avulsas
  fixMovDoc: function (a) {
    var lastIdDocumento
    for (var i = 0; i < a.length; i++) {
      var movdoc = a[i]

      // verifica se a peça está fora de ordem
      if (movdoc.doc.idDocumento) {
        if (!lastIdDocumento === undefined) {
          lastIdDocumento = Number(movdoc.doc.idDocumento)
        } else if (Number(movdoc.doc.idDocumento) > lastIdDocumento) {
          // localizar o primeiro que já é menor do
          // que o que está fora de posição
          for (var j = 0; j < i; j++) {
            var md = a[j]
            if (
              md.doc.idDocumento &&
              Number(md.doc.idDocumento) < Number(movdoc.doc.idDocumento)
            ) {
              UtilsBL.arrayMove(a, i, j)
              break
            }
          }
        } else {
          lastIdDocumento = Number(movdoc.doc.idDocumento)
        }
      }
    }
  },
  filtrar: function (a, f) {
    try {
      var fs, i, k

      // Nenhum filtro
      if (!f || f.trim() === '') {
        fs = a
      } else if (f.substring(0, 1) === '#') {
        // Filtrador por hashtag
        var ff = f.split(' ')
        fs = []
        for (i = 0; i < a.length; i++) {
          for (k = 0; k < ff.length; k++) {
            if (
              (ff[k] === '#marca' && a[i].marca && a[i].marca.length > 0) ||
              (a[i].mov && a[i].mov.tipo && a[i].mov.tipo === ff[k])
            ) {
              fs.push(a[i])
              break
            }
          }
        }
      } else {
        fs = UtilsBL.filtrarPorSubstring(a, f)
      }

      a = fs

      // Quando existem duas ou mais linhas referentes ao
      // mesmo movimento, omitir o movimento e aumentar o
      // rowspan da primeira linha.
      for (i = 0; i < a.length - 1; i++) {
        if (a[i].mov) {
          a[i].hidemov = false
          a[i].rowspan = 1
        }
      }
      for (i = 0; i < a.length - 1; i++) {
        if (!a[i].mov || a[i].hidemov) {
          continue
        }
        for (k = i + 1; k < a.length; k++) {
          if (a[i].mov === a[k].mov) {
            a[i].rowspan++
            a[k].rowspan = 0
            a[k].hidemov = true
          } else {
            i = k - 1
            break
          }
        }
      }

      // Marcar pares e impares
      var odd = false
      for (i = 0; i < a.length; i++) {
        if (a[i].mov && !a[i].hidemov) {
          odd = !odd
        }
        a[i].odd = odd
      }
      return a
    } catch (ex) {
      return []
    }
  },
  mostrarTexto: function (movdoc, doc, f) {
    for (var i = 0; i < movdoc.length; i++) {
      var md = movdoc[i]
      if (doc === md.doc) {
        for (var j = i; j >= 0; j--) {
          if (movdoc[j].mov) {
            break
          }
        }
        if (
          movdoc[j].rowspan &&
          j < md.length - 1 &&
          movdoc[j + 1].rowspan === undefined
        ) {
          movdoc[j].rowspan += f ? 1 : -1
        }
        doc.exibirTexto = f
        break
      }
    }
  },
  colocarLink: function (s) {
    if (s) s = s.replace(' e ', ', ')
    var a = s.split(', ')
    for (var i = 0; i < a.length; i++) {
      a[i] =
        '<a href="#/processo/' +
        a[i] +
        '" target="_blank">' +
        this.formatarProcesso(a[i]) +
        '</a>'
    }
    return a.join(', ')
  },
  formatarTexto: function (s) {
    if (s === undefined) {
      return s
    }
    return s
      .replace(/^\s\s*/, '')
      .replace(/\s\s*$/, '')
      .replace(/\n\s+\n/g, '<div class="break"></div>')
      .replace(/\n/g, '<br/>')
  },
  somenteNumeros: function (s) {
    if (s === undefined) return
    return s.replace(/\D/g, '')
  },
  regexFormatarProcesso: /^(\d{7})-?(\d{2})\.?(\d{4})\.?(4)\.?(02)\.?(\d{4})\/?-?(\d{2})?/,
  formatarProcesso: function (filename) {
    var m = this.regexFormatarProcesso.exec(filename)
    if (!m) return
    var s =
      m[1] + '-' + m[2] + '.' + m[3] + '.' + m[4] + '.' + m[5] + '.' + m[6]
    if (m[7]) s += '/' + m[7]
    return s
  },
  arrayToString: function (a) {
    if (!Array.isArray(a)) return a
    var str = a.join(', ')
    var n = str.lastIndexOf(', ')
    if (n >= 0) {
      str = str.substring(0, n) + ' e ' + str.substring(n + 2)
    }
    return str
  },
  arrayOfStringsToObjects: function (a, props) {
    if (a === undefined) return
    if (!Array.isArray(a)) a = [a]
    var r = []
    for (var i = 0; i < a.length; i++) {
      var aa = a[i].split('|')
      var o = {}
      for (var j = 0; j < aa.length; j++) {
        o[props[j]] = aa[j]
      }
      r.push(o)
    }
    return r
  }
}
