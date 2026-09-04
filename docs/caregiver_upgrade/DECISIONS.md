# Registro de decisiones — Caregiver Pro CA (Android)

## ADR-001 — Este entorno no puede compilar contra el Android SDK

- Fecha: 2026-09-04
- Estado: accepted
- Contexto: `./gradlew tasks` falla en la resolución del plugin `com.android.application:8.6.0` porque `google()` resuelve a `dl.google.com`, bloqueado explícitamente por la política de red de este sandbox (`connect_rejected`, "gateway answered 403 to CONNECT (policy denial)"). `maven.google.com` redirige (301) al mismo host. Se verificó de nuevo en esta sesión (no se asumió el resultado de la sesión anterior).
- Decisión: implementar todo el batch con máximo cuidado manual (imports, scopes de Compose, firmas de función, balance de llaves) y documentar explícitamente que no hay evidencia de compilación real. La primera verificación real debe ocurrir en Android Studio con acceso normal a internet.
- Alternativas consideradas: ninguna — no existe forma de instalar el SDK sin `dl.google.com`.
- Consecuencias: todos los "TEST_REPORT.md" de este lote reportan `blocked` para cualquier prueba que requiera compilar, ejecutar en emulador/dispositivo, o Gradle más allá de la evaluación de `settings.gradle.kts`/`build.gradle.kts`.
- Riesgos: puede haber errores de tipos/API que solo aparezcan al compilar (por ejemplo, un nombre de ícono de `material-icons-extended` inexistente, o una firma de `LinearProgressIndicator` distinta a la asumida). Se listan las suposiciones más frágiles en la sección "Riesgos de verificación" abajo.
- Revisión futura: en la primera sesión con Android Studio y red normal, ejecutar `./gradlew :app:assembleDebug :app:testDebugUnitTest lint` y corregir cualquier error de compilación antes de continuar con las fases siguientes.

## ADR-002 — DataStore en vez de Room para este lote

- Fecha: 2026-09-04
- Estado: accepted
- Contexto: `03_ANDROID_TECHNICAL/ANDROID_ARCHITECTURE.md` pide Room para "hechos durables de aprendizaje" (intentos, cola de repaso, progreso del currículo) y DataStore para preferencias pequeñas (idioma, `pathwayId`). El motor de aprendizaje (Fase 4) que generaría esos hechos durables no se implementó en este lote. Room requiere el plugin KSP emparejado con la versión exacta de Kotlin (2.0.21) y no hay forma de verificar en este entorno que la versión de KSP elegida exista o compile.
- Decisión: usar solo `androidx.datastore:datastore-preferences:1.1.1` (una única dependencia madura, sin generación de código) para persistir `pathwayId`, `onboardingCompleted`, `currentPlanDay` y `completedBlocksToday`. Esto ya satisface el criterio de aceptación de A-021 para lo que existe hoy (la ruta elegida sobrevive el relanzamiento) sin arriesgar un build roto por una versión de KSP inventada.
- Alternativas consideradas: (a) implementar Room igualmente, aceptando el riesgo — rechazada porque un error de anotación no detectado bloquearía TODO el módulo, no solo la función nueva; (b) no persistir nada — rechazada porque A-021 es un hallazgo Alto y la persistencia de la ruta es la base de Carrera/Requisitos.
- Consecuencias: `currentPlanDay`/`completedBlocksToday` existen en el repositorio pero ninguna pantalla los actualiza todavía (Fase 4 no implementada), así que hoy son campos preparados, no funcionalidad visible.
- Riesgos: cuando se implemente el motor de aprendizaje real, este esquema de DataStore no alcanza (no modela intentos individuales, cola de repaso `dueAt`, ni evidencia por objetivo) — Room seguirá siendo necesario para eso. No reemplaza el `DATA_MODEL_AND_SCHEMA.md` completo.
- Revisión futura: al implementar Fase 4 (motor de aprendizaje), introducir Room siguiendo `DATA_MODEL_AND_SCHEMA.md` y migrar `currentPlanDay`/`completedBlocksToday` desde DataStore a Room si corresponde.

