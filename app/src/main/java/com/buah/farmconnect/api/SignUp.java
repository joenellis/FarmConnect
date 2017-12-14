package com.buah.farmconnect.api;

public class SignUp {

    static String firstName;
    static String lastName;
    static String number;
    static String email;
    static String password;
    static String securityQuestion_id;
    static String answer;

    public static String getFullName() {
        return SignUp.firstName + " " + SignUp.lastName;
    }

    public static void setFirstName(String firstName) {
        SignUp.firstName = firstName;
    }

    public static void setLastName(String lastName) {
        SignUp.lastName = lastName;
    }

    public static String getNumber() {
        return number;
    }

    public static void setNumber(String number) {
        SignUp.number = number;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        SignUp.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        SignUp.password = password;
    }

    public static String getSecurityQuestion_id() {
        return securityQuestion_id;
    }

    public static void setSecurityQuestion_id(String securityQuestion_id) {
        SignUp.securityQuestion_id = securityQuestion_id;
    }

    public static String getAnswer() {
        return answer;
    }

    public static void setAnswer(String answer) {
        SignUp.answer = answer;
    }
}
