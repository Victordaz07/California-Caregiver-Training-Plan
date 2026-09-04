package com.caregiverproca.app.content

/** Verbatim from 02_PRODUCT_CONTENT/RUBRIC_CATALOG.json — 34 observable criteria used by [Scenario]s. */
data class RubricCriterion(
    val id: String,
    val labelEs: String,
    val descriptionEs: String,
    val critical: Boolean,
)

val RubricCriteria: List<RubricCriterion> = listOf(
    RubricCriterion("911", "Activación del 911", "Llama al 911 ante peligro inmediato y no retrasa por la lección.", true),
    RubricCriterion("accurate_language", "Lenguaje regulatorio exacto", "Usa registro, inscripción, certificación o práctica según la ruta real.", true),
    RubricCriterion("action_and_notification", "Acción y notificación", "Indica qué se hizo y a quién se informó.", false),
    RubricCriterion("baseline_change", "Cambio de línea base", "Reconoce y comunica un cambio nuevo sin diagnosticar.", true),
    RubricCriterion("care_plan", "Uso del plan de cuidado", "Consulta y sigue instrucciones individualizadas y contactos.", true),
    RubricCriterion("chronology", "Cronología", "Registra hora y secuencia sin rellenar datos desconocidos.", false),
    RubricCriterion("clarification", "Aclaración", "Pide repetición o explicación cuando no entiende.", true),
    RubricCriterion("consent", "Consentimiento", "Pide permiso y responde sin coacción a una pausa o rechazo.", true),
    RubricCriterion("correct_channel", "Canal correcto", "Usa 911, APS, policía o responsable según peligro, entorno y regla.", true),
    RubricCriterion("credential_language", "Lenguaje de credenciales", "No presenta formación interna como aprobación estatal.", true),
    RubricCriterion("dignity", "Dignidad", "Mantiene respeto, elección y lenguaje no estigmatizante.", false),
    RubricCriterion("emergency_escalation", "Escalamiento de emergencia", "Reconoce cuándo el plan o 911 requiere acción urgente.", true),
    RubricCriterion("emergency_priority", "Prioridad de emergencia", "Actúa antes de timer, documentación o progreso.", true),
    RubricCriterion("escalation", "Escalamiento", "Contacta a la persona autorizada con hechos útiles.", true),
    RubricCriterion("hand_hygiene", "Higiene de manos", "La realiza en el momento correcto y no confía solo en guantes.", true),
    RubricCriterion("hands_on_limit", "Límite de práctica manual", "No improvisa equipo o técnica sin formación/verificación.", true),
    RubricCriterion("immediate_safety", "Seguridad inmediata", "Protege a la persona antes de tareas secundarias.", true),
    RubricCriterion("no_dose_change", "No cambiar dosis", "No recomienda, duplica, omite o cambia una dosis por decisión propia.", true),
    RubricCriterion("objective_documentation", "Documentación objetiva", "Registra hechos y palabras exactas sin juicio.", false),
    RubricCriterion("objective_facts", "Hechos observables", "Distingue observación de interpretación o diagnóstico.", false),
    RubricCriterion("objective_observation", "Observación objetiva", "Describe lugar, apariencia, hora y cambio observable.", false),
    RubricCriterion("pathway_identification", "Identificación de ruta", "Pregunta la relación/objetivo antes de mostrar requisitos.", true),
    RubricCriterion("pathway_separation", "Separación de rutas", "No combina HCA, IHSS, HHA y CNA.", true),
    RubricCriterion("ppe", "PPE adecuado", "Usa, cambia y retira PPE según riesgo, formación y política.", true),
    RubricCriterion("privacy", "Privacidad", "Minimiza datos, audiencia, canal y grabación.", true),
    RubricCriterion("record_integrity", "Integridad del registro", "No falsifica, retrofecha ni completa de memoria.", true),
    RubricCriterion("report_timing", "Tiempo del reporte", "No espera confirmación; respeta reporte inmediato y seguimiento aplicable.", true),
    RubricCriterion("scope", "Alcance de la función", "No diagnostica, prescribe o ejecuta tarea no autorizada.", true),
    RubricCriterion("source_use", "Uso de fuente", "Consulta autoridad oficial, fecha, jurisdicción y ruta.", false),
    RubricCriterion("source_verification", "Verificación de fuente", "No confía solo en logos o marketing; verifica lista/página oficial.", true),
    RubricCriterion("stop_unsafe_action", "Detener acción insegura", "Pausa una tarea cuando surge riesgo o cambio no resuelto.", true),
    RubricCriterion("task_sequence", "Secuencia de tareas", "Evita trasladar contaminación o perder pasos esenciales.", false),
    RubricCriterion("teach_back", "Teach-back", "Confirma comprensión sin culpar ni examinar a la persona.", false),
    RubricCriterion("urgent_escalation", "Escalamiento urgente", "Comunica de inmediato un cambio agudo o riesgo alto.", true),
)

fun rubricCriterion(id: String): RubricCriterion? = RubricCriteria.firstOrNull { it.id == id }
