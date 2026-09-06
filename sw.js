const CACHE_VERSION = "caregiver-pro-ca-v1";
const APP_SHELL = [
  "/",
  "/index.html",
  "/manifest.json",
  "/css/app.css",
  "/js/app.js",
  "/js/data.js",
  "/js/state.js",
  "/js/scheduler.js",
  "/js/router.js",
  "/assets/logo.svg",
  "/assets/icons/icon-192.png",
  "/assets/icons/icon-512.png",
];

self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_VERSION).then((cache) => cache.addAll(APP_SHELL)).catch(() => {}),
  );
  self.skipWaiting();
});

self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(keys.filter((k) => k !== CACHE_VERSION).map((k) => caches.delete(k))),
    ),
  );
  self.clients.claim();
});

/**
 * Strategy: HTML shell uses network-first (so a redeploy is picked up when
 * online), everything else (data/*.json, assets/audio/*.mp3, screens/*)
 * uses cache-first with a background network fill — once visited/loaded
 * once, it works fully offline, matching the "13 semanas offline" promise
 * this app already makes.
 */
self.addEventListener("fetch", (event) => {
  const req = event.request;
  if (req.method !== "GET") return;

  const url = new URL(req.url);
  const isHtmlShell = url.pathname === "/" || url.pathname.endsWith(".html");

  if (isHtmlShell) {
    event.respondWith(
      fetch(req)
        .then((res) => {
          const copy = res.clone();
          caches.open(CACHE_VERSION).then((cache) => cache.put(req, copy));
          return res;
        })
        .catch(() => caches.match(req)),
    );
    return;
  }

  event.respondWith(
    caches.match(req).then((cached) => {
      if (cached) return cached;
      return fetch(req).then((res) => {
        if (res.ok) {
          const copy = res.clone();
          caches.open(CACHE_VERSION).then((cache) => cache.put(req, copy));
        }
        return res;
      });
    }),
  );
});
