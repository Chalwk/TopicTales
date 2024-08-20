package com.chalwk.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TopicManager {

    private static final String TOPICS_FILE = "topics.json";
    private List<Topic> topics;
    private boolean isGameRunning;

    /**
     * Constructs a new TopicManager instance with an empty list of topics and the game set as not running.
     */
    public TopicManager() {
        this.topics = new ArrayList<>();
        this.isGameRunning = false;
    }

    /**
     * Returns a list containing all the topics in the game.
     *
     * @return a list of topics
     */
    public List<String> getTopics() {
        List<String> topicsList = new ArrayList<>();
        for (Topic topic : this.topics) {
            topicsList.add(topic.getTopic());
        }
        return topicsList;
    }

    /**
     * Checks if the provided topic is valid by comparing it to the list of available topics.
     *
     * @param topic the topic to check for validity
     * @return true if the topic is valid (i.e., it exists in the list of available topics), false otherwise
     */
    public boolean isValidTopic(String topic) {
        List<String> topicsList = this.getTopics();
        return topicsList.contains(topic);
    }

    /**
     * Returns the current status of the game (running or not running).
     *
     * @return true if the game is running, false otherwise
     */
    public boolean isGameRunning() {
        return isGameRunning;
    }

    /**
     * Sets the game status to running or not running.
     *
     * @param running true if the game should be running, false otherwise
     */
    public void setGameRunning(boolean running) {
        isGameRunning = running;
    }

    /**
     * Adds a new topic to the game and updates the file.
     *
     * @param topic the topic to be added
     */
    public void addTopic(String topic) {
        this.topics.add(new Topic(topic, new ArrayList<>()));
        writeTopicsToFile();
    }

    /**
     * Removes the specified topic from the game and updates the file.
     *
     * @param topic the topic to be removed
     */
    public void removeTopic(String topic) {
        this.topics.removeIf(t -> t.getTopic().equals(topic));
        writeTopicsToFile();
    }

    /**
     * Adds a synonym to the specified topic and updates the file.
     *
     * @param topic the topic to which the synonym will be added
     * @param synonym the synonym to be added
     */
    public void addSynonym(String topic, String synonym) {
        Topic targetTopic = topics.stream()
                .filter(t -> t.getTopic().equals(topic))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Topic not found: " + topic));

        targetTopic.addSynonym(synonym);
        writeTopicsToFile();
    }

    /**
     * Removes the specified synonym from the topic and updates the file.
     *
     * @param topic the topic from which the synonym will be removed
     * @param synonym the synonym to be removed
     */
    public void removeSynonym(String topic, String synonym) {
        Topic targetTopic = topics.stream()
                .filter(t -> t.getTopic().equals(topic))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Topic not found: " + topic));

        targetTopic.removeSynonym(synonym);
        writeTopicsToFile();
    }

    /**
     * Writes the current list of topics and their synonyms to the specified file in
     * JSON format.
     *
     * @throws IOException if an I/O error occurs when writing to the file
     * @throws JSONException if there is a problem with JSON serialization
     */
    private void writeTopicsToFile() {
        try {
            JSONArray topicsArray = new JSONArray();
            for (Topic topic : this.topics) {
                JSONObject topicObj = new JSONObject();
                topicObj.put("topic", topic.getTopic());
                JSONArray synonymsArray = new JSONArray();
                for (String synonym : topic.getSynonyms()) {
                    synonymsArray.put(synonym);
                }
                topicObj.put("synonyms", synonymsArray);
                topicsArray.put(topicObj);
            }
            Files.writeString(Paths.get(TOPICS_FILE), topicsArray.toString(4));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads topics and their synonyms from the specified JSON file and populates the
     * topics list.
     *
     * @throws IOException if an I/O error occurs when reading from the file
     * @throws JSONException if there is a problem with JSON deserialization
     */
    public void loadTopics() {
        String fileName = "topics.json";
        InputStream inputStream = TopicManager.class.getClassLoader().getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder json = new StringBuilder();

        try {
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }

            topics = parseTopics(json.toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the provided JSON string representing topics and their synonyms and
     * returns a list of Topic objects.
     *
     * @param json the JSON string to parse
     * @return a list of Topic objects
     * @throws JSONException if there is a problem with JSON deserialization
     */
    private List<Topic> parseTopics(String json) throws JSONException {
        JSONArray topicsArray = new JSONArray(json);
        List<Topic> topicsList = new ArrayList<>();

        for (int i = 0; i < topicsArray.length(); i++) {
            JSONObject topicJson = topicsArray.getJSONObject(i);
            String topic = topicJson.getString("topic");
            List<String> synonyms = new ArrayList<>();

            JSONArray synonymsArray = topicJson.getJSONArray("synonyms");
            for (int j = 0; j < synonymsArray.length(); j++) {
                synonyms.add(synonymsArray.getString(j));
            }
            topicsList.add(new Topic(topic, synonyms));
        }

        return topicsList;
    }

    /**
     * Checks whether the provided guess matches a topic or one of its synonyms, and updates
     * the game state if the guess is correct.
     *
     * @param guess the guess to check
     * @return true if the guess matches a topic or one of its synonyms, false otherwise
     */
    public boolean checkGuess(String guess) {
        for (Topic topic : this.topics) {
            if (topic.getTopic().equalsIgnoreCase(guess) || topic.getSynonyms().contains(guess)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a Topic instance from the list of topics based on the provided topic name.
     *
     * @param topic the topic name to search for
     * @return the Topic instance with the provided topic name
     * @throws IllegalArgumentException if no Topic instance is found with the provided topic name
     */
    public Topic getTopic(String topic) {
        return topics.stream()
                .filter(t -> t.getTopic().equals(topic))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Topic not found: " + topic));
    }
}