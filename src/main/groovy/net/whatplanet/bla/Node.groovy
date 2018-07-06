package net.whatplanet.bla

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.Sortable
import groovy.transform.ToString

/**
 * Represents a square meter of farm and holds state used in finding fertile areas.
 */
@CompileStatic
@EqualsAndHashCode(excludes = 'fertile')
@Sortable(includes = ['x', 'y'])
@ToString
class Node {
  int x
  int y
  boolean fertile
  boolean accounted

  Node account() {
    this.accounted = true
    return this
  }
}
