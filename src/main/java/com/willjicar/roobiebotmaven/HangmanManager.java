package com.willjicar.roobiebotmaven;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

class HangmanManager {

    private ArrayList<String> words = new ArrayList<>();
    private ArrayList<Hangman> games = new ArrayList<>();
    /*
    Enter !hangman to start, it makes new game and picks a random word
    Display words as _ _ _ _ _ and say how many tries left
        Split word into char array, then make array display with amount of letters
    !hangman x = letter, loop through word to see

     */

    HangmanManager() throws IOException {
        File file = new File("words.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null)
        {
            words.add(st);
        }
    }

    void Start(Player player, MessageReceivedEvent event)
    {
        //create new game
        games.add(new Hangman(chooseRandomWord(), player));
        Hangman game = findGameByID(player.getCurrentHangmanID());
        if(game == null)
        {
            event.getChannel().sendMessage("Game not found.").queue();
            return;
        }
        event.getChannel().sendMessage("New Game of Hangman started!\n" +
                game.getDisplay() + " " + game.getGuesses() + " guesses left.").queue();
    }

    void Guess(char c, Player player, MessageReceivedEvent event)
    {
        Hangman game = findGameByID(player.getCurrentHangmanID());
        if(game == null)
        {
            event.getChannel().sendMessage("Game not found.").queue();
            return;
        }
        if(game.Guess(c))
        {
            //If true, print
            if(game.getState() == State.WON)
            {
                event.getChannel().sendMessage("Congratulations! " + game.getDisplay() + " You guessed correctly with " + game.getGuesses() + " left.").queue();
                return;
            }
            event.getChannel().sendMessage("Correct! " + game.getDisplay() + " " + game.getGuesses() + " guesses left.").queue();
        }
        else
        {
            if(game.getState() == State.LOST)
            {
                event.getChannel().sendMessage("Unlucky! No more guesses left. The word was " + game.getWord()).queue();
                return;
            }
            event.getChannel().sendMessage("Wrong! " + game.getDisplay() + " Only " + game.getGuesses() + " guesses left.").queue();
        }
    }

    void Guess(String word, Player player, MessageReceivedEvent event)
    {
        Hangman game = findGameByID(player.getCurrentHangmanID());
        if(game == null)
        {
            event.getChannel().sendMessage("Game not found.").queue();
            return;
        }
        if(game.Guess(word))
        {
            event.getChannel().sendMessage("Congratulations! " + game.getWord() + " You guessed correctly with " + game.getGuesses() + " left.").queue();
        }
        else
        {
            if(game.getState() == State.LOST)
            {
                event.getChannel().sendMessage("Unlucky! No more guesses left. The word was " + game.getWord()).queue();
                return;
            }
            event.getChannel().sendMessage("Wrong! " + game.getDisplay() + " Only " + game.getGuesses() + " guesses left.").queue();
        }
    }

    private String chooseRandomWord()
    {
        Random rand = new Random();
        return words.get(rand.nextInt(words.size()));
    }

    private Hangman findGameByID(String ID)
    {
        for(Hangman hm : games)
        {
            if(ID.equals(hm.getID()))
            {
                return hm;
            }
        }
        return null;
    }
}
