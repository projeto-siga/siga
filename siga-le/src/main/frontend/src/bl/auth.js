import Vue from 'vue'
import decode from 'jwt-decode'

const ID_TOKEN_KEY = 'bv-jwt'

export default {
  logout: function () {
    this.clearIdToken()
  },

  getIdToken: function () {
    return localStorage.getItem(ID_TOKEN_KEY)
  },

  clearIdToken: function () {
    localStorage.removeItem(ID_TOKEN_KEY)
    Vue.http.headers.common['Authorization'] = undefined
  },

  // Get and store id_token in local storage
  setIdToken: function (idToken) {
    localStorage.setItem(ID_TOKEN_KEY, idToken)
    Vue.http.headers.common['Authorization'] = 'Bearer ' + idToken
  },

  isLoggedIn: function () {
    const idToken = this.getIdToken()
    return !!idToken && !this.isTokenExpired(idToken)
  },

  decodeToken: function (encodedToken) {
    var decoded = decode(encodedToken)
    if (decoded && decoded.users) {
      var a = decoded.users.split(';')
      decoded.parsedUsers = {}
      for (var i = 0; i < a.length; i++) {
        var b = a[i].split(',')
        decoded.parsedUsers[b[0]] = {
          ieusu: b[1],
          ieunidade: b[2],
          perfil: b[3]
        }
      }
    }
    if (decoded.email)
      decoded.company = decoded.email !== null ? decoded.email.split('@')[1] : undefined
    return decoded
  },

  getTokenExpirationDate: function (encodedToken) {
    const token = decode(encodedToken)
    if (!token.exp) {
      return null
    }

    const date = new Date(0)
    date.setUTCSeconds(token.exp)

    return date
  },

  isTokenExpired: function (token) {
    const expirationDate = this.getTokenExpirationDate(token)
    return expirationDate < new Date()
  }
}