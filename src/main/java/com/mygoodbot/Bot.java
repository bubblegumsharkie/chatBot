package com.mygoodbot;

import com.google.common.xml.XmlEscapers;
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
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) {
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

        keyboardFirstRow.add(new KeyboardButton("please help 🙊"));
        keyboardFirstRow.add(new KeyboardButton("send meow 🐱"));
        keyboardFirstRow.add(new KeyboardButton("show me random gif of a cat"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        System.out.println("----- new request from: " + message.getFrom().getFirstName() + " ------");
        System.out.println("-----------------------------------");
        if (message !=null) {
            System.out.println("initial msg: " + message.getText());
            System.out.println("-----------------------------------");
            System.out.println("Whole info about msg:" + message.toString());
            System.out.println("-----------------------------------");

            if (message.getText().startsWith("/gif")) {
                String strGif = message.getText().replaceFirst("/gif ", "");
                System.out.println("Tag to search is: " + strGif);
                System.out.println("-----------------------------------");
                sendGif(message,strGif);
            }

            switch (message.getText()) {
                case "/start":
                    sendMsg(message, "Hi " + message.getFrom().getFirstName() + "! There are some buttons over here to push and also I can show you a random gif! " +
                            "\n\nJust type /gif *keyword* and I'll do the rest!");
                    break;
                case "please help 🙊" :
                    sendMsg(message, "Wanna see a random gif? Just type /gif and keyword");
                    break;
                case "show me random gif of a cat" :
                    sendGif(message, "cat");
                    break;
                case "send meow 🐱" :
                    sendMsg(message, "ok ok meow alright ok meow just chill");
                    break;
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "thisIsNotACatBot";
    }

    @Override
    public String getBotToken() {
        return "1671274841:AAEyy6UHkVPvfGfpX9I2c79-emGCeLLRfS4";
    }
}
