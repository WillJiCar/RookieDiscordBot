/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.willjicar.roobiebotmaven;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import com.rookiebot.sentence.RandomSentence;
/**
 *
 * @author William
 */
public class RookieBot extends ListenerAdapter {
    
    /*
    TO DO:
    upload all this to GitHub,
    Use CodeDeploy to upload to github
    */
    
    private HigherLower hl;
    private ChatShit chatShit;
    private static Storage storage;
    private GameManager gameManager;
    private HangmanManager hangmanManager;
    
    private RookieBot(){
        hl = new HigherLower();
        chatShit = new ChatShit();
        storage = new Storage();
        gameManager = new GameManager();
        try {
            hangmanManager = new HangmanManager();
        } catch (IOException ex) {
            Logger.getLogger(RookieBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws LoginException {
        // TODO code application logic here
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "NTI2MDc1OTIxMzIxNjg5MDk5.Dv_6IQ.0kyCaywCYt4D-_fywaXu7WWjGZ8";
        builder.setToken(token);
        builder.addEventListener(new RookieBot());
        builder.build();
        Scanner s = new Scanner(System.in);
        Timer timer = new Timer();
        timer.schedule(storage, 0, 10000);
        if(s.next().equalsIgnoreCase("close"))
        {
            storage.SerializeObjects();
            System.exit(0);
        }
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        String message = event.getMessage().getContentRaw();

        if (!event.getAuthor().isBot()) {
            Player player = storage.GetPlayerByID(event.getAuthor().getId());
            if(message.equalsIgnoreCase("!help"))
            {
                event.getChannel().sendMessage("Rookie Bot Version 0.03 December 2019\n" +
                        "Please enter '!help'' followed by a category: gambling.").queue();
            }
            if(message.equalsIgnoreCase("!help gambling"))
            {
                event.getChannel().sendMessage("!balance - Check your Balance\n" +
                        "!higherlower - Start a game of Higher Lower\n" +
                        "!higher or !lower - The next guess for non-monetary play.\n" +
                        "!gamble - Has to be followed with an amount and then the guess.\n" +
                        "!skip - Skip the current number to guess to try and improve your odds.\n" +
                        "!leaderboard - Shows the top 10 players in the server.").queue();
            }
            if (message.equalsIgnoreCase("!higherlower")) {
                event.getChannel().sendMessage("Enter '!higher' or '!lower'").queue();
                hl.Init();
                event.getChannel().sendMessage(hl.fNum() + " " + hl.GetOdds()).queue();
            }
            if (message.equalsIgnoreCase("!higher") && hl.isGameInPlay()) {
                Play(true, event, false, 0);
            }
            if (message.equalsIgnoreCase("!lower") && hl.isGameInPlay()) {
                Play(false, event, false, 0);
            }
            if(message.equalsIgnoreCase("!lukas"))
            {
                String[] arr = message.split(" ", 2);
                event.getChannel().sendMessage(chatShit.GetInsult(Integer.parseInt(arr[1]))).queue();
            }

            if(args[0].equalsIgnoreCase("!insult"))
            {
                String[] arr = message.split(" ", 2);
                chatShit.AddInsult(arr[1]);
                event.getChannel().sendMessage("Added " + arr[1]).queue();
            }

            if(args[0].equalsIgnoreCase("!gamble") && args[2].equalsIgnoreCase("higher"))
            {
                Play(true, event, true, Float.valueOf(args[1]));
            }
            if(args[0].equalsIgnoreCase("!gamble") && args[2].equalsIgnoreCase("lower"))
            {
                Play(false, event, true, Float.valueOf(args[1]));
            }
            if(message.equalsIgnoreCase("!skip"))
            {
                hl.Next(false);
                event.getChannel().sendMessage(hl.fNum() + " " + hl.GetOdds()).queue();
            }
            if(message.equalsIgnoreCase("!balance"))
            {
                event.getChannel().sendMessage(player.GetName() + " £" + player.getBalance()).queue();
            }
            if(message.equalsIgnoreCase("!leaderboard"))
            {
                int i = 0;
                String newMessage = "";
                for(Player lPlayer : storage.sortedPlayers())
                {
                    i++;
                    if(i <= 10)
                    {
                        newMessage += "#"+i+" - "+ lPlayer.GetName()+" - £"+ lPlayer.getBalance()+"\n";
                    }
                }
                event.getChannel().sendMessage(newMessage).queue();
            }
            if(message.equalsIgnoreCase("!reset"))
            {
                player.ResetBalance();
                event.getChannel().sendMessage("Balance reset to £100").queue();
            }
            if(args[0].equalsIgnoreCase("!21") && args[1].equalsIgnoreCase("new") && args.length > 2)
            {
                gameManager.new21(args[2], event, player);
            }
            if(args[0].equalsIgnoreCase("!join") && args.length > 1)
            {
                gameManager.join21(args[1], event, player);
            }
            if(args[0].equalsIgnoreCase("!start"))
            {
                gameManager.start21(player, event);
            }
            if(args[0].equalsIgnoreCase("!21") && args[1].equalsIgnoreCase("n") && args.length > 2)
            {
                gameManager.next21(player, Integer.valueOf(args[2]), event);
            }
            if(message.equalsIgnoreCase("!21 reset"))
            {
                gameManager.start21(player, event);
            }
            if(message.equalsIgnoreCase("!21 end"))
            {
                gameManager.end21(player, event);
            }
            if(message.equalsIgnoreCase("!leave"))
            {
                gameManager.leave21(player, event);
            }
            if(message.equalsIgnoreCase(("!hangman")))
            {
                hangmanManager.Start(player, event);
            }
            if(args[0].equalsIgnoreCase("!hm") && args[1].length() == 1)
            {
                hangmanManager.Guess(args[1].charAt(0), player, event);
            }
            if(args[0].equalsIgnoreCase("!hm") && args[1].length() > 1)
            {
                hangmanManager.Guess(args[1], player, event);
            }
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        List<User> users = event.getJDA().getUsers();
        for (User user : users) {
            storage.AddPlayer(user.getId(), user.getName());
            storage.GetPlayerByID(user.getId()).Set21GameName("");
        }
    }

    private void Play(boolean higher, MessageReceivedEvent event, boolean gamble, float amount)
    {
        Player player = storage.GetPlayerByID(event.getAuthor().getId());
        if((player.getBalance() - amount) < 0 || amount < 0)
        {
            event.getChannel().sendMessage("You do not have sufficient funds.").queue();
            return;
        }
        float tmpOdds = hl.GetOddsFloat();
        float tmpHighOdds = hl.GetHigherOdds();
        float tmpLowOdds = hl.GetLowerOdds();
        int tmpFirst = hl.fNum();
        hl.Next(false); //78, 38
        event.getChannel().sendMessage(hl.fNum() + " " + hl.GetOdds()).queue();
        amount *= tmpOdds;
        if(higher)
        {
            if (hl.fNum() > tmpFirst) {
                if(gamble)
                {
                    if(tmpHighOdds > 1) //Maybe it has to be less than 1??
                    {
                        event.getChannel().sendMessage("Congrats " + player.GetName() + "!").queue();
                        player.UpdateBalance(amount);
                        event.getChannel().sendMessage("You won " + amount + "! New Balance: " + player.getBalance()).queue();
                    }
                    else
                    {
                        event.getChannel().sendMessage("You must bet against the odds or type !skip").queue();
                    }
                }
                else
                    event.getChannel().sendMessage("Congrats " + player.GetName() + "!").queue();
            } else {
                event.getChannel().sendMessage("Wrong").queue();
                if(gamble)
                {
                    player.UpdateBalance(-amount);
                    if(player.getBalance() <= 0)
                        player.SetBalance(0);
                    event.getChannel().sendMessage("You lost " + -amount + ". New Balance: " + player.getBalance()).queue();
                }
            }
        }
        if(!higher)
        {
            if (hl.fNum() < tmpFirst) {
                if(gamble)
                {
                    if(tmpLowOdds > 1)
                    {
                        event.getChannel().sendMessage("Congrats " + event.getAuthor().getName() + "!").queue();
                        player.UpdateBalance(amount);
                        event.getChannel().sendMessage("You won " + amount + "! New Balance: " + player.getBalance()).queue();
                    }
                    else
                        event.getChannel().sendMessage("You must bet against the odds or type !skip").queue();
                }
                else
                    event.getChannel().sendMessage("Congrats " + event.getAuthor().getName() + "!").queue();
            } else {
                event.getChannel().sendMessage("Wrong").queue();
                if(gamble)
                {
                    player.UpdateBalance(-amount);
                    if(player.getBalance() <= 0)
                        player.SetBalance(0);
                    event.getChannel().sendMessage("You lost " + -amount + ". New Balance: " + player.getBalance()).queue();
                }
            }
        }
    }
}
