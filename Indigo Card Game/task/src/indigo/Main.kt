package indigo

enum class Suit(private val symbol: String) {
    CLUBS("♣"),
    DIAMOND("♦"),
    HEART("♥"),
    SPADE("♠");

    override fun toString(): String = symbol
}

enum class Rank(private val symbol: String, val point: Int) {
    KING("K", 1),
    QUEEN("Q", 1),
    JACK("J", 1),
    TEN("10", 1),
    NINE("9", 0),
    EIGHT("8", 0),
    SEVEN("7", 0),
    SIX("6", 0),
    FIVE("5", 0),
    FOUR("4", 0),
    THREE("3", 0),
    TWO("2", 0),
    ACE("A", 1);

    override fun toString(): String = symbol
}

enum class Player {
    USER, COMPUTER
}

data class Card(private val suit: Suit, private val rank: Rank) {
    override fun toString(): String = "$rank$suit"

    fun getPoint(): Int {
        return this.rank.point
    }

    fun getSuit(): Suit {
        return this.suit
    }

    fun getRank(): Rank {
        return this.rank
    }
}

class Deck {
    private val list: MutableList<Card> = mutableListOf()

    init {
        init()
    }

    private fun init() {
        list.clear()

        for (suit in Suit.values()) {
            for (rank in Rank.values()) {
                list.add(Card(suit, rank))
            }
        }
    }

    fun reset() {
        init()
    }

    fun shuffle() {
        list.shuffle()
    }

    fun get(number: Int): List<Card> {
        val take = list.take(number)
        val drop = list.drop(number)

        list.clear()
        list.addAll(drop)

        return take
    }

    fun size(): Int {
        return list.size
    }
}

class Game {
    private val deck: Deck = Deck()
    private val tableCardList: MutableList<Card> = mutableListOf()
    private val userCardList: MutableList<Card> = mutableListOf()
    private val computerCardList: MutableList<Card> = mutableListOf()

    private val userCardWinList: MutableList<Card> = mutableListOf()
    private val computerCardWinList: MutableList<Card> = mutableListOf()

    private var currentPlayer: Player = Player.COMPUTER
    private var firstPlayer: Player = Player.COMPUTER
    private var lastWinPlayer: Player? = null

    private val tableCardCount: Int = 4
    private val playerCardCount: Int = 6
    private val deckSize: Int = this.deck.size()

    init {
        println("Indigo Card Game")
    }

    private fun reset() {
        this.deck.reset()
        this.deck.shuffle()

        this.tableCardList.addAll(this.deck.get(this.tableCardCount))

        this.userCardList.addAll(this.deck.get(this.playerCardCount))
        this.computerCardList.addAll(this.deck.get(this.playerCardCount))
    }

    private fun chooseFirstPlayer() {
        while (true) {
            println("Play first?")
            val input = readln().lowercase()

            this.currentPlayer = when (input) {
                "yes" -> Player.USER
                "no" -> Player.COMPUTER
                else -> continue
            }

            this.firstPlayer = this.currentPlayer

            break
        }
    }

    private fun printTableStatus() {
        if (this.tableCardList.isEmpty()) {
            println("No cards on the table")
            return
        }

        println("${this.tableCardList.size} cards on the table, and the top card is ${this.tableCardList.last()}")
    }

    private fun printGameStatus(isGameOver: Boolean = false) {
        var userScore: Int = this.getPointSumByCardList(this.userCardWinList)
        var computerScore: Int = this.getPointSumByCardList(this.computerCardWinList)

        if (isGameOver) {
            if (this.userCardWinList.size > this.computerCardWinList.size) {
                userScore += 3
            }

            if (this.userCardWinList.size < this.computerCardWinList.size) {
                computerScore += 3
            }

            if (this.userCardWinList.size == this.computerCardWinList.size) {
                if (this.firstPlayer == Player.USER) {
                    userScore += 3
                }

                if (this.firstPlayer == Player.COMPUTER) {
                    computerScore += 3
                }
            }
        }

        println("Score: Player $userScore - Computer $computerScore")
        println("Cards: Player ${this.userCardWinList.size} - Computer ${this.computerCardWinList.size}")
    }

    private fun getPointSumByCardList(list: MutableList<Card>): Int {
        var count: Int = 0

        for (card in list) {
            count += card.getPoint()
        }

        return count
    }

    private fun userMove(): Boolean {
        println(
            "Cards in hand: ${
                this.userCardList.joinToString(
                    separator = " ",
                    transform = { "${this.userCardList.indexOf(it) + 1})$it" })
            }"
        )

        var cardNumber: String

        while (true) {
            println("Choose a card to play (1-${this.userCardList.size}):")
            cardNumber = readln()

            if (cardNumber == "exit") return false
            if (cardNumber.toIntOrNull() !in 1..this.userCardList.size) continue

            break
        }

        val card = this.userCardList.removeAt(cardNumber.toInt() - 1)
        this.tableCardList.add(card)

        if (this.userCardList.size == 0) {
            this.userCardList.addAll(this.deck.get(this.playerCardCount))
        }

