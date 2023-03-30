package org.example;


// @MaiMax_bot
// API: 5769001050:AAHD6F8FS50jAmq7rk2tYgrzyJbTG2unUyA

import jdk.jfr.Name;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


import static org.apache.http.util.TextUtils.isEmpty;

public class Main extends TelegramLongPollingBot{
    String name = "MaiMaxbot";
    String BotAPI = "5769001050:AAHD6F8FS50jAmq7rk2tYgrzyJbTG2unUyA";
    private Map<Long, Integer> levels = new HashMap<>();

    public static void main(String[] args) throws TelegramApiException, IOException {
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
        if (update.hasMessage() && update.getMessage().getText().equals("/start")){
            sendImage("level-1", chatId);

            try {
                message.setText(
                        "Привіт "
                                + fullname(update)
                                + "!\n" + Objects.requireNonNull(
                                        readFile(Path.of("task/text1.txt"))
                        )
                );
                message.setParseMode("markdown");
                message.setChatId(chatId);

                attachButtons(message, Map.of(
                            "Сплести маскувальну сітку (+15 монет)", "level_1_task",
                            "Зібрати кошти патріотичними піснями (+15 монет) ","level_1_task",
                            "Вступити в Міністерство Мемів України (+15 монет) ", "level_1_task"
                        ));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sendApiMethodAsync(message);
        }

        if (update.hasCallbackQuery()){
            if (update.getCallbackQuery().getData().equals("level_1_task")){
                sendImage("level-2", chatId);
                try {
                    message.setText(Objects.requireNonNull(readFile(Path.of("task/text2.txt"))));
                    message.setParseMode("markdown");
                    message.setChatId(chatId);

                    attachButtons(message, Map.of(
                            "Зібрати комарів для нової біологічної зброї (+15 монет) ", "level_2_task",
                            "Пройти курс молодого бійця (+15 монет) ","level_2_task",
                            "Задонатити на ЗСУ (+15 монет)  ", "level_2_task"
                    ));
                    sendApiMethodAsync(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (update.hasCallbackQuery()){
            if (update.getCallbackQuery().getData().equals("level_2_task")){
                sendImage("level-3", chatId);
                try {
                    message.setText(Objects.requireNonNull(readFile(Path.of("task/text3.txt"))));
                    message.setParseMode("markdown");
                    message.setChatId(chatId);

                    attachButtons(message, Map.of(
                            "Злітати на тестовий рейд по чотирьох позиціях (+15 монет)  ", "level_3_task",
                            "Відвезти гуманітарку на передок (+15 монет) ","level_3_task",
                            "Знайти зрадника та здати в СБУ (+15 монет) ", "level_3_task"
                    ));
                    sendApiMethodAsync(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static String fullname(Update update){
        String first_name = update.getMessage().getChat().getFirstName();
        String last_name = update.getMessage().getChat().getLastName();
        String username = update.getMessage().getChat().getUserName();

        if (!isEmpty(first_name))
            return first_name;

        if (!isEmpty(last_name))
            return last_name;

        return username;
    }

    public void attachButtons(SendMessage message, Map<String, String> buttons){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String buttonName : buttons.keySet()){
            String buttonValue = buttons.get(buttonName);

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonName);
            button.setCallbackData(buttonValue);
            keyboard.add(Arrays.asList(button));
        }

        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
    }
    public static String readFile(Path path) throws IOException {
        try(FileReader reader = new FileReader(path.toFile()))
        {
            char[] buf = new char[2084];
            int c;
            while((c = reader.read(buf))>0){

                if(c < 2084){
                    buf = Arrays.copyOf(buf, c);
                }
                return String.valueOf(buf);
            }
        }
        catch(IOException ex){
            return ex.getMessage();
        }
        return null;
    }

    public void sendImage(String img_name, Long chatId){
        SendAnimation animation = new SendAnimation();

        InputFile inputFile = new InputFile();
        inputFile.setMedia(new File("images/" + img_name + ".gif"));

        animation.setAnimation(inputFile);
        animation.setChatId(chatId);

        executeAsync(animation);
    }

    public int getLevel(Long chatId){
        return levels.getOrDefault(chatId, 1);
    }

    public void setLevel(Long chatId, int level) {
        levels.put(chatId, level);
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