package model;

public abstract class Player {
    protected Board board = new Board();

    public Board getBoard() {
        return board;
    }

    public abstract int[] makeMove();
}
