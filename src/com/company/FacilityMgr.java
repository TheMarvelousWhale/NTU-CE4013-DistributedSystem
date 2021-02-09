package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * The singleton Facility Mgr to keep all instances of all facilities in one place for ALL clients
 */
public class FacilityMgr {
    private static FacilityMgr single_instance = null;
    public HashMap<String, Facility> FacilityRecords;
    //I will make this a singleton class
    private FacilityMgr(){
        this.FacilityRecords = new HashMap<String, Facility>();

        //create facil
        Facility[] FacilityArray = new Facility[] {
                                     new Facility("cornhub",0),
                                     new Facility("swimming-pool",1),
                                     new Facility("library",2),
                                };
        //register them in Mgr
        for (Facility f: FacilityArray)
            this.addFacility(f.name,f);
        this.addFacility("~Choose this option to terminate program~",null);   //this null option is here for client to terminate program
    }

    public static FacilityMgr getInstance() {
        if (single_instance == null)
            single_instance = new FacilityMgr();
        return single_instance;
    }

    private void addFacility(String name, Facility facility){
        this.FacilityRecords.put(name,facility);
    }

    public Facility getFacility(String facil){
        /**
         * @see: getUserToChooseFacil
         * Same idea, but no user input
         */
        if (this.FacilityRecords.containsKey(facil))
            return this.FacilityRecords.get(facil);
        else
            return null;
    }

    public Facility getUserToChooseFacil(Utils utils) {
        /**
         * This function get the user to choose one of the available facilities
         * And return that facility reference to the caller
         */
        utils.println("Currently these are the available facilities for booking:");
        int count = 1;

        //keep track of the index of the (later) alphabetically ordered facility
        HashMap<Integer,String> temp = new HashMap<Integer,String>();

        //sort the facil name by alphabetical order
        ArrayList<String> sortedKeys =  new ArrayList<String>(this.FacilityRecords.keySet());
        Collections.sort(sortedKeys);
        for (String i : sortedKeys) {
            utils.println("\t"+count+". "+i);
            temp.put(count,i);
            count++;
        }
        int choice = utils.checkUserIntInput(1, count - 1);            //count was ++ on the last iter of the for loop
        Facility facility = this.FacilityRecords.get(temp.get(choice));
        return facility;
    }

}
