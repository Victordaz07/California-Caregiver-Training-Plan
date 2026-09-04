# Informe de contenido regulatorio — Caregiver Pro CA (Android)

Fecha de verificación: 2026-09-04 (heredada de `caregiver_pro_ca_android_upgrade_pack_v1`, `verifiedAt` en cada archivo JSON fuente)
Revisor: sesión de Claude Code — no es asesoría legal ni revisión por un profesional autorizado en California. Debe revisarse por una persona cualificada antes de cualquier lanzamiento.
Versión del bundle: contenido embebido directamente en Kotlin (`content/*.kt`), no hay versión de bundle descargable todavía (ver A-045 pendiente).

## Claims activos

| Claim ID | Texto mostrado | Ruta | Fuente primaria | Vigencia | Verificado | Estado |
|---|---|---|---|---|---|---|
| hca_registration_affiliated | "Registro HCA para afiliación con HCO" | hca_affiliated_hco | HSC art. 7, CDSS application process | — | 2026-09-04 | Activo |
| hca_guardian_application | "Solicitud Guardian y Live Scan", tarifa $35 | hca_affiliated_hco, direct_private_caregiver | CDSS application process + application fees | — | 2026-09-04 (tarifa marcada como mutable) | Activo |
| hca_entry_training | "Cinco horas de formación inicial" (2 orientación + 3 seguridad) | hca_affiliated_hco | HSC art. 8 | Vigente desde 2016-01-01 | 2026-09-04 | Activo |
| hca_annual_training | "Cinco horas anuales de formación" | hca_affiliated_hco | HSC art. 8 | — | 2026-09-04 | Activo |
| hca_dementia_2027 | "Demencia como tema anual" | hca_affiliated_hco | HSC art. 8 | Vigente desde 2027-01-01 | 2026-09-04 | Activo (futuro — ver abajo) |
| hca_tb | "Evaluación de tuberculosis" | hca_affiliated_hco | HSC art. 8 | — | 2026-09-04 | Activo |
| hca_cpr | "CPR y primeros auxilios" — recomendado/exigido por empleador, NO §1796.43 | las 6 rutas | HSC art. 7 (para descartar la mala atribución) | — | 2026-09-04 | Activo |
| ihss_enrollment | "Inscripción de proveedor IHSS" (SOC 426, orientación, SOC 846, DOJ) | ihss_provider | CDSS orientation process | — | 2026-09-04 | Activo |
| mandated_reporting_home | "Reporte de abuso en entorno domiciliario" (911 primero; WIC §15630) | las 6 rutas | WIC §15630, CDSS APS | — | 2026-09-04 | Activo |
| cna_training | "Programa aprobado CNA" (60h aula + 100h clínicas) | certified_nurse_assistant | CDPH NATP | — | 2026-09-04 | Activo |
| hha_training | "Programa aprobado HHA" (120h base / 40h para CNA activo) | home_health_aide | CDPH HHA | — | 2026-09-04 | Activo |
| internal_completion | "Finalización dentro de la app" — certificado interno, no gubernamental | las 6 rutas | N/A (aviso propio) | — | 2026-09-04 | Activo |

Todos estos claims están implementados en `content/LegalRequirements.kt` y se renderizan en `RequisitosCertificacionScreen` filtrados por la ruta del usuario — no aparecen fuera de su ruta correspondiente.

## Claims futuros

| Claim ID | Entrada en vigor | Comportamiento antes/durante/después | Tests |
|---|---|---|---|
| hca_dementia_2027 | 2027-01-01 | La app muestra el mismo texto con `status = FutureRequirement` y la etiqueta "VIGENCIA FUTURA" en todo momento — **no cambia automáticamente de estado según la fecha del dispositivo** (no hay un motor de reglas con reloj inyectable todavía, a diferencia de lo que pide A-013/`LEARNING_ENGINE_SPEC.md`) | Bloqueado — requeriría un `Clock` inyectable y una prueba que simule antes/después del 2027-01-01; no implementado en este lote |

## Claims retirados

