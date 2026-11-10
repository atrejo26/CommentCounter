/**
 * A '*' was just read inside a Javadoc comment. A '/' ends the comment,
 * another '*' keeps us here, anything else goes back into the comment.
 */
public class JavadocStarState extends State {
    @Override
    public void slash(CommentCounter context, char ch) {
        //"*/" ends the Javadoc
        context.addChar(ch, CharType.JAVADOC);
        context.setState(context.getCodeState());
    }

    @Override
    public void star(CommentCounter context, char ch) {
        context.addChar(ch, CharType.JAVADOC);
    }

    @Override
    public void other(CommentCounter context, char ch) {
        context.addChar(ch, CharType.JAVADOC);
        context.setState(context.getJavadocState());
    }
}