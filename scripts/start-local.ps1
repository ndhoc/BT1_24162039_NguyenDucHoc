param(
    [string]$TomcatHome = 'C:\apache-tomcat-11.0.27',
    [string]$JavaHome = 'C:\Program Files\Java\jdk-21.0.12.1',
    [int]$Port = 8081
)
$ErrorActionPreference = 'Stop'
$projectDir = Split-Path -Parent $PSScriptRoot
$baseDir = Join-Path $projectDir 'target\tomcat'
if (!(Test-Path -LiteralPath (Join-Path $JavaHome 'bin\java.exe'))) { throw 'Không tìm thấy JDK 21. Truyền -JavaHome đúng đường dẫn.' }
if (!(Test-Path -LiteralPath (Join-Path $TomcatHome 'lib\catalina.jar'))) { throw 'Không tìm thấy Tomcat 11. Truyền -TomcatHome đúng đường dẫn.' }
$warFile = Join-Path $projectDir 'target\24162039_NguyenDucHoc.war'
if (!(Test-Path -LiteralPath $warFile)) { throw 'Chạy mvn clean package trước.' }
foreach ($dir in @('conf','logs','temp','webapps','work','uploads\category')) {
    New-Item -ItemType Directory -Path (Join-Path $baseDir $dir) -Force | Out-Null
}
Copy-Item -Path (Join-Path $TomcatHome 'conf\*') -Destination (Join-Path $baseDir 'conf') -Force
Copy-Item -LiteralPath $warFile -Destination (Join-Path $baseDir 'webapps\24162039_NguyenDucHoc.war') -Force
@"
<?xml version="1.0" encoding="UTF-8"?>
<Server port="-1">
  <Service name="Catalina">
    <Connector address="127.0.0.1" port="$Port" protocol="HTTP/1.1" connectionTimeout="20000" URIEncoding="UTF-8" />
    <Engine name="Catalina" defaultHost="localhost">
      <Host name="localhost" appBase="webapps" unpackWARs="true" autoDeploy="false" />
    </Engine>
  </Service>
</Server>
"@ | Set-Content -LiteralPath (Join-Path $baseDir 'conf\server.xml') -Encoding utf8
Write-Host "Mở http://localhost:$Port/24162039_NguyenDucHoc/"
& (Join-Path $JavaHome 'bin\java.exe') "-Dcatalina.home=$TomcatHome" "-Dcatalina.base=$baseDir" "-Djava.io.tmpdir=$(Join-Path $baseDir 'temp')" '-Dfile.encoding=UTF-8' -cp "$(Join-Path $TomcatHome 'bin\bootstrap.jar');$(Join-Path $TomcatHome 'bin\tomcat-juli.jar')" org.apache.catalina.startup.Bootstrap start
