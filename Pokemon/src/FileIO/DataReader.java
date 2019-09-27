/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package FileIO;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author brousserie
 */
public class DataReader
{

    /**
     * Sets the first char to upper case and the others to lower case
     * 
     * @param stringToConvert String that has to be normalyzed
     * @return The string normalyzed
     */
    public static String normalyzeUpperOnFirst(String stringToConvert)
    {
        /*Because the loading of a data wouldn't work if not respecting case,
          we can modify name to make sure in fits with file datas then retry*/
        String lower = stringToConvert.toLowerCase();
        String upper = stringToConvert.toUpperCase();
        stringToConvert = upper.charAt(0) + lower.substring(1);
        return stringToConvert;
    }

    /**
     * Makes an HashMap grouping data out of a string
     * 
     * @param values String containing the datas to group
     * @return The grouped datas
     */
    public static HashMap<String, Integer> linkDataTXT(String values)
    {
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

    /**
     * Returns the line that starts with the specified target, from the 
     * specified file, splitting datas using ";"
     * 
     * @param filename file to take data from
     * @param target beginning of the line you're looking for
     * @return an array containing all datas of the target from the indicated file
     * @throws ReaderException if the encoding is wrong or the file not found
     */
    public static String[] readFileArray(String filename, String target) throws ReaderException
    {
        try {
            BufferedReader file
                           = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream("data/" + filename + ".txt"), "UTF-8"));
            String line;
            while (!(line = file.readLine()).startsWith(target)) {
            }
            return line.split(";");
        } catch (Exception e) {
            throw new ReaderException("Impossible de lire le fichier " + filename + ".\n" + e.getMessage());
        }
    }

    /**
     * Reads the file and takes it into a Stream 
     * 
     * @param fileName file to take datas from
     * @return the whole file, splited line by line into a Stream
     * @throws ReaderException if the encoding's wrong or the file not found
     */
    public static Stream<String> readFile(String fileName) throws ReaderException
    {
        try {
            return new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("data/" + fileName + ".txt"), "UTF-8"))
                    .lines();
        } catch (Exception e) {
            throw new ReaderException("Impossible de lire le fichier " + fileName + ".\n" + e.getMessage());
        }
    }

    /**
     * Reads the file and takes it into a Stream, splitting lines using ";"
     * 
     * @param fileName file to take datas from
     * @return the whole file, line by line into a Stream, splitted using ";"
     * @throws ReaderException if the encoding's wrong or the file not found
     */
    public static Stream<String[]> readFileArray(String fileName) throws ReaderException
    {
        return readFile(fileName).map(e -> e.split(";"));
    }

    /**
     * Returns the game that has previously been saved
     * @return
     */
    public static JsonObject loadGame()
    {
        try {
            BufferedReader file
                           = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream("data/save.txt"), "UTF-8"));
            return Json.parse(file.lines().collect(Collectors.joining())).asObject();
        } catch (IOException e) {
            System.out.println("Aucun fichier de sauvegarde n'a été trouvé");
        }
        return new JsonObject();
    }
}
