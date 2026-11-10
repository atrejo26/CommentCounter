/**
 * A '\' was just read inside a string. Whatever comes next (even a '"' or
 * another '\') is part of the string.
 */
public class StringEscapeState extends State {
    @Override
    public void other(CommentCounter context, char ch) {
        context.addChar(ch, CharType.CODE);
        context.setState(context.getStringState());
    }
}