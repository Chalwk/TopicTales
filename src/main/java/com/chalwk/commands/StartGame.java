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

public class StartGame implements CommandInterface {

    private final TopicManager topicManager;

    public StartGame(TopicManager topicManager) {
        this.topicManager = topicManager;
    }


    @Override
    public String getName() {
        return "tt-start";
    }

    @Override
    public String getDescription() {
        return "Embark on a captivating storytelling journey with TopicTales!";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();

        OptionData topicOption = new OptionData(OptionType.STRING, "topic", "The topic to start the game with (use /tt-list to view all topics)");
        topicOption.setRequired(true);

        OptionData promptOption = new OptionData(OptionType.STRING, "prompt", "The prompt for the topic");
        promptOption.setRequired(true);

        options.add(topicOption);
        options.add(promptOption);

        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        if (topicManager.isGameRunning()) {
            embed.setDescription("A game is already running. Use **/tt-stop** to end the current game.");
            embed.setColor(0xFF0000); // red
        } else {

            String topic = event.getOption("topic").getAsString();
            if (!topicManager.isValidTopic(topic)) {
                embed.setDescription("Invalid topic! Please select a valid topic from the list.");
                embed.setColor(0xFF0000); // red
            } else {
                String prompt = event.getOption("prompt").getAsString();
                topicManager.setGameRunning(true);
                embed.addField("New Game", "A new game of TopicTales has begun! Guess the topic based on the prompt.", false);
                embed.addField("Prompt:", "*" + prompt + "*", false);
                embed.setColor(0x00FF00); // green
                embed.setFooter("Use /tt-list to view all topics.\nUse /tt-stop to end the game.");
            }
        }

        // Technical note:
        // We have to 'reply' otherwise the command will not be acknowledged by Discord.
        // However, the app will respond with the contents of our embed and include the complete command string (/tt-start topic prompt) in the response.
        // First we reply with a simple (hidden) message to let the user know the game is starting.
        // Then we send the embed with the game information. Otherwise users will see the command string in the response and know the topic of the prompt.
        event.reply("Starting game...").setEphemeral(true).queue();
        event.getChannel().sendMessageEmbeds(embed.build()).queue();
    }
}