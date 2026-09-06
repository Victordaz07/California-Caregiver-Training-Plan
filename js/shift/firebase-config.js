/**
 * Firebase project config for the "Modo Turno" module (patients, geofenced
 * check-in/out, hours, agency panel). See docs/FIREBASE_SETUP.md for how to
 * create the free project and get these values. Not secret: Firestore
 * security is enforced by firestore.rules, not by hiding this object.
 *
 * Until you paste your real project's values below, Modo Turno shows a
 * "not configured yet" message instead of a login form — the rest of the
 * app (90-day course) is unaffected either way.
 */
window.FIREBASE_CONFIG = {
  apiKey: "REEMPLAZA_CON_TU_API_KEY",
  authDomain: "REEMPLAZA_CON_TU_PROYECTO.firebaseapp.com",
  projectId: "REEMPLAZA_CON_TU_PROYECTO",
  storageBucket: "REEMPLAZA_CON_TU_PROYECTO.appspot.com",
  messagingSenderId: "REEMPLAZA_CON_TU_SENDER_ID",
  appId: "REEMPLAZA_CON_TU_APP_ID",
};
