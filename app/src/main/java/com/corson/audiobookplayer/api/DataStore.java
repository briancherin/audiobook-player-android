package com.corson.audiobookplayer.api;

public interface DataStore {

    /**
     * Store the data, associated with the specified key
     * @param key   The String key that will be used to access the value
     * @param value The String value to save
     */
    void saveKeyValue(String key, String value);

    /**
     *
     * @param key   The String key that will be used to access the value
     * @param value The integer value to save
     */
    void saveKeyValue(String key, int value);

    /**
     *
     * @param key
     * @return The data associated with the specified key
     */
    String getKeyValueString(String key);

    /**
     *
     * @param key
     * @return The integer associated with the specified key
     */
    int getKeyValueInt(String key);
}
