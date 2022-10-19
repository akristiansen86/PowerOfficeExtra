/*
Oppgave 4

Base64 enkoding
https://www.codewars.com/kata/base64-encoding/csharp

Benytter padding.

Tar utgangspunkt i at jeg ikke skal bruke innebygde 
funksjoner i java for denne encoding/decoding som for 
eksempel: 

Base64.getDecoder()
Base64.getEncoder()



*/
//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

//import java.util.ArrayList;
//import java.util.List;
//import javax.print.attribute.standard.Finishings;
//import org.json.JSONObject;
//import org.skyscreamer.jsonassert.JSONAssert;
//import java.lang.*;


public class ExerciseFour {
	public static void main(String[] args){
        
        String text = "this is a string!!";
		String code = "dGhpcyBpcyBhIHN0cmluZyEh";
		String tempOne, tempTwo = "";
		String newText = "";

		char[] input_String = text.toCharArray();
		
        for(int i = 0; i < input_String.length; i++){
			tempOne = Integer.toBinaryString(input_String[i]);
			tempTwo = makeLetterBinary(tempOne);
			newText = newText + tempTwo;
        }
       	
        String fromCode = myDecode(code);
        String doneText = "";
        
        
        char[] goCode = fromCode.toCharArray();
        String temptemp = "";
        
        int counter = 0;
        while(counter < goCode.length-3){
            for(int k = 0; k < 8; k++){
                temptemp = temptemp + goCode[counter];
                counter++;	
            }
            doneText = doneText + (char)Integer.parseInt(temptemp, 2);

            temptemp = "";
        }
        
	}

    public static String fromBase64(String input_String){
        String binaryCode = (String)myDecode(input_String);
        String doneText = "";
        System.out.println(binaryCode);
        String text = "";
        char[] binaryArray = binaryCode.toCharArray();
        
        int counter = 0;
        while(counter < binaryArray.length-3){
            for(int k = 0; k < 8; k++){
                text = text + binaryArray[counter];
                counter++;	
            }
            
            doneText = doneText + (char)Integer.parseInt(text, 2);
            text = "";
        }
        return doneText;
    }
	
    public static String toBase64(String input_String){
        char[] text = input_String.toCharArray();
		String temp, code = "";
        String newText = "";
        for(int i = 0; i < text.length; i++){
			temp = Integer.toBinaryString(text[i]);
			temp = makeLetterBinary(temp);
			newText = newText + temp;
		}

        code = myEncode(newText);
       	
		return code;
    }
	
	/*Create all letters a binary number with 8 characters*/ 
	public static String makeLetterBinary(String binValue){
		char[] input_String = binValue.toCharArray();
        int addons = 8 - input_String.length;
		String newBinValue = "";

		for (int i = 0; i < addons; i++){
			newBinValue = newBinValue + "0";
		}
		newBinValue = newBinValue + binValue;
		return newBinValue; 
	}

    public static String makeNumberBinary(String binValue){
		char[] input_String = binValue.toCharArray();
        int addons = 6 - input_String.length;
		String newBinValue = "";

		for (int i = 0; i < addons; i++){
			newBinValue = newBinValue + "0";
		}
		newBinValue = newBinValue + binValue;
		return newBinValue; 
	}

    public static String myDecode(String codeString){
		char myBase64[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
        char[] readyToDecode = codeString.toCharArray();
		
        String binaryText = "";
		int counter = 0;
		int numInBase = 0;
        int check = 0;
        String temp = "";
        if(readyToDecode[readyToDecode.length-1] == '='){
            check = 2;
            if(readyToDecode[readyToDecode.length - 2] == '='){
                check = 4;
            }
        }
        while(counter < readyToDecode.length - check){
			for(int j = 0; j < myBase64.length; j++){
                if(readyToDecode[counter] == myBase64[j]){
                    numInBase = j;
                    break;
                }
            }
            temp = Integer.toBinaryString(numInBase);
			temp = makeNumberBinary(temp);
			binaryText = binaryText + temp;
            counter++;
		}
		return binaryText;
	}

    public static String myEncode(String binString){
		char myBase64[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
        char[] readyToEncode = binString.toCharArray();
		
        String codedText = "";
		String secureCode = "";
		int counter = 0;
		int numInBase = 0;
        while(counter < readyToEncode.length){
			if(readyToEncode.length - counter >= 6){
                for(int k = 0; k < 6; k++){
                    codedText = codedText + readyToEncode[counter];
                    counter++;	
                }
                numInBase = Integer.parseInt(codedText,2);
                secureCode = secureCode + myBase64[numInBase];
			    
            }else if(readyToEncode.length - counter == 4){
                for(int k = 0; k < 4; k++){
                    codedText = codedText + readyToEncode[counter];
                    counter++;	
                }
                codedText = codedText + "00";
                numInBase = Integer.parseInt(codedText,2);
                secureCode = secureCode + myBase64[numInBase] + "=";
			
            }else if(readyToEncode.length - counter == 2){
                for(int k = 0; k < 6; k++){
                    codedText = codedText + readyToEncode[counter];
                    counter++;	
                }
                codedText = codedText + "0000";
                numInBase = Integer.parseInt(codedText,2);
                secureCode = secureCode + myBase64[numInBase] + "==";
			
            }

		    codedText = "";
            
		}
        return secureCode;
	}

    /*Tester for å sjekke at programmet kjører slik oppgaven påkrever.
    For å forhindre tvetydighet for assertEquals sender jeg inn 
    objektet text og ikke stringen text.
    */
    @Test
    public void testFirst(){
        Object text = "this is a string!!";
		String code = "dGhpcyBpcyBhIHN0cmluZyEh";
        assertEquals(text, fromBase64(code), "NO!");
    }
    @Test
    public void testSecond(){
        String text = "this is a string!!";
		Object code = "dGhpcyBpcyBhIHN0cmluZyEh";
        assertEquals(code, toBase64(text), "NO!");
    }
}