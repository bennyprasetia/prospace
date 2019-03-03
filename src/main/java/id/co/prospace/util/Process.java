/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.prospace.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Benny Prasetia
 */
public class Process {

    static Map<String, String> tokenRomanValueMapping = new HashMap<String, String>();
    static Map<String, Float> tokenIntegerValue = new HashMap<String, Float>();
    static Map<String, String> questionAndReply = new HashMap<String, String>();
    static ArrayList<String> missingValues = new ArrayList<String>();
    static Map<String, Float> elementValueList = new HashMap<String, Float>();

    public static void proccessInputData(String filePath) throws IOException {
        BufferedReader bufferedReader = null;
        if (filePath == null) {
            InputStream in = Process.class.getResourceAsStream("Input");
            bufferedReader = new BufferedReader(new InputStreamReader(in));
        } else {
            FileReader fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
        }
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            processLine(line);
        }
        bufferedReader.close();
    }

    public static void processLine(String line) {
        String arr[] = line.split("((?<=:)|(?=:))|( )");

        if (line.endsWith("?")) {
            questionAndReply.put(line, "");
        } else if (arr.length == 3 && arr[1].equalsIgnoreCase("is")) {
            tokenRomanValueMapping.put(arr[0], arr[arr.length - 1]);
        } else if (line.toLowerCase().endsWith("credits")) {
            missingValues.add(line);
        }
    }

    public static void MapTokentoIntegerValue() {

        Iterator it = tokenRomanValueMapping.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry token = (Map.Entry) it.next();
            float integerValue = new ConvertToDecimal().numberToDecimal(token.getValue().toString());
            tokenIntegerValue.put(token.getKey().toString(), integerValue);
        }
        mapMissingEntities();
    }

    private static void mapMissingEntities() {
        for (int i = 0; i < missingValues.size(); i++) {
            deCodeMissingQuery(missingValues.get(i));
        }
    }

    private static void deCodeMissingQuery(String query) {
        String array[] = query.split("((?<=:)|(?=:))|( )");
        int splitIndex = 0;
        int creditValue = 0;
        String element = null;
        String[] valueofElement = null;
        for (int i = 0; i < array.length; i++) {
            if (array[i].toLowerCase().equals("credits")) {
                creditValue = Integer.parseInt(array[i - 1]);
            }
            if (array[i].toLowerCase().equals("is")) {
                splitIndex = i - 1;
                element = array[i - 1];
            }
            valueofElement = java.util.Arrays.copyOfRange(array, 0, splitIndex);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < valueofElement.length; j++) {
            stringBuilder.append(tokenRomanValueMapping.get(valueofElement[j]));
        }
        float valueOfElementInDecimal = new ConvertToDecimal().numberToDecimal(stringBuilder.toString());
        elementValueList.put(element, creditValue / valueOfElementInDecimal);
    }

    public static void processOutputData() {
        Map<String, String> map = questionAndReply;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            processReply(entry.getKey());
        }
    }

    private static void processReply(String query) {
        if (query.toLowerCase().startsWith("how much")) {
            findValueOfRoman(query);
        } else if (query.toLowerCase().startsWith("how many")) {
            findValueOfElement(query);
        }

    }

    public static void findValueOfRoman(String query) {
        if (isValidinput(query) == true) {
            ArrayList<String> tokenValueToRoman = new ArrayList<String>();
            ArrayList<String> tokenValue = splitQuery(query);
            for (int i = 0; i < tokenValue.size(); i++) {
                tokenValueToRoman.add(tokenRomanValueMapping.get(tokenValue.get(i)));
            }
            float value = new ConvertToDecimal().numberToDecimal(tokenValueToRoman.toString());
            tokenValue.add("is");
            tokenValue.add(Float.toString(value));
            System.out.println(query + " " + outputFormatter(tokenValue));
        } else {
            System.err.println(query + " : I have no idea what you are talking about");
        }
    }

    private static void findValueOfElement(String query) {
        if (isValidinput(query) == true) {
            ArrayList<String> tokenValue = splitQuery(query);
            ArrayList<String> tokenValueToRoman = new ArrayList<String>();
            String element = null;
            for (int i = 0; i < tokenValue.size(); i++) {
                if (tokenRomanValueMapping.get(tokenValue.get(i)) != null) {
                    tokenValueToRoman.add(tokenRomanValueMapping.get(tokenValue.get(i)));
                } else if (elementValueList.get(tokenValue.get(i)) != null) {
                    element = tokenValue.get(i);
                } else {
                    System.err.println(query + " : I have no idea what you are talking about");
                }
            }
            float elementValue = (new ConvertToDecimal().numberToDecimal(tokenValueToRoman.toString()) * elementValueList.get(element));
            tokenValue.add("is");
            tokenValue.add(Float.toString(elementValue));
            tokenValue.add("Credits");
            System.out.println(query + " " + outputFormatter(tokenValue));
        } else {
            System.err.println(query + " : I have no idea what you are talking about");
        }
    }

    private static String outputFormatter(ArrayList<String> output) {
        return output.toString().replace(",", "").replace("[", "").replace("]", "");
    }

    private static boolean isValidinput(String query) {
        Pattern regex = Pattern.compile("[$&+,:;=@#|]");
        Matcher matcher = regex.matcher(query);
        if (matcher.find()) {
            return false;
        } else {
            return true;
        }

    }

   
    private static ArrayList<String> splitQuery(String query) {
        ArrayList<String> queryArray = new ArrayList<String>(Arrays.asList(query.split("((?<=:)|(?=:))|( )")));
        int startIndex = 0, endIndex = 0;
        for (int i = 0; i < queryArray.size(); i++) {
            if (queryArray.get(i).toLowerCase().equals("is")) {
                startIndex = i + 1;
            } else if (queryArray.get(i).toLowerCase().equals("?")) {
                endIndex = i;

            }
        }
        String[] array = queryArray.toArray(new String[queryArray.size()]);
        return new ArrayList<String>(Arrays.asList(java.util.Arrays.copyOfRange(array, startIndex, endIndex)));

    }
}
