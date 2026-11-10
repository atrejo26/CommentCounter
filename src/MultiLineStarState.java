/**
 * A '*' was just read inside a multi-line comment. A '/' ends the comment,
 * another '*' keeps us here, anything else goes back into the comment.
 */
public class MultiLineStarState extends State {
    @Override
    public void slash(CommentCounter context, char ch) {
        //"*/" ends the comment
        context.addChar(ch, CharType.MULTI_LINE);
        context.setState(context.getCodeState());
    }

    @Override
    public void star(CommentCounter context, char ch) {
        //"**" the new star could still be the start of the end
        context.addChar(ch, CharType.MULTI_LINE);
    }

    @Override
    public void other(CommentCounter context, char ch) {
        context.addChar(ch, CharType.MULTI_LINE);
        context.setState(context.getMultiLineCommentState());
    }
}