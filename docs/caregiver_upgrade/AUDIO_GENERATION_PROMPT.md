# Prompt para generar el audio real de las 13 lecciones (con Gemini)

Este documento resuelve A-024/A-025 (ver `DECISIONS.md` ADR-006): el paquete de auditoría
nunca incluyó archivos de audio, solo los 13 outlines de `content/AudioEpisodes.kt`. Este
prompt convierte cada outline en un guion narrado completo, fiel al contenido original
(frase clave, pronunciación, contexto), sin agregar ninguna afirmación legal o regulatoria
nueva.

## Cómo usarlo

1. Copia el bloque **"PROMPT PARA GEMINI"** completo (todo, de un tirón) en Gemini
   (la app, o AI Studio si quieres más control de voz/parámetros).
2. Si tu herramienta de Gemini solo genera un audio a la vez, pídele que te dé
   primero **un episodio** (dile "genera solo el Episodio 1") y repite para
   cada uno — el guion completo de los 13 ya está en el prompt, no necesitas
   reescribir nada entre pedidos.
3. Pide explícitamente el archivo de salida en `.mp3`.
4. **Nombra cada archivo exactamente así** (para que yo pueda conectarlo directo al código sin renombrar nada):

   | Semana | Nombre de archivo |
   |---|---|
   | 1 | `audio_w01.mp3` |
   | 2 | `audio_w02.mp3` |
   | 3 | `audio_w03.mp3` |
   | 4 | `audio_w04.mp3` |
   | 5 | `audio_w05.mp3` |
   | 6 | `audio_w06.mp3` |
   | 7 | `audio_w07.mp3` |
   | 8 | `audio_w08.mp3` |
   | 9 | `audio_w09.mp3` |
   | 10 | `audio_w10.mp3` |
   | 11 | `audio_w11.mp3` |
   | 12 | `audio_w12.mp3` |
   | 13 | `audio_w13.mp3` |

5. Cuando tengas los 13 archivos, súbelos a este chat y yo agrego la reproducción real
   en `AudioLeccionesScreen`/`BibliotecaAudiosScreen` (hoy solo muestran el guion + grabación
   de tu propia voz, porque no existía audio narrado).

**Nota honesta**: el outline original solo daba una frase clave + pronunciación + un
prompt de práctica, no un guion de 10-15 minutos. Convertí cada uno en una lección corta
y real (~60-90 segundos hablados) en vez de inflarlo con relleno para llegar a esa
duración — es mejor un guion real y corto que uno largo con contenido inventado.

---

## PROMPT PARA GEMINI (copiar desde aquí)

Eres el narrador oficial de un curso interno de capacitación para cuidadores no médicos en
California (app "Caregiver Pro CA"). Vas a generar el audio de 13 lecciones bilingües
cortas (inglés funcional para el trabajo + español de apoyo). Tu voz debe sonar como una
persona real, cálida, clara y profesional — como un instructor paciente, no como un
locutor comercial ni un robot.

### Reglas de estilo (aplican a las 13 lecciones)

- Ritmo pausado, apto para alguien que está aprendiendo inglés como segundo idioma.
- Estructura fija por episodio:
  1. Saludo breve + número de semana + título (en español).
  2. La frase clave en inglés, dicha a **ritmo normal**, una vez.
  3. La misma frase, dicha **lento y separada por sílabas**, dos veces.
  4. La guía de pronunciación en "español fonético" leída en voz alta, despacio.
  5. Una frase de transición en español explicando el contexto de uso (basada en el
     "prompt de práctica" de cada episodio).
  6. Una invitación breve a practicar: "Ahora te toca a ti. Grábate diciendo esta frase
     en voz alta." — y una pausa de 2 segundos de silencio al final para que la persona
     hable después de escuchar (no le hables tú durante esa pausa).
- Nunca agregues datos legales, cifras, nombres de leyes o afirmaciones de certificación
  que no estén en el guion de abajo — este contenido es práctica interna, no un documento
  legal ni un curso acreditado por el estado.
- No uses música de fondo ni efectos — solo voz clara.
- Idioma de la narración de contexto: español. Idioma de la frase práctica: inglés.

### Guion por episodio

**Episodio 1 — Semana 1 — "Dignidad, consentimiento y alcance" (~70 seg)**
- Frase clave: "How would you like me to help?"
- Pronunciación: jau wud yu laik mi tu jelp
- Contexto: Explica en 60 segundos cómo pedir permiso y mantenerte dentro de tu función
  como cuidador — nunca decidir por la persona, siempre preguntar primero.

**Episodio 2 — Semana 2 — "Seis rutas, seis procesos" (~80 seg)**
- Frase clave: "Which pathway applies to me?"
- Pronunciación: uich PATH-uei a-PLAIS tu mi
- Contexto: Compara brevemente IHSS con HCA afiliado a una agencia, sin usar la palabra
  "certificación" (son procesos distintos: inscripción de proveedor vs. registro).

**Episodio 3 — Semana 3 — "Cortar la contaminación cruzada" (~70 seg)**
- Frase clave: "I need to clean my hands before the next task."
- Pronunciación: ai nid tu klin mai jands bi-FOR de nekst task
- Contexto: Describe una secuencia segura entre el cuidado personal y la preparación
  de comida — lavarse las manos entre una tarea y otra.

