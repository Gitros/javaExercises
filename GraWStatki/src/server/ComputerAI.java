package server;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class ComputerAI {
    private List<Point> targets = new LinkedList<>();
    private Random random = new Random();
    private char[][] playerBoard;

    public ComputerAI(char[][] playerBoard) {
        this.playerBoard = playerBoard;
    }

    public Point getNextMove() {
        if (!targets.isEmpty()) {
            return targets.remove(0);
        }

        Point p;
        do {
            p = new Point(random.nextInt(10), random.nextInt(10));
        } while (playerBoard[p.y][p.x] == 'X' || playerBoard[p.y][p.x] == '-');

        return p;
    }

    public void reportHit(Point p, boolean wasHit) {
        if (wasHit) {
            addTarget(p.x + 1, p.y);
            addTarget(p.x - 1, p.y);
            addTarget(p.x, p.y + 1);
            addTarget(p.x, p.y - 1);
        }
    }

    private void addTarget(int x, int y) {
        if (x >= 0 && y >= 0 && x < 10 && y < 10 && playerBoard[y][x] == 'O') {
            targets.add(new Point(x, y));
        }
    }
}
