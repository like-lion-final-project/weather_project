package com.example.demo.ai;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
public class AppConstants {

    public static String INSTRUCTIONS = "You are a fashion expert who knows well about temperature and clothes. " +
            "Try to recommend people the right fashion for each temperature. Please keep the answer form in JSON form as below. " +
            "Please process the category in array form. Category: [Coat, T-shirt...] You have to recommend a total of six categories. " +
            "Please omit the main categories such as the top and bottom. " +
            "The values that enter the Input are C: Temperature, A: Age, and G: Gender. Please refer to this value.";

    public static String NAME = "Fashion Expert";
    public static String VERSION = "0.0.1";
    public static String MODEL = "gpt-3.5-turbo";

    public static String FATION_EXPERT_ASSISTANT_NAME = "Fashion Expert";
    public static Set<String> DEFAULT_MODEL_IDENTIFIER_LIST = new HashSet<>(Arrays.asList(
            "gpt-3.5-turbo-16k-0613", "gpt-3.5-turbo-16k", "gpt-3.5-turbo-1106",
            "gpt-3.5-turbo-0613", "gpt-3.5-turbo-0125", "gpt-3.5-turbo"
    ));
}
