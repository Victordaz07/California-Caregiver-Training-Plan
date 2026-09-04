package com.caregiverproca.app.content

/**
 * Verbatim from 02_PRODUCT_CONTENT/SCENARIOS_SEED.json — 13 fictional branching
 * scenarios, one per week. Every scenario is explicitly fictional
 * ("fictionalByDefault": true in the source) and every choice maps to an
 * [ScenarioOutcome] plus feedback, so a learner answers before seeing the
 * outcome (per Fase 4 "el alumno responde antes de ver solución").
 */
enum class ScenarioOutcome { Safe, CriticalFail, NeedsCorrection }

data class ScenarioChoice(
    val id: String,
    val textEs: String,
    val outcome: ScenarioOutcome,
    val feedbackEs: String,
)

data class Scenario(
    val id: String,
    val week: Int,
    val titleEs: String,
    val stemEs: String,
    val choices: List<ScenarioChoice>,
    val rubricIds: List<String>,
    val sourceTags: List<String>,
)

val Scenarios: List<Scenario> = listOf(
    Scenario(
        id = "w01_bathing_refusal",
        week = 1,
        titleEs = "El baño que debe esperar",
        stemEs = "Escenario ficticio: Rosa dice con claridad que hoy no quiere bañarse. El plan permite ofrecer baño o aseo parcial; no hay peligro inmediato.",
        choices = listOf(
            ScenarioChoice("a", "Insistir porque la higiene está programada", ScenarioOutcome.CriticalFail, "Una tarea programada no elimina consentimiento ni justifica coacción."),
            ScenarioChoice("b", "Reconocer su decisión, ofrecer opciones y documentar/escalar la preocupación según el plan", ScenarioOutcome.Safe, "Protege dignidad, ofrece una alternativa y mantiene al equipo informado."),
            ScenarioChoice("c", "Citar WIC §15610.05 para obligarla", ScenarioOutcome.CriticalFail, "Esa sección define abandono; no autoriza coacción ni es la fuente correcta sobre autonomía."),
        ),
        rubricIds = listOf("consent", "dignity", "care_plan", "objective_documentation"),
        sourceTags = listOf("care_plan", "WIC_15610_05_correction"),
    ),
    Scenario(
        id = "w02_pathway_first",
        week = 2,
        titleEs = "¿Qué trámite corresponde?",
        stemEs = "Escenario ficticio: Devon dice que cuidará a su tía, quizá será pagado por IHSS y también considera una agencia. Pregunta si debe tomar el examen Guardian.",
        choices = listOf(
            ScenarioChoice("a", "Decir que todo cuidador debe aprobar el examen Guardian", ScenarioOutcome.CriticalFail, "Guardian no se describe como examen y las rutas tienen procesos distintos."),
            ScenarioChoice("b", "Aclarar primero si seguirá IHSS, HCO o contratación directa y mostrar el proceso específico", ScenarioOutcome.Safe, "La ruta y la relación laboral determinan qué información aplica."),
            ScenarioChoice("c", "Emitirle una certificación combinada IHSS/HCA", ScenarioOutcome.CriticalFail, "La app no concede credenciales y esas rutas no se combinan."),
        ),
        rubricIds = listOf("pathway_identification", "accurate_language", "source_use"),
        sourceTags = listOf("CDSS_HCA", "CDSS_IHSS"),
    ),
    Scenario(
        id = "w03_glove_cross_contamination",
        week = 3,
        titleEs = "Los guantes no lo hacen todo",
        stemEs = "Escenario ficticio: Después de una tarea de cuidado personal, el alumno con los mismos guantes toca el teléfono y empieza a preparar comida.",
        choices = listOf(
            ScenarioChoice("a", "Continuar porque los guantes siguen puestos", ScenarioOutcome.CriticalFail, "Los guantes contaminados pueden transferir microorganismos."),
            ScenarioChoice("b", "Detenerse, retirar los guantes de forma segura, realizar higiene de manos y limpiar según política antes de seguir", ScenarioOutcome.Safe, "Interrumpe la contaminación cruzada y respeta la secuencia de tareas."),
            ScenarioChoice("c", "Rociar las manos enguantadas con cualquier químico", ScenarioOutcome.NeedsCorrection, "No improvises con productos; sigue etiqueta, formación y política."),
        ),
        rubricIds = listOf("hand_hygiene", "ppe", "task_sequence"),
        sourceTags = listOf("infection_control", "employer_policy"),
    ),
    Scenario(
        id = "w04_immediate_danger",
        week = 4,
        titleEs = "El cronómetro no manda",
        stemEs = "Escenario ficticio: Durante una práctica, la persona cae, no responde y parece no respirar normalmente. El temporizador del bloque marca tres minutos restantes.",
        choices = listOf(
            ScenarioChoice("a", "Terminar el bloque para conservar la racha", ScenarioOutcome.CriticalFail, "Nunca se retrasa una respuesta de emergencia por una lección o racha."),
            ScenarioChoice("b", "Activar el plan de emergencia y llamar al 911 de inmediato; usar solo habilidades para las que se está entrenado", ScenarioOutcome.Safe, "La seguridad inmediata tiene prioridad. El temporizador debe pausarse automáticamente."),
            ScenarioChoice("c", "Escribir primero una nota completa", ScenarioOutcome.CriticalFail, "La documentación viene después de activar ayuda inmediata."),
        ),
        rubricIds = listOf("emergency_priority", "911", "scope"),
        sourceTags = listOf("911", "hands_on"),
    ),
    Scenario(
        id = "w05_skin_observation",
        week = 5,
        titleEs = "Observar sin diagnosticar",
        stemEs = "Escenario ficticio: Mientras ayuda a vestirse, el alumno observa una zona roja nueva en el talón y la persona dice que duele.",
        choices = listOf(
            ScenarioChoice("a", "Diagnosticar una lesión y aplicar un tratamiento no indicado", ScenarioOutcome.CriticalFail, "Diagnosticar y decidir tratamiento excede el alcance no médico."),
            ScenarioChoice("b", "Registrar ubicación, apariencia observable, hora y palabras de la persona; escalar según el plan", ScenarioOutcome.Safe, "La nota objetiva permite una evaluación apropiada por quien corresponde."),
            ScenarioChoice("c", "Ignorarlo porque no estaba en el turno anterior", ScenarioOutcome.NeedsCorrection, "Un cambio nuevo debe comunicarse según el plan."),
        ),
        rubricIds = listOf("objective_observation", "scope", "escalation"),
        sourceTags = listOf("care_plan", "scope"),
    ),
    Scenario(
        id = "w06_untrained_lift",
        week = 6,
        titleEs = "La grúa desconocida",
        stemEs = "Escenario ficticio: El plan menciona una grúa, pero el alumno nunca recibió práctica presencial con ese modelo y hoy no está la segunda persona indicada.",
        choices = listOf(
            ScenarioChoice("a", "Ver un video rápido y hacer la transferencia solo", ScenarioOutcome.CriticalFail, "Un video no demuestra competencia ni reemplaza la ayuda indicada."),
            ScenarioChoice("b", "No realizar la transferencia, mantener a la persona segura y pedir apoyo cualificado según el plan", ScenarioOutcome.Safe, "Reconocer límites evita improvisación de alto riesgo."),
            ScenarioChoice("c", "Cambiar el plan para que ya no requiera dos personas", ScenarioOutcome.CriticalFail, "El alumno no puede alterar unilateralmente el plan."),
        ),
        rubricIds = listOf("hands_on_limit", "care_plan", "escalation"),
        sourceTags = listOf("hands_on", "employer_policy"),
    ),
    Scenario(
        id = "w07_swallowing_change",
        week = 7,
        titleEs = "Cambio durante la comida",
        stemEs = "Escenario ficticio: Durante el almuerzo, la persona comienza a toser repetidamente y su voz suena diferente. No hay obstrucción completa visible, pero es un cambio nuevo.",
        choices = listOf(
            ScenarioChoice("a", "Cambiar la textura de la dieta por cuenta propia", ScenarioOutcome.CriticalFail, "No cambies una dieta indicada sin autoridad; detén la ayuda insegura y escala."),
            ScenarioChoice("b", "Pausar la alimentación, observar, seguir el plan y pedir evaluación; llamar al 911 si aparece peligro inmediato", ScenarioOutcome.Safe, "Responde al cambio sin diagnosticar ni improvisar tratamiento."),
            ScenarioChoice("c", "Animarla a comer más rápido para terminar", ScenarioOutcome.CriticalFail, "Aumenta el riesgo y no respeta las señales observadas."),
        ),
        rubricIds = listOf("stop_unsafe_action", "care_plan", "emergency_escalation"),
        sourceTags = listOf("care_plan", "911"),
    ),
    Scenario(
        id = "w08_sudden_confusion",
        week = 8,
        titleEs = "No todo cambio es demencia",
        stemEs = "Escenario ficticio: Una persona que usualmente conversa con claridad está de repente muy desorientada y somnolienta.",
        choices = listOf(
            ScenarioChoice("a", "Asumir que es parte normal de la demencia y esperar al próximo turno", ScenarioOutcome.CriticalFail, "Un cambio repentino respecto a la línea base requiere escalamiento urgente."),
            ScenarioChoice("b", "Proteger seguridad, observar hechos y activar el contacto urgente o 911 según el plan y el peligro", ScenarioOutcome.Safe, "La respuesta se basa en el cambio y el riesgo, no en un diagnóstico del cuidador."),
            ScenarioChoice("c", "Dar un medicamento adicional", ScenarioOutcome.CriticalFail, "No se decide ni cambia una dosis para tratar un cambio observado."),
        ),
        rubricIds = listOf("baseline_change", "scope", "urgent_escalation"),
        sourceTags = listOf("care_plan", "911"),
    ),
    Scenario(
        id = "w09_language_uncertainty",
        week = 9,
        titleEs = "No fingir que entendiste",
        stemEs = "Escenario ficticio: Un supervisor da en inglés una instrucción nueva relacionada con una medicación. El alumno reconoce solo parte de la frase.",
        choices = listOf(
            ScenarioChoice("a", "Decir que sí y adivinar después", ScenarioOutcome.CriticalFail, "Adivinar una instrucción de seguridad crea riesgo."),
            ScenarioChoice("b", "Pedir que repita lentamente, confirmar con teach-back y usar apoyo lingüístico autorizado", ScenarioOutcome.Safe, "La aclaración explícita es una habilidad de seguridad."),
            ScenarioChoice("c", "Traducir con una app personal incluyendo nombre y diagnóstico del cliente", ScenarioOutcome.CriticalFail, "Puede exponer información privada y no garantiza exactitud."),
        ),
        rubricIds = listOf("clarification", "teach_back", "privacy"),
        sourceTags = listOf("privacy", "language_access"),
    ),
    Scenario(
        id = "w10_objective_handoff",
        week = 10,
        titleEs = "Hechos en la entrega de turno",
        stemEs = "Escenario ficticio: La nota dice: “El cliente fue difícil y parece deprimido. No quiso comer”.",
        choices = listOf(
            ScenarioChoice("a", "Copiar la frase porque es breve", ScenarioOutcome.NeedsCorrection, "Incluye juicio y diagnóstico no autorizado."),
            ScenarioChoice("b", "Registrar hora, alimento ofrecido, cantidad observada, palabras exactas, acción y a quién se notificó", ScenarioOutcome.Safe, "La entrega queda objetiva y accionable."),
            ScenarioChoice("c", "Eliminar todo para proteger privacidad", ScenarioOutcome.NeedsCorrection, "La privacidad exige canal adecuado, no omitir información necesaria del cuidado."),
        ),
        rubricIds = listOf("objective_facts", "chronology", "action_and_notification"),
        sourceTags = listOf("documentation", "privacy"),
    ),
    Scenario(
        id = "w11_financial_abuse_report",
        week = 11,
        titleEs = "La tarjeta bancaria",
        stemEs = "Escenario ficticio: Una persona mayor dice que un familiar tomó su tarjeta y retiró dinero sin permiso. El cuidador cubierto por WIC §15630 está en una casa particular.",
        choices = listOf(
            ScenarioChoice("a", "Confrontar al familiar y esperar una confesión", ScenarioOutcome.CriticalFail, "No investigues ni retrases el reporte para obtener prueba."),
            ScenarioChoice("b", "Proteger ante peligro, escuchar sin prometer secreto, reportar de inmediato o tan pronto como sea posible a APS o policía según corresponda y documentar hechos", ScenarioOutcome.Safe, "Sigue el flujo de entorno domiciliario y separa reporte de investigación."),
            ScenarioChoice("c", "Publicar una advertencia con nombres en redes sociales", ScenarioOutcome.CriticalFail, "Viola privacidad y no sustituye un reporte legal."),
        ),
        rubricIds = listOf("immediate_safety", "report_timing", "correct_channel", "objective_documentation"),
        sourceTags = listOf("WIC_15630", "APS"),
    ),
    Scenario(
        id = "w12_missed_medication",
        week = 12,
        titleEs = "La dosis que no aparece",
        stemEs = "Escenario ficticio: El organizador muestra una dosis todavía presente, pero el registro no aclara si fue omitida. La persona pregunta si debe tomar dos.",
        choices = listOf(
            ScenarioChoice("a", "Recomendar dos dosis para compensar", ScenarioOutcome.CriticalFail, "El cuidador no debe decidir ni cambiar la dosis."),
            ScenarioChoice("b", "No adivinar, mantener seguridad, seguir el plan y contactar al responsable autorizado para instrucciones", ScenarioOutcome.Safe, "Escala la discrepancia sin tomar una decisión clínica."),
            ScenarioChoice("c", "Alterar el registro para que parezca administrada", ScenarioOutcome.CriticalFail, "Nunca falsifiques ni completes un registro de memoria."),
        ),
        rubricIds = listOf("no_dose_change", "care_plan", "escalation", "record_integrity"),
        sourceTags = listOf("HSC_1796_12", "care_plan"),
    ),
    Scenario(
        id = "w13_false_school_claim",
        week = 13,
        titleEs = "La escuela “aprobada”",
        stemEs = "Escenario ficticio: Un anuncio promete que 110 horas dentro de una app otorgan certificación combinada HCA, IHSS, HHA y CNA de California.",
        choices = listOf(
            ScenarioChoice("a", "Inscribirse porque aparecen logotipos oficiales", ScenarioOutcome.CriticalFail, "Un logo no demuestra aprobación y las rutas no forman una credencial combinada."),
            ScenarioChoice("b", "Verificar la ruta, la agencia, el programa y la lista oficial; pedir por escrito qué credencial emite realmente", ScenarioOutcome.Safe, "La verificación separa formación complementaria de aprobación gubernamental."),
            ScenarioChoice("c", "Asumir que HCA significa licencia clínica", ScenarioOutcome.CriticalFail, "HCA es un registro no médico en este contexto, no una licencia clínica."),
        ),
        rubricIds = listOf("source_verification", "pathway_separation", "credential_language"),
        sourceTags = listOf("CDSS_HCA", "CDSS_IHSS", "CDPH_HHA", "CDPH_CNA"),
    ),
)

fun scenarioFor(week: Int): Scenario? = Scenarios.firstOrNull { it.week == week }
