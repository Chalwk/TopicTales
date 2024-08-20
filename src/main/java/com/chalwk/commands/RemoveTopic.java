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

public class RemoveTopic implements CommandInterface {

    private final TopicManager topicManager;

    public RemoveTopic(TopicManager topicManager) {
        this.topicManager = topicManager;
    }

    @Override
    public String getName() {
        return "tt-remove-topic";
    }

    @Override
    public String getDescription() {
        return "Remove a topic from the list of topics";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();

        OptionData addTopicOption = new OptionData(OptionType.STRING, "topic", "The name of the topic to remove");
        addTopicOption.setRequired(true);

        options.add(addTopicOption);

        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String topic = event.getOption("topic").getAsString();
        EmbedBuilder embed = new EmbedBuilder();
        if (!this.topicManager.getTopics().contains(topic)) {
            embed.setTitle("Topic Not Found");
            embed.setDescription("The topic [**" + topic + "**] was not found in the list of topics.");
        } else {
            embed.setTitle("Topic Removed");
            embed.setDescription("The topic [**" + topic + "**] has been removed from the list of topics. There are now [**" + this.topicManager.getTopics().size() + "**] topics.");
            this.topicManager.removeTopic(topic);
        }
        embed.setColor(0xFF0000); // red
        event.replyEmbeds(embed.build()).queue();
    }
}