package Classes.Email;

public class EmailTemplates {
    private static String welcome = "Welcome To Roombooking";
    private static String bookingConfirmation;


    public static String welcomeMessageBody(String recipientEmailName){
        return "Welcome" + " " +  recipientEmailName + " to Roombooking\n" +
                "We hope that you will enjoy our service\n" + "\n" +
                "Kind Regards, \n" +
                "The Roombooking Team";
    }

    public static String getWelcome() {
        return welcome;
    }

    public static void setWelcome(String welcome) {
        EmailTemplates.welcome = welcome;
    }

    public static String getBookingConfirmation() {
        return bookingConfirmation;
    }

    public static void setBookingConfirmation(String bookingConfirmation) {
        EmailTemplates.bookingConfirmation = bookingConfirmation;
    }
}
