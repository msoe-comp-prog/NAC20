package nac20

fun main() {
    val (size, numShips) = readLine()!!.split(" ").map(String::toInt)

    val map = Array(size) { readLine()!!.toCharArray() }

    val ships = ArrayList<Int>()
    repeat(numShips) {
        ships += readLine()!!.toInt()
    }

    println(r(ships, map))
}

fun r(ships: List<Int>, map: Array<CharArray>) : Int {
    if (ships.isEmpty()) {
        return if (map.none { it.any { chr -> chr == 'O' } }) {
            1
        } else {
            0
        }
    } else {
        val check = ships.first()
        val pass = ships.drop(1)

        var ret = 0

        for (x in map.indices) {
            for (y in map.indices) {
                var good = true
                for (c in x until x + check) {
                    if (c < map.size) {
                        good = good and (map[c][y] == 'O' || map[c][y] == '.')
                    } else {
                        good = false
                    }
                }
                if (good) {
                    for (c in x until x + check) {
                        map[c][y] = if (map[c][y] == 'O') 'o' else 'T'
                    }
                    ret += r(pass, map)
                    for (c in x until x + check) {
                        map[c][y] = if (map[c][y] == 'o') 'O' else '.'
                    }
                }

                if (check != 1) {
                    good = true
                    for (c in y until y + check) {
                        if (c < map.size) {
                            good = good and (map[x][c] == 'O' || map[x][c] == '.')
                        } else {
                            good = false
                        }
                    }
                    if (good) {
                        for (c in y until y + check) {
                            map[x][c] = if (map[x][c] == 'O') 'o' else 'T'
                        }
                        ret += r(pass, map)
                        for (c in y until y + check) {
                            map[x][c] = if (map[x][c] == 'o') 'O' else '.'
                        }
                    }
                }
            }
        }

        return ret
    }
}