package sk.stuba.fei.uim.oop.board;

import javax.swing.*;
import java.awt.*;

public class Stone extends JPanel{
    private final StoneColor owner;
    private int width;
    private int height;
    private int x;
    private int y;

    public Stone(StoneColor owner, int size) {
        this.owner = owner;
        setOpaque(false);
        this.sizeOfStone(size);
        this.locationOfStone(size);
    }

    private void sizeOfStone(int size) {
        this.width = 360/size;
        this.height = 360/size;
    }

    private void locationOfStone(int size) {
        switch (size) {
            case 6:
                this.x = 16;
                this.y = 13;
                break;
            case 8:
                this.x = 12;
                this.y = 9;
                break;
            case 10:
                this.x = 10;
                this.y = 7;
                break;
            case 12:
                this.x = 8;
                this.y = 5;
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.owner.equals(StoneColor.BLACK)) {
            g.setColor(Color.BLACK);
            g.fillArc(this.x, this.y, this.width, this.height, 0,360);
        } else if (this.owner.equals(StoneColor.WHITE)) {
            g.setColor(Color.WHITE);
            g.fillArc(this.x, this.y, this.width, this.height, 0,360);
        } else if(this.owner.equals(StoneColor.POSSIBLE)) {
            g.setColor(Color.BLACK);
            g.drawArc(this.x, this.y, this.width, this.height,0,360);
        }
    }
}
