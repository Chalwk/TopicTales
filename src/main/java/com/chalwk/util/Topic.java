/* Copyright (c) 2024, TopicTales. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.util;

import java.util.List;

/**
 * Represents a topic with its associated synonyms.
 */
public class Topic {

    private String topic;
    private List<String> synonyms;

    /**
     * Constructs a new Topic with the specified topic and synonyms.
     *
     * @param topic the main topic
     * @param synonyms a list of synonyms associated with the topic
     */
    public Topic(String topic, List<String> synonyms) {
        this.topic = topic;
        this.synonyms = synonyms;
    }

    /**
     * Returns the main topic.
     *
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Returns the list of synonyms for the topic.
     *
     * @return a list of synonyms
     */
    public List<String> getSynonyms() {
        return synonyms;
    }

    /**
     * Adds a synonym to the list of synonyms for the topic.
     *
     * @param synonym the synonym to be added
     */
    public void addSynonym(String synonym) {
        synonyms.add(synonym);
    }

    /**
     * Removes the specified synonym from the list of synonyms for the topic.
     *
     * @param synonym the synonym to be removed
     */
    public void removeSynonym(String synonym) {
        synonyms.remove(synonym);
    }
}