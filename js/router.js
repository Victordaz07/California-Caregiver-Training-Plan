/**
 * Minimal hash router. Each entry maps a path to a render function
 * (from js/screens/*.js) plus whether it's a "hub" (bottom-nav tab root,
 * no back arrow — like ui/components/HubScaffold.kt) or a "detail" screen
 * (back arrow, no bottom nav — like ui/components/DetailScaffold.kt).
 */
const TRAINING_TABS = [
  { id: "hoy", path: "#/hoy", icon: "today", label: "Hoy" },
  { id: "plan", path: "#/plan", icon: "calendar_month", label: "Plan" },
  { id: "practica", path: "#/practica", icon: "school", label: "Práctica" },
  { id: "audio", path: "#/audio", icon: "headphones", label: "Audio" },
  { id: "carrera", path: "#/carrera", icon: "work", label: "Carrera" },
];

// Bottom nav for Modo Turno (js/shift/*) — a separate on-shift assistant
// module (patients, geofenced check-in/out, hours, agency panel), reached
// from Carrera > Perfil. Not to be confused with #/practica/turno, the
// unrelated "Simulación de Turno" practice screen in the 90-day course.
const SHIFT_TABS = [
  { id: "turno", path: "#/shift", icon: "work_history", label: "Turno" },
  { id: "ayuda", path: "#/shift/ayuda", icon: "help_center", label: "Ayuda" },
  { id: "aprender", path: "#/practica", icon: "school", label: "Aprender" },
  { id: "horas", path: "#/shift/horas", icon: "schedule", label: "Horas" },
  { id: "constancia", path: "#/shift/constancia", icon: "workspace_premium", label: "Constancia" },
];

const ROUTES = {
  "#/welcome": { kind: "onboarding", render: (data) => Screens.welcome(data) },
  "#/pathway": { kind: "onboarding", render: (data) => Screens.pathwaySelection(data) },

  "#/hoy": { kind: "hub", navGroup: "training", tab: "hoy", noHeader: true, render: (data) => Screens.hoy(data) },
  "#/plan": { kind: "hub", navGroup: "training", tab: "plan", noHeader: true, render: (data) => Screens.plan(data) },
  "#/practica": { kind: "hub", navGroup: "training", tab: "practica", render: (data) => Screens.practicaHub(data) },
  "#/audio": { kind: "hub", navGroup: "training", tab: "audio", render: (data) => Screens.audioHub(data) },
  "#/carrera": { kind: "hub", navGroup: "training", tab: "carrera", render: (data) => Screens.carreraHub(data) },

  "#/practica/escenarios": { kind: "detail", back: "#/practica", title: "Escenarios y Flashcards", render: (data) => Screens.escenarios(data) },
  "#/practica/roleplay": { kind: "detail", back: "#/practica", title: "Role Play Bilingüe", render: (data) => Screens.roleplay(data) },
  "#/practica/turno": { kind: "detail", back: "#/practica", noHeader: true, render: (data) => Screens.turno(data) },

  "#/audio/lecciones": { kind: "detail", back: "#/audio", noHeader: true, render: (data) => Screens.lecciones(data) },
  "#/audio/biblioteca": { kind: "detail", back: "#/audio", title: "Biblioteca de Audios", render: (data) => Screens.biblioteca(data) },

  "#/carrera/requisitos": { kind: "detail", back: "#/carrera", title: "Requisitos y Certificación", render: (data) => Screens.requisitos(data) },
  "#/carrera/recursos": { kind: "detail", back: "#/carrera", title: "Recursos por Semana", render: (data) => Screens.recursos(data) },
  "#/carrera/vocacional": { kind: "detail", back: "#/carrera", title: "Analizador Vocacional", render: (data) => Screens.vocacional(data) },
  "#/carrera/progreso": { kind: "detail", back: "#/carrera", noHeader: true, render: (data) => Screens.progreso(data) },
  "#/carrera/perfil": { kind: "detail", back: "#/carrera", noHeader: true, render: (data) => Screens.perfil(data) },

  // Modo Turno (js/shift/*): separate account system (Firebase), see
  // docs/FIREBASE_SETUP.md. "shift-auth" screens have no header/bottomnav.
  "#/shift/login": { kind: "shift-auth", render: (data) => Screens.shiftLogin(data) },
  "#/shift/signup": { kind: "shift-auth", render: (data) => Screens.shiftSignup(data) },
  "#/shift": { kind: "hub", navGroup: "shift", tab: "turno", noHeader: true, render: (data) => Screens.shiftHub(data) },
};

function currentPath() {
  return window.location.hash || "#/hoy";
}
