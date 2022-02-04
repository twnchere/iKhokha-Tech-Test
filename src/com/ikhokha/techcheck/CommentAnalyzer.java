package com.ikhokha.techcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommentAnalyzer {

    private File file;
    private int shorterThan15 = 15;
	pprivate final String METRIC_NAME = "SHORTER_THAN_15"

    public CommentAnalyzer(File file) {
        this.file = file;
    }

    public Map<String, Integer> analyze() {

        Map<String, Integer> resultsMap = new HashMap<>();

        Map<String, String> metrics = getMetrics ();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line = null;
            while ((line = reader.readLine()) != null) {

                if(line.length() < shorterThan15){

                    incOccurrence(resultsMap, METRIC_NAME);

                }
                else {
                    for (Map.Entry<String, String> metric : metrics.entrySet()) {
                        String key = metric.getKey();
                        String value = metric.getValue();

                        if ((line.toUpperCase().contains(value.toUpperCase())) && (line.length() >= 15)) {

                            incOccurrence(resultsMap, key);

                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getAbsolutePath());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO Error processing file: " + file.getAbsolutePath());
            e.printStackTrace();
        }

        return resultsMap;

    }

    /**
     * This method increments a counter by 1 for a match type on the countMap. Uninitialized keys will be set to 1
     * @param countMap the map that keeps track of counts
     * @param key the key for the value to increment
     */
    private void incOccurrence(Map<String, Integer> countMap, String key) {

        countMap.putIfAbsent(key, 0);
        countMap.put(key, countMap.get(key) + 1);
    }

    /**
     * This method returns the metrics
     * New metrics can be added here
     */
    private Map<String, String> getMetrics (){
        Map<String, String> metrics = new HashMap<>();

        metrics.put("MOVER_MENTIONS","Mover");
        metrics.put("SHAKER_MENTIONS","Shaker");
        metrics.put("QUESTIONS ","?");
        metrics.put("SPAM ","http");

        return metrics;
    }
}