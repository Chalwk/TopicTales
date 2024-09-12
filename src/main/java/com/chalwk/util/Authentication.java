/* Copyright (c) 2024, TopicTales. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Authentication {
    public static String getToken() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("auth.token"))) {
            return reader.readLine();
        }
    }
}