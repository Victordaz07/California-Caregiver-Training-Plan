/**
 * Thin wrapper around the Firebase compat SDK (loaded via CDN script tags
 * in index.html — no bundler, consistent with the rest of this project).
 * Fails soft: if js/shift/firebase-config.js still has placeholder values,
 * isConfigured() returns false and every Modo Turno screen shows a setup
 * notice instead of crashing.
 */
var FirebaseApp = (() => {
  let app = null;
  let auth = null;
  let db = null;
  let configured = false;

  function looksConfigured(cfg) {
    return !!cfg && typeof cfg.apiKey === "string" && !cfg.apiKey.startsWith("REEMPLAZA_");
  }

  function init() {
    if (app) return;
    if (!looksConfigured(window.FIREBASE_CONFIG)) {
      configured = false;
      return;
    }
    app = firebase.initializeApp(window.FIREBASE_CONFIG);
    auth = firebase.auth();
    db = firebase.firestore();
    configured = true;
  }

  return {
    init,
    isConfigured: () => configured,
    get auth() {
      return auth;
    },
    get db() {
      return db;
    },
  };
})();
window.FirebaseApp = FirebaseApp;
