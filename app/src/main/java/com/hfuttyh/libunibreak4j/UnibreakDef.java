package com.hfuttyh.libunibreak4j;

public class UnibreakDef {
    public static final char EOS           = 0xFFFF;


    /**
     * Gets the next Unicode character in a UTF-16 sequence.  The index will
     * be advanced to the next complete character, unless the end of string
     * is reached in the middle of a UTF-16 surrogate pair.
     *
     * @param     s     [in]    input UTF-16 string
     * @return                the Unicode character beginning at the index; or
     *                        #EOS if end of input is encountered
     */
    public static char lb_get_next_char_utf16(char[] s, int curPos)
    {
        char ch;
//        int ip = mPosCur;

//        assert(*ip <= len);
        if (curPos == s.length)
            return EOS;
        ch = s[curPos++];

        // if (ch < 0xD800 || ch > 0xDBFF)
        if (!Character.isHighSurrogate(ch))
        {    /* If the character is not a high surrogate */
            return ch;
        }
        if (curPos == s.length)
        {    /* If the input ends here (an error) */
            --curPos;
            return EOS;
        }
        // if (s[mPosCur] < 0xDC00 || s[mPosCur] > 0xDFFF)
        if (!Character.isLowSurrogate(s[curPos]))
        {    /* If the next character is not the low surrogate (an error) */
            return ch;
        }
        /* Return the constructed character and advance the index again */
        return (char)((((int)ch & 0x3FF) << 10) + (s[curPos++] & 0x3FF) + 0x10000);
    }

    public static int lb_get_next_char_length(char[] s, int curPos)
    {
        char ch;
//        int ip = mPosCur;

//        assert(*ip <= len);
        if (curPos == s.length)
            return 0;
        ch = s[curPos++];

        // if (ch < 0xD800 || ch > 0xDBFF)
        if (!Character.isHighSurrogate(ch))
        {    /* If the character is not a high surrogate */
            return 1;
        }
        if (curPos == s.length)
        {    /* If the input ends here (an error) */
            --curPos;
            return 0;
        }
        // if (s[mPosCur] < 0xDC00 || s[mPosCur] > 0xDFFF)
        if (!Character.isLowSurrogate(s[curPos]))
        {    /* If the next character is not the low surrogate (an error) */
            return 1;
        }
        /* Return the constructed character and advance the index again */
        return 2;
    }
}
