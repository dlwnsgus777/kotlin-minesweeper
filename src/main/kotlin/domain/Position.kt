package domain

data class Position(val row: PositiveNumber, val column: PositiveNumber) {

    fun getValidAdjacentPositions(height: PositiveNumber, width: PositiveNumber): Positions {
        val adjacentPositions = AdjacentPosition.values()
        return adjacentPositions.map { position ->
            val newRow = row.value + position.row
            val newColumn = column.value + position.column
            newRow to newColumn
        }
            .filter { (row, column) ->
                row in MineSweeperMap.MAP_START_INDEX_VALUE..height.value &&
                    column in MineSweeperMap.MAP_START_INDEX_VALUE..width.value
            }
            .map { fromInt(it.first, it.second) }
            .toPositions()
    }

    companion object {
        fun fromInt(row: Int, column: Int): Position {
            return Position(row.toPositiveNumber(), column.toPositiveNumber())
        }
    }
}
