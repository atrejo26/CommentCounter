import javax.swing.SwingUtilities;

/**
* This class is a driver for a hello world program. It prints some test
* output to the terminal, tests the comment counter, and then opens the
* JavaDoc Comment Counter window.
*/
public class Driver {
    //the number of times to repeat the greeting
    private static final int REPEAT = 3; //a single line comment after code

    /*****************************************
     * a banner style comment, it starts
     * with slash star star so it counts as
     * Javadoc even with all the extra stars
     *****************************************/

    public static void main(String[] args) {
        /* a plain multi-line comment, NOT Javadoc */

        /*
        //some simple arithmetic
        int x = 10 / 2;
        int y = x * 7;
        */

        //some prints
        System.out.println("Hello World!!!");
        System.out.println("/**this is not a comment*/");
        System.out.println("/*this is not a comment*/");
        System.out.println("//this is not a comment");
        System.out.println("Mark said, \"Oh no, this isn't a comment either\"");

        //division and multiplication right next to comment characters
        int a = 10 / 3;
        int b = 10/3;
        int c = 5 *3;
        int d = 5*/*inline comment*/3;
        int e = a /b;
        e /= 2;
        System.out.println(a + " " + b + " " + c + " " + d + " " + e);

        //a slash right before a string and a char
        String half = "" + 10/2 + "/";
        System.out.println(half);

        //char literals that look like the start of strings or comments
        char quote = '"';
        char apostrophe = '\'';
        char slash = '/';
        char star = '*';
        char backslash = '\\';
        char carriageReturn = '\r';
        System.out.println("" + quote + apostrophe + slash + star + backslash + (int)carriageReturn);

        //strings that end with an escaped backslash
        String path = "C:\\temp\\";
        String url = "https://www.carthage.edu"; //the // in the url is not a comment
        System.out.println(path + " " + url);

        /**/ //an empty multi-line comment (not Javadoc)
        /***/ //the shortest Javadoc comment

        /* a multi-line comment with // and /* inside of it ** and stars */
        // a single line comment with /* and */ inside of it

        System.out.println(greet("Mark", REPEAT));
        System.out.println(average(new int[] {4, 8, 15, 16, 23, 42}));

        //terminal tests for the comment counter
        System.out.println();
        System.out.println("Comment counter tests:");
        testCounter("division", "int d = 10 / 3;", 15, 0, 0, 0);
        testCounter("comment in a string", "String s = \"/* not a comment */\";", 33, 0, 0, 0);
        testCounter("quote char", "char c = '\"';", 13, 0, 0, 0);
        testCounter("single line", "int x = 5; // note", 11, 7, 0, 0);
        testCounter("star inside comment", "/* this is 5 *3 */", 0, 0, 18, 0);
        testCounter("empty comment", "/**/", 0, 0, 4, 0);
        testCounter("javadoc", "/** doc */", 0, 0, 0, 10);

        //open the comment counter window (keep this last)
        SwingUtilities.invokeLater(() -> new CommentCounterGUI().setVisible(true));
    }

    /**
     * Builds a greeting that repeats a name
     * @param name The name to greet
     * @param times How many times to repeat it
     * @return The greeting
     */
    public static String greet(String name, int times) {
        StringBuilder retVal = new StringBuilder();

        //repeat the greeting
        for(int i = 0;i < times;i++) {
            retVal.append("Hi, " + name + "! "); /* trailing comment */
        }

        return retVal.toString();
    }

    /**
     * Finds the average of some numbers. A star at the end of this line *
     * and a ** double star should not end the Javadoc early.
     * @param nums The numbers to average
     * @return The average
     */
    public static double average(int[] nums) {
        int sum = 0;
        for(int i = 0;i < nums.length;i++) {
            sum += nums[i];
        }

        return (double)sum / nums.length; /** a Javadoc style comment at the end of a line **/
    }

    /**
     * Runs the comment counter on some code and prints whether the counts are what was expected
     * @param label A short name for the test
     * @param code The code to analyze
     * @param expCode Expected code characters
     * @param expSingle Expected single line characters
     * @param expMulti Expected multi-line characters
     * @param expJavadoc Expected Javadoc characters
     */
    public static void testCounter(String label, String code, int expCode, int expSingle, int expMulti, int expJavadoc) {
        CommentCounter counter = new CommentCounter();
        counter.analyze(code);

        //compare what the counter found to what was expected
        boolean passed = counter.getNumCodeChars() == expCode
            && counter.getNumSingleLineChars() == expSingle
            && counter.getNumMultiLineChars() == expMulti
            && counter.getNumJavadocChars() == expJavadoc;

        String result = "FAIL";
        if(passed) {
            result = "PASS";
        }

        System.out.println(result + " " + label + " -> code: " + counter.getNumCodeChars()
            + ", single: " + counter.getNumSingleLineChars()
            + ", multi: " + counter.getNumMultiLineChars()
            + ", javadoc: " + counter.getNumJavadocChars());
    }
}