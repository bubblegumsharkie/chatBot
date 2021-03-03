package com.mygoodbot;

import com.mygoodbot.logs.CreateFile;
import com.mygoodbot.logs.WriteToFile;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.*;

public class Bot extends TelegramLongPollingBot {

    Map<String, Integer> users = new HashMap<>();

    // fill your info here
    String ownerID = "";
    String apiKey = "";


    public static void main(String[] args) {
        CreateFile.create();
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());

        } catch (TelegramApiRequestException e)  {
            e.printStackTrace();
        }
    }


//    public void saveSticker(Message message)  {
////        SendMessage sendMessage = new SendMessage();
////        sendMessage.setChatId(message.getChatId());
//        SendPhoto sendPhoto = new SendPhoto();
//        sendPhoto.setChatId(message.getChatId());
//        String fileName = "sticker";
//        String ext = "png";
//
//        try {
//            FileOutputStream file = new FileOutputStream("pet.png");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        sendPhoto.setPhoto(image);
//    }

    public void sendMail() {
        // this function will send you your content from message (i.e. text, photo, video, voice etc)
    }

    public void remindMe() {
        // this function will remind you about your content from message (i.e. text, photo, video, voice etc) at selected time
    }

        //this will be connected to a DB later
    public void whoUsedBot(Message message) {
        if (users.containsKey(message.getFrom().getUserName())) {
            users.put(message.getFrom().getUserName(), (users.get(message.getFrom().getUserName()) + 1));
        } else {
            users.put(message.getFrom().getUserName(), 1);
        }
        String reply = users.toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(ownerID);
        sendMessage.setText(reply);
        try {
            execute(sendMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void whoIsThere(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(ownerID);
        long epoch = message.getDate();
        String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(epoch*1000L));
        sendMessage.setText("Somebody just used your bot and the username is @" + message.getFrom().getUserName() + "\n\nthe message was: \n" + message.getText() +"" +
                "\nDate: " + date);
        try {
            execute(sendMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendWasASleep(Message message) {

        if (users.get(message.getFrom().getUserName()) == 1) {


            long epochCurrent = System.currentTimeMillis() / 1000;
            if ((epochCurrent - message.getDate()) > 10) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId().toString());
                sendMessage.setText("Hello! We went offline for a bit but here we are and I will look into all your messages in a millisec or two");
                try {
                    showButtons(sendMessage);
                    execute(sendMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            users.put(message.getFrom().getUserName(), users.get(message.getFrom().getUserName())-1);
        }

    }
    public void sendGif(Message message, String text) {
        SendAnimation sendAnimation = new SendAnimation();
        String gifURL = CryMeALink.gifLinkCreate(text);
        sendAnimation.setAnimation(gifURL);
        WriteToFile.write("the reply is: " + gifURL);
        sendAnimation.setChatId(message.getChatId().toString());
        try {
            execute(sendAnimation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //logging gifs
        SendAnimation sendMessage = new SendAnimation();
        sendMessage.setChatId(ownerID);
        sendMessage.setAnimation(gifURL);
        try {
            execute(sendMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendMsg(Message message, String text) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setReplyToMessageId(message.getMessageId());
            sendMessage.setText(text);
            WriteToFile.write("the reply is: " + text);
            try {
                showButtons(sendMessage);
                execute(sendMessage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void sendRates(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        String reply = RatesToChat.rateMyCurrency(text);
        sendMessage.setText(reply);
        WriteToFile.write("the reply is: " + reply);
        System.out.println("-----------------------------------");
        try {
            showButtons(sendMessage);
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("what can you do? 🙊"));
        keyboardFirstRow.add(new KeyboardButton("send meow 🐱"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public void onUpdateReceived(Update update) {
        whoIsThere(update.getMessage());
        long epoch = update.getMessage().getDate();
        String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(epoch*1000L));
        Message message = update.getMessage();
        whoUsedBot(message);
        sendWasASleep(message);
        WriteToFile.write("\n\n----- new request from: " + message.getFrom().getFirstName() + " -----");
        WriteToFile.write("----- username: " + message.getFrom().getUserName() + " -----");
        System.out.println("----- new request from: " + message.getFrom().getFirstName() + " ------");
        WriteToFile.write("----- made on: " + date + " -----");
        System.out.println("----- made on: " + date);
        System.out.println("-----------------------------------");
        WriteToFile.write("-----------------------------------");
//        if( message.hasSticker()) {
//            saveSticker(message); }
//        else {
            if (message != null) {
                System.out.println("initial msg: " + message.getText());
                WriteToFile.write("initial msg: " + message.getText());
                System.out.println("-----------------------------------");
                System.out.println("Whole info about msg:" + message.toString());
                WriteToFile.writeFull("Whole info about msg:\n" + message.toString() + "\n---------------------------------");
                WriteToFile.write("-----------------------------------");
                System.out.println("-----------------------------------");

                if (message.getText().startsWith("/gif")) {
                    String strGif = message.getText().replaceFirst("/gif ", "");
                    System.out.println("Tag to search is: " + strGif);
                    System.out.println("-----------------------------------");
                    sendGif(message, strGif);
                }

                if (message.getText().startsWith("/rate")) {
                    String strRate = message.getText().replaceFirst("/rate ", "");
                    System.out.println("Rate to look up is: *" + strRate + "*");
                    System.out.println("-----------------------------------");
                    sendRates(message, strRate);
                }

                switch (message.getText()) {
                    case "/start":
                        sendMsg(message, "Hi " + message.getFrom().getFirstName() + "! There are some buttons over here to push and also I can show you a random gif! " +
                                "\n\nJust type /gif *keyword* and I'll do the rest!");
                        break;
                    case "what can you do? 🙊":
                        sendMsg(message, "Wanna see a random gif? Just type /gif and keyword \n\nWanna know rate of RUB in other currencies? Type /rate USD or any other currency  \n\nTo see all available currencies type /ratelist");
                        break;
                    case "send meow 🐱":
                        sendMsg(message, "ok ok meow alright ok meow just chill");
                        break;
                }
            }
//        }
    }

    @Override
    public String getBotUsername() {
        return "thisIsNotACatBot";
    }

    @Override
    public String getBotToken() {
        return apiKey;
    }
}
