/**
 * A '/' has been read in code. The next character decides whether it was a
 * division sign, the start of a single line comment, or the start of a
 * multi-line or Javadoc comment. The '/' is not counted until then.
 */
public class SlashState extends State {
    @Override
    public void slash(CommentCounter context, char ch) {
        //"//" starts a single line comment
        context.addChars("//", CharType.SINGLE_LINE);
        context.setState(context.getSingleLineCommentState());
    }

    @Override
    public void star(CommentCounter context, char ch) {
        //"/*" is a multi-line or Javadoc comment, need one more character to know
        context.setState(context.getSlashStarState());
    }

    @Override
    public void doubleQuote(CommentCounter context, char ch) {
        //division followed by a string
        context.addChars("/" + ch, CharType.CODE);
        context.setState(context.getStringState());
    }

    @Override
    public void singleQuote(CommentCounter context, char ch) {
        //division followed by a char literal
        context.addChars("/" + ch, CharType.CODE);
        context.setState(context.getCharLiteralState());
    }

    @Override
    public void other(CommentCounter context, char ch) {
        //it was a division sign
        context.addChars("/" + ch, CharType.CODE);
        context.setState(context.getCodeState());
    }

    @Override
    public void endOfInput(CommentCounter context) {
        //the file ended right after a '/', count it as code
        context.addChar('/', CharType.CODE);
    }
}