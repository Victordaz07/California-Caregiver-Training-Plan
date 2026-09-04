# Registro técnico de implementación — Caregiver Pro CA (Android)

## Resumen

- Rango de commits: desde `0e7c903` ("Add native Android app skeleton") hasta el commit que incluye este lote (ver `git log`).
- Módulos tocados: `:app` (único módulo del proyecto).
- Base de datos antes/después: no había base de datos antes; sigue sin haberla — se usó DataStore Preferences en su lugar (ver `docs/caregiver_upgrade/DECISIONS.md`, ADR-002).
- Versión de contenido antes/después: antes, todo el contenido legal/de rutas estaba hardcodeado y en varios casos incorrecto (mezclaba HCA/IHSS/HHA/CNA, atribuía CPR a HSC §1796.43, etc.). Después, el contenido legal vive en `content/Pathway.kt`, `content/LegalRequirements.kt`, `content/OfficialResources.kt`, `content/Disclaimers.kt` — copiados verbatim de `PATHWAYS_CA.json` / `LEGAL_REQUIREMENTS_CA.json` / `OFFICIAL_RESOURCES.json` / `DISCLAIMERS_ES_EN.json` (schemaVersion 1, verifiedAt 2026-09-04).

## Cambios por lote

### Lote 1 — Corrección legal/de contenido (P0)

- Motivo: hallazgos A-001 a A-045 de `01_AUDIT/MATRIZ_CORRECCIONES.csv` (10 críticos, 27 altos, 8 medios) — ver `BASELINE_AUDIT.md` para la fila por fila.
- Archivos: los 10 archivos de `ui/screens/*.kt` existentes (texto corregido), más 4 archivos nuevos en `content/`.
- Comportamiento anterior: la app conflacionaba HCA/IHSS/HHA/CNA, atribuía un examen a Guardian, citaba mal WIC §15610.05 y HSC §1796.43, mostraba "110h acreditadas", cifras de salario sin fuente, y llamaba "CDSS" a técnicas de práctica internas (DAR, protocolos, evaluaciones).
- Comportamiento nuevo: cada afirmación legal visible viene de `content/LegalRequirements.kt`, con `status`, fuente y (cuando aplica) fecha de vigencia. Las pantallas de escenarios y turno se etiquetan como práctica interna y ficticia. El audio "Gemini" se renombra a "orientación guiada" (A-028). Las cifras de salario sin fuente se retiran (A-017/A-018).
- Compatibilidad/migración: N/A (no había datos de usuario previos).
- Pruebas: ver `TEST_REPORT.md` — bloqueadas por falta de SDK en este entorno; verificación manual de texto vía `grep` (ver comandos en `TEST_REPORT.md`).
- Decisiones/deuda: ver `DECISIONS.md` ADR-004/ADR-005 (onboarding condensado, sin localización a inglés todavía).

### Lote 2 — Shell de navegación de 5 pestañas

- Motivo: A-029 (la barra inferior cambiaba de forma) y Fase 3 del prompt maestro.
- Archivos: `ui/navigation/MainTab.kt` (nuevo), `ui/navigation/CaregiverNavHost.kt` (reescrito), `ui/navigation/Screen.kt` (reducido de 10 a 8 entradas — Rutina Diaria y Plan 90 Días pasan a ser el contenido de los tabs Hoy/Plan, no pantallas empujadas), `ui/screens/hub/PracticeHubScreen.kt`, `AudioHubScreen.kt`, `CareerHubScreen.kt` (nuevos), `ui/components/HubScaffold.kt`, `ui/components/ScreenNavCard.kt` (nuevos). `ui/screens/HomeScreen.kt` se elimina (reemplazado por el shell).
- Comportamiento anterior: una sola pantalla "Home" en forma de galería listaba las 10 pantallas; no había pestañas.
- Comportamiento nuevo: `Scaffold` raíz con `NavigationBar` de 5 destinos estables (Hoy/Plan/Práctica/Audio/Carrera). La barra se oculta en pantallas empujadas (que ya tienen flecha de regreso vía `DetailScaffold`) y en onboarding.
- Compatibilidad/migración: N/A.
- Pruebas: bloqueadas (ver `TEST_REPORT.md`).
- Decisiones/deuda: ninguna pantalla nueva de ajustes/perfil en la barra superior todavía (mencionada en Fase 3 pero fuera de alcance de este lote).

