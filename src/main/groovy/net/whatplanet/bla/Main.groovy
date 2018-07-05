package net.whatplanet.bla

import net.whatplanet.bla.exceptions.InvalidInputStringException
import net.whatplanet.bla.exceptions.InvalidOptionsException

class Main {
  static void main(String[] args) {
    CLI cli = new CLI(args)
    if (cli.helpRequested()) {
      cli.printUsage()
    } else {
      try {
        Farm farm = new Farm(cli.getWidth(), cli.getHeight(), cli.getBarrenAreas(true))
        List<Integer> fertileAreas = farm.getFertileAreas()
        println(fertileAreas.join(' '))
      } catch (InvalidOptionsException invalidOptionsException) {
        println("Invalid options: ${invalidOptionsException.message}")
        println(cli.getUsage())
      } catch (InvalidInputStringException invalidInputStringException) {
        println("Invalid input: ${invalidInputStringException.message}")
        println(cli.getUsage())
      }
    }
  }
}
