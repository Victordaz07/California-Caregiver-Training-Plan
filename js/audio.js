/**
 * Web equivalents of audio/AudioNarrationController.kt (plays the 13 real
 * mp3s generated with Cloud Text-to-Speech) and audio/VoiceRecorderController.kt
 * (records the learner's own voice for practice). Uses the platform
 * <audio> element and the MediaRecorder Web API — no native app, no
 * server, nothing leaves the browser.
 *
 * Recordings live only in memory for this session (a Blob URL) — a
 * deliberate, honest simplification versus the Android app's persisted
 * file: this is a "listen back right after recording" practice tool, not
 * a personal audio archive.
 */
const Narration = (() => {
  let player = null;
  let currentEpisodeId = null;
  let onEndCallback = null;
  let onTimeCallback = null;
  let rate = 1.0;

  function play(episodeId, onEnd) {
    stop();
    player = new Audio(`assets/audio/${episodeId}.mp3`);
    player.playbackRate = rate;
    currentEpisodeId = episodeId;
    onEndCallback = onEnd;
    player.addEventListener("ended", () => {
      player = null;
      currentEpisodeId = null;
      if (onEndCallback) onEndCallback();
    });
    if (onTimeCallback) {
      player.addEventListener("timeupdate", () => onTimeCallback(player.currentTime, player.duration || 0));
    }
    player.play().catch((err) => {
      console.warn("No se pudo reproducir el audio:", err);
      if (onEndCallback) onEndCallback();
    });
  }

  /** Pause/resume the currently loaded episode without resetting position. */
  function togglePause() {
    if (!player) return null;
    if (player.paused) {
      player.play();
      return "playing";
    }
    player.pause();
    return "paused";
  }

  function seekBy(deltaSeconds) {
    if (!player) return;
    player.currentTime = Math.max(0, Math.min(player.duration || 0, player.currentTime + deltaSeconds));
  }

  function cycleRate() {
    const rates = [1.0, 1.25, 1.5, 0.75];
    const idx = rates.indexOf(rate);
    rate = rates[(idx + 1) % rates.length];
    if (player) player.playbackRate = rate;
    return rate;
  }

  function setOnTimeUpdate(cb) {
    onTimeCallback = cb;
  }

  function isPaused() {
    return !player || player.paused;
  }

  function stop() {
    if (player) {
      player.pause();
      player = null;
      currentEpisodeId = null;
    }
  }

  function isPlayingId(episodeId) {
    return currentEpisodeId === episodeId;
  }

  function currentRate() {
    return rate;
  }

  return { play, stop, isPlayingId, togglePause, seekBy, cycleRate, currentRate, setOnTimeUpdate, isPaused };
})();

const VoiceRecorder = (() => {
  let mediaRecorder = null;
  let chunks = [];
  let stream = null;
  let lastBlobUrl = null;

  async function start() {
    stream = await navigator.mediaDevices.getUserMedia({ audio: true });
    chunks = [];
    mediaRecorder = new MediaRecorder(stream);
    mediaRecorder.ondataavailable = (e) => chunks.push(e.data);
    mediaRecorder.start();
  }

  function stopStream() {
    if (stream) {
      stream.getTracks().forEach((t) => t.stop());
      stream = null;
    }
  }

  /** Resolves with a Blob URL for the recorded audio. */
  function stopRecording() {
    return new Promise((resolve, reject) => {
      if (!mediaRecorder) return reject(new Error("No hay grabación activa."));
      mediaRecorder.addEventListener("stop", () => {
        const blob = new Blob(chunks, { type: "audio/webm" });
        if (lastBlobUrl) URL.revokeObjectURL(lastBlobUrl);
        lastBlobUrl = URL.createObjectURL(blob);
        stopStream();
        resolve(lastBlobUrl);
      });
      mediaRecorder.stop();
    });
  }

  function discard() {
    if (lastBlobUrl) {
      URL.revokeObjectURL(lastBlobUrl);
      lastBlobUrl = null;
    }
  }

  function isSupported() {
    return !!(navigator.mediaDevices && window.MediaRecorder);
  }

  return { start, stopRecording, discard, isSupported };
})();

/**
 * Renders a record/play/delete card (web equivalent of
 * ui/components/VoiceRecordCard.kt) and wires its buttons. Call
 * `voiceRecordCardHtml(id)` to get the markup, insert it into the DOM, then
 * call `attachVoiceRecordCard(id, promptLabel)` once the element exists.
 */
