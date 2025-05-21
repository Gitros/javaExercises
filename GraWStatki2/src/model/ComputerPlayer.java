package model;

import java.util.*;

public class ComputerPlayer extends Player {

    private final List<Coordinate> remainingShots; // pola, których jeszcze nie próbowaliśmy
    private final Random rng = new Random();

    public ComputerPlayer(String name, int boardSize) {
        super(name, boardSize);
        remainingShots = new ArrayList<>();
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                remainingShots.add(new Coordinate(r, c));
            }
        }
    }

    @Override
    public Coordinate makeMove() {
        // ✨ najprostszy poziom trudności – losowy wybór
        int idx = rng.nextInt(remainingShots.size());
        return remainingShots.remove(idx);
    }

    @Override
    public void handleShotResult(Coordinate coord, boolean wasHit) {
        super.handleShotResult(coord, wasHit);

        // 🔜 później tu można dodać inteligencję:
        // jeśli trafiliśmy, to zaplanuj strzały obok itp.
    }

    @Override
    public void placeShips(List<Ship> fleet) {
        // Losowe, aż się uda.  Bardzo uproszczone – na początek OK.
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
                s.setPositions(coords);          // metoda w Ship – ustawia współrzędne
                placed = ownBoard.placeShip(s);  // próba wstawienia
            }
        }
    }
}
