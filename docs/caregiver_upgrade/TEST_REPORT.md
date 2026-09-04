# Informe de pruebas — Caregiver Pro CA (Android)

Fecha: 2026-09-04 (dos lotes de trabajo el mismo día — ver `IMPLEMENTATION_CHANGELOG.md`)
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
| `cd android && ./gradlew tasks --offline` (Lote 1, antes) | 1 (BUILD FAILED) | Falla en resolución del plugin AGP 8.6.0 — `dl.google.com` bloqueado | salida completa en `BASELINE_AUDIT.md` |
| `cd android && ./gradlew tasks --offline` (Lote 1, después) | 1 (BUILD FAILED) | Mismo punto exacto de falla | — |
| `cd android && ./gradlew tasks --offline` (Lote 2, después de añadir Room/KSP) | 1 (BUILD FAILED) | **Mismo punto exacto de falla otra vez** — Gradle sigue llegando a la resolución de AGP 8.6.0 antes de fallar, lo que confirma que `libs.versions.toml` (entradas `room`, `ksp`), `android/build.gradle.kts` (alias `libs.plugins.ksp`) y `android/app/build.gradle.kts` (bloque `ksp { arg(...) }`, dependencias Room) son sintácticamente válidos — Gradle no se cae antes por un error de TOML/KTS. La versión de KSP en sí (`2.0.21-1.0.27`) sigue sin verificarse; ver `DECISIONS.md` ADR-007. | ver salida en esta sesión |
| `grep -rn "onClick = { }" app/src/main/java` | 0 | 2 coincidencias (antes 11) — ver tabla actualizada abajo | — |
| `grep -rniE "gemini\|ia en vivo"` en `ui/screens` | 0 | Solo aparece en comentarios de código explicando la corrección (A-028), no en texto de UI | — |
| `grep -rniE "examen HCA\|certificación activa\|certificación IHSS\|IA en vivo\|110h acreditadas\|CDSS Title 22\|1796\.43\|WIC.?15610\|Handoff Oficial\|Bitácora Oficial\|Protocolo CDSS\|Política CDSS\|89%\|\+35%\|\+20%\|salario garantizado\|examen Guardian\|licencia HCA\|certificación HCA"` | 0 | Sin coincidencias en texto de UI | — |
| brace/paren balance, los 50 archivos `.kt` del proyecto | — | 0 desbalances | — |
| chequeo de imports duplicados, los 50 archivos | — | 0 duplicados | — |
| `find . -iname "*.mp3" -o -iname "*.wav" -o -iname "*.m4a" -o -iname "*.ogg"` sobre el paquete de auditoría completo | — | 0 resultados — confirma que no hay audio narrado real en el paquete (ver `DECISIONS.md` ADR-006) | — |

**No se pudo ejecutar**: `./gradlew :app:assembleDebug`, `:app:testDebugUnitTest`, `:app:lint`, `:app:connectedAndroidTest`, ni cualquier prueba de UI/instrumentación — todas requieren el Android SDK, que este entorno no puede descargar (ver `BASELINE_AUDIT.md`).

## Matriz T-001 a T-050

No existe una suite de pruebas automatizadas en el proyecto (A-043 sigue sin resolver). El nuevo `domain/ReviewScheduler.kt` está escrito para ser fácilmente testeable (reloj inyectable, función pura `schedule()`), pero no se añadió ningún archivo bajo `app/src/test` en este lote — sería el primer test candidato una vez que el proyecto compile.

## Pruebas especiales

### Modo avión

Bloqueado — no hay audio narrado real (A-024/A-025 siguen pendientes, ver ADR-006). Lo que sí es real y debería sobrevivir modo avión sin ninguna prueba adicional: el contenido de `content/*.kt` (currículo, tarjetas, escenarios, guiones) está compilado en el APK, no requiere red bajo ninguna circunstancia.

### Process death

Bloqueado por falta de emulador/dispositivo. Revisión de código:
- `UserPreferencesRepository` (DataStore) y `LearningRepository` (Room) persisten a disco, no a memoria de proceso — `pathwayId`, progreso del currículo y estado de repaso deberían sobrevivir un `process death` real.
- La sesión diaria en curso (bloque activo, segundos restantes) usa `rememberSaveable`, que sobrevive rotación pero **no** un process death completo mientras el timer corre — ver `DECISIONS.md` ADR-002/ADR-007 y el comentario en `RutinaDiariaScreen.kt`. Esto es una limitación documentada, no una función fingida.

### Accesibilidad

Sin cambios desde el lote anterior — bloqueado por falta de dispositivo/TalkBack.

### Migración

Ahora aplica: `AppDatabase` está en versión 1 (primera versión, sin migraciones que probar todavía). `exportSchema = true` está configurado para que el esquema quede versionado en `android/app/schemas/` a partir del primer build real.

## Fallos o flakes

Ninguno registrado — no se ejecutó ninguna prueba automatizada que pudiera ser intermitente.

## Botones/acciones sin implementar (A-020, remanente tras el Lote 2)

| Pantalla | Control | Motivo |
|---|---|---|
| Analizador Vocacional | "Calibrar" | Cuestionario de recalibración no implementado |
| Recursos por Semana | "Ver Guía" / "Comenzar" / "Sedes CA" (por recurso) | Custom Tabs (Fase 2, AndroidX Browser) no implementada |

Los otros 9 controles del Lote 1 (temporizador de sesión, reproducción/grabación de audio, calificación SM-2, evaluación de escenarios) ahora tienen lógica real — ver `IMPLEMENTATION_CHANGELOG.md`.

## Conclusión

Sigue sin poder marcarse `pass` en ningún caso que requiera compilar, ejecutar o instrumentar la app — todo eso está `blocked` por la falta de Android SDK en este entorno. Lo nuevo en este lote: el proyecto ahora incluye Room + KSP, cuya resolución de versión es un riesgo adicional no verificable aquí (documentado en `DECISIONS.md` ADR-007) — la evidencia de que Gradle sigue llegando al mismo punto de fallo (AGP, no KSP) indica que el archivo de configuración es sintácticamente correcto, pero no confirma que la versión de KSP exista. La primera compilación real debe hacerse en Android Studio; ver `ANDROID_STUDIO_SETUP.md` para qué hacer si falla.
