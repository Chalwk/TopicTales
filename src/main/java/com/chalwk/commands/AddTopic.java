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

public class AddTopic implements CommandInterface {

    private final TopicManager topicManager;

    public AddTopic(TopicManager topicManager) {
        this.topicManager = topicManager;
    }

    @Override
    public String getName() {
        return "tt-add-topic";
    }

    @Override
    public String getDescription() {
        return "Add a topic to the list of topics";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();

        OptionData addTopicOption = new OptionData(OptionType.STRING, "topic", "The name of the topic to add");
        addTopicOption.setRequired(true);

        options.add(addTopicOption);

        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String topic = event.getOption("topic").getAsString();
        this.topicManager.addTopic(topic);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Topic Added");
        embed.setDescription("The topic [**" + topic + "**] has been added to the list of topics. There are now [**" + this.topicManager.getTopics().size() + "**] topics.");
        embed.setColor(0x00FF00); // green
        event.replyEmbeds(embed.build()).queue();
    }
}