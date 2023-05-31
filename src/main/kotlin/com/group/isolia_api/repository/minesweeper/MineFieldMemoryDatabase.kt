package com.group.isolia_api.repository.minesweeper

object MineFieldMemoryDatabase {
    private const val size: Int = 12
    private const val mines: Int = 20
    private val mineCoords = mutableListOf<Pair<Int, Int>>()
    private val actionHistory = mutableListOf<Pair<Int, Int>>()

    fun reset() {
        mineCoords.clear()
        actionHistory.clear()
        for (i in 0 until mines) {
            var x = (Math.random() * size).toInt()
            var y = (Math.random() * size).toInt()
            while (mineCoords.contains(Pair(x, y))) {
                x = (Math.random() * size).toInt()
                y = (Math.random() * size).toInt()
            }
            mineCoords.add(Pair(x, y))
        }
    }
}