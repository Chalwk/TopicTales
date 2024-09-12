package com.chalwk.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class TopicManager {

    private static final String TOPICS_FILE = "topics.json";
    private List<Topic> topics;
    private boolean isGameRunning;

    public TopicManager() {
        this.topics = new ArrayList<>();
        this.isGameRunning = false;
    }

    public List<String> getTopics() {
        List<String> topicsList = new ArrayList<>();
        for (Topic topic : this.topics) {
            topicsList.add(topic.getTopic());
        }
        return topicsList;
    }

    public boolean isValidTopic(String topic) {
        return getTopics().contains(topic);
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public void setGameRunning(boolean running) {
        isGameRunning = running;
    }

    public void addTopic(String topic) {
        this.topics.add(new Topic(topic, new ArrayList<>()));
        writeTopicsToFile();
    }

    public void removeTopic(String topic) {
        this.topics.removeIf(t -> t.getTopic().equals(topic));
        writeTopicsToFile();
    }

    public void addSynonym(String topic, String synonym) {
        Topic targetTopic = getTopic(topic);
        targetTopic.addSynonym(synonym);
        writeTopicsToFile();
    }

    public void removeSynonym(String topic, String synonym) {
        Topic targetTopic = getTopic(topic);
        targetTopic.removeSynonym(synonym);
        writeTopicsToFile();
    }

    private void writeTopicsToFile() {
        try {
            JSONArray topicsArray = new JSONArray();
            for (Topic topic : this.topics) {
                JSONObject topicObj = new JSONObject();
                topicObj.put("topic", topic.getTopic());
                JSONArray synonymsArray = new JSONArray(topic.getSynonyms());
                topicObj.put("synonyms", synonymsArray);
                topicsArray.put(topicObj);
            }
            Files.writeString(Paths.get(TOPICS_FILE), topicsArray.toString(4));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadTopics() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(TOPICS_FILE);
             BufferedReader br = new BufferedReader(new InputStreamReader(Optional.ofNullable(inputStream)
                     .orElseThrow(() -> new FileNotFoundException("File not found: " + TOPICS_FILE))))) {

            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
            topics = parseTopics(json.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private List<Topic> parseTopics(String json) throws JSONException {
        JSONArray topicsArray = new JSONArray(json);
        List<Topic> topicsList = new ArrayList<>();

        for (int i = 0; i < topicsArray.length(); i++) {
            JSONObject topicJson = topicsArray.getJSONObject(i);
            String topic = topicJson.getString("topic");
            JSONArray synonymsArray = topicJson.getJSONArray("synonyms");
            List<String> synonyms = new ArrayList<>();
            for (int j = 0; j < synonymsArray.length(); j++) {
                synonyms.add(synonymsArray.getString(j));
            }
            topicsList.add(new Topic(topic, synonyms));
        }
        return topicsList;
    }

    public boolean checkGuess(String guess) {
        return topics.stream()
                .anyMatch(topic -> topic.getTopic().equalsIgnoreCase(guess) || topic.getSynonyms().contains(guess));
    }

    public Topic getTopic(String topic) {
        return topics.stream()
                .filter(t -> t.getTopic().equals(topic))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Topic not found: " + topic));
    }
}