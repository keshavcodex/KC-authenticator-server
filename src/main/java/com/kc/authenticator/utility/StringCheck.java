package com.kc.authenticator.utility;

import org.springframework.stereotype.Component;

@Component
public class StringCheck {
    public boolean hasSpecialCharacter(String input) {
        if (input == null || input.isEmpty()) {
            return false; // or handle as needed; returning true if no characters means no special characters
        }
        // Regex pattern for special characters
        String regex = "[^a-zA-Z0-9-]";
        return input.matches(".*" + regex + ".*"); // returns true if no special characters
    }
}
