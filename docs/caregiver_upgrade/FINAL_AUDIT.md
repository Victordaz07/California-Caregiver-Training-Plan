# Auditoría final de Caregiver Pro California (Android)

Fecha: 2026-09-04
Commit/rama: `claude/new-session-7rq8dw`, commit del lote descrito en `IMPLEMENTATION_CHANGELOG.md`
Auditor: sesión de Claude Code (Sonnet 5)

## Dictamen

Estado: **CONDITIONAL GO** para seguir desarrollando en Android Studio — **NO-GO para lanzamiento**.

Esta sesión cerró la mayoría de los hallazgos regulatorios Críticos y Altos que eran correcciones de **contenido/texto/estructura de navegación** (A-001 a A-020, A-028 a A-039, A-042, A-044 — ver tabla abajo). En un segundo lote de trabajo el mismo día, también se cerró el currículo completo de 90 días (A-022), la persistencia real de progreso (A-021, vía Room), la repetición espaciada (A-023), y la grabación real de voz del usuario (A-026/A-041). En un tercer lote, el usuario generó narración real con Google Cloud Text-to-Speech (fuera de la app, con su propia key) para los 13 episodios de audio, cerrando también A-024/A-025 (ver `DECISIONS.md` ADR-008) — ya no queda ningún hallazgo de audio simulado. Lo que sigue sin cerrarse es la evidencia de competencias separada (A-040) y ninguna prueba automatizada (A-043), porque este entorno no tiene acceso al Android SDK y no pudo compilar ni ejecutar nada (ver `TEST_REPORT.md`). Por eso el estado sigue sin ser GO: aunque ya no falta ninguna funcionalidad Crítica ni de audio, nada de lo escrito en esta sesión (ningún lote) tiene evidencia de compilación real, y persisten deudas Altas (A-040, A-043) y de accesibilidad (A-034).

## Línea base vs resultado

