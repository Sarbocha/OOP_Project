package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Scoreboard {
    private static final String FILE = "scoreboard.txt";
    private final Map<String, Integer> wins = new HashMap<>();
    private final Map<String, Integer> losses = new HashMap<>();

    public Scoreboard() { load(); }

    public void addWin(String player) { wins.put(player, wins.getOrDefault(player, 0) + 1); }
    public void addLoss(String player) { losses.put(player, losses.getOrDefault(player, 0) + 1); }

    public int getWins(String player) { return wins.getOrDefault(player, 0); }
    public int getLosses(String player) { return losses.getOrDefault(player, 0); }

    public void save() {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE))) {
            for (String player : wins.keySet()) {
                out.println(player + " W:" + wins.get(player) + " L:" + losses.getOrDefault(player, 0));
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void load() {
        File f = new File(FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" W:| L:");
                if (parts.length == 3) {
                    wins.put(parts[0], Integer.parseInt(parts[1]));
                    losses.put(parts[0], Integer.parseInt(parts[2]));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}