### Lote 3 — Onboarding y selección de ruta

- Motivo: A-001 (hallazgo crítico — la app no distinguía rutas).
- Archivos: `ui/screens/onboarding/WelcomeScreen.kt`, `PathwaySelectionScreen.kt` (nuevos), `data/UserPreferencesRepository.kt` (nuevo, DataStore).
- Comportamiento anterior: no existía onboarding; ninguna pantalla sabía a qué ruta pertenecía el usuario.
- Comportamiento nuevo: primer arranque muestra bienvenida + disclaimers, luego selección explícita de una de las 6 rutas (sin ruta por defecto). La ruta elegida se persiste en DataStore y condiciona el contenido de `RequisitosCertificacionScreen`, `RecursosSemanaScreen` y `CareerHubScreen`.
- Compatibilidad/migración: N/A (primera versión de este dato).
- Pruebas: bloqueadas.
- Decisiones/deuda: ver `DECISIONS.md` ADR-004.

### Lote 4 — Currículo completo, Room y motor de aprendizaje

- Motivo: A-022 (solo existía una semana de muestra), A-021 (progreso sin persistir), A-023 (repetición espaciada solo de etiqueta), y la petición explícita del usuario de aplicar el resto del paquete.
- Archivos: `content/Curriculum.kt` (90 días, generado desde `CURRICULO_90_DIAS.csv`), `content/Flashcards.kt` (39 tarjetas), `content/Scenarios.kt` (13 escenarios ramificados), `content/Rubrics.kt` (34 criterios), `content/Glossary.kt` (30 términos), `content/AudioEpisodes.kt` (13 guiones) — todos generados desde el CSV/JSON de origen con un script, no transcritos a mano. `domain/ReviewScheduler.kt` (heurística de repaso, adaptada de `reference_kotlin/ReviewScheduler.kt.example`). `data/local/Entities.kt`, `Daos.kt`, `AppDatabase.kt` (Room). `data/LearningRepository.kt`.
- Comportamiento anterior: `Plan90DiasScreen` mostraba solo la semana 1 de ejemplo; `EscenariosFlashcardsScreen` tenía una tarjeta y un caso fijos sin lógica de repaso; nada persistía.
- Comportamiento nuevo: `RutinaDiariaScreen` muestra el día real del currículo (título/objetivo/práctica) según el día persistido en DataStore, con un temporizador real de 75 minutos en 5 bloques que, al completarse, marca el día en Room y avanza el día siguiente. `EscenariosFlashcardsScreen` calcula una cola real de tarjetas vencidas (Room), aplica la calificación Again/Hard/Good/Easy con `ReviewScheduler`, y muestra el escenario ramificado real de la semana actual con retroalimentación por elección.
- Compatibilidad/migración: primera versión del esquema Room (`version = 1`, `exportSchema = true`); no hay datos previos que migrar.
- Pruebas: bloqueadas (ver `TEST_REPORT.md`); `ReviewScheduler` se escribió con reloj inyectable para ser testeable cuando el proyecto compile.
- Decisiones/deuda: ver `DECISIONS.md` ADR-007 (riesgo de versión KSP/Room sin verificar) y el comentario en `RutinaDiariaScreen.kt` (el timer no sobrevive process death completo, solo rotación).

### Lote 5 — Audio y grabación real (sin Media3)

