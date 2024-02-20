package io.getarrays.securecapita.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import static com.twilio.rest.api.v2010.account.Message.creator;


/**
 * Description of SmsUtils
 *
 * @author mac
 * @version 1.0
 * @since 2/20/24
 **/
public class SmsUtils {
    //all from twillo
    public static final String FROM_NUMBER = "+8615009005715";
    public static final String SID_KEY = "huy";
    public static final String TOKEN_KEY = "huy";

    public static void sendSMS(String to, String messageBody) {
        Twilio.init(SID_KEY,TOKEN_KEY);
        Message message = creator(new PhoneNumber("+" + to), new PhoneNumber(FROM_NUMBER),messageBody).create();
        System.out.println(message);

    }
}
