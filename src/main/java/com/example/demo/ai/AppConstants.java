package com.example.demo.ai;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
public class AppConstants {

    public static String INSTRUCTIONS = "You are a fashion expert. You know more about the appropriate clothes for each weather. If a user enters data in the form [{fcstTime:0:00,fcstValue:1}, this value should be analyzed and the appropriate dress for the day should be recommended. Response data follows the format below. { CATEGORIES: [코트, 청바지] } Note that the key CATEGORIES should be written in English and the data in the array should be written in Korean. array size is minimum 6";

    public static String NAME = "Fashion Expert";
    public static String VERSION = "0.0.1";
    public static String MODEL = "gpt-3.5-turbo";
    public static String MESSAGE_SUFFIX = " Please keep the response data format.";

    public static String FATION_EXPERT_ASSISTANT_NAME = "Fashion Expert";
    public static Set<String> DEFAULT_MODEL_IDENTIFIER_LIST = new HashSet<>(Arrays.asList(
            "gpt-3.5-turbo-16k-0613", "gpt-3.5-turbo-16k", "gpt-3.5-turbo-1106",
            "gpt-3.5-turbo-0613", "gpt-3.5-turbo-0125", "gpt-3.5-turbo"
    ));
}
