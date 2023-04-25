class LewisCarrollBook {
    var name: String = ""
        get() {
            println("The name of the book is $field")
            return field
        }
        set(value) {
            println("Now, a book called $value")
            field = value
        }
    var author: String = "Lewis Carroll"
        get() {
            println("The author of the book is $field")
            return field
        }
        set(value) {
            field = value
        }
    var price: Int = 0
        get() {
            return field
        }
        set(value) {
            println("Putting a new price...")
            println("The new price is $value")
            field = value
        }
}