/**
 * The base class for every state in the comment counter state machine.
 * There is one method for each event of interest. By default every event
 * is handled by other(), so each concrete state only has to override the
 * events that do something special in that state.
 */
public abstract class State {
    /**
     * Handles a '/' character
     * @param context The comment counter that owns the state machine
     * @param ch The character that was read in
     */
    public void slash(CommentCounter context, char ch) {
        other(context, ch);
    }

    /**
     * Handles a '*' character
     * @param context The comment counter that owns the state machine
     * @param ch The character that was read in
     */
    public void star(CommentCounter context, char ch) {
        other(context, ch);
    }

    /**
     * Handles a newline character
     * @param context The comment counter that owns the state machine
     * @param ch The character that was read in
     */
    public void newline(CommentCounter context, char ch) {
        other(context, ch);
    }

    /**
     * Handles a '"' character
     * @param context The comment counter that owns the state machine
     * @param ch The character that was read in
     */
    public void doubleQuote(CommentCounter context, char ch) {
        other(context, ch);
    }

    /**
     * Handles a '\'' character
     * @param context The comment counter that owns the state machine
     * @param ch The character that was read in
     */
    public void singleQuote(CommentCounter context, char ch) {
        other(context, ch);
    }

    /**
     * Handles a '\\' character
     * @param context The comment counter that owns the state machine
     * @param ch The character that was read in
     */
    public void backslash(CommentCounter context, char ch) {
        other(context, ch);
    }

    /**
     * Handles any character that has no special meaning in this state
     * @param context The comment counter that owns the state machine
     * @param ch The character that was read in
     */
    public abstract void other(CommentCounter context, char ch);

    /**
     * Called when the whole file has been read. States that are holding
     * on to characters they have not counted yet override this.
     * @param context The comment counter that owns the state machine
     */
    public void endOfInput(CommentCounter context) {
        //most states are not holding on to anything
    }
}