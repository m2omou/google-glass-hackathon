/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.glass.sample.compass.model;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a point of interest that has geographical coordinates (latitude and
 * longitude) and a name that is displayed to the user.
 */
public class Place implements Serializable {

    private final double mLatitude;
    private final double mLongitude;
    private final String mName;
    private final String mFact;
    private final String mQuestion;
    private final String mCorrectAnswer;
    private final List<String> mAnswerChoices;

    /**
     * Initializes a new place with the specified coordinates and name.
     *
     * @param latitude the latitude of the place
     * @param longitude the longitude of the place
     * @param name the name of the place
     * @param facts the facts of the place
     * @param question the question for the place
     * @param correctAnswer the correct answer for the question
     * @param answerChoices the multiple choices for the question
     */
    public Place(double latitude, double longitude, String name,
    		String fact, String question, String correctAnswer, 
    		List<String> answerChoices) {
        mLatitude = latitude;
        mLongitude = longitude;
        mName = name;
        mFact = fact;
        mQuestion = question;
        mCorrectAnswer = correctAnswer;
        mAnswerChoices = answerChoices;
    }

    /**
     * Gets the latitude of the place.
     *
     * @return the latitude of the place
     */
    public double getLatitude() {
        return mLatitude;
    }

    /**
     * Gets the longitude of the place.
     *
     * @return the longitude of the place
     */
    public double getLongitude() {
        return mLongitude;
    }

    /**
     * Gets the name of the place.
     *
     * @return the name of the place
     */
    public String getName() {
        return mName;
    }
    
    /**
     * Gets the facts of the place.
     *
     * @return the facts of the place
     */
    public String getFacts() {
        return mFact;
    }
    
    /**
     * Gets the question for the place.
     *
     * @return the question for the place
     */
    public String getQuestion() {
        return mQuestion;
    }
    
    /**
     * Gets the correct answer for the question of place.
     *
     * @return the correct answer for the question of the place
     */
    public String getCorrectAnswer() {
        return mCorrectAnswer;
    }
    
    /**
     * Gets the possible answers for the question of the place.
     *
     * @return possible answers for the question of the place
     */
    public List<String> getAnswerChoices() {
        return mAnswerChoices;
    }
    
}
