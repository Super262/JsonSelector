package tools;

import java.util.ArrayList;
import java.util.HashMap;

public class Analyzer {
    private final Object JSON_DATA;
    private int currentIndex;
    private final String rawStr;
    private final int strLen;
    private final HashMap<String, Integer> sizeMap;

    public Analyzer(String rawStr) throws Exception {
        this.rawStr = rawStr;
        this.strLen = rawStr.length();
        currentIndex = 0;
        this.JSON_DATA = parseValue();
        this.sizeMap = new HashMap<>();
    }

    public Object getJsonData() {
        return JSON_DATA;
    }

    private Object parseValue() throws Exception {
        skipWhitespace();
        Object value = null;
        switch(rawStr.charAt(currentIndex)){
            case '{':{
                value = parseObject();
                break;
            }
            case '[':{
                value = parseArray();
                break;
            }
            case '"':{
                value = parseString();
                break;
            }
            default:{
                Object value1 = parseNumber();
                Object value2 = parseKeyword("true", true);
                Object value3 = parseKeyword("false", false);
                Object value4 = parseKeyword("null", null);
                if(value1 != null){
                    value = value1;
                }
                else if(value2 != null){
                    value = value2;
                }
                else if(value3 != null){
                    value = value3;
                }
                else if(value4 != null){
                    value = value4;
                }
            }
        }
        skipWhitespace();
        return value;
    }

    private HashMap<String, Object> parseObject() throws Exception {
        String tag = "(Object)";
        if(currentIndex < strLen && rawStr.charAt(currentIndex) == '{'){
            HashMap<String, Object> result = new HashMap<>();
            ++currentIndex;
            skipWhitespace();
            boolean initial = true;
            while(currentIndex < strLen && rawStr.charAt(currentIndex) != '}'){
                if(!initial){
                    testComma();
                    skipWhitespace();
                }
                final String key = parseString();
                skipWhitespace();
                testColon();
                skipWhitespace();
                final Object value = parseValue();
                if(key != null){
                    result.put(key, value);
                }
                initial = false;
            }
            ++currentIndex;

            return result;
        }
        else{
            return null;
        }
    }

    private Object[] parseArray() throws Exception {
        String tag = "(Array)";
        if(currentIndex < strLen && rawStr.charAt(currentIndex) == '['){
            ArrayList<Object> tempArray = new ArrayList<>();
            ++currentIndex;
            skipWhitespace();
            boolean initial = true;
            while(currentIndex < strLen && rawStr.charAt(currentIndex) != ']'){
                if(!initial){
                    testComma();
                    skipWhitespace();
                }
                final Object value = parseValue();
                tempArray.add(value);
                initial = false;
            }
            ++currentIndex;
            Object[] result = new Object[tempArray.size()];
            int i = 0;
            for (Object obj : tempArray) {
                result[i] = obj;
                ++i;
            }
            return result;
        }
        else{
            return null;
        }
    }

    private Object parseKeyword( final String valName, Object value) {
        String tag = "(Keyword)";
        if(currentIndex < strLen && currentIndex + valName.length() < strLen && rawStr.substring(currentIndex, currentIndex + valName.length()).equals(valName)){
            currentIndex += valName.length();
            return value;
        }
        else{
            return null;
        }
    }