- Motivo: A-024/A-025/A-026/A-028/A-041. Al importar `AUDIO_EPISODE_OUTLINES.csv` se descubrió que el paquete no incluye ningún archivo de audio real (ver `DECISIONS.md` ADR-006), lo que cambió el plan original de "añadir Media3".
- Archivos: `audio/VoiceRecorderController.kt` (nuevo, envuelve `MediaRecorder`/`MediaPlayer` de plataforma), `ui/components/VoiceRecordCard.kt` (nuevo, UI de grabar/escuchar/borrar con flujo de permiso en contexto). `AndroidManifest.xml` (permiso `RECORD_AUDIO`). Reescritos: `AudioLeccionesScreen.kt`, `BibliotecaAudiosScreen.kt` (contenido real de los 13 guiones, sin controles de reproducción falsos ni estados de "descargado" fabricados), `RolePlayBilingueScreen.kt`, `SimulacionTurnoScreen.kt` (grabación real reemplaza los botones decorativos; se retiran las puntuaciones falsas de "Evaluación de IA" y "Criterios Evaluados en Tiempo Real", reemplazadas por autoevaluación honesta).
- Comportamiento anterior: botones de "Escuchar Audio" y "Grabar" sin `onClick` funcional; "18 de 52 episodios descargados" y "420 MB / 1.2 GB" eran texto fijo sin ninguna descarga real detrás; "Evaluación de IA — 94% (C2 Care)" era una cifra inventada.
- Comportamiento nuevo: grabar/reproducir/borrar la propia voz funciona de verdad, con almacenamiento privado (`context.filesDir/voice_practice/`) y nombre de archivo opaco (UUID). La biblioteca de audio muestra honestamente que los guiones ya están incluidos sin conexión (no hay "descarga" que simular).
- Compatibilidad/migración: N/A.
- Pruebas: bloqueadas (sin dispositivo con micrófono disponible aquí).
- Decisiones/deuda: ver `DECISIONS.md` ADR-006.

## Archivos creados

