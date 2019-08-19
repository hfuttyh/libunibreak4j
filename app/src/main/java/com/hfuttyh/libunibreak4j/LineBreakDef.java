package com.hfuttyh.libunibreak4j;

public class LineBreakDef {
    /**
     * Line break classes.  This is a mapping of Table 1 of Unicode
     * Standard Annex 14.
     */
    public enum LineBreakClass
    {
        /* This is used to signal an error condition. */
        LBP_Undefined,  /**< Undefined */

        /* The following break classes are treated in the pair table. */
        LBP_OP,         /**< Opening punctuation */
        LBP_CL,         /**< Closing punctuation */
        LBP_CP,         /**< Closing parenthesis */
        LBP_QU,         /**< Ambiguous quotation */
        LBP_GL,         /**< Glue */
        LBP_NS,         /**< Non-starters */
        LBP_EX,         /**< Exclamation/Interrogation */
        LBP_SY,         /**< Symbols allowing break after */
        LBP_IS,         /**< Infix separator */
        LBP_PR,         /**< Prefix */
        LBP_PO,         /**< Postfix */
        LBP_NU,         /**< Numeric */
        LBP_AL,         /**< Alphabetic */
        LBP_HL,         /**< Hebrew letter */
        LBP_ID,         /**< Ideographic */
        LBP_IN,         /**< Inseparable characters */
        LBP_HY,         /**< Hyphen */
        LBP_BA,         /**< Break after */
        LBP_BB,         /**< Break before */
        LBP_B2,         /**< Break on either side (but not pair) */
        LBP_ZW,         /**< Zero-width space */
        LBP_CM,         /**< Combining marks */
        LBP_WJ,         /**< Word joiner */
        LBP_H2,         /**< Hangul LV */
        LBP_H3,         /**< Hangul LVT */
        LBP_JL,         /**< Hangul L Jamo */
        LBP_JV,         /**< Hangul V Jamo */
        LBP_JT,         /**< Hangul T Jamo */
        LBP_RI,         /**< Regional indicator */
        LBP_EB,         /**< Emoji base */
        LBP_EM,         /**< Emoji modifier */
        LBP_ZWJ,        /**< Zero width joiner */

        /* The following break class is treated in the pair table, but it is
         * not part of Table 2 of UAX #14-37. */
        LBP_CB,         /**< Contingent break */

        /* The following break classes are not treated in the pair table */
        LBP_AI,         /**< Ambiguous (alphabetic or ideograph) */
        LBP_BK,         /**< Break (mandatory) */
        LBP_CJ,         /**< Conditional Japanese starter */
        LBP_CR,         /**< Carriage return */
        LBP_LF,         /**< Line feed */
        LBP_NL,         /**< Next line */
        LBP_SA,         /**< South-East Asian */
        LBP_SG,         /**< Surrogates */
        LBP_SP,         /**< Space */
        LBP_XX          /**< Unknown */
    }

    /**
     * English-specifc data over the default Unicode rules.
     */
    public static LineBreakProperties lb_prop_English[] = new LineBreakProperties[]{
            new LineBreakProperties( 0x2018, 0x2018, LineBreakClass.LBP_OP ),	/* Left single quotation mark: opening */
            new LineBreakProperties( 0x201C, 0x201C, LineBreakClass.LBP_OP ),	/* Left double quotation mark: opening */
            new LineBreakProperties( 0x201D, 0x201D, LineBreakClass.LBP_CL ),	/* Right double quotation mark: closing */
            new LineBreakProperties( 0, 0, LineBreakClass.LBP_Undefined )
    };

    /**
     * German-specifc data over the default Unicode rules.
     */
    public static LineBreakProperties lb_prop_German[] = new LineBreakProperties[]{
            new LineBreakProperties( 0x00AB, 0x00AB, LineBreakClass.LBP_CL ),	/* Left double angle quotation mark: closing */
            new LineBreakProperties( 0x00BB, 0x00BB, LineBreakClass.LBP_OP ),	/* Right double angle quotation mark: opening */
            new LineBreakProperties( 0x2018, 0x2018, LineBreakClass.LBP_CL ),	/* Left single quotation mark: closing */
            new LineBreakProperties( 0x201C, 0x201C, LineBreakClass.LBP_CL ),	/* Left double quotation mark: closing */
            new LineBreakProperties( 0x2039, 0x2039, LineBreakClass.LBP_CL ),	/* Left single angle quotation mark: closing */
            new LineBreakProperties( 0x203A, 0x203A, LineBreakClass.LBP_OP ),	/* Right single angle quotation mark: opening */
            new LineBreakProperties( 0, 0, LineBreakClass.LBP_Undefined )
    };

