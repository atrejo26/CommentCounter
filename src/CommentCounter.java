/**
 * This is the context class for the State pattern. It reads a Java file one
 * character at a time, turns each character into an event, and passes the
 * event to the current state. The states call back into this class to count
 * characters and to build the text shown in each tab of the GUI.
 */
public class CommentCounter {
    //the character used to hide parts of the file in a tab
    public static final char REDACT_CHAR = '.';

    //all of the states in the state machine
    private final State codeState = new CodeState();
    private final State slashState = new SlashState();
    private final State slashStarState = new SlashStarState();
    private final State slashStarStarState = new SlashStarStarState();
    private final State singleLineCommentState = new SingleLineCommentState();
    private final State multiLineCommentState = new MultiLineCommentState();
    private final State multiLineStarState = new MultiLineStarState();
    private final State javadocState = new JavadocState();
    private final State javadocStarState = new JavadocStarState();
    private final State stringState = new StringState();
    private final State stringEscapeState = new StringEscapeState();
    private final State charLiteralState = new CharLiteralState();
    private final State charEscapeState = new CharEscapeState();

    //the state the machine is currently in
    private State currentState;

    //character counts
    private int numCodeChars;
    private int numSingleLineChars;
    private int numMultiLineChars;
    private int numJavadocChars;

    //the text for each tab
    private StringBuilder codeText;
    private StringBuilder commentsText;
    private StringBuilder javadocText;
    private String originalText;

     //Creates a comment counter with everything reset
    public CommentCounter() {
        reset();
    }

    //Puts the machine back in the start state and clears all counts and text
    public void reset() {
        currentState = codeState;

        numCodeChars = 0;
        numSingleLineChars = 0;
        numMultiLineChars = 0;
        numJavadocChars = 0;

        codeText = new StringBuilder();
        commentsText = new StringBuilder();
        javadocText = new StringBuilder();
        originalText = "";
    }

    /**
     * Parses all of the text from a Java file
     * @param text The contents of a Java file
     */
    public void analyze(String text) {
        reset();

        //builds the Original tab text without the \r characters
        StringBuilder original = new StringBuilder();

        //generate an event for every character
        for(int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            //Windows ends lines with \r\n, skip the \r and keep the \n
            if(ch != '\r') {
                original.append(ch);
                handleChar(ch);
            }
        }
        originalText = original.toString();

        //let the last state count anything it is still holding on to
        currentState.endOfInput(this);
    }

    /**
     * Turns one character into an event and sends it to the current state
     * @param ch The character that was read in
     */
    public void handleChar(char ch) {
        Event event = Event.fromChar(ch);

        switch(event) {
            case SLASH:
                currentState.slash(this, ch);
                break;
            case STAR:
                currentState.star(this, ch);
                break;
            case NEWLINE:
                currentState.newline(this, ch);
                break;
            case DOUBLE_QUOTE:
                currentState.doubleQuote(this, ch);
                break;
            case SINGLE_QUOTE:
                currentState.singleQuote(this, ch);
                break;
            case BACKSLASH:
                currentState.backslash(this, ch);
                break;
            default:
                currentState.other(this, ch);
                break;
        }
    }

    /**
     * Counts some characters as one type and adds them to the tab text. The
     * characters show up in the tab for their type and are replaced with the
     * redact character in the other tabs. Newlines are kept in every tab so
     * the lines still line up.
     * @param chars The characters to count
     * @param type What kind of characters they are
     */
    public void addChars(String chars, CharType type) {
        for(int i = 0; i < chars.length(); i++) {
            char ch = chars.charAt(i);

            //count the character
            if(type == CharType.CODE) {
                numCodeChars++;
            }
            else if(type == CharType.SINGLE_LINE) {
                numSingleLineChars++;
            }
            else if(type == CharType.MULTI_LINE) {
                numMultiLineChars++;
            }
            else {
                numJavadocChars++;
            }

            //add it to the tab that shows it, redact it everywhere else
            boolean isComment = type == CharType.SINGLE_LINE || type == CharType.MULTI_LINE;
            codeText.append(showOrRedact(ch, type == CharType.CODE));
            commentsText.append(showOrRedact(ch, isComment));
            javadocText.append(showOrRedact(ch, type == CharType.JAVADOC));
        }
    }

    /**
     * Counts a single character as one type
     * @param ch The character to count
     * @param type What kind of character it is
     */
    public void addChar(char ch, CharType type) {
        addChars(String.valueOf(ch), type);
    }

    /**
     * Decides what to put in a tab for a character
     * @param ch The character
     * @param show Whether this tab should show the character
     * @return The character itself or the redact character
     */
    private char showOrRedact(char ch, boolean show) {
        char retVal = REDACT_CHAR;

        //newlines are never redacted so the lines stay the same
        if(show || ch == '\n') {
            retVal = ch;
        }

        return retVal;
    }

    /**
     * Changes the current state
     * @param newState The state to move to
     */
    public void setState(State newState) {
        currentState = newState;
    }

    //getters for the states so the states can move between each other
    public State getCodeState() {
        return codeState;
    }

    public State getSlashState() {
        return slashState;
    }

    public State getSlashStarState() {
        return slashStarState;
    }

    public State getSlashStarStarState() {
        return slashStarStarState;
    }

    public State getSingleLineCommentState() {
        return singleLineCommentState;
    }

    public State getMultiLineCommentState() {
        return multiLineCommentState;
    }

    public State getMultiLineStarState() {
        return multiLineStarState;
    }

    public State getJavadocState() {
        return javadocState;
    }

    public State getJavadocStarState() {
        return javadocStarState;
    }

    public State getStringState() {
        return stringState;
    }

    public State getStringEscapeState() {
        return stringEscapeState;
    }

    public State getCharLiteralState() {
        return charLiteralState;
    }

    public State getCharEscapeState() {
        return charEscapeState;
    }

    //getters for the results
    public int getNumCodeChars() {
        return numCodeChars;
    }

    public int getNumSingleLineChars() {
        return numSingleLineChars;
    }

    public int getNumMultiLineChars() {
        return numMultiLineChars;
    }

    public int getNumJavadocChars() {
        return numJavadocChars;
    }

    public String getCodeText() {
        return codeText.toString();
    }

    public String getCommentsText() {
        return commentsText.toString();
    }

    public String getJavadocText() {
        return javadocText.toString();
    }

    public String getOriginalText() {
        return originalText;
    }
}