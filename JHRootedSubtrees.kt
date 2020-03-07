package kattis.nac20

import java.lang.StringBuilder
import java.lang.System.`in`
import java.util.*
import kotlin.math.max
import kotlin.math.min

fun main() {
    val br = `in`.bufferedReader()
    val sb = StringBuilder()
    val (numNodes, numQueries) = br.readLine().split(' ').map(String::toInt)
    val connections = Array(numNodes) {
        emptyList<Int>().toMutableList()
    }
    repeat(numNodes - 1) {
        val (id1, id2) = br.readLine().split(' ').map { it.toInt() - 1 }
        connections[id1].add(id2)
        connections[id2].add(id1)
    }

    val preCompArray = Array(1 + Integer.bitCount(Integer.highestOneBit(2 * numNodes - 1) - 1)) {
        IntArray(2 * numNodes - 1)
    }
    val traversalIDs = IntArray(numNodes)
    var traversalID = 0
    var currentNode = 0
    var depth = 0
    val nodeStack = LinkedList<Int>()
    val itrStack = LinkedList<Iterator<Int>>()
    nodeStack.push(-1)
    itrStack.push(connections[currentNode].iterator())
    while (!itrStack.isEmpty()) {
        preCompArray[0][traversalID] = depth
        traversalID++
        var goUp = true
        if (itrStack.peek().hasNext()) {
            var next = itrStack.peek().next()
            if (next == nodeStack.peek()) {
                if (itrStack.peek().hasNext()) {
                    goUp = false
                    next = itrStack.peek().next()
                }
            } else {
                goUp = false
            }
            if (!goUp) {
                depth++
                nodeStack.push(currentNode)
                itrStack.push(connections[next].iterator())
                currentNode = next
                traversalIDs[next] = traversalID
            }
        }
        if (goUp) {
            depth--
            currentNode = nodeStack.pop()
            itrStack.pop()
        }
    }

    for (pow in 1..preCompArray.lastIndex) {
        val num = 1 shl pow
        for (start in preCompArray[pow].indices) {
            if (start + num <= preCompArray[pow].size) {
                preCompArray[pow][start] = min(preCompArray[pow - 1][start], preCompArray[pow - 1][start + (num shr 1)])
            }
        }
    }
    for (query in 1..numQueries) {
        val (a, b) = br.readLine().split(' ').map { it.toInt() - 1 }
        val dist = dist(traversalIDs[a], traversalIDs[b], preCompArray).toLong()
        val ways = numNodes.toLong() + (dist * (dist + 1L)) / 2L
        sb.append(ways)
        sb.append('\n')
    }
    print(sb)
}

private fun dist(traversalIDA: Int, traversalIDB: Int, preCompArray: Array<IntArray>): Int {
    return preCompArray[0][traversalIDA] + preCompArray[0][traversalIDB] - 2 * lcaDist(traversalIDA, traversalIDB, preCompArray)
}

private fun lcaDist(traversalIDA: Int, traversalIDB: Int, preCompArray: Array<IntArray>): Int {
    val a = min(traversalIDA, traversalIDB)
    val b = max(traversalIDA, traversalIDB)
    return if ((b - a) and (b - a + 1) == 0) {
        preCompArray[Integer.bitCount(b - a)][a]
    } else {
        val highestPow = Integer.highestOneBit(b - a) - 1
        min(preCompArray[Integer.bitCount(highestPow)][a], preCompArray[Integer.bitCount(highestPow)][b - highestPow])
    }
}
