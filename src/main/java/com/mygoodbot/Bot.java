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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

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
//        keyboardFirstRow.add(new KeyboardButton("list of currencies"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public void onUpdateReceived(Update update) {
        int epoch = update.getMessage().getDate();
        String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(epoch*1000L));
        Message message = update.getMessage();

        WriteToFile.write("\n\n----- new request from: " + message.getFrom().getFirstName() + " -----");
        WriteToFile.write("----- username: " + message.getFrom().getUserName() + " -----");
        System.out.println("----- new request from: " + message.getFrom().getFirstName() + " ------");
        WriteToFile.write("----- made on: " + date + " -----");
        System.out.printf("----- made on: " + date);
        System.out.println("-----------------------------------");
        WriteToFile.write("-----------------------------------");
        if (message !=null) {
            System.out.println("initial msg: " + message.getText());
            WriteToFile.write("initial msg: " + message.getText());
            System.out.println("-----------------------------------");
            System.out.println("Whole info about msg:" + message.toString());
            WriteToFile.writeFull("Whole info about msg:\n" + message.toString() +"\n---------------------------------");
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
                case "what can you do? 🙊" :
                    sendMsg(message, "Wanna see a random gif? Just type /gif and keyword \n\nWanna know rate of RUB in other currencies? Type /rate USD or any other currency  \n\nTo see all available currencies type /ratelist");
                    break;
                case "send meow 🐱" :
                    sendMsg(message, "ok ok meow alright ok meow just chill");
                    break;
//
//                case "list of currencies" :
//                    sendMsg(message, "rates");
//                    break;


            }
        }
    }

    @Override
    public String getBotUsername() {
        return "thisIsNotACatBot";
    }

    @Override
    public String getBotToken() {
        return "";
    }
}
