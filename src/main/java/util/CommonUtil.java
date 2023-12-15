package util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    private static final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBER_LIST = "0123456789";

    public CommonUtil() {
    }

    public static String getRandomString() {
        StringBuilder rndString = new StringBuilder();

        while(rndString.length() < 5) {
            int index = (new Random()).nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZ".length());
            rndString.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(index));
        }

        return rndString.toString();
    }

    public static String getRandomString(int length) {
        StringBuilder rndString = new StringBuilder();

        while(rndString.length() < length) {
            int index = (new Random()).nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZ".length());
            rndString.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(index));
        }

        return rndString.toString().toLowerCase(Locale.ROOT);
    }

    public static String getRandomLimitedSpecialCharacter(int length) {
        String specialChars = "!@#$%^&";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < length; ++i) {
            sb.append(specialChars.charAt(random.nextInt(specialChars.length())));
        }

        return sb.toString();
    }

    public static String getRandomAllSpecialCharacter(int length) {
        String specialChars = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < length; ++i) {
            sb.append(specialChars.charAt(random.nextInt(specialChars.length())));
        }

        return sb.toString();
    }

    public static String getRandomSixNumbersString() {
        StringBuilder rndString = new StringBuilder();

        while(rndString.length() < 6) {
            int index = (new Random()).nextInt("0123456789".length());
            rndString.append("0123456789".charAt(index));
        }

        return rndString.toString();
    }

    public static String getRandomNumber(int length) {
        String candidateChars = "0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < length; ++i) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
        }

        return sb.toString();
    }

    public static String getRandomBirthDate() {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(1900, 2010);
        gc.set(1, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(6));
        gc.set(6, dayOfYear);
        return gc.get(1) + "-" + (gc.get(2) + 1) + "-" + gc.get(5);
    }

    public static String getRandom(String[] array) {
        Random generator = new Random();
        int randomIndex = generator.nextInt(array.length);
        return array[randomIndex];
    }

    public static String getRandomElement(String[] arr) {
        int idx = (new Random()).nextInt(arr.length);
        return arr[idx];
    }

    public static int getRandomHour() {
        Random rand = new Random();
        return rand.nextInt(11) + 1;
    }

    public static void sleep(long seconds) {
        sleepFor(seconds);
    }

    private static void sleepFor(long seconds) {
        long start = (new Date()).getTime();

        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException var7) {
            long end = (new Date()).getTime();

            do {
                end = (new Date()).getTime();
            } while(start + seconds * 1000L > end);
        }

    }

    private static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (double)(end - start));
    }

    public static double round(double num, int decimalToRound) {
        return (new BigDecimal(num)).setScale(decimalToRound, RoundingMode.HALF_UP).doubleValue();
    }

    public static int getRandomIntegerBetweenRange(double min, double max) {
        int x = (int) (((int)(Math.random() * (max - min + 1.0D))) + min);
        return x;
    }

    public static String convertUTCToLocal(String utcTime, String pattern) {
        TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(utcTime);
        Instant input = Instant.from(ta);
        LocalDateTime dateTime = LocalDateTime.ofInstant(input, ZoneId.systemDefault());
        String localTime = dateTime.format(DateTimeFormatter.ofPattern(pattern));
        return localTime;
    }

    public static String convertUTCToMYT(String utcTime, String pattern) {
        TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(utcTime);
        Instant input = Instant.from(ta);
        LocalDateTime dateTime = LocalDateTime.ofInstant(input, ZoneId.of("Asia/Kuala_Lumpur"));
        String localTime = dateTime.format(DateTimeFormatter.ofPattern(pattern));
        return localTime;
    }

    public static boolean compareDates(String date, String startDate, String endDate, String format) {
        SimpleDateFormat dateFmt = new SimpleDateFormat(format);

        try {
            Date fromDate = dateFmt.parse(startDate);
            Date toDate = dateFmt.parse(endDate);
            Date actualDate = dateFmt.parse(date);
            if (actualDate.compareTo(fromDate) >= 0 && actualDate.compareTo(toDate) <= 0) {
                return true;
            }
        } catch (ParseException var8) {
            var8.printStackTrace();
        }

        return false;
    }

    public static String getUTCTimePlusDays(int day) {
        DateTime now = (new DateTime()).withTimeAtStartOfDay();
        DateTime date = now.toDateTime(DateTimeZone.UTC);
        return String.valueOf(date.plusDays(day));
    }

    public static String getUTCTimeMinusDays(int day) {
        DateTime now = (new DateTime()).withTimeAtStartOfDay();
        DateTime date = now.toDateTime(DateTimeZone.UTC);
        return String.valueOf(date.minusDays(day));
    }

    public static String getCurrentUTCDate(String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withZone(ZoneId.of("UTC"));
        String currentUTCDate = formatter.format(Instant.now());
        return currentUTCDate;
    }

    public static String getCurrentCustomUTCDate(String utcZone, String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withZone(ZoneId.of(utcZone));
        String currentUTCDate = formatter.format(Instant.now());
        return currentUTCDate;
    }

    public static String getCurrentUTCSubDays(int days, String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withZone(ZoneId.of("UTC"));
        Instant instant = Instant.now().minus((long)days, ChronoUnit.DAYS);
        String currentUTCSubDays = formatter.format(instant);
        return currentUTCSubDays;
    }

    public static String getDayAfterSomeDays(int day) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd");
        LocalDateTime nowAddSomeDays = LocalDate.now().plusDays((long)day).atTime(11, 0);
        return dtf.format(nowAddSomeDays);
    }

    public static String getDayMonthYearAfterSomeDays(int day) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
        LocalDateTime nowAddSomeDays = LocalDate.now().plusDays((long)day).atTime(11, 0);
        return dtf.format(nowAddSomeDays);
    }

    public static double getRandomDoubleBetweenRange(double min, double max) {
        double x = Math.floor((Math.random() * (max - min) + min) * 100.0D) / 100.0D;
        return x;
    }

    public static int randomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

    public static int randomInt(int max) {
        return randomInt(0, max);
    }

    public static String getNowSubDaysAt00hh00(int days, String dateFormat) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime nowSubDays = LocalDate.now().minusDays((long)days).atTime(0, 0);
        return dtf.format(nowSubDays);
    }

    public static String changeDateTimeFormat(String stringToFormat, String originalStringFormat, String desiredStringFormat) {
        SimpleDateFormat readingFormat = new SimpleDateFormat(originalStringFormat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(desiredStringFormat);
        String output = "";

        try {
            Date date = readingFormat.parse(stringToFormat);
            output = outputFormat.format(date);
        } catch (ParseException var7) {
            var7.printStackTrace();
        }

        return output;
    }

    public static String getUTCEndDay(int day) {
        DateTime now = (new DateTime()).withTimeAtStartOfDay().minus(1L);
        DateTime date = now.toDateTime(DateTimeZone.UTC);
        return String.valueOf(date.minusDays(day));
    }

    public static String getStartDateOfMonth(int plusMonths, int plusYears) {
        DateTime dateTime = (new DateTime()).plusYears(plusYears).plusMonths(plusMonths).withDayOfMonth(1).withTimeAtStartOfDay().toDateTime(DateTimeZone.UTC);
        return String.valueOf(dateTime);
    }

    public static String getLastDateOfCurrentMonth() {
        LocalDate localDate = LocalDate.now();
        int day = localDate.withDayOfMonth(localDate.getMonth().length(localDate.isLeapYear())).getDayOfMonth();
        DateTime dateTime = (new DateTime()).withDayOfMonth(day).withTimeAtStartOfDay();
        return String.valueOf(dateTime);
    }

    public static String getRandomHexColor() {
        Random random = new Random();
        String[] letters = "0123456789ABCDEF".split("");
        String color = "#";

        for(int i = 0; i < 6; ++i) {
            color = color + letters[Math.round(random.nextFloat() * 15.0F)];
        }

        return color;
    }

    public static List<String> pickRandom(List<String> stringList, int n) {
        Collections.shuffle(stringList);
        List<String> copyList = new ArrayList(stringList);
        return n > copyList.size() ? copyList.subList(0, copyList.size()) : copyList.subList(0, n);
    }

    public static String doubleToString(double input, int decimalNumber) {
        StringBuffer decimal = new StringBuffer();

        for(int i = 0; i < decimalNumber; ++i) {
            decimal.append("0");
        }

        DecimalFormat df;
        if (decimal.length() > 0) {
            df = new DecimalFormat(String.format("#.%s", decimal));
        } else {
            df = new DecimalFormat("#");
        }

        return df.format(input);
    }

    public static String removeSpecialCharacters(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", "");
    }

    public static DateTime getUTCStartDateNow() {
        DateTime now = (new DateTime()).withTimeAtStartOfDay();
        DateTime startDateNow = now.toDateTime(DateTimeZone.UTC);
        return startDateNow;
    }

    public static String getLocalTimeNow(String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.now().format(dtf);
    }

    public static boolean compareDatesWithStartDate(String date, String startDate, String format) {
        SimpleDateFormat dateFmt = new SimpleDateFormat(format);

        try {
            Date fromDate = dateFmt.parse(startDate);
            Date actualDate = dateFmt.parse(date);
            if (actualDate.compareTo(fromDate) >= 0) {
                return true;
            }
        } catch (ParseException var6) {
            var6.printStackTrace();
        }

        return false;
    }

    public static boolean compareDatesWithEndDate(String date, String endDate, String format) {
        SimpleDateFormat dateFmt = new SimpleDateFormat(format);

        try {
            Date toDate = dateFmt.parse(endDate);
            Date actualDate = dateFmt.parse(date);
            if (actualDate.compareTo(toDate) <= 0) {
                return true;
            }
        } catch (ParseException var6) {
            var6.printStackTrace();
        }

        return false;
    }

    public static String getHashSHA256(String signature) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(signature.getBytes());
        byte[] byteData = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer();

        for(int i = 0; i < byteData.length; ++i) {
            stringBuffer.append(Integer.toString((byteData[i] & 255) + 256, 16).substring(1));
        }

        return stringBuffer.toString();
    }
    public static long convertDateToMilliseconds(String date, String format) {
        String dateLocal = convertUTCToLocal(date, format);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long millis = 0;
        try{
            millis = simpleDateFormat.parse(dateLocal).getTime();
            return millis;
        }catch (ParseException exception){
            System.out.println(exception.getMessage());
        }
        return millis;
    }
    public static boolean checkFormatApiKey(String apiKey){
        String regex = "[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(apiKey);
        return matcher.matches();
    }
    public static boolean verifyFormatOfDate(String date, String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try{
            Date dateTime = simpleDateFormat.parse(date);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
