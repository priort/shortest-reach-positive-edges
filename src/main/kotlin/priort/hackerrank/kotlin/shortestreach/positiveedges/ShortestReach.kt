package priort.hackerrank.kotlin.shortestreach.positiveedges

import java.util.*
import java.io.InputStreamReader
import java.io.BufferedReader
import kotlin.collections.HashSet


fun shortestReach(numNodes: Int, edges: Array<Array<Int>>, start: Int): Array<Int> =
    shortestReach(numNodes, edges.map { Edge(it[0], it[1], it[2]) }.toSet().toList().toGraph(), start)

fun shortestReach(numNodes: Int, edges: List<Edge>, start: Int): Array<Int> =
    shortestReach(numNodes, edges.toGraph(), start)

data class Edge(val start: Int, val end: Int, val length: Int)

data class Child(val node: Int, val length: Int)

typealias Graph = Map<Int, List<Child>>

fun List<Edge>.toGraph(): Map<Int, List<Child>> =
    HashMap(flatMap { listOf(it.start to Child(it.end, it.length), it.end to Child(it.start, it.length)) }.let {
            edgesAsPairs ->
        edgesAsPairs
            .groupBy { it.first }
            .toMap()
            .mapValues { mapEntryOfParentToChildren
                -> mapEntryOfParentToChildren.value.map { it.second }.toList().sortedBy { it.length }
            }
    })

typealias End = Int

fun shortestReach(numNodes: Int, graph: Graph, start: Int): Array<Int> {

    val shortestReachCache = HashMap<End, Int>()
    val shortestButNotNecessarilyVisitedCache = HashMap<End, Int>()
    (1 .. numNodes).asSequence()
        .filter { it != start }
        .let { ends ->
                    shortestReach(numNodes,
                        start,
                        ends.toSet(),
                        graph,
                        shortestReachCache,
                        shortestButNotNecessarilyVisitedCache)
        }
    return (1 .. numNodes)
        .filter { it != start }
        .map { shortestReachCache[it] ?: -1 }
        .toTypedArray()
}

data class NodeWithShortestReachSoFar(val node: Int, val shortestReachSoFar: Int)

fun shortestReach(
    numNodes: Int,
    start: Int,
    ends: Set<Int>,
    graph: Graph,
    shortestReachCache: MutableMap<End, Int>,
    shortestButNotNecessarilyVisitedCache: MutableMap<End, Int>) {

    fun calculateShortestReach() {
        val alreadyVisited = mutableSetOf<Int>()
        val toVisit =
            PriorityQueue<NodeWithShortestReachSoFar>(numNodes, kotlin.Comparator { n1, n2 ->
                n1.shortestReachSoFar.compareTo(n2.shortestReachSoFar)
            })
        toVisit.offer(NodeWithShortestReachSoFar(start, 0))


        do {
            val current = toVisit.poll()

            if (current.node !in alreadyVisited) {
                if (current.node in ends) {
                    val shortestReachSoFarToCurrent = shortestReachCache[current.node]
                    if (shortestReachSoFarToCurrent == null) {
                        shortestReachCache[current.node] = current.shortestReachSoFar
                        shortestButNotNecessarilyVisitedCache[current.node] = current.shortestReachSoFar
                    }
                }
                val shortestReachSoFarToCurrent = shortestReachCache[current.node]
                if (shortestReachSoFarToCurrent == null) {
                    shortestReachCache[current.node] = current.shortestReachSoFar
                }
                val children = graph[current.node] ?: emptyList()
                children.forEach { child ->
                    val reachToChild = current.shortestReachSoFar + child.length
                    val shortestReachSoFarToChild = shortestReachCache[child.node]
                    val shortestButNotNecessarilyVisitedForChild = shortestButNotNecessarilyVisitedCache[child.node]
                    if ((shortestReachSoFarToChild == null || shortestReachSoFarToChild == reachToChild)
                        && (shortestButNotNecessarilyVisitedForChild == null
                                || shortestButNotNecessarilyVisitedForChild >= reachToChild)
                    ) {

                        toVisit.offer(NodeWithShortestReachSoFar(child.node, reachToChild))
                    }
                    shortestButNotNecessarilyVisitedCache[child.node] = reachToChild

                }
                alreadyVisited.add(current.node)
            }

        } while (toVisit.isNotEmpty())
    }

    calculateShortestReach()
}

fun main(args: Array<String>) {
    val br = BufferedReader(InputStreamReader(System.`in`))

    val t = br.readLine().toInt()

    for (tItr in 1..t) {

        val nm = br.readLine()
        val st = StringTokenizer(nm)
        val n = st.nextToken().toInt()
        val m = st.nextToken().toInt()

        val edges = arrayListOf<Edge>()
        val parsedEdges = HashSet<Edge>()

        for (i in 0 until m) {
            val st2 = StringTokenizer(br.readLine())

            val edge = Edge(Integer.parseInt(st2.nextToken()), Integer.parseInt(st2.nextToken()), Integer.parseInt(st2.nextToken()))
            if (!parsedEdges.contains(edge) && !parsedEdges.contains(Edge(edge.end, edge.start, edge.length))) {
                edges.add(edge)
                parsedEdges.add(edge)
            }
        }

        val s = br.readLine().toInt()

        val result = shortestReach(n, edges, s)

        println(result.joinToString(" "))
    }
}



