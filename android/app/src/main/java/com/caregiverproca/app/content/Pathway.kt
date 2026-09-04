package com.caregiverproca.app.content

/**
 * The six user pathways, verbatim from
 * caregiver_pro_ca_android_upgrade_pack_v1/02_PRODUCT_CONTENT/PATHWAYS_CA.json
 * (schemaVersion 1, verifiedAt 2026-09-04, jurisdiction California, US).
 *
 * These are deliberately NOT the same credential: HCA is a registry, IHSS is a
 * county-administered enrollment, HHA/CNA are CDPH-approved training pathways,
 * and family/direct-private caregivers may not need any of the above. Never
 * merge these into a single "certification" — that conflation is Critical
 * finding A-001 in 01_AUDIT/MATRIZ_CORRECCIONES.csv.
 */
enum class PathwayId {
    FamilyCaregiver,
    IhssProvider,
    HcaAffiliatedHco,
    DirectPrivateCaregiver,
    HomeHealthAide,
    CertifiedNurseAssistant,
}

data class Pathway(
    val id: PathwayId,
    val titleEs: String,
    val titleEn: String,
    val summaryEs: String,
    val stateCredentialFromApp: Boolean,
    val coreStepsEs: List<String>,
    val routeNoticeEs: String,
    val officialResourceIds: List<String>,
)

