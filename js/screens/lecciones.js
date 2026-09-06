/**
 * Web equivalent of ui/screens/AudioLeccionesScreen.kt. Visual language:
 * "Caregiver Pro v2" redesign — full dark-teal player card with an
 * animated waveform, real ±10s/speed/pause controls, key-phrase card, and
 * real self-recording below.
 */
var Screens = window.Screens || {};

const WAVE_COLORS = ["coral", "coral", "coral", "coral2", "coral2", "mint", "mint", "mint", "dim", "dim", "dim", "dim", "dim"];

function formatTime(sec) {
  if (!isFinite(sec) || sec < 0) sec = 0;
  const m = Math.floor(sec / 60);
  const s = Math.floor(sec % 60);
  return `${m}:${String(s).padStart(2, "0")}`;
}

function playerCardHtml(episode, idPrefix) {
  const bars = WAVE_COLORS.map(
    (c, i) =>
      `<div class="flex-1 h-full rounded-full origin-bottom" data-wave-bar style="background:${c === "coral" ? "var(--coral)" : c === "coral2" ? "#F08A7E" : c === "mint" ? "var(--mint)" : "rgba(255,255,255,.28)"}; animation-delay:${(i * 0.09).toFixed(2)}s"></div>`,
  ).join("");
  return `
  <div id="${idPrefix}" class="rounded-3xl p-space-md flex flex-col gap-space-md" style="background:var(--teal-dark)">
    <div class="flex items-center justify-between">
      <span class="text-xs font-extrabold tracking-wide" style="color:rgba(255,255,255,.7)">SEMANA ${episode.week}</span>
      <span style="color:rgba(255,255,255,.7)">${UI.icon("headphones")}</span>
    </div>
    <div>
      <div class="font-display text-2xl text-white leading-tight">${UI.escapeHtml(episode.titleEs)}</div>
      <div class="text-sm font-bold mt-1" style="color:var(--mint)">Narración bilingüe · ${episode.targetMinutes} min · sin conexión</div>
    </div>
    <div class="flex items-end gap-1 h-12" data-waveform>${bars}</div>
    <div class="flex justify-between text-xs font-extrabold" style="color:rgba(255,255,255,.6); margin-top:-8px">
      <span data-time-current>0:00</span><span data-time-total>${formatTime(0)}</span>
    </div>
    <div class="flex items-center justify-between">
      <button data-action="rate" class="w-11 h-11 rounded-xl text-white text-xs font-extrabold flex items-center justify-center" style="background:rgba(255,255,255,.12)">1.0x</button>
      <button data-action="back10" class="text-white">${UI.icon("replay_10", "text-3xl")}</button>
      <button data-action="toggle" class="w-[74px] h-[74px] rounded-full text-white flex items-center justify-center" style="background:var(--coral); box-shadow:0 6px 0 var(--coral-dark)">${UI.icon("play_arrow", "text-4xl")}</button>
      <button data-action="fwd10" class="text-white">${UI.icon("forward_10", "text-3xl")}</button>
      <button class="w-11 h-11 rounded-xl text-white flex items-center justify-center" style="background:rgba(255,255,255,.12)">${UI.icon("subtitles")}</button>
    </div>
    <div class="rounded-2xl p-space-sm flex flex-col gap-1.5" style="background:var(--cream)">
      <div class="flex items-center gap-2">
        ${UI.icon("translate", "text-primary")}<span class="font-display text-sm text-primary tracking-wide">FRASE CLAVE</span>
      </div>
      <div class="text-lg font-extrabold text-on-surface leading-tight">"${UI.escapeHtml(episode.keyPhraseEn)}"</div>
      <div class="font-hand text-xl text-outline">${UI.escapeHtml(episode.pronunciationEs)}</div>
    </div>
  </div>`;
}

