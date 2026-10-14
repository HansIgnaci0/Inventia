<#
.SYNOPSIS
  Compila Inventia y abre una vista, sin Maven y sin NetBeans.

.EXAMPLE
  .\ejecutar.ps1                 # abre Mainn (menu principal)
  .\ejecutar.ps1 Login           # abre otra vista
  .\ejecutar.ps1 -Captura        # guarda un PNG de cada vista en capturas\ (sin base de datos)
  .\ejecutar.ps1 -Avisos         # muestra los avisos de vidrio en pantalla real y guarda PNG

Sin acceso a la base de datos, los botones y pantallas que consultan datos
muestran un error de conexion. Es lo esperado en este equipo.
#>
param(
    [string]$Vista = "Mainn",
    [switch]$Captura,
    [switch]$Avisos
)

$ErrorActionPreference = "Stop"
$raiz = $PSScriptRoot
$libs = Join-Path $raiz ".dev-libs"
$salida = Join-Path $raiz ".dev-out"
$fuentes = Join-Path $raiz "Inventia-main\src\main\java"
$jcalendar = Join-Path $raiz "Inventia-main\lib\unknown\binary\jcalendar-1.4\SNAPSHOT\jcalendar-1.4-SNAPSHOT.jar"

$jdk = "C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.3\jbr\bin"
if (-not (Test-Path "$jdk\javac.exe")) {
    $javac = Get-Command javac -ErrorAction SilentlyContinue
    if (-not $javac) { throw "No se encontro javac. Instala un JDK 21 o superior." }
    $jdk = Split-Path $javac.Source
}

New-Item -ItemType Directory -Force $libs | Out-Null
$descargas = @{
    "flatlaf-3.5.4.jar" = "https://repo1.maven.org/maven2/com/formdev/flatlaf/3.5.4/flatlaf-3.5.4.jar"
    "mysql-connector-java-8.0.26.jar" = "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.26/mysql-connector-java-8.0.26.jar"
}
$ProgressPreference = "SilentlyContinue"
foreach ($nombre in $descargas.Keys) {
    $destino = Join-Path $libs $nombre
    if (-not (Test-Path $destino)) { Invoke-WebRequest $descargas[$nombre] -OutFile $destino }
}

$cp = (Get-ChildItem $libs -Filter *.jar | ForEach-Object FullName) + $jcalendar -join ";"

if (Test-Path $salida) { Remove-Item $salida -Recurse -Force }
New-Item -ItemType Directory -Force $salida | Out-Null

$archivos = @(Get-ChildItem $fuentes -Recurse -Filter *.java | ForEach-Object FullName)
$archivos += Join-Path $raiz "dev\Captura.java"
$archivos += Join-Path $raiz "dev\CapturaAviso.java"
& "$jdk\javac.exe" --release 21 -encoding UTF-8 -nowarn -d $salida -cp $cp $archivos
if ($LASTEXITCODE -ne 0) { throw "Fallo la compilacion." }

$java = "$jdk\java.exe"
$cpEjecucion = "$salida;$cp"
if ($Avisos) {
    & $java --enable-native-access=ALL-UNNAMED -cp $cpEjecucion CapturaAviso (Join-Path $raiz "capturas")
} elseif ($Captura) {
    & $java --enable-native-access=ALL-UNNAMED -cp $cpEjecucion Captura (Join-Path $raiz "capturas")
} else {
    & $java --enable-native-access=ALL-UNNAMED -cp $cpEjecucion "Vistas.$Vista"
}
