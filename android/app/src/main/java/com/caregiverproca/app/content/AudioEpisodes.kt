package com.caregiverproca.app.content

/**
 * Verbatim from 02_PRODUCT_CONTENT/AUDIO_EPISODE_OUTLINES.csv — 13 episode
 * scripts (one per week), each with an English key phrase, a plain-Spanish
 * pronunciation guide, and a learner prompt for recorded practice.
 *
 * IMPORTANT: this pack ships no actual audio files (no .mp3/.wav anywhere in
 * `06_SOURCE_REFERENCE` or `04_DESIGN_ASSETS`) — only these scripts. There is
 * therefore no narrated audio to play back with Media3 yet; producing real
 * narration would need either a recording session or a text-to-speech
 * service, and the latter is exactly the kind of external service this
 * session was asked to avoid for a first Android app. See
 * docs/caregiver_upgrade/DECISIONS.md (ADR-006) for how this is handled:
 * the script is shown as read-along content, and the "recording" feature
 * lets the learner record themselves reading it (real, local, no service).
 */
data class AudioEpisodeOutline(
    val episodeId: String,
    val week: Int,
    val titleEs: String,
    val targetMinutes: Int,
    val keyPhraseEn: String,
    val pronunciationEs: String,
    val learnerPromptEs: String,
    val sourceTags: List<String>,
)

val AudioEpisodeOutlines: List<AudioEpisodeOutline> = listOf(
    AudioEpisodeOutline("audio_w01", 1, "Dignidad, consentimiento y alcance", 12, "How would you like me to help?", "jau wud yu laik mi tu jelp", "Explica en 60 segundos cómo pedir permiso y mantenerte en tu función", listOf("consent", "scope")),
    AudioEpisodeOutline("audio_w02", 2, "Seis rutas, seis procesos", 14, "Which pathway applies to me?", "uich PATH-uei a-PLAIS tu mi", "Compara IHSS con HCA afiliado sin usar la palabra certificación", listOf("CDSS_HCA", "CDSS_IHSS")),
    AudioEpisodeOutline("audio_w03", 3, "Cortar la contaminación cruzada", 12, "I need to clean my hands before the next task.", "ai nid tu klin mai jands bi-FOR de nekst task", "Describe una secuencia segura entre cuidado personal y comida", listOf("infection_control")),
    AudioEpisodeOutline("audio_w04", 4, "La emergencia primero", 10, "Call 911 now.", "kol nain uan uan nau", "Di qué ocurre con el temporizador ante peligro inmediato", listOf("911")),
    AudioEpisodeOutline("audio_w05", 5, "Ayudar sin tomar control", 12, "Would you like a full bath or a partial wash?", "wud yu laik a ful bath or a PAR-shal uash", "Ofrece dos opciones respetuosas ante una tarea diaria", listOf("consent", "ADL")),
    AudioEpisodeOutline("audio_w06", 6, "Movilidad: saber cuándo detenerse", 13, "I am not trained to use this lift safely.", "ai am not treind tu ius dis lift SEIF-li", "Practica pedir apoyo sin culpar a la persona", listOf("hands_on", "care_plan")),
    AudioEpisodeOutline("audio_w07", 7, "Comidas, hidratación y cambios", 12, "I noticed a new change while you were eating.", "ai NOU-tist a niu cheinch uail yu uer I-ting", "Entrega un reporte objetivo de un cambio durante la comida", listOf("care_plan", "scope")),
    AudioEpisodeOutline("audio_w08", 8, "Apoyo cognitivo centrado en la persona", 14, "You seem worried. I am here with you.", "yu sim UO-rid, ai am jir uiz yu", "Valida una emoción sin confirmar un hecho falso", listOf("dementia", "communication")),
    AudioEpisodeOutline("audio_w09", 9, "Aclarar en inglés con seguridad", 15, "Please say that again slowly, and let me repeat it back.", "plis sei dat a-GUEN SLOU-li, and let mi ri-PIT it bak", "Practica pedir repetición y usar teach-back", listOf("language_access", "teach_back")),
    AudioEpisodeOutline("audio_w10", 10, "Una entrega de turno objetiva", 13, "At 5:45, I observed... I notified... The next step is...", "at faiv FOR-ti faiv, ai ob-SERVD; ai NOU-ti-faid; de nekst step is", "Graba una entrega de 60 segundos con hora, hecho, acción y pendiente", listOf("handoff", "documentation")),
    AudioEpisodeOutline("audio_w11", 11, "Reportar sin investigar", 14, "I need to report this concern right away.", "ai nid tu ri-PORT dis kon-SERN rait a-UEI", "Explica por qué no debes esperar una confesión", listOf("WIC_15630", "APS")),
    AudioEpisodeOutline("audio_w12", 12, "Ayuda con medicación dentro del alcance", 13, "I cannot change the dose. I will follow the plan and call the authorized person.", "ai KA-not cheinch de dous; ai uil FA-lou de plan and kol di O-tho-raizd PER-son", "Responde a una dosis dudosa sin dar consejo clínico", listOf("HSC_1796_12")),
    AudioEpisodeOutline("audio_w13", 13, "Próximos pasos sin promesas falsas", 12, "This is internal training, not a state credential.", "dis is in-TER-nal TREI-ning, not a steit kri-DEN-shal", "Presenta tu aprendizaje en una entrevista con lenguaje honesto", listOf("disclaimer", "pathways")),
)

fun audioEpisodeFor(week: Int): AudioEpisodeOutline? = AudioEpisodeOutlines.firstOrNull { it.week == week }
