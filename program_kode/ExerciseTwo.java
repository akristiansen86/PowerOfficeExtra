/*
Oppgave 2

Telle atomer i molekyler 
https://www.codewars.com/kata/molecule-to-atoms/csharp

(Du trenger ikke registrere deg. Bare løs i hht. oppgaveteksten 
og organiser og lever på samme måten som oppgave 1)
*/ 
//import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

//import javax.print.attribute.standard.Finishings;

import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
//import java.lang.*;

public class ExerciseTwo {
    public static void main(String[] args) {
        String t = "K4(ON(SO3)2)2";
        //String t = ""Mg(OH)2")"
        //String t = "H2O";
        JSONObject obj = new JSONObject();
        while (isNoSpecialSigns(t) == false) {
            t = parseMoleculeSigns(t);    
        }
        while (isNoNumbers(t) == false){
            t = parseMoleculeNumbers(t);
                      
        }
        obj = countMolecules(t);
        System.out.println(t);
        System.out.println(obj);
        
    }

    public static JSONObject parseMolecule(String t){
        JSONObject obj = new JSONObject();
        while (isNoSpecialSigns(t) == false) {
            t = parseMoleculeSigns(t);    
        }
        while (isNoNumbers(t) == false){
            t = parseMoleculeNumbers(t);
        }
        obj = countMolecules(t);
        return obj;
    }

    public static boolean isNoSpecialSigns(String t) {
        char[] input_Molecule = t.toCharArray();
        for(int i = input_Molecule.length - 1; i >= 0; i--){
            if (input_Molecule[i] == '(' || input_Molecule[i] == ')'){
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNoNumbers(String t) {
        char[] input_Molecule = t.toCharArray();
        
        for(int i = input_Molecule.length - 1; i >= 0; i--){
                
            if (Character.isDigit(input_Molecule[i])){
                return false;
            }
        }
        return true;
    }

    public static String[] addElement(String[] currentElements, String newElement){
        String[] moreElements = new String[currentElements.length + 1];

        for(int i= 0; i < currentElements.length; i++){
            moreElements[i] = currentElements[i];
        }

        int j = moreElements.length-1;
        moreElements[j] = newElement;
        return moreElements;
    }

    public static JSONObject countMolecules(String t){
        char[] input_Molecule = t.toCharArray();
        String[] moleculeElements = {};
        
        JSONObject obj = new JSONObject();
        String newElement = "";
        int numberAtoms = 0;
            
        for(int i = input_Molecule.length - 1; i >= 0; i--){
            if(Character.isLowerCase(input_Molecule[i])){
                newElement = newElement + input_Molecule[i-1] + input_Molecule[i];
                input_Molecule[i] = 2;
                input_Molecule[i-1] = 2;
                moleculeElements = addElement(moleculeElements, newElement);
                newElement = "";
            }else if (Character.isUpperCase(input_Molecule[i])){
                newElement = newElement + input_Molecule[i];
                moleculeElements = addElement(moleculeElements, newElement);
                newElement = "";
            }
                
            
        }
    
        for(int j = 0; j < moleculeElements.length; j++){
            String temp = moleculeElements[j];
            moleculeElements[j] = "2";
            if(temp != "2"){
                numberAtoms = 1;
                for(int k = 0; k < moleculeElements.length; k++){ 
                    if (moleculeElements[k].equals(temp)){
                        moleculeElements[k] = "2";
                        numberAtoms = numberAtoms + 1;
                        
                    }
                    
                }
                obj.put(temp, numberAtoms);        
            }   
        }
        return obj;
    }

    public static String parseMoleculeSigns(String t){
        String outputStr = "";
        char[] input_Molecule = t.toCharArray();
        int par = 0;
        int start = 0;
        int slutt = 0;
        int factor = 1;
        for (int i = input_Molecule.length - 1; i >= 0; i--){
            if(input_Molecule[i] == ')'){
                if (slutt == 0){
                    slutt = i;
                    factor = Integer.parseInt(String.valueOf(input_Molecule[i+1]));
                }
                par = par + 1;
                
            }else if (input_Molecule[i] == '('){
                par = par -1;
                if (par == 0){
                    start = i;
                    break;
                }
                
            }
            
        }
        for(int j = 0; j < factor; j++){
            for(int k = start+1; k < slutt; k++){
                outputStr = outputStr + input_Molecule[k];
            }
        }
        for(int l = 0; l < start; l++){
            outputStr = outputStr + input_Molecule[l];
        }
        if (slutt + 2 < input_Molecule.length-1){
            for(int m = slutt + 2; m < input_Molecule.length; m++){
                outputStr = outputStr + input_Molecule[m];
            }
        }
        
        return outputStr;
    }
    
    public static String parseMoleculeNumbers(String t){
        String outputStr = "";
        char[] input_Molecule = t.toCharArray();
        int numberAtoms = 0;
            
        for (int i = input_Molecule.length - 1; i >= 0; i--) {
            if(Character.isDigit(input_Molecule[i]) == true){
                numberAtoms = Integer.parseInt(String.valueOf(input_Molecule[i]));
                if(Character.isUpperCase(input_Molecule[i-1])){
                    for (int k = 0; k < numberAtoms; k++){
                        outputStr = outputStr + input_Molecule[i-1];
                    }
                }else if(Character.isLowerCase(input_Molecule[i-1])){
                    for (int k = 0; k < numberAtoms; k++){
                        outputStr = outputStr + input_Molecule[i-2] + input_Molecule[i-1];
                    }
                } 
                            
            }else if(Character.isUpperCase(input_Molecule[i])){
                if (i+1 < input_Molecule.length){
                    if(Character.isDigit(input_Molecule[i+1]) != true || Character.isLowerCase(input_Molecule[i+1])){
                        outputStr = outputStr + input_Molecule[i];
                    }
                }else{
                    outputStr = outputStr + input_Molecule[i];
                }
                
            }
    
        } 
        return outputStr;
    }


    /*Tester for å sjekke at programmet kjører slik oppgaven påkrever.
    */
    @Test
    public void testFirst(){
        JSONObject obj = new JSONObject();
        obj.put("H", 2);
        obj.put("O", 1);
        JSONAssert.assertEquals(obj, parseMolecule("H2O"), true);
    }
    @Test
    public void testSecond(){
        JSONObject obj = new JSONObject();
        obj.put("Mg", 1);
        obj.put("O", 2);
        obj.put("H", 2);
        JSONAssert.assertEquals(obj, parseMolecule("Mg(OH)2"), true); 
    }
    @Test
    public void testThird(){
        JSONObject obj = new JSONObject();
        obj.put("S", 4);
        obj.put("K", 4);
        obj.put("N", 2);
        obj.put("O", 14);
        JSONAssert.assertEquals(obj, parseMolecule("K4(ON(SO3)2)2"), true);  
    }
}