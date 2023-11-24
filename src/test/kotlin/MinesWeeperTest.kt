import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MinesWeeperTest {

    @Test
    fun `게임 시나리오 테스트`() {
        // given
        val userInterface = MockUserInterface()
        val mineGenerator = MockMineGenerator()
        val minesWeeper = MinesWeeper(userInterface, mineGenerator)

        // when
        minesWeeper.start()

        // then
        userInterface.assertStartAnnouncement()
        userInterface.assertGameBoard()
    }
}

class MockMineGenerator : MineGenerator {
    override fun generate(height: Int, width: Int, count: Int): List<Point> =
        listOf(
            Point(0, 0),
            Point(1, 1),
            Point(2, 2),
            Point(3, 3),
            Point(4, 4),
        )
}

class MockUserInterface : UserInterface {
    private val startAnnouncement = "지뢰찾기 게임을 시작합니다."
    private val height = 5
    private val width = 5
    private val mineCount = 5
    private val gameBoard =
        """
        |* C C C C
        |C * C C C
        |C C * C C
        |C C C * C
        |C C C C *
        """.trimMargin()

    private val outputs = mutableListOf<String>()

    override fun askHeight(): Int = height

    override fun askWidth(): Int = width

    override fun askMineCount(): Int = mineCount

    override fun printStartAnnouncement() {
        outputs.add(startAnnouncement)
    }

    override fun printGameBoard(gameBoard: GameBoard) {
        outputs.add(
            gameBoard.cellMatrix.joinToString("\n") { cellGrid ->
                cellGrid.cells.joinToString(" ") { if (it.isMine()) "*" else "C" }
            }
        )
    }

    fun assertStartAnnouncement() = outputs[0] shouldBe startAnnouncement

    fun assertGameBoard() = outputs[1] shouldBe gameBoard
}