/** Web equivalent of ui/screens/hub/PracticeHubScreen.kt. */
var Screens = window.Screens || {};

Screens.practicaHub = async (data) => {
  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-md">
      ${UI.brandScene(data, "practice")}
      <h1 class="font-headline-lg text-headline-lg text-on-surface">Práctica</h1>
      ${UI.screenNavCard({ title: "Escenarios y Flashcards", subtitle: "Repaso espaciado y casos de decisión", iconName: "quiz", href: "#/practica/escenarios" })}
      ${UI.screenNavCard({ title: "Role Play Bilingüe", subtitle: "Guiones de práctica en español e inglés", iconName: "record_voice_over", href: "#/practica/roleplay" })}
      ${UI.screenNavCard({ title: "Simulación de Turno", subtitle: "Entrega de turno y autorevisión", iconName: "assignment", href: "#/practica/turno" })}
    </div>`;
  return { body };
};

window.Screens = Screens;
