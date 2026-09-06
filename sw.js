const CACHE_VERSION = "caregiver-pro-ca-v2";
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
 * online). Everything else (js/*, data/*.json, assets/*) uses
 * stale-while-revalidate: respond from cache immediately when present (so
 * the app stays instant and works fully offline), but always also fetch in
 * the background and update the cache for next time — a pure cache-first
 * strategy here previously meant a deploy's new JS/CSS/JSON was NEVER
 * picked up by an already-installed PWA until someone bumped CACHE_VERSION
 * by hand or the user manually cleared site data (a real bug hit in
 * testing: a code fix shipped days earlier still wasn't visible on a
 * device that had opened the app before that fix existed).
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
      const networkFetch = fetch(req)
        .then((res) => {
          if (res.ok) {
            const copy = res.clone();
            caches.open(CACHE_VERSION).then((cache) => cache.put(req, copy));
          }
          return res;
        })
        .catch(() => cached);
      // Keep the background refresh alive even after we've already
      // responded from cache below — respondWith() alone doesn't do that.
      event.waitUntil(networkFetch);
      return cached || networkFetch;
    }),
  );
});
