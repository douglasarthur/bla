package net.whatplanet.bla

import static org.apache.commons.cli.Option.UNLIMITED_VALUES

import net.whatplanet.bla.exceptions.InvalidOptionsException

/**
 * Handles command line options and user input.
 */
class CLI {
  private static final Integer DEFAULT_HEIGHT = 600
  private static final Integer DEFAULT_WIDTH = 400

  CliBuilder cli
  OptionAccessor options
  String header = '''
This tool takes a width and height in meters which define a rectangular farm (defaulting to 400m x 600m) and a set of input strings defining
rectangular areas of the farm that are barren. An input string is is a series of four integers separated by spaces. The first two integers in
an input string are the x, y coordinates of the Southwest (lower left) corner of the barren area. The last two integers in an input string are
the x, y coordinates of the Northeast (upper right) corner of the barren area. Barren area inputs must be in bounds for the given width
and height (i.e. > 0 && < the significant dimension). Given this input, the application will output a list of integers giving the total areas of
contiguous regions of fertile land sorted from smallest to largest.

Barren Area input strings can be provided in three ways:

1. Using the -b (--barren) option with one value containing all input strings separated by commas (-b "1 1 100 100, 200 200 300 300")
or by using multiple options with one input string per option (-b "1 1 100 100" -b "200 200 300 300")

2. Using the -f (--file) option with a name for a file containing a list of unquoted input strings, one per line (-f /Users/z001mmt/ba.txt)

3. If neither -b nor -f is provided, you will be asked to enter input strings at the command line. Enter each input string followed by a return at
each prompt. Return without entering anything when you are done.

Options:
'''
  CLI(String[] args) {
    cli = new CliBuilder(usage: 'java -jar <jar_name> [options]', header: header)
    cli.with {
      b longOpt: 'barren', args: UNLIMITED_VALUES, required: false, valueSeparator: ',' as char,
          'Any number of barren area input strings separated by commas. Invalid if -f is also provided.'
      f longOpt: 'file', args: 1, required: false, 'A file name containing barren area  input strings, one per line.' +
          ' Invalid if -b is also provided.'
      x longOpt: 'width', args: 1, required: false, 'The width (east-west) of the farm in meters (defaults to 600).'
      y longOpt: 'height', args: 1, required: false, 'The height (north-south) of the farm in meters (defaults to 400).'
      h longOpt: 'help', required: false, 'Display usage.'
    }
    options = validateOptions(cli.parse(args))
  }

  boolean helpRequested() {
    return options.h
  }

  void printUsage() {
    cli.usage()
  }

  Integer getWidth() {
    return options?.x ? options.x as Integer : DEFAULT_WIDTH
  }

  Integer getHeight() {
    return options?.y ? options.y as Integer : DEFAULT_HEIGHT
  }

  List<BarrenArea> getBarrenAreas(boolean getFromUserIfNone = false) {
    List<BarrenArea> barrenAreas = []
    if (options.b) {
      barrenAreas = options.bs.collect { return BarrenArea.buildBarrenArea(it, width, height) }
    } else if (options.f) {
      File file = new File(options.f)
      file.eachLine { barrenAreas << BarrenArea.buildBarrenArea(it, width, height) }
    } else if (getFromUserIfNone) {
      barrenAreas = buildBarrenAreasFromUserInput()
    }
    return barrenAreas
  }

  private List<BarrenArea> buildBarrenAreasFromUserInput() {
    Scanner scanner = new Scanner(System.in)
    List<BarrenArea> barrenAreas = []
    println('Enter an input string.')
    String input = scanner.nextLine()
    while (input) {
      barrenAreas << BarrenArea.buildBarrenArea(input, width, height)
      println("You have entered ${barrenAreas.size()} input strings. Enter the next input string, or just hit return if you are done.")
      input = scanner.nextLine()
    }
    return barrenAreas
  }

  private static OptionAccessor validateOptions(OptionAccessor options) throws InvalidOptionsException {
    String error
    if (options.b && options.f) {
      error = 'Only -b or -f should be provided, not both.'
    }
    if (options.f) {
      File file = new File(options.f)
      if (!file.canRead()) {
        error = "${options.f} is not the name of a readable file."
      }
    }
    if (options.x && !options.x.isInteger()) {
      error = 'The value of -x must be an integer.'
    }
    if (options.y && !options.y.isInteger()) {
      error = 'The value of -y must be an integer.'
    }

    if (error) {
      throw new InvalidOptionsException(error)
    }
    return options
  }

}
