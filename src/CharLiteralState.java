/**
 * Inside a char literal like '"' or '/'. Everything is code. A '\'' ends
 * the literal and a '\\' escapes the next character.
 */
public class CharLiteralState extends State {
    @Override
    public void singleQuote(CommentCounter context, char ch) {
        context.addChar(ch, CharType.CODE);
        context.setState(context.getCodeState());
    }

    @Override
    public void backslash(CommentCounter context, char ch) {
        context.addChar(ch, CharType.CODE);
        context.setState(context.getCharEscapeState());
    }

    @Override
    public void other(CommentCounter context, char ch) {
        context.addChar(ch, CharType.CODE);
    }
}