package minesweeper_refactor.domain.board

import minesweeper_refactor.domain.block.Block
import minesweeper_refactor.domain.block.Blocks
import minesweeper_refactor.domain.coordinate.Coordinate

class BlockGenerator(private val coordinates: ArrayDeque<Coordinate>) {

    fun createMineBlocks(size: Int): Blocks {
        val coordinatesSize = coordinates.size

        check(value = coordinatesSize >= size) {
            "남아있는 블록 좌표가 부족합니다. 잔여 블록 : $coordinatesSize, 요청한 블록 : $size"
        }

        return Blocks(
            blocks = List(size = size) {
                Block.toMineBlockFrom(coordinate = coordinates.removeLast())
            },
        )
    }

    fun createRemainNumberBlocks(aroundMineCount: (Coordinate) -> Int): Blocks = Blocks(
        blocks = List(size = coordinates.size) {
            val coordinate = coordinates.removeLast()

            Block.toNumberBlockOf(
                coordinate = coordinate,
                aroundMineCount = aroundMineCount(coordinate),
            )
        },
    )
}