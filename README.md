# Caregiver Pro CA — App Web (PWA)

App de entrenamiento personal de 90 días para cuidadores no médicos en California — **de uso personal**, no un producto de certificación ni afiliado a ninguna agencia estatal. Nació como un prototipo HTML de [Stitch](https://stitch.withgoogle.com); ahora es una **PWA real e instalable**, funcional sin backend: todo el contenido y el progreso viven en el propio navegador (`localStorage`), con los mismos datos legales/curriculares ya corregidos y verificados que se usaron en la versión nativa de Android (`android/`).

## Cómo verla

Sirve la carpeta con cualquier servidor estático (no requiere build ni Node):

```bash
python3 -m http.server 8000
```

Luego visita `http://localhost:8000`. Para instalarla como app (PWA), ábrela en Chrome/Edge y usa "Instalar app" en la barra de direcciones — funciona sin conexión después de la primera carga.

## Desplegar en Vercel

Es un sitio 100% estático — no necesita configuración especial. En [vercel.com](https://vercel.com), "Import Project" desde este repo y despliega tal cual (no hay comando de build ni carpeta de salida distinta a la raíz).

## Estructura

```
index.html          App shell (una sola página real, no una galería)
manifest.json        Manifiesto de la PWA (íconos, nombre, modo standalone)
sw.js                 Service worker: cachea todo para uso sin conexión
css/app.css           Estilos base (además de Tailwind vía CDN)
js/
  data.js             Carga el contenido real (data/*.json)
  state.js             Progreso/estado en localStorage (equivalente a DataStore/Room de Android)
  scheduler.js          Repetición espaciada (mismo algoritmo que domain/ReviewScheduler.kt)
  audio.js              Reproducción de narración real + grabación de tu voz (Web Audio/MediaRecorder)
  components.js          Helpers de UI compartidos
  router.js, app.js       Router por hash + shell de navegación (5 pestañas)
  screens/*.js            Una pantalla por archivo (Hoy, Plan, Práctica, Audio, Carrera y sus detalles)
data/*.json           Contenido real: 6 rutas, currículo de 90 días (con lección real por día),
                        39 tarjetas, 13 escenarios, 34 rúbricas, 30 términos, requisitos y recursos
                        oficiales — verbatim del mismo contenido auditado que usa la app Android.
assets/audio/*.mp3    Los 13 episodios narrados reales (generados con Cloud Text-to-Speech)
assets/illustrations/  Diagramas paso a paso (estilo cómic) para temas de movimiento físico
docs/design/DESIGN.md  Sistema de diseño (colores, tipografía) — la fuente de los tokens de Tailwind
```

## Qué es real (y qué no)

- ✅ Currículo completo de 90 días, con lección de enseñanza real por día (no solo práctica).
- ✅ Repetición espaciada real sobre las 39 tarjetas, persistida en tu navegador.
- ✅ Escenarios ramificados, requisitos legales y recursos oficiales filtrados por tu ruta.
- ✅ Audio narrado real (13 episodios) + grabación de tu propia voz para practicar.
- ✅ Diagramas ilustrados paso a paso para temas de movimiento físico (mecánica corporal, respuesta tras una caída) — principios generales de referencia, no un sustituto de entrenamiento presencial supervisado.
- ❌ Sin pruebas automatizadas todavía.
- ❌ Los diagramas ilustrados solo cubren 2 días de muestra por ahora; el resto de temas de movimiento físico quedan pendientes de ilustrar.

## App nativa de Android

En `android/` vive una versión nativa (Kotlin + Jetpack Compose) construida a partir del mismo contenido y diseño. Ver `android/README.md` y `docs/caregiver_upgrade/` para su estado y los 5 informes de auditoría de contenido legal.
