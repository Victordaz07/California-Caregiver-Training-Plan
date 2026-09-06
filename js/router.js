/**
 * Minimal hash router. Each entry maps a path to a render function
 * (from js/screens/*.js) plus whether it's a "hub" (bottom-nav tab root,
 * no back arrow — like ui/components/HubScaffold.kt) or a "detail" screen
 * (back arrow, no bottom nav — like ui/components/DetailScaffold.kt).
 */
const TABS = [
  { id: "hoy", path: "#/hoy", icon: "today", label: "Hoy" },
  { id: "plan", path: "#/plan", icon: "calendar_month", label: "Plan" },
  { id: "practica", path: "#/practica", icon: "school", label: "Práctica" },
  { id: "audio", path: "#/audio", icon: "headphones", label: "Audio" },
  { id: "carrera", path: "#/carrera", icon: "work", label: "Carrera" },
];

const ROUTES = {
  "#/welcome": { kind: "onboarding", render: (data) => Screens.welcome(data) },
  "#/pathway": { kind: "onboarding", render: (data) => Screens.pathwaySelection(data) },

  "#/hoy": { kind: "hub", tab: "hoy", noHeader: true, render: (data) => Screens.hoy(data) },
  "#/plan": { kind: "hub", tab: "plan", noHeader: true, render: (data) => Screens.plan(data) },
  "#/practica": { kind: "hub", tab: "practica", render: (data) => Screens.practicaHub(data) },
  "#/audio": { kind: "hub", tab: "audio", render: (data) => Screens.audioHub(data) },
  "#/carrera": { kind: "hub", tab: "carrera", render: (data) => Screens.carreraHub(data) },

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
};

function currentPath() {
  return window.location.hash || "#/hoy";
}
