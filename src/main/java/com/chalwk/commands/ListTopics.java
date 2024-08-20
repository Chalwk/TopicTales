/* Copyright (c) 2024, TopicTales. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.commands;

import com.chalwk.CommandManager.CommandInterface;
import com.chalwk.util.Topic;
import com.chalwk.util.TopicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class ListTopics implements CommandInterface {

    private final TopicManager topicManager;

    public ListTopics(TopicManager topicManager) {
        this.topicManager = topicManager;
    }

    @Override
    public String getName() {
        return "tt-list";
    }

    @Override
    public String getDescription() {
        return "Show a list of all available topics";
    }

    @Override
    public List<OptionData> getOptions() {
        return new ArrayList<>();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("List of Topics");

        StringBuilder description = new StringBuilder();
        List<String> topics = this.topicManager.getTopics();

        for (String topic : topics) {
            Topic topicObj = this.topicManager.getTopic(topic);
            description.append("**").append(topic).append("**");
            if (!topicObj.getSynonyms().isEmpty()) {
                description.append(" (Synonyms: ");
                for (String synonym : topicObj.getSynonyms()) {
                    description.append(synonym).append(", ");
                }
                description.deleteCharAt(description.length() - 1);
                description.deleteCharAt(description.length() - 1);
                description.append(")");
            }
            description.append("\n");
        }

        embed.setDescription(description.toString());
        embed.addField("Total Topics", String.valueOf(this.topicManager.getTopics().size()), false);
        embed.setColor(0x00FF00); // green
        event.replyEmbeds(embed.build()).queue();
    }
}