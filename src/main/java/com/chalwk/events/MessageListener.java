/* Copyright (c) 2024, TopicTales. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.events;

import com.chalwk.util.TopicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MessageListener extends ListenerAdapter {

    private static final String CHANNEL_ID = "1275265376141447199";
    private final TopicManager topicManager;

    public MessageListener(TopicManager topicManager) {
        this.topicManager = topicManager;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getAuthor().isBot() && event.getChannel().getId().equals(CHANNEL_ID) && topicManager.isGameRunning()) {
            handleGuess(event, event.getMessage().getContentRaw());
        }
    }

    private void handleGuess(MessageReceivedEvent event, String guess) {
        EmbedBuilder embed = new EmbedBuilder();
        if (topicManager.checkGuess(guess)) {
            topicManager.setGameRunning(false);
            embed.setTitle("Congratulations!")
                    .setDescription("You guessed the topic correctly!")
                    .setColor(0x00FF00); // green
        } else {
            embed.setTitle("Incorrect Guess")
                    .setDescription("Try again!")
                    .setColor(0xFF0000); // red
        }
        Optional.of(event.getChannel()).ifPresent(channel -> channel.sendMessageEmbeds(embed.build()).queue());
    }
}