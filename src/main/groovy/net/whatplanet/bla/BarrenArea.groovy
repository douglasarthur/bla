package net.whatplanet.bla

import net.whatplanet.bla.exceptions.InvalidInputStringException

class BarrenArea {
  Node southWest
  Node northEast

  BarrenArea() {
  }

  BarrenArea(int southWestX, int southWestY, int northEastX, int northEastY) {
    this.southWest = new Node(x: southWestX, y: southWestY)
    this.northEast = new Node(x: northEastX, y: northEastY)
  }

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
