/**
 * A '\' was just read inside a char literal. Whatever comes next is part of
 * the literal, so '\'' does not end early.
 */
public class CharEscapeState extends State {
    @Override
    public void other(CommentCounter context, char ch) {
        context.addChar(ch, CharType.CODE);
        context.setState(context.getCharLiteralState());
    }
}