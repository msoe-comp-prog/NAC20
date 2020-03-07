package kattis.nac20

import java.lang.System.`in`
import java.util.*
import kotlin.math.max
import kotlin.math.min

fun main() {
    val br = `in`.bufferedReader()
    val (n, p, q, s) = br.readLine().split(' ').map(String::toInt)
    val pProblems = IntArray(p)
    val qProblems = IntArray(q)
    for (x in 0 until p) {
        pProblems[x] = br.readLine().toInt()
    }
    for (x in 0 until q) {
        qProblems[x] = br.readLine().toInt()
    }
    pProblems.sort()
    qProblems.sort()
    var maxD = 1 + max(pProblems.last() - qProblems.first(), qProblems.last() - pProblems.first())
    var minD = 0
    var curGuess = (maxD + minD) / 2
    var lowestCorrect = -1
    while (minD != maxD) {
        if (checkPossible(curGuess, pProblems, qProblems, s, n)) {
            lowestCorrect = curGuess
            maxD = curGuess
        } else {
            minD = curGuess + 1
        }
        curGuess = (minD + maxD) / 2
    }

    print(lowestCorrect)
}

private fun checkPossible(testThreshold: Int, classicalProblems: IntArray, creativeProblems: IntArray, maxSum: Int, numDays: Int): Boolean {
    var classicalID = binarySearchHighestNotExceeding((testThreshold + maxSum) / 2, classicalProblems)
    val smartQueue = LinkedList<Int>()
    if (classicalID == -1) {
        return false
    }
    var classicalProb = classicalProblems[classicalID]

    var creativeTopID1 = binarySearchHighestNotExceeding(classicalProb + testThreshold, creativeProblems)
    var creativeTopID2 = binarySearchHighestNotExceeding(maxSum - classicalProb, creativeProblems)
    var prevTop = min(creativeTopID1, creativeTopID2)
    var curTop: Int
    var creativeLowID = binarySearchLowestNotBelow(classicalProb - testThreshold, creativeProblems)
    var prevBottom = creativeLowID

    for (creativeIDToAdd in creativeLowID..prevTop) {
        smartQueue.addFirst(creativeProblems[creativeIDToAdd])
    }
    var daysAssigned = 0
    while (classicalID > -1) {
        classicalProb = classicalProblems[classicalID]
        while (creativeTopID1 != -1 && creativeProblems[creativeTopID1] > classicalProb + testThreshold) {
            creativeTopID1--
        }
        while (creativeTopID2 != creativeProblems.lastIndex && creativeProblems[creativeTopID2 + 1] <= maxSum - classicalProb) {
            creativeTopID2++
        }
        while (creativeLowID != 0 && creativeProblems[creativeLowID - 1] >= classicalProb - testThreshold) {
            creativeLowID--
        }
        curTop = min(creativeTopID1, creativeTopID2)
        for(creativeIDToAdd in (prevTop + 1)..curTop){
            smartQueue.addFirst(creativeProblems[creativeIDToAdd])
        }
        for(creativeIDToAdd in creativeLowID until prevBottom){
            smartQueue.addLast(creativeProblems[prevBottom - creativeIDToAdd + creativeLowID - 1])
        }
        prevBottom = creativeLowID
        prevTop = curTop

        while(!smartQueue.isEmpty() && smartQueue.first - classicalProb > testThreshold){
            smartQueue.removeFirst()
        }
        if(!smartQueue.isEmpty()){
            smartQueue.removeFirst()
            daysAssigned++
            if(daysAssigned >= numDays){
                return true
            }
        }
        classicalID--
    }
    return false
}

private fun binarySearchHighestNotExceeding(target: Int, array: IntArray): Int {
    var max = array.size
    var min = 0
    var cur = (max + min) / 2
    var best = -1
    while (min != max) {
        val check = array[cur]
        if (check > target) {
            max = cur
        } else {
            min = cur + 1
            best = cur
        }
        cur = (max + min) / 2
    }
    return best
}

private fun binarySearchLowestNotBelow(target: Int, array: IntArray): Int {
    var max = array.size
    var min = 0
    var cur = (max + min) / 2
    var best = array.size
    while (min != max) {
        val check = array[cur]
        if (check >= target) {
            max = cur
            best = cur
        } else {
            min = cur + 1
        }
        cur = (max + min) / 2
    }
    return best
}
