package mineswipper.domain.map

import mineswipper.domain.map.position.Position
import mineswipper.domain.map.position.Positions
import mineswipper.domain.map.position.Row
import mineswipper.domain.map.position.Size
import mineswipper.domain.map.util.MarkGenerator

class Field(
    val size: Size,
    minePositions: Positions
) {
    val field: Map<Row, Pedals>

    init {
        val initField: MutableMap<Row, Pedals> = mutableMapOf()
        repeat(size.height) { x ->
            initField[Row(x)] = pedalSetting(x, minePositions)
        }
        field = initField.toMap()

        MarkGenerator.markGeneration(this)
    }

    private fun pedalSetting(x: Int, minePositions: Positions): Pedals {
        val pedalList = (0 until size.width).map { y ->
            val position = Position(x, y)
            createPedal(minePositions, position)
        }
        return Pedals(pedalList)
    }

    private fun createPedal(
        minePositions: Positions,
        position: Position
    ): Pedal {
        return if (minePositions.contains(position)) Mine() else NormalPedal()
    }

    fun findPedal(position: Position): Pedal {
        val pedals = field[position.toRow()]
        require(pedals != null) { VALID_MESSAGE }

        return pedals.get(position.x)
    }

    companion object {
        private const val VALID_MESSAGE: String = "해당 위치에 값이 존재하지 않습니다."
    }
}
