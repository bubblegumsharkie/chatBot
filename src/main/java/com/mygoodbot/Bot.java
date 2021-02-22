package com.mygoodbot;

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
import com.mygoodbot.CryMeALink;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
            System.out.println("Succ");

        } catch (TelegramApiRequestException e)  {
            e.printStackTrace();
            System.out.println("Failed");
        }
        System.out.println("Finally!");
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
        if (message !=null) {
            System.out.println("initial msg: " + message.getText());
            System.out.println("Whole info about msg:" + message.toString());
            if (message.getText().startsWith("/gif")) {
                String strGif = message.getText().replaceFirst("/gif ", "");
                System.out.println(strGif);
                sendGif(message,strGif);
            }
            switch (message.getText()) {
                case "/start":
                    sendMsg(message, "Hello there! There are some buttons over here and also i can show you a random gif! Just type /gif *keyword* and i'll do the rest!");
                    break;
                case "please help 🙊" :
                    sendMsg(message, "Wanna see a random gif? Just type /gif and keyword");
                    break;
                case "show me random gif of a cat" :
                    sendGif(message, "gif!");
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
        return "";
    }
}
