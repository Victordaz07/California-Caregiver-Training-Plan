# Guía breve de identidad visual

## Concepto

Las dos protagonistas representan una alianza: la mujer mayor conserva voz, preferencias y capacidad; la caregiver aporta preparación, observación y apoyo. Deben aparecer como adultas colaborando, nunca como una figura dominante y otra infantilizada.

## Uso de los logos

- Usa `mascots-avatar-circle.png` para perfiles, logros, chips e insignias.
- Usa `mascots-avatar-square.png` para el icono del curso, portada o tarjetas grandes.
- Usa `mascots-duo-transparent.png` cuando necesites colocar a las protagonistas sobre un fondo de la interfaz.
- Conserva un margen libre equivalente al 10 % de la altura de la imagen y evita colocar texto sobre caras, manos o la carpeta.
- No estires, reflejes, recolorees ni separes los personajes como si una fuera subordinada de la otra.

## Banners de lección

Hay un banner de 1600 × 420 px para cada día. Cada archivo muestra número, semana, módulo y título exacto. La ruta se consulta en `banner_manifest.json`; en la app conviene seleccionar por `day`, no construir el nombre del archivo manualmente.

En pantallas estrechas, recorta primero el extremo derecho solo si se conserva visible el título completo. Para accesibilidad, utiliza siempre el campo `altEs` del manifiesto. No incrustes botones dentro de la imagen: coloca los controles de la app como componentes reales debajo o encima de un área segura.

## Escenas dentro de la app

Las seis escenas horizontales incluyen áreas pensadas para interfaz. `safeTextSide` indica dónde puede colocarse texto sin tapar la acción. Cuando el valor sea `none`, usa la escena como ilustración completa y coloca el contenido fuera de ella.

Usos sugeridos:

- bienvenida: portada y comienzo de jornada;
- práctica: antes de una simulación;
- progreso: cierre semanal o celebración;
- seguridad: centro de riesgos y emergencias;
- fuentes: recursos oficiales y referencias;
- carrera: portafolio y continuidad después del día 90.

## Seguridad editorial

Las ilustraciones muestran principios y secuencias pedagógicas; no sustituyen un plan individual, entrenamiento presencial, política del empleador ni autorización para usar equipos. Nunca conviertas una lámina en permiso automático para realizar una transferencia, primeros auxilios, ayuda con medicamentos u otra tarea regulada. Conserva visibles las advertencias y puntos de detención del manifiesto visual.
