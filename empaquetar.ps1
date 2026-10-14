<#
.SYNOPSIS
  Compila Inventia y genera un unico .jar ejecutable (sin Maven ni NetBeans).

.EXAMPLE
  .\empaquetar.ps1
  # Resultado: dist\Inventia.jar  ->  java -jar dist\Inventia.jar

El .jar lleva dentro MySQL, FlatLaf y jcalendar, igual que el que genera Maven con el plugin Shade.
Solo hace falta Java 21 o superior para ejecutarlo.
#>
$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem

$raiz = $PSScriptRoot
$libs = Join-Path $raiz ".dev-libs"
$fuentes = Join-Path $raiz "Inventia-main\src\main\java"
$jcalendar = Join-Path $raiz "Inventia-main\lib\unknown\binary\jcalendar-1.4\SNAPSHOT\jcalendar-1.4-SNAPSHOT.jar"
$trabajo = Join-Path $raiz ".dev-build"
$dist = Join-Path $raiz "dist"
$destino = Join-Path $dist "Inventia.jar"

$jdk = "C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.3\jbr\bin"
if (-not (Test-Path "$jdk\javac.exe")) {
    $javac = Get-Command javac -ErrorAction SilentlyContinue
    if (-not $javac) { throw "No se encontro javac. Instala un JDK 21 o superior." }
    $jdk = Split-Path $javac.Source
}

# Las librerias se descargan una vez (las mismas versiones del pom.xml)
New-Item -ItemType Directory -Force $libs | Out-Null
$ProgressPreference = "SilentlyContinue"
$descargas = @{
    "flatlaf-3.5.4.jar" = "https://repo1.maven.org/maven2/com/formdev/flatlaf/3.5.4/flatlaf-3.5.4.jar"
    "mysql-connector-java-8.0.26.jar" = "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.26/mysql-connector-java-8.0.26.jar"
}
foreach ($nombre in $descargas.Keys) {
    $ruta = Join-Path $libs $nombre
    if (-not (Test-Path $ruta)) { Invoke-WebRequest $descargas[$nombre] -OutFile $ruta }
}
$dependencias = @((Get-ChildItem $libs -Filter *.jar | ForEach-Object FullName) + $jcalendar)

# 1) Compilar solo la aplicacion (sin las herramientas de dev\)
if (Test-Path $trabajo) { Remove-Item $trabajo -Recurse -Force }
$clases = Join-Path $trabajo "clases"
New-Item -ItemType Directory -Force $clases | Out-Null
$archivos = @(Get-ChildItem $fuentes -Recurse -Filter *.java | ForEach-Object FullName)
& "$jdk\javac.exe" --release 21 -encoding UTF-8 -nowarn -d $clases -cp ($dependencias -join ";") $archivos
if ($LASTEXITCODE -ne 0) { throw "Fallo la compilacion." }

# 2) Reunir clases y librerias en una sola carpeta
$todo = Join-Path $trabajo "todo"
New-Item -ItemType Directory -Force $todo | Out-Null
Copy-Item "$clases\*" $todo -Recurse -Force
foreach ($dep in $dependencias) {
    $zip = [System.IO.Compression.ZipFile]::OpenRead($dep)
    try {
        foreach ($entrada in $zip.Entries) {
            if ($entrada.FullName.EndsWith("/")) { continue }
            $nombre = $entrada.FullName
            # firmas y descriptores de modulo no sirven en un jar combinado
            if ($nombre -match '^META-INF/.*\.(SF|RSA|DSA|EC)$' -or $nombre -eq "module-info.class" -or $nombre -eq "META-INF/MANIFEST.MF") { continue }
            $salida = Join-Path $todo ($nombre -replace '/', '\')
            if (Test-Path $salida) { continue }
            New-Item -ItemType Directory -Force (Split-Path $salida) | Out-Null
            [System.IO.Compression.ZipFileExtensions]::ExtractToFile($entrada, $salida)
        }
    } finally { $zip.Dispose() }
}

# 3) Manifiesto con la clase principal y crear el .jar
$meta = Join-Path $todo "META-INF"
New-Item -ItemType Directory -Force $meta | Out-Null
[System.IO.File]::WriteAllText((Join-Path $meta "MANIFEST.MF"), "Manifest-Version: 1.0`r`nMain-Class: Vistas.Mainn`r`nCreated-By: empaquetar.ps1`r`n`r`n", (New-Object System.Text.UTF8Encoding($false)))

New-Item -ItemType Directory -Force $dist | Out-Null
if (Test-Path $destino) { Remove-Item $destino -Force }
$jar = [System.IO.Compression.ZipFile]::Open($destino, [System.IO.Compression.ZipArchiveMode]::Create)
try {
    # el manifiesto va primero
    [System.IO.Compression.ZipFileExtensions]::CreateEntryFromFile($jar, (Join-Path $meta "MANIFEST.MF"), "META-INF/MANIFEST.MF") | Out-Null
    $baseLen = $todo.Length + 1
    foreach ($f in Get-ChildItem $todo -Recurse -File) {
        $nombre = $f.FullName.Substring($baseLen) -replace '\\', '/'
        if ($nombre -eq "META-INF/MANIFEST.MF") { continue }
        [System.IO.Compression.ZipFileExtensions]::CreateEntryFromFile($jar, $f.FullName, $nombre) | Out-Null
    }
} finally { $jar.Dispose() }

Remove-Item $trabajo -Recurse -Force
$mb = [math]::Round((Get-Item $destino).Length / 1MB, 1)
Write-Host "Listo: $destino ($mb MB)"
Write-Host "Ejecutar con: java -jar `"$destino`"   (requiere Java 21 o superior)"
