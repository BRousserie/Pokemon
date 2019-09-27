/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileIO;

import GameEngine.Game;
import com.eclipsesource.json.WriterConfig;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author brousserie
 */
public class DataWriter
{

    /**
     * Writes the save in a txt file and create it if it doesn't exist
     * @param game
     */
    public static void saveGame(Game game)
    {
        try {
            File f = new File("data/save.txt");
            if (!f.exists())
                f.createNewFile();
            FileWriter fw = new FileWriter(f);
            fw.write(game.save().toString(WriterConfig.PRETTY_PRINT));
            System.out.println(game.save().toString(WriterConfig.PRETTY_PRINT));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
