# Reporte de validación

Fecha: 2026-10-26
Emulador: `Medium_Phone_API_36.1`

## Aprobadas

| Prueba | Resultado | Evidencia |
|---|---|---|
| Compilación `assembleDebug` | APROBADA | APK generado en `app-debug.apk` |
| Instalación del APK | APROBADA | Instalación ADB reportada como `Success` |
| Apertura de la aplicación | APROBADA tras corregir namespace | `01_exito.png` |
| Consulta y presentación de usuarios | APROBADA | `01_exito.png` |
| Selección de usuario | APROBADA | `02_detalle.png` |
| Consulta de publicaciones del usuario seleccionado | APROBADA | `02_detalle.png` |

## No ejecutadas o bloqueadas

| Prueba | Estado | Motivo |
|---|---|---|
| Captura real del indicador de carga | NO EJECUTADA | La carga fue demasiado rápida y el AVD quedó inestable al intentar repetirla |
| Error/sin conexión con reintento | NO CERTIFICADA | El AVD quedó bloqueado por `Application Not Responding: com.android.systemui` |
| Favoritos de dos usuarios | NO CERTIFICADA | La automatización quedó bloqueada por el mismo diálogo del sistema |
| Persistencia tras cerrar y abrir | NO CERTIFICADA | Dependía de completar la prueba de favoritos |
| Favoritos sin Internet | NO CERTIFICADA | Dependía de completar la prueba de favoritos |
| Retirar favoritos sin conexión | NO CERTIFICADA | Dependía de completar la prueba de favoritos |

## Hallazgo corregido

La primera instalación cerraba porque `applicationId/namespace` no coincidía con el paquete Kotlin. Se corrigió a `com.example.directoriosusuarios`, se recompiló y la aplicación abrió correctamente.

## Estado de evidencias

- `01_exito.png`: captura real de usuarios, correos y empresas.
- `02_detalle.png`: captura real de datos completos y publicaciones.
- `03_carga.png`: pendiente; no se fabrica.
- `04_error_sin_conexion.png`: pendiente; no se fabrica.
- `05_favoritos_offline.png`: pendiente; no se fabrica.
- Las capturas antiguas con `System UI isn't responding` no se consideran evidencia válida.

## Documentación y publicación

No se generó un Word/PDF definitivo porque no hay conversor DOCX/PDF disponible en el entorno y el documento base contiene una plantilla que no debe presentarse como final sin completar. No existe URL GitHub verificada; publicar requiere autorización explícita.
