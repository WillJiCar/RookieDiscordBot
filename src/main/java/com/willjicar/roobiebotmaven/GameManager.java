package com.willjicar.roobiebotmaven;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

class GameManager {

    private ArrayList<TwentyOne> games21;

    GameManager()
    {
        games21 = new ArrayList<>();
    }

    void new21(String name, MessageReceivedEvent event, Player host)
    {
        if(host.isPlaying21()){
            event.getChannel().sendMessage("Leave your current game before starting another one.").queue();
            return;
        }
        else if(name.isEmpty()){
            event.getChannel().sendMessage("Name cannot be blank.").queue();
            return;
        }
        else if(doesGameExist(name)){
            event.getChannel().sendMessage("Game name already exists.").queue();
            return;
        }
        games21.add(new TwentyOne(name, host));
        event.getChannel().sendMessage(host.GetName() + " created a game, type !join " + name + " to join\n" +
                host.GetName() + " starts by entering !start. Anyone is able to leave at any time by entering !21 end").queue();
    }

    void join21(String name, MessageReceivedEvent event, Player player) {
        TwentyOne game = findGameByName(name);
        if(game == null)
        {
            event.getChannel().sendMessage("Game not found.").queue();
            return;
        }
        else if (game.getState() != State.LOBBY)
        {
            event.getChannel().sendMessage("Game has started already.").queue();
            return;
        }
        for(Player newPlayer : game.GetPlayers())
        {
            if(newPlayer.GetID().equals(player.GetID()))
            {
                event.getChannel().sendMessage("You are already in the game.").queue();
                return;
            }
            else if(player.isPlaying21())
            {
                event.getChannel().sendMessage("Please leave your current game before joining a new one.").queue();
                return;
            }
        }
        game.AddPlayer(player);
        event.getChannel().sendMessage(player.GetName() + " joined successfully.\n" +
        game.GetPlayers().size() + " total players, " + game.GetHost().GetName() + " can now start.").queue();
    }

    void start21(Player host, MessageReceivedEvent event)
    {
        if(host.getGame21Name().isEmpty())
        {
            event.getChannel().sendMessage("You are not the host, you cannot start the game").queue();
            return;
        }
        TwentyOne game = findGameByName(host.getGame21Name());
        if(game == null)
        {
            event.getChannel().sendMessage("Game not found.").queue();
            return;
        }
        else if (!game.GetHost().GetID().equals(host.GetID()))
        {
            event.getChannel().sendMessage("You are not the host, you cannot start the game").queue();
            return;
        }
        else if(game.GetPlayers().size() < 2)
        {
            event.getChannel().sendMessage("Not enough players in the game.").queue();
            return;
        }
        game.Start();
        event.getChannel().sendMessage("Game Started! Round " + game.getRound()+ ", " + game.getPersonCurrent().GetName() + "'s turn.\n" +
                "Please enter '!21 n' followed by a number between 1 and 3").queue();
    }

    void next21(Player player, int number, MessageReceivedEvent event)
    {
        TwentyOne game = findGameByName(player.getGame21Name());
        if(game == null)
        {
            event.getChannel().sendMessage("Game not found.").queue();
            return;
        }
        else if(!game.getPersonCurrent().GetID().equals(player.GetID()))
        {
            event.getChannel().sendMessage("It is not your turn.").queue();
            return;
        }
        else if(number < 1 || number > 3)
        {
            event.getChannel().sendMessage("Number out of range").queue();
            return;
        }
        else if(game.getCurrentNumber() + number > 20)
        {
            event.getChannel().sendMessage("Error: result is more than 20").queue();
            return;
        }
        game.Next(number);
        event.getChannel().sendMessage(game.getCurrentNumber() + ", " + game.getPersonCurrent().GetName() + "'s turn, please enter a number between 1 and 3.").queue();
        if(game.getState() == State.PAUSED)
        {
            event.getChannel().sendMessage("21! " + game.getLoser().GetName() + " you have lost.\n" +
                    "Play again? type '!21 reset' to start again, or type '!21 end' to leave").queue();
        }
    }

    void end21(Player player, MessageReceivedEvent event)
    {
        TwentyOne game = findGameByName(player.getGame21Name());
        if(game == null)
        {
            event.getChannel().sendMessage("Game not found.").queue();
            return;
        }
        game.End();
        event.getChannel().sendMessage("Game ended successfully").queue();
        games21.remove(game);
    }

    void leave21(Player player, MessageReceivedEvent event)
    {
        TwentyOne game = findGameByName(player.getGame21Name());
        if(game == null)
        {
            event.getChannel().sendMessage("Game not found.").queue();
            return;
        }
        if(game.GetPlayers().size() < 2 || game.GetHost().GetID().equalsIgnoreCase(player.GetID()))
        {
            end21(player, event);
        }
        else
        {
            game.Leave(player);
            player.Set21GameName("");
            event.getChannel().sendMessage("Left game successfully").queue();
        }
    }

    private TwentyOne findGameByName(String name)
    {
        for(TwentyOne game : games21)
        {
            if(name.equals(game.GetName()))
            {
                return game;
            }
        }
        return null;
    }

    private boolean doesGameExist(String name)
    {
        for (TwentyOne game : games21)
        {
            if(name.equals(game.GetName()))
            {
                return true;
            }
        }
        return false;
    }
}
