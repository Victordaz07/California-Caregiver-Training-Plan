---
name: Caregiver Companion System
colors:
  surface: '#f7faf8'
  surface-dim: '#d7dbd9'
  surface-bright: '#f7faf8'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f1f4f2'
  surface-container: '#ebefed'
  surface-container-high: '#e6e9e7'
  surface-container-highest: '#e0e3e1'
  on-surface: '#181c1b'
  on-surface-variant: '#3f4947'
  inverse-surface: '#2d3130'
  inverse-on-surface: '#eef1ef'
  outline: '#6f7977'
  outline-variant: '#bec9c7'
  surface-tint: '#216963'
  primary: '#004541'
  on-primary: '#ffffff'
  primary-container: '#115e59'
  on-primary-container: '#91d5ce'
  inverse-primary: '#8fd3cc'
  secondary: '#9a442d'
  on-secondary: '#ffffff'
  secondary-container: '#fc9174'
  on-secondary-container: '#742814'
  tertiary: '#134159'
  on-tertiary: '#ffffff'
  tertiary-container: '#2f5971'
  on-tertiary-container: '#a5cfeb'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#abefe8'
  primary-fixed-dim: '#8fd3cc'
  on-primary-fixed: '#00201e'
  on-primary-fixed-variant: '#00504b'
  secondary-fixed: '#ffdbd2'
  secondary-fixed-dim: '#ffb4a1'
  on-secondary-fixed: '#3c0800'
  on-secondary-fixed-variant: '#7c2e19'
  tertiary-fixed: '#c5e7ff'
  tertiary-fixed-dim: '#a2cce8'
  on-tertiary-fixed: '#001e2d'
  on-tertiary-fixed-variant: '#1f4b63'
  background: '#f7faf8'
  on-background: '#181c1b'
  surface-variant: '#e0e3e1'
typography:
  display-lg:
    fontFamily: manrope
    fontSize: 36px
    fontWeight: '700'
    lineHeight: 44px
    letterSpacing: -0.02em
  display-lg-mobile:
    fontFamily: manrope
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 34px
    letterSpacing: -0.01em
  headline-lg:
    fontFamily: manrope
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: manrope
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  headline-sm:
    fontFamily: manrope
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: manrope
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: manrope
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: manrope
    fontSize: 13px
    fontWeight: '400'
    lineHeight: 18px
  label-lg:
    fontFamily: jetbrainsMono
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.02em
  label-md:
    fontFamily: jetbrainsMono
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.04em
  label-sm:
    fontFamily: jetbrainsMono
    fontSize: 10px
    fontWeight: '500'
    lineHeight: 14px
    letterSpacing: 0.06em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  space-xxs: 0.25rem
  space-xs: 0.5rem
  space-sm: 0.75rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
  space-2xl: 3rem
  gutter-mobile: 1rem
  gutter-tablet: 1.5rem
  margin-mobile: 1rem
  margin-tablet: 2rem
  touch-target-min: 3rem
---

## Brand & Style

This design system serves frontline non-medical and certified nursing assistants (CNAs), in-home supportive service (IHSS) workers, and family caregivers across California. The brand narrative balances clinical rigor with profound human empathy: it is reassuring, grounded, and authoritative without feeling sterile, intimidating, or overly administrative.

The visual approach is **Modern Clinical-Humanist**:
- **Clarity under pressure:** Caregivers operate in emotionally intense, time-constrained environments. Layouts maintain high legibility, immediate visual hierarchy, and forgiving touch zones.
- **Calm reassurance:** Soft, warm undertones remove hospital coldness, replacing anxiety with structured guidance and steady encouragement.
- **Micro-competency focus:** Daily routines, timed scenario drills, and certification milestones feel manageable, tactile, and rewarding rather than punitive.

## Colors

The palette grounds the interface in reliable healthcare authority while using sunlit terracotta to denote key actions, time-sensitive checks, and certifications.

- **Primary Deep Teal (`#115E59`)**: The voice of clinical confidence, protocol certainty, and calm leadership. Used for top-level navigation, primary buttons, essential headings, and verified certification badges.
- **Secondary Warm Terracotta (`#E07A5F`)**: Conveys human warmth, urgency, and active focus. Deployed on high-priority alerts, active timers, actionable callouts, and step completions.
- **Tertiary Slate Marine (`#28536B`)**: Supports secondary metadata, category tags, module markers, and supplemental metric graphs.
- **Neutral Soft Sage Tint (`#F4F7F5`)**: A clean, eye-easing foundational background that softens glare compared to harsh sterile whites, accommodating extended reading during late-night shifts.
- **Semantic Accents**:
  - Success / Validated: `#2D6A4F` (Forest Sage)
  - Critical / Protocol Breach: `#C1121F` (Muted Crimson)
  - Surface Pure: `#FFFFFF` for elevated cards, active flashcard faces, and input surfaces.

## Typography

The type system prioritizes rapid scannability during scenario checklists, timed interventions, and certification studying.

