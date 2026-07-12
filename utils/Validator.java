package utils;
public class Validator {
    
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}