/**
 * "/*" has been read. If the next character is a '*' this might be a Javadoc
 * comment, otherwise it is a multi-line comment.
 */
public class SlashStarState extends State {
    @Override
    public void star(CommentCounter context, char ch) {
        //"/**" is usually Javadoc, but "/**/" is an empty multi-line comment
        context.setState(context.getSlashStarStarState());
    }

    @Override
    public void other(CommentCounter context, char ch) {
        //anything else (even a '/') is the first character of a multi-line comment
        context.addChars("/*" + ch, CharType.MULTI_LINE);
        context.setState(context.getMultiLineCommentState());
    }
}