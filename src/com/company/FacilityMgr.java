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
        Facility pornhub = new Facility("vlxx",0);
        Facility swimming = new Facility("swimming-pool",1);
        Facility QUIT = null;
        //register them in Mgr
        this.addFacility(pornhub.name, pornhub);
        this.addFacility(swimming.name,swimming);
        this.addFacility("~Choose this option to terminate program~",QUIT);
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
        HashMap<Integer,String> temp = new HashMap<Integer,String>();
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
