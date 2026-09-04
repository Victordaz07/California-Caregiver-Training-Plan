package com.caregiverproca.app.content

/**
 * Verbatim from 02_PRODUCT_CONTENT/LEGAL_REQUIREMENTS_CA.json
 * (schemaVersion 1, verifiedAt 2026-09-04, jurisdiction California, US).
 *
 * Every legal claim shown in the UI must come from one of these entries (or
 * from [OfficialResources]) rather than being typed inline in a screen, so a
 * single source review updates every screen that cites it.
 */
enum class RequirementStatus {
    RequiredForPathway,
    ConditionalByPathway,
    FutureRequirement,
    RecommendedOrExternalRequirement,
    RoleAndFactDependent,
    InternalOnly,
}

data class LegalRequirement(
    val id: String,
    val pathwayIds: List<PathwayId>,
    val titleEs: String,
    val status: RequirementStatus,
    val summaryEs: String,
    val effectiveFrom: String? = null,
    val feeUsd: Int? = null,
    val sourceUrls: List<String> = emptyList(),
)

private val allPathways = PathwayId.entries.toList()

val LegalRequirements: List<LegalRequirement> = listOf(
    LegalRequirement(
        id = "hca_registration_affiliated",
        pathwayIds = listOf(PathwayId.HcaAffiliatedHco),
        titleEs = "Registro HCA para afiliación con HCO",
        status = RequirementStatus.RequiredForPathway,
        summaryEs = "El HCA afiliado debe completar el registro y la verificación de antecedentes aplicables antes de prestar servicios conforme al proceso de la HCO.",
        sourceUrls = listOf(
            "https://leginfo.legislature.ca.gov/faces/codes_displayText.xhtml?article=7.&chapter=13.&division=2.&lawCode=HSC&part=&title=",
            "https://www.cdss.ca.gov/inforesources/community-care/home-care-services/home-care-aide-application-process",
        ),
    ),
    LegalRequirement(
        id = "hca_guardian_application",
        pathwayIds = listOf(PathwayId.HcaAffiliatedHco, PathwayId.DirectPrivateCaregiver),
        titleEs = "Solicitud Guardian y Live Scan",
        status = RequirementStatus.ConditionalByPathway,
        summaryEs = "El proceso publicado incluye cuenta, solicitud, PIN del HCO o PIN independiente, tarifa verificada en esta fecha y Live Scan. No se describe un examen de conocimientos.",
        feeUsd = 35,
        sourceUrls = listOf(
            "https://www.cdss.ca.gov/inforesources/community-care/home-care-services/home-care-aide-application-process",
            "https://www.cdss.ca.gov/inforesources/community-care/home-care-services/application-fees",
        ),
    ),
    LegalRequirement(
        id = "hca_entry_training",
        pathwayIds = listOf(PathwayId.HcaAffiliatedHco),
        titleEs = "Cinco horas de formación inicial",
        status = RequirementStatus.RequiredForPathway,
        summaryEs = "Antes de estar con un cliente, un HCA afiliado completa como mínimo dos horas de orientación y tres horas de seguridad, incluidas precauciones básicas, emergencias y control de infección.",
        effectiveFrom = "2016-01-01",
        sourceUrls = listOf("https://leginfo.legislature.ca.gov/faces/codes_displayText.xhtml?article=8.&chapter=13.&division=2.&lawCode=HSC&part=&title="),
    ),
    LegalRequirement(
        id = "hca_annual_training",
        pathwayIds = listOf(PathwayId.HcaAffiliatedHco),
        titleEs = "Cinco horas anuales de formación",
        status = RequirementStatus.RequiredForPathway,
        summaryEs = "La formación anual cubre competencias centrales y población específica, incluidos derechos y seguridad, necesidades diarias, abuso/neglect, higiene y transporte cuando corresponda.",
        sourceUrls = listOf("https://leginfo.legislature.ca.gov/faces/codes_displayText.xhtml?article=8.&chapter=13.&division=2.&lawCode=HSC&part=&title="),
    ),
    LegalRequirement(
        id = "hca_dementia_2027",
        pathwayIds = listOf(PathwayId.HcaAffiliatedHco),
        titleEs = "Demencia como tema anual",
        status = RequirementStatus.FutureRequirement,
        effectiveFrom = "2027-01-01",
        summaryEs = "La versión de HSC §1796.44 operativa el 1 de enero de 2027 incluye las necesidades especiales de clientes con demencia entre las áreas anuales.",
        sourceUrls = listOf("https://leginfo.legislature.ca.gov/faces/codes_displayText.xhtml?article=8.&chapter=13.&division=2.&lawCode=HSC&part=&title="),
    ),
    LegalRequirement(
        id = "hca_tb",
        pathwayIds = listOf(PathwayId.HcaAffiliatedHco),
        titleEs = "Evaluación de tuberculosis",
        status = RequirementStatus.RequiredForPathway,
        summaryEs = "Para la ruta afiliada, HSC §1796.45 exige examen dentro del periodo legal y documentación de ausencia de riesgo de enfermedad activa; un resultado negativo se repite al menos cada dos años, con reglas específicas para resultados positivos documentados.",
        sourceUrls = listOf("https://leginfo.legislature.ca.gov/faces/codes_displayText.xhtml?article=8.&chapter=13.&division=2.&lawCode=HSC&part=&title="),
    ),
    LegalRequirement(
        id = "hca_cpr",
        pathwayIds = allPathways,
        titleEs = "CPR y primeros auxilios",
        status = RequirementStatus.RecommendedOrExternalRequirement,
        summaryEs = "Puede ser recomendado o exigido por el empleador, programa, puesto o asegurador. HSC §1796.43 no debe citarse como mandato general de CPR/AED.",
        sourceUrls = listOf("https://leginfo.legislature.ca.gov/faces/codes_displayText.xhtml?article=7.&chapter=13.&division=2.&lawCode=HSC&part=&title="),
    ),
    LegalRequirement(
        id = "ihss_enrollment",
        pathwayIds = listOf(PathwayId.IhssProvider),
        titleEs = "Inscripción de proveedor IHSS",
        status = RequirementStatus.RequiredForPathway,
        summaryEs = "El proceso incluye SOC 426, orientación, SOC 846, huellas y verificación de antecedentes DOJ, gestionado con el condado.",
        sourceUrls = listOf("https://www.cdss.ca.gov/inforesources/ihss/ihss-providers/orientation-process"),
    ),
    LegalRequirement(
        id = "mandated_reporting_home",
        pathwayIds = allPathways,
        titleEs = "Reporte de abuso en entorno domiciliario",
        status = RequirementStatus.RoleAndFactDependent,
        summaryEs = "Quienes están cubiertos por WIC §15630 deben reportar conocimiento u observación, alegato o sospecha razonable según la ley. El reporte es inmediato o tan pronto como sea posible; si el inicial es telefónico, se presenta seguimiento escrito/Internet dentro de dos días laborables. En peligro inmediato, prioriza el 911.",
        sourceUrls = listOf(
            "https://leginfo.legislature.ca.gov/faces/codes_displaySection.xhtml?lawCode=WIC&sectionNum=15630.",
            "https://www.cdss.ca.gov/adult-protective-services",
        ),
    ),
    LegalRequirement(
        id = "cna_training",
        pathwayIds = listOf(PathwayId.CertifiedNurseAssistant),
        titleEs = "Programa aprobado CNA",
        status = RequirementStatus.RequiredForPathway,
        summaryEs = "La ruta base publicada por CDPH incluye al menos 60 horas de aula y 100 horas de práctica clínica supervisada, además de los pasos oficiales aplicables.",
        sourceUrls = listOf("https://www.cdph.ca.gov/Programs/CHCQ/LCP/Pages/Nurse-Assistant-Training-Program-Applicants.aspx"),
    ),
    LegalRequirement(
        id = "hha_training",
        pathwayIds = listOf(PathwayId.HomeHealthAide),
        titleEs = "Programa aprobado HHA",
        status = RequirementStatus.RequiredForPathway,
        summaryEs = "CDPH publica una ruta básica HHA de 120 horas y una opción aprobada de 40 horas para candidatos elegibles con CNA/formación aplicable.",
        sourceUrls = listOf(
            "https://www.cdph.ca.gov/Programs/CHCQ/LCP/Pages/120-Hour-Home-Health-Aide-Training-Program-Applicants.aspx",
            "https://www.cdph.ca.gov/Programs/CHCQ/LCP/Pages/40-Hour-Home-Health-Aide-Training-Program-Applicants.aspx",
        ),
    ),
    LegalRequirement(
        id = "internal_completion",
        pathwayIds = allPathways,
        titleEs = "Finalización dentro de la app",
        status = RequirementStatus.InternalOnly,
        summaryEs = "La app puede registrar horas internas, conocimiento, práctica y observaciones. No concede una licencia, registro o certificación gubernamental.",
    ),
)

fun legalRequirementsFor(pathwayId: PathwayId): List<LegalRequirement> =
    LegalRequirements.filter { pathwayId in it.pathwayIds }

/** APS statewide line, routed by ZIP code — see mandated_reporting_home. */
const val ApsStatewidePhone = "1-833-401-0832"