        return true
    }

    private fun getComputerCard(): Card {
        // 1) If there is only one card in hand, put it on the table
        if (this.computerCardList.size == 1) {
            return this.computerCardList.first()
        }

        val candidateCardList: MutableList<Card> = mutableListOf()
        val candidateSameSuitCardList: MutableList<Card> = mutableListOf()
        val candidateSameRankCardList: MutableList<Card> = mutableListOf()

        val sameSuitCardList: MutableList<Card> = mutableListOf()
        val sameRankCardList: MutableList<Card> = mutableListOf()

        if (this.tableCardList.isNotEmpty()) {
            val topCard = this.tableCardList.last()

            for (computerCard in this.computerCardList) {
                if (computerCard.getSuit() == topCard.getSuit()) {
                    candidateSameSuitCardList.add(computerCard)
                }

                if (computerCard.getRank() == topCard.getRank()) {
                    candidateSameRankCardList.add(computerCard)
                }
            }
        }

        candidateCardList.addAll(candidateSameSuitCardList)
        candidateCardList.addAll(candidateSameRankCardList)

        for (computerCardFirst in this.computerCardList) {
            for (computerCardSecond in this.computerCardList) {
                if (computerCardFirst == computerCardSecond) continue

                if (!sameSuitCardList.contains(computerCardFirst)) {
                    if (computerCardFirst.getSuit() == computerCardSecond.getSuit()) {
                        sameSuitCardList.add(computerCardFirst)
                    }
                }

                if (!sameRankCardList.contains(computerCardFirst)) {
                    if (computerCardFirst.getRank() == computerCardSecond.getRank()) {
                        sameRankCardList.add(computerCardFirst)
                    }
                }
            }
        }

        // 2) If there is only one candidate card, put it on the table
        if (candidateCardList.size == 1) {
            return candidateCardList.first()
        }

        // 3) If there are no cards on the table
        if (this.tableCardList.isEmpty()) {
            if (sameSuitCardList.isNotEmpty()) {
                return sameSuitCardList.random()
            }
            if (sameRankCardList.isNotEmpty()) {
                return sameRankCardList.random()
            }
            return this.computerCardList.random()
        }

        // 4) If there are cards on the table but no candidate cards
        if (this.tableCardList.isNotEmpty() && candidateCardList.isEmpty()) {
            if (sameSuitCardList.isNotEmpty()) {
                return sameSuitCardList.random()
            }
            if (sameRankCardList.isNotEmpty()) {
                return sameRankCardList.random()
            }
            return this.computerCardList.random()
        }

        // 5) If there are two or more candidate cards
        if (candidateCardList.size > 1) {
            if (candidateSameSuitCardList.size > 1) {
                return candidateSameSuitCardList.random()
            }
            if (candidateSameRankCardList.size > 1) {
                return candidateSameRankCardList.random()
            }
            return candidateCardList.random()
        }

        // If there is only one card in hand
        return this.computerCardList.first()
    }

    private fun computerMove(): Boolean {
        println(this.computerCardList.joinToString(" "))

        val card = this.getComputerCard()
        this.computerCardList.remove(card)
        this.tableCardList.add(card)

        println("Computer plays $card")

        if (this.computerCardList.size == 0) {
            this.computerCardList.addAll(this.deck.get(this.playerCardCount))
        }

        return true
    }

    private fun checkPlayerMove() {
        if (this.tableCardList.size < 2) return;

        val topCard: Card = this.tableCardList[this.tableCardList.lastIndex - 1]
        val playerCard: Card = this.tableCardList.last()
        var isWinner: Boolean = false

        if (topCard.getRank() == playerCard.getRank() || topCard.getSuit() == playerCard.getSuit()) {
            isWinner = true
        }

        if (!isWinner) return

        if (this.currentPlayer == Player.USER) {
            this.userCardWinList.addAll(this.tableCardList)

            println("Player wins cards")
        }

        if (this.currentPlayer == Player.COMPUTER) {
            this.computerCardWinList.addAll(this.tableCardList)

            println("Computer wins cards")
        }

        this.lastWinPlayer = this.currentPlayer

        this.tableCardList.clear()

        this.printGameStatus()
    }


    private fun checkGameStatus(): Boolean {
        var isGameOver = false

        if (this.currentPlayer == Player.USER && this.userCardList.isEmpty()) {
            isGameOver = true
        }

        if (this.currentPlayer == Player.COMPUTER && this.computerCardList.isEmpty()) {
            isGameOver = true
        }

        if (this.tableCardList.size == this.deckSize) {
            isGameOver = true
        }

        if (!isGameOver) return true

        if (this.lastWinPlayer == Player.USER) {
            this.userCardWinList.addAll(this.tableCardList)
        }

        if (this.lastWinPlayer == Player.COMPUTER) {
            this.computerCardWinList.addAll(this.tableCardList)
        }

        if (this.lastWinPlayer == null) {
            if (this.firstPlayer == Player.USER) {
                this.userCardWinList.addAll(this.tableCardList)
            }

            if (this.firstPlayer == Player.COMPUTER) {
                this.computerCardWinList.addAll(this.tableCardList)
            }
        }

        this.tableCardList.clear()
        this.printGameStatus(true)

        return false
    }

    fun start() {
        this.reset()
        this.chooseFirstPlayer()

        println("Initial cards on the table: ${this.tableCardList.joinToString(" ")}")

        while (true) {
            println()
            this.printTableStatus()

            if (!checkGameStatus()) return // exit

            if (this.currentPlayer == Player.USER) {
                if (!this.userMove()) return // exit
            }

            if (this.currentPlayer == Player.COMPUTER) {
                this.computerMove()
            }

            this.checkPlayerMove()

            // toggle
            this.currentPlayer = if (this.currentPlayer == Player.USER) Player.COMPUTER else Player.USER
        }
    }

    fun finish() {
        println("Game over")
    }
}

fun main() {
    val game: Game = Game()

    game.start()
    game.finish()
}