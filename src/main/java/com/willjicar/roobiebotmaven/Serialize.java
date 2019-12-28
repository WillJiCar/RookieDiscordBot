package com.willjicar.roobiebotmaven;

import java.io.*;
import java.util.ArrayList;

public class Serialize {

    public ArrayList<Player> DeserializePlayers()
    {
        ArrayList<Player> players = null;   //Inits null araylist
        try
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("players.txt")); //New input stream created from team file
            players = (ArrayList<Player>)in.readObject();   //Loads arraylist in file to System array list
            in.close(); //Closes stream
        }
        catch(Exception e) {
            e.printStackTrace();}   //Prints exception to console
        return players;
    }

    public void SerializePlayers(ArrayList<Player> players)
    {
        try {
            FileOutputStream fileOut = new FileOutputStream("players.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(players);
            out.close();
            fileOut.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
