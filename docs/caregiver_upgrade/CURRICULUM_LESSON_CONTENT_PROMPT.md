# Prompt para generar el contenido de enseñanza real (90 días) con GPT

## Por qué existe este documento

Descubrimos que `content/Curriculum.kt` solo tiene, por cada uno de los 90 días,
un título, un objetivo de una línea y una actividad de práctica/evaluación —
**nunca una lección real que enseñe el tema antes de practicarlo**. Eso viene
del paquete de auditoría original (`CURRICULO_90_DIAS.csv`), que era un
esqueleto de currículo (temas + objetivos + método de evaluación), no las
lecciones en sí. Este prompt le pide a GPT que escriba esa lección faltante
para cada uno de los 90 días, alineada exactamente con el tema/objetivo/
práctica que ya existen en la app — no contenido genérico.

## Cómo usarlo

1. Copia todo el bloque **"PROMPT PARA GPT"** de abajo (incluye la tabla completa
   de los 90 días) y pégalo en ChatGPT.
2. Pide la salida como **un solo bloque de código JSON** (dile explícitamente
   "dame solo el JSON, sin texto antes ni después") — así puedo tomarlo
   directo y fusionarlo en el código sin transcribir nada a mano.
3. Si el modelo corta la respuesta por longitud, pídele que continúe
   ("continúa el JSON desde el día 46" o donde se haya cortado) y luego yo
   uno las partes.
4. Pégame aquí el JSON resultante (completo o por partes) y yo lo reviso,
   lo integro en `content/Curriculum.kt` (agregando un campo `lessonEs` nuevo
   a `CurriculumDay`) y actualizo `RutinaDiariaScreen` para mostrarlo antes
   de la práctica del día.

## Qué NO debe hacer GPT (importante)

