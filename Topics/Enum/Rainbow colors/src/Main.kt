enum class Rainbow {
    RED,
    ORANGE,
    YELLOW,
    GREEN,
    BLUE,
    INDIGO,
    VIOLET
}

fun main() {
    val input: String = readln()

    var isRainbow: Boolean = false

    for (enum in Rainbow.values()) {
        if (input.toUpperCase() == enum.name) {
            isRainbow = true
            break
        }
    }

    println(isRainbow)
}
