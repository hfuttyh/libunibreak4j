package com.hfuttyh.libunibreak4j;

import com.hfuttyh.libunibreak4j.LineBreakDef.LineBreakClass;

/**
 * Struct for entries of line break properties.  The array of the
 * entries \e must be sorted.
 */
public class LineBreakProperties
{
	int start;				/**< Starting coding point */
	int end;				/**< End coding point */
	LineBreakClass prop;				/**< The line breaking property */

	public LineBreakProperties(int s, int e, LineBreakClass p)
	{
		start = s;
		end = e;
		prop = p;
	}
};