- **No inventar citas legales, artículos de ley, estadísticas ni cifras.**
  Esta app ya tuvo que corregir 45 errores de contenido legal en una auditoría
  previa (confundir HCA con IHSS, atribuir mal el CPR a una ley que no lo exige,
  inventar cifras de salario, etc.) — el contenido legal ya vive, correcto y
  con fuente, en otro archivo (`LegalRequirements.kt`). Esta lección es sobre
  **técnica y criterio de cuidado**, no sobre leyes. Si un día toca un tema
  legal (p. ej. "reportante obligatorio", "alcance no médico"), que hable en
  términos prácticos y de sentido común ("consulta a tu supervisor o la ley de
  tu estado"), sin citar artículos ni números de ley específicos.
- **No llamar a esto "certificación", "curso oficial" ni "acreditado por el
  estado".** Es formación interna de práctica. Ya se corrigió este lenguaje en
  toda la app — no lo reintroduzcas.
- **No repetir la misma estructura de frases en los 90 días** — cada lección
  debe sonar escrita para su tema específico, no una plantilla rellenada.

## PROMPT PARA GPT (copiar desde aquí)

Eres un redactor experto en formación de cuidadores no médicos, escribiendo
para una app de entrenamiento interno (no oficial, no acreditada por el
estado) dirigida a cuidadores familiares, proveedores de IHSS, cuidadores
afiliados a HCA, cuidadores privados, HHA y CNA en California. Tu audiencia
son personas sin experiencia previa en cuidado profesional.

Te voy a dar una tabla con 90 días de currículo. Cada fila tiene: número de
día, semana, módulo, título, objetivo de aprendizaje, actividad de práctica,
y si el día es "crítico" (toca un tema de seguridad donde un error real
podría dañar a alguien).

Para **cada uno de los 90 días**, escribe una lección real de 120 a 200
palabras en español, en segundo persona ("tú"), que:

1. Explique **qué es** el tema y **por qué importa** en el cuidado diario real
   (no en abstracto).
2. Explique **cómo hacerlo bien**, con 2-3 puntos de criterio práctico (no una
   lista larga — intégralo en prosa clara y cálida).
3. Incluya **un ejemplo concreto y breve** (una situación típica en un hogar),
   sin datos reales de ninguna persona.
4. Termine con **una frase de transición** que conecte naturalmente con la
   actividad de práctica de ese día (para que la práctica se sienta como
   continuación de la lección, no como un salto).
5. Si el día es crítico (marcado "SI"), incluye una frase clara y directa
   de por qué ese tema requiere especial cuidado o cuándo escalar/pedir ayuda
   — sin alarmismo, con tono de "esto importa de verdad".

Reglas estrictas:
- No inventes ni cites leyes, artículos, números de sección ni estadísticas.
- No llames a esto certificación, curso oficial, ni acreditado por el estado.
- No repitas frases de apertura o cierre entre lecciones — cada una debe
  sonar escrita específicamente para su tema.
- Español claro, sin tecnicismos innecesarios, como si le explicaras a un
  familiar que empieza a cuidar a alguien por primera vez.

Formato de salida exigido: **un único bloque de código JSON**, sin ningún
texto antes o después, con esta forma exacta:

```json
[
  { "day": 1, "lessonEs": "texto de la lección del día 1..." },
  { "day": 2, "lessonEs": "texto de la lección del día 2..." },
  ...
  { "day": 90, "lessonEs": "texto de la lección del día 90..." }
]
```

Exactamente 90 objetos, uno por cada día de la tabla, en orden. Si la
respuesta se corta por longitud, para donde vayas y dime en qué día te
quedaste — yo te pido que continúes desde ahí.

Aquí está la tabla completa (separada por `|`: día|semana|módulo|título|objetivo|práctica|crítico):

```
day|week|modulo|titulo|objetivo|practica|critico
1|1|foundations|Propósito y límites del curso|Distinguir formación interna de una credencial estatal|Clasificar cinco afirmaciones como educación, registro o certificación|no
2|1|foundations|Dignidad y enfoque centrado en la persona|Reconocer preferencias y capacidades antes de ayudar|Reescribir órdenes como opciones respetuosas|no
3|1|foundations|Consentimiento antes de ayudar|Pedir permiso y aceptar una pausa o rechazo seguro|Practicar tres frases de consentimiento en español e inglés|no
4|1|foundations|Privacidad y confidencialidad|Proteger conversaciones, cuerpo, hogar e información|Detectar seis violaciones de privacidad en una escena ficticia|no
5|1|foundations|Alcance no médico|Separar ayuda permitida de diagnóstico, prescripción o tareas no autorizadas|Ordenar acciones en hacer, verificar o escalar|SI
6|1|foundations|Límites profesionales|Identificar conflictos, regalos, dinero y relaciones duales|Responder a un ofrecimiento de préstamo sin avergonzar al cliente|no
7|1|foundations|Usar el plan de cuidado|Consultar instrucciones individuales y registrar dudas|Localizar tarea, preferencia, riesgo y contacto de escalamiento en un plan ficticio|SI
8|2|pathways|Mapa de rutas de California|Diferenciar cuidador familiar, IHSS, HCA/HCO, privado, HHA y CNA|Elegir la ruta correcta para seis perfiles ficticios|no
9|2|pathways|Ruta del cuidador familiar|Explicar por qué no existe una credencial universal para todo cuidador familiar|Crear un plan de límites y contactos para apoyo informal|no
10|2|pathways|Inscripción IHSS|Recordar SOC 426, orientación, SOC 846 y antecedentes DOJ|Ordenar los cuatro pasos sin introducir datos reales|no
11|2|pathways|HCA afiliado a una HCO|Identificar registro, cinco horas iniciales, formación anual y TB|Construir una lista por momento: antes del cliente, anual y seguimiento|no
12|2|pathways|Cuidador privado o directo|Distinguir acuerdo directo de afiliación HCO e IHSS|Marcar qué asuntos requieren contrato, fuente oficial o asesoría|no
13|2|pathways|HHA y CNA no son lo mismo|Comparar programas aprobados y límites de esta app|Completar tabla 120 h HHA, 40 h elegible y 160 h CNA|no
14|2|pathways|Comprobar una fuente oficial|Evaluar agencia, jurisdicción, fecha y aplicabilidad|Auditar una tarjeta de requisito desactualizada|no
15|3|infection|Cómo se transmite una infección|Reconocer cadena de transmisión sin diagnosticar|Identificar puntos para cortar transmisión en un hogar ficticio|no
16|3|infection|Momentos de higiene de manos|Elegir cuándo limpiar las manos según tarea y riesgo|Secuenciar preparación, cuidado y limpieza|SI
17|3|infection|Uso responsable de guantes y PPE|Entender que los guantes no sustituyen higiene ni política|Detectar contaminación cruzada en cuatro decisiones|SI
18|3|infection|Limpieza de superficies|Seguir etiqueta, ventilación y plan sin mezclar químicos|Elegir una respuesta segura ante productos incompatibles|SI
19|3|infection|Ropa, residuos y objetos personales|Reducir exposición y preservar dignidad|Planear manipulación sin sacudir ni mezclar objetos personales|no
20|3|infection|Etiqueta respiratoria y enfermedad|Aplicar medidas del plan y escalar síntomas observados|Practicar un reporte objetivo al supervisor|SI
21|3|infection|Caso integrado de control de infección|Combinar higiene, PPE, limpieza y comunicación|Resolver un escenario ramificado y reintentar fallos|SI
22|4|safety|Recorrido de seguridad del hogar|Observar riesgos sin modificar el hogar sin permiso|Documentar iluminación, cables, alfombras y salidas como hechos|no
23|4|safety|Plan de emergencia y contactos|Localizar dirección, contactos, salida y equipo autorizado|Completar un plan ficticio sin datos personales|SI
24|4|safety|Cuándo llamar al 911|Priorizar peligro inmediato sobre temporizador o documentación|Elegir acción inicial en seis situaciones|SI
25|4|safety|Prevención de caídas|Reducir riesgos respetando movilidad y autonomía|Proponer cambios y pedir consentimiento antes de actuar|SI
26|4|safety|Atragantamiento: reconocer y activar ayuda|Reconocer una emergencia y usar formación presencial aplicable|Distinguir tos efectiva, obstrucción grave y necesidad de 911 sin simular certificación|SI
27|4|safety|Fuego, humo, calor y electricidad|Seguir salida segura y plan de emergencia|Seleccionar ruta de evacuación en escenario ficticio|SI
28|4|safety|Evaluación de seguridad|Integrar observación, acción inmediata y escalamiento|Completar evaluación sin usar ayuda durante el primer intento|SI
29|5|adl|Ayudar sin quitar independencia|Usar la menor ayuda segura necesaria|Convertir cuatro tareas en indicación, preparación o asistencia|no
30|5|adl|Baño, elección y rechazo|Responder a rechazo sin coacción y revisar riesgos/plan|Role-play de ofrecer opciones y escalar una preocupación|SI
31|5|adl|Vestido y preferencias|Apoyar elección, privacidad y ritmo|Preparar una secuencia de ayuda centrada en la persona|no
32|5|adl|Toileting y dignidad|Proteger privacidad, seguridad y control de infección|Detectar lenguaje y acciones que deben cambiar|SI
33|5|adl|Cuidado oral y observación de piel|Observar y reportar cambios sin diagnosticar ni tratar|Redactar una nota de hechos visibles y malestar reportado|SI
34|5|adl|Rutina, descanso y sueño|Apoyar rutinas documentadas y notar cambios|Planear una conversación sobre preferencias de descanso|no
35|5|adl|Caso integrado de actividades diarias|Combinar consentimiento, privacidad, seguridad y alcance|Resolver un turno matutino ficticio con varias decisiones|SI
36|6|mobility|Movilidad y elección|Preguntar cómo prefiere moverse y seguir el plan|Practicar indicaciones breves antes de ofrecer contacto|no
37|6|mobility|Entorno, calzado y ayudas|Revisar condiciones y equipo autorizado|Identificar cinco riesgos sin mover pertenencias sin permiso|SI
38|6|mobility|Mecánica corporal del cuidador|Proteger al usuario y al cuidador sin improvisar|Reconocer cuándo detenerse y pedir segunda persona o equipo|SI
39|6|mobility|Límites de las transferencias|No ejecutar una transferencia no entrenada o fuera del plan|Elegir entre asistir, pausar o escalar|SI
40|6|mobility|Grúas y equipos de movilidad|Entender que la app no acredita uso de equipos|Completar checklist previo y marcar verificación presencial requerida|SI
41|6|mobility|Respuesta después de una caída|Proteger seguridad, no improvisar levantamiento y activar ayuda según condición/plan|Ordenar acciones en un escenario de caída con posible lesión|SI
42|6|mobility|Evaluación de movilidad segura|Integrar consentimiento, entorno, límites y escalamiento|Resolver escenario y solicitar firma de observación separada|SI
43|7|nutrition|Plan, preferencias y dieta indicada|Seguir preferencias y restricciones documentadas sin prescribir|Distinguir elección personal, orden del plan y cambio que requiere consulta|SI
44|7|nutrition|Observar hidratación|Ofrecer líquidos permitidos y reportar cambios observables|Registrar ingesta ficticia y una preocupación sin diagnosticar|no
45|7|nutrition|Preparación de comidas segura|Prevenir contaminación y respetar restricciones|Secuenciar lavado, separación, cocción y almacenamiento|SI
46|7|nutrition|Compras y presupuesto con límites|Manejar listas, recibos y dinero con transparencia|Resolver discrepancia de recibo sin mezclar fondos|no
47|7|nutrition|Dificultad al tragar y señales de alarma|Observar, detener la ayuda insegura y escalar según plan; 911 ante emergencia|Responder a tos/cambio repentino sin cambiar textura por cuenta propia|SI
48|7|nutrition|Comidas con dignidad y autonomía|Dar tiempo, opciones y la ayuda mínima necesaria|Role-play bilingüe durante una comida|no
49|7|nutrition|Caso integrado de nutrición|Combinar plan, seguridad alimentaria, observación y límites|Completar caso ramificado de una comida y documentar|SI
50|8|dementia|Lenguaje centrado en la persona|Separar a la persona de un diagnóstico o conducta|Reformular cinco etiquetas estigmatizantes|no
51|8|dementia|Comunicación calmada y simple|Usar una idea por vez, tiempo y validación|Grabar tres respuestas cortas en español e inglés|no
52|8|dementia|Rutinas y señales del entorno|Apoyar previsibilidad sin controlar innecesariamente|Diseñar una rutina ficticia basada en preferencias|no
53|8|dementia|La conducta comunica una necesidad|Observar desencadenantes y necesidades sin diagnosticar|Completar antecedente, hecho observable y respuesta segura|SI
54|8|dementia|Salida no planificada y seguridad|Aplicar plan, supervisión autorizada y escalamiento inmediato|Resolver un escenario de puerta abierta sin restricción improvisada|SI
55|8|dementia|Cambio repentino no es rutina|Tratar un cambio agudo como preocupación que requiere escalamiento|Comunicar inicio, hechos y diferencia respecto a la línea base|SI
56|8|dementia|Caso integrado de apoyo cognitivo|Usar validación, opciones, seguridad y documentación|Completar escenario y autoevaluar tono y claridad|SI
57|9|communication|Escucha activa|Reflejar necesidad y comprobar comprensión|Responder sin interrumpir ni asumir|no
58|9|communication|Lenguaje claro y una instrucción a la vez|Reducir jerga y carga cognitiva|Simplificar seis frases de cuidado|no
59|9|communication|Teach-back sin examen|Comprobar cómo explicaste, no culpar al oyente|Practicar una invitación respetuosa a repetir el plan|no
60|9|communication|Apoyo bilingüe y límites|Reconocer cuándo el idioma propio no basta y pedir apoyo autorizado|Elegir respuesta ante información compleja o sensible|SI
61|9|communication|Frases esenciales de turno en inglés|Comunicar observación, acción y pendiente con lenguaje sencillo|Shadowing a velocidad lenta y normal|no
62|9|communication|Pronunciación y aclaración segura|Pedir repetición, deletreo o confirmación sin fingir entender|Grabar y comparar cinco frases funcionales|no
63|9|communication|Role-play bilingüe completo|Mantener dignidad, alcance y exactitud entre idiomas|Completar conversación ramificada y reintento|SI
64|10|documentation|Hechos, no diagnósticos|Documentar lo observado, oído y realizado|Corregir notas con juicios o diagnósticos|SI
65|10|documentation|Hora, secuencia y exactitud|Registrar cronología sin alterar ni rellenar memoria|Ordenar eventos y marcar dato desconocido|no
66|10|documentation|DAR y SOAP como técnicas de práctica|Usar estructuras solo cuando el empleador las acepte|Convertir hechos en dos formatos y comparar límites|no
67|10|documentation|Medicamentos: registrar sin inventar|Seguir plan/formato autorizado y no corregir un registro de memoria|Resolver una discrepancia escalando antes de firmar|SI
68|10|documentation|Entrega de turno estructurada|Comunicar situación, hechos, acciones y pendiente|Realizar una entrega de 60 segundos|SI
69|10|documentation|Privacidad al comunicar|Usar canal, audiencia y contenido mínimos|Elegir dónde y con quién compartir una actualización|SI
70|10|documentation|Simulación completa de turno|Integrar cuidado, notas y entrega sin omisiones internas|Completar un turno ficticio; cero omisiones es criterio de la simulación|SI
71|11|abuse|Tipos de abuso, neglect y explotación|Reconocer categorías sin investigar ni confrontar|Clasificar señales ficticias y separar hecho de inferencia|SI
72|11|abuse|Señales y sospecha razonable|Entender que no se necesita prueba definitiva para activar el deber aplicable|Decidir qué observación requiere reporte o consulta inmediata|SI
73|11|abuse|Quién puede ser reportante obligatorio|Relacionar responsabilidad de cuidado con la definición legal y el entorno|Evaluar perfiles ficticios sin dar asesoría legal personalizada|SI
74|11|abuse|Peligro inmediato, APS y policía|Elegir 911 ante peligro y canal apropiado para reporte en el hogar|Resolver cuatro ramas con ubicación ficticia|SI
75|11|abuse|Tiempo y seguimiento del reporte|Recordar inmediato/tan pronto como sea posible y seguimiento en dos días laborables tras teléfono|Construir línea de tiempo de un reporte ficticio|SI
76|11|abuse|Documentar sin investigar|Registrar palabras exactas y hechos sin prometer secreto|Reescribir una nota que confronta al presunto agresor|SI
77|11|abuse|Caso integrado de reporte|Proteger, reportar y documentar según entorno|Completar caso crítico y localizar fuente oficial|SI
78|12|medication_support|Medicamentos autoadministrados y alcance|Distinguir asistencia permitida de prescribir, decidir o cambiar dosis|Clasificar ocho acciones en ayudar, verificar o escalar|SI
79|12|medication_support|Seguir el plan y la etiqueta|Comparar instrucciones autorizadas sin interpretar cambios|Responder a una etiqueta y plan que no coinciden|SI
80|12|medication_support|Dosis omitida o rechazo|No forzar, duplicar ni decidir; observar y escalar según plan|Role-play de respuesta y notificación|SI
81|12|medication_support|Cambios observables y efectos reportados|Comunicar hechos y palabras de la persona sin diagnosticar|Preparar llamada estructurada con hora, hecho y acción|SI
82|12|medication_support|Apoyo a condiciones crónicas|Seguir rutinas autorizadas y reconocer límites|Elegir apoyo no médico y cuándo llamar ayuda|SI
83|12|medication_support|Citas y comunicación con el equipo|Preparar observaciones/preguntas sin hablar por la persona innecesariamente|Crear lista breve centrada en preferencias|no
84|12|medication_support|Caso integrado de apoyo con medicación|Combinar plan, rechazo, observación y escalamiento|Resolver caso crítico sin tomar decisiones clínicas|SI
85|13|career|Confirmar mi ruta|Elegir una ruta y reconocer cuándo debe cambiar|Actualizar perfil y explicar la elección con dos fuentes|no
86|13|career|Checklist oficial de próximos pasos|Separar tareas dentro de la app de acciones con agencia, condado o programa|Crear lista fechada con enlaces oficiales|no
87|13|career|Guardian e IHSS: procesos distintos|Explicar diferencias sin compartir datos sensibles|Comparar solicitud HCA con inscripción IHSS|no
88|13|career|Comparar HHA y CNA|Elegir preguntas para un programa aprobado y reconocer horas supervisadas|Auditar una publicidad ficticia de escuela|no
89|13|career|Portafolio, entrevista y autocuidado|Presentar aprendizaje honestamente y planificar límites saludables|Redactar logro sin afirmar credencial y una pregunta de entrevista|no
90|13|career|Evaluación final interna y plan de continuidad|Integrar seguridad, alcance, comunicación, reporte y ruta profesional|Completar evaluación, revisar errores y crear próximos pasos oficiales|SI
```