| Texto anterior | Razón | Sustituto | Archivos |
|---|---|---|---|
| "Tu certificación activa" | A-009 — HCA es registro, no certificación | "Tu registro HCA (si aplica a tu ruta)..." | RequisitosCertificacionScreen.kt |
| "Capacitación Acreditada... 110h avanzadas" | A-004 | "Formación (horas internas)... 110 horas de formación y práctica (no acreditadas por el estado)" | RequisitosCertificacionScreen.kt |
| "Obligatorio según § 1796.43" (CPR) | A-003 | "Puede ser recomendado o exigido por tu empleador, programa o puesto — HSC §1796.43 no establece un mandato general" | RecursosSemanaScreen.kt |
| "Meta: Certificación IHSS/CNA" | A-005/A-011 | "Meta: preparación interna para la ruta que elegiste" | Plan90DiasScreen.kt |
| "Evaluación oral para supervisión CDSS" | A-014 | "Práctica oral con rúbrica interna" | SimulacionTurnoScreen.kt |
| "Bitácora Oficial del Turno (DAR)" | A-014 | "Práctica de Bitácora del Turno (DAR)" | SimulacionTurnoScreen.kt |
| "Acciones protocolarias según alcance CDSS Title 22" | A-014 | "Acciones según el plan de cuidado y tu alcance de función" | SimulacionTurnoScreen.kt |
| "Protocolo CDSS California (4 Pasos Clave)" | A-014 | "Protocolo Interno de Práctica (4 Pasos Clave)" | EscenariosFlashcardsScreen.kt |
| "Ver Estándar Regulado CA" | A-014 | "Ver Ficha de Aprendizaje" | EscenariosFlashcardsScreen.kt |
| "Regla California" (sobre contención física) | A-006 (evitar cita legal implícita no verificada) | "Principio de Práctica" | EscenariosFlashcardsScreen.kt |
| "Regla de Oro: CDSS Title 22 (No-Médico)" | A-014 | "Límite de tu Función (No Médico)" | RolePlayBilingueScreen.kt |
| "Audio-Tutor Gemini: Síntesis Dinámica" / "Generado por Gemini AI Audio" | A-028 — no hay backend de IA real conectado | "Orientación Guiada: Audio Conversacional" | AudioLeccionesScreen.kt |
| "Protocolo Oficial y Verificación CDSS" | A-014 | "Práctica guiada y fuentes oficiales verificadas" | AudioLeccionesScreen.kt |
| "CA REGISTRY VALIDADO" (pill) | Implica una validación real que la app no hace | Eliminado | AnalizadorVocacionalScreen.kt |
| "Potencial de ingreso hasta $38/h" | A-017 — sin fuente/condado/fecha/método | Eliminado, reemplazado por aviso de que varía por condado | AnalizadorVocacionalScreen.kt |
| "Rango Salarial CA: $24–$28/hora" | A-017 | Eliminado, mismo aviso | AnalizadorVocacionalScreen.kt |
| "RECOMENDADA PARA TI · MATCH 94%" | El "94%" no tiene metodología declarada | "RECOMENDADA SEGÚN TUS RESPUESTAS" (sin cifra) | AnalizadorVocacionalScreen.kt |
| "100% VERIFICADO CA 2025 · REV. MARZO 2025" | A-042 — fecha obsoleta | "REVISADO 2026-09-04 (VER FECHA POR FUENTE)" | RecursosSemanaScreen.kt |
| "IMPACTO SALARIO +35% Est." | A-017/A-018 | Reemplazado por "LÍNEA APS" (dato real: 1-833-401-0832) | RecursosSemanaScreen.kt |
| "Caso #42" / "Caso #15" sin etiqueta | A-039 | "Caso Ficticio #42", banner `Disclaimers.FictionalCase` en 3 pantallas de escenarios | EscenariosFlashcardsScreen.kt, SimulacionTurnoScreen.kt, RolePlayBilingueScreen.kt |
| "Evaluación de IA — 94% (C2 Care)" / porcentajes de "Criterios Evaluados en Tiempo Real" | A-028 — no hay backend de IA ni modelo de evaluación real conectado; las cifras eran inventadas | Autoevaluación honesta con lista de verificación (`ChecklistRow`) sobre la propia grabación de voz del usuario, sin ninguna puntuación automática | RolePlayBilingueScreen.kt, SimulacionTurnoScreen.kt |
| "18 de 52 episodios descargados" / "420 MB / 1.2 GB" (barra de progreso de descarga) | A-025 — no existía ninguna descarga real; el paquete no incluye audio narrado (ver `DECISIONS.md` ADR-006) | "Los 13 guiones están incluidos en la app: disponibles sin conexión, sin nada que descargar" | BibliotecaAudiosScreen.kt |
| Botón "Escuchar Audio" sin `onClick` (simulaba reproducción de narración inexistente) | A-024/A-026 — no hay archivos de audio narrado en el paquete de origen | `VoiceRecordCard`: el usuario graba y escucha su propia voz practicando el guion (no hay narración pregrabada que reproducir) | AudioLeccionesScreen.kt, RolePlayBilingueScreen.kt, SimulacionTurnoScreen.kt |

