package com.hfuttyh.libunibreak4j;

public class ExtendedPictograpic {
    public int start;                /**< Start codepoint */
    public int end;                  /**< End codepoint, inclusive */

    public ExtendedPictograpic(int start, int end)
    {
        this.start = start;
        this.end = end;
    }
}
