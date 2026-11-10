/**
 * The events of interest for the comment counter state machine. Every
 * character read from the input file is turned into one of these events.
 */
public enum Event {
    SLASH,
    STAR,
    NEWLINE,
    DOUBLE_QUOTE,
    SINGLE_QUOTE,
    BACKSLASH,
    CHAR;

    /**
     * Converts a character into the event it generates
     * @param ch The character that was read in
     * @return The event for that character
     */
    public static Event fromChar(char ch) {
        Event retVal = CHAR;

        //pick the event based on the character
        if(ch == '/') {
            retVal = SLASH;
        }
        else if(ch == '*') {
            retVal = STAR;
        }
        else if(ch == '\n') {
            retVal = NEWLINE;
        }
        else if(ch == '"') {
            retVal = DOUBLE_QUOTE;
        }
        else if(ch == '\'') {
            retVal = SINGLE_QUOTE;
        }
        else if(ch == '\\') {
            retVal = BACKSLASH;
        }

        return retVal;
    }
}