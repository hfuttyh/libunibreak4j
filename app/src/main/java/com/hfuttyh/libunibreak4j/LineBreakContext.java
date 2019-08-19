package com.hfuttyh.libunibreak4j;

import com.hfuttyh.libunibreak4j.LineBreakDef.LineBreakClass;
/**
 * Context representing internal state of the line breaking algorithm.
 * This is useful to callers if incremental analysis is wanted.
 */
public class LineBreakContext
{
    public String lang;                    /**< Language name */
    public LineBreakProperties[] lbpLang;  /**< Pointer to LineBreakProperties */
    public LineBreakClass lbcCur;          /**< Breaking class of current codepoint */
    public LineBreakClass lbcNew;          /**< Breaking class of next codepoint */
    public LineBreakClass lbcLast;         /**< Breaking class of last codepoint */
    public Boolean fLb8aZwj;               /**< Flag for ZWJ (LB8a) */
    public Boolean fLb10LeadSpace;         /**< Flag for leading space (LB10) */
    public Boolean fLb21aHebrew;           /**< Flag for Hebrew letters (LB21a) */
    public int cLb30aRI;                   /**< Count of RI characters (LB30a) */
};
