package net.whatplanet.bla

import spock.lang.Specification
import spock.lang.Unroll

class FarmSpec extends Specification {

  @Unroll
  void 'isBarren correctly identifies barren area using coordinate #x, #y'() {
    given: 'a farm with barren areas'
    Farm farm = new Farm(400, 600, [new BarrenArea(100, 100, 200, 200), new BarrenArea(200, 200, 300, 300)])

    when:
    boolean result = farm.isBarren(x, y)

    then:
    result == expected

    where:
    x   | y   | expected
    100 | 100 | true
    200 | 200 | true
    300 | 300 | true
    150 | 150 | true
    250 | 250 | true
    99  | 150 | false
    200 | 301 | false
    0   | 0   | false
  }

  @Unroll
  void 'findUnaccountedFertileNeighbors with valid input: #x, #y'() {
    given: 'a farm with barren areas'
    Farm farm = new Farm(600, 400, [new BarrenArea(100, 100, 200, 200), new BarrenArea(200, 200, 300, 300)])
    farm.initGrid()

    when:
    List<Node> results = farm.findUnaccountedFertileNeighbors(new Node(x: x, y: y))

    then:
    results == expected.collect { new Node(x: it[0], y: it[1]) }

    where:
    x   | y   | expected
    0   | 0   | [[0, 1], [1, 0]]
    0   | 399 | [[0, 398], [1, 399]]
    599 | 0   | [[599, 1], [598, 0]]
    599 | 399 | [[599, 398], [598, 399]]
    0   | 1   | [[0, 2], [0, 0], [1, 1]]
    1   | 0   | [[1, 1], [2, 0], [0, 0]]
    1   | 399 | [[1, 398], [2, 399], [0, 399]]
    0   | 398 | [[0, 399], [0, 397], [1, 398]]
    599 | 1   | [[599, 2], [599, 0], [598, 1]]
    598 | 0   | [[598, 1], [599, 0], [597, 0]]
    598 | 399 | [[598, 398], [599, 399], [597, 399]]
    2   | 2   | [[2, 3], [2, 1], [3, 2], [1, 2]]
    2   | 397 | [[2, 398], [2, 396], [3, 397], [1, 397]]
    150 | 150 | []
    250 | 250 | []
    99  | 99  | [[99, 100], [99, 98], [100, 99], [98, 99]]
    100 | 99  | [[100, 98], [101, 99], [99, 99]]
    100 | 201 | [[100, 202], [101, 201], [99, 201]]
    201 | 201 | []
    99  | 201 | [[99, 202], [99, 200], [100, 201], [98, 201]]
    201 | 99  | [[201, 100], [201, 98], [202, 99], [200, 99]]
    201 | 199 | [[201, 198], [202, 199]]
    199 | 201 | [[199, 202], [198, 201]]
  }

  @Unroll
  void 'getFertileNeighbors with invalid input: #x, #y'() {
    given: 'a farm with barren areas'
    Farm farm = new Farm(400, 600, [new BarrenArea(100, 100, 200, 200), new BarrenArea(200, 200, 300, 300)])

    when:
    farm.findUnaccountedFertileNeighbors(new Node(x: x, y: y))

    then:
    thrown(AssertionError)

    where:
    x   | y
    -1  | 0
    0   | -1
    400 | 0
    0   | 600
    -1  | 600
  }

  void 'extendFertileAreaFrom with a farm with barren areas not segmenting fertile area and not overlapping'() {
    given:
    Farm farm = new Farm(400, 600, [new BarrenArea(100, 100, 199, 199), new BarrenArea(300, 100, 399, 199), new BarrenArea(201, 150, 298, 150)])
    farm.initGrid()
    int expectedSize = 219902

    when:
    SortedSet<Node> results = farm.extendFertileAreaFrom(new TreeSet([farm.grid[0][0]]))

    then:
    results.size() == expectedSize
  }

  void 'extendFertileAreaFrom with a farm with barren areas not segmenting fertile area and overlapping'() {
    given:
    Farm farm = new Farm(600, 400, [new BarrenArea(100, 100, 199, 199), new BarrenArea(300, 100, 399, 199), new BarrenArea(150, 150, 350, 150)])
    farm.initGrid()
    int expectedSize = 219900

    when:
    SortedSet<Node> results = farm.extendFertileAreaFrom(new TreeSet([farm.grid[0][0]]))

    then:
    results.size() == expectedSize
  }

  void 'extendFertileAreaFrom with a farm with barren areas segmenting fertile area and not overlapping'() {
    given:
    Farm farm = new Farm(600, 400, [new BarrenArea(100, 0, 100, 399), new BarrenArea(0, 200, 99, 200)])
    farm.initGrid()
    int expectedSize = 20000

    when:
    SortedSet<Node> results = farm.extendFertileAreaFrom(new TreeSet([farm.grid[0][0]]))

    then:
    results.size() == expectedSize
  }

  @Unroll
  void 'getNextFertileUnaccountedNode #desc'() {
    given:
    Farm farm = new Farm(600, 400, [new BarrenArea(0, 0, 99, 99), new BarrenArea(0, 200, 599, 200), new BarrenArea(599, 399, 599, 399)])
    farm.initGrid()
    farm.grid[199][599].accounted = true
    farm.grid[399][598].accounted = true

    when:
    Node node = farm.getNextFertileUnaccountedNode(farm.grid[startY][startX])

    then:
    node?.x == expectedX
    node?.y == expectedY

    where:
    startX | startY | expectedX | expectedY | desc
    0      | 0      | 100       | 0         | 'start at beginning of barren area bottom edge'
    50     | 0      | 100       | 0         | 'start mid-width of barren area bottom edge'
    0      | 2      | 100       | 2         | 'start at beginning of barren area mid-height'
    50     | 2      | 100       | 2         | 'start at mid-width of barren area mid-height'
    0      | 100    | 0         | 100       | 'start at beginning on clear row just above barren area'
    50     | 100    | 50        | 100       | 'start mid-width on clear row just above barren area'
    599    | 199    | 0         | 201       | 'start in accounted cell end of row just under a completely barren row'
    598    | 399    | null      | null      | 'start in accounted cell with nothing but barren areas ahead of it'
  }

  @Unroll
  void 'getFertileAreas'() {
    given:
    List<BarrenArea> barrenAreas = inputStrings.collect { BarrenArea.buildBarrenArea(it, width, height) }
    Farm farm = new Farm(width, height, barrenAreas)

    when:
    List<Integer> fertileAreas = farm.getFertileAreas()

    then:
    fertileAreas == expected

    where:
    width | height | inputStrings                                                             | expected
    400   | 600    | ['0 292 399 307']                                                        | [116800, 116800]
    400   | 600    | ['48 192 351 207', '48 392 351 407', '120 52 135 547', '260 52 275 547'] | [22816, 192608]
    500   | 500    | ['0 0 0 499', '0 499 499 499', ' 499 0 499 499', '0 0 499 0']            | [248004]
    500   | 500    | ['1 1 1 498', '1 498 498 498', ' 498 1 498 498', '1 1 498 1']            | [1996, 246016]
    400   | 600    | ['200 0 200 299', '200 301 200 599']                                     | [239401]
  }
}
