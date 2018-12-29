package priort.hackerrank.kotlin.shortestreach.positiveedges

import org.junit.Assert
import org.junit.Test

class ShortestReachTests {

    @Test
    fun `finds path with smallest length from start to every other node`() {
        val edges = arrayOf(
            arrayOf(1 ,2, 24),
            arrayOf(1 ,4, 20),
            arrayOf(3 ,1, 3),
            arrayOf(4 ,3, 12)
        )
        Assert.assertArrayEquals(
            arrayOf(24, 3, 15),
            shortestReach(4, edges, 1))
    }

    @Test
    fun `toGraph represents an array of Edges as a map of each start node to list of each child with length to the child bi-directionally`() {
        val edges = listOf(
            Edge(1, 2, 4),
            Edge(1, 4, 7),
            Edge(2, 3, 38),
            Edge(3, 1, 71),
            Edge(3, 4, 72)
        )
        val expectedGraph = mapOf(
            1 to setOf(Child(2, 4), Child(4, 7), Child(3, 71)),
            2 to setOf(Child(1, 4), Child(3, 38)),
            3 to setOf(Child(1, 71), Child(4, 72), Child(2, 38)),
            4 to setOf(Child(1, 7), Child(3, 72))
        )

        Assert.assertEquals(expectedGraph, edges.toGraph())
    }
}