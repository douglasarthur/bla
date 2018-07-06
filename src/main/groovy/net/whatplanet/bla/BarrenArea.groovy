package net.whatplanet.bla

import net.whatplanet.bla.exceptions.InvalidInputStringException

/**
 * Defines a barren area as parsed from an input string.
 */
class BarrenArea {
  Node southWest
  Node northEast

  static BarrenArea buildBarrenArea(String input, int w, int h) throws InvalidInputStringException {
    List<String> tokens = input.tokenize()
    if (tokens.size() != 4 || tokens.findAll { it.isInteger() }.size() != 4) {
      throw new InvalidInputStringException("Input string ${input} is not valid.")
    }

    List<Integer> coordinates = tokens.collect { Integer.valueOf(it) }
    if (coordinates.any { it < 0 } ||
        coordinates[0] >= w ||
        coordinates[2] >= w ||
        coordinates[1] >= h ||
        coordinates[3] >= h ||
        coordinates[2] < coordinates[0] ||
        coordinates[3] < coordinates[1]
    ) {
      throw new InvalidInputStringException("Input string ${input} is out of bounds.")
    }

    return new BarrenArea(
        southWest: new Node(x: coordinates[0], y: coordinates[1], fertile: false),
        northEast: new Node(x: coordinates[2], y: coordinates[3], fertile: false)
    )
  }
}
