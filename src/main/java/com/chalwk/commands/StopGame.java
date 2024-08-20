/* Copyright (c) 2024, TopicTales. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.commands;

import com.chalwk.CommandManager.CommandInterface;
import com.chalwk.util.TopicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class StopGame implements CommandInterface {

    private final TopicManager topicManager;

    public StopGame(TopicManager topicManager) {
        this.topicManager = topicManager;
    }

    @Override
    public String getName() {
        return "tt-stop";
    }

    @Override
    public String getDescription() {
        return "End the current game of TopicTales.";
    }

    @Override
    public List<OptionData> getOptions() {
        return new ArrayList<>();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        if (!this.topicManager.isGameRunning()) {
            embed.setDescription("There is no game currently running. Use **/tt-start** to begin a new game.");
        } else {
            this.topicManager.setGameRunning(false);
            embed.setDescription("The current game of TopicTales has ended.");
        }
        embed.setColor(0xFF0000); // red
        event.replyEmbeds(embed.build()).queue();
    }
}