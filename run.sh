#! /bin/bash
# This script was written with the purpose of building the caris project and
# running her with the provided token. This can be hardcoded, but is not
# recommended.

# Variables
declare GRADLE="gradle"
declare PROJECTNAME="CARIS"
declare EXTENSION=".tar"
declare TOKEN=""

# Check if gradle is avalible
if ! hash gradle; then
  GRADLE=gradlew
  if ! [ -f "gradlew" ]; then
    echo "There is no avalible Gradle installation.\nPlease install Gradle and try again."
    exit 1;
  fi
fi

# Check the status of the token
if [[ $TOKEN == "" ]]; then
  if [ -z $1 ]; then
    echo "A token is required. Either pass it as an argument, or set it as a constant in the script."
    exit 1;
  fi
  TOKEN=$1
fi

$GRADLE clean build
cd build/distributions || exit 1;

if hash aunpack; then
  aunpack $PROJECTNAME$EXTENSION
else
  if hash tar; then
    tar -x -f $PROJECTNAME$EXTENSION
  else
    echo "A program for extracting files is required.\nPlease install either aunpack or tar to continue."
    exit 1;
  fi
fi

cd $PROJECTNAME/bin || exit 1;
./$PROJECTNAME $TOKEN
