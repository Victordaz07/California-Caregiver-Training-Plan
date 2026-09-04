package com.caregiverproca.app.content

/**
 * Verbatim from 02_PRODUCT_CONTENT/OFFICIAL_RESOURCES.json
 * (schemaVersion 1, verifiedAt 2026-09-04, jurisdiction California, US).
 *
 * Open these only via Custom Tabs, and only when the host is in [AllowedResourceHosts].
 */
data class OfficialResource(
    val id: String,
    val titleEs: String,
    val agency: String,
    val url: String,
    val pathwayIds: List<PathwayId>,
    val phone: String? = null,
    val geography: String? = null,
)

val OfficialResources: List<OfficialResource> = listOf(
    OfficialResource("hsc_article1", "HSC, definiciones de Home Care Services", "California Legislature", "https://leginfo.legislature.ca.gov/faces/codes_displayText.xhtml?article=1.&chapter=13.&division=2.&lawCode=HSC&part=&title=", listOf(PathwayId.HcaAffiliatedHco, PathwayId.DirectPrivateCaregiver)),
    OfficialResource("hsc_article2", "HSC, HCA independiente y contratación directa", "California Legislature", "https://leginfo.legislature.ca.gov/faces/codes_displayText.xhtml?article=2.&chapter=13.&division=2.&lawCode=HSC&part=&title=", listOf(PathwayId.DirectPrivateCaregiver)),
    OfficialResource("hsc_article7", "HSC, requisitos de Home Care Aide", "California Legislature", "https://leginfo.legislature.ca.gov/faces/codes_displayText.xhtml?article=7.&chapter=13.&division=2.&lawCode=HSC&part=&title=", listOf(PathwayId.HcaAffiliatedHco)),
    OfficialResource("hsc_article8", "HSC, formación y tuberculosis para HCA afiliado", "California Legislature", "https://leginfo.legislature.ca.gov/faces/codes_displayText.xhtml?article=8.&chapter=13.&division=2.&lawCode=HSC&part=&title=", listOf(PathwayId.HcaAffiliatedHco)),
    OfficialResource("hca_application", "Solicitud y renovación HCA en Guardian", "CDSS", "https://www.cdss.ca.gov/inforesources/community-care/home-care-services/home-care-aide-application-process", listOf(PathwayId.HcaAffiliatedHco, PathwayId.DirectPrivateCaregiver)),
    OfficialResource("hca_fees", "Tarifas de Home Care Services", "CDSS", "https://www.cdss.ca.gov/inforesources/community-care/home-care-services/application-fees", listOf(PathwayId.HcaAffiliatedHco, PathwayId.DirectPrivateCaregiver)),
    OfficialResource("hca_resources", "Recursos para Home Care Aides", "CDSS", "https://www.cdss.ca.gov/inforesources/community-care/home-care-services/resources-for-home-care-aides", listOf(PathwayId.HcaAffiliatedHco, PathwayId.DirectPrivateCaregiver)),
    OfficialResource("ihss_orientation", "Proceso de orientación e inscripción IHSS", "CDSS", "https://www.cdss.ca.gov/inforesources/ihss/ihss-providers/orientation-process", listOf(PathwayId.IhssProvider)),
    OfficialResource("ihss_soc426", "Formulario SOC 426", "CDSS", "https://www.cdss.ca.gov/cdssweb/entres/forms/English/SOC426.PDF", listOf(PathwayId.IhssProvider)),
    OfficialResource("ihss_forms_es", "Formularios CDSS traducidos al español", "CDSS", "https://www.cdss.ca.gov/inforesources/forms-brochures/translated-forms-and-publications/spanish", listOf(PathwayId.IhssProvider)),
    OfficialResource("wic_abandonment", "WIC §15610.05 — Abandono", "California Legislature", "https://leginfo.legislature.ca.gov/faces/codes_displaySection.xhtml?lawCode=WIC&sectionNum=15610.05.", PathwayId.entries.toList()),
    OfficialResource("wic_reporting", "WIC §15630 — Reporte obligatorio", "California Legislature", "https://leginfo.legislature.ca.gov/faces/codes_displaySection.xhtml?lawCode=WIC&sectionNum=15630.", PathwayId.entries.toList()),
    OfficialResource("ca_aps", "Adult Protective Services y línea estatal", "CDSS", "https://www.cdss.ca.gov/adult-protective-services", PathwayId.entries.toList(), phone = ApsStatewidePhone),
    OfficialResource("aps_forms", "Formularios APS y SOC 341", "CDSS", "https://www.cdss.ca.gov/inforesources/cdss-programs/adult-protective-services/program-forms", listOf(PathwayId.IhssProvider, PathwayId.HcaAffiliatedHco, PathwayId.DirectPrivateCaregiver, PathwayId.HomeHealthAide, PathwayId.CertifiedNurseAssistant)),
    OfficialResource("cdph_cna_training", "Nurse Assistant Training Program", "CDPH", "https://www.cdph.ca.gov/Programs/CHCQ/LCP/Pages/Nurse-Assistant-Training-Program-Applicants.aspx", listOf(PathwayId.CertifiedNurseAssistant)),
    OfficialResource("cdph_cna", "Programas de formación Certified Nurse Assistant", "CDPH", "https://www.cdph.ca.gov/Programs/CHCQ/LCP/Pages/Certified-Nurse-Assistant-Training-Programs.aspx", listOf(PathwayId.CertifiedNurseAssistant)),
    OfficialResource("cdph_hha", "Home Health Aide", "CDPH", "https://www.cdph.ca.gov/Programs/CHCQ/LCP/Pages/hha.aspx", listOf(PathwayId.HomeHealthAide)),
    OfficialResource("cdph_hha_120", "Programa HHA de 120 horas", "CDPH", "https://www.cdph.ca.gov/Programs/CHCQ/LCP/Pages/120-Hour-Home-Health-Aide-Training-Program-Applicants.aspx", listOf(PathwayId.HomeHealthAide)),
    OfficialResource("cdph_hha_40", "Programa HHA de 40 horas", "CDPH", "https://www.cdph.ca.gov/Programs/CHCQ/LCP/Pages/40-Hour-Home-Health-Aide-Training-Program-Applicants.aspx", listOf(PathwayId.HomeHealthAide)),
    OfficialResource("ca_aging", "California Department of Aging", "California Department of Aging", "https://aging.ca.gov/", listOf(PathwayId.FamilyCaregiver)),
    OfficialResource("sf_ihss_benefits", "Beneficios y capacitación para proveedores IHSS de San Francisco", "San Francisco Human Services Agency", "https://www.sfhsa.org/services/disability-aging/home-care/home-supportive-services-ihss/provide-home-supportive-services-ihss/get-ihss-job-benefits-and-training", listOf(PathwayId.IhssProvider), geography = "San Francisco County"),
)

/** Only open Custom Tabs to these hosts — matches OFFICIAL_RESOURCES.json's allowedHosts. */
val AllowedResourceHosts: Set<String> = setOf(
    "leginfo.legislature.ca.gov",
    "www.cdss.ca.gov",
    "guardian.dss.ca.gov",
    "www.cdph.ca.gov",
    "aging.ca.gov",
    "www.sfhsa.org",
)

fun officialResourcesFor(pathwayId: PathwayId): List<OfficialResource> =
    OfficialResources.filter { pathwayId in it.pathwayIds }
