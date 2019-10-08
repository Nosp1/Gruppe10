package Classes.Email;

import Classes.Order;

/**
 * Handles Email templates to insert into Email Util for easy content.
 *
 * @author trym
 */
public class EmailTemplates {
    private static String welcome = "Welcome To Roombooking";
    private static String bookingReceipt = "Booking receipt";

    /**
     *
     * @param recipientEmailName the recipient of the email
     * @return the body of the email with the recipients name added to the string.
     */
    public static String welcomeMessageBody(String recipientEmailName) {
        return "Welcome" + " " + recipientEmailName + " to Roombooking\n" +
                "We hope that you will enjoy our service\n" + "\n" +
                "Kind Regards, \n" +
                "The Roombooking Team";
    }

    /**
     *
     * @param recipientEmailName the recipient of the email
     * @param order object is sent with the method to access order properties, such as the orderID, orderName, roomId etc.
     * @return the body of the email with recipients name and order properties added to the string.
     */
    public static String bookingConfirmation(String recipientEmailName, Order order) {
        return "Reservation number " + order.getID() + "\n" +
                recipientEmailName + " you have successfully booked " + order.getRoomID() +
                " from" + order.getTimestampStart() + " -- " + order.getTimestampEnd()
                + "\n" + "for any inquires regarding your order please send us an email at \n" +
                "grproom@gmail.com with your order reservation number found in this email" + "\n" +
                "\n " + "Kind Regards, \n" +
                "The Roombooking Team";

    }

    /**
     * Getters and Setters.
     * @return variable welcome as a string
     */

    public static String getWelcome() {
        return welcome;
    }


    public static void setWelcome(String welcome) {
        EmailTemplates.welcome = welcome;
    }

    public static String getBookingReceipt() {
        return bookingReceipt;
    }

    public static void setBookingReceipt(String bookingReceipt) {
        EmailTemplates.bookingReceipt = bookingReceipt;
    }
}