    private String parseString() {
        String tag = "(String)";
        if (currentIndex < strLen && rawStr.charAt(currentIndex) == '"') {
            StringBuilder result = new StringBuilder();
            final HashMap<Character, Character> escapeChar = new HashMap<>();
            escapeChar.put('b', '\b');
            escapeChar.put('n', '\n');
            escapeChar.put('t', '\t');
            escapeChar.put('r', '\r');
            escapeChar.put('f', '\f');
            escapeChar.put('\"', '\"');
            escapeChar.put('\\', '\\');
            ++currentIndex;
            while (currentIndex < strLen && rawStr.charAt(currentIndex) != '"') {
                if (rawStr.charAt(currentIndex) == '\\' && currentIndex < strLen - 1) {
                    final char nextChar = rawStr.charAt(currentIndex + 1);
                    if (escapeChar.containsKey(nextChar)) {
                        result.append(escapeChar.get(nextChar));
                        ++currentIndex;
                    } else if (nextChar == 'u') {
                        if (currentIndex + 5 < strLen &&
                                isHexadecimal(rawStr.charAt(currentIndex + 2)) &&
                                isHexadecimal(rawStr.charAt(currentIndex + 3)) &&
                                isHexadecimal(rawStr.charAt(currentIndex + 4)) &&
                                isHexadecimal(rawStr.charAt(currentIndex + 5))
                        ) {
                            result.append(Integer.toHexString(Integer.parseInt(rawStr.substring(currentIndex + 2, currentIndex + 6))));
                            currentIndex += 5;
                        }
                    }
                } else {
                    result.append(rawStr.charAt(currentIndex));
                }
                ++currentIndex;
            }
            ++currentIndex;
            return result.toString();
        } else {
            return null;
        }
    }

    private Object parseNumber() {
        String tag = "(Number)";
        int start = currentIndex;
        boolean isDouble = false;

        if (currentIndex < strLen && rawStr.charAt(currentIndex) == '-') {
            ++currentIndex;
        }

        if (currentIndex < strLen && rawStr.charAt(currentIndex) == '0') {
            ++currentIndex;
        } else if (currentIndex < strLen && rawStr.charAt(currentIndex) >= '1' && rawStr.charAt(currentIndex) <= '9') {
            ++currentIndex;
            while (currentIndex < strLen && rawStr.charAt(currentIndex) >= '0' && rawStr.charAt(currentIndex) <= '9') {
                ++currentIndex;
            }
        }

        if (currentIndex < strLen && rawStr.charAt(currentIndex) == '.') {
            isDouble = true;
            ++currentIndex;
            while (currentIndex < strLen && rawStr.charAt(currentIndex) >= '0' && rawStr.charAt(currentIndex) <= '9') {
                ++currentIndex;
            }
        }

        if (currentIndex < strLen && (rawStr.charAt(currentIndex) == 'e' || rawStr.charAt(currentIndex) == 'E')) {
            ++currentIndex;
            if (currentIndex < strLen && (rawStr.charAt(currentIndex) == '+' || rawStr.charAt(currentIndex) == '-')) {
                ++currentIndex;
            }
            while (currentIndex < strLen && rawStr.charAt(currentIndex) >= '0' && rawStr.charAt(currentIndex) <= '9') {
                ++currentIndex;
            }
        }

        if (currentIndex < strLen && currentIndex > start) {
            if (isDouble) {
                return Double.parseDouble(rawStr.substring(start, currentIndex));
            } else {
                return Integer.parseInt(rawStr.substring(start, currentIndex));
            }
        } else {
            return null;
        }
    }


    private void testComma() throws Exception {
        if (currentIndex < strLen && rawStr.charAt(currentIndex) != ',') {
            throw new Exception("Expected \",\". ");
        }
        ++currentIndex;
    }

    private void testColon() throws Exception {
        if (currentIndex < strLen && rawStr.charAt(currentIndex) != ':') {
            throw new Exception("Expected \":\". ");
        }
        ++currentIndex;
    }

    private void skipWhitespace() {
        while (currentIndex < strLen &&
                (rawStr.charAt(currentIndex) == ' ' ||
                        rawStr.charAt(currentIndex) == '\n' ||
                        rawStr.charAt(currentIndex) == '\t' ||
                        rawStr.charAt(currentIndex) == '\r'
                )
        ) {
            ++currentIndex;
        }
    }

    private boolean isHexadecimal(final char c) {
        return ((c >= '0' && c <= '9') ||
                (
                        (String.valueOf(c).toLowerCase().compareTo("a") >= 0) &&
                                (String.valueOf(c).toLowerCase().compareTo("f") <= 0)
                )
        );
    }
}