function voiceRecordCardHtml(id) {
  return `<div id="${id}" class="bg-surface-container-lowest rounded-xl p-space-md shadow-sm flex flex-col gap-space-sm">
    <div class="flex items-center gap-space-xs">
      ${UI.icon("mic", "text-primary")}
      <h3 class="font-headline-sm text-headline-sm text-on-surface">Practica tu voz</h3>
    </div>
    <p class="font-body-sm text-body-sm text-outline" data-role="prompt"></p>
    <div data-role="rationale"></div>
    <div data-role="controls" class="flex items-center gap-space-sm"></div>
    <audio data-role="player" class="hidden" controls></audio>
  </div>`;
}

function attachVoiceRecordCard(containerId, promptLabel) {
  const root = document.getElementById(containerId);
  if (!root) return;
  root.querySelector('[data-role="prompt"]').textContent = promptLabel;

  const controls = root.querySelector('[data-role="controls"]');
  const audioEl = root.querySelector('[data-role="player"]');
  const rationaleEl = root.querySelector('[data-role="rationale"]');

  let state = "idle"; // idle | rationale | recording | recorded
  let blobUrl = null;

  function render() {
    if (!VoiceRecorder.isSupported()) {
      controls.innerHTML = `<p class="font-body-sm text-body-sm text-error">Tu navegador no permite grabar audio aquí.</p>`;
      return;
    }
    if (state === "idle") {
      rationaleEl.innerHTML = "";
      audioEl.classList.add("hidden");
      controls.innerHTML = `<button class="h-11 px-space-md bg-primary-container text-on-primary-container rounded-lg font-label-lg text-label-lg flex items-center gap-1.5" data-action="ask">${UI.icon("mic")}<span>Grabar</span></button>`;
    } else if (state === "rationale") {
      rationaleEl.innerHTML = `<div class="bg-surface-container rounded-lg p-space-sm text-body-sm text-on-surface-variant">Vamos a pedirte permiso de micrófono. Solo se graba tu propia voz, aquí en tu navegador — nada se sube a ningún servidor. Puedes borrar la grabación cuando quieras.</div>`;
      controls.innerHTML = `<button class="h-11 px-space-md bg-primary text-on-primary rounded-lg font-label-lg text-label-lg" data-action="start">Entiendo, permitir micrófono</button>`;
    } else if (state === "recording") {
      rationaleEl.innerHTML = "";
      controls.innerHTML = `<button class="h-11 px-space-md bg-error text-on-error rounded-lg font-label-lg text-label-lg flex items-center gap-1.5" data-action="stop">${UI.icon("stop")}<span>Detener</span></button>`;
    } else if (state === "recorded") {
      audioEl.src = blobUrl;
      audioEl.classList.remove("hidden");
      controls.innerHTML = `
        <button class="h-11 px-space-md bg-surface-container-high text-on-surface rounded-lg font-label-lg text-label-lg" data-action="retry">Grabar de nuevo</button>
        <button class="h-11 px-space-md bg-surface-container-high text-error rounded-lg font-label-lg text-label-lg" data-action="delete">Borrar</button>`;
    }
    wire();
  }

  function wire() {
    const ask = controls.querySelector('[data-action="ask"]');
    const start = controls.querySelector('[data-action="start"]');
    const stopBtn = controls.querySelector('[data-action="stop"]');
    const retry = controls.querySelector('[data-action="retry"]');
    const del = controls.querySelector('[data-action="delete"]');
    if (ask) ask.onclick = () => { state = "rationale"; render(); };
    if (start) start.onclick = async () => {
      try {
        await VoiceRecorder.start();
        state = "recording";
        render();
      } catch (err) {
        console.warn("Permiso de micrófono denegado o no disponible.", err);
        state = "idle";
        render();
      }
    };
    if (stopBtn) stopBtn.onclick = async () => {
      blobUrl = await VoiceRecorder.stopRecording();
      state = "recorded";
      AppState.incrementRecordingsCount();
      render();
    };
    if (retry) retry.onclick = () => { VoiceRecorder.discard(); state = "rationale"; render(); };
    if (del) del.onclick = () => { VoiceRecorder.discard(); blobUrl = null; state = "idle"; render(); };
  }

  render();
}