    /**
     * Spanish-specifc data over the default Unicode rules.
     */
    public static LineBreakProperties lb_prop_Spanish[] = new LineBreakProperties[]{
            new LineBreakProperties( 0x00AB, 0x00AB, LineBreakClass.LBP_OP ),	/* Left double angle quotation mark: opening */
            new LineBreakProperties( 0x00BB, 0x00BB, LineBreakClass.LBP_CL ),	/* Right double angle quotation mark: closing */
            new LineBreakProperties( 0x2018, 0x2018, LineBreakClass.LBP_OP ),	/* Left single quotation mark: opening */
            new LineBreakProperties( 0x201C, 0x201C, LineBreakClass.LBP_OP ),	/* Left double quotation mark: opening */
            new LineBreakProperties( 0x201D, 0x201D, LineBreakClass.LBP_CL ),	/* Right double quotation mark: closing */
            new LineBreakProperties( 0x2039, 0x2039, LineBreakClass.LBP_OP ),	/* Left single angle quotation mark: opening */
            new LineBreakProperties( 0x203A, 0x203A, LineBreakClass.LBP_CL ),	/* Right single angle quotation mark: closing */
            new LineBreakProperties( 0, 0, LineBreakClass.LBP_Undefined )
    };

    /**
     * French-specifc data over the default Unicode rules.
     */
    public static LineBreakProperties lb_prop_French[] = new LineBreakProperties[]{
            new LineBreakProperties( 0x00AB, 0x00AB, LineBreakClass.LBP_OP ),	/* Left double angle quotation mark: opening */
            new LineBreakProperties( 0x00BB, 0x00BB, LineBreakClass.LBP_CL ),	/* Right double angle quotation mark: closing */
            new LineBreakProperties( 0x2018, 0x2018, LineBreakClass.LBP_OP ),	/* Left single quotation mark: opening */
            new LineBreakProperties( 0x201C, 0x201C, LineBreakClass.LBP_OP ),	/* Left double quotation mark: opening */
            new LineBreakProperties( 0x201D, 0x201D, LineBreakClass.LBP_CL ),	/* Right double quotation mark: closing */
            new LineBreakProperties( 0x2039, 0x2039, LineBreakClass.LBP_OP ),	/* Left single angle quotation mark: opening */
            new LineBreakProperties( 0x203A, 0x203A, LineBreakClass.LBP_CL ),	/* Right single angle quotation mark: closing */
            new LineBreakProperties( 0, 0, LineBreakClass.LBP_Undefined )
    };

    /**
     * Russian-specifc data over the default Unicode rules.
     */
    public static LineBreakProperties lb_prop_Russian[] = new LineBreakProperties[]{
            new LineBreakProperties( 0x00AB, 0x00AB, LineBreakClass.LBP_OP ),	/* Left double angle quotation mark: opening */
            new LineBreakProperties( 0x00BB, 0x00BB, LineBreakClass.LBP_CL ),	/* Right double angle quotation mark: closing */
            new LineBreakProperties( 0x201C, 0x201C, LineBreakClass.LBP_CL ),	/* Left double quotation mark: closing */
            new LineBreakProperties( 0, 0, LineBreakClass.LBP_Undefined )
    };

    /**
     * Chinese-specifc data over the default Unicode rules.
     */
    public static LineBreakProperties lb_prop_Chinese[] = new LineBreakProperties[]{
            new LineBreakProperties( 0x2018, 0x2018, LineBreakClass.LBP_OP ),	/* Left single quotation mark: opening */
            new LineBreakProperties( 0x2019, 0x2019, LineBreakClass.LBP_CL ),	/* Right single quotation mark: closing */
            new LineBreakProperties( 0x201C, 0x201C, LineBreakClass.LBP_OP ),	/* Left double quotation mark: opening */
            new LineBreakProperties( 0x201D, 0x201D, LineBreakClass.LBP_CL ),	/* Right double quotation mark: closing */
            new LineBreakProperties( 0, 0, LineBreakClass.LBP_Undefined )
    };

    /**
     * Association data of language-specific line breaking properties with
     * language names.  This is the definition for the static data in this
     * file.  If you want more flexibility, or do not need the data here,
     * you may want to redefine \e lb_prop_lang_map in your C source file.
     */
    public static LineBreakPropertiesLang lb_prop_lang_map[] = new LineBreakPropertiesLang[]{
            new LineBreakPropertiesLang( "en", lb_prop_English ),
            new LineBreakPropertiesLang( "de", lb_prop_German ),
            new LineBreakPropertiesLang( "es", lb_prop_Spanish ),
            new LineBreakPropertiesLang( "fr", lb_prop_French ),
            new LineBreakPropertiesLang( "ru", lb_prop_Russian ),
            new LineBreakPropertiesLang( "zh", lb_prop_Chinese ),
//		new LineBreakPropertiesLang( null, 0, null )
    };
}
