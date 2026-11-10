/**
 * "/**" has been read. This is a Javadoc comment unless the next character
 * is a '/', which makes it the empty multi-line comment "/**&#47;".
 */
public class SlashStarStarState extends State {
    @Override
    public void slash(CommentCounter context, char ch) {
        //"/**/" is an empty multi-line comment, back to code
        context.addChars("/**/", CharType.MULTI_LINE);
        context.setState(context.getCodeState());
    }

    @Override
    public void star(CommentCounter context, char ch) {
        //"/***" is Javadoc and the last star might be the start of the end
        context.addChars("/***", CharType.JAVADOC);
        context.setState(context.getJavadocStarState());
    }

    @Override
    public void other(CommentCounter context, char ch) {
        context.addChars("/**" + ch, CharType.JAVADOC);
        context.setState(context.getJavadocState());
    }
}