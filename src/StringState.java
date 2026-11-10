/**
 * Inside a string literal. Everything is code, so "//" and "/*" in a string
 * are not comments. A '"' ends the string and a '\' escapes the next
 * character so that \" does not end it.
 */
public class StringState extends State {
    @Override
    public void doubleQuote(CommentCounter context, char ch) {
        context.addChar(ch, CharType.CODE);
        context.setState(context.getCodeState());
    }

    @Override
    public void backslash(CommentCounter context, char ch) {
        context.addChar(ch, CharType.CODE);
        context.setState(context.getStringEscapeState());
    }

    @Override
    public void other(CommentCounter context, char ch) {
        context.addChar(ch, CharType.CODE);
    }
}