## ADR-003 — Sin Hilt en este lote

- Fecha: 2026-09-04
- Estado: accepted
- Contexto: el proyecto no tenía Hilt instalado. `ANDROID_ARCHITECTURE.md` dice "usa Hilt si ya existe o si introducirlo no crea una migración desproporcionada".
- Decisión: no introducir Hilt. El único repositorio nuevo (`UserPreferencesRepository`) se instancia directamente con `remember { UserPreferencesRepository(context) }` dentro de `CaregiverNavHost`. Es suficiente para el alcance actual (una sola fuente de datos, sin lógica de negocio compleja que necesite sustitución en pruebas).
- Alternativas consideradas: introducir Hilt ahora para dejar el patrón listo — rechazada por el mismo motivo que Room: el plugin KSP/kapt de Hilt tampoco se puede verificar aquí, y añadir dos piezas de codegen no verificables en la misma sesión multiplica el riesgo de un build roto sin forma de detectarlo.
- Consecuencias: cuando existan más repositorios (Room, Media3, grabación), este patrón manual dejará de escalar bien.
- Riesgos: ninguno inmediato.
- Revisión futura: introducir Hilt cuando se agregue Room, en una sesión donde se pueda compilar y verificar la generación de código.

## ADR-004 — Onboarding condensado a dos pantallas

- Fecha: 2026-09-04
- Estado: accepted
- Contexto: Fase 3 pide seis pasos de onboarding (idioma/accesibilidad, objetivo, ruta, condado opcional, aviso+consentimiento, plan inicial editable).
- Decisión: implementar completos los pasos 3 (selección de ruta, `PathwaySelectionScreen`) y una versión condensada de 1/2/5 (`WelcomeScreen`, que muestra los siete disclaimers de `DISCLAIMERS_ES_EN.json` y pide "Entender" antes de continuar — no hay un checkbox de consentimiento explícito todavía). El paso 4 (condado opcional) y el paso 6 (plan inicial editable) no están implementados.
- Alternativas consideradas: bloquear todo el onboarding hasta tener las seis pantallas completas — rechazada porque hubiera dejado la app sin ninguna forma de elegir ruta en este lote, que es el hallazgo Crítico A-001.
- Consecuencias: el consentimiento actual es "leer y continuar", no una casilla separada de notificaciones/grabación como pide el paso 5 literal.
- Riesgos: si se lanza así, un revisor podría pedir un consentimiento más explícito (casilla, no solo botón "Entiendo").
- Revisión futura: agregar selector de condado y checkboxes de consentimiento separados (notificaciones, grabación) antes de cualquier lanzamiento real.

## ADR-005 — Localización: solo español por ahora

- Fecha: 2026-09-04
- Estado: accepted
- Contexto: el prompt maestro pide "español principal, inglés de apoyo" con recursos localizados de verdad (Fase 8).
- Decisión: los modelos de contenido (`Pathway`, `LegalRequirement`, `Disclaimers`) solo exponen campos en español por ahora, aunque el paquete de origen trae ambos idiomas para `Pathway` (`titleEn`, `summaryEn` sí se guardaron; el resto no). La UI completa sigue en español fijo, igual que el esqueleto anterior.
- Alternativas consideradas: implementar cambio de idioma completo ahora — rechazada por alcance de tiempo; hubiera significado duplicar cada string en `strings.xml`/recursos localizados y tocar las diez pantallas otra vez.
- Consecuencias: A-032/Fase 8 (localización real) sigue pendiente.
- Revisión futura: mover todo el texto de UI a `strings.xml` con `values-en/`, y completar los campos `*En` que faltan en los modelos de contenido.

## ADR-006 — Sin Media3; MediaPlayer/MediaRecorder de plataforma en su lugar