function attachPlayerCard(idPrefix, episodeId, initialDurationHint) {
  const root = document.getElementById(idPrefix);
  const toggleBtn = root.querySelector('[data-action="toggle"]');
  const rateBtn = root.querySelector('[data-action="rate"]');
  const waveform = root.querySelector("[data-waveform]");
  const timeCurrent = root.querySelector("[data-time-current]");
  const timeTotal = root.querySelector("[data-time-total]");
  let started = false;

  function setPlayingVisual(playing) {
    toggleBtn.innerHTML = UI.icon(playing ? "pause" : "play_arrow", "text-4xl");
    waveform.querySelectorAll("[data-wave-bar]").forEach((bar) => {
      bar.style.animation = playing ? "omWave 1.1s ease-in-out infinite" : "none";
      bar.style.animationDelay = bar.style.animationDelay || "0s";
    });
  }

  toggleBtn.onclick = () => {
    if (!started) {
      started = true;
      Narration.setOnTimeUpdate((cur, dur) => {
        timeCurrent.textContent = formatTime(cur);
        if (dur) timeTotal.textContent = formatTime(dur);
      });
      Narration.play(episodeId, () => {
        setPlayingVisual(false);
        AppState.markAudioListened(episodeId);
      });
      setPlayingVisual(true);
      return;
    }
    const state = Narration.togglePause();
    setPlayingVisual(state === "playing");
  };
  root.querySelector('[data-action="back10"]').onclick = () => Narration.seekBy(-10);
  root.querySelector('[data-action="fwd10"]').onclick = () => Narration.seekBy(10);
  rateBtn.onclick = () => {
    const r = Narration.cycleRate();
    rateBtn.textContent = `${r}x`;
  };
}

Screens.lecciones = async (data) => {
  const state = AppState.get();
  const currentDay = DataStore.curriculumDayFor(data, state.currentPlanDay);
  const currentWeek = currentDay ? currentDay.week : 1;
  const episode = DataStore.audioEpisodeFor(data, currentWeek) || data.audio_episodes[0];

  function episodeRow(ep) {
    return `<div class="flex items-center justify-between bg-white border-2 border-surface-container-high rounded-xl p-space-sm">
      <div class="min-w-0 flex-1">
        <p class="font-body-md text-body-md text-on-surface truncate">Semana ${ep.week} · ${UI.escapeHtml(ep.titleEs)}</p>
        <p class="font-body-sm text-body-sm text-outline truncate">"${UI.escapeHtml(ep.keyPhraseEn)}"</p>
      </div>
      <button data-play="${ep.episodeId}" class="w-11 h-11 flex-shrink-0 flex items-center justify-center rounded-full text-secondary">${UI.icon("play_circle")}</button>
      ${UI.statusPill(`${ep.targetMinutes} min`)}
    </div>`;
  }

  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg">
      ${playerCardHtml(episode, "player-today")}
      <div id="vrc"></div>
      <h3 class="font-headline-md text-headline-md uppercase text-on-surface">Biblioteca de Guiones · 13 Semanas</h3>
      <div class="flex flex-col gap-1.5">
        ${data.audio_episodes.map(episodeRow).join("")}
      </div>
    </div>`;

  return {
    body,
    afterRender: () => {
      Narration.stop();
      attachPlayerCard("player-today", episode.episodeId);
      document.getElementById("vrc").innerHTML = voiceRecordCardHtml("vrc-lecciones");
      attachVoiceRecordCard("vrc-lecciones", "Escucha la narración y luego grábate repitiendo la frase clave de esta semana.");

      document.querySelectorAll("[data-play]:not([data-action])").forEach((btn) => {
        btn.onclick = () => {
          const id = btn.dataset.play;
          if (Narration.isPlayingId(id)) {
            Narration.stop();
            btn.innerHTML = UI.icon("play_circle");
          } else {
            document.querySelectorAll("[data-play]").forEach((b) => (b.innerHTML = UI.icon("play_circle")));
            btn.innerHTML = UI.icon("stop_circle");
            Narration.play(id, () => {
              btn.innerHTML = UI.icon("play_circle");
              AppState.markAudioListened(id);
            });
          }
        };
      });
    },
  };
};

window.Screens = Screens;
