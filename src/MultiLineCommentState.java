/**
 * Inside a multi-line comment. Everything is a multi-line comment character.
 * A '*' might be the start of the closing "*&#47;".
 */
public class MultiLineCommentState extends State {
    @Override
    public void star(CommentCounter context, char ch) {
        context.addChar(ch, CharType.MULTI_LINE);
        context.setState(context.getMultiLineStarState());
    }

    @Override
    public void other(CommentCounter context, char ch) {
        context.addChar(ch, CharType.MULTI_LINE);
    }
}
