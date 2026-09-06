/**
 * Shared render helpers — the web equivalent of ui/components/*.kt
 * (SectionCard, StatusPill, DisclaimerBanner, ChecklistRow, NumberedStep).
 * Each function returns an HTML string; screens assemble these into a page.
 */
const UI = (() => {
  function escapeHtml(str) {
    return String(str)
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;");
  }

  function icon(name, extraClass = "") {
    return `<span class="material-symbols-outlined ${extraClass}">${name}</span>`;
  }

  function sectionCard(innerHtml, { bg = "bg-surface-container-lowest" } = {}) {
    return `<div class="${bg} rounded-xl p-space-md shadow-sm flex flex-col gap-space-sm">${innerHtml}</div>`;
  }

  function statusPill(text, { bg = "bg-secondary-fixed", color = "text-on-secondary-fixed-variant" } = {}) {
    return `<span class="inline-flex items-center px-2 py-0.5 rounded-full ${bg} ${color} font-label-sm text-label-sm uppercase tracking-wide">${escapeHtml(text)}</span>`;
  }

  const DISCLAIMER_STYLES = {
    General: { icon: "info", bg: "bg-surface-container", color: "text-on-surface-variant" },
    Emergency: { icon: "emergency", bg: "bg-error-container", color: "text-on-error-container" },
    Scope: { icon: "fact_check", bg: "bg-tertiary-container/20", color: "text-on-surface" },
    FictionalCase: { icon: "theater_comedy", bg: "bg-surface-container", color: "text-on-surface-variant" },
    HandsOn: { icon: "front_hand", bg: "bg-secondary-fixed/50", color: "text-on-secondary-fixed-variant" },
    Certificate: { icon: "workspace_premium", bg: "bg-surface-container", color: "text-on-surface-variant" },
    Voice: { icon: "mic", bg: "bg-surface-container", color: "text-on-surface-variant" },
    Sources: { icon: "gavel", bg: "bg-surface-container", color: "text-on-surface-variant" },
  };

  function disclaimerBanner(key, disclaimers) {
    const style = DISCLAIMER_STYLES[key] || DISCLAIMER_STYLES.General;
    const text = disclaimers[key] || "";
    return `<div class="${style.bg} rounded-xl p-space-md flex gap-space-sm items-start">
      <div class="flex-shrink-0 mt-0.5 ${style.color}">${icon(style.icon)}</div>
      <p class="font-body-sm text-body-sm ${style.color}">${escapeHtml(text)}</p>
    </div>`;
  }

  function checklistRow(text, checked = false) {
    return `<label class="flex items-center gap-space-sm py-1 cursor-pointer">
      <input type="checkbox" class="w-5 h-5 rounded border-outline-variant text-primary focus:ring-primary" ${checked ? "checked" : ""}/>
      <span class="font-body-md text-body-md text-on-surface">${escapeHtml(text)}</span>
    </label>`;
  }

  function numberedStep(n, text) {
    return `<div class="flex gap-space-sm items-start">
      <div class="w-6 h-6 rounded-full bg-primary-container text-on-primary-container flex items-center justify-center font-label-sm text-label-sm font-semibold flex-shrink-0">${n}</div>
      <p class="font-body-md text-body-md text-on-surface pt-0.5">${escapeHtml(text)}</p>
    </div>`;
  }

  function labeledProgress(label, current, total) {
    const pct = total > 0 ? Math.round((current / total) * 100) : 0;
    return `<div class="flex flex-col gap-1.5">
      <div class="flex justify-between font-label-sm text-label-sm text-outline">
        <span>${escapeHtml(label)}</span>
        <span>${current} / ${total}</span>
      </div>
      <div class="w-full bg-surface-container-high h-2.5 rounded-full overflow-hidden">
        <div class="h-full bg-secondary transition-all duration-500 rounded-full" style="width: ${pct}%"></div>
      </div>
    </div>`;
  }

  function screenNavCard({ title, subtitle, iconName, href }) {
    return `<a href="${href}" class="group flex items-center gap-space-md bg-surface-container-lowest rounded-xl p-space-md shadow-sm hover:shadow-md transition">
      <div class="w-11 h-11 rounded-full bg-primary-container flex items-center justify-center text-on-primary-container flex-shrink-0">
        ${icon(iconName)}
      </div>
      <div class="flex-1 min-w-0">
        <h3 class="font-headline-sm text-headline-sm text-on-surface group-hover:text-primary">${escapeHtml(title)}</h3>
        <p class="font-body-sm text-body-sm text-outline truncate">${escapeHtml(subtitle)}</p>
      </div>
      ${icon("chevron_right", "text-outline")}
    </a>`;
  }

  function detailHeader(title, backHref) {
    return `<div class="h-16 px-gutter-mobile flex items-center gap-space-xs">
      <a href="${backHref}" aria-label="Volver" class="min-h-touch-target-min min-w-touch-target-min flex items-center justify-center -ml-2 text-on-surface">
        ${icon("arrow_back")}
      </a>
      <h1 class="font-headline-sm text-headline-sm text-on-surface truncate">${escapeHtml(title)}</h1>
    </div>`;
  }

  function hubHeader(title, subtitle) {
    return `<div class="h-16 px-gutter-mobile flex items-center gap-space-xs">
      <img alt="Caregiver Pro California" class="h-8 w-8" src="assets/logo.svg"/>
      <div class="flex flex-col min-w-0">
        <span class="font-headline-sm text-headline-sm text-primary tracking-tight truncate">${escapeHtml(title)}</span>
        ${subtitle ? `<span class="font-label-sm text-label-sm text-outline truncate">${escapeHtml(subtitle)}</span>` : ""}
      </div>
    </div>`;
  }

  return {
    escapeHtml,
    icon,
    sectionCard,
    statusPill,
    disclaimerBanner,
    checklistRow,
    numberedStep,
    labeledProgress,
    screenNavCard,
    detailHeader,
    hubHeader,
  };
})();
