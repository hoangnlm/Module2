/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.text.DefaultFormatter;

/**
 *
 * @author Hoang
 */
public class StringFormatter extends DefaultFormatter {

    private Pattern pattern;
    private Matcher matcher;
    
    // Cus name: bat dau bang 1 alphabet, ket thuc la 1 alphabet hoac 1 so
    // chieu dai toi thieu la 2, toi da 50.
    public static final String PATTERN_CUSNAME="^\\p{Alpha}[A-Za-z0-9 ]{0,48}\\p{Alnum}$";

    public StringFormatter() {
        super();
    }

    /**
     * Creates a regular expression based AbstractFormatter. pattern specifies
     * the regular expression that will be used to determine if a value is
     * legal.
     */
    public StringFormatter(String pattern) throws PatternSyntaxException {
        this();
        setPattern(Pattern.compile(pattern));
    }

    /**
     * Creates a regular expression based AbstractFormatter. pattern specifies
     * the regular expression that will be used to determine if a value is
     * legal.
     */
    public StringFormatter(Pattern pattern) {
        this();
        setPattern(pattern);
    }

    /**
     * Sets the pattern that will be used to determine if a value is legal.
     */
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Returns the Pattern used to determine if a value is legal.
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Sets the Matcher used in the most recent test if a value is legal.
     */
    protected void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    /**
     * Returns the Matcher from the most test.
     */
    protected Matcher getMatcher() {
        return matcher;
    }

    /**
     * Parses text returning an arbitrary Object. Some formatters may return
     * null.
     *
     * If a Pattern has been specified and the text completely matches the
     * regular expression this will invoke setMatcher.
     *
     * @throws ParseException if there is an error in the conversion
     * @param text String to convert
     * @return Object representation of text
     */
    @Override
    public Object stringToValue(String text) throws ParseException {
        Pattern pattern = getPattern();

        if (pattern != null) {
            Matcher matcher = pattern.matcher(text);

            if (matcher.matches()) {
                setMatcher(matcher);
                return super.stringToValue(text);
            }
            throw new ParseException("Invalid value entered!", 0);
        }
        return text;
    }
}

