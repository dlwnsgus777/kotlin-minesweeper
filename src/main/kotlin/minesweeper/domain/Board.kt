package minesweeper.domain

class Board(val matrix: Matrix, mineCount: Int) {
    init {
        createRandomMine(mineCount)
        createSafe()
    }

    private fun createRandomMine(count: Int) {
        require(count <= matrix.width() * matrix.height()) {
            "지뢰 개수는 지도에 존재하는 모든 필드의 수보다 클 수 없습니다."
        }

        coordinates().shuffled()
            .take(count)
            .forEach { matrix.rows[it.rows][it.cols] = Mine() }
    }

    fun open(coordinate: Coordinate) {
        val field = matrix.rows[coordinate.rows][coordinate.cols]
        field.open()

        if (field is Safe && field.aroundMineCount == 0) this.openAround(coordinate)

        canOpenFields(coordinate)
            .filter { noMinesAround(it) }
            .forEach { this.openAround(it) }
    }

    fun isWin(): Boolean {
        return coordinates().filter { matrix.rows[it.rows][it.cols] is Safe }
            .all { matrix.rows[it.rows][it.cols].isOpened() }
    }

    fun isLose(): Boolean {
        return coordinates().filter { matrix.rows[it.rows][it.cols] is Mine }
            .any { matrix.rows[it.rows][it.cols].isOpened() }
    }

    private fun openAround(coordinate: Coordinate) {
        matrix.rows[coordinate.rows][coordinate.cols].open()

        canOpenFields(coordinate)
            .filter { !noMinesAround(it) }
            .forEach { matrix.rows[it.rows][it.cols].open() }

        canOpenFields(coordinate)
            .filter { noMinesAround(it) }
            .forEach { openAround(it) }
    }

    private fun canOpenFields(coordinate: Coordinate) =
        CoordinateDirection.around(coordinate)
            .asSequence()
            .filter { it.rows in 0 until matrix.height() && it.cols in 0 until matrix.width() }
            .filter { !matrix.rows[it.rows][it.cols].isOpened() }
            .toList()

    private fun noMinesAround(coordinate: Coordinate) =
        matrix.rows[coordinate.rows][coordinate.cols] is Safe && (matrix.rows[coordinate.rows][coordinate.cols] as Safe).aroundMineCount == 0

    private fun createSafe() {
        coordinates()
            .forEach {
                if (matrix.rows[it.rows][it.cols] !is Mine)
                    matrix.rows[it.rows][it.cols] = Safe(aroundMineCount(it))
            }
    }

    private fun aroundMineCount(coordinate: Coordinate): Int {
        return CoordinateDirection.around(coordinate)
            .filter { it.rows in 0 until matrix.width() && it.cols in 0 until matrix.height() }
            .count { matrix.rows[it.rows][it.cols] is Mine }
    }

    private fun coordinates() = matrix.rows
        .indices
        .flatMap { rows: Int -> rowsCoordinates(rows) }

    private fun rowsCoordinates(rows: Int) =
        (0 until matrix.rows[rows].size).map { cols: Int -> Coordinate(rows, cols) }

    companion object {
        fun of(width: Int, height: Int, mineCount: Int) =
            Board(Matrix.of(height, width), mineCount)
    }
}