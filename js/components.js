/**
 * Shared render helpers — the web equivalent of ui/components/*.kt
 * (SectionCard, StatusPill, DisclaimerBanner, ChecklistRow, NumberedStep).
 * Visual language ported from the "Caregiver Pro v2" Claude Design canvas
 * (see css/app.css for the raw color/font tokens). Each function returns an
 * HTML string; screens assemble these into a page.
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
    return `<span class="material-symbols-rounded ${extraClass}">${name}</span>`;
  }

  /**
   * Minimal Markdown → HTML for the CURRICULUM_LESSONS_ES_90D.json content
   * (see docs — each lessonEs uses only: "### " headings, **bold**,
   * "1. " ordered lists and [text](url) links). Not a general renderer —
   * escapes first, then applies exactly those four transforms, so it's
   * safe even though this content is our own trusted JSON.
   */
  function inlineMarkdown(text) {
    return escapeHtml(text)
      .replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank" rel="noopener" class="text-primary underline">$1</a>')
      .replace(/\*\*([^*]+)\*\*/g, "<strong>$1</strong>");
  }

  function renderMarkdown(md) {
    const blocks = String(md || "").split(/\n\s*\n/);
    return blocks
      .map((block) => {
        const trimmed = block.trim();
        if (!trimmed) return "";
        if (trimmed.startsWith("### ")) {
          return `<h3 class="font-headline-sm text-headline-sm uppercase text-primary mt-2">${inlineMarkdown(trimmed.slice(4))}</h3>`;
        }
        const lines = trimmed.split("\n").map((l) => l.trim());
        if (lines.every((l) => /^\d+\.\s/.test(l))) {
          const items = lines.map((l) => `<li class="pl-1">${inlineMarkdown(l.replace(/^\d+\.\s/, ""))}</li>`).join("");
          return `<ol class="list-decimal list-inside flex flex-col gap-2 font-body-md text-body-md text-on-surface">${items}</ol>`;
        }
        return `<p class="font-body-md text-body-md text-on-surface leading-relaxed">${inlineMarkdown(trimmed)}</p>`;
      })
      .join("");
  }

  /**
   * Pulls the content under one "### Heading" out of a lessonEs Markdown
   * string (e.g. "Cómo se hace, paso a paso" or "Punto crítico de
   * seguridad") — every lesson uses this same fixed section structure.
   * Handles both layouts the content uses: heading and text sharing one
   * paragraph block (single newline, e.g. "Punto crítico..."), and heading
   * as its own block followed by separate block(s) — e.g. the numbered
   * steps list always has a blank line after its heading. Returns null if
   * the lesson has no such section (e.g. non-critical days have no
   * "Punto crítico de seguridad").
   */
  function findMarkdownBlock(md, headingText) {
    const blocks = String(md || "")
      .split(/\n\s*\n/)
      .map((b) => b.trim());
    const needle = "### " + headingText;
    const idx = blocks.findIndex((b) => b.startsWith(needle));
    if (idx === -1) return null;
    const sameBlockRest = blocks[idx].slice(needle.length).trim();
    if (sameBlockRest) return sameBlockRest;
    const collected = [];
    for (let i = idx + 1; i < blocks.length && !blocks[i].startsWith("### "); i++) {
      if (blocks[i]) collected.push(blocks[i]);
    }
    return collected.join("\n\n") || null;
  }

  function sectionCard(innerHtml, { bg = "bg-surface-container-lowest", border = true } = {}) {
    const borderCls = border ? "border-2 border-surface-container-high" : "";
    return `<div class="${bg} ${borderCls} rounded-xl p-space-md flex flex-col gap-space-sm">${innerHtml}</div>`;
  }

  function statusPill(text, { bg = "bg-secondary-fixed", color = "text-on-secondary-fixed-variant" } = {}) {
    return `<span class="pill ${bg} ${color} uppercase tracking-wide">${escapeHtml(text)}</span>`;
  }

  const DISCLAIMER_STYLES = {
    General: { icon: "info", bg: "bg-surface-container", color: "text-on-surface-variant" },
    Emergency: { icon: "emergency", bg: "bg-secondary", color: "text-on-secondary" },
    Scope: { icon: "fact_check", bg: "bg-primary-container", color: "text-on-primary-container" },
    FictionalCase: { icon: "theater_comedy", bg: "bg-surface-container", color: "text-on-surface-variant" },
    HandsOn: { icon: "front_hand", bg: "bg-secondary-fixed", color: "text-on-secondary-fixed-variant" },
    Certificate: { icon: "workspace_premium", bg: "bg-surface-container", color: "text-on-surface-variant" },
    Voice: { icon: "mic", bg: "bg-surface-container", color: "text-on-surface-variant" },
    Sources: { icon: "gavel", bg: "bg-primary-container", color: "text-on-primary-container" },
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
      <input type="checkbox" class="w-5 h-5 rounded border-2 border-outline-variant text-secondary focus:ring-secondary" ${checked ? "checked" : ""}/>
      <span class="font-body-md text-body-md text-on-surface">${escapeHtml(text)}</span>
    </label>`;
  }

  function numberedStep(n, text, { danger = false } = {}) {
    const numBg = danger ? "bg-secondary" : "bg-primary";
    const cardBg = danger ? "bg-secondary-container border-secondary/30" : "bg-[--cream] border-[--cream-border]";
    return `<div class="flex gap-space-sm items-center border-2 rounded-xl p-space-sm" style="background:${danger ? "var(--coral-bg)" : "var(--cream)"}; border-color:${danger ? "#F6C6BE" : "var(--cream-border)"}">
      <div class="w-9 h-9 ${numBg} text-white flex items-center justify-center font-display text-lg rounded-xl flex-shrink-0">${n}</div>
      <p class="font-body-md text-body-md flex-1" style="color:${danger ? "var(--coral-dark)" : "var(--text-dark)"}">${escapeHtml(text)}</p>
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

  /**
   * A brand scene illustration (assets/brand/scene-*.png) as a rounded
   * banner at the top of a screen. `key` matches a substring of the file
   * name (see data/brand_manifest.json > scenes[]) — e.g. "welcome-home",
   * "practice", "progress", "safety-center", "official-sources",
   * "career-portfolio". Returns "" if brand data isn't loaded or no scene
   * matches, so callers can splice it in unconditionally.
   */
  function brandScene(data, key) {
    const scene = data?.brand_manifest?.scenes?.find((s) => s.file.includes(key));
    if (!scene) return "";
    return `<img src="${scene.file}" alt="${escapeHtml(scene.altEs)}" class="w-full h-auto rounded-2xl block" loading="lazy"/>`;
  }

  function screenNavCard({ title, subtitle, iconName, href, tileBg = "bg-primary-container", tileFg = "text-on-primary-container" }) {
    return `<a href="${href}" class="group flex items-center gap-space-md bg-surface-container-lowest border-2 border-surface-container-high rounded-xl p-space-md hover:border-primary/50 transition">
      <div class="w-12 h-12 rounded-xl ${tileBg} ${tileFg} flex items-center justify-center flex-shrink-0">
        ${icon(iconName)}
      </div>
      <div class="flex-1 min-w-0">
        <h3 class="font-headline-sm text-headline-sm uppercase text-on-surface">${escapeHtml(title)}</h3>
        <p class="font-body-sm text-body-sm text-outline truncate">${escapeHtml(subtitle)}</p>
      </div>
      ${icon("chevron_right", "text-secondary")}
    </a>`;
  }

  function detailHeader(title, backHref) {
    return `<div class="bg-surface h-16 px-gutter-mobile flex items-center gap-space-xs">
      <a href="${backHref}" aria-label="Volver" class="min-h-touch-target-min min-w-touch-target-min flex items-center justify-center -ml-2 bg-primary-container text-on-primary-container rounded-xl w-10 h-10">
        ${icon("arrow_back")}
      </a>
      <h1 class="font-headline-sm text-headline-sm uppercase text-on-surface truncate">${escapeHtml(title)}</h1>
    </div>`;
  }

  function hubHeader(title, subtitle) {
    return `<div class="h-16 px-gutter-mobile flex items-center gap-space-xs">
      <img alt="Caregiver Pro California" class="h-8 w-8" src="assets/logo.svg"/>
      <div class="flex flex-col min-w-0">
        <span class="font-headline-sm text-headline-sm uppercase text-primary tracking-tight truncate">${escapeHtml(title)}</span>
        ${subtitle ? `<span class="font-label-sm text-label-sm text-outline truncate">${escapeHtml(subtitle)}</span>` : ""}
      </div>
    </div>`;
  }

  /** The neubrutalist "pressed" CTA button used throughout the redesign. */
  function bigButton(label, { color = "coral", iconName = null, id = null } = {}) {
    const idAttr = id ? `id="${id}"` : "";
    return `<div ${idAttr} class="btn-solid btn-${color} py-4 px-space-lg flex items-center justify-center gap-2 text-lg">
      <span>${escapeHtml(label)}</span>${iconName ? icon(iconName) : ""}
    </div>`;
  }

  return {
    escapeHtml,
    icon,
    renderMarkdown,
    inlineMarkdown,
    findMarkdownBlock,
    brandScene,
    sectionCard,
    statusPill,
    disclaimerBanner,
    checklistRow,
    numberedStep,
    labeledProgress,
    screenNavCard,
    detailHeader,
    hubHeader,
    bigButton,
  };
})();
