# Barren Land Analysis (bla)

This tool was completed as a case study. It takes a width and height in meters which define a rectangular farm (defaulting to 300m x 600m) and a set of input strings (details below) that define rectangular areas of the farm that are barren. An input string is is a series of four integers separated by spaces. The first two integers in an input string are the x, y coordinates of the Southwest (lower left) corner of the barren area. The last two integers in an input string are the x, y coordinates of the Northeast (upper right) corner of the barren area. Barren area inputs must be in bounds for the given width and height (i.e. > 0 && < the significant dimension). Given this input, the application will output a list of integers giving the total area of a contiguous region of fertile farm sorted from smallest to largest.

## System Requirements

Barren Land Analysis is written in Groovy and requires Java to run. It has been tested using Java 1.8.0_51 but should work with any 1.7+.

## Building and Running Tests

Barren Land Analysis uses Gradle for builds and dependency management. After cloning the repository, you should be able to build the code and run the tests in a shell with:

```
./gradlew clean build
```

## Runing the application

The application provides a simple CLI. The build will create a fat jar containing all dependencies (except Java) at

```
build/libs/bla-0.1.0.jar
```

This can be executed using the java -jar option. For example, display usage with:

```
java -jar build/libs/bla-0.1.0.jar --help
```

Additional options are:

```
 -b,--barren <arg>   Any number of barren area input strings separated by
                     commas. Invalid if -f is also provided.
 -f,--file <arg>     A file name containing barren area  input strings,
                     one per line. Invalid if -b is also provided.
 -h,--help           Display usage.
 -x,--width <arg>    The width (east-west) of the farm in meters (defaults
                     to 600).
 -y,--height <arg>   The height (north-south) of the farm in meters
                     (defaults to 400).
```

## Input Strings
Barren Area input strings can be provided in three ways:

1. Using the -b (--barren) option with one value containing all input strings separated by commas (java -jar build/libs/bla-0.1.0.jar -b "1 1 100 100, 200 200 300 300") or by using multiple options with one input string per option (java -jar build/libs/bla-0.1.0.jar -b "1 1 100 100" -b "200 200 300 300")

2. Using the -f (--file) option with a name for a file containing a list of unquoted input strings, one per line (java -jar build/libs/bla-0.1.0.jar -f ./samples/ba.txt)

3. If neither -b nor -f is provided, you will be asked to enter input strings at the command line. Enter each input string followed by a return at each prompt. Return without entering anything when you are done.



