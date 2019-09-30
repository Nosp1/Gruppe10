package Tests;

public class EmailTemplates {
    private static String welcome = "Welcome To Roombooking";
    private static String bookingConfirmation;


    public void welcomeMessageBody(String recipientEmailName){
        String body = "Welcome" + recipientEmailName + " to Roombooking\n" +
                "We hope that you will enjoy our service\n" +
                "Kind Regards, \n" +
                "The Roombooking Team";

    }








}
