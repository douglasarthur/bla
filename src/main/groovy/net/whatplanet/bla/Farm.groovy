package net.whatplanet.bla

import groovy.transform.CompileStatic

@CompileStatic
class Farm {
  private final int w
  private final int h
  private final List<BarrenArea> barrenAreas
  private List<List<Node>> grid

  Farm(int w, int h, List<BarrenArea> barrenAreas) {
    this.w = w
    this.h = h
    this.barrenAreas = barrenAreas
  }

  List<Integer> getFertileAreas() {
    initGrid()
    List<SortedSet<Node>> fertileAreas = []
    Node startNode = getNextFertileUnaccountedNode(grid[0][0])
    while (startNode) {
      fertileAreas << extendFertileAreaFrom(new TreeSet<Node>([startNode]))
      startNode = getNextFertileUnaccountedNode(startNode)
    }

    return fertileAreas*.size().sort().toList() as List<Integer>
  }

  private Node getNextFertileUnaccountedNode(Node start) {
    if (start.fertile && !start.accounted) {
      return start
    }
    int rowIndex = start.y
    while (rowIndex < h) {
      int colStartIndex = rowIndex == start.y ? start.x : 0
      List<Node> row
      if (colStartIndex == 0 && rowIndex > start.y) {
        row = grid[rowIndex]
      } else {
        row = grid[rowIndex].takeRight(w - colStartIndex - 1)
      }
      Node found = row.find { it.fertile && !it.accounted }
      if (found) {
        return found
      }
      rowIndex = rowIndex + 1
    }
  }

  private SortedSet<Node> extendFertileAreaFrom(SortedSet<Node> edges, SortedSet<Node> fertileArea = [] as SortedSet) {
    if (!edges) {
      return fertileArea
    }
    SortedSet<Node> nextEdges = [] as SortedSet
    edges.each { Node edge ->
        fertileArea << edge.account()
        nextEdges.addAll(findUnaccountedFertileNeighbors(edge))
    }
    return extendFertileAreaFrom(nextEdges, fertileArea)
  }

  private List<Node> findUnaccountedFertileNeighbors(Node node) {
    List<Node> results = []
    assert isInBounds(node.x, node.y)
    // north
    if (node.y < h - 1) {
      Node northNode = grid[node.y + 1][node.x]
      if (northNode.fertile && !northNode.accounted) {
        results << northNode
      }
    }
    // south
    if (node.y > 0) {
      Node southNode = grid[node.y - 1][node.x]
      if (southNode.fertile && !southNode.accounted) {
        results << southNode
      }
    }
    // east
    if (node.x < w - 1) {
      Node eastNode = grid[node.y][node.x + 1]
      if (eastNode.fertile && !eastNode.accounted) {
        results << eastNode
      }
    }
    // west
    if (node.x > 0) {
      Node westNode = grid[node.y][node.x - 1]
      if (westNode.fertile && !westNode.accounted) {
        results << westNode
      }
    }
    return results
  }

  private boolean isBarren(int x, int y) {
    assert isInBounds(x, y)
    return barrenAreas.find { BarrenArea ba ->
        ba.southWest.x <= x &&
        ba.southWest.y <= y &&
        ba.northEast.x >= x &&
        ba.northEast.y >= y } ? true : false
  }

  private void initGrid() {
    List<List<Node>> grid = []
    (0..h - 1).each { Integer y ->
      List<Node> row = []
      (0..w - 1).each { Integer x ->
        row << new Node(x: x, y: y, fertile: !isBarren(x, y), accounted: false)
      }
      grid << row
    }
    this.grid = grid
  }

  private boolean isInBounds(int x, int y) {
    return (x >= 0 && y >= 0 && x < w && y < h)
  }
}
