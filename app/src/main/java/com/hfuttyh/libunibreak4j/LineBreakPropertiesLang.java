package com.hfuttyh.libunibreak4j;

/**
 * Struct for association of language-specific line breaking properties
 * with language names.
 */
public class LineBreakPropertiesLang
{
	String lang;						/**< Language name */
	LineBreakProperties[] lbp;			/**< Pointer to associated data */

	public LineBreakPropertiesLang(String l, LineBreakProperties[] lbpp)
	{
		lang = l;
		lbp = lbpp;
	}
};