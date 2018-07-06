package net.whatplanet.bla

import net.whatplanet.bla.exceptions.InvalidInputStringException
import spock.lang.Specification
import spock.lang.Unroll

class BarrenAreaSpec extends Specification {

  @Unroll
  void 'buildBarrenArea with valid input #inputString'() {
    when:
    BarrenArea ba = BarrenArea.buildBarrenArea(inputString, w, h)

    then:
    ba.southWest.x == swx
    ba.southWest.y == swy
    ba.northEast.x == nex
    ba.northEast.y == ney
    !ba.northEast.fertile
    !ba.southWest.fertile

    where:
    inputString       | w    | h    | swx | swy | nex | ney
    '0 0 0 0'         | 100  | 100  | 0   | 0   | 0   | 0
    '10 20 30 40'     | 100  | 100  | 10  | 20  | 30  | 40
    '99 99 99 99'     | 100  | 100  | 99  | 99  | 99  | 99
    '0 0 99 99'       | 100  | 100  | 0   | 0   | 99  | 99
    '0 0 99 99'       | 400  | 600  | 0   | 0   | 99  | 99
  }

  @Unroll
  void 'buildBarrenArea with invalid input string format #inputString'() {
    when:
    BarrenArea.buildBarrenArea(inputString, 100, 100)

    then:
    InvalidInputStringException exception = thrown(InvalidInputStringException)
    exception.message == "Input string ${inputString} is not valid."

    where:
    inputString << ['0, 0, 99, 99', '0, 0, 99', '0 0, 99 99', 'a b c d']
  }

  @Unroll
  void 'buildBarrenArea with input string out of bounds #inputString'() {
    when:
    BarrenArea.buildBarrenArea(inputString, 100, 100)

    then:
    InvalidInputStringException exception = thrown(InvalidInputStringException)
    exception.message == "Input string ${inputString} is out of bounds."

    where:
    inputString << ['0 0 100 100', '0 100 99 99', '99 99 0 0']
  }
}
