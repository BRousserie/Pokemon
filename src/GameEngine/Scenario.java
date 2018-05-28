/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package GameEngine;

import FileIO.DataReader;
import FileIO.ReaderException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Classe dont l'objectif principal est d'alléger la lecture de GameEngine.
 * Permet également de sauter des parties du scénario plus rapidement en les
 * commentant, pendant le développement.
 *
 * @author Baptiste
 */
public class Scenario {

    public class ScenarioScript<String> implements Iterator<String> {

        int readIndex = 0;
        ArrayList<String> instructions = new ArrayList<>();

        ScenarioScript() {
            try {
                instructions = (ArrayList<String>)DataReader.readFile(eventFile).collect(Collectors.toList());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public boolean hasNext() {
            return (readIndex < instructions.size());
        }

        @Override
        public String next() {
            return (hasNext()) ? instructions.get(readIndex++) : (String)"END";
        }
        
        public String readNext() {
            try {
                return instructions.get(readIndex+1);
            } catch (IndexOutOfBoundsException e) {
                return (String)"END";
            }
        }

        public String current() {
            return (hasNext()) ? instructions.get(readIndex) : (String)"END";
        }
        
        public String previous() {
            return instructions.get(readIndex - 1);
        }
    };

    private int eventNumber;
    private String eventZone;
    private String eventFile;
    private ScenarioScript<String> eventInstructions;
    private boolean hasFXML;

    private Scenario(int eventNumber, String eventZone, String eventFile, boolean hasFXML) {
        this.eventNumber = eventNumber;
        this.eventZone = eventZone;
        this.eventFile = eventFile;
        this.hasFXML = hasFXML;
        eventInstructions = new ScenarioScript();
    }
    
    public Scenario (String eventFile) {
        this.eventFile = eventFile;
        eventInstructions = new ScenarioScript();
    }

    public String getEventZone() {
        return eventZone;
    }

    public String getEventFile() {
        return eventFile;
    }

    public int getEventNumber() {
        return eventNumber;
    }

    public boolean isHasFXML() {
        return hasFXML;
    }

    public ScenarioScript<String> Instructions() {
        return eventInstructions;
    }

    public static ArrayList<Scenario> loadScenarioFromFile() throws ReaderException {
        ArrayList<Scenario> scenario = new ArrayList<>();
        Stream<String[]> events = DataReader.readFileArray("scenario");
        events.forEach(event -> scenario.add(
                new Scenario(Integer.parseInt(event[0]), event[1], event[2], 
                        Boolean.parseBoolean(event[3]))));
        return scenario;
    }
}
