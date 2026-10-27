# Directorio de Usuarios

Aplicación Android nativa en Kotlin para consultar usuarios de JSONPlaceholder, ver sus datos y publicaciones, y guardar favoritos localmente.

## Tecnologías

- Kotlin + Jetpack Compose + Material 3
- MVVM: UI → ViewModel → Repository → Retrofit/Room
- Retrofit 2.11 con Gson y OkHttp
- Room 2.6.1 para favoritos persistentes
- Android API 36, `minSdk 26`

## Ejecutar

1. Abrir esta carpeta en Android Studio.
2. Usar el JDK embebido de Android Studio (JDK 21).
3. Sincronizar Gradle y ejecutar la configuración `app` en un emulador o dispositivo.

Desde PowerShell, si Gradle está disponible:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
gradle.bat assembleDebug
```

El APK generado queda en `app/build/outputs/apk/debug/app-debug.apk`.

## Funcionalidad

- Lista usuarios desde `https://jsonplaceholder.typicode.com/users`.
- Detalle con dirección, contacto, empresa y publicaciones desde `/posts?userId={id}`.
- Favoritos persistidos en `directory.db` mediante Room y visibles sin conexión.
- Estados de carga, éxito, error HTTP y ausencia de conexión con reintento.
- Permiso `INTERNET` declarado en el manifest.

## Evidencias

Las capturas obtenidas durante la prueba están en [evidencias/](./evidencias/). El informe de prueba indica cuáles son válidas y qué escenarios no pudieron completarse por inestabilidad del AVD.

## Publicación

El repositorio GitHub no se ha publicado porque esa acción requiere autorización explícita del propietario.
