/** Web equivalent of ui/screens/hub/AudioHubScreen.kt. */
var Screens = window.Screens || {};

Screens.audioHub = async (data) => {
  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-md">
      <h1 class="font-headline-lg text-headline-lg text-on-surface">Audio</h1>
      ${UI.screenNavCard({ title: "Audio Lecciones", subtitle: "Episodio de la semana + tu propia práctica", iconName: "graphic_eq", href: "#/audio/lecciones" })}
      ${UI.screenNavCard({ title: "Biblioteca de Audios", subtitle: "13 episodios narrados, sin conexión", iconName: "library_music", href: "#/audio/biblioteca" })}
    </div>`;
  return { body };
};

window.Screens = Screens;
