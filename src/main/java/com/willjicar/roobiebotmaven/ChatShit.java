package com.willjicar.roobiebotmaven;
import java.util.ArrayList;

public class ChatShit {

    ArrayList<String> insults;

    public ChatShit() {
        insults = new ArrayList<>();
    }

    public void AddInsult(String insult)
    {
        insults.add(insult);
    }

    public String GetInsult(int index)
    {
        return insults.get(index);
    }
}
