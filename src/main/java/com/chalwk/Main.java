/* Copyright (c) 2024, TopicTales. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk;

import com.chalwk.bot.BotInitializer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            new BotInitializer().initializeBot();
        } catch (IOException e) {
            System.err.println("Error reading token or initializing the bot: " + e.getMessage());
        }
    }
}