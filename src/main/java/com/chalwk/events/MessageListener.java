/* Copyright (c) 2024, TopicTales. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.events;

import com.chalwk.util.TopicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {

    private final TopicManager topicManager;

    public MessageListener(TopicManager topicManager) {
        this.topicManager = topicManager;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String channelID = "1275265376141447199";

        boolean isBot = event.getAuthor().isBot();
        boolean isCorrectChannel = event.getChannel().getId().equals(channelID);
        boolean isGameRunning = topicManager.isGameRunning();

        if (!isBot && isCorrectChannel && isGameRunning) {
            handleGuess(event, event.getMessage().getContentRaw());
        }
    }

    private void handleGuess(MessageReceivedEvent event, String guess) {
        EmbedBuilder embed = new EmbedBuilder();
        if (topicManager.checkGuess(guess)) {
            topicManager.setGameRunning(false);
            embed.setTitle("Congratulations!");
            embed.setDescription("You guessed the topic correctly!");
            embed.setColor(0x00FF00); // green
        } else {
            embed.setTitle("Incorrect Guess");
            embed.setDescription("Try again!");
            embed.setColor(0xFF0000); // red
        }
        event.getChannel().sendMessageEmbeds(embed.build()).queue();
    }
}