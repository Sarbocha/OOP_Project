package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * Manages persistent win/loss records for players.
 * Format (CSV): playerName,wins,losses
 * Responsibilities:
 *  - load and save scoreboard (atomic save)
 *  - basic record operations
 *  - rename/remove utilities for safe migration of names
 */
public class Scoreboard {

    private static final String FILE = "scoreboard.txt";
    private final Map<String, Integer> wins = new HashMap<>();
    private final Map<String, Integer> losses = new HashMap<>();

    public Scoreboard() {
        load();
    }

    /* ---------------------
       Basic access & updates
       --------------------- */

    public int getWins(String player) {
        return wins.getOrDefault(validName(player), 0);
    }

    public int getLosses(String player) {
        return losses.getOrDefault(validName(player), 0);
    }

    public void addWin(String player) {
        String p = validName(player);
        wins.put(p, getWins(p) + 1);
    }

    public void addLoss(String player) {
        String p = validName(player);
        losses.put(p, getLosses(p) + 1);
    }

    /**
     * Set record explicitly (overwrites).
     */
    public void setRecord(String player, int winCount, int lossCount) {
        String p = validName(player);
        wins.put(p, Math.max(0, winCount));
        losses.put(p, Math.max(0, lossCount));
    }

    /**
     * Remove a player from the scoreboard entirely.
     */
    public void removePlayer(String player) {
        String p = validName(player);
        wins.remove(p);
        losses.remove(p);
    }

    /**
     * Rename a player (migrate stats). If the target name already exists,
     * the stats are merged by summing wins/losses.
     */
    public void renamePlayer(String oldName, String newName) {
        String o = validName(oldName);
        String n = validName(newName);
        if (o.equals(n)) return;

        int oWins = getWins(o);
        int oLosses = getLosses(o);

        if (oWins == 0 && oLosses == 0) {
            // nothing to migrate; just ensure new exists
            if (!wins.containsKey(n) && !losses.containsKey(n)) {
                wins.put(n, 0);
                losses.put(n, 0);
            }
            removePlayer(o);
            return;
        }

        // merge into new
        int newWins = getWins(n) + oWins;
        int newLosses = getLosses(n) + oLosses;
        setRecord(n, newWins, newLosses);

        // remove old entry
        removePlayer(o);
    }

    /* ---------------------
       Save / Load (IO)
       --------------------- */

    /**
     * Atomic save: write to temp file then replace.
     */
    public synchronized void save() {
        File target = new File(FILE);
        File temp = new File(FILE + ".tmp");

        try (PrintWriter out = new PrintWriter(new FileWriter(temp))) {
            // write players sorted alphabetically for determinism
            for (String player : getSortedPlayers()) {
                out.printf("%s,%d,%d%n", player, getWins(player), getLosses(player));
            }
        } catch (IOException e) {
            System.err.println("Failed to write scoreboard: " + e.getMessage());
            e.printStackTrace();
            // delete temp if exists (best-effort cleanup)
            if (temp.exists()) temp.delete();
            return;
        }

        // Replace target with temp atomically where possible
        try {
            Files.move(temp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException ex) {
            // If atomic move not supported, fall back to non-atomic replace
            try {
                Files.copy(temp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                temp.delete();
            } catch (IOException e) {
                System.err.println("Failed to replace scoreboard file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Load scoreboard from file; ignores malformed lines.
     */
    public final synchronized void load() {
        wins.clear();
        losses.clear();
        File f = new File(FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                // expected CSV: name,wins,losses
                String[] parts = line.split(",", -1);
                if (parts.length != 3) continue;
                String name = parts[0].trim();
                try {
                    int w = Math.max(0, Integer.parseInt(parts[1].trim()));
                    int l = Math.max(0, Integer.parseInt(parts[2].trim()));
                    if (!name.isEmpty()) {
                        wins.put(name, w);
                        losses.put(name, l);
                    }
                } catch (NumberFormatException ignore) {
                    // skip malformed numeric lines
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read scoreboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* ---------------------
       Utility helpers
       --------------------- */

    private String validName(String name) {
        if (name == null) return "";
        return name.trim();
    }

    /**
     * Returns players sorted alphabetically.
     * Useful for deterministic saves and basic displays.
     */
    public List<String> getSortedPlayers() {
        List<String> list = new ArrayList<>();
        // include players present in either map
        Set<String> keys = new HashSet<>();
        keys.addAll(wins.keySet());
        keys.addAll(losses.keySet());
        list.addAll(keys);
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    /**
     * Returns players sorted by wins descending, then losses ascending, then name.
     * Useful for top-player displays.
     */
    public List<String> getTopPlayers() {
        List<String> list = getSortedPlayers();
        list.sort((a, b) -> {
            int dw = Integer.compare(getWins(b), getWins(a)); // desc wins
            if (dw != 0) return dw;
            int dl = Integer.compare(getLosses(a), getLosses(b)); // asc losses
            if (dl != 0) return dl;
            return a.compareToIgnoreCase(b);
        });
        return list;
    }
}
