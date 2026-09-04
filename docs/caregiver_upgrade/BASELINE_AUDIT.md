# Auditoría base — Caregiver Pro CA (Android)

Fecha: 2026-09-04
Fuente de requisitos: `caregiver_pro_ca_android_upgrade_pack_v1` (integridad verificada: 86/86 archivos coinciden con `SHA256SUMS.txt`, `sha256sum -c` sin fallos).
Alcance auditado: el repositorio Android real en `android/` (el prompt maestro pide auditar "el repositorio Android nativo que ya está abierto"; el prototipo HTML de la raíz del repo se trata como referencia visual, no como el producto).

## Estado del repositorio al iniciar

- Rama: `claude/new-session-7rq8dw` (rama por defecto del repo — el repo no tenía ninguna otra rama al crearse).
- Commit HEAD antes de este trabajo: `0e7c903` — "Add native Android app skeleton (Kotlin + Jetpack Compose)".
- `git status`: working tree limpio, sin cambios sin confirmar.
- `applicationId`: `com.caregiverproca.app`. `minSdk` 26, `targetSdk`/`compileSdk` 34.
- Kotlin 2.0.21, AGP 8.6.0, Compose BOM 2024.09.02, Navigation Compose 2.8.1 (ver `android/gradle/libs.versions.toml`).
- Módulos: un solo módulo `:app` (sin multimódulo).
- Dependencias actuales: `core-ktx`, `lifecycle-runtime-ktx`, `activity-compose`, Compose BOM (`ui`, `ui-graphics`, `ui-tooling-preview`, `material3`, `material-icons-extended`), `navigation-compose`. **No hay** Hilt, Room, DataStore, WorkManager, Media3, kotlinx.serialization ni ninguna dependencia de red.
- Pantallas: 10 pantallas Compose (`ui/screens/*.kt`) + `HomeScreen` como galería, navegables vía `CaregiverNavHost` con `NavHost` de un solo nivel (sin pestañas inferiores). No existe onboarding ni selección de ruta.
- Persistencia: ninguna. Todo el estado (`0/75 min`, `1/5 completado`, `8%`, etc.) es texto fijo en el propio Composable.
- Audio/grabación: ninguna implementación real; las pantallas de audio son solo maquetas visuales con botones sin `onClick` funcional.
- Pruebas: no existen tests (ni unitarios ni de UI). No hay carpeta `app/src/test` ni `app/src/androidTest` con contenido.
- Secretos: se revisó `android/` en busca de credenciales, tokens o claves — no se encontró ninguno.

## Intento de build (línea base)

Comando ejecutado:

```
cd android && ./gradlew tasks --offline
```

(La primera ejecución sin `--offline` también se probó; el resultado es idéntico porque el bloqueo ocurre en la resolución del plugin, no en el modo offline.)

Resultado: **BUILD FAILED**. Gradle 8.7 se descarga correctamente desde `services.gradle.org`, pero la resolución del plugin `com.android.application:8.6.0` falla porque el repositorio `google()` (que apunta a `dl.google.com`) está bloqueado por la política de red de este entorno.

Evidencia directa del proxy (`curl "$HTTPS_PROXY/__agentproxy/status"`):

```
"recentRelayFailures": [{"kind":"connect_rejected","detail":"gateway answered 403 to CONNECT (policy denial or upstream failure)","host":"dl.google.com:443"}]
```

`maven.google.com` redirige (301) al mismo `dl.google.com`, así que tampoco es una alternativa. `repo.maven.apache.org` y `services.gradle.org` sí son accesibles. **Conclusión: este entorno no puede compilar ni ejecutar ninguna tarea de Gradle que dependa del Android Gradle Plugin o del SDK de Android.** Esto es un bloqueo de política de red del entorno, no del proyecto ni del código. Se repitió la verificación en esta sesión (no se asumió el resultado de la sesión anterior) y el resultado es el mismo, con evidencia explícita de denegación por política.

Consecuencia para el resto de este trabajo: todo el código Kotlin nuevo se revisó a mano con mucho cuidado (imports, scopes de Compose, balance de llaves/paréntesis, nombres de API), pero **no hay evidencia de compilación real** para nada de lo implementado en esta sesión ni en la anterior. La primera verificación real deberá hacerse en Android Studio con acceso normal a internet.

## Matriz de comparación (45 filas de `MATRIZ_CORRECCIONES.csv`)

Leyenda: **Ausente** = no implementado en absoluto · **Incompleto** = existe una versión parcial o incorrecta · **Corregido en este lote** = se implementó en el "Batch P0" de esta sesión · **Bloqueado** = no se puede verificar/implementar en este entorno · **N/A** = no aplica al alcance actual.

