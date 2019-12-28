package com.willjicar.roobiebotmaven;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    private String ID;
    private String name;
    private double balance;
    private boolean playing21;
    private String game21Name;
    private String currentHangmanID;

    public Player(String ID, String name)
    {
        //ID = Name+#+Number
        this.ID = ID;
        this.name = name;
        balance = 2000;
        playing21 = false;
    }

    public String GetID()
    {
        return ID;
    }

    public void UpdateBalance(float amount)
    {
        balance += amount;
    }

    public float getBalance() {
         return Math.round(balance * 100) / 100;
    }

    public void SetBalance(float amount)
    {
        balance = amount;
    }

    public String GetName()
    {
        return name;
    }

    public void ResetBalance(){
        balance = 100;
    }

    public int compareTo(Player o) {
        if(balance < o.getBalance())
            return -1;
        else if(o.getBalance() < this.getBalance())
            return 1;
        return 0;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        int compareBalance = (int)((Player)o).getBalance();
        return compareBalance - (int)getBalance();
    }

    public boolean isPlaying21()
    {
        return playing21;
    }

    public void Set21GameName(String name)
    {
        game21Name = name;
        if(name.equals(""))
            playing21 = false;
        else
            playing21 = true;
    }

    public String getGame21Name()
    {
        return game21Name;
    }

    public void setCurrentHangmanID(String ID)
    {
            currentHangmanID = ID;
    }

    String getCurrentHangmanID() {
        return currentHangmanID;
    }
}