- Fecha: 2026-09-04
- Estado: accepted
- Contexto: al importar `AUDIO_EPISODE_OUTLINES.csv` se confirmó (búsqueda de `.mp3`/`.wav`/`.m4a`/`.ogg` en todo el paquete de auditoría, incluida `06_SOURCE_REFERENCE` y `04_DESIGN_ASSETS/ASSET_MANIFEST.md`) que **el paquete no incluye ningún archivo de audio real** — solo 13 guiones/transcripciones con frase clave, pronunciación aproximada y prompt de práctica. No hay nada que Media3 pudiera reproducir como "lección narrada"; producir esa narración exigiría grabarla de verdad o usar un servicio de texto a voz, y esto último es exactamente el tipo de "servicio externo" que esta sesión debía evitar para el primer proyecto Android del usuario.
- Decisión: no añadir la dependencia Media3/ExoPlayer en este lote. `AudioLeccionesScreen` y `BibliotecaAudiosScreen` ahora muestran los 13 guiones reales como contenido de lectura/práctica (ya disponible sin conexión porque está compilado en el APK, sin necesidad de "descargar" nada). Para la única reproducción de audio real posible — la propia voz grabada del alumno — se usa `android.media.MediaRecorder`/`MediaPlayer` (parte del framework de Android, sin dependencia nueva) en `audio/VoiceRecorderController.kt`, expuesto en pantalla vía `ui/components/VoiceRecordCard.kt`.
- Alternativas consideradas: (a) añadir Media3 igualmente con un archivo de silencio de relleno, para dejar el "cableado" listo — rechazada porque sería fingir una función (mostrar controles de reproducción de "lección" que en realidad reproducen silencio) y el prompt maestro prohíbe explícitamente simular funciones; (b) generar narración con un servicio de texto a voz — rechazada porque el usuario pidió explícitamente evitar servicios externos en su primera app.
- Consecuencias: A-024 (audio real) y A-025 (descargas verificables) siguen sin resolver para *narración*, pero A-026/A-041 (grabación con consentimiento) sí quedan resueltos con este lote, con una implementación real, no simulada.
- Riesgos: si más adelante se consigue narración real (grabada o por un servicio de TTS que el usuario apruebe explícitamente), Media3 sí será necesario para streaming/descargas — la decisión de esta sesión no lo descarta, solo lo pospone a que exista contenido real que reproducir.
- Revisión futura: cuando exista audio narrado real, añadir Media3 + `DownloadService` y aplicar el mismo cuidado de verificación de versión que se describe en ADR-002 para Room.

## ADR-007 — Room añadido en este lote, con riesgo de versión sin verificar

- Fecha: 2026-09-04
- Estado: accepted (supersede parcialmente a ADR-002)
- Contexto: ADR-002 (sesión anterior) evitó Room por el riesgo de una versión de KSP inventada rompiendo el build sin forma de detectarlo. El usuario pidió explícitamente en esta sesión aplicar "todo de una", incluyendo persistencia real de progreso y repetición espaciada — funciones que si dependen únicamente de DataStore no pueden modelar bien (cola de repaso por tarjeta, intentos, fechas de vencimiento).
- Decisión: añadir Room 2.6.1 + KSP `2.0.21-1.0.27` (ver ADR-001 "Riesgos de verificación" abajo — esta versión de KSP NO se pudo confirmar que exista/compile en este entorno). Se usó `ksp { arg("room.schemaLocation", "$projectDir/schemas") }` en vez del plugin `androidx.room` más nuevo (`room { schemaDirectory(...) }`), porque ese plugin adicional habría sido otra pieza de versión sin verificar apilada sobre KSP.
- Alternativas consideradas: mantener ADR-002 (solo DataStore) — rechazada porque el usuario pidió explícitamente la funcionalidad completa y DataStore no modela bien datos por-tarjeta con cola de vencimiento.
- Consecuencias: `LearningRepository` (progreso de currículo, estado de repaso) depende de que Gradle resuelva `com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.0.21-1.0.27` y `androidx.room:room-compiler:2.6.1` — ninguno de los dos se pudo descargar/verificar aquí (mismo bloqueo de `dl.google.com`/Maven que impide compilar cualquier cosa en este entorno).
- Riesgos: si `2.0.21-1.0.27` no existe exactamente, Android Studio lo señalará como un error de resolución de plugin claro y con solución de un clic ("Actualizar versión") — ver `docs/caregiver_upgrade/ANDROID_STUDIO_SETUP.md` paso 3.
- Revisión futura: primera vez que se compile con SDK real, confirmar la versión de KSP y ajustar si Android Studio sugiere una distinta.

