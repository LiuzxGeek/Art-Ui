package com.art.uilibrary.widget;


import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import java.lang.reflect.Field;

/**
 * Created by 56262 on 2017/10/19.
 * 对最长字符控制的计算方式进行修改(InputFilter.LengthFilter的计算方式)
 * 中文和中文符号计为2个字符，英文、数字和英文符号计为1个字符。
 */
public class SuperEditTexit extends EditText {

    public SuperEditTexit(Context context) {
        super(context);
    }

    public SuperEditTexit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setFilters(InputFilter[] filters) {
        if (filters == null) {
            return;
        }
        int len = filters.length;
        if (len <= 0) {
            return;
        }
        for (int i = len - 1; i >= 0; i--) {
            InputFilter filter = filters[i];
            if (filter instanceof InputFilter.LengthFilter) {
                filters[i] = new SMLengthFilter(getMaxLength((InputFilter.LengthFilter) filter));
                break;
            }
        }
        super.setFilters(filters);
    }

    private int getMaxLength(InputFilter.LengthFilter filter) {
        int max = 0;
        Class clazz = (Class) filter.getClass();
        try {
            Field field = clazz.getDeclaredField("mMax");
            field.setAccessible(true);
            max = field.getInt(filter);
            Log.d("getMaxLength", "max=" + max);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return max;
    }

    public static class SMLengthFilter extends InputFilter.LengthFilter {

        int selfMax = 0;

        public SMLengthFilter(int max) {
            super(max);
            selfMax = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//            Log.d("debug", source + "/" + start + "/" + end + "/" + dest.toString() + "/" + dstart + "/" + dend);
            int destLen = dend = getSequenceLength(dest);
            int sourceLen = getSequenceLength(source);
            int keep = selfMax - destLen;
            if (keep <= 0) {
                return "";
            } else if (keep >= sourceLen) {
                return null; // keep original
            } else {
                return subSequence(source, keep);
            }
        }

        private CharSequence subSequence(CharSequence source, int keep) {
            if (source == null || source.length() <= 0) {
                return "";
            }
            int length = 0;
            char c;
            for (int i = 0; i < source.length(); i++) {
                c = source.charAt(i);
                if (isChinese(c)) {
                    length += 2;
                } else {
                    length++;
                }
                if (length > keep) {
                    if (i == 0) {
                        return "";
                    } else {
                        return source.subSequence(0, i);
                    }
                }
            }
            return source;
        }

        private int getSequenceLength(CharSequence cs) {
            int length = 0;
            if (cs == null || cs.length() == 0) return length;
            char c;
            for (int i = 0; i < cs.length(); i++) {
                c = cs.charAt(i);
                if (isChinese(c)) {
                    length += 2;
                } else {
                    length++;
                }
            }
            return length;
        }

        private boolean isChinese(char c) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                return true;
            }
            return false;
        }

/*        private boolean isChinese(String s) {
            Pattern p= Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m=p.matcher(s);
            if(m.matches()){
                return true;
            } else {
                return false;
            }
        }*/
    }
}
