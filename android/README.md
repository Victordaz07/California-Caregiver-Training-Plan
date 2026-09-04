# Caregiver Pro CA — Android (nativo)

App nativa de Android para el plan de entrenamiento de 90 días, construida en **Kotlin + Jetpack Compose**. El diseño parte del export de Stitch en la raíz del repo (`/screens/*.html`, `/docs/design/DESIGN.md`); el contenido legal y de rutas profesionales viene del paquete de auditoría `caregiver_pro_ca_android_upgrade_pack_v1` (ver `/docs/caregiver_upgrade/`).

## Estado actual

Navegación, contenido legal, currículo completo, motor de aprendizaje y grabación de voz reales; audio narrado real y pruebas automatizadas todavía no:

- ✅ Onboarding con selección explícita de una de **6 rutas** (cuidador familiar, proveedor IHSS, HCA afiliado a HCO, cuidador privado, HHA, CNA) — ninguna se trata como sinónimo de otra.
- ✅ Shell de navegación estable de **5 pestañas**: Hoy / Plan / Práctica / Audio / Carrera (`ui/navigation/MainTab.kt`).
- ✅ Contenido legal (requisitos, recursos oficiales, avisos) copiado verbatim del paquete de auditoría y filtrado por la ruta elegida — no hay texto que mezcle HCA/IHSS/HHA/CNA ni afirmaciones sin fuente (ver `docs/caregiver_upgrade/REGULATORY_CONTENT_REPORT.md`).
- ✅ La ruta elegida y el estado de onboarding se persisten de verdad con DataStore (sobreviven cerrar y reabrir la app).
- ✅ El sistema de diseño de `docs/design/DESIGN.md` traducido a un `ColorScheme`/`Typography` de Material 3 (`ui/theme/`).
- ✅ **Currículo completo de 90 días** (`content/Curriculum.kt`), 39 tarjetas, 13 escenarios ramificados, 34 rúbricas y 30 términos de glosario — todo copiado del paquete de auditoría, no datos de muestra.
- ✅ **Motor de aprendizaje real**: sesión diaria con temporizador de 75 minutos en 5 bloques (`RutinaDiariaScreen`), repetición espaciada con heurística SM-2-like y reloj inyectable (`domain/ReviewScheduler.kt`), progreso y cola de repaso persistidos con **Room** (`data/local/`, `data/LearningRepository.kt`).
- ✅ **Grabación de voz real**: el usuario graba y escucha su propia práctica (Role Play, Simulación de Turno, Audio Lecciones) vía `MediaRecorder`/`MediaPlayer` de plataforma, con permiso de micrófono solicitado en contexto (`audio/VoiceRecorderController.kt`, `ui/components/VoiceRecordCard.kt`).
- ❌ Sin audio narrado real reproducible: el paquete de origen no incluye ningún archivo de audio (ver `DECISIONS.md` ADR-006) — las lecciones de audio son guiones de lectura + la propia grabación del usuario, no narración profesional.
- ❌ Sin pruebas automatizadas (aunque `ReviewScheduler` ya está escrito para ser fácilmente testeable).
- ❌ Sin Hilt, sin localización real a inglés, sin revisión de accesibilidad en dispositivo (ver deuda pendiente en `FINAL_AUDIT.md`).

Ver `docs/caregiver_upgrade/FINAL_AUDIT.md` para el estado fila por fila contra las 45 correcciones del paquete de auditoría, y `docs/caregiver_upgrade/DECISIONS.md` para las decisiones de arquitectura y sus razones (incluye ADR-006 sobre por qué no hay Media3/audio narrado y ADR-007 sobre el riesgo de versión de Room/KSP sin verificar).

## Cómo abrirlo

