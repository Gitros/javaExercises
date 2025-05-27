package model;

import java.util.List;

public abstract class Player {

    protected String name;
    protected Board ownBoard;          // nasze statki
    protected Board opponentViewBoard; // co wiemy o planszy przeciwnika (trafienia/pudła)

    public Player(String name, int boardSize) {
        this.name = name;
        this.ownBoard = new Board(boardSize);
        this.opponentViewBoard = new Board(boardSize); // pusta, tylko do podglądu
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

    /**
     * Zwraca współrzędne kolejnego strzału.
     * Implementacja zależy od rodzaju gracza.
     */
    public abstract Coordinate makeMove();

    /**
     * Informujemy gracza o wyniku strzału (przyda się AI i podglądowi dla człowieka).
     */
    public void handleShotResult(Coordinate coord, boolean wasHit) {
        opponentViewBoard.markShot(coord, wasHit); // metoda pomocnicza, dodamy niżej
    }

    /**
     * Domyślnie: proste rozmieszczenie statków – możesz nadpisać w HumanPlayer,
     * żeby korzystać z GUI, a w AI zrobić losowe.
     */
    public abstract void placeShips(List<Ship> fleet);
}