## ADR-008 — Narración real generada offline con Google Cloud Text-to-Speech

- Fecha: 2026-09-04
- Estado: accepted (resuelve A-024/A-025, complementa ADR-006)
- Contexto: ADR-006 documentó que el paquete de auditoría no incluye ningún audio narrado real y pospuso Media3 hasta que existiera contenido real que reproducir. El usuario decidió producir esa narración él mismo con Google Cloud Text-to-Speech (creó un proyecto en Google Cloud Console, habilitó "Cloud Text-to-Speech API" y generó una API key restringida solo a esa API).
- Decisión: generar los 13 episodios como audio real, **una sola vez, fuera de la app** (un script ejecutado en esta sesión, no en tiempo de ejecución de la app), usando la voz `es-US-Neural2-A` con SSML (pausas, repetición lenta de la frase clave, pausa final de 2s para que el alumno practique). El guion narrado de cada episodio es una expansión fiel de los campos ya existentes en `content/AudioEpisodes.kt` (`titleEs`, `keyPhraseEn`, `pronunciationEs`, `learnerPromptEs`) — no se agregó ningún dato legal/regulatorio nuevo. Los 13 `.mp3` resultantes (`audio_w01.mp3`…`audio_w13.mp3`, ~4.1 MB en total) se guardaron como recursos estáticos en `app/src/main/res/raw/` y se reproducen con `android.media.MediaPlayer.create(context, resId)` (mismo patrón "framework de Android, sin dependencia nueva" que ADR-006 ya eligió para la grabación) vía `audio/AudioNarrationController.kt`. **La API key de Cloud TTS nunca entró al código ni a la app** — se usó una vez, por fuera, y el usuario fue instruido a restringirla/rotarla en Cloud Console después de generar los archivos.
- Alternativas consideradas: (a) añadir Media3/streaming — rechazada, sigue sin ser necesaria porque los 13 archivos son cortos (~20-35s cada uno) y ya están embebidos en el APK, no hay nada que transmitir ni descargar bajo demanda; (b) generar la narración en tiempo real dentro de la app llamando a Cloud TTS con la key embebida — rechazada explícitamente: expondría la key en el APK (extraíble por ingeniería inversa), requeriría internet en cada reproducción, y rompería el diseño "100% local, sin backend" ya comunicado al usuario en `ANDROID_STUDIO_SETUP.md`.
- Consecuencias: A-024 y A-025 quedan resueltos con audio real, no con un sustituto. `VoiceRecordCard` (grabación de la propia voz, ADR-006) se mantiene sin cambios — ahora la app ofrece primero "escuchar la narración real" y después "grábate practicando", que es el flujo original que pedía el paquete de auditoría.
- Riesgos: la voz es sintética (TTS), no una locución humana profesional; el pronunciador del "es-US" habla el fragmento en inglés con acento del modelo, no con pronunciación nativa perfecta — aceptable para el propósito (frase clave + guía fonética en español), pero debe evaluarse con oídos humanos antes de cualquier lanzamiento. Los archivos no se pudieron probar dentro de un build real de la app (ver ADR-001) — solo se verificó que el MP3 se genera y decodifica correctamente (`ffprobe`/tamaño de archivo), no que `MediaPlayer.create` los reproduzca sin errores en un dispositivo real.
- Revisión futura: escuchar los 13 episodios en Android Studio/emulador y, si la pronunciación del inglés dentro de la voz `es-US` no es lo bastante clara, evaluar generar la frase clave con una voz `en-US` separada y concatenar, o volver a grabación humana.

