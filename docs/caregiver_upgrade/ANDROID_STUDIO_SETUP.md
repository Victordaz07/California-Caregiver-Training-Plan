# Cómo abrir y correr esta app (primera vez con Android Studio)

Esta guía asume que nunca has compilado una app Android. No necesitas crear ninguna cuenta de Google Cloud, Firebase, ni pedir ninguna API key — esta app es 100% local, sin backend ni servicios externos.

## 1. Instalar Android Studio

1. Descarga Android Studio desde <https://developer.android.com/studio> (versión gratuita, oficial de Google).
2. Instálalo con las opciones por defecto. El instalador te preguntará si quieres instalar el "Android SDK" y un emulador — di que sí a ambos. Esto puede tardar varios minutos y varios GB de descarga; es normal.

## 2. Abrir el proyecto

1. Abre Android Studio.
2. `File > Open...` (o el botón "Open" en la pantalla de bienvenida).
3. Selecciona la carpeta **`android/`** dentro de este repositorio — **no** la raíz del repositorio (la raíz tiene el prototipo web, no el proyecto Android).
4. Android Studio empezará a "sincronizar" (verás una barra de progreso abajo que dice "Gradle Sync"). La primera vez puede tardar varios minutos porque descarga todas las dependencias (Compose, Room, etc.).

## 3. Si la sincronización falla

Este proyecto se escribió y revisó en un entorno sin acceso a internet normal, así que **nunca se compiló de verdad hasta que tú lo abras**. Es posible (aunque no seguro) que la primera sincronización marque un error. Si eso pasa:

1. Lee el mensaje de error en la pestaña "Build" de abajo — Android Studio casi siempre te da un botón para arreglarlo automáticamente (por ejemplo "Update Gradle plugin version" o "Install missing SDK component"). Haz clic en esos botones cuando aparezcan.
2. Si el error menciona `com.google.devtools.ksp` (el plugin de KSP que usa Room), es el riesgo que ya anticipamos en `docs/caregiver_upgrade/DECISIONS.md` — la versión exacta `2.0.21-1.0.27` que puse en `android/gradle/libs.versions.toml` no se pudo verificar sin internet. Solución: en Android Studio, click derecho sobre `libs.versions.toml` → o simplemente cambia la línea `ksp = "2.0.21-1.0.27"` por la versión que Android Studio te sugiera (el propio IDE suele sugerir la versión correcta en el mensaje de error).
3. Si el error es de otro tipo, cópialo y pégamelo en una conversación conmigo (Claude) — con el mensaje exacto puedo arreglarlo directamente en el código.

## 4. Correr la app

1. Necesitas un dispositivo: un **emulador** (una versión virtual de Android que corre en tu computadora) o un **teléfono Android real** conectado por USB con "Depuración USB" activada en Ajustes > Opciones de desarrollador.
2. Para crear un emulador: `Tools > Device Manager > Create Device`, elige cualquier teléfono moderno (por ejemplo "Pixel 8") y una imagen de sistema reciente (Android 14 / API 34).
3. Con el emulador o teléfono seleccionado arriba, haz clic en el botón ▶️ verde ("Run 'app'") o `Shift+F10`.
4. La primera vez que abras la app en un dispositivo/emulador nuevo, verás la pantalla de bienvenida (onboarding) y tendrás que elegir tu ruta (familiar, IHSS, HCA, etc.) — eso es normal, es la primera vez que se ejecuta.

## 5. El permiso de micrófono

Cuando llegues a "Práctica" (Role Play o Simulación de Turno) y toques "Grabar" por primera vez, verás primero una tarjeta explicando para qué es el micrófono (no es un diálogo del sistema todavía). Al tocar "Entiendo, permitir micrófono", Android te mostrará el diálogo estándar del sistema pidiendo el permiso. Si lo rechazas, puedes intentarlo de nuevo o habilitarlo después en Ajustes del sistema de Android para esta app.

No hay ningún otro permiso que la app pida (no hay cámara, ubicación, contactos, ni internet).

## 6. ¿Qué NO vas a necesitar configurar?

- ❌ Ninguna cuenta de Google Cloud / Firebase.
- ❌ Ninguna API key.
- ❌ Ningún archivo `google-services.json`.
- ❌ Ninguna base de datos remota — todo se guarda en el propio teléfono (DataStore + Room, ambos locales).
- ❌ Ningún servicio de IA — las pantallas que antes decían "Gemini" o "IA en vivo" se corrigieron porque no hay ningún backend de IA conectado (ver `docs/caregiver_upgrade/REGULATORY_CONTENT_REPORT.md`).

Lo único externo que la app usa son enlaces que abres tú manualmente hacia sitios oficiales de California (CDSS, CDPH) — y ni siquiera eso requiere configuración de tu parte.

## 7. Publicar / compartir la app (para más adelante, no ahora)

Cuando quieras compartir un APK de prueba (por ejemplo con alguien que no tiene Android Studio), Android Studio lo genera con `Build > Build App Bundle(s) / APK(s) > Build APK(s)`. Para subirla a Google Play más adelante necesitarás una cuenta de desarrollador de Google Play (de pago, única vez) — eso es un paso completamente aparte y no es necesario para simplemente probar la app tú mismo.