1. Instala [Android Studio](https://developer.android.com/studio) (incluye el SDK de Android).
2. `Open` → selecciona la carpeta `android/` de este repo (no la raíz del repo).
3. Deja que Android Studio sincronice Gradle la primera vez (descarga el SDK/dependencias).
4. Ejecuta en un emulador o teléfono conectado (▶️ `app`).

> **Nota:** este proyecto se generó y revisó en un entorno sin acceso a `dl.google.com` (el dominio que sirve el Android SDK y el Android Gradle Plugin), así que **no se pudo compilar aquí en ninguna sesión de trabajo**. El código fue revisado a mano con mucho cuidado (ver `docs/caregiver_upgrade/TEST_REPORT.md`), pero la primera sincronización en Android Studio es el primer build real. Esta vez el riesgo es un poco mayor que antes porque se agregó Room + KSP (`com.google.devtools.ksp` versión `2.0.21-1.0.27`), cuya compatibilidad exacta con Kotlin 2.0.21 no se pudo verificar sin internet — si la sincronización falla mencionando KSP, es ese riesgo ya anticipado; sigue las instrucciones paso a paso de `docs/caregiver_upgrade/ANDROID_STUDIO_SETUP.md` (pensada para quien nunca compiló una app Android) y la sección "Riesgos de verificación" de `docs/caregiver_upgrade/DECISIONS.md`.

## Estructura

```
app/src/main/java/com/caregiverproca/app/
├── MainActivity.kt
├── content/             Pathway.kt, LegalRequirements.kt, OfficialResources.kt, Disclaimers.kt,
│                          Curriculum.kt (90 días), Flashcards.kt (39), Scenarios.kt (13),
│                          Rubrics.kt (34), Glossary.kt (30), AudioEpisodes.kt (13 guiones)
│                          — todo el contenido, verbatim del paquete de auditoría
├── domain/              ReviewScheduler.kt — heurística de repetición espaciada, reloj inyectable
├── audio/               VoiceRecorderController.kt — envoltorio de MediaRecorder/MediaPlayer
├── data/                UserPreferencesRepository.kt (DataStore: ruta, onboarding, día actual)
│                          LearningRepository.kt (fachada sobre Room + ReviewScheduler)
├── data/local/          Entities.kt, Daos.kt, AppDatabase.kt — persistencia Room (progreso, repaso)
├── ui/theme/            Color.kt, Type.kt, Shape.kt, Theme.kt — el sistema de diseño
├── ui/navigation/       MainTab.kt (las 5 pestañas), Screen.kt (8 pantallas empujadas), CaregiverNavHost.kt
├── ui/components/       SectionCard, StatusPill, NumberedStep, ChecklistRow, LabeledProgress,
│                          DetailScaffold (con flecha atrás), HubScaffold (sin flecha, para tabs),
│                          ScreenNavCard, DisclaimerBanner, VoiceRecordCard
├── ui/screens/          Rutina Diaria (tab Hoy, temporizador real de 75 min), Plan 90 Días (tab Plan)
│                          y las 8 pantallas empujadas
├── ui/screens/hub/      PracticeHubScreen, AudioHubScreen, CareerHubScreen — contenido de 3 de las 5 pestañas
└── ui/screens/onboarding/  WelcomeScreen, PathwaySelectionScreen
```

## Documentación de la auditoría

`docs/caregiver_upgrade/` (en la raíz del repo, no dentro de `android/`) tiene los 5 informes que exige el paquete de auditoría:

- `BASELINE_AUDIT.md` — estado antes de este lote, comparado contra las 45 correcciones.
- `FINAL_AUDIT.md` — estado después, fila por fila.
- `IMPLEMENTATION_CHANGELOG.md` — qué archivo cambió y por qué.
- `TEST_REPORT.md` — qué se pudo verificar en este entorno (sin SDK) y qué no.
- `REGULATORY_CONTENT_REPORT.md` — cada afirmación legal, su fuente y su estado.
- `DECISIONS.md` — decisiones de arquitectura (por qué DataStore y no Room todavía, por qué no Hilt, etc.) y los riesgos de verificación a revisar en la primera compilación real.

## Próximos pasos sugeridos (en orden)

1. **Compilar de verdad**: abrir en Android Studio con internet normal y resolver cualquier error de compilación, empezando por la versión de KSP si falla (ver `ANDROID_STUDIO_SETUP.md` y "Riesgos de verificación" en `DECISIONS.md`).
2. **Pruebas automatizadas**: una vez que compile, añadir un test JUnit para `domain/ReviewScheduler.kt` (ya escrito con reloj inyectable para esto) y Compose UI tests (A-043 en la auditoría).
3. **Audio narrado real**: conseguir o producir narración real para los 13 guiones de `content/AudioEpisodes.kt` (el paquete de origen no incluye ningún archivo de audio, ver ADR-006) e integrar Media3 para reproducirla.
4. **Evidencia de competencias en 3 niveles** (A-040): separar "conocimiento" (tarjetas), "práctica" (escenarios/grabación) y "observado" en el modelo de progreso, en vez de solo "día completado".
5. **Fuentes reales**: agregar los `.ttf` de Manrope y JetBrains Mono en `app/src/main/res/font/` (ahora mismo usa las fuentes del sistema).
6. **Localización a inglés**: mover el texto de UI a recursos (`strings.xml`) para que el "inglés de apoyo" que pide el producto sea real, no solo los campos `titleEn` que ya existen en `content/Pathway.kt`.
7. **Accesibilidad en dispositivo**: revisar TalkBack, tamaños táctiles y el contraste de `outline` sobre `surface` (por debajo de 4.5:1, ver `FINAL_AUDIT.md`) una vez que haya un emulador disponible.