**Episodio 4 — Semana 4 — "La emergencia primero" (~60 seg)**
- Frase clave: "Call 911 now."
- Pronunciación: kol nain uan uan nau
- Contexto: Explica que ante peligro inmediato para la vida, se llama al 911 primero —
  cualquier temporizador de práctica o sesión se detiene de inmediato.

**Episodio 5 — Semana 5 — "Ayudar sin tomar control" (~70 seg)**
- Frase clave: "Would you like a full bath or a partial wash?"
- Pronunciación: wud yu laik a ful bath or a PAR-shal uash
- Contexto: Ofrece dos opciones respetuosas ante una tarea diaria, dejando que la
  persona decida.

**Episodio 6 — Semana 6 — "Movilidad: saber cuándo detenerse" (~75 seg)**
- Frase clave: "I am not trained to use this lift safely."
- Pronunciación: ai am not treind tu ius dis lift SEIF-li
- Contexto: Practica cómo pedir apoyo cuando una tarea de movilidad supera tu
  entrenamiento, sin culpar a la persona que recibe el cuidado.

**Episodio 7 — Semana 7 — "Comidas, hidratación y cambios" (~70 seg)**
- Frase clave: "I noticed a new change while you were eating."
- Pronunciación: ai NOU-tist a niu cheinch uail yu uer I-ting
- Contexto: Entrega un reporte objetivo (solo lo que se observó, sin diagnosticar) de un
  cambio ocurrido durante la comida.

**Episodio 8 — Semana 8 — "Apoyo cognitivo centrado en la persona" (~80 seg)**
- Frase clave: "You seem worried. I am here with you."
- Pronunciación: yu sim UO-rid, ai am jir uiz yu
- Contexto: Valida la emoción de la persona sin confirmar un hecho falso — útil en
  situaciones de confusión o demencia.

**Episodio 9 — Semana 9 — "Aclarar en inglés con seguridad" (~85 seg)**
- Frase clave: "Please say that again slowly, and let me repeat it back."
- Pronunciación: plis sei dat a-GUEN SLOU-li, and let mi ri-PIT it bak
- Contexto: Practica pedir que repitan algo y usar la técnica de "teach-back" (repetir
  para confirmar que entendiste bien).

**Episodio 10 — Semana 10 — "Una entrega de turno objetiva" (~75 seg)**
- Frase clave: "At 5:45, I observed... I notified... The next step is..."
- Pronunciación: at faiv FOR-ti faiv, ai ob-SERVD; ai NOU-ti-faid; de nekst step is
- Contexto: Modela una entrega de turno de 60 segundos con hora, hecho observado, acción
  tomada y lo que queda pendiente.

**Episodio 11 — Semana 11 — "Reportar sin investigar" (~80 seg)**
- Frase clave: "I need to report this concern right away."
- Pronunciación: ai nid tu ri-PORT dis kon-SERN rait a-UEI
- Contexto: Explica por qué un cuidador reporta una preocupación de inmediato, sin
  esperar una confesión ni investigar por su cuenta.

**Episodio 12 — Semana 12 — "Ayuda con medicación dentro del alcance" (~80 seg)**
- Frase clave: "I cannot change the dose. I will follow the plan and call the authorized
  person."
- Pronunciación: ai KA-not cheinch de dous; ai uil FA-lou de plan and kol di O-tho-raizd
  PER-son
- Contexto: Responde con seguridad ante una dosis que parece dudosa, sin dar ningún
  consejo clínico propio.

**Episodio 13 — Semana 13 — "Próximos pasos sin promesas falsas" (~75 seg)**
- Frase clave: "This is internal training, not a state credential."
- Pronunciación: dis is in-TER-nal TREI-ning, not a steit kri-DEN-shal
- Contexto: Presenta el aprendizaje del curso en una entrevista de trabajo con lenguaje
  honesto, dejando claro que es formación interna, no una credencial estatal.

### Formato de entrega pedido

Genera 13 archivos de audio independientes en `.mp3`, uno por episodio, nombrados
`audio_w01.mp3` hasta `audio_w13.mp3` en ese orden. Si solo puedes generar uno a la vez,
dime cuál episodio quieres primero y lo genero con exactamente el mismo guion de arriba.

---

## Después de generar el audio

Cuando tengas los 13 `.mp3`, súbelos aquí. Los pasos técnicos que seguirán (para que
sepas qué esperar, no hace falta que hagas nada de esto tú):

1. Colocarlos en `android/app/src/main/res/raw/` (Android exige nombres en minúsculas
   sin guiones, por eso `audio_w01.mp3` etc. ya vienen listos).
2. Agregar reproducción real con `MediaPlayer` en `AudioLeccionesScreen` y
   `BibliotecaAudiosScreen` (hoy solo muestran el guion de texto).
3. Actualizar `FINAL_AUDIT.md`/`REGULATORY_CONTENT_REPORT.md`/`DECISIONS.md` para cerrar
   A-024/A-025 como resueltos de verdad, no solo "sustituido por grabación propia".
