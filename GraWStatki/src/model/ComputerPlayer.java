package model;

import java.util.Random;

public class ComputerPlayer extends Player {
    private Random random = new Random();

    @Override
    public int[] makeMove() {
        return new int[]{random.nextInt(10), random.nextInt(10)};
    }
}
