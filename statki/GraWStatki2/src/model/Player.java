package model;

import java.util.List;

public abstract class Player {

    protected String name;
    protected Board ownBoard;
    protected Board opponentViewBoard;

    public Player(String name, int boardSize) {
        this.name = name;
        this.ownBoard = new Board(boardSize);
        this.opponentViewBoard = new Board(boardSize);
    }

    public String getName() {
        return name;
    }

    public Board getOwnBoard() {
        return ownBoard;
    }

    public Board getOpponentViewBoard() {
        return opponentViewBoard;
    }

    public abstract Coordinate makeMove();

    public void handleShotResult(Coordinate coord, boolean wasHit) {
        opponentViewBoard.markShot(coord, wasHit); // metoda pomocnicza, dodamy niżej
    }

    public abstract void placeShips(List<Ship> fleet);
}

