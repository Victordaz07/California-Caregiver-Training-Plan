# Caregiver Pro CA — Android (nativo)

App nativa de Android para el plan de entrenamiento de 90 días, construida en **Kotlin + Jetpack Compose** a partir del diseño de Stitch que vive en la raíz del repo (`/screens/*.html`, `/docs/design/DESIGN.md`).

## Estado actual: esqueleto funcional

Esta primera versión es un **esqueleto navegable**, no la app completa:

- ✅ Las 10 pantallas del plan, con navegación real entre ellas (Compose Navigation).
- ✅ El sistema de diseño aplicado de verdad: los colores/tipografía/formas de `docs/design/DESIGN.md` están traducidos a un `ColorScheme` de Material 3 (`ui/theme/`).
- ✅ Contenido representativo de cada pantalla (textos reales tomados del export de Stitch).
- ❌ Sin lógica real todavía: los temporizadores, la grabación de audio, la reproducción, el guardado de progreso y las llamadas a IA son visuales/estáticos (botones que no hacen nada aún).
- ❌ Sin persistencia ni backend: no hay base de datos local ni cuentas de usuario.

## Cómo abrirlo

1. Instala [Android Studio](https://developer.android.com/studio) (incluye el SDK de Android).
2. `Open` → selecciona la carpeta `android/` de este repo (no la raíz del repo).
3. Deja que Android Studio sincronice Gradle la primera vez (descarga el SDK/dependencias).
4. Ejecuta en un emulador o teléfono conectado (▶️ `app`).

> **Nota:** este proyecto se generó y revisó en un entorno sin acceso a `dl.google.com` (el dominio que sirve el Android SDK y el Android Gradle Plugin), así que **no se pudo compilar aquí**. El código fue revisado a mano con cuidado, pero la primera sincronización en Android Studio es el primer build real — si algo no compila, es el siguiente paso a resolver.

## Estructura

```
app/src/main/java/com/caregiverproca/app/
├── MainActivity.kt
├── ui/theme/          Color.kt, Type.kt, Shape.kt, Theme.kt — el sistema de diseño
├── ui/navigation/      Screen.kt (las 10 rutas) + CaregiverNavHost.kt
├── ui/components/       SectionCard, StatusPill, NumberedStep, ChecklistRow, LabeledProgress, DetailScaffold
└── ui/screens/          Una pantalla por archivo, una por cada screens/*.html de la raíz
```

## Próximos pasos sugeridos (en orden)

1. **Fuentes reales**: agregar los `.ttf` de Manrope y JetBrains Mono en `app/src/main/res/font/` y conectarlos en `ui/theme/Type.kt` (ahora mismo usa las fuentes del sistema).
2. **Estado y persistencia**: elegir una fuente de verdad para el progreso del plan de 90 días (Room o DataStore) en vez de los valores fijos que hay ahora en cada pantalla.
3. **Rutina Diaria**: conectar el botón "Iniciar Sesión" a un temporizador real de 75 minutos.
4. **Audio**: implementar reproducción real (ExoPlayer/Media3) para Audio Lecciones y Biblioteca de Audios, y grabación real (MediaRecorder) para Simulación de Turno y Role Play Bilingüe.
5. **Contenido real**: reemplazar el contenido de ejemplo por el contenido oficial (lecciones, audios, enlaces a CDSS/IHSS).