## Rutas

Confirmación por ruta (contenido diferenciado, ver `content/LegalRequirements.kt` y `RequisitosCertificacionScreen.kt`):

- **Cuidador familiar**: sin requisitos legales propios en `LegalRequirements` salvo los transversales (CPR recomendado, reporte de abuso, finalización interna) — correcto, no se le atribuye ningún requisito de HCA/IHSS/CNA/HHA.
- **Proveedor IHSS**: `ihss_enrollment` (SOC 426, orientación, SOC 846, DOJ) — no se le atribuye registro HCA ni formación de 5h/TB de HCA afiliado.
- **HCA afiliado a HCO**: los 6 requisitos específicos de HCA (registro, Guardian, 5h inicial, 5h anual, demencia 2027, TB) — separados de IHSS/CNA/HHA.
- **Cuidador privado/directo**: comparte `hca_guardian_application` (registro independiente opcional) pero NO los requisitos de formación/TB exclusivos de HCA afiliado — diferenciación correcta per A-012.
- **HHA**: `hha_training` (120h/40h CDPH) — no mezclado con CNA.
- **CNA**: `cna_training` (60h aula + 100h clínica CDPH) — no mezclado con HHA.

Las seis rutas se seleccionan explícitamente en onboarding (`PathwaySelectionScreen`), sin ruta por defecto (`defaultPathwayId: null` en el JSON de origen, respetado en `Pathway.kt`).

## Revisión de lenguaje

- **Registro vs licencia/certificación**: corregido en las pantallas tocadas (ver tabla de claims retirados). No se hizo una búsqueda exhaustiva del prototipo HTML de la raíz del repo (fuera de alcance — el prompt maestro pide auditar el repositorio Android, no el HTML).
- **Interno vs estatal**: corregido — "certificado interno de finalización" reemplaza cualquier lenguaje de examen/certificación estatal.
- **Recomendado/condicional vs obligatorio**: corregido para CPR/AED específicamente (el hallazgo más citado en la auditoría). TB y horas de HCA siguen presentadas como obligatorias, correctamente, porque sí lo son *para esa ruta específica* (HSC §1796.44/§1796.45).
- **Práctica vs habilidad observada**: no implementado todavía — A-040 (separar conocimiento/práctica/observado) sigue pendiente de Fase 5.
- **Estimación vs garantía**: corregido — se retiraron todas las cifras de salario sin fuente en vez de marcarlas como "estimación", porque no había una fuente/condado/fecha/método verificable para conservarlas con esa etiqueta.

## Fuentes que cambiaron o no pudieron verificarse

| Fuente | Problema | Claim afectado | Mitigación |
|---|---|---|---|
| Todas las URLs de `leginfo.legislature.ca.gov`, `cdss.ca.gov`, `cdph.ca.gov` | No se pudo hacer fetch real de ninguna URL en este entorno (sin acceso HTTPS de propósito general fuera del proxy de la sesión) | Todos los `sourceUrls` en `LegalRequirements.kt`/`OfficialResources.kt` | Se copiaron verbatim del paquete verificado (`verifiedAt: 2026-09-04`) sin re-verificar el contenido en vivo; revisar antes de lanzamiento |

## Aprobación

Este contenido fue trasladado del paquete de auditoría al código por una sesión de Claude Code, sin revisión humana posterior todavía. **No constituye asesoría legal.** Antes de cualquier lanzamiento público, una persona con conocimiento del marco regulatorio de California (CDSS/CDPH/WIC) debe revisar `content/LegalRequirements.kt`, `content/Pathway.kt` y `content/Disclaimers.kt` línea por línea contra las fuentes primarias enlazadas.
