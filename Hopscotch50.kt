package kattis.nac20

import java.lang.System.`in`
import java.util.*
import kotlin.math.abs
import kotlin.math.min

fun main() {
    val br = `in`.bufferedReader()
    val (n, k) = br.readLine().split(" ").map(String::toInt)
    val nodesByNum = Array<MutableList<Node>>(k) {
        LinkedList()
    }

    repeat(n) {
        val line = br.readLine().split(" ").map { a -> a.toInt() - 1 }
        for (c in 0 until n) {
            nodesByNum[line[c]].add(Node(c, it, line[c]))
        }
    }
    var minDist = Int.MAX_VALUE

    val nodeQueue = PriorityQueue<NodeWrapper>(NodeWrapper::compare)

    for (x in nodesByNum[0]) {
        nodeQueue.add(NodeWrapper(x, 0))
    }

    while (!nodeQueue.isEmpty()) {
        val nextNodeWrapper = nodeQueue.poll()
        val node = nextNodeWrapper.node
        val dist = nextNodeWrapper.dist
        if (dist <= node.dist) {
            node.dist = dist
            if (node.label < k - 1) {
                for (c in nodesByNum[node.label + 1]) {
                    val dist2 = dist + abs(c.x - node.x) + abs(c.y - node.y)
                    if (dist2 < c.dist) {
                        c.dist = dist2
                        nodeQueue.add(NodeWrapper(c, dist2))
                    }
                }
            } else if (node.label == k - 1) {
                minDist = min(dist, minDist)
            }
        }
    }
    print(if (minDist == Int.MAX_VALUE) {
        -1
    } else {
        minDist
    })
}

private class Node(val x: Int, val y: Int, val label: Int) {
    var dist = Int.MAX_VALUE
}

private class NodeWrapper(val node: Node, val dist: Int) {
    fun compare(other: NodeWrapper): Int {
        return dist - other.dist
    }
}