package org.jssec.android.sqlite;

public class DataValidator {
    //Validate the Input value
    //validate numeric characters
    public static boolean validateNo(String idno) {
        //null and blank are OK
        if (idno == null || idno.length() == 0) {
           return true;
        }

        //Validate that it's numeric character.
        try {
            if (!idno.matches("[1-9][0-9]*")) {
                //Error if it's not numeric value 
                return false;
            }
        } catch (NullPointerException e) {
            //Detected an error
            return false;
        }

        return true;
    }

    // Validate the length of a character string
    public static boolean validateLength(String str, int max_length) {
       //null and blank are OK
       if (str == null || str.length() == 0) {
               return true;
       }

       //Validate the length of a character string is less than MAX
       try {
           if (str.length() > max_length) {
               //When it's longer than MAX, error
               return false;
           }
       } catch (NullPointerException e) {
           //Bug
           return false;
       }

       return true;
    }

   // Validate the Input value
    public static boolean validateData(String idno, String name, String info) {
       if (!validateNo(idno)) {
           return false;
        }
       if (!validateLength(name, CommonData.TEXT_DATA_LENGTH_MAX)) {
           return false;
        }else if(!validateLength(info, CommonData.TEXT_DATA_LENGTH_MAX)) {
           return false;
        }    
       return true;
    }
}