| Archivo | Propósito | Consumidor |
|---|---|---|
| `content/Pathway.kt` | Las 6 rutas verbatim de PATHWAYS_CA.json | Onboarding, Carrera, Requisitos, Recursos |
| `content/LegalRequirements.kt` | Requisitos legales verbatim de LEGAL_REQUIREMENTS_CA.json | RequisitosCertificacionScreen |
| `content/OfficialResources.kt` | Recursos oficiales + allowedHosts verbatim de OFFICIAL_RESOURCES.json | RecursosSemanaScreen |
| `content/Disclaimers.kt` | Los 7 avisos verbatim de DISCLAIMERS_ES_EN.json | DisclaimerBanner en 6 pantallas |
| `data/UserPreferencesRepository.kt` | Persistencia DataStore de ruta/idioma/progreso mínimo | CaregiverNavHost |
| `ui/components/DisclaimerBanner.kt` | Banner de aviso legal/emergencia reutilizable | 6 pantallas |
| `ui/components/HubScaffold.kt` | Chrome sin flecha de regreso para los 5 tabs | RutinaDiaria, Plan90Dias, 3 hubs |
| `ui/components/ScreenNavCard.kt` | Tarjeta de navegación reutilizable | 3 hubs |
| `ui/navigation/MainTab.kt` | Las 5 pestañas estables | CaregiverNavHost |
| `ui/screens/onboarding/WelcomeScreen.kt` | Bienvenida + disclaimers | CaregiverNavHost |
| `ui/screens/onboarding/PathwaySelectionScreen.kt` | Selección explícita de ruta | CaregiverNavHost |
| `ui/screens/hub/PracticeHubScreen.kt` | Tab "Práctica" | CaregiverNavHost |
| `ui/screens/hub/AudioHubScreen.kt` | Tab "Audio" | CaregiverNavHost |
| `ui/screens/hub/CareerHubScreen.kt` | Tab "Carrera" | CaregiverNavHost |
| `docs/caregiver_upgrade/*.md` | Los 5 informes exigidos por Fase 11 | Este mismo repositorio |
| `content/Curriculum.kt` | 90 días de currículo, generado desde `CURRICULO_90_DIAS.csv` | RutinaDiariaScreen |
| `content/Flashcards.kt` | 39 tarjetas, generadas desde `FLASHCARDS_SEED.json` | EscenariosFlashcardsScreen |
| `content/Scenarios.kt` | 13 escenarios ramificados, generados desde `SCENARIOS_SEED.json` | EscenariosFlashcardsScreen |
| `content/Rubrics.kt` | 34 criterios de rúbrica, generados desde `RUBRIC_CATALOG.json` | Escenarios/RolePlay/SimulaciónTurno |
| `content/Glossary.kt` | 30 términos, generados desde `GLOSSARY_ES_EN.csv` | (disponible para futuras pantallas) |
| `content/AudioEpisodes.kt` | 13 guiones de audio, generados desde `AUDIO_EPISODE_OUTLINES.csv` | AudioLeccionesScreen, BibliotecaAudiosScreen |
| `domain/ReviewScheduler.kt` | Heurística de repetición espaciada, reloj inyectable | LearningRepository |
| `data/local/Entities.kt` | Entidades Room (`CurriculumProgressEntity`, `ReviewStateEntity`) | AppDatabase |
| `data/local/Daos.kt` | DAOs Room (Flow + `@Upsert`) | AppDatabase, LearningRepository |
| `data/local/AppDatabase.kt` | Base de datos Room, singleton | LearningRepository |
| `data/LearningRepository.kt` | Fachada sobre Room + ReviewScheduler | RutinaDiariaScreen, EscenariosFlashcardsScreen |
| `audio/VoiceRecorderController.kt` | Envoltorio de `MediaRecorder`/`MediaPlayer` de plataforma | VoiceRecordCard |
| `ui/components/VoiceRecordCard.kt` | UI de grabar/escuchar/borrar con permiso en contexto | AudioLecciones, RolePlay, SimulaciónTurno |
| `docs/caregiver_upgrade/ANDROID_STUDIO_SETUP.md` | Guía de primera compilación para un desarrollador Android primerizo | Usuario (fuera del código) |

## Archivos modificados

| Archivo | Cambio | Riesgo |
|---|---|---|
| `ui/navigation/CaregiverNavHost.kt` | Reescrito completo: onboarding + shell de 5 tabs + persistencia | Alto (archivo central, no verificado por build) |
| `ui/navigation/Screen.kt` | Se quitan RutinaDiaria/Plan90Dias, quedan 8 entradas | Medio |
| `ui/screens/RutinaDiariaScreen.kt` | Sin parámetro `onBack`; usa `HubScaffold`; texto corregido | Medio |
| `ui/screens/Plan90DiasScreen.kt` | Sin parámetro `onBack`; usa `HubScaffold`; texto corregido; métricas de progreso separadas | Medio |
| `ui/screens/RequisitosCertificacionScreen.kt` | Reescrito: ahora recibe `pathwayId` y renderiza desde `LegalRequirements.kt` | Alto (lógica nueva, no solo texto) |
| `ui/screens/RecursosSemanaScreen.kt` | Recibe `pathwayId`; agrega sección de enlaces oficiales por ruta; texto corregido | Medio |
| `ui/screens/SimulacionTurnoScreen.kt`, `EscenariosFlashcardsScreen.kt`, `RolePlayBilingueScreen.kt`, `AudioLeccionesScreen.kt`, `AnalizadorVocacionalScreen.kt` | Solo texto + banners de disclaimer añadidos | Bajo |
| `android/gradle/libs.versions.toml`, `android/app/build.gradle.kts` | Se agrega `androidx.datastore:datastore-preferences:1.1.1` | Bajo (una sola dependencia estable) |
| `android/gradle/libs.versions.toml` | Se agregan `room`, `ksp`, `lifecycleViewmodelCompose` y las entradas de librería/plugin correspondientes | Medio (versión KSP sin verificar por build, ver ADR-007) |
| `android/build.gradle.kts` | Se agrega `alias(libs.plugins.ksp) apply false` | Bajo |
| `android/app/build.gradle.kts` | Se agrega `alias(libs.plugins.ksp)`, bloque `ksp { arg("room.schemaLocation", ...) }`, dependencias Room y `lifecycle-viewmodel-compose` | Medio (ver ADR-007) |
| `android/app/src/main/AndroidManifest.xml` | Se agrega `<uses-permission android:name="android.permission.RECORD_AUDIO" />` | Bajo |
| `ui/screens/RutinaDiariaScreen.kt` | Reescrito: sin parámetros; temporizador real de 75 min/5 bloques; usa `LearningRepository`/`Curriculum.kt` en vez de datos de ejemplo | Alto (lógica nueva, no solo texto) |
| `ui/screens/EscenariosFlashcardsScreen.kt` | Reescrito: cola de tarjetas vencidas real (Room), calificación SM-2-like, escenario ramificado real de la semana | Alto |
| `ui/screens/RolePlayBilingueScreen.kt` | Reescrito: guion real + `VoiceRecordCard`; se retira la evaluación de IA falsa | Medio |
| `ui/screens/SimulacionTurnoScreen.kt` | Reescrito: `VoiceRecordCard` + autorevisión honesta; se retiran los porcentajes en tiempo real falsos | Medio |
| `ui/screens/AudioLeccionesScreen.kt` | Reescrito: episodio real del día + librería completa de 13 guiones + `VoiceRecordCard` | Medio |
| `ui/screens/BibliotecaAudiosScreen.kt` | Reescrito: agrupado por 4 fases con datos reales; se retiran las cifras de descarga falsas | Medio |

## Archivos retirados

| Archivo | Razón | Sustituto | Recuperable en Git |
|---|---|---|---|
| `ui/screens/HomeScreen.kt` | La galería plana de 10 pantallas se reemplaza por el shell de 5 tabs | `CaregiverNavHost.kt` + `ui/screens/hub/*` | Sí (`git show 0e7c903:...`) |

## Dependencias

| Dependencia | Versión | Motivo | Fuente de versión/licencia |
|---|---|---|---|
| `androidx.datastore:datastore-preferences` | 1.1.1 | Persistir ruta/idioma/progreso mínimo sin generación de código (ver ADR-002) | AndroidX (Apache 2.0), versión estable pública — no verificada por build en este entorno |
| `androidx.room:room-runtime`, `androidx.room:room-ktx` | 2.6.1 | Persistir progreso de currículo y estado de repaso espaciado (ver ADR-007) | AndroidX (Apache 2.0), versión estable pública — no verificada por build en este entorno |
| `androidx.room:room-compiler` (vía KSP) | 2.6.1 | Generación de código Room en tiempo de compilación | Igual que arriba |
| `com.google.devtools.ksp` (plugin) | 2.0.21-1.0.27 | Requerido por `room-compiler`; versión elegida para coincidir con Kotlin 2.0.21 del proyecto | Google (Apache 2.0) — **no verificada por build en este entorno**, mayor riesgo del lote (ver ADR-007 y `ANDROID_STUDIO_SETUP.md` §3) |
| `androidx.lifecycle:lifecycle-viewmodel-compose` | 2.8.6 | Integración de `ViewModel` con Compose para el temporizador de sesión | AndroidX (Apache 2.0), versión estable pública — no verificada por build en este entorno |

## Migraciones

No aplica para DataStore: no había datos de usuario en ninguna versión anterior de la app (el skeleton previo no persistía nada). La primera vez que un usuario real abra esta versión, DataStore se inicializa vacío y el flujo de onboarding se ejecuta desde cero.

Para Room: `AppDatabase` nace en `version = 1` con `exportSchema = true`; no hay ninguna migración que aplicar todavía porque no existe una versión anterior del esquema.
