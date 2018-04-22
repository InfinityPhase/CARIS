# This script was written with the purpose of building the caris project and
# running her with the provided token. This can be hardcoded, but is not
# recommended. This script may require that the execution policy of powershell
# be altered with: set-executionpolicy remotesigned

<#
.SYNOPSIS
Starts the CARIS bot, with the provided token when run from the root directory.
.DESCRIPTION
Allows for easily testing caris, when still in development. It is required that
this script be run from the root directory of the project. This means that the
src folder should be present in this directory.
.PARAMETER TOKEN
The token that CARIS needs to connect to Discord.
.EXAMPLE
run -TOKEN tokenstring
.LINK
github.com/InfinityPhase/CARIS
#>

# Parameter declaration
param (
  [Parameter(Mandatory=$False)] # Perhaps this should be mandatory...
  [string]$TOKEN = ""
)

# Imports
Add-Type -assembly "system.io.compression.filesystem"

# Variable declarations
$GRADLE = "gradle"
$PROJECTNAME = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
$COMPRESSED_EXTENSION = ".zip"
$EXECUTABLE_EXTENSION = ".bat"
$ROOT_PATH = Convert-Path .

if( $TOKEN == "" ) {
  # Not exit, as that will close the shell. Stops execution.
  Write-Host "No token was found, or given as a parameter. Please pass a token to the script."
  return
}

# Check if gradle is not in the path
if( ( Get-Command "gradle" -ErrorAction SilentlyContinue ) -eq $null ) {
   if( Test-Path gradlew.bat ) {
     # Use gradlew.bat instead of gradle
     $GRADLE = $ROOT_PATH + "gradlew" + $EXECUTABLE_EXTENSION
   } else {
     Write-Host "No sutiable Gradle install was found. Either install Gradle globally, or use a gradlew.bat executable."
     return
   }
}

# Perhaps use cmd.exe /c "$GRADLE$EXECUTABLE_EXTENSION" instead?
# FYI, Invoke-Expression is the same as placing a $ sign infront of the string.
Invoke-Expression "/" + $GRADLE clean build
Set-Location -Path $ROOT_PATH + "build/distributions" # Same as cd'ing

[io.compression.zipfile]::ExtractToDirectory( ( $ROOT_PATH + "build/distributions/" + $PROJECTNAME + $COMPRESSED_EXTENSION ), ( $ROOT_PATH + "build/distributions/" + $PROJECTNAME ) )

Set-Location -Path $ROOT_PATH + "build/distributions" + $PROJECTNAME + "/bin"
.\$PROJECTNAME + $EXECUTABLE_EXTENSION $TOKEN
