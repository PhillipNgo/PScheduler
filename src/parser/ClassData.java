package parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import ds.LinkedList;
import scheduler.VTCourse;

/**
 * Singleton to hold our classes instead of parsing every darn time!
 */
public class ClassData {
    private static 
        HashMap<String, HashMap<String, HashMap<String, LinkedList<VTCourse>>>> classMap = null;
    private static
        HashMap<String, ArrayList<String>> classLists = null;
    
    protected ClassData() {}
    
    private static void createClassMap() throws Exception {
        classMap = new HashMap<>();
        classMap.put("201701", VTParser.parseTermFile("201701"));
        classMap.put("201709", VTParser.parseTermFile("201709"));
        classMap.put("201801", VTParser.parseTermFile("201801"));
        classMap.put("201809", VTParser.parseTermFile("201809"));
    }
    
    public static HashMap<String, HashMap<String, LinkedList<VTCourse>>>
        getTermClasses(String term) throws Exception {
        if (classMap == null) {
            createClassMap();
        }
        return classMap.get(term);
    }
    
    private static ArrayList<String> readTermFile(String term) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        Scanner scan; //scanner to read from database
        try {
            scan = new Scanner(new File("webapps/ROOT/Database/" + term + ".txt"));
        } catch (Exception e) {
            scan = new Scanner(new File("WebContent/Database/" + term + ".txt"));
        }
        while (scan.hasNextLine()) {
            list.add(scan.nextLine());
        }
        scan.close();
        return list;
    }
    
    private static void createClassLists() throws IOException {
        classLists = new HashMap<>();
        classLists.put("201701", readTermFile("201701"));
        classLists.put("201709", readTermFile("201709"));
        classLists.put("201801", readTermFile("201801"));
        classLists.put("201809", readTermFile("201809"));
    }
    
    public static ArrayList<String>
        getTermSearchList(String term) throws IOException {
        if (classLists == null) {
            createClassLists();
        }
        return classLists.get(term);
    }
}
