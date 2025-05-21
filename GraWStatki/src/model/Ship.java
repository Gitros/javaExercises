package model;

public class Ship {
    private int size;
    private boolean[] hits;

    public Ship(int size) {
        this.size = size;
        this.hits = new boolean[size];
    }

    public int getSize() {
        return size;
    }

    public void hit(int index) {
        if (index >= 0 && index < size) {
            hits[index] = true;
        }
    }

    public boolean isSunk() {
        for (boolean hit : hits) {
            if (!hit) return false;
        }
        return true;
    }
}