val Pathways: List<Pathway> = listOf(
    Pathway(
        id = PathwayId.FamilyCaregiver,
        titleEs = "Cuidador familiar o de apoyo informal",
        titleEn = "Family or informal caregiver",
        summaryEs = "Aprendes apoyo seguro y digno para una persona conocida; esta ruta no presume empleo ni una credencial estatal.",
        stateCredentialFromApp = false,
        coreStepsEs = listOf(
            "Acordar preferencias, consentimiento y límites con la persona y su red de apoyo.",
            "Seguir el plan de cuidado y las instrucciones de profesionales autorizados.",
            "Aprender seguridad, emergencias, prevención de infección y autocuidado del cuidador.",
            "Verificar requisitos adicionales si la relación cambia a empleo, IHSS o agencia.",
        ),
        routeNoticeEs = "No existe una credencial estatal universal de California para todo cuidador familiar. El entorno, la tarea y la fuente de pago pueden activar reglas distintas.",
        officialResourceIds = listOf("ca_aps", "ca_aging"),
    ),
    Pathway(
        id = PathwayId.IhssProvider,
        titleEs = "Proveedor IHSS",
        titleEn = "IHSS provider",
        summaryEs = "Inscripción de proveedor administrada por el condado para prestar servicios autorizados a una persona receptora de IHSS.",
        stateCredentialFromApp = false,
        coreStepsEs = listOf(
            "Completar y presentar el formulario de inscripción de proveedor SOC 426 según instrucciones del condado.",
            "Completar la orientación de proveedores IHSS.",
            "Firmar el acuerdo de inscripción SOC 846.",
            "Presentar huellas y completar la verificación de antecedentes del DOJ.",
            "Cumplir reglas de horas, timesheets y servicios autorizados de la persona receptora.",
        ),
        routeNoticeEs = "La app no realiza la inscripción, no recibe SSN ni antecedentes y no sustituye al condado o Public Authority. Salarios y beneficios varían por condado y elegibilidad.",
        officialResourceIds = listOf("ihss_orientation", "ihss_soc426", "ihss_forms_es", "sf_ihss_benefits"),
    ),
    Pathway(
        id = PathwayId.HcaAffiliatedHco,
        titleEs = "HCA afiliado a una HCO",
        titleEn = "Home Care Aide affiliated with an HCO",
        summaryEs = "Cuidador no médico empleado o vinculado a una Home Care Organization licenciada.",
        stateCredentialFromApp = false,
        coreStepsEs = listOf(
            "Solicitar el registro HCA y completar el proceso Guardian/Live Scan aplicable.",
            "Completar cinco horas iniciales antes de estar con un cliente: dos de orientación y tres de seguridad.",
            "Completar al menos cinco horas de formación anual aplicable.",
            "Cumplir la evaluación y documentación de tuberculosis de HSC §1796.45.",
            "Seguir políticas, supervisión, plan de cuidado y formación del empleador.",
        ),
        routeNoticeEs = "HCA es un registro, no una licencia. Esta app no registra al usuario ni certifica cumplimiento del empleador.",
        officialResourceIds = listOf("hca_application", "hca_fees", "hca_resources", "hsc_article8"),
    ),
    Pathway(
        id = PathwayId.DirectPrivateCaregiver,
        titleEs = "Cuidador privado o contratación directa",
        titleEn = "Direct-hire or private caregiver",
        summaryEs = "Acuerdo directo con una persona o familia, fuera de una HCO; puede existir registro HCA independiente, pero no debe confundirse con IHSS o empleo de agencia.",
        stateCredentialFromApp = false,
        coreStepsEs = listOf(
            "Definir por escrito tareas, horario, límites no médicos, pago y plan de emergencia.",
            "Verificar obligaciones laborales, fiscales, de seguro y locales aplicables a la relación concreta.",
            "Considerar el registro HCA independiente y antecedentes para confianza del consumidor, según la ruta elegida.",
            "No aceptar tareas clínicas fuera de formación, autoridad y supervisión legal.",
        ),
        routeNoticeEs = "Una contratación directa no convierte automáticamente al cuidador en HCA afiliado. Las obligaciones dependen de hechos específicos; busca asesoría oficial o profesional cuando sea necesario.",
        officialResourceIds = listOf("hsc_article2", "hca_application", "hca_resources"),
    ),
    Pathway(
        id = PathwayId.HomeHealthAide,
        titleEs = "Home Health Aide (HHA)",
        titleEn = "Home Health Aide (HHA)",
        summaryEs = "Ruta de certificación CDPH para trabajo de salud en el hogar dentro del contexto autorizado y supervisado correspondiente.",
        stateCredentialFromApp = false,
        coreStepsEs = listOf(
            "Verificar elegibilidad y seleccionar un programa HHA aprobado por CDPH.",
            "Completar la ruta básica de 120 horas o, si corresponde por CNA activo/formación previa, una ruta aprobada de 40 horas.",
            "Completar solicitud y requisitos vigentes publicados por CDPH.",
            "Trabajar dentro del plan, la autoridad y la supervisión del entorno de salud en el hogar.",
        ),
        routeNoticeEs = "El contenido de esta app es preparación complementaria y no sustituye un programa HHA aprobado ni concede certificación CDPH.",
        officialResourceIds = listOf("cdph_hha", "cdph_hha_120", "cdph_hha_40"),
    ),
    Pathway(
        id = PathwayId.CertifiedNurseAssistant,
        titleEs = "Certified Nurse Assistant (CNA)",
        titleEn = "Certified Nurse Assistant (CNA)",
        summaryEs = "Ruta de auxiliar de enfermería con programa aprobado, práctica clínica supervisada y evaluación oficial aplicable.",
        stateCredentialFromApp = false,
        coreStepsEs = listOf(
            "Verificar elegibilidad y matricularse en un Nurse Assistant Training Program aprobado por CDPH.",
            "Completar como mínimo 60 horas de aula y 100 horas de práctica clínica supervisada en la ruta base publicada.",
            "Cumplir solicitud, evaluación de competencia y demás requisitos vigentes.",
            "Mantener el certificado conforme a reglas CDPH y del empleador.",
        ),
        routeNoticeEs = "La app no es un NATP aprobado, no ofrece horas clínicas y no concede ni renueva el certificado CNA.",
        officialResourceIds = listOf("cdph_cna_training", "cdph_cna"),
    ),
)

fun pathwayById(id: PathwayId): Pathway = Pathways.first { it.id == id }
