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
public class Scenario
{

    /**
     *Class permittting to use the scenario 
     * 
     * @param <String>
     */
    public class ScenarioScript<String> implements Iterator<String>
    {

    // <editor-fold defaultstate="collapsed" desc="Iterator and methods">
        int readIndex = 0;
        ArrayList<String> instructions = new ArrayList<>();

        ScenarioScript()
        {
            try {
                instructions = (ArrayList<String>) DataReader.readFile(eventFile).collect(Collectors.toList());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public boolean hasNext()
        {
            return (readIndex < instructions.size());
        }

        @Override
        public String next()
        {
            return (hasNext()) ? instructions.get(readIndex++) : (String) "END";
        }

        
        /**
         *  Reads the next line of the file if it's not the end
         * @return the text or END
         */
        public String readNext()
        {
            try {
                return instructions.get(readIndex + 1);
            } catch (IndexOutOfBoundsException e) {
                return (String) "END";
            }
        }

        /**
         * Reds the current line if it exist and END if it's the end of the file
         * @return current line or END
         */
        public String current()
        {
            return (hasNext()) ? instructions.get(readIndex) : (String) "END";
        }

        /**
         * Reads the previous line of the file
         * @return
         */
        public String previous()
        {
            return instructions.get(readIndex - 1);
        }
    };
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    private int eventNumber;
    private String eventZone;
    private String eventFile;
    private ScenarioScript<String> eventInstructions;
    private boolean hasFXML;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Constructor for events with a number, a zone, a file specified
     * @param eventNumber
     * @param eventZone
     * @param eventFile
     * @param hasFXML 
     */
    private Scenario(int eventNumber, String eventZone, String eventFile, boolean hasFXML)
    {
        this.eventNumber = eventNumber;
        this.eventZone = eventZone;
        this.eventFile = eventFile;
        this.hasFXML = hasFXML;
        eventInstructions = new ScenarioScript();
    }

    /**
     * Constructor for events
     * @param eventFile
     */
    public Scenario(String eventFile)
    {
        this.eventFile = eventFile;
        eventInstructions = new ScenarioScript();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * Gets the event of  zone
     * @return the event in the zone
     */
    public String getEventZone()
    {
        return eventZone;
    }

    /**
     * Gets the file where the event is stored
     * @return the file 
     */
    public String getEventFile()
    {
        return eventFile;
    }

    /**
     * gets the number of the event 
     * @return teh event number
     */
    public int getEventNumber()
    {
        return eventNumber;
    }

    /**
     * Says if the event has a specific view
     * @return true or false
     */
    public boolean isHasFXML()
    {
        return hasFXML;
    }

    /**
     * Returns the dialog of an event 
     * @return the event instructions
     */
    public ScenarioScript<String> Instructions()
    {
        return eventInstructions;
    }

    /**
     * Writes the line of the file in an arrayList
     * @return the list
     * @throws ReaderException
     */
    public static ArrayList<Scenario> loadScenarioFromFile() throws ReaderException
    {
        ArrayList<Scenario> scenario = new ArrayList<>();
        Stream<String[]> events = DataReader.readFileArray("scenario");
        events.forEach(
                event -> scenario.add(
                        new Scenario(Integer.parseInt(event[0]), event[1], event[2],
                                     !event[2].startsWith("events"))));
        return scenario;
    }
    // </editor-fold>
}
