/**
 * Inside a "//" comment. Everything is a single line comment character until
 * the end of the line. The newline itself ends the comment and is counted as
 * code.
 */
public class SingleLineCommentState extends State {
    @Override
    public void newline(CommentCounter context, char ch) {
        context.addChar(ch, CharType.CODE);
        context.setState(context.getCodeState());
    }

    @Override
    public void other(CommentCounter context, char ch) {
        context.addChar(ch, CharType.SINGLE_LINE);
    }
}