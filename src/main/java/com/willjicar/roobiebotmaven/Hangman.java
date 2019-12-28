package com.willjicar.roobiebotmaven;

import org.jetbrains.annotations.NotNull;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class Hangman {

    private String word;
    private ArrayList<Character> letters;
    private String display;
    private ArrayList<Character> dList;
    private Player player;
    private String ID;
    private int guesses;
    private int left;
    private State state;

    Hangman(String word, @NotNull Player player)
    {
        state = State.STARTED;
        display = "";
        dList = new ArrayList<>();
        this.word = word;
        this.player = player;
        letters = wordToLetters(word);
        guesses = 10;
        left = letters.size();
        this.ID = UUID.randomUUID().toString();
        player.setCurrentHangmanID(ID);
    }

    ArrayList<Character> wordToLetters(String str)
    {
        display = "";
        ArrayList<Character> characters = new ArrayList<>();
        for(char c : str.toCharArray())
        {
            characters.add(c);
            dList.add('-');
        }
        display = dList.toString();
        return characters;
    }

    void updateDisplay(char c, int index)
    {
        dList.set(index, c);
        letters.set(index, '+');
        display = dList.toString();
        left--;
    }

    boolean Guess(char c)
    {
        int correct = 0;
        guesses--;
        for(char ch : letters)
        {
            if(ch == c)
            {
                correct++;
                updateDisplay(c, letters.indexOf(ch));
            }
        }
        if(correct > 0)
        {
            if(left == 0)
            {
                state = State.WON;
                End();
            }
            return true;
        }
        else
        {
            if(guesses == 0)
            {
                state = State.LOST;
                End();
            }
            return false;
        }
    }

    boolean Guess(String word)
    {
        guesses--;
        if(word.equalsIgnoreCase(this.word))
        {
            state = State.WON;
            End();
            return true;
        }
        else
        {
            if(guesses == 0)
            {
                state = State.LOST;
                End();
            }
            return false;
        }
    }

    private void End()
    {
        player.setCurrentHangmanID("");
    }

    String getDisplay()
    {
        return display;
    }

    Player getPlayer()
    {
        return player;
    }

    String getID()
    {
        return ID;
    }

    int getGuesses()
    {
        return guesses;
    }

    State getState()
    {
        return state;
    }

    String getWord()
    {
        return word;
    }
}