| ID matriz | Antes | Cambio | Archivo(s) | Prueba | Estado |
|---|---|---|---|---|---|
| A-001 | Sin onboarding, ninguna ruta distinguida | Onboarding + selección explícita de 6 rutas, sin ruta por defecto | `PathwaySelectionScreen.kt`, `content/Pathway.kt` | Revisión manual + grep | Resuelto (sin compilar) |
| A-002 | Guardian descrito ambiguamente | "No incluye examen de conocimientos" explícito en el requisito | `content/LegalRequirements.kt` (hca_guardian_application) | grep "examen HCA" → 0 | Resuelto (sin compilar) |
| A-003 | CPR atribuido a §1796.43 | Atribución retirada, texto "recomendado o exigido por tu empleador" | `RecursosSemanaScreen.kt`, `content/LegalRequirements.kt` | grep "1796.43" solo en contexto correcto | Resuelto (sin compilar) |
| A-004 | "110h acreditadas" | "110 horas de formación y práctica (no acreditadas)" | `RequisitosCertificacionScreen.kt`, `Plan90DiasScreen.kt` | grep "acreditad" → 0 en UI | Resuelto (sin compilar) |
| A-005 | "Certificación IHSS/CNA", "examen de estado" | "preparación interna", "certificado interno de finalización" | `Plan90DiasScreen.kt` | grep "Certificación IHSS" → 0 | Resuelto (sin compilar) |
| A-006 | No aplicaba en el skeleton Android (WIC no citado) | `LegalRequirements.kt` cita WIC §15630 correctamente, no §15610.05 para autonomía | `content/LegalRequirements.kt` | Revisión manual | Resuelto preventivamente |
| A-007 | Sin flujo de reporte de abuso | `mandated_reporting_home` con 911 primero, APS 1-833-401-0832 | `content/LegalRequirements.kt`, `RecursosSemanaScreen.kt` | Revisión manual | Resuelto (sin compilar); sin pantalla de escenario de reporte dedicada |
| A-008 | Sin aviso de emergencia junto al timer | `DisclaimerBanner(Emergency)` en Hoy, Escenarios, Turno | `RutinaDiariaScreen.kt` + otras | Revisión manual | Resuelto (sin compilar) |
| A-009 a A-012 | Lenguaje de "certificación"/"licencia" mezclado entre rutas | `RequisitosCertificacionScreen` ahora es por-ruta, con lenguaje de registro/inscripción | `RequisitosCertificacionScreen.kt` | grep de frases prohibidas → 0 | Resuelto (sin compilar) |
| A-013 | Sin versión de regla futura | `hca_dementia_2027` con `effectiveFrom` pero SIN motor de reglas por fecha | `content/LegalRequirements.kt` | — | Parcial — dato versionado, sin lógica de fecha inyectable |
| A-014 | DAR/handoff/protocolo llamados "oficiales CDSS" | Relabeled como práctica interna en 4 pantallas | `SimulacionTurnoScreen.kt`, `EscenariosFlashcardsScreen.kt`, `AudioLeccionesScreen.kt` | grep → 0 | Resuelto (sin compilar) |
| A-015 | N/A (no existe portafolio) | N/A | — | — | No aplica todavía |
| A-016 | N/A en Android (89% no estaba) | N/A | — | grep "89%" → 0 | No aplica |
| A-017/A-018 | "$38/h", "$24–$28/hora", "+35% Est." sin fuente | Cifras retiradas, reemplazadas por avisos de "varía por condado" | `AnalizadorVocacionalScreen.kt`, `RecursosSemanaScreen.kt` | grep "\+35%" → 0 | Resuelto (sin compilar) |
| A-019 | Ya resuelto en el skeleton previo (Compose Navigation, no `href=#`) | Sin cambios necesarios | — | — | Ya resuelto |
| A-020 | Muchos botones sin acción | 2 controles restantes documentados explícitamente como pendientes (no ocultos ni fingidos); los otros 9 del inventario original ya tienen lógica real | Ver `TEST_REPORT.md` | Inventario manual | Parcial — deuda restante documentada, no oculta |
| A-021 | Sin persistencia | DataStore persiste `pathwayId`/`onboardingCompleted`; Room (`AppDatabase`) persiste progreso de currículo y estado de repaso de verdad | `UserPreferencesRepository.kt`, `data/local/*.kt`, `data/LearningRepository.kt` | Revisión de código (DataStore/Room son persistentes por diseño, sin evidencia de ejecución real) | Resuelto (sin compilar) |
| A-022 | Solo semana de muestra | Currículo completo de 90 días importado desde `CURRICULO_90_DIAS.csv`, consumido por día real en `RutinaDiariaScreen` | `content/Curriculum.kt` | Revisión manual + conteo de entradas (90) | Resuelto (sin compilar) |
| A-023 | Repetición espaciada solo de etiqueta | Heurística SM-2-like real (`ReviewScheduler`, reloj inyectable), aplicada a una cola de tarjetas vencidas persistida en Room | `domain/ReviewScheduler.kt`, `data/LearningRepository.kt`, `EscenariosFlashcardsScreen.kt` | Revisión manual de la función pura `schedule()` | Resuelto (sin compilar); sin prueba unitaria todavía (ver A-043) |
| A-024/A-025 | Audio/descargas simuladas | En un tercer lote, el usuario generó narración real con Google Cloud Text-to-Speech (fuera de la app, con su propia API key restringida) para los 13 episodios; los `.mp3` resultantes se bundlearon como `res/raw/` y se reproducen con `MediaPlayer.create()` vía `audio/AudioNarrationController.kt`. Las cifras falsas de descarga se retiraron y la biblioteca se muestra honestamente como ya incluida sin conexión, ahora con reproducción real | `audio/AudioNarrationController.kt`, `content/AudioEpisodes.kt`, `AudioLeccionesScreen.kt`, `BibliotecaAudiosScreen.kt`, `res/raw/audio_w01.mp3`…`audio_w13.mp3` | Revisión manual + verificación de generación (13/13 archivos, tamaño total 4.1 MB) | Resuelto (sin compilar) — ver `DECISIONS.md` ADR-008; la key de Cloud TTS no forma parte de la app, solo se usó una vez para producir los archivos |
| A-026/A-041 | Grabación sin flujo de privacidad | `VoiceRecorderController` (MediaRecorder/MediaPlayer) + `VoiceRecordCard` con rationale in-app antes del diálogo de permiso del sistema; archivos guardados en almacenamiento privado de la app (`filesDir/voice_practice/`, nombre UUID) | `audio/VoiceRecorderController.kt`, `ui/components/VoiceRecordCard.kt`, `AndroidManifest.xml` | Revisión manual del flujo de permiso y almacenamiento | Resuelto (sin compilar); sin prueba en dispositivo real (sin micrófono disponible en este entorno) |
| A-027 | N/A (no se recolecta nada) | Regla documentada en `PRIVACY_SECURITY.md`-equivalente (ver DECISIONS.md); ningún formulario nuevo recolecta datos prohibidos | — | Revisión manual | Sin riesgo nuevo introducido |
| A-028 | "Gemini AI Audio" sin backend real | Renombrado a "Orientación Guiada" | `AudioLeccionesScreen.kt` | grep "IA en vivo" → 0 | Resuelto (sin compilar) |
| A-029 | Barra inferior inconsistente (era una galería plana) | Shell de 5 tabs estable | `MainTab.kt`, `CaregiverNavHost.kt` | Revisión manual | Resuelto (sin compilar) |
| A-030 | "8%", "1/13", "Día 90" mezclados | Métricas separadas con etiqueta y denominador explícitos | `Plan90DiasScreen.kt` | Revisión manual | Resuelto (sin compilar) |
| A-031 | N/A (no había modo corto en Android) | Sin cambios | — | — | Pendiente |
| A-032/A-035/A-036/A-037 | Ya resueltos en el skeleton previo | Sin cambios necesarios | — | — | Ya resuelto |
| A-033 | `labelSmall` a 10sp | Sin cambios — se documenta como límite aceptable solo para etiquetas cortas | — | — | Aceptado con nota (ver BASELINE_AUDIT) |
| A-034 | `outline` (#6F7977) sobre `surface` bajo 4.5:1 | Sin cambios — requiere revisión de todo el sistema de color | — | — | Pendiente |
| A-038 | Sin pruebas de breakpoints | Sin cambios — requiere emulador | — | — | Bloqueado (sin SDK) |
| A-039 | Casos sin etiqueta de ficticio | `Disclaimers.FictionalCase` en 3 pantallas de escenarios | `EscenariosFlashcardsScreen.kt`, `SimulacionTurnoScreen.kt`, `RolePlayBilingueScreen.kt` | grep de "Ficticio" presente | Resuelto (sin compilar) |
| A-040 | Sin separación conocimiento/práctica/observado | Sin cambios | — | — | Pendiente (Fase 5) |
| A-042 | "VERIFICADO CA 2025" obsoleto | "REVISADO 2026-09-04" | `RecursosSemanaScreen.kt` | grep "2025" en badge → 0 | Resuelto (sin compilar) |
| A-043 | Sin pruebas | Sin cambios — sigue sin haber ninguna prueba automatizada | — | — | Pendiente — bloqueado además por falta de SDK |
| A-044 | Sin disclaimer de primer uso | `WelcomeScreen` con los 7 disclaimers antes de continuar | `WelcomeScreen.kt` | Revisión manual | Resuelto (sin compilar) |
| A-045 | Sin versión de bundle | Contenido en Kotlin con comentarios de `schemaVersion`/`verifiedAt`, pero sin bundle descargable versionado real | `content/*.kt` | — | Parcial |

## Trazabilidad

| Requisito | Implementación | Prueba | Resultado | Evidencia |
|---|---|---|---|---|
| Onboarding de 6 rutas (A-001) | `PathwaySelectionScreen.kt` + `content/Pathway.kt` | Revisión manual de código; sin ejecución real | No verificado en dispositivo | Código en el repositorio |
| Requisitos por ruta (A-009 a A-012) | `RequisitosCertificacionScreen.kt` filtra `legalRequirementsFor(pathwayId)` | Revisión manual | No verificado en dispositivo | Código en el repositorio |
| Shell de 5 tabs (A-029) | `CaregiverNavHost.kt` + `MainTab.kt` | Revisión manual del patrón estándar de Navigation Compose | No verificado en dispositivo | Código en el repositorio |
| Persistencia de ruta (A-021 parcial) | `UserPreferencesRepository.kt` (DataStore) | Revisión manual (comportamiento documentado de DataStore) | No verificado en dispositivo | Código en el repositorio |
| Ausencia de frases prohibidas | Todas las correcciones de texto | `grep` sobre el árbol fuente completo | 0 coincidencias en texto de UI | Comandos y salidas en `TEST_REPORT.md` |

## Búsquedas de regresión

Ver la tabla de comandos en `TEST_REPORT.md` — se ejecutaron y documentaron ahí (frases prohibidas, TODO/lorem, botones sin acción, enlaces hardcodeados, secretos, balance de sintaxis, imports duplicados). No se repiten aquí para evitar duplicación.

## Asuntos abiertos

| Severidad | Problema | Impacto | Reproducción | Próxima acción |
|---|---|---|---|---|
| Crítica | Ningún archivo de este proyecto (ningún lote) tiene evidencia de compilación real | Todo el trabajo podría tener errores de tipos/API no detectables sin SDK — el riesgo creció con Room/KSP (versión sin verificar, ver ADR-007) | Abrir `android/` en Android Studio y sincronizar | Ejecutar `./gradlew :app:assembleDebug` y corregir lo que falle; ver "Riesgos de verificación" en `DECISIONS.md` y `ANDROID_STUDIO_SETUP.md` §3 |
| Media | A-024/A-025 (cerrado, con nota): la narración es voz sintética (Cloud TTS), no locución humana profesional | La pronunciación del inglés dentro de la voz `es-US` puede no ser perfectamente nativa | Escuchar cualquier episodio en Android Studio/emulador | Evaluar con oídos humanos antes de lanzamiento; si no es clara, regenerar la frase clave con voz `en-US` separada (ver `DECISIONS.md` ADR-008) |
| Alta | A-043: cero pruebas automatizadas | No hay forma de detectar regresiones futuras sin revisión manual | — | Añadir JUnit para `domain/ReviewScheduler.kt` (ya escrito con reloj inyectable para ser testeable) y Compose UI tests, una vez que haya SDK disponible para ejecutarlos |
| Alta | A-040: sin separación conocimiento/práctica/observado en la evidencia de competencias | No se puede distinguir qué tan bien domina el usuario cada habilidad más allá de "completado" | Revisar cualquier resumen de progreso | Diseñar e implementar un modelo de evidencia de 3 niveles (Fase 5 del prompt maestro) |
| Media | A-013: la regla de demencia 2027 no cambia de estado según la fecha real | La UI siempre muestra "VIGENCIA FUTURA", incluso después del 1 de enero de 2027 | Cambiar la fecha del dispositivo a 2027 (no probado) | Implementar `Clock` inyectable en `LegalRequirement` |
| Media | A-034: contraste de `outline` sobre `surface` sigue por debajo de 4.5:1 | Texto secundario poco legible para baja visión | Inspección visual con herramienta de contraste | Revisar el token en `ui/theme/Color.kt` junto con el resto del sistema de color |

## Limitaciones de la auditoría

- **Dispositivo/emulador**: ninguno disponible. Cero pantallas se vieron renderizadas realmente.
- **Red**: `dl.google.com` bloqueado por política del entorno — sin esto, no hay Android SDK, no hay AGP, no hay compilación.
- **Cuentas/backend**: no aplica — la app no tiene backend.
- **Alcance de la revisión de texto**: se corrigió el contenido de las pantallas Android (`android/app/src/main/java`). El prototipo HTML en la raíz del repositorio (`/screens/*.html`) **no se tocó** en esta sesión — sigue teniendo el mismo contenido legal incorrecto que documentó `01_AUDIT/AUDITORIA_COMPARATIVA.md`, porque el prompt maestro pide auditar explícitamente "el repositorio Android nativo", no el prototipo HTML.
