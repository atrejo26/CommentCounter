/**
 * The start state. Every character here is code, except a '/' which might
 * start a comment, and quotes which start a string or char literal.
 */
public class CodeState extends State {
    @Override
    public void slash(CommentCounter context, char ch) {
        //might be division or the start of a comment, wait and see
        context.setState(context.getSlashState());
    }

    @Override
    public void doubleQuote(CommentCounter context, char ch) {
        context.addChar(ch, CharType.CODE);
        context.setState(context.getStringState());
    }

    @Override
    public void singleQuote(CommentCounter context, char ch) {
        context.addChar(ch, CharType.CODE);
        context.setState(context.getCharLiteralState());
    }

    @Override
    public void other(CommentCounter context, char ch) {
        context.addChar(ch, CharType.CODE);
    }
}