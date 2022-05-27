package sk.stuba.fei.uim.oop.player;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.StoneColor;

public class Player {
    @Getter
    private final StoneColor stone;

    public Player(StoneColor stone) {
        this.stone = stone;
    }

    public void flipStones(int row, int col, int adjRow, int adjCol, int distance, StoneColor[][] logicBoard) {
        for (int stoneNumber = 1; stoneNumber < distance; stoneNumber++) {
            int changeRow = row + (stoneNumber * adjRow);
            int changeCol = col + (stoneNumber * adjCol);
            logicBoard[changeRow][changeCol] = this.stone;
        }
    }
}
