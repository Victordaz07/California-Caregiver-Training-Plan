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

## Riesgos de verificación (para la primera compilación real)

Suposiciones hechas sin poder verificarlas, en orden de riesgo:

1. Los nombres de íconos usados (`Icons.Outlined.CalendarMonth`, `Headphones`, `Work`, `Schedule`, `Warning`, `CheckCircle`, `Lock`, etc.) existen en `androidx.compose.material:material-icons-extended` en la versión resuelta por el Compose BOM 2024.09.02. Son íconos clásicos y de uso muy común; el riesgo es bajo pero no nulo.
2. `LinearProgressIndicator(progress = { ... }, ...)` (API de lambda, no `Float` directo) es la firma estable en la versión de `material3` que trae ese BOM. Si el BOM resuelve una versión de material3 anterior a 1.2, esa firma no existiría y habría que volver a `progress: Float`.
3. `ColorScheme` acepta los parámetros `primaryFixed`, `primaryFixedDim`, `onPrimaryFixed`, `onPrimaryFixedVariant` (y sus equivalentes secondary/tertiary) en `lightColorScheme(...)` — añadidos en una versión de material3 relativamente reciente. Mismo riesgo que el punto 2.
4. `androidx.datastore:datastore-preferences:1.1.1` es compatible con `compileSdk 34` / Kotlin 2.0.21 sin fricción — es una suposición razonable (es una librería muy estable) pero no verificada aquí.
5. El patrón de navegación inferior (`popUpTo(navController.graph.findStartDestination().id) { saveState = true }`) usa `androidx.navigation.NavGraph.Companion.findStartDestination`, disponible en `navigation-compose:2.8.1`.

Ninguno de estos se pudo confirmar compilando. Revisar esta lista primero si el primer `./gradlew :app:assembleDebug` en Android Studio falla.
