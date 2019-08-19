package com.hfuttyh.libunibreak4j;

import android.text.TextUtils;

import com.hfuttyh.libunibreak4j.LineBreakDef.LineBreakClass;

public class LineBreak
{
    public static final byte LINEBREAK_MUSTBREAK      = 0;    /* Break is mandatory */
    public static final byte LINEBREAK_ALLOWBREAK     = 1;    /* Break is allowed */
    public static final byte LINEBREAK_NOBREAK        = 2;    /* No break is possible */
    public static final byte LINEBREAK_INSIDEACHAR    = 3;    /* A UTF-8/16 sequence is unfinished */

    /**
     * Special value used internally to indicate an undefined break result.
     */
    private static final byte LINEBREAK_UNDEFINED    = -1;

    private static int LINEBREAK_INDEX_SIZE         = 40;
    
    /**
     * Enumeration of break actions.  They are used in the break action
     * pair table #baTable.
     */
    public  enum BreakAction
    {
        DIR_BRK,        /**< Direct break opportunity */
        IND_BRK,        /**< Indirect break opportunity */
        CMI_BRK,        /**< Indirect break opportunity for combining marks */
        CMP_BRK,        /**< Prohibited break for combining marks */
        PRH_BRK         /**< Prohibited break */
    }
    
    /**
     * Break action pair table.  This is a direct mapping of Table 2 of
     * Unicode Standard Annex 14, Revision 37, except for ZWJ (manually
     * adjusted after special processing as per LB8a of Revision 41) and CB
     * (manually added as per LB20).
     */
    private static BreakAction[][] baTable = new BreakAction[][]
    {
        {   /* OP */
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK,
                BreakAction.CMP_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK },
        {   /* CL */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* CP */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* QU */
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK },
        {   /* GL */
                BreakAction.IND_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK },
        {   /* NS */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* EX */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* SY */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* IS */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* PR */
                BreakAction.IND_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* PO */
                BreakAction.IND_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* NU */
                BreakAction.IND_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* AL */
                BreakAction.IND_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* HL */
                BreakAction.IND_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* ID */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* IN */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* HY */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* BA */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* BB */
                BreakAction.IND_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* B2 */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* ZW */
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK },
        {   /* CM */
                BreakAction.IND_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* WJ */
                BreakAction.IND_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK },
        {   /* H2 */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* H3 */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* JL */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* JV */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* JT */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* RI */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* EB */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* EM */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* ZWJ */
                BreakAction.IND_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK,
                BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
        {   /* CB */
                BreakAction.DIR_BRK, BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.IND_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.PRH_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.PRH_BRK,
                BreakAction.CMI_BRK, BreakAction.PRH_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK,
                BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.DIR_BRK, BreakAction.IND_BRK, BreakAction.DIR_BRK },
    };
    /**
     * Struct for the second-level index to the line breaking properties.
     */
    private class LineBreakPropertiesIndex
    {
        int end;                    /**< End coding point */
        int lbp_index;                /**< Pointer to line breaking properties */
        
        public LineBreakPropertiesIndex()
        {
            
        }
    }

    /**
     * Second-level index to the line breaking properties.
     */
    private static LineBreakPropertiesIndex[] lb_prop_index = new LineBreakPropertiesIndex[LINEBREAK_INDEX_SIZE];

    public LineBreak()
    {
        init_linebreak();
    }
    /**
     * Initializes the second-level index to the line breaking properties.
     * If it is not called, the performance of #get_char_lb_class_lang (and
     * thus the main functionality) can be pretty bad, especially for big
     * code points like those of Chinese.
     */
    private void init_linebreak()
    {
        int i;
        int iPropDefault;
        int step;

        step = (LineBreakData.lb_prop_default.length-1) / LINEBREAK_INDEX_SIZE;
        iPropDefault = 0;
        for (i = 0; i < LINEBREAK_INDEX_SIZE; ++i)
        {
            lb_prop_index[i] = new LineBreakPropertiesIndex();
            lb_prop_index[i].lbp_index = iPropDefault;
            iPropDefault += step;
            lb_prop_index[i].end = LineBreakData.lb_prop_default[iPropDefault].start - 1;
        }
        lb_prop_index[--i].end = 0xFFFFFFFF;
    }

    /**
     * Gets the language-specific line breaking properties.
     *
     * @param lang    language of the text
     * @return        pointer to the language-specific line breaking
     *                properties array if found; \c NULL otherwise
     */
    private static LineBreakProperties[] get_lb_prop_lang(String lang)
    {
        LineBreakPropertiesLang lbplIter;
        if (lang != null)
        {
            for (int k = 0; k < LineBreakDef.lb_prop_lang_map.length; k++)
            {
                lbplIter = LineBreakDef.lb_prop_lang_map[k];
                if (TextUtils.equals(lbplIter.lang, lang))
                    return lbplIter.lbp;
            }
        }
        return null;
    }

    /**
     * Gets the line breaking class of a character from a line breaking
     * properties array.
     *
     * @param ch    character to check
     * @param lbp    pointer to the line breaking properties array
     * @return        the line breaking class if found; \c LBP_XX otherwise
     */
    private static LineBreakClass get_char_lb_class(
            int ch,
            LineBreakProperties[] lbp,
            int start)
    {
        int k = start;
        LineBreakProperties lbpp = lbp[k];
        while (lbpp.prop != LineBreakClass.LBP_Undefined && ch >= lbpp.start)
        {
            if (ch <= lbpp.end)
                return lbpp.prop;
            lbpp = lbp[++k];
        }
        return LineBreakClass.LBP_XX;
    }

    /**
     * Gets the line breaking class of a character from the default line
     * breaking properties array.
     *
     * @param ch    character to check
     * @return        the line breaking class if found; \c LBP_XX otherwise
     */
    private static LineBreakClass get_char_lb_class_default(
            int ch)
    {
        int i = 0;
        while (ch > lb_prop_index[i].end)
        {
            ++i;
            if ( i == LINEBREAK_INDEX_SIZE)
            {
                i--;
                break;
            }
        }
        return get_char_lb_class(ch, LineBreakData.lb_prop_default, lb_prop_index[i].lbp_index);
    }

    /**
     * Gets the line breaking class of a character for a specific
     * language.  This function will check the language-specific data first,
     * and then the default data if there is no language-specific property
     * available for the character.
     *
     * @param ch        character to check
     * @param lbpLang    pointer to the language-specific line breaking
     *                    properties array
     * @return            the line breaking class if found; \c LBP_XX
     *                    otherwise
     */
    private static LineBreakClass get_char_lb_class_lang(
            int ch,
            LineBreakProperties[] lbpLang)
    {
        LineBreakClass lbcResult;

        /* Find the language-specific line breaking class for a character */
        if (lbpLang != null)
        {
            lbcResult = get_char_lb_class(ch, lbpLang, 0);
            if (lbcResult != LineBreakClass.LBP_XX)
                return lbcResult;
        }

        /* Find the generic language-specific line breaking class, if no
         * language context is provided, or language-specific data are not
         * available for the specific character in the specified language */
        return get_char_lb_class_default(ch);
    }

    /**
     * Resolves the line breaking class for certain ambiguous or complicated
     * characters.  They are treated in a simplistic way in this
     * implementation.
     *
     * @param lbc    line breaking class to resolve
     * @param lang    language of the text
     * @return        the resolved line breaking class
     */
    private static LineBreakClass resolve_lb_class(LineBreakClass lbc, String lang)
    {
        switch (lbc)
        {
            case LBP_AI:
                if (lang != null &&
                        (lang.equalsIgnoreCase("zh") ||    /* Chinese */
                         lang.equalsIgnoreCase("ja") ||    /* Japanese */
                         lang.equalsIgnoreCase("ko")))    /* Korean */
                    return LineBreakClass.LBP_ID;
            case LBP_CJ:
                /* `Strict' and `normal' line breaking.  See
                 * <url:http://www.unicode.org/reports/tr14/#CJ>
                 * for details. */
                if (lang != null && lang.endsWith("-strict"))
                {
                    return LineBreakClass.LBP_NS;
                }
                else
                {
                    return LineBreakClass.LBP_ID;
                }
            case LBP_SA:
            case LBP_SG:
            case LBP_XX:
                return LineBreakClass.LBP_AL;
            default:
                return lbc;
        }
    }

    /**
     * Treats specially for the first character in a line.
     *
     * @param[in,out] lbpCtx  pointer to the line breaking context
     * @pre                   \a lbpCtx->lbcCur has a valid line break class
     * @post                  \a lbpCtx->lbcCur has the updated line break class
     */
    private static void treat_first_char(LineBreakContext lbpCtx)
    {
        switch (lbpCtx.lbcCur)
        {
            case LBP_LF:
            case LBP_NL:
                lbpCtx.lbcCur = LineBreakClass.LBP_BK;        /* Rule LB5 */
                break;
            case LBP_SP:
                lbpCtx.lbcCur = LineBreakClass.LBP_WJ;        /* Leading space treated as WJ */
                break;
            default:
                break;
        }
    }

    /**
     * Tries telling the line break opportunity by simple rules.
     *
     * @param[in,out] lbpCtx  pointer to the line breaking context
     * @pre                   \a lbpCtx->lbcCur has the current line break
     *                        class; and \a lbpCtx->lbcNew has the line
     *                        break class for the next character
     * @post                  \a lbpCtx->lbcCur has the updated line break
     *                        class
     * @return                break result, one of #LINEBREAK_MUSTBREAK,
     *                        #LINEBREAK_ALLOWBREAK, and #LINEBREAK_NOBREAK
     *                        if identified; or #LINEBREAK_UNDEFINED if
     *                        table lookup is needed
     */
    private static int get_lb_result_simple(LineBreakContext lbpCtx)
    {
        if (lbpCtx.lbcCur == LineBreakClass.LBP_BK
                || (lbpCtx.lbcCur == LineBreakClass.LBP_CR && lbpCtx.lbcNew != LineBreakClass.LBP_LF))
        {
            return LINEBREAK_MUSTBREAK;     /* Rules LB4 and LB5 */
        }

        switch (lbpCtx.lbcNew)
        {
            case LBP_SP:
                return LINEBREAK_NOBREAK;       /* Rule LB7; no change to lbcCur */
            case LBP_BK:
            case LBP_LF:
            case LBP_NL:
                lbpCtx.lbcCur = LineBreakClass.LBP_BK;        /* Mandatory break after */
                return LINEBREAK_NOBREAK;       /* Rule LB6 */
            case LBP_CR:
                lbpCtx.lbcCur = LineBreakClass.LBP_CR;
                return LINEBREAK_NOBREAK;       /* Rule LB6 */
            default:
                return LINEBREAK_UNDEFINED;     /* Table lookup is needed */
        }
    }

    /**
     * Tells the line break opportunity by table lookup.
     *
     * @param[in,out] lbpCtx  pointer to the line breaking context
     * @pre                   \a lbpCtx->lbcCur has the current line break
     *                        class; \a lbpCtx->lbcLast has the line break
     *                        class for the last character; and \a
     *                        lbcCur->lbcNew has the line break class for
     *                        the next character
     * @post                  \a lbpCtx->lbcCur has the updated line break
     *                        class
     * @return                break result, one of #LINEBREAK_MUSTBREAK,
     *                        #LINEBREAK_ALLOWBREAK, and #LINEBREAK_NOBREAK
     */
    private static int get_lb_result_lookup(LineBreakContext lbpCtx)
    {
        int brk = LINEBREAK_UNDEFINED;

//        assert(lbpCtx.lbcCur.compareTo(LineBreakClass.LBP_CB) <= 0);
//        assert(lbpCtx.lbcNew.compareTo(LineBreakClass.LBP_CB) <= 0);
        switch (baTable[lbpCtx.lbcCur.ordinal() - 1][lbpCtx.lbcNew.ordinal() - 1])
        {
            case DIR_BRK:
                brk = LINEBREAK_ALLOWBREAK;
                break;
            case IND_BRK:
                brk = (lbpCtx.lbcLast == LineBreakClass.LBP_SP)
                        ? LINEBREAK_ALLOWBREAK
                        : LINEBREAK_NOBREAK;
                break;
            case CMI_BRK:
                brk = LINEBREAK_ALLOWBREAK;
                if (lbpCtx.lbcLast != LineBreakClass.LBP_SP)
                {
                    brk = LINEBREAK_NOBREAK;
                    return brk;                 /* Do not update lbcCur */
                }
                break;
            case CMP_BRK:
                brk = LINEBREAK_NOBREAK;
                if (lbpCtx.lbcLast != LineBreakClass.LBP_SP)
                    return brk;                 /* Do not update lbcCur */
                break;
            case PRH_BRK:
                brk = LINEBREAK_NOBREAK;
                break;
        }

        /* Special processing due to rule LB8a */
        if (lbpCtx.fLb8aZwj)
        {
            brk = LINEBREAK_NOBREAK;
        }

        /* Special processing due to rule LB21a */
        if (lbpCtx.fLb21aHebrew &&
                (lbpCtx.lbcCur == LineBreakClass.LBP_HY || lbpCtx.lbcCur == LineBreakClass.LBP_BA))
        {
            brk = LINEBREAK_NOBREAK;
            lbpCtx.fLb21aHebrew = false;
        }
        else
        {
            lbpCtx.fLb21aHebrew = (lbpCtx.lbcCur == LineBreakClass.LBP_HL);
        }

        /* Special processing due to rule LB30a */
        if (lbpCtx.lbcCur == LineBreakClass.LBP_RI)
        {
            lbpCtx.cLb30aRI++;
            if (lbpCtx.cLb30aRI == 2 && lbpCtx.lbcNew == LineBreakClass.LBP_RI)
            {
                brk = LINEBREAK_ALLOWBREAK;
                lbpCtx.cLb30aRI = 0;
            }
        }
        else
        {
            lbpCtx.cLb30aRI = 0;
        }

        lbpCtx.lbcCur = lbpCtx.lbcNew;
        return brk;
    }

    /**
     * Initializes line breaking context for a given language.
     *
     * @param lbpCtx    [in,out]  pointer to the line breaking context
     * @param ch        [in]     the first character to process
     * @param lang      [in]    language of the input
     * @post                  the line breaking context is initialized
     */
    private void lb_init_break_context(LineBreakContext lbpCtx, int ch,    String lang)
    {
        lbpCtx.lang = lang;
        lbpCtx.lbpLang = get_lb_prop_lang(lang);
        lbpCtx.lbcLast = LineBreakClass.LBP_Undefined;
        lbpCtx.lbcNew = LineBreakClass.LBP_Undefined;
        lbpCtx.lbcCur = resolve_lb_class(
                get_char_lb_class_lang(ch, lbpCtx.lbpLang),
                lbpCtx.lang);
        lbpCtx.fLb8aZwj =
                (get_char_lb_class_lang(ch, lbpCtx.lbpLang) == LineBreakClass.LBP_ZWJ);
        lbpCtx.fLb10LeadSpace =
                (get_char_lb_class_lang(ch, lbpCtx.lbpLang) == LineBreakClass.LBP_SP);
        lbpCtx.fLb21aHebrew = false;
        lbpCtx.cLb30aRI = 0;
        treat_first_char(lbpCtx);
    }

    /**
     * Updates LineBreakingContext for the next codepoint and returns
     * the detected break.
     *
     * @param   lbpCtx  [in,out]  pointer to the line breaking context
     * @param   ch      [in]      Unicode codepoint
     * @return                break result, one of #LINEBREAK_MUSTBREAK,
     *                        #LINEBREAK_ALLOWBREAK, and #LINEBREAK_NOBREAK
     *                        the line breaking context is updated
     */
    private int lb_process_next_char(LineBreakContext lbpCtx, char ch)
    {
        int brk;

        lbpCtx.lbcLast = lbpCtx.lbcNew;
        lbpCtx.lbcNew = get_char_lb_class_lang(ch, lbpCtx.lbpLang);
        brk = get_lb_result_simple(lbpCtx);
        switch (brk)
        {
            case LINEBREAK_MUSTBREAK:
                lbpCtx.lbcCur = resolve_lb_class(lbpCtx.lbcNew, lbpCtx.lang);
                treat_first_char(lbpCtx);
                break;
            case LINEBREAK_UNDEFINED:
                lbpCtx.lbcNew = resolve_lb_class(lbpCtx.lbcNew, lbpCtx.lang);
                brk = get_lb_result_lookup(lbpCtx);
                break;
            default:
                break;
        }

        /* Special processing due to rule LB8a */
        if (lbpCtx.lbcNew == LineBreakClass.LBP_ZWJ)
        {
            lbpCtx.fLb8aZwj = true;
        }
        else
        {
            lbpCtx.fLb8aZwj = false;
        }

        /* Special processing due to rule LB10 */
        if (lbpCtx.fLb10LeadSpace)
        {
            if (lbpCtx.lbcNew == LineBreakClass.LBP_CM || lbpCtx.lbcNew == LineBreakClass.LBP_ZWJ)
                brk = LINEBREAK_ALLOWBREAK;
            lbpCtx.fLb10LeadSpace = false;
        }

        return brk;
    }

    /**
     * Sets the line breaking information for a generic input string.
     *
     * Currently, this implementation has customization for the following
     * ISO 639-1 language codes (for \a lang):
     *
     *  - de (German)
     *  - en (English)
     *  - es (Spanish)
     *  - fr (French)
     *  - ja (Japanese)
     *  - ko (Korean)
     *  - ru (Russian)
     *  - zh (Chinese)
     *
     * In addition, a suffix <code>"-strict"</code> may be added to indicate
     * strict (as versus normal) line-breaking behaviour.  See the <a
     * href="http://www.unicode.org/reports/tr14/#CJ">Conditional Japanese
     * Starter section of UAX #14</a> for more details.
     *
     * @param   s        [in]          input string
     * @param   lang     [in]          language of the input
     * @param   brks     [out]         pointer to the output breaking data,
     *                           containing #LINEBREAK_MUSTBREAK,
     *                           #LINEBREAK_ALLOWBREAK, #LINEBREAK_NOBREAK,
     *                           or #LINEBREAK_INSIDEACHAR
     */
    public void set_linebreaks(char[] s, String lang, int[] brks)
    {
        char ch;
        LineBreakContext lbCtx = new LineBreakContext();
        int posCur = 0;
        int posLast = 0;

        --posLast;  /* To be ++'d later */
        ch = UnibreakDef.lb_get_next_char_utf16(s, posCur);
        posCur += UnibreakDef.lb_get_next_char_length(s, posCur);
        if (ch == UnibreakDef.EOS)
            return;
        lb_init_break_context(lbCtx, ch, lang);

        /* Process a line till an explicit break or end of string */
        for (;;)
        {
            for (++posLast; posLast < posCur - 1; ++posLast)
            {
                brks[posLast] = LINEBREAK_INSIDEACHAR;
            }
            // assert(posLast == posCur - 1);
            ch = UnibreakDef.lb_get_next_char_utf16(s, posCur);
            posCur += UnibreakDef.lb_get_next_char_length(s, posCur);
            if (ch == UnibreakDef.EOS)
                break;
            brks[posLast] = lb_process_next_char(lbCtx, ch);
        }

        // assert(posLast == mPosCur - 1 && mPosCur <= s.length);
        /* Break after the last character */
        brks[posLast] = LINEBREAK_MUSTBREAK;
        /* When the input contains incomplete sequences */
        while (posCur < s.length)
        {
            brks[posCur++] = LINEBREAK_INSIDEACHAR;
        }
    }

}
