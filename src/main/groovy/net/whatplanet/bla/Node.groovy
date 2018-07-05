package net.whatplanet.bla

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.Sortable
import groovy.transform.ToString

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
