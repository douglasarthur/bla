package net.whatplanet.bla.exceptions

import groovy.transform.CompileStatic

@CompileStatic
class InvalidInputStringException extends Exception {
  InvalidInputStringException(String message) {
    super(message)
  }
}
