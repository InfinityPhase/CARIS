# CARIS

### 604's Auto-Responding Discord Chat Bot

#### Developed by [QuiPS](https://quips.info)

Uses framework: https://github.com/austinv11/Discord4J

## How to run CARIS:

### Quick Method

If on a \*nix system, it is likely that the run.sh script will suit your needs.
To use it, pass the the desired token as the first parameter.
```
./run.sh TOKEN
```
Alternatively, it is possible to store the token inside of the script itself, at the line: `declare TOKEN=""`
This is not reccomended, as it can lead to the easy mistake of committing the token value by accident.

### Using Gradle

A version of Gradle has been included in the CARIS repository.
It can be run with `./gradlew`, or `./gradlew.bat`, depending on whether the system is \*nix based or Windows based.

It is also possible to use a system install of gradle, if desired.
Both methods of running Gradle are equivalent.

To install dependencies for usage with Eclipse (to avoid import errors), use:
```
gradle eclipse
```
This will download the required dependencies and configure your project file to properly point to the dependencies.

To build a project, it is reccomended (but not required) to also clean the build direcory of the previous build.
To both clean and then build the project:
```
gradle clean build
```
This will create a zipped file under `build/distributions`, one of which is a `.tar`, and one a `.zip`.
The unpacked compressed folder will contain two subdirectories.
One is `lib`, which contains all of the required jar files for the project.
The other, `bin`, contains two scripts: `CARIS` and `CARIS.bat`, which are used to start CARIS on \*nix and Windows respectively.

When starting CARIS, it is required to pass the bot token as the first argument.
For example:
```
./CARIS TOKEN
```

A useful one-liner that does the same as `run.sh` is:
```
gradle clean build && cd build/distributions && tar -x -f CARIS.tar && cd CARIS/bin && ./CARIS TOKEN
```
