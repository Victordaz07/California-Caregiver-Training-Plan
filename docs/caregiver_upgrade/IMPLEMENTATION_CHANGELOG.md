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

## Archivos retirados

| Archivo | Razón | Sustituto | Recuperable en Git |
|---|---|---|---|
| `ui/screens/HomeScreen.kt` | La galería plana de 10 pantallas se reemplaza por el shell de 5 tabs | `CaregiverNavHost.kt` + `ui/screens/hub/*` | Sí (`git show 0e7c903:...`) |

## Dependencias

| Dependencia | Versión | Motivo | Fuente de versión/licencia |
|---|---|---|---|
| `androidx.datastore:datastore-preferences` | 1.1.1 | Persistir ruta/idioma/progreso mínimo sin generación de código (ver ADR-002) | AndroidX (Apache 2.0), versión estable pública — no verificada por build en este entorno |

## Migraciones

No aplica: no había datos de usuario en ninguna versión anterior de la app (el skeleton previo no persistía nada). La primera vez que un usuario real abra esta versión, DataStore se inicializa vacío y el flujo de onboarding se ejecuta desde cero.
