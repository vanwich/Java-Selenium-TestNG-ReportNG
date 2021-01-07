package help.data;

public interface MarkupParser {
    /**
     * Parses string,  processes in-line mark-up and returns processed string back
     * @param s string to be processed
     * @return processed string
     */
    public String parse(String s);
}
