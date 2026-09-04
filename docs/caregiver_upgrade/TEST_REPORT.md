# Informe de pruebas — Caregiver Pro CA (Android)

Fecha: 2026-09-04
Commit: ver `git log --oneline -1` en el momento de este commit.

## Entorno

| Variable | Valor |
|---|---|
| Gradle/JDK | Gradle 8.7 (wrapper), JDK 21.0.10 (OpenJDK) |
| Android API/device | Ninguno disponible — no hay Android SDK instalado ni emulador en este entorno |
| Locale | N/A (no se pudo ejecutar la app) |
| Font scale | N/A |
| Network | Proxy corporativo del sandbox; `dl.google.com` denegado explícitamente por política (`connect_rejected`, 403 a CONNECT). `repo.maven.apache.org` y `services.gradle.org` sí accesibles. |

## Comandos automatizados

| Comando | Exit code | Resultado | Artefacto/log |
|---|---:|---|---|
| `sha256sum -c SHA256SUMS.txt` (paquete de requisitos) | 0 | 86/86 archivos OK | ver `BASELINE_AUDIT.md` |
| `cd android && ./gradlew tasks --offline` (antes del lote) | 1 (BUILD FAILED) | Falla en resolución del plugin AGP 8.6.0 — `dl.google.com` bloqueado | salida completa en `BASELINE_AUDIT.md` |
| `cd android && ./gradlew tasks --offline` (después del lote, mismo comando) | 1 (BUILD FAILED) | Falla en el **mismo punto exacto** — confirma que los cambios a `libs.versions.toml`/`build.gradle.kts` no rompieron el parseo de Gradle antes de llegar al bloqueo de red | ver salida en esta sesión |
| `grep -rn "onClick = { }" app/src/main/java` | 0 | 11 coincidencias — botones sin acción real, documentados abajo | — |
| `grep -rniE "TODO\|FIXME\|lorem ipsum" app/src/main/java` | 1 (sin coincidencias reales) | Sin TODO/FIXME/lorem — los "TODO" que aparecieron eran falsos positivos de la palabra "Todos" | — |
| `grep -rniE "examen HCA\|certificación activa\|certificación IHSS\|IA en vivo\|110h acreditadas\|CDSS Title 22\|1796\.43\|WIC.?15610\|Handoff Oficial\|Bitácora Oficial\|Protocolo CDSS\|Política CDSS\|89%\|\+35%\|\+20%\|salario garantizado\|examen Guardian\|licencia HCA\|certificación HCA" app/src/main/java` | 0 | Sin coincidencias en texto de UI (solo en comentarios de código que documentan la corrección) | ver salida en esta sesión |
| `grep -rniE "http://\|https://" app/src/main/java/com/caregiverproca/app/ui` (excluye `content/`) | 1 (sin coincidencias) | Sin URLs hardcodeadas fuera de `content/OfficialResources.kt` (donde se espera que estén) | — |
| `grep -rniE "api[_-]?key\|secret\|password\s*=\|token\s*="` | 1 (sin coincidencias) | Sin secretos hardcodeados | — |
| brace/paren balance por archivo (script bash) | — | 0 archivos con desbalance en 22 archivos `.kt` existentes + todos los nuevos | — |
| chequeo de imports duplicados por archivo | — | 0 duplicados | — |

**No se pudo ejecutar**: `./gradlew :app:assembleDebug`, `:app:testDebugUnitTest`, `:app:lint`, `:app:connectedAndroidTest`, ni cualquier prueba de UI/instrumentación — todas requieren el Android SDK, que este entorno no puede descargar (ver `BASELINE_AUDIT.md`).

## Matriz T-001 a T-050

No existe una suite de pruebas automatizadas en el proyecto (A-043 sigue sin resolver). No hay tests unitarios ni de UI que ejecutar — el árbol `app/src/test` y `app/src/androidTest` está vacío. Por lo tanto no hay resultados T-00N que reportar; el trabajo de esta sesión fue 100% revisión manual + búsquedas de regresión (arriba).

## Pruebas especiales

### Modo avión

Bloqueado — no hay audio real implementado todavía (A-024/A-025 pendientes), así que no hay nada que probar en modo avión.

### Process death

Bloqueado por falta de emulador/dispositivo. Revisión de código: `UserPreferencesRepository` usa DataStore (que persiste a disco, no a memoria de proceso), así que `pathwayId`/`onboardingCompleted` deberían sobrevivir un `process death` real — pero esto es una expectativa basada en el comportamiento documentado de DataStore, no una prueba ejecutada.

### Accesibilidad

Bloqueado por falta de dispositivo/TalkBack. Revisión de código: los botones e íconos interactivos son `Button`/`IconButton`/`clickable` de Compose (semántica de rol automática), no contenedores sin rol. `LabeledProgress` y `ChecklistRow` no dependen solo de color. No se verificó contraste real ni escalado de fuente al 200%.

### Migración

No aplica — no hay base de datos Room todavía (ver `DECISIONS.md` ADR-002).

## Fallos o flakes

Ninguno registrado — no se ejecutó ninguna prueba automatizada que pudiera ser intermitente.

## Botones/acciones sin implementar (A-020, remanente)

Inventario honesto de controles primarios sin acción real todavía (documentados en el código con un comentario explicando por qué, no ocultos):

| Pantalla | Control | Motivo |
|---|---|---|
| Hoy (RutinaDiariaScreen) | "Iniciar Sesión" | Fase 4 (motor de aprendizaje) no implementada |
| Audio Lecciones | "-15s" / "Pausa" / "+30s" | Fase 6 (Media3) no implementada |
| Biblioteca de Audios | "Descargar Fase 1 Completa" | Fase 6 (descargas offline) no implementada |
| Analizador Vocacional | "Calibrar" | Cuestionario de recalibración no implementado |
| Recursos por Semana | "Ver Guía" / "Comenzar" / "Sedes CA" (por recurso) | Custom Tabs (Fase 2, AndroidX Browser) no implementada |
| Role Play Bilingüe | "Escuchar Audio" | Fase 6 (Media3) no implementada |
| Role Play Bilingüe | "Toca para regrabar" | Fase 6 (MediaRecorder) no implementada |
| Simulación de Turno | "Pausar / Finalizar Audio de Entrega" | Fase 6 (MediaRecorder) no implementada |
| Escenarios y Flashcards | "Ver Ficha de Aprendizaje" | Contenido de ficha detallada no implementado |
| Escenarios y Flashcards | Difícil/Bien/Fácil (SM-2) | Fase 4 (repetición espaciada) no implementada |

Estos 11 controles son deuda conocida, no funciones fingidas como reales — ninguno muestra un estado de "completado"/"descargado"/"guardado" falso al presionarse (simplemente no hacen nada todavía).

## Conclusión

No se puede marcar `pass` en ningún caso que requiera compilar, ejecutar o instrumentar la app — todo eso está `blocked` por la falta de Android SDK en este entorno (ver `BASELINE_AUDIT.md` para la evidencia exacta del bloqueo de red). Lo que sí se completó y tiene evidencia real: integridad del paquete de requisitos, ausencia de regresiones de texto/frases prohibidas, ausencia de secretos, balance sintáctico de todos los archivos Kotlin, y que el parseo de Gradle llega exactamente al mismo punto antes y después de los cambios (es decir, los archivos de build modificados no introdujeron un error de sintaxis Kotlin/TOML nuevo). La primera compilación real debe hacerse en Android Studio.
