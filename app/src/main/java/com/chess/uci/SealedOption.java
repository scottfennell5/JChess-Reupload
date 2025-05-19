package com.chess.uci;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public sealed class SealedOption permits CheckOption, SpinOption, ComboOption, ButtonOption, StringOption {
    /**
     * Parse an option string into a subclass of SealedOption.
     * @param optionString The option string to parse.
     * @return A subclass of SealedOption, or an exception if the regex fails.
     */
    public static SealedOption parse(String optionString) {
        // Check that the option string starts with a valid option prefix.
        Matcher baseMatcher = basePattern.matcher(optionString);
        // Validate that the option string matches the base option value.
        if (!baseMatcher.find())
            throw new IllegalArgumentException("optionString is not a valid UCI option statement.");
        if (baseMatcher.groupCount() < 2)
            throw new IllegalStateException("The matched optionString does not have enough groups.");
        // Extract the option name and type.
        String name = baseMatcher.group(1);
        String type = baseMatcher.group(2);
        // Select the correct pattern for this option type.
        switch (type) {
            case "check" -> {
                Matcher checkMatcher = checkOption.matcher(optionString);
                // Validate the match.
                if (!checkMatcher.find())
                    throw new IllegalArgumentException("optionString, supposedly a checkbox type, isn't valid.");
                if (checkMatcher.groupCount() < 3)
                    throw new IllegalStateException("optionString, supposedly a checkbox type, has less than 3 groups.");
                // Extract the extra data.
                boolean def = Boolean.parseBoolean(checkMatcher.group(3));
                // Return the checkbox option.
                return new CheckOption(name, def);
            }
            case "spin" -> {
                Matcher spinMatcher = spinOption.matcher(optionString);
                // Validate the match.
                if (!spinMatcher.find())
                    throw new IllegalArgumentException("optionString, supposedly a spinner, isn't valid.");
                if (spinMatcher.groupCount() < 5)
                    throw new IllegalStateException("optionString, supposedly a spinner, has less than 5 groups.");
                // Extract the extra data.
                int def = Integer.parseInt(spinMatcher.group(3));
                int min = Integer.parseInt(spinMatcher.group(4));
                int max = Integer.parseInt(spinMatcher.group(5));
                // Return the new spinner option.
                return new SpinOption(name, min, max, def);
            }
            case "combo" -> {
                // Phase 1: get the default value.
                Matcher comboMatcher = comboOption.matcher(optionString);
                // Validate the match.
                if (!comboMatcher.find())
                    throw new IllegalArgumentException("optionString, supposedly a combo, isn't valid.");
                if (comboMatcher.groupCount() < 3)
                    throw new IllegalStateException("optionString, supposedly a combo, has less than 3 groups.");
                // Extract the default value.
                String def = comboMatcher.group(3);
                // Phase 2: get the possible values.
                System.out.println("DEBUG: Before: " + optionString);
                String reduced = optionString.replaceFirst(baseOption + "\s(?:default)\s(\\w+)\s", "");
                System.out.println("DEBUG: After: " + reduced);
                /*
                Matcher comboVarMatcher = comboVarStatement.matcher(reduced);
                // Validate the match.
                if (!comboVarMatcher.find())
                    throw new IllegalStateException("Provided combo box optionString doesn't have possible options!");
                if (comboVarMatcher.groupCount() == 0)
                    throw new IllegalStateException("Provided combo box options statement is invalid!");
                // Get each one of the matches.
                ArrayList<String> possible = new ArrayList<>();
                for (int i = 1; i <= comboVarMatcher.groupCount(); i++) possible.add(comboVarMatcher.group(i));
                 */
                // Remove "var " at the beginning of the option string
                reduced = reduced.replaceFirst("(var)\s", "");
                // Replace each " var " with a ", "
                reduced = reduced.replaceAll("\s(var)\s", ", ");
                // Return the statement.
                return new ComboOption(name, reduced.split("(,)\s"), def);
            }
            case "button" -> {
                // Button has no default or extended options, so it Just Works
                return new ButtonOption(name);
            }
            case "string" -> {
                Matcher stringMatcher = stringOption.matcher(optionString);
                if (optionString.endsWith("default")) {
                    return new StringOption(name, "");
                } else {
                    // Validate the match.
                    if (!stringMatcher.find())
                        throw new IllegalArgumentException("optionString, supposedly a string, isn't valid.");
                    if (stringMatcher.groupCount() < 3)
                        throw new IllegalStateException("optionString, supposedly a string, has less than 3 groups.");
                    // Extract the extra data.
                    String def = stringMatcher.group(3);
                    // Return the new string option.
                    return new StringOption(name, def);
                }
            }
            default -> throw new IllegalStateException("This should never happen!");
        }
    }

    // This pattern parses the components that all option statements start with "option name {name} type".
    // It should produce 2 extractable values: the name, and the type.
    private static final String baseOption = "(?:option)\s(?:name)\s(.*)\s(?:type)\s(check|spin|combo|button|string)";

    // Don't use this unless you're verifying that the option statement starts with the correct contents.
    private static final Pattern basePattern = Pattern.compile(baseOption);

    // This pattern parses the check type.
    // It should produce 3 extractable values: the name, the type, and the default boolean value.
    private static final Pattern checkOption = Pattern.compile(baseOption + "\s(?:default)\s(true|false)");

    // This pattern parses the spin type.
    // It should produce 5 extractable values: the name, the type, the default value, the minimum, and the maximum.
    private static final Pattern spinOption =
        Pattern.compile(baseOption + "\s(?:default)\s(-?\\d+)\s(?:min)\s(-?\\d+)\s(?:max)\s(-?\\d+)");

    // This pattern parses the combo type.
    // It should produce 3 extractable values: the name, the type, and the default value.
    // However, once this matches, you *must* find and replace this pattern with an empty string, and then
    // apply the comboVarStatement pattern to get the potential options for this combo box!
    private static final Pattern comboOption = Pattern.compile(baseOption + "\s(?:default)\s(\\w+)\s");

    // This pattern matches the string type.
    private static final Pattern stringOption = Pattern.compile(baseOption + "\s(?:default)\s(.*)");
}
