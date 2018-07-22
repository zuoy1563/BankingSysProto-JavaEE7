package bsp.common;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zoe on 8/5/17.
 */
public class Validation {

    /**
     * Validate that if the amount is greater than 0
     * @param amount
     * @return true or false
     */
    public static boolean amountShouldBeGreaterThan0(float amount) {
        if (amount > 0)
            return true;
        else
            throw new IllegalArgumentException("Amount should be greater than 0!");
    }

    /**
     * Validate that if the float is equal or greater than 0
     * @param f
     * @param desc
     * @return true or false
     */
    public static boolean floatShouldBeEqualOrGreaterThan0(float f, String desc) {
        if (f >= 0)
            return true;
        else
            throw new IllegalArgumentException(firstCharInCapital(desc) + " should be greater than 0!");
    }

    /**
     * Validate that if the balance is equal or less than 0
     * @param balance
     * @return true or false
     */
    public static boolean balanceShouldBeEqualOrLessThan0(float balance) {
        if (balance <= 0)
            return true;
        else
            throw new IllegalArgumentException("Balance should be less than 0!");
    }

    /**
     * Validate that if the integer is equal or greater than 0
     * @param integer
     * @param desc
     * @return true or false
     */
    public static boolean intMustBeEqualOrGreaterThan0(int integer, String desc) {
        if (integer >= 0)
            return true;
        else
            throw new IllegalArgumentException(firstCharInCapital(desc) + " should be equal or greater than 0!");
    }

    /**
     * Validate that if the formate of saving account number is correct
     * @param accountNo
     * @return true or false
     */
    public static boolean savingAccountNumberShouldBeInCorrectFormat(int accountNo) {
        String str = Integer.toString(accountNo);

        // saving account number should begin with 1 and has 8 numbers in total
        String regEx = "^[1]([0-9]{7})$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);

        if (matcher.matches())
            return true;
        else
            throw new IllegalArgumentException("Saving account number format incorrect!");
    }

    /**
     * Validate that if the formate of term deposit account number is correct
     * @param accountNo
     * @return true or false
     */
    public static boolean termDepositAccountNumberShouldBeInCorrectFormat(int accountNo) {
        String str = Integer.toString(accountNo);

        // term deposit account number should begin with 2 and has 8 numbers in total
        String regEx = "^[2]([0-9]{7})$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);

        if (matcher.matches())
            return true;
        else
            throw new IllegalArgumentException("Term deposit account number format incorrect!");
    }

    /**
     * Validate that if the format of credit account number is correct
     * @param accountNo
     * @return true or false
     */
    public static boolean creditAccountNumberShouldBeInCorrectFormat(int accountNo) {
        String str = Integer.toString(accountNo);

        // credit account number should begin with 3 and has 8 numbers in total
        String regEx = "^[3]([0-9]{7})$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);

        if (matcher.matches())
            return true;
        else
            throw new IllegalArgumentException("Credit account number format incorrect!");
    }

    /**
     * Validate that if the formate of home loan account number is correct
     * @param accountNo
     * @return true or false
     */
    public static boolean homeloanAccountNumberShouldBeInCorrectFormat(int accountNo) {
        String str = Integer.toString(accountNo);
        
        // home loan account number should begin with 4 and has 8 numbers in total
        String regEx = "^[4]([0-9]{7})$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);

        if (matcher.matches())
            return true;
        else
            throw new IllegalArgumentException("Home loan account number format incorrect!");
    }

    /**
     * Validate that if the formate of account number is correct
     * @param accountNo
     * @return true or false
     */
    public static boolean accountNumberShouldBeInCorrectFormat(int accountNo) {
        String str = Integer.toString(accountNo);
        
        // account number should begin with 1-4 and has 8 numbers in total
        String regEx = "^[1-4]([0-9]{7})$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);

        if (matcher.matches() || accountNo == 0)
            return true;
        else
            throw new IllegalArgumentException("Account number format incorrect!");
    }

    /**
     * Validate that if the formate of customer id is correct
     * @param customerID
     * @return true or false
     */
    public static boolean customerIDShouleBeInCorrectFormat(String customerID) {
        // customer id should begin with 5 and has 8 numbers in total
        String regEx = "^[5]([0-9]{7})$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(customerID);

        if (matcher.matches())
            return true;
        else
            throw new IllegalArgumentException("Customer ID format incorrect!");
    }

    /**
     * Validate that if the format of transaction number is correct
     * @param transactionNo
     * @return true or false
     */
    public static boolean transactionNumberShouleBeInCorrectFormat(int transactionNo) {
        String transactionNoStr = Integer.toString(transactionNo);
        
        // transaction number should begin with 6 and has 8 numbers in total
        String regEx = "^[6]([0-9]{7})$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(transactionNoStr);

        if (matcher.matches())
            return true;
        else
            throw new IllegalArgumentException("Transaction number format incorrect!");
    }

