package sk.stuba.fei.uim.oop.player;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.board.Board;
import sk.stuba.fei.uim.oop.board.StoneColor;
import sk.stuba.fei.uim.oop.controls.GameLogic;

@Getter @Setter
public class Computer extends Player{
    private int mostStones;
    private int row;
    private int col;

    public Computer(StoneColor stone) {
        super(stone);
        this.mostStones = 0;
    }

    public void setComputerMove(int row, int col, int numberOfStones, Player currentPlayer) {
        if (currentPlayer == this) {
            if (numberOfStones > this.getMostStones()) {
                this.setMostStones(numberOfStones);
                this.setRow(row);
                this.setCol(col);
            }
        }
    }

    public void selectStone(Board board, GameLogic logic) {
        logic.checkForChange(row, col, getStone(),true);
        board.placeStone(row, col, this, logic);
        mostStones = 0;
    }
}
