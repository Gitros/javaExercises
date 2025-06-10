package model;

import java.util.*;

public class Board {
    private final int size;
    private final Map<Coordinate, CellState> grid;
    private final List<Ship> ships;
    private Ship lastHitShip = null;

    // Konstruktor tworzy pustą planszę o podanym rozmiarze
    public Board(int size) {
        this.size = size;
        grid = new HashMap<>();
        ships = new ArrayList<>();

        // inicjalizacja wszystkich komorek jako puste
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid.put(new Coordinate(row, col), CellState.EMPTY);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public List<Ship> getShips() {
        return ships;
    }

    // oznacza pole jako trafione lub chybione
    public void markShot(Coordinate c, boolean hit) {
        grid.put(c, hit ? CellState.HIT : CellState.MISS);
    }

    // zwraca stan danego pola
    public CellState getCellState(Coordinate coord) {
        return grid.getOrDefault(coord, CellState.EMPTY);
    }

    // probuje umiescic statek na planszy
    public boolean placeShip(Ship ship) {
        for (Coordinate coord : ship.getPositions()) {
            if (!grid.containsKey(coord) || grid.get(coord) != CellState.EMPTY) {
                return false;
            }
        }
        for (Coordinate coord : ship.getPositions()) {
            grid.put(coord, CellState.SHIP);
        }
        ships.add(ship);
        return true;
    }

    // obsluguje strzal na dane pole
    public boolean receiveShot(Coordinate coord) {
        if (!grid.containsKey(coord)) return false;

        CellState current = grid.get(coord);
        if (current == CellState.HIT || current == CellState.MISS) return false; // Już strzelone

        if (current == CellState.SHIP) {
            grid.put(coord, CellState.HIT); // trafiono

            // ktory statek został trafiony
            for (Ship s : ships) {
                if (s.contains(coord)) {
                    s.hit(coord);
                    lastHitShip = s;
                    break;
                }
            }

            return true;
        } else {
            grid.put(coord, CellState.MISS);
            lastHitShip = null;
            return false;
        }
    }


    public boolean wasLastShipSunk() {
        return lastHitShip != null && lastHitShip.isSunk();
    }

    public boolean allShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) return false;
        }
        return true;
    }
}