    /**
     * Validate that if date1 and date2 are at the same day
     * @param date1
     * @param date2
     * @return true or false
     */
    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }
    

    /**
     * Get the months months are there between two given dates
     * @param startDate
     * @param endDate
     * @return list of dates between start date and end date
     */
    public static List<Date> getMonthsBetweenTwoDate(Date startDate, Date endDate) {
        if (startDate.before(endDate)) {
            List<Date> months = new ArrayList<Date>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            while (endDate.after(calendar.getTime())) {
                calendar.add(Calendar.MONTH, 1);
                if (calendar.getTime().before(endDate))
                    months.add(calendar.getTime());
            }
            return months;
        }
        else
            throw new IllegalArgumentException("Start date must before end date!");
    }

    /**
     * Get end date given start date and term (by months)
     * @param startDate
     * @param term
     * @return end date
     */
    public static Date getEndDate(Date startDate, int term) {
        Calendar calculateEndDate = Calendar.getInstance();
        calculateEndDate.setTime(startDate);
        calculateEndDate.add(Calendar.MONTH, term);
        return calculateEndDate.getTime();
    }
    
    /**
     * Get end year given start date and term (by years)
     * @param startDate
     * @param term
     * @return end date
     */
    public static Date getEndYear(Date startDate, int term) {
        Calendar calculateEndDate = Calendar.getInstance();
        calculateEndDate.setTime(startDate);
        calculateEndDate.add(Calendar.YEAR, term);
        return calculateEndDate.getTime();
    }

    /**
     * Validate that if the string is null or empty
     * @param s
     * @param desc
     * @return true or false
     */
    public static boolean stringShouldNotBeNullOrEmpty(String s, String desc) {
        if (s == null || s.trim().equals(""))
            throw new IllegalArgumentException(firstCharInCapital(desc) + " cannot be empty!");
        else
            return true;
    }

    /**
     * Convert the first char in the String to upper case
     * @param s
     * @return converted string
     */
    public static String firstCharInCapital(String s) {
        char[] cs = s.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    /**
     * Validate that if the formate of phone number is correct
     * @param phoneNumber
     * @return true or false
     */
    public static boolean phoneNumberShouldBeInCorrectFormat(long phoneNumber) {
        // phone number should be exactly 10 numbers
        String phoneNumberStr = Long.toString(phoneNumber);
        String regEx = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(phoneNumberStr);
        if (matcher.matches())
            return true;
        else
            throw new IllegalArgumentException("Phone number should be exactly 10 numbers!");
    }

    /**
     * Validate that if the formate of pin is correct
     * @param pin
     * @return true or false
     */
    public static boolean pinShouldBeInCorrectFormat(String pin) {
        // pin should only contain 8 numbers
        String regEx = "^[0-9]{8}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(pin);
        if (matcher.matches())
            return true;
        else
            throw new IllegalArgumentException("PIN format incorrect, it should be exactly 8 numbers.");
    }

    /**
     * Validate that if the format of password is correct
     * @param password
     * @return true or false
     */
    public static boolean passwordShouleBeInCorrectFormat(String password) {
        // should only contain numbers and letters and should have 6-12 characters
        String regEx = "^[a-zA-Z0-9]{6,12}"; 
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches())
            return true;
        else
            throw new IllegalArgumentException("Password should contain only letters and numbers, and it should " +
                                                 "have 6 - 12 characters.");
    }

    /**
     * Validate that if the string can be converted to integer
     * @param s
     * @param desc
     * @return converted integer
     */
    public static int parseStringToInt(String s, String desc) {
        int i;
        try {
            i = Integer.parseInt(s);
            return i;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(firstCharInCapital(desc) + " format incorrect!");
        }
    }

    /**
     * Validate that if the string can be converted to long
     * @param s
     * @param desc
     * @return converted long
     */
    public static long parseStringToLong(String s, String desc) {
        Long i;
        try {
            i = Long.parseLong(s);
            return i;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(firstCharInCapital(desc) + " format incorrect!");
        }
    }

    /**
     * Validate that if the string can be converted to float
     * @param s
     * @param desc
     * @return vonverted float
     */
    public static float parseStringToFloat(String s, String desc) {
        float i;
        try {
            i = Float.parseFloat(s);
            return i;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(firstCharInCapital(desc) + " format incorrect!");
        }
    }

    /**
     * Validate that if the string can be converted to date
     * @param s
     * @param desc
     * @return converted date
     */
    public static Date parseStringToDate(String s, String desc) {
        Date i;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy/HH:mm:ss" );
            i = sdf.parse(s);
            return i;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(firstCharInCapital(desc) + " format incorrect!");
        }
    }
}
