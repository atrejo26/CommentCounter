/**
 * Inside a Javadoc comment. Everything is a Javadoc character. A '*' might
 * be the start of the closing "*&#47;".
 */
public class JavadocState extends State {
    @Override
    public void star(CommentCounter context, char ch) {
        context.addChar(ch, CharType.JAVADOC);
        context.setState(context.getJavadocStarState());
    }

    @Override
    public void other(CommentCounter context, char ch) {
        context.addChar(ch, CharType.JAVADOC);
    }
}