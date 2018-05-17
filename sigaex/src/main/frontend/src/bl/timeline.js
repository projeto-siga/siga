import UtilsBL from './utils.js'

const DIA_EM_MINUTOS = 60 * 24

export default {
  emptyTimeline: function () {
    return JSON.parse(
      JSON.stringify({
        autuacao: {
          passou: true
        },
        distribuicao: {},
        primeirodespacho: {},
        intimacao: {},
        remessa: {},
        devolucao: {},
        juntada: {},
        audiencia: {},
        conclusao: {},
        sentenca: {
          texto: '-'
        },
        suspensao: {},
        apelacao: {
          texto: '-'
        },
        devolucaoapelacao: {},
        baixa: {},
        execucao: {},
        arquivamento: {}
      })
    )
  },

  updateTimeline: function (orgao, movdoc, calcularTempos) {
    var contains = function (m, a) {
      return a.indexOf(m.movimentoLocal.codigoMovimento) !== -1
    }
    var timeline = this.emptyTimeline()
    var movs = movdoc
    var prev
    var fApelacao = false

    timeline.sentenca.texto = orgao === 'TRF2' ? 'Inteiro Teor' : 'Sentença'
    timeline.apelacao.texto = orgao === 'TRF2' ? undefined : 'TRF2'

    var e
    var hora, ultHora
    for (var i = movs.length - 1; i >= 0; i--) {
      var m = movs[i].mov
      e = undefined
      if (m === undefined || !m.movimentoLocal) continue
      if (
        contains(m, [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 60, 61, 62, 63, 80, 81, 83])
      ) {
        e = timeline.distribuicao
      }
      if (contains(m, [11, 76])) {
        if (m.complemento[0] === 'Despacho' || m.complemento[0] === 'Decisão') {
          e = timeline.conclusao
        } else if (
          m.complemento[0] === 'Sentença' ||
          m.complemento[0] === 'Sentença/Julgamento'
        ) {
          e = timeline.sentenca
        }
      }
      if (
        contains(m, [78]) // Inteiro Teor
      ) {
        e = timeline.sentenca
      }
      if (contains(m, [12])) e = timeline.intimacao
      if (contains(m, [14])) {
        if (m.complemento && m.complemento[0] === 'TRF - 2ª Região') {
          e = timeline.apelacao
          fApelacao = true
        } else e = timeline.remessa
      }
      if (contains(m, [15])) {
        if (fApelacao) {
          e = timeline.devolucaoapelacao
          fApelacao = false
        } else e = timeline.devolucao
      }
      if (contains(m, [27])) e = timeline.juntada
      if (contains(m, [101])) e = timeline.suspensao
      if (contains(m, [19])) e = timeline.audiencia
      if (contains(m, [18])) e = timeline.execucao
      if (contains(m, [26])) e = timeline.baixa
      if (e) {
        if (calcularTempos) {
          if (!e.tempo) e.tempo = 0
          hora = UtilsBL.convertString2DateYYYYMMDDHHMM(m.dataHora)
          if (ultHora) {
            var dif = (hora - ultHora) / 1000 / 60
            e.tempo += dif
          }
          ultHora = hora
        }

        for (var key in timeline) {
          if (timeline.hasOwnProperty(key) && e === timeline[key]) {
            m.tipo = '#' + key
          }
        }
        e.passou = true
        if (e.contador) e.contador += 1
        else e.contador = 1
        if (prev) {
          prev.esta = false

          // delete prev.complemento;
        }
        e.esta = true
        e.dataHora = m.dataHora
        if (m.complemento) {
          e.complemento = []
          if (m.complemento && m.complemento.length > 0) {
            e.complemento[0] = UtilsBL.trunc(m.complemento[0], 30, true)
          }
          if (m.complemento && m.complemento.length > 1) {
            e.complemento[1] = UtilsBL.trunc(m.complemento[1], 30, true)
          }
          if (
            m.complemento &&
            m.complemento.length > 1 &&
            e.complemento[1] === 'Registro no Sistema'
          ) {
            delete e.complemento[1]
          }
        }
        prev = e
      }
      if (
        e === timeline.conclusao ||
        e === timeline.sentenca ||
        e === timeline.apelacao ||
        e === timeline.execucao ||
        e === timeline.baixa
      ) {
        for (var k in timeline) {
          if (timeline.hasOwnProperty(k)) delete timeline[k].complemento
        }
      }

      // if (e === timeline.devolucaoapelacao) break;
    }

    // Calcula tempo médio e cores
    if (calcularTempos) {
      var ti, tempoAcumulado, perc
      tempoAcumulado = 0
      for (k in timeline) {
        if (!timeline.hasOwnProperty(k)) continue
        if (!timeline[k].contador) continue
        timeline[k].tempoMedio = timeline[k].tempo / timeline[k].contador
        tempoAcumulado += timeline[k].tempoMedio
      }
      for (k in timeline) {
        if (!timeline.hasOwnProperty(k)) continue
        ti = timeline[k]
        if (!ti.contador) continue
        perc = ti.tempoMedio / tempoAcumulado
        ti.transito = 'verde'
        if (ti.tempo > 15 * DIA_EM_MINUTOS && perc > 0.3) {
          ti.transito = 'laranja'
        }
        if (ti.tempo > 30 * DIA_EM_MINUTOS && perc > 0.5) {
          ti.transito = 'vermelho'
        }
        if (ti.tempo > 60 * DIA_EM_MINUTOS && perc > 0.8) ti.transito = 'vinho'
      }
    }
    console.log(timeline)
    return timeline
  }
}
