package com.example.libunibreak4j;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hfuttyh.libunibreak4j.LineBreak;
import com.hfuttyh.libunibreak4j.WordBreak;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "libunibreak";

    private static String[] sarr = new String[] {
            "Combining marks and accented letters behave as expected: Your naïveté amuses me!",
            "Unbreakable: $(12.35) 2,1234 (12)¢ 12.54¢ (s)he file(s); breakable: ()()",
            "Unbreakable pairs: ‘$9’, ‘$[’, ‘$-’, ‘-9’, ‘/9’, ‘99’, ‘,9’, ‘9%’, ‘]%’",
            "Hyphenated words are breakable: server-side",
            "Em dashes can be broken on either side, but not in the middle: A——B",
            "NOT each space is breakable, like the last one in the following sentence (in English): He did say, “He said, ‘Good morning.’ ”",
            "Regretfully it does not work for the alternative quoting style due to the ambiguity of “’” (quote and apostrophe): one should not insert normal spaces between the last two quotation marks in ‘He said, “Good morning.”’ — Of course, non-breaking spaces are OK: ‘He said, “Good morning.” ’",
            "Still, the space rule makes my favourite ellipsis style work well, like ‘once upon a time . . .’.",
            "Here's an apostrophe; and there’s a right single quotation mark.",
            "Spanish inverted punctuation marks are always opening (e.g.)¡Foo bar!",
            "There should be no break after Hebrew + Hyphen like א-ת, or between solidus and Hebrew like /ת.",
            "Lines like this should not have their punctuation broken!… ",
            "Emojis joined by ZWJs should not be broken: 👩‍❤️‍👩.",
            "Breaks can occur between Chinese characters, except before or after certain punctuation marks: 「你看過《三國演義》嗎？」他問我。",
            "我们的祖国就像一个大家庭，而穆斯林是这个大家庭的一员，常言道：国家兴亡，匹夫有责。穆罕默德圣人صلى الله عليه وسلم说，爱国是信仰的一部分，所以，我今天演讲的题目是：爱国爱教。"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LineBreak lb = new LineBreak();
        WordBreak wb = new WordBreak();
        for (String text:sarr)
        {
            int[] brk = new int[text.length()];

            lb.set_linebreaks(text.toCharArray(), "zh", brk);
            dumpLineBreak(text, brk);
            int[] wbrk = new int[text.length()];
            wb.set_wordbreaks(text.toCharArray(), "en", wbrk);
            dumpWordBreak(text, wbrk);
        }
    }

    private void dumpLineBreak(String text, int[] brk)
    {
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < text.length(); k++)
        {
            String b;
            switch (brk[k])
            {
                case LineBreak.LINEBREAK_MUSTBREAK:
                    b = "#";
                    break;
                case LineBreak.LINEBREAK_ALLOWBREAK:
                    b = "|";
                    break;
                default:
                    b = "";
                    break;
            }
            sb.append(text.charAt(k)+b);
        }
        Log.d(TAG, sb.toString());
    }

    private void dumpWordBreak(String text, int[] brk)
    {
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < text.length(); k++)
        {
            String b;
            switch (brk[k])
            {
                case WordBreak.WORDBREAK_BREAK:
                    b = "|";
                    break;
                case WordBreak.WORDBREAK_INSIDEACHAR:
                    b = "·";
                    break;
                default:
                    b = "";
                    break;
            }
            sb.append(text.charAt(k)+b);
        }
        Log.d(TAG, sb.toString());
    }
}
