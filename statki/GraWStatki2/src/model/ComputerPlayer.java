package model;

import java.util.*;

public class ComputerPlayer extends Player {

    private final List<Coordinate> remainingShots = new ArrayList<>();
    private final Set<Coordinate> triedShots = new HashSet<>();
    private final Queue<Coordinate> targetQueue = new LinkedList<>();
    private final Random rng = new Random();

    public ComputerPlayer(String name, int boardSize) {
        super(name, boardSize);
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                remainingShots.add(new Coordinate(r, c));
            }
        }
    }

    @Override
    public Coordinate makeMove() {
        Coordinate coord;
        if (!targetQueue.isEmpty()) {
            coord = targetQueue.poll();
        } else {
            int idx = rng.nextInt(remainingShots.size());
            coord = remainingShots.remove(idx);
        }
        triedShots.add(coord);
        return coord;
    }

    @Override
    public void handleShotResult(Coordinate coord, boolean wasHit) {
        super.handleShotResult(coord, wasHit);
        if (wasHit) {
            for (Coordinate neighbor : getAdjacent(coord)) {
                if (!triedShots.contains(neighbor)) {
                    targetQueue.add(neighbor);
                }
            }
        }
    }

    private List<Coordinate> getAdjacent(Coordinate coord) {
        List<Coordinate> adj = new ArrayList<>();
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] d : dirs) {
            int r = coord.getRow() + d[0];
            int c = coord.getCol() + d[1];
            if (r >= 0 && r < ownBoard.getSize() && c >= 0 && c < ownBoard.getSize()) {
                Coordinate neighbor = new Coordinate(r, c);
                if (!triedShots.contains(neighbor)) {
                    adj.add(neighbor);
                }
            }
        }
        return adj;
    }

    @Override
    public void placeShips(List<Ship> fleet) {
        Random rand = new Random();
        for (Ship s : fleet) {
            boolean placed = false;
            while (!placed) {
                boolean horizontal = rand.nextBoolean();
                int row = rand.nextInt(ownBoard.getSize());
                int col = rand.nextInt(ownBoard.getSize());
                List<Coordinate> coords = new ArrayList<>();
                for (int i = 0; i < s.getLength(); i++) {
                    int r = row + (horizontal ? 0 : i);
                    int c = col + (horizontal ? i : 0);
                    coords.add(new Coordinate(r, c));
                }
                s.setPositions(coords);
                placed = ownBoard.placeShip(s);
            }
        }
    }
}
