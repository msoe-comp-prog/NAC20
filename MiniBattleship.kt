package kattis.nac20

fun main() {
    val (size, numships) = readLine()!!.split(" ").map(String::toInt)

    val map = Array(size) {
        readLine()!!.toCharArray()
    }

    val ships = ArrayList<Int>()
    repeat(numships) {
        ships += readLine()!!.toInt()
    }
    print(r(ships, map))
}

fun r(ships: List<Int>, map: Array<CharArray>): Int {
    if (ships.isEmpty()) {
        return if (map.none { it.any { char -> char == 'O' } }) 1 else 0
    } else {
        val check = ships.first();
        val pass = ships.drop(1);
        var sum = 0;
        for (row in map.indices) {
            for (col in map[row].indices) {
                if (check + row <= map.size) {
                    var valid = true
                    for (x in 0 until check) {
                        if (map[row + x][col] != '.' && map[row + x][col] != 'O') {
                            valid = false;
                            break
                        }
                    }
                    if (valid) {
                        for (x in 0 until check) {
                            if (map[row + x][col] == '.') {
                                map[row + x][col] = 'T';
                            } else if (map[row + x][col] == 'O') {
                                map[row + x][col] = 'o';
                            }
                        }

                        sum += r(pass, map);

                        for (x in 0 until check) {
                            if (map[row + x][col] == 'T') {
                                map[row + x][col] = '.';
                            } else if (map[row + x][col] == 'o') {
                                map[row + x][col] = 'O';
                            }
                        }
                    }

                }
                if (check > 1) {
                    if (check + col <= map.size) {
                        var valid = true
                        for (x in 0 until check) {
                            if (map[row][col + x] != '.' && map[row][col + x] != 'O') {
                                valid = false;
                                break
                            }
                        }
                        if (valid) {
                            for (x in 0 until check) {
                                if (map[row][col + x] == '.') {
                                    map[row][col + x] = 'T';
                                } else if (map[row][col + x] == 'O') {
                                    map[row][col + x] = 'o';
                                }
                            }
                            sum += r(pass, map);
                            for (x in 0 until check) {
                                if (map[row][col + x] == 'T') {
                                    map[row][col + x] = '.';
                                } else if (map[row][col + x] == 'o') {
                                    map[row][col + x] = 'O';
                                }
                            }
                        }
                    }
                }
            }
        }
        return sum;
    }
}
