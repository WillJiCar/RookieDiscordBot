package com.willjicar.roobiebotmaven;
import java.util.Random;

import static java.lang.Math.round;

public class HigherLower {
    private int[] numbers;
    private boolean gameInPlay;
    private float lowerOdds;
    private float higherOdds;

    public HigherLower()
    {
        numbers = new int[2];
        gameInPlay = false;
    }

    public int fNum()
    {
        return numbers[0];
    }   //63 , Play , 78

    public  int sNum()
    {
        return  numbers[1];
    } //78 Play 38

    public  boolean isGameInPlay()
    {
        return  gameInPlay;
    }

    public int GenerateNumber ()
    {
        Random rand = new Random();
        return rand.nextInt(100) + 1;
    }

    public  int GenerateNumberFixed(int min, int max)
    {
        Random rand = new Random();
        return rand.nextInt(max + 1 - min) + min;
    }

    public void Init()
    {
        gameInPlay = true;
        numbers[0] = GenerateNumber();  //63
        numbers[1] = GenerateNumber();  //78
        GenOdds(numbers[0]);
    }

    public  void Next(boolean fixed)
    {
        if (fixed)
        {
            numbers[1] = GenerateNumberFixed(0, numbers[0]);
        }
        else
        {
            numbers[0] = numbers[1];    //78
            numbers[1] = GenerateNumber();  //38
            GenOdds(numbers[0]);
        }
    }

    private void GenOdds(float number)
    {
        lowerOdds = Math.round(((1f / (number / 100f)) - 1f) * 10f) / 10f;
        higherOdds = Math.round(((1f / ((100f - number) / 100f)) - 1f) * 10f) / 10f;
    }

    public String GetOdds()
    {
        if(lowerOdds > 1)
            return "Lower Odds: " + lowerOdds+"/1";
        else
            return "Higher Odds: " + higherOdds+"/1";
    }

    public float GetOddsFloat()
    {
        if(lowerOdds > 1)
            return lowerOdds;
        else
            return higherOdds;
    }

    public float GetHigherOdds()
    {
        return higherOdds;
    }

    public float GetLowerOdds()
    {
        return lowerOdds;
    }
}
