package model;

import java.util.List;

public class Ship {
    private List<Coordinate> positions;
    private int hitsTaken;

    public Ship(List<Coordinate> positions) {
        this.positions = positions;
        this.hitsTaken = 0;
    }
    public int getLength() { return positions.size(); }

    public void setPositions(List<Coordinate> coords) {
        this.positions = coords;
        this.hitsTaken = 0;
    }


    public List<Coordinate> getPositions() {
        return positions;
    }

    public void hit(Coordinate coord) {
        hitsTaken++;
    }

    public boolean isSunk() {
        return hitsTaken >= positions.size();
    }

    public boolean contains(Coordinate coord) {
        return positions.contains(coord);
    }
}
