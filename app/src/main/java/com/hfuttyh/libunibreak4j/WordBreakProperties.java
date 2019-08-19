package com.hfuttyh.libunibreak4j;

public class WordBreakProperties {
    public enum WordBreakClass
    {
        WBP_Undefined,
        WBP_CR,
        WBP_LF,
        WBP_Newline,
        WBP_Extend,
        WBP_ZWJ,
        WBP_Regional_Indicator,
        WBP_Format,
        WBP_Katakana,
        WBP_Hebrew_Letter,
        WBP_ALetter,
        WBP_Single_Quote,
        WBP_Double_Quote,
        WBP_MidNumLet,
        WBP_MidLetter,
        WBP_MidNum,
        WBP_Numeric,
        WBP_ExtendNumLet,
        WBP_WSegSpace,
        WBP_Any
    };

    int start;              /**< Start codepoint */
    int end;                /**< End codepoint, inclusive */
    WordBreakClass prop;   /**< The word breaking property */

    public WordBreakProperties(int start, int end, WordBreakClass prop)
    {
        this.start = start;
        this.end = end;
        this.prop = prop;
    }
}
