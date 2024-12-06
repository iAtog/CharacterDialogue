package me.iatog.characterdialogue.dialogs;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodConfiguration {

    private final Pattern pattern;
    private final Map<String, Object> objects;
    private final String argument;

    public MethodConfiguration(String argument) {
        this.objects = new HashMap<>();
        String regex = "(\\w+)='([^']*)'\\s*,?|\\s*(\\w+)=([^,\\s}]+)";
        this.pattern = Pattern.compile(regex);
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }

    public Object get(String key) {
        return objects.get(key);
    }

    public Object get(String key, Object def) {
        return !has(key) ? def : get(key);
    }

    public String getString(String key) {
        return (String) objects.get(key);
    }

    public String getString(String key, String def) {
        return !has(key) ? def : (String) get(key);
    }

    public boolean getBoolean(String key) {
        return (boolean) get(key);
    }

    public boolean getBoolean(String key, boolean def) {
        return !has(key) ? def : (boolean) get(key);
    }

    public int getInteger(String key) {
        return (Integer) get(key);
    }

    public int getInteger(String key, int def) {
        return !has(key) ? def : (int) get(key);
    }

    public float getFloat(String key) {
        return (float) get(key);
    }

    public float getFloat(String key, float def) {
        return !has(key) ? def : (float) get(key);
    }

    public boolean has(String key) {
        return objects.containsKey(key);
    }

    public void init(String input) {
        if(input.isEmpty()) {
            return;
        }

        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                objects.put(matcher.group(1),  matcher.group(2));
            } else {
                objects.put(matcher.group(3),  this.convertValue(matcher.group(4)));
            }
        }
    }

    public Map<String, Object> getObjects() {
        return objects;
    }

    private Object convertValue(String value) {
        if(value == null) {
            return null;
        }
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(value);
        } else if (value.matches("-?\\d+(\\.\\d+)?")) {
            try {
                if (value.contains(".")) {
                    return Float.parseFloat(value);
                } else {
                    return Integer.parseInt(value);
                }
            } catch (NumberFormatException exception) {
                return value;
            }
        } else {
            return value;
        }
    }

    // Testing regex
    public static void main(String[] a) {
        String dialog = "TITLE{title='My title, nice title',subtitle='This is a subtitle, yay!', fadeIn=20, stay=60, fadeOut=20, animated=true, deep=2}: he";
        String[] split = dialog.split(":", 2);
        String methodName = split[0].toUpperCase().trim().split("\\{")[0];
        String configPart = split[0].substring(methodName.length()).trim();
        String arg = split.length > 1 ? split[1].trim() : "";

        System.out.println("Method: " + methodName);
        System.out.println("Configurations: " + configPart);
        System.out.println("Argument: " + arg);

        MethodConfiguration config = new MethodConfiguration(arg);
        config.init(configPart);

        System.out.println("Listing all configurations:");
        config.getObjects().forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });

        System.out.println("toString configuration: " + config);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");

        objects.forEach((key, value) -> {
            if(value instanceof String) {
                stringBuilder.append(key).append("='").append(value).append("'");
            } else {
                stringBuilder.append(key).append("=").append(value);
            }
            stringBuilder.append(",");
        });
        if(stringBuilder.toString().endsWith(",")) {
            stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

}