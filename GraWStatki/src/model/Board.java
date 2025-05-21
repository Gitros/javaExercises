package model;

public class Board {
    private char[][] grid; // 'O' = puste, 'S' = statek, 'X' = trafienie, '-' = pudło

    public Board() {
        grid = new char[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                grid[i][j] = 'O';
    }

    public boolean placeShip(int x, int y, int size, boolean horizontal) {
        // Sprawdzenie kolizji
        if (horizontal) {
            if (x + size > 10) return false;
            for (int i = x; i < x + size; i++)
                if (grid[y][i] != 'O') return false;
            for (int i = x; i < x + size; i++)
                grid[y][i] = 'S';
        } else {
            if (y + size > 10) return false;
            for (int i = y; i < y + size; i++)
                if (grid[i][x] != 'O') return false;
            for (int i = y; i < y + size; i++)
                grid[i][x] = 'S';
        }
        return true;
    }

    public boolean fireAt(int x, int y) {
        if (grid[y][x] == 'S') {
            grid[y][x] = 'X';
            return true;
        } else if (grid[y][x] == 'O') {
            grid[y][x] = '-';
        }
        return false;
    }

    public char getCell(int x, int y) {
        return grid[y][x];
    }
}