- **Primary Typeface (`Manrope`)**: Provides modern geometric balance, rounded optical terminals, and high legibility across small handheld screens. It eliminates visual fatigue during long protocol reviews.
- **Monospace Technical Accent (`JetBrains Mono`)**: Reserved strictly for timers, dosage checkouts, 90-day progress metrics (`Day 42/90`), and compliance standard IDs (e.g., `CA-HSC-1796`). This anchors precision metrics distinctly from prose.
- **Micro-adjustments**: Body copy preserves an accessible 1.5 line-height ratio. Interactive check items always use medium or semibold weights to remain readable in low light.

## Layout & Spacing

The layout is built primarily around a single-column mobile-first grid, expanding gracefully to multi-panel cards on tablet displays commonly used in training labs.

- **Mobile Viewport (360px – 599px)**: 4-column layout with `16px` (`1rem`) outer margins and gutters. Checklists and flashcards fill 100% of the content width. Interactive controls honor a strict `48px` (`3rem`) minimum touch target height to prevent errors while wearing medical gloves or multi-tasking.
- **Tablet & Split-Screen (600px – 1023px)**: 8-column layout with `24px` gutters and `32px` margins. Enables split views: daily 90-day timeline on the left rail, active lesson/flashcard deck on the right.
- **Rhythm**: All paddings adhere to an 8pt system, using `4px` solely for tight badge internal padding and input icon offsets.

## Elevation & Depth

To preserve an approachable, distraction-free environment, depth is achieved through **tonal layering and low-contrast ambient shadows**, avoiding heavy skeuomorphic drops or glossy finishes:

- **Base Layer (Level 0)**: Background `#F4F7F5`. Flat, calming canvas.
- **Surface Layer (Level 1)**: Pure `#FFFFFF` cards, lists, and form rows with a micro-border of `1px solid rgba(17, 94, 89, 0.08)`. No blur shadow; structural contrast alone keeps elements crisp.
- **Floating / Active Layer (Level 2)**: Active scenario cards, interactive flashcard flips, and running countdown timer docks. Supported by a warm ambient shadow: `0 4px 16px -2px rgba(17, 94, 89, 0.08), 0 2px 6px -1px rgba(224, 122, 95, 0.04)`.
- **Overlay & Modal Layer (Level 3)**: Critical alerts and emergency scenario steps. Shadow: `0 12px 32px -4px rgba(15, 76, 92, 0.16)`. Backdrop overlay uses `#0F2F2E` with a 45% opacity wash to mute peripheral noise without completely blocking context.

## Shapes

The shape architecture uses rounded geometry (Level 2) to convey approachability, safety, and care:

- **Standard Containers & Cards**: `16px` (`1rem`) corner radius for daily module cards, flashcards, and progress widgets.
- **Inner Interactive Targets**: `8px` (`0.5rem`) corner radius for button controls, input inputs, and scenario checklist item boxes.
- **Badges, Trackers, and Pills**: Fully rounded pill shapes (`9999px`) for status pills (e.g., `IHSS Approved`, `Core Module 3`), time tags, and streak counters.
- **Borders**: All surface cards carry a soft `1px` structural outline to maintain legibility when screen brightness is reduced in resident rooms.

## Components

### Buttons
- **Primary**: Solid Deep Teal (`#115E59`) fill, white text, 8px radius, 48px height. Focus states apply a 2px offset ring of Warm Terracotta (`#E07A5F`).
- **Secondary Action**: Warm Terracotta (`#E07A5F`) fill with white text, reserved for "Start Drill", "Record Observation", or "Mark as Emergency".
- **Outline / Tertiary**: Transparent background with 1px border in `#115E59`, text in `#115E59`. Used for secondary actions like "Save for Review".

### Checklist Items & Radio Scenarios
- **Scenario Checkboxes**: Custom square blocks with 8px radius, minimum 48px row height. Default state features a subtle 1px border on white background; checked state fills with Deep Teal (`#115E59`) and displays an animated white checkmark.
- **Protocol Radios**: Circular, 24px diameter target centered within a 48px interactive row. Active state features a solid Terracotta indicator with an outer focus halo.

### Flashcards
- **Structure**: 16px radius, white surface, 1px subtle border. Minimum height of 220px on mobile.
- **Card Header**: Displays category tags (e.g., *Infection Control*, *Fall Prevention*) in Monospace `label-sm` along with an audio pronunciation button.
- **Interaction**: Flip trigger with smooth 300ms transition; the back of the card features a soft sage-tinted banner highlighting California regulatory compliance points.

### Timers
- **Display**: High-visibility JetBrains Mono numbers (`display-lg-mobile`), centrally docked.
- **State Changes**: Standard active timer pulses in Deep Teal. Warning states (last 10 seconds of CPR cadence or medication check) switch to Warm Terracotta (`#E07A5F`) with a steady ambient perimeter pulse.

### 90-Day Progress Tracker
- **Segmented Milestone Bar**: Divided into California regulatory chunks: Day 1-30 (Foundations), Day 31-60 (Supervised Practice), Day 61-90 (Final Certification).
- **Nodes**: Inactive nodes appear in soft slate gray; completed nodes in Deep Teal with checkmarks; the active day highlighted with a Terracotta pulse ring.

### Input Fields
- **Container**: 48px height, 8px radius, pure white background, 1px border in `#CBD5E1`.
- **Focused State**: Border shifts to `#115E59` with a soft 3px box-shadow tinted at 10% teal opacity. Labels sit prominently above the input in `body-sm` semibold to guarantee immediate clarity.