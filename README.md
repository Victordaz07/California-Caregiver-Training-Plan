# Caregiver Pro CA — Plan de Entrenamiento de 90 Días

Prototipo de una app de entrenamiento y certificación para cuidadores no médicos, asistentes de enfermería (CNA) y trabajadores IHSS en California. El diseño fue generado con [Stitch](https://stitch.withgoogle.com) y se conserva aquí como páginas HTML estáticas y autocontenidas (Tailwind vía CDN, fuentes Manrope / JetBrains Mono, iconos Material Symbols).

## Cómo verlo

Abre `index.html` en un navegador, o sirve la carpeta con cualquier servidor estático:

```bash
python3 -m http.server 8000
```

Luego visita `http://localhost:8000`.

## Estructura

```
index.html              Portada con enlaces a todas las pantallas
screens/                 Las 10 pantallas del prototipo (HTML independientes)
assets/logo.svg          Logo de la marca
docs/design/DESIGN.md    Sistema de diseño (colores, tipografía, componentes)
docs/design/previews/    Capturas de referencia de cada pantalla
```

## Pantallas

| Pantalla | Descripción |
|---|---|
| `screens/rutina-diaria-75-minutos.html` | Panel principal: sesión guiada diaria y progreso de 90 días |
| `screens/escenarios-y-flashcards.html` | Práctica con escenarios y flashcards |
| `screens/simulacion-de-turno-y-handoff.html` | Simulación de entrega de turno (handoff) |
| `screens/role-play-audio-bilingue-semana-10.html` | Role play de audio bilingüe (semana 10) |
| `screens/audio-lecciones-gemini-manos-libres.html` | Lecciones de audio manos libres con repaso formal |
| `screens/biblioteca-audios-offline-13-semanas.html` | Biblioteca de audios descargables (13 semanas) |
| `screens/requisitos-y-certificacion-ca.html` | Requisitos y certificación en California |
| `screens/recursos-oficiales-certificaciones-semana.html` | Recursos oficiales y certificaciones por semana |
| `screens/analizador-vocacional-metas-carrera.html` | Analizador vocacional y metas de carrera |
| `screens/plan-90-dias-evaluaciones.html` | Plan completo de 90 días y evaluaciones |

## Sistema de diseño

Ver `docs/design/DESIGN.md` para la paleta de colores, tipografía, espaciado, elevación, formas y especificaciones de componentes ("Modern Clinical-Humanist").

## Próximos pasos sugeridos

Estas pantallas son un prototipo visual: los contadores, temporizadores y barras de progreso funcionan en el navegador (JS embebido por pantalla), pero no hay backend, autenticación ni persistencia de datos entre pantallas. Los siguientes pasos típicos serían:
- Elegir un framework (p. ej. Next.js) y convertir cada pantalla en un componente.
- Añadir estado/persistencia compartida (progreso del plan de 90 días, resultados de evaluaciones).
- Conectar contenido real (lecciones, audios, recursos oficiales de CDSS/IHSS).
