package com.willjicar.roobiebotmaven;
import java.util.ArrayList;

enum State{
    LOBBY, STARTED, ENDED, PAUSED, WON, LOST
}

class TwentyOne {
    /*
    TODO: Leave Game.
     */

    private String name;
    private ArrayList<Player> players;
    private Player host;
    private State state;
    private int round;
    private int currentNumber;
    private Player loser;
    private Player personCurrent;
    private int index;

    TwentyOne(String name, Player host)
    {
        players = new ArrayList<>();
        state = State.LOBBY;
        round = 0;
        this.name = name;
        this.host = host;
        players.add(host);
        host.Set21GameName(name);
        index = 0;
    }

    void AddPlayer(Player player)
    {
        players.add(player);
        player.Set21GameName(name);
    }

    void Start()
    {
        if(state != State.STARTED)
        {
            round++;
            state = State.STARTED;
            index = 0;
            currentNumber = 0;
            personCurrent = players.get(index);
        }
    }

    void Next(int number)
    {
        if(state == State.STARTED)
        {
            currentNumber += number;
            index++;
            personCurrent = players.get(index);
            if(index == players.size() - 1)
            {
                index = -1;
            }
            if(currentNumber == 20)
            {
                state = State.PAUSED;
                loser = personCurrent;
            }

        }
    }

    void End()
    {
        state = State.ENDED;
        for(Player player : players)
        {
            Leave(player);
        }
    }

    void Leave(Player player)
    {
        player.Set21GameName("");
        players.remove(player);
    }

    Player getPersonCurrent()
    {
        return personCurrent;
    }

    int getRound()
    {
        return round;
    }

    String GetName()
    {
        return name;
    }

    ArrayList<Player> GetPlayers()
    {
        return players;
    }

    Player GetHost()
    {
        return host;
    }

    State getState()
    {
        return state;
    }

    int getCurrentNumber()
    {
        return currentNumber;
    }

    Player getLoser()
    {
        return loser;
    }
}
