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
public class DataWriter {

    public static void saveGame(Game game) {
        try {
            File f = new File("data/save.txt");
            if (!f.exists())f.createNewFile();
            FileWriter fw = new FileWriter(f);
            fw.write(game.save().toString(WriterConfig.PRETTY_PRINT));
            System.out.println(game.save().toString(WriterConfig.PRETTY_PRINT));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Old way, without Json
//    public static String saveGame() {
//        try {
//            File f = new File("save.txt");
//            f.createNewFile();
//            FileWriter fw = new FileWriter(f);
//            fw.write(formatPlayerInfos());
//            fw.close();
//            return "Sauvegarde effectuée avec succès";
//        } catch (Exception e) {
//            return "Erreur de sauvegarde";
//        }
//    }
//    public static String formatPlayerInfos() {
//        String infos;
//        Player player = Game.getGame().getPlayer();
//
//        infos = Game.getGame().getCurrentZone().getZoneType() + "\n";
//        infos += Game.getGame().getCurrentZone().getName() + "\n";
//        infos += Game.getGame().getCurrentStoryEvent() + "\n";
//        
//        infos += player.getBag().getPokeDollars()+"\n";
//
//        for (Item item : player.getBag().getItems().keySet()) {
//            infos += item.getClass().getName() + "\n" + item.getName() + "\n" + player.getBag().getItems().get(item)+"\n";
//        }
//        infos += "EndItems\n";
//        for (CapturedPkmn pkmn : player.getMyPokemons()) {
//            infos += "Pkmn:\n" + pkmn.getName() + "\n" + pkmn.getLevel() + "\n" + pkmn.getID() + "\n";
//            for (int i = 0; i < 5; i++) {
//                infos += pkmn.getIVs()[i] + "\n";
//            }
//            for (int i = 0; i < 5; i++) {
//                infos += pkmn.getEVs()[i] + "\n";
//            }
//            infos += pkmn.getExp() + "\n";
//            for (Attack atk : pkmn.getAttacks()) {
//                infos += "ATK"+ atk.getName() + "\n";
//            }
//            if (pkmn.getPkmnStatus() != null) {
//                infos += "ST" + pkmn.getPkmnStatus() + "\n";
//            }
//            if (pkmn.getItem() != null) {
//                Item item = pkmn.getItem();
//                infos += "IT" + item.getClass().getName() + "\n" + item.getName() + "\n" + player.getBag().getItems().get(item)+"\n";
//            }
//            infos += "\n";
//        }
//        infos += "EndPkmns\n";
//        for (FightingPkmn pkmn : player.getPkmns()) {
//            infos += pkmn.getID()+";";
//        }
//        infos += "\nEND";
//
//        return infos;
//    }
}