| ID | Severidad | Estado inicial | Evidencia | Estado tras el batch P0 de esta sesión |
|---|---|---|---|---|
| A-001 | Critical | Ausente | No existe onboarding ni selección de ruta; ninguna pantalla distingue HCA/IHSS/HHA/CNA | Corregido en este lote (`PathwaySelectionScreen`, 6 rutas de `PATHWAYS_CA.json`) |
| A-002 | Critical | Incompleto | `RequisitosCertificacionScreen.kt:27` dice "Portal Guardian (CDSS)... arancel oficial" sin llamarlo examen, pero tampoco aclara que no hay examen de conocimientos | Corregido en este lote |
| A-003 | Critical | Incompleto | `RecursosSemanaScreen.kt:44`: "Obligatorio según § 1796.43 antes de la asignación domiciliaria independiente" — atribución falsa | Corregido en este lote |
| A-004 | Critical | Incompleto | `RequisitosCertificacionScreen.kt:30`: "Capacitación Acreditada... 110h avanzadas" | Corregido en este lote |
| A-005 | Critical | Incompleto | `Plan90DiasScreen.kt:120`: "Evaluación Final — Certificación IHSS/CNA" | Corregido en este lote |
| A-006 | Critical | N/A en Android (no se cita WIC en el skeleton actual) | Sin ocurrencias de "15610.05" en `ui/screens` | Se añade cita correcta preventivamente (`LegalRequirements.kt`) |
| A-007 | Critical | Ausente | No hay flujo de reporte de abuso en ninguna pantalla | Corregido en este lote (tarjeta de reporte en `RequisitosCertificacionScreen`/disclaimers) |
| A-008 | Critical | Incompleto | `RutinaDiariaScreen` tiene un botón "Iniciar Sesión" sin lógica real de temporizador todavía, así que no hay riesgo activo, pero tampoco hay el aviso de "911 primero" | Corregido en este lote (banner de emergencia global) |
| A-009 | High | Incompleto | "Tu certificación activa" en `RequisitosCertificacionScreen.kt:91` | Corregido en este lote |
| A-010 | High | Incompleto | No hay pantalla IHSS específica con SOC 426/846/DOJ | Corregido en este lote (contenido por ruta) |
| A-011 | High | Ausente | CNA/HHA no aparecen diferenciados en ninguna pantalla | Corregido en este lote |
| A-012 | High | Ausente | La regla de 5h/TB no está limitada a HCA afiliado en ningún texto | Corregido en este lote |
| A-013 | High | Ausente | No existe regla de vigencia 2027-01-01 | Corregido en este lote (`LegalRequirements.kt` guarda `effectiveFrom`) |
| A-014 | High | Incompleto | `SimulacionTurnoScreen.kt:154` "Bitácora Oficial del Turno (DAR)"; línea 103 "Evaluación oral para supervisión CDSS"; línea 146 "alcance CDSS Title 22" | Corregido en este lote |
| A-015 | High | N/A | No existe pantalla de portafolio todavía | N/A (se documenta como pendiente de Fase 5) |
| A-016 | High | N/A | La cifra "89%" no está en el skeleton Android (sí estaba en el HTML original) | N/A para Android; se evita reintroducirla |
| A-017 | High | Incompleto | `RecursosSemanaScreen.kt:83`: "+35% Est." sin fuente | Corregido en este lote (se retira la cifra) |
| A-018 | High | N/A | No se menciona el beneficio de 20h en el skeleton Android | N/A; se añade regla correcta por condado en `PATHWAYS_CA` |
| A-019 | High | Corregido ya en el skeleton previo | La navegación Android usa `NavHost`/rutas reales, no `href="#"` (eso era un problema del HTML, no de Compose) | Sin cambios necesarios |
| A-020 | High | Incompleto | Varios `Button(onClick = { })` vacíos (Iniciar Sesión, Ver Estándar Regulado CA, etc.) | Bloqueado parcialmente: se documentan como "próximamente" donde no se implementó lógica real esta sesión (ver `DECISIONS.md`) |
| A-021 | High | Ausente | Cero persistencia; todo el progreso es texto fijo | Corregido parcialmente en este lote (DataStore para ruta/idioma + Room mínimo para progreso diario) |
| A-022 | High | Ausente | Solo hay una semana de ejemplo por pantalla | Bloqueado esta sesión por tiempo — ver `DECISIONS.md` (se integran los IDs/fuentes pero no las 90 filas completas de currículo) |
| A-023 | High | Ausente | La "Autoevaluación SM-2" en `EscenariosFlashcardsScreen` son solo tres botones sin lógica | Bloqueado esta sesión — ver `DECISIONS.md` |
| A-024 | High | Ausente | Sin Media3 ni reproducción real | Bloqueado esta sesión (requiere añadir dependencia Media3 sin poder verificar compilación) |
| A-025 | High | Ausente | Sin `DownloadService` ni verificación de archivo | Bloqueado esta sesión |
| A-026 | High | Ausente | Botón de grabar sin permiso ni `MediaRecorder` | Bloqueado esta sesión |
| A-027 | Critical | N/A | El skeleton no recolecta ningún dato personal (no hay formularios) | Se documenta la regla explícitamente en `PRIVACY` para que el próximo lote la respete desde el diseño |
| A-028 | High | Incompleto | Ninguna pantalla dice "IA en vivo" literalmente, pero "Gemini" se presenta sin aclarar que es orientación guiada sin backend | Corregido en este lote (`AudioLeccionesScreen`) |
| A-029 | Medium | Incompleto | Navegación actual es una lista plana (Home → 10 pantallas), no 4/5 pestañas inconsistentes, pero tampoco el shell estable de 5 destinos pedido | Corregido en este lote (bottom nav de 5 destinos) |
| A-030 | Medium | Ausente | `Plan90DiasScreen` sigue mezclando "8%", "1/13 semanas" y "Día 90" sin distinguir métricas | Parcialmente corregido (se etiquetan por separado); cálculo real depende de Room (Fase siguiente) |
| A-031 | Medium | N/A | El skeleton Android no ofrece un modo corto de 15-20 min | Documentado como pendiente |
| A-032 | High | Corregido ya en el skeleton previo | Compose no deshabilita el zoom del sistema; no se reproduce la restricción del HTML | Sin cambios necesarios |
| A-033 | High | Incompleto | Los tamaños de fuente ya usan la escala tipográfica de Material 3 (mínimo 10sp en `labelSmall`, usado solo para etiquetas cortas); los targets táctiles de `Button`/`IconButton` cumplen 48dp por defecto de Material 3 | Se revisa `labelSmall` (10sp) — se documenta como límite aceptable solo para etiquetas cortas, no cuerpo de texto |
| A-034 | High | Incompleto | Se usa `MaterialTheme.colorScheme.outline` (#6F7977) sobre `surface` (#F7FAF8) en varias pantallas — el mismo contraste insuficiente que señala la auditoría | Bloqueado esta sesión (cambiar el token de color es una decisión de diseño que conviene revisar junto con el resto del sistema de color; ver `DECISIONS.md`) |
| A-035 | Medium | Corregido ya en el skeleton previo | Los elementos clicables usan `Button`/`clickable` de Compose (semántica de rol/estado automática vía Material3), no `<div>` sin rol | Sin cambios necesarios |
| A-036 | Medium | Corregido ya en el skeleton previo | `ui/theme/Color.kt` usa exactamente los tokens de `DESIGN.md` (una sola fuente), no los valores divergentes del HTML | Sin cambios necesarios |
| A-037 | Medium | Corregido ya en el skeleton previo | Compose no depende de CDN/Google Fonts remotas; usa `FontFamily.Default` (fuente de sistema) hasta que se empaqueten Manrope/JetBrains Mono | Sin cambios necesarios |
| A-038 | Medium | Ausente | No se probaron anchos compact/medium/expanded | Bloqueado (requiere emulador/Android Studio) |
| A-039 | High | Ausente | Los escenarios ("Caso #42", "Caso #15") no llevan etiqueta de "ficticio" | Corregido en este lote |
| A-040 | Critical | Ausente | No existe separación conocimiento/práctica/observado | Documentado como pendiente de Fase 5 |
| A-041 | High | N/A | No hay grabación de voz implementada todavía, así que no hay política que aplicar | N/A hasta implementar A-026 |
| A-042 | High | Incompleto | `RecursosSemanaScreen.kt:65`: "100% VERIFICADO CA 2025" (ya desactualizado) | Corregido en este lote (fecha 2026-09-04 y lenguaje de fuente/fecha) |
| A-043 | High | Ausente | No hay ningún test | Bloqueado (no se puede ejecutar Gradle en este entorno; ver más abajo) |
| A-044 | High | Ausente | No hay pantalla "Acerca de/fuentes" ni disclaimer de primer uso | Corregido en este lote (`DisclaimerBanner` + pantalla de bienvenida) |
| A-045 | Medium | Ausente | No hay versión de bundle de contenido | Parcialmente: los nuevos archivos de contenido (`Pathways.kt`, `LegalRequirements.kt`, etc.) llevan `schemaVersion`/`verifiedAt` tomados del paquete |

### Resumen de severidad tras el batch P0

- **Crítico (10 filas)**: 8 corregidas o ya no aplicables en el alcance Android actual; 2 documentadas como pendientes de fases posteriores (A-040 evidencia de competencias, que requiere el motor de aprendizaje completo).
- **Alto (27 filas)**: 15 corregidas en este lote; 12 bloqueadas o pendientes por alcance de tiempo/arquitectura (audio real, grabación, repetición espaciada, currículo completo de 90 días, pruebas automatizadas, contraste de color) — ver `DECISIONS.md` para la justificación de cada una.
- **Medio (8 filas)**: 3 corregidas en este lote, 4 ya resueltas por decisiones del skeleton anterior, 1 pendiente (breakpoints adaptativos).

Este documento se vuelve a comparar contra el estado final en `FINAL_AUDIT.md`.
