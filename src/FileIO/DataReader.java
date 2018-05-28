/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileIO;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author brousserie
 */
public class DataReader {

    public static String normalyzeUpperOnFirst(String stringToConvert) {
        /*Because the loading of a data wouldn't work if not respecting case,
          we can modify name to make sure in fits with file datas then retry*/
        String lower = stringToConvert.toLowerCase();
        String upper = stringToConvert.toUpperCase();
        stringToConvert = upper.charAt(0) + lower.substring(1);
        return stringToConvert;
    }

    public static String getTextAt(String separator, String line) {
        //Cuts what is before the wanted data
        line = line.substring(line.indexOf(separator) + separator.length());
        line = line.substring(0, line.indexOf(";"));
        return line;
    }

    private static String getLine(int numLine, BufferedReader file) throws IOException {
        for (int i = 1; i < numLine; i++) {
            file.readLine();
        }
        return file.readLine();
    }

    public static HashMap<Integer, String> linkDataNUM(String values) {
        HashMap<Integer, String> linkedDatas = new HashMap<>();
        while (values.contains("/")) {
            int key = values.indexOf("-");
            int value = values.indexOf("/");
            linkedDatas.put(Integer.parseInt(values.substring(0, key)),
                    values.substring(key + 1, value));
            values = values.substring(value + 1);
        }
        return linkedDatas;
    }

    public static HashMap<String, Integer> linkDataTXT(String values) {
        HashMap<String, Integer> linkedDatas = new HashMap<>();
        while (values.contains("/")) {
            int key = values.indexOf("-");
            int value = values.indexOf("/");
            linkedDatas.put(values.substring(0, key),
                    Integer.parseInt(values.substring(key + 1, value)));
            values = values.substring(value + 1);
        }
        return linkedDatas;
    }
    
    public static String[] readFileArray(String filename, String target) throws ReaderException {
        try {
            return new BufferedReader(new FileReader(new File("data/"+filename+".txt")))
                    .lines()
                    .filter(e -> e.startsWith(target))
                    .findFirst()
                    .get()
                    .split(";");
        } catch (Exception e) {
            throw new ReaderException("Impossible de lire le fichier "+filename+".\n"+e.getMessage());
        }
    }
    
    public static Stream<String> readFile(String fileName) throws ReaderException {
        try {
            return new BufferedReader(new FileReader(new File("data/"+fileName+".txt")))
                    .lines();
        } catch (Exception e) {
            throw new ReaderException("Impossible de lire le fichier "+fileName+".\n"+e.getMessage());
        }
    }
    
    public static Stream<String[]> readFileArray(String fileName) throws ReaderException {
        return readFile(fileName).map(e -> e.split(";"));
    }

    public static float readTypeDamageRate(int atkIndex, int defIndex) throws ReaderException {
        try {
            BufferedReader file = new BufferedReader(new FileReader(new File("data/types.txt")));

            return Float.parseFloat(getTextAt(defIndex + "_", getLine(atkIndex, file)));
        } catch (Exception e) {
            throw new ReaderException("Impossible de lire le fichier types.txt" + e.getMessage());
        }
    }

    public static JsonObject loadGame() {
        try {
            BufferedReader file = new BufferedReader(new FileReader(new File("data/save.txt")));
            return Json.parse(file.lines().collect(Collectors.joining())).asObject();
        } catch (IOException e) {
            System.out.println("Aucun fichier de sauvegarde n'a été trouvé");
        }
        return null;
    }
}
