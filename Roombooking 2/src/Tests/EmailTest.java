package Tests;

import Classes.Email.EmailUtil;
import Classes.Email.TLSEmail;
import Tools.DbFunctionality;
import Tools.DbTool;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;

public class EmailTest {
    DbTool dbTool;
    DbFunctionality dbFunctionality;
    EmailUtil emailUtil;
    TLSEmail tlsEmail;
    Connection testConnection;

    @Before
    public void init() {
        System.out.println("test init of email");
        dbTool = new DbTool();
        dbFunctionality = new DbFunctionality();
        testConnection = dbTool.dbLogIn();
    }
    @Test
    public void testSendEmail() {

    }




}
