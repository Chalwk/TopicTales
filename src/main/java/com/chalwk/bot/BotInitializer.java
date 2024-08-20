/* Copyright (c) 2024, TopicTales. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.bot;

import com.chalwk.CommandManager.CommandListener;
import com.chalwk.commands.*;
import com.chalwk.events.MessageListener;
import com.chalwk.util.Authentication;
import com.chalwk.util.TopicManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.IOException;

public class BotInitializer {

    private final String token;

    public BotInitializer() throws IOException {
        this.token = Authentication.getToken();
    }

    public void initializeBot() {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(this.token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.listening("Your stories"));
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.MESSAGE_CONTENT);

        TopicManager topicManager = new TopicManager();
        topicManager.loadTopics();

        ShardManager shardManager = builder.build();
        shardManager.addEventListener(new MessageListener(topicManager));

        registerCommands(topicManager, shardManager);
    }

    private void registerCommands(TopicManager topicManager, ShardManager shardManager) {
        CommandListener commands = new CommandListener();
        commands.add(new AddTopic(topicManager));
        commands.add(new ListTopics(topicManager));
        commands.add(new RemoveTopic(topicManager));
        commands.add(new StartGame(topicManager));
        commands.add(new StopGame(topicManager));
        shardManager.addEventListener(commands);
    }
}
