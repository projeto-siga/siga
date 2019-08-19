/**
 * Neste arquivo são configuradas as informações do service worker, como o que será feito cache
 * e até mesmo as informações install da aplicação.
 */
var dataCacheName = 'SigadocPWA';
var cacheName = 'SigaDoc';
var filesToCache = [
  '/siga',
  '/siga.base.url',
  '/sigaex/app/expediente/doc/editar',
  '/sigaex/app/expediente/doc/listar?primeiraVez=sim',
  '/sigaex/app/mesa',
  '/sigaex/app/expediente/mov/transferir_lote',
  '/sigaex/app/expediente/mov/receber_lote',
  '/sigaex/app/expediente/mov/anotar_lote',
  '/sigaex/app/expediente/mov/assinar_tudo',
  '/sigaex/app/expediente/mov/assinar_lote',
  '/sigaex/app/expediente/mov/assinar_despacho_lote',
  '/sigaex/app/expediente/mov/arquivar_corrente_lote',
  '/sigaex/app/expediente/mov/arquivar_intermediario_lote',
  '/sigaex/app/expediente/mov/arquivar_permanente_lote',
  '/sigaex/app/expediente/mov/atender_pedido_publicacao',
  '/sigaex/app/expediente/configuracao/gerenciar_publicacao_boletim'
];

self.addEventListener('install', function(e) {
  console.log('[ServiceWorker] Install');
  e.waitUntil(
    caches.open(cacheName).then(function(cache) {
      console.log('[ServiceWorker] Caching app shell');
      return cache.addAll(filesToCache);
    })
  );
});

self.addEventListener('activate', function(e) {
  console.log('[ServiceWorker] Activate');
  e.waitUntil(
    caches.keys().then(function(keyList) {
      return Promise.all(keyList.map(function(key) {
        if (key !== cacheName && key !== dataCacheName) {
          console.log('[ServiceWorker] Removing old cache', key);
          return caches.delete(key);
        }
      }));
    })
  );
  return self.clients.claim();
});

self.addEventListener('fetch', function(e) {
  var dataUrl = 'https://www.sigasp.homologacao.sp.gov.br';
  if (e.request.url.indexOf(dataUrl) > -1) {
    e.respondWith(
      caches.open(dataCacheName).then(function(cache) {
        return fetch(e.request).then(function(response){
          cache.put(e.request.url, response.clone());
          return response;
        });
      })
    );
  } else {
    e.respondWith(
      caches.match(e.request).then(function(response) {
        return response || fetch(e.request);
      })
    );
  }
});
