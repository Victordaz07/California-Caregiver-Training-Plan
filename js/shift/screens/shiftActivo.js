/**
 * Turno Activo — screen 14 in Caregiver Pro v3.dc.html. Live countdown
 * against the checked-in shift's real checkIn.at timestamp (same
 * setInterval-tick pattern as js/screens/hoy.js's daily-session timer).
 */
var Screens = window.Screens || {};

function formatHms(totalSeconds) {
  const h = Math.floor(totalSeconds / 3600);
  const m = Math.floor((totalSeconds % 3600) / 60);
  const s = Math.floor(totalSeconds % 60);
  return `${h}:${String(m).padStart(2, "0")}:${String(s).padStart(2, "0")}`;
}

Screens.shiftActivo = async (data, params) => {
  if (!FirebaseApp.isConfigured()) return { body: shiftNotConfiguredBody() };
  const user = AuthStore.get();
  const shift = await ShiftStore.getShift(params.shiftId);
  if (!shift) return { body: `<div class="p-8 text-center text-outline">No se encontró este turno.</div>` };
  const patient = await ShiftStore.getPatient(user.agencyId, shift.patientId);
  const tasks = patient?.tasks || [];
  const completedSet = new Set(shift.tasksCompleted || []);

  const scheduledMs = Math.max(1, shift.scheduledEnd - shift.scheduledStart);

  const tasksHtml = tasks
    .map((t, i) => {
      const taskId = `t${i}`;
      const done = completedSet.has(taskId);
      return `<label class="flex gap-3 items-center bg-white border-2 rounded-2xl px-3 py-3 cursor-pointer" style="border-color:${done ? "#B7E4C9" : "var(--surface-container-high, #E4EFF2)"}; background:${done ? "#F1FBF5" : "#fff"}">
        <input type="checkbox" data-task-id="${taskId}" class="task-checkbox w-6 h-6 rounded-lg" ${done ? "checked" : ""}/>
        <span class="flex-1 font-body-md text-body-md font-extrabold ${done ? "" : "text-on-surface"}" style="${done ? "color:#2F7A52; text-decoration:line-through" : ""}">${UI.escapeHtml(t.label)}</span>
        <span class="text-outline">${UI.icon(t.icon || "task_alt")}</span>
      </label>`;
    })
    .join("");

  const body = `
    <div class="bg-primary rounded-b-3xl px-margin-mobile pt-space-xl pb-space-lg flex flex-col items-center gap-space-sm text-center">
      <span class="flex items-center gap-1.5 text-[10px] font-black tracking-wide px-3 py-1.5 rounded-full" style="background:rgba(47,158,95,.22); color:#8FE3B4">${UI.icon("radio_button_checked", "text-sm")}TURNO ACTIVO · ${UI.escapeHtml((patient?.name || "").toUpperCase())}</span>
      <div>
        <div id="shift-timer" class="font-display text-5xl text-white leading-none tracking-wide">0:00:00</div>
        <div class="text-xs font-black tracking-wide mt-1.5" style="color:var(--mint)">ENTRASTE ${new Date(shift.checkIn.at).toLocaleTimeString("es-ES", { hour: "2-digit", minute: "2-digit" })}</div>
      </div>
      <div class="w-full h-2.5 rounded-full overflow-hidden" style="background:rgba(255,255,255,.16)"><div id="shift-progress" class="h-full rounded-full" style="width:0%; background:var(--coral)"></div></div>
    </div>

    <div class="px-margin-mobile pt-space-md flex flex-col gap-space-sm pb-space-2xl">
      <div class="flex justify-between items-baseline">
        <span class="font-display text-base tracking-wide text-primary">TAREAS DE HOY</span>
        <span id="tasks-done-label" class="font-body-sm text-body-sm font-extrabold text-secondary"></span>
      </div>
      ${tasksHtml || `<p class="font-body-sm text-body-sm text-outline">Sin tareas asignadas para este paciente.</p>`}

      <div class="flex gap-2.5 items-start rounded-2xl p-space-sm mt-space-sm" style="background:var(--surface-container, #F1F5F6)">
        ${UI.icon("wifi_off", "text-outline flex-shrink-0")}
        <p class="font-body-sm text-body-sm text-outline">Sin señal, el turno sigue contando. Se sincroniza cuando haya internet.</p>
      </div>
    </div>

    <div class="fixed bottom-0 left-0 right-0 border-t border-surface-container-high bg-white px-margin-mobile py-space-sm pb-safe">
      <div id="btn-salida" class="text-center font-display text-lg tracking-wide py-space-md rounded-2xl text-white cursor-pointer" style="background:var(--coral-dark)">REGISTRAR SALIDA</div>
    </div>`;

  return {
    body,
    afterRender: () => {
      function refreshDoneLabel() {
        const checked = document.querySelectorAll(".task-checkbox:checked").length;
        document.getElementById("tasks-done-label").textContent = tasks.length ? `${checked} de ${tasks.length} hechas` : "";
      }
      refreshDoneLabel();

      document.querySelectorAll(".task-checkbox").forEach((cb) => {
        cb.onchange = async () => {
          try {
            await ShiftStore.toggleShiftTask(shift.id, cb.dataset.taskId, cb.checked);
            refreshDoneLabel();
          } catch (err) {
            cb.checked = !cb.checked;
            alert("No se pudo guardar: " + err.message);
          }
        };
      });

      const timerEl = document.getElementById("shift-timer");
      const progressEl = document.getElementById("shift-progress");
      function tick() {
        const elapsedMs = Date.now() - shift.checkIn.at;
        timerEl.textContent = formatHms(elapsedMs / 1000);
        progressEl.style.width = Math.min(100, Math.round((elapsedMs / scheduledMs) * 100)) + "%";
      }
      tick();
      const intervalId = setInterval(tick, 1000);
      window.addEventListener("hashchange", () => clearInterval(intervalId), { once: true });

      document.getElementById("btn-salida").onclick = () => {
        window.location.hash = `#/shift/salida/${shift.id}`;
      };
    },
  };
};

window.Screens = Screens;
