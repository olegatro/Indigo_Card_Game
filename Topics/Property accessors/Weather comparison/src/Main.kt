class City(val name: String) {
    var degrees: Int = 0
        set(value) {
            field = when {
                value < -92 || value > 57 -> when (this.name) {
                    "Moscow" -> 5
                    "Hanoi" -> 20
                    "Dubai" -> 30
                    else -> 0
                }
                else -> value
            }
        }
}

fun main() {
    val first = readLine()!!.toInt()
    val second = readLine()!!.toInt()
    val third = readLine()!!.toInt()
    val firstCity = City("Dubai")
    firstCity.degrees = first
    val secondCity = City("Moscow")
    secondCity.degrees = second
    val thirdCity = City("Hanoi")
    thirdCity.degrees = third

    //implement comparing here
    var city: City = firstCity

    if (secondCity.degrees < city.degrees) {
        city = secondCity
    }

    if (thirdCity.degrees < city.degrees) {
        city = thirdCity
    }

    if (city.name != firstCity.name && city.degrees == firstCity.degrees) {
        println("neither")
        return
    }
    if (city.name != secondCity.name && city.degrees == secondCity.degrees) {
        println("neither")
        return
    }
    if (city.name != thirdCity.name && city.degrees == thirdCity.degrees) {
        println("neither")
        return
    }

    println(city.name)
}