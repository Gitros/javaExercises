package model;

import java.util.*;

public class Board {
    private int size;
    private Map<Coordinate, CellState> grid;
    private List<Ship> ships;

    public Board(int size) {
        this.size = size;
        grid = new HashMap<>();
        ships = new ArrayList<>();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid.put(new Coordinate(row, col), CellState.EMPTY);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public void markShot(Coordinate c, boolean hit) {
        grid.put(c, hit ? CellState.HIT : CellState.MISS);
    }

    public CellState getCellState(Coordinate coord) {
        return grid.getOrDefault(coord, CellState.EMPTY);
    }

    public boolean placeShip(Ship ship) {
        for (Coordinate coord : ship.getPositions()) {
            if (!grid.containsKey(coord) || grid.get(coord) != CellState.EMPTY) {
                return false; // Kolizja lub poza planszą
            }
        }
        // Jeśli miejsce wolne, umieszczamy statek
        for (Coordinate coord : ship.getPositions()) {
            grid.put(coord, CellState.SHIP);
        }
        ships.add(ship);
        return true;
    }

    public boolean receiveShot(Coordinate coord) {
        if (!grid.containsKey(coord)) return false;

        CellState current = grid.get(coord);
        if (current == CellState.HIT || current == CellState.MISS) return false; // Już strzelone

        if (current == CellState.SHIP) {
            grid.put(coord, CellState.HIT);
            for (Ship ship : ships) {
                if (ship.getPositions().contains(coord)) {
                    ship.hit();
                    break;
                }
            }
            return true;
        } else {
            grid.put(coord, CellState.MISS);
            return false;
        }
    }

    public boolean allShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) return false;
        }
        return true;
    }

    public void display() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Coordinate coord = new Coordinate(row, col);
                CellState state = grid.get(coord);
                switch (state) {
                    case SHIP -> System.out.print("S ");
                    case HIT -> System.out.print("X ");
                    case MISS -> System.out.print("o ");
                    default -> System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}
