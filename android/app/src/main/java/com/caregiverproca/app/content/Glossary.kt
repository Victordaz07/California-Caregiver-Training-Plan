package com.caregiverproca.app.content

/** Verbatim from 02_PRODUCT_CONTENT/GLOSSARY_ES_EN.csv — 30 bilingual support terms. */
data class GlossaryTerm(
    val id: String,
    val spanish: String,
    val english: String,
    val plainPronunciationEs: String,
    val meaningEs: String,
)

val Glossary: List<GlossaryTerm> = listOf(
    GlossaryTerm("scope", "alcance de la función", "scope", "skoup", "Tareas que puedes realizar según tu rol, formación, autorización y plan"),
    GlossaryTerm("care_plan", "plan de cuidado", "care plan", "ker plan", "Instrucciones individualizadas que guían el apoyo"),
    GlossaryTerm("consent", "consentimiento", "consent", "kon-SENT", "Permiso informado y voluntario de la persona"),
    GlossaryTerm("privacy", "privacidad", "privacy", "PRAI-va-si", "Protección de información y espacio personal"),
    GlossaryTerm("dignity", "dignidad", "dignity", "DIG-ni-ti", "Trato respetuoso que reconoce valor y autonomía"),
    GlossaryTerm("handoff", "entrega de turno", "handoff", "JAND-of", "Comunicación estructurada de hechos, acciones y pendientes"),
    GlossaryTerm("observe", "observar", "observe", "ob-SERV", "Notar hechos visibles sin diagnosticar"),
    GlossaryTerm("report", "reportar", "report", "ri-PORT", "Comunicar hechos por el canal requerido"),
    GlossaryTerm("escalate", "escalar o pedir ayuda", "escalate", "ES-ka-leit", "Contactar a la persona responsable o servicio de emergencia"),
    GlossaryTerm("emergency", "emergencia", "emergency", "i-MER-yen-si", "Situación de peligro que requiere respuesta inmediata"),
    GlossaryTerm("fall", "caída", "fall", "fol", "Descenso accidental al suelo o nivel inferior"),
    GlossaryTerm("choking", "atragantamiento", "choking", "CHOU-king", "Bloqueo de la vía aérea por alimento u objeto"),
    GlossaryTerm("infection_control", "control de infección", "infection control", "in-FEK-shon kon-TROL", "Medidas para reducir transmisión de microorganismos"),
    GlossaryTerm("hand_hygiene", "higiene de manos", "hand hygiene", "jand JAI-yin", "Limpieza adecuada de manos en momentos clave"),
    GlossaryTerm("PPE", "equipo de protección personal", "personal protective equipment", "PER-so-nal pro-TEK-tiv i-KUIP-ment", "Barreras como guantes según riesgo y política"),
    GlossaryTerm("ADL", "actividad de la vida diaria", "activity of daily living", "ak-TI-vi-ti ov DEI-li LI-ving", "Tarea cotidiana como baño, vestido o alimentación"),
    GlossaryTerm("IADL", "actividad instrumental de la vida diaria", "instrumental activity of daily living", "in-stru-MEN-tal ak-TI-vi-ti", "Tarea como compras, comidas o limpieza ligera"),
    GlossaryTerm("mobility", "movilidad", "mobility", "mou-BI-li-ti", "Capacidad de moverse de forma segura"),
    GlossaryTerm("transfer", "transferencia", "transfer", "trans-FER", "Movimiento entre superficies con técnica/equipo autorizado"),
    GlossaryTerm("assist", "ayudar", "assist", "a-SIST", "Dar apoyo sin quitar independencia innecesariamente"),
    GlossaryTerm("cue", "indicación o pista", "cue", "kiu", "Señal verbal o visual breve para apoyar una acción"),
    GlossaryTerm("redirection", "redirección", "redirection", "ri-da-REK-shon", "Cambio calmado de atención hacia una opción segura"),
    GlossaryTerm("mandated_reporter", "reportante obligatorio", "mandated reporter", "MAN-dei-tid ri-POR-ter", "Persona a quien la ley exige reportar ciertas sospechas o hechos"),
    GlossaryTerm("APS", "Servicios de Protección para Adultos", "Adult Protective Services", "A-dolt pro-TEK-tiv SER-vi-ses", "Agencia que recibe y evalúa reportes de abuso en la comunidad"),
    GlossaryTerm("Live_Scan", "Live Scan", "Live Scan", "laiv skan", "Servicio electrónico de huellas para verificación de antecedentes"),
    GlossaryTerm("registry", "registro", "registry", "RE-yis-tri", "Lista o estado administrativo; no equivale automáticamente a licencia"),
    GlossaryTerm("credential", "credencial", "credential", "kri-DEN-shal", "Reconocimiento formal emitido por una entidad autorizada"),
    GlossaryTerm("supervisor", "supervisor", "supervisor", "SU-per-vai-zor", "Persona designada para orientar, revisar o recibir escalamiento"),
    GlossaryTerm("due_review", "repaso pendiente", "due review", "diu ri-VIU", "Elemento que el programador marca para revisar ahora"),
    GlossaryTerm("fictional", "ficticio", "fictional", "FIK-sho-nal", "Creado para practicar; no corresponde a una persona real"),
)
