fun main() {
    val input = readln().split(" ")
    val firstNumber: Long = input[0].toLong()
    val operator: String = input[1]
    val secondNumber: Long = input[2].toLong()

    println(
        when (operator) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "*" -> firstNumber * secondNumber
            "/" -> when (secondNumber) {
                0L -> "Division by 0!"
                else -> firstNumber / secondNumber
            }

            else -> "Unknown operator"
        }
    )
}
