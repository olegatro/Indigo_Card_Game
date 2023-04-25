import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    var balance = readln().toInt()
    var hasError: Boolean = false

    while (scanner.hasNextInt()) {
        val payment = scanner.nextInt()

        if (balance < payment) {
            println("Error, insufficient funds for the purchase. Your balance is $balance, but you need $payment.")
            hasError = true
            break
        }

        balance -= payment
    }

    if (!hasError) {
        println("Thank you for choosing us to manage your account! Your balance is $balance.")
    }
}