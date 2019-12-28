package com.willjicar.roobiebotmaven;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TimerTask;

public final class Storage extends TimerTask {

    private final Serialize serialize;
    private final ArrayList<Player> players;
    private final ArrayList<String> playerIDs;

    public Storage() {
        players = new ArrayList<>();
        playerIDs = new ArrayList<>();
        serialize = new Serialize();
        LoadObjectsFromFile();
    }

    @Override
    public void run()
    {
        SerializeObjects();
    }

    public void SerializeObjects()
    {
        serialize.SerializePlayers(players);
    }

    public void LoadObjectsFromFile(){
        if(FileNotNull("players"))
        {
            players.addAll(serialize.DeserializePlayers());
            for(Player player : players)
            {
                playerIDs.add(player.GetID());
            }
        }
    }

    private boolean FileNotNull(String fileName){
        File tmpDir = new File(fileName+".txt");
        return tmpDir.exists();
    }

    public ArrayList<Player> GetPlayers()
    {
        return players;
    }

    public void AddPlayer(String ID, String name)
    {
        //Check to see if already in list, then add
        if (!playerIDs.contains(ID)) {
            players.add(new Player(ID, name));
        }
    }

    public Player GetPlayerByID(String ID)
    {
        for(Player player : players)
        {
            if(player.GetID().equalsIgnoreCase(ID))
            {
                return player;
            }
        }
        return null;
    }

    public ArrayList<Player> sortedPlayers()
    {
        Collections.sort(players);
        return players;
    }
}
