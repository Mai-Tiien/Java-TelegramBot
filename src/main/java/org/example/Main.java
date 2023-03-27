package org.example;


// @MaiMax_bot
// API: 5769001050:AAHD6F8FS50jAmq7rk2tYgrzyJbTG2unUyA

import jdk.jfr.Name;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static org.apache.http.util.TextUtils.isEmpty;

public class Main extends TelegramLongPollingBot{
    String name = "MaiMaxbot";
    String BotAPI = "5769001050:AAHD6F8FS50jAmq7rk2tYgrzyJbTG2unUyA";

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);

        try {
            api.registerBot(new Main());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.println("MaiMaxbot successfully started!");
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return BotAPI;
    }


    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);
        SendMessage message = new SendMessage();
        String text = message.getText();

//        if (text.equals("/start")){
//            message.setText("Hello " + fullname(update));
//        }
        message.setText("Hello " + fullname(update));
        message.setChatId(chatId);
        sendApiMethodAsync(message);

    }

    public static String fullname(Update update){
        String first_name = update.getMessage().getChat().getFirstName();
        String last_name = update.getMessage().getChat().getLastName();
        String username = update.getMessage().getChat().getUserName();

        if (!isEmpty(first_name))
            return first_name + " " + last_name;

        if (!isEmpty(last_name))
            return first_name + " " + last_name;

        return username;
    }

    public Long getChatId(Update update){
        if (update.hasMessage()){
            return update.getMessage().getFrom().getId();

        }
        if (update.hasCallbackQuery()){
            return update.getCallbackQuery().getFrom().getId();
        }
        return null;
    }

}