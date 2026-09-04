# Caregiver Pro CA — Android (nativo)

App nativa de Android para el plan de entrenamiento de 90 días, construida en **Kotlin + Jetpack Compose**. El diseño parte del export de Stitch en la raíz del repo (`/screens/*.html`, `/docs/design/DESIGN.md`); el contenido legal y de rutas profesionales viene del paquete de auditoría `caregiver_pro_ca_android_upgrade_pack_v1` (ver `/docs/caregiver_upgrade/`).

## Estado actual

Navegación y contenido legal reales; motor de aprendizaje, audio y grabación todavía no:

- ✅ Onboarding con selección explícita de una de **6 rutas** (cuidador familiar, proveedor IHSS, HCA afiliado a HCO, cuidador privado, HHA, CNA) — ninguna se trata como sinónimo de otra.
- ✅ Shell de navegación estable de **5 pestañas**: Hoy / Plan / Práctica / Audio / Carrera (`ui/navigation/MainTab.kt`).
- ✅ Contenido legal (requisitos, recursos oficiales, avisos) copiado verbatim del paquete de auditoría y filtrado por la ruta elegida — no hay texto que mezcle HCA/IHSS/HHA/CNA ni afirmaciones sin fuente (ver `docs/caregiver_upgrade/REGULATORY_CONTENT_REPORT.md`).
- ✅ La ruta elegida y el estado de onboarding se persisten de verdad con DataStore (sobreviven cerrar y reabrir la app).
- ✅ El sistema de diseño de `docs/design/DESIGN.md` traducido a un `ColorScheme`/`Typography` de Material 3 (`ui/theme/`).
- ❌ Sin motor de aprendizaje real: los botones de sesión/repetición espaciada no hacen nada todavía.
- ❌ Sin audio ni grabación real (Media3/MediaRecorder no están integrados).
- ❌ Sin Room: el progreso de aprendizaje (intentos, cola de repaso) no se persiste — solo la ruta elegida.
- ❌ Sin pruebas automatizadas.

Ver `docs/caregiver_upgrade/FINAL_AUDIT.md` para el estado fila por fila contra las 45 correcciones del paquete de auditoría, y `docs/caregiver_upgrade/DECISIONS.md` para las decisiones de arquitectura y sus razones.

## Cómo abrirlo

1. Instala [Android Studio](https://developer.android.com/studio) (incluye el SDK de Android).
2. `Open` → selecciona la carpeta `android/` de este repo (no la raíz del repo).
3. Deja que Android Studio sincronice Gradle la primera vez (descarga el SDK/dependencias).
4. Ejecuta en un emulador o teléfono conectado (▶️ `app`).

> **Nota:** este proyecto se generó y revisó en un entorno sin acceso a `dl.google.com` (el dominio que sirve el Android SDK y el Android Gradle Plugin), así que **no se pudo compilar aquí en ninguna de las dos sesiones de trabajo**. El código fue revisado a mano con mucho cuidado (ver `docs/caregiver_upgrade/TEST_REPORT.md`), pero la primera sincronización en Android Studio es el primer build real — si algo no compila, revisa primero la sección "Riesgos de verificación" de `docs/caregiver_upgrade/DECISIONS.md`.

## Estructura

```
app/src/main/java/com/caregiverproca/app/
├── MainActivity.kt
├── content/             Pathway.kt, LegalRequirements.kt, OfficialResources.kt, Disclaimers.kt
│                          — contenido legal/de rutas, verbatim del paquete de auditoría
├── data/                UserPreferencesRepository.kt — persistencia DataStore (ruta, onboarding, progreso mínimo)
├── ui/theme/            Color.kt, Type.kt, Shape.kt, Theme.kt — el sistema de diseño
├── ui/navigation/       MainTab.kt (las 5 pestañas), Screen.kt (8 pantallas empujadas), CaregiverNavHost.kt
├── ui/components/       SectionCard, StatusPill, NumberedStep, ChecklistRow, LabeledProgress,
│                          DetailScaffold (con flecha atrás), HubScaffold (sin flecha, para tabs),
│                          ScreenNavCard, DisclaimerBanner
├── ui/screens/          Rutina Diaria (tab Hoy), Plan 90 Días (tab Plan) y las 8 pantallas empujadas
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

1. **Compilar de verdad**: abrir en Android Studio con internet normal y resolver cualquier error de compilación (ver "Riesgos de verificación" en `DECISIONS.md`).
2. **Currículo completo**: importar las 90 filas de `CURRICULO_90_DIAS.csv` del paquete de auditoría en vez de la semana de muestra actual en Plan90DiasScreen.
3. **Motor de aprendizaje**: implementar Fase 4 del prompt maestro (recuerdo activo, práctica deliberada, repetición espaciada) — esto es lo que justificaría introducir Room.
4. **Audio y grabación reales**: Media3 para Audio Lecciones/Biblioteca, MediaRecorder con flujo de consentimiento para Role Play/Turno.
5. **Fuentes reales**: agregar los `.ttf` de Manrope y JetBrains Mono en `app/src/main/res/font/` (ahora mismo usa las fuentes del sistema).
6. **Localización a inglés**: mover el texto de UI a recursos (`strings.xml`) para que el "inglés de apoyo" que pide el producto sea real, no solo los campos `titleEn` que ya existen en `content/Pathway.kt`.
7. **Pruebas automatizadas**: una vez que compile, añadir JUnit/Compose UI tests (A-043 en la auditoría).
