/*
Oppgave 3

Reverse Polish Notation 
https://www.codewars.com/kata/reverse-polish-notation-calculator/csharp

(Du trenger ikke registrere deg. Bare løs i hht. oppgaveteksten 
og organiser og lever på samme måten som oppgave 1)
*/ 
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

//import javax.print.attribute.standard.Finishings;

import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
//import java.lang.*;

public class ExerciseThree {
    public static void main(String[] args) {
        String t = "512+4*+3-";
        int position;
        int count = 0;
        
        while (isNoMathSigns(t) == false) {
            count = count + 1;
            System.out.println(count);
            position = findPosition(t);
            t = updateString(t, position);
            while(isNoSpecialSigns(t) == false){
                t = updateString(t, -1);
            }   
        }      
        
    }

    public static int reversePolish(String t){
        int position;
        int count = 0;
        if (t == ""){
            return 0;
        }
        while (isNoMathSigns(t) == false) {
            count = count + 1;
            System.out.println(count);
            position = findPosition(t);
            t = updateString(t, position);
            while(isNoSpecialSigns(t) == false){
                t = updateString(t, -1);
            }   
        }
        int res = Integer.parseInt(String.valueOf(t));
        return res;
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

    public static boolean isNoMathSigns(String t) {
        char[] input_Query = t.toCharArray();
        for(int i = 0; i < input_Query.length - 1; i++){
            if (Character.isDigit(input_Query[i]) != true){
                return false;
            }
        }
        return true;
    }
    
    public static int findPosition(String t) {
        char[] input_Query = t.toCharArray();
        int check = 0;
        for(int i = 0; i < input_Query.length; i++){
            if (Character.isDigit(input_Query[i]) != true && check == 0){
                if(input_Query[i] == '(' || input_Query[i] == ')'){
                    
                }
                else{
                    check = i;
                }
            }
        }
        return check;
    }

    public static String updateString(String t, int position) {
        char[] input_Query = t.toCharArray();
        String result = "";
        String newMathPart = "";
        int check = 0;
        int res = 0;
        String largeNum = "";

        if (input_Query.length < 5 && position == -1){
            newMathPart = newMathPart + input_Query[1] + input_Query[2];
        }
        else if (position == -1){
            int newPosition = findPosition(t);
            int newCheck = 0;
            int a = 0;
            int b = 0;
            for(int i = 0; i < input_Query.length; i++){
                if (i < newPosition - 5){
                    newMathPart = newMathPart + input_Query[i];    
                }else if(i >= newPosition - 5 && i <= newPosition && newCheck == 0){
                    newCheck = 1;
                    if(input_Query[newPosition-1] == ')'){
                        largeNum = largeNum + input_Query[newPosition - 3] + input_Query[newPosition - 2];
                        a = Integer.parseInt(String.valueOf(input_Query[newPosition - 5]));
                        b = Integer.parseInt(String.valueOf(largeNum));
                    }else{
                        largeNum = largeNum + input_Query[newPosition - 4] + input_Query[newPosition - 3];
                        a = Integer.parseInt(String.valueOf(largeNum));
                        b = Integer.parseInt(String.valueOf(input_Query[newPosition - 1]));   
                    }
                    if (input_Query[newPosition] == '+'){
                        res = a + b;
                        
                    }else if (input_Query[newPosition] == '-'){
                        res = a - b;
                        
                    }else if (input_Query[newPosition] == '*'){
                        res = a * b;
                        
                    }else if (input_Query[newPosition] == '/'){
                        res = a / b;
                        
                    }
                    if(res >= 10){
                        result = Integer.toString(res); 
                        newMathPart = newMathPart + '(' + result + ')';    
                    }else{
                        result = Integer.toString(res); 
                        newMathPart = newMathPart + result;
                    }
                }if (i > newPosition){
                    newMathPart = newMathPart + input_Query[i];
                }
            }

        }else{
            for(int i = 0; i < input_Query.length; i++){
                if (i < position - 2){
                    newMathPart = newMathPart + input_Query[i];
                }else if(i >= position - 2 && i <= position && check == 0){
                    check = 1;
                    int a = Integer.parseInt(String.valueOf(input_Query[position - 2]));
                    int b = Integer.parseInt(String.valueOf(input_Query[position - 1]));
                    if (input_Query[position] == '+'){
                        res = a + b;
                        
                    }else if (input_Query[position] == '-'){
                        res = a - b;
                        
                    }else if (input_Query[position] == '*'){
                        res = a * b;
                        
                    }else if (input_Query[position] == '/'){
                        res = a / b;
                        
                    }
                    if(res >= 10){
                        result = Integer.toString(res); 
                        newMathPart = newMathPart + '(' + result + ')';    
                    }else{
                        result = Integer.toString(res); 
                        newMathPart = newMathPart + result;
                    }
                }
                
                if (i > position){
                    newMathPart = newMathPart + input_Query[i];
                    
                }
                
            }
        }
    
        return newMathPart;

    }

    
    /*Tester for å sjekke at programmet kjører slik oppgaven påkrever.
    */
    @Test
    public void testFirst(){
        String expression = "512+4*+3-";
        int i = 14;
        assertEquals(i, reversePolish(expression));
    }
    @Test
    public void testSecond(){
        String expression = "";
        int i = 0;
        assertEquals(i, reversePolish(expression));
    }
}