package edu.calvin.cs262.homework2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Player data holder.
 */
public class Player {

    private int id;
    private String email;
    private String name;

    public Player(int id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return "" + id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
