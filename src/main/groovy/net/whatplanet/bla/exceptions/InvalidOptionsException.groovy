package net.whatplanet.bla.exceptions

import groovy.transform.CompileStatic

@CompileStatic
class InvalidOptionsException extends Exception {
  InvalidOptionsException(String message) {
    super(message)
  }
}