## Riesgos de verificación (para la primera compilación real)

Suposiciones hechas sin poder verificarlas, en orden de riesgo:

1. Los nombres de íconos usados (`Icons.Outlined.CalendarMonth`, `Headphones`, `Work`, `Schedule`, `Warning`, `CheckCircle`, `Lock`, etc.) existen en `androidx.compose.material:material-icons-extended` en la versión resuelta por el Compose BOM 2024.09.02. Son íconos clásicos y de uso muy común; el riesgo es bajo pero no nulo.
2. `LinearProgressIndicator(progress = { ... }, ...)` (API de lambda, no `Float` directo) es la firma estable en la versión de `material3` que trae ese BOM. Si el BOM resuelve una versión de material3 anterior a 1.2, esa firma no existiría y habría que volver a `progress: Float`.
3. `ColorScheme` acepta los parámetros `primaryFixed`, `primaryFixedDim`, `onPrimaryFixed`, `onPrimaryFixedVariant` (y sus equivalentes secondary/tertiary) en `lightColorScheme(...)` — añadidos en una versión de material3 relativamente reciente. Mismo riesgo que el punto 2.
4. `androidx.datastore:datastore-preferences:1.1.1` es compatible con `compileSdk 34` / Kotlin 2.0.21 sin fricción — es una suposición razonable (es una librería muy estable) pero no verificada aquí.
5. El patrón de navegación inferior (`popUpTo(navController.graph.findStartDestination().id) { saveState = true }`) usa `androidx.navigation.NavGraph.Companion.findStartDestination`, disponible en `navigation-compose:2.8.1`.
6. `androidx.room:room-compiler:2.6.1` vía KSP `2.0.21-1.0.27` (ver ADR-007) — el par de versiones exacto no se pudo confirmar. `@Upsert` en los DAOs es una anotación de Room 2.5+, debería existir en 2.6.1.
7. `rememberSaveable { mutableIntStateOf(0) }` / `mutableStateOf(false)` (usados para el temporizador de la sesión diaria) usan los savers integrados de Compose para `MutableIntState`/`MutableState` — estándar desde hace varias versiones de `androidx.compose.runtime`, riesgo bajo.
8. `@Suppress("DEPRECATION") MediaRecorder()` (constructor sin argumentos) en `audio/VoiceRecorderController.kt` — es la API correcta para `minSdk 26` (el constructor `MediaRecorder(Context)` requiere API 31); si Android Studio marca la deprecación como error en vez de advertencia por la configuración de lint del proyecto, hay que ajustar `lint` o usar `MediaRecorder(context)` con una rama condicional por versión de SDK.
9. `Icons.Outlined.PlayCircle` y `Icons.Outlined.StopCircle` (usados en `audio/AudioNarrationController.kt`'s consumidores, `AudioLeccionesScreen.kt` y `BibliotecaAudiosScreen.kt`) — nombres estándar de Material Icons, se asume que existen en `material-icons-extended` en la versión resuelta por el BOM, igual que el riesgo 1, pero no verificados por build.
10. `android.media.MediaPlayer.create(context, resId)` leyendo un `.mp3` en `res/raw/` (ver ADR-008) — es la forma estándar y muy estable del framework para reproducir un recurso de audio corto embebido; el riesgo real no es la API en sí, sino que los 13 archivos MP3 generados por Cloud Text-to-Speech no se pudieron reproducir en un dispositivo/emulador real en este entorno para confirmar que decodifican sin error.

Ninguno de estos se pudo confirmar compilando. Revisar esta lista primero si el primer `./gradlew :app:assembleDebug` en Android Studio falla.
