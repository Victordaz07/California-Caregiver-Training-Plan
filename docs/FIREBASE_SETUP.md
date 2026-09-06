# Configurar Firebase para el módulo "Modo Turno"

Este módulo (pacientes, entrada/salida por geocerca, horas, panel de agencia)
necesita una cuenta de usuarios real y una base de datos compartida — algo
que el resto de la app (el curso de 90 días) no necesita, porque ese vive
solo en tu navegador. Sigue estos pasos una sola vez.

## 1. Crear el proyecto (gratis)

1. Ve a **console.firebase.google.com** e inicia sesión con tu cuenta de Google.
2. Clic en **"Agregar proyecto"**.
3. Ponle un nombre, por ejemplo `caregiver-pro-ca`.
4. Puedes desactivar Google Analytics (no lo necesitamos) y crear el proyecto.

## 2. Activar Authentication (login de usuarios)

1. En el menú izquierdo: **Compilación → Authentication**.
2. Clic en **"Comenzar"**.
3. En la pestaña **"Sign-in method"**, habilita **"Correo electrónico/contraseña"**.

## 3. Activar Firestore (la base de datos)

1. En el menú izquierdo: **Compilación → Firestore Database**.
2. Clic en **"Crear base de datos"**.
3. Elige la ubicación más cercana (por ejemplo `us-west1` para California).
4. Empieza en **modo de producción** (las reglas de seguridad ya están
   escritas en `firestore.rules` en este repo — las publicas en el paso 5).

## 4. Registrar la app web y copiar la configuración

1. En la página principal del proyecto, clic en el ícono **`</>`** ("Web").
2. Ponle un apodo, por ejemplo `caregiver-pro-web`. No necesitas Firebase
   Hosting (ya usamos Vercel).
3. Firebase te mostrará un bloque `firebaseConfig` como este:
   ```js
   const firebaseConfig = {
     apiKey: "AIza...",
     authDomain: "caregiver-pro-ca.firebaseapp.com",
     projectId: "caregiver-pro-ca",
     storageBucket: "caregiver-pro-ca.appspot.com",
     messagingSenderId: "...",
     appId: "..."
   };
   ```
   **Copia ese bloque completo** y pégamelo en el chat (o pégalo directamente
   en `js/firebase-config.js` una vez lo creemos) — no es información secreta,
   Firebase protege los datos con las reglas de `firestore.rules`, no
   ocultando esta configuración.

## 5. Publicar las reglas de seguridad

1. Ve a **Firestore Database → Reglas**.
2. Borra lo que haya y pega el contenido completo de `firestore.rules`
   (está en la raíz de este repositorio).
3. Clic en **"Publicar"**.

## Eso es todo

Con el proyecto creado, Authentication y Firestore activados, y el bloque
`firebaseConfig` en mano, ya puedo conectar la app. No necesitas tocar nada
más de la consola de Firebase — el resto (pantallas, lógica de turno,
geocerca) lo hago yo en el código.
