package com.hfuttyh.libunibreak4j;

public class EmojiDef {

    /**
     * Finds out if a codepoint is extended pictographic.
     *
     * @param[in] ch  character to check
     * @return        \c true if the codepoint is extended pictographic;
     *                \c false otherwise
     */
    public static boolean ub_is_extended_pictographic(int ch)
    {
        int min = 0;
        int max = EmojiData.ep_prop.length;
        int mid;

        do
        {
            mid = (min + max) / 2;

            if (ch < EmojiData.ep_prop[mid].start)
                max = mid - 1;
            else if (ch > EmojiData.ep_prop[mid].end)
                min = mid + 1;
            else
                return true;
        } while (min <= max);

        return false;
    }
}
