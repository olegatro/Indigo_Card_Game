// do not change this data class
data class Box(val height: Int, val length: Int, val width: Int)

fun main() {
    val height: Int = readln().toInt()
    val length1: Int = readln().toInt()
    val length2: Int = readln().toInt()
    val width: Int = readln().toInt()

    val box: Box = Box(height, length1, width)
    val box2: Box = box.copy(length = length2)

    println(box)
    println(box2)
}