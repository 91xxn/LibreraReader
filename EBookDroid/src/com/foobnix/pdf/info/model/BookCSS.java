package com.foobnix.pdf.info.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.foobnix.android.utils.TxtUtils;
import com.foobnix.pdf.info.ExtUtils;
import com.foobnix.pdf.info.FontExtractor;
import com.foobnix.pdf.info.wrapper.MagicHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;

public class BookCSS {

    public static final String FONTS_DIR = "Fonts";

    public static final int TEXT_ALIGN_JUSTIFY = 0;
    public static final int TEXT_ALIGN_LEFT = 1;
    public static final int TEXT_ALIGN_RIGHT = 2;
    public static final int TEXT_ALIGN_CENTER = 3;

    public static int STYLES_DOC_AND_USER = 0;
    public static int STYLES_ONLY_DOC = 1;
    public static int STYLES_ONLY_USER = 2;

    public static final String COURIER = "Courier";
    private static final String DEFAULT_FONT = "Times New Roman";
    private static final String BOOK_CSS_SP = "BookCSS";
    public static List<String> fontExts = Arrays.asList(".ttf", ".otf");

    private static List<String> FONT_NAMES = Arrays.asList(//
            "Arial", //
            // "Noto Serif", "Noto Sans", //
            // "Charis SIL", //
            COURIER, //
            // "Helvetica", //
            DEFAULT_FONT);
    static {
        Collections.sort(FONT_NAMES);
    }
    public int documentStyle = STYLES_DOC_AND_USER;
    public int marginTop;
    public int marginRight;
    public int marginBottom;
    public int marginLeft;

    public int emptyLine;

    public int lineHeight;
    public int textIndent;
    public int fontWeight;

    public int textAlign;

    public String fontFolder;

    public String normalFont;
    public String boldFont;
    public String boldItalicFont;
    public String italicFont;
    public String headersFont;

    public int spinnerIndex = FONT_NAMES.indexOf(DEFAULT_FONT);

    public void load(Context c) {
        if (c == null) {
            return;
        }
        resetToDefault(c);

        SharedPreferences sp = c.getSharedPreferences(BOOK_CSS_SP, Context.MODE_PRIVATE);
        fontFolder = sp.getString("fontFolder", fontFolder);

        normalFont = sp.getString("normalFont", normalFont);
        boldFont = sp.getString("boldFont", boldFont);
        boldItalicFont = sp.getString("boldItalicFont", boldItalicFont);
        italicFont = sp.getString("italicFont", italicFont);
        headersFont = sp.getString("headersFont", headersFont);

        marginTop = sp.getInt("marginTop", marginTop);
        marginRight = sp.getInt("marginRight", marginRight);
        marginBottom = sp.getInt("marginBottom", marginBottom);
        marginLeft = sp.getInt("marginLeft", marginLeft);
        emptyLine = sp.getInt("emptyLine", emptyLine);

        lineHeight = sp.getInt("lineHeight", lineHeight);
        textIndent = sp.getInt("textIndent", textIndent);
        fontWeight = sp.getInt("fontWeight", fontWeight);
        spinnerIndex = sp.getInt("spinnerIndex", spinnerIndex);
        documentStyle = sp.getInt("documentStyle", documentStyle);

    }

    public int position(String fontName) {
        try {
            List<String> allFonts = getAllFonts();
            return allFonts.indexOf(fontName);
        } catch (Exception e) {
            return 0;
        }

    }

    public void resetToDefault(Context c) {
        textAlign = TEXT_ALIGN_JUSTIFY;

        marginTop = 6;
        marginRight = 5;
        marginBottom = 6;
        marginLeft = 5;

        emptyLine = 5;

        lineHeight = 13;
        textIndent = 10;
        fontWeight = 400;

        fontFolder = FontExtractor.getFontsDir(c, FONTS_DIR).getPath();
        normalFont = DEFAULT_FONT;
        boldFont = DEFAULT_FONT;
        italicFont = DEFAULT_FONT;
        boldItalicFont = DEFAULT_FONT;
        headersFont = DEFAULT_FONT;

        documentStyle = STYLES_DOC_AND_USER;
    }

    public void allFonts(String fontName) {
        normalFont = fontName;
    }

    public void save(Context c) {
        if (c == null) {
            return;
        }
        SharedPreferences sp = c.getSharedPreferences(BOOK_CSS_SP, Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.putString("fontFolder", fontFolder);

        edit.putString("normalFont", normalFont);
        edit.putString("boldFont", boldFont);
        edit.putString("italicFont", italicFont);
        edit.putString("boldItalicFont", boldItalicFont);
        edit.putString("headersFont", headersFont);

        edit.putInt("marginTop", marginTop);
        edit.putInt("marginRight", marginRight);
        edit.putInt("marginBottom", marginBottom);
        edit.putInt("marginLeft", marginLeft);
        edit.putInt("emptyLine", emptyLine);

        edit.putInt("lineHeight", lineHeight);
        edit.putInt("textIndent", textIndent);
        edit.putInt("fontWeight", fontWeight);
        edit.putInt("spinnerIndex", spinnerIndex);
        edit.putInt("documentStyle", documentStyle);

        edit.commit();
    }

    public void resetAll(String initFontName) {
        if (FONT_NAMES.contains(initFontName)) {
            normalFont = initFontName;
            boldFont = initFontName;
            italicFont = initFontName;
            boldItalicFont = initFontName;
            headersFont = initFontName;
            return;
        }
        normalFont = DEFAULT_FONT;
        boldFont = DEFAULT_FONT;
        italicFont = DEFAULT_FONT;
        boldItalicFont = DEFAULT_FONT;
        headersFont = DEFAULT_FONT;

        initFontName = initFontName.replace(".ttf", "").replace(".otf", "").trim().toLowerCase(Locale.US);

        List<String> allFonts = getAllFonts();
        for (String fullName : allFonts) {
            String fontName = fullName.replace(".ttf", "").replace(".otf", "").trim().toLowerCase(Locale.US);

            if (fontName.startsWith(initFontName) || fontName.equals(initFontName)) {

                if (fontName.equals(initFontName) || fontName.endsWith("regular") || fontName.endsWith("normal") || fontName.endsWith("light") || fontName.endsWith("medium") || fontName.endsWith("me")) {
                    normalFont = fullName;

                } else if (fontName.endsWith("bolditalic") || fontName.endsWith("boldit") || fontName.endsWith("boit") || fontName.endsWith("bold it") || fontName.endsWith("bold italic")) {
                    boldItalicFont = fullName;

                } else if (fontName.endsWith("bold") || fontName.endsWith("bo") || fontName.endsWith("bd") || fontName.endsWith("bolt")) {
                    headersFont = boldFont = fullName;

                } else if (fontName.endsWith("italic") || fontName.endsWith("it")) {
                    italicFont = fullName;

                }
            }
        }

    }

    public List<String> getAllFonts() {
        List<String> all = new ArrayList<String>();
        all.addAll(FONT_NAMES);
        all.addAll(getAllFontsFromFolder(fontFolder));
        return all;
    }

    public List<String> getAllFontsFiltered() {
        List<String> all = new ArrayList<String>();
        all.addAll(FONT_NAMES);
        all.addAll(getAllFontsFiltered(fontFolder));
        return all;
    }

    private Collection<String> getAllFontsFiltered(String path) {
        if (TxtUtils.isNotEmpty(path) && new File(path).isDirectory()) {
            File file = new File(path);
            String[] list = file.list(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    name = name.toLowerCase(Locale.US);
                    for (String ext : fontExts) {
                        if (name.endsWith(ext)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
            if (list != null && list.length >= 1) {
                List<String> filtered = new ArrayList<String>();

                for (String fontName : list) {
                    String ext = ExtUtils.getFileExtension(fontName);
                    if (fontName.contains("-")) {
                        fontName = fontName.substring(0, fontName.lastIndexOf("-")) + "." + ext;
                    } else if (fontName.contains("_")) {
                        fontName = fontName.substring(0, fontName.lastIndexOf("_")) + "." + ext;
                    } else if (fontName.contains(" ")) {
                        fontName = fontName.substring(0, fontName.lastIndexOf(" ")) + "." + ext;
                    }

                    if (!filtered.contains(fontName)) {
                        filtered.add(fontName);
                    }
                }

                Collections.sort(filtered, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.toLowerCase().compareTo(o2.toLowerCase());
                    }

                });
                return filtered;
            }
        }
        return Collections.EMPTY_LIST;
    }

    private Collection<String> getAllFontsFromFolder(String path) {
        if (TxtUtils.isNotEmpty(path) && new File(path).isDirectory()) {
            File file = new File(path);
            String[] list = file.list(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    name = name.toLowerCase(Locale.US);
                    for (String ext : fontExts) {
                        if (name.endsWith(ext)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
            if (list != null && list.length >= 1) {
                List<String> filtered = new ArrayList<String>();

                for (String fontName : list) {
                    filtered.add(fontName);
                }

                Collections.sort(filtered, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.toLowerCase().compareTo(o2.toLowerCase());
                    }

                });
                return filtered;
            }
        }
        return Collections.EMPTY_LIST;
    }

    public boolean isFontFileName(String name) {
        if (TxtUtils.isEmpty(name)) {
            return false;
        }
        name = name.toLowerCase(Locale.US);
        for (String ext : fontExts) {
            if (name.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    public String em(int value) {
        float em = (float) value / 10;
        return "" + em + "em";
    }

    private static BookCSS inst = new BookCSS();

    public static BookCSS get() {

        return inst;
    }

    public String getTextAlignConst(int id) {
        if (id == TEXT_ALIGN_JUSTIFY) {
            return "justify";
        }
        if (id == TEXT_ALIGN_LEFT) {
            return "left";
        }
        if (id == TEXT_ALIGN_RIGHT) {
            return "right";
        }
        if (id == TEXT_ALIGN_CENTER) {
            return "center";
        }
        return "initial";
    }

    public String toCssString() {
        StringBuilder builder = new StringBuilder();

        String backgroundColor = MagicHelper.colorToString(MagicHelper.getBgColor());
        String textColor = MagicHelper.colorToString(MagicHelper.getTextColor());

        builder.append("documentStyle" + documentStyle + "{}");

        // PAGE BEGIN
        builder.append("@page{");
        builder.append(String.format("margin-top:%s !important;", em(marginTop + 1)));
        builder.append(String.format("margin-right:%s !important;", em(marginRight)));
        builder.append(String.format("margin-bottom:%s !important;", em(marginBottom - 1)));
        builder.append(String.format("margin-left:%s !important;", em(marginLeft)));
        builder.append("}");
        // PAGE END

        // FB2
        builder.append("section>title{page-break-before:avoide;}");
        builder.append("title>p{text-align:center !important;}");
        builder.append("section+section>title{page-break-before:always}");
        builder.append(String.format("empty-line{padding-top:%s;}", em(emptyLine)));
        builder.append("epigraph{text-align:right; margin-left:2em;font-style: italic;}");
        builder.append("text-author{font-style: italic;font-weight: bold;}");
        // FB2 END

        builder.append("p,div,body{");
        builder.append(String.format("background-color:%s !important;", backgroundColor));
        builder.append(String.format("color:%s !important;", textColor));
        builder.append(String.format("line-height:%s !important;", em(lineHeight)));
        builder.append("}");

        builder.append("body{");
        builder.append("padding:0 !important; margin:0 !important;");
        builder.append("}");

        if (documentStyle == STYLES_DOC_AND_USER || documentStyle == STYLES_ONLY_USER) {

            // FONTS BEGIN
            if (isFontFileName(normalFont)) {
                builder.append("@font-face {font-family: my; src: url('" + getFontPath(normalFont) + "') format('truetype'); font-weight: normal; font-style: normal;}");
            }

            if (isFontFileName(boldFont)) {
                builder.append("@font-face {font-family: my; src: url('" + getFontPath(boldFont) + "') format('truetype'); font-weight: bold; font-style: normal;}");
            }

            if (isFontFileName(italicFont)) {
                builder.append("@font-face {font-family: my; src: url('" + getFontPath(italicFont) + "') format('truetype'); font-weight: normal; font-style: italic;}");
            }

            if (isFontFileName(boldItalicFont)) {
                builder.append("@font-face {font-family: my; src: url('" + getFontPath(boldItalicFont) + "') format('truetype'); font-weight: bold; font-style: italic;}");
            }

            if (isFontFileName(headersFont)) {
                builder.append("@font-face {font-family: myHeader; src: url('" + getFontPath(headersFont) + "') format('truetype');}");
                builder.append("h1{font-size:1.50em; text-align: center; font-weight: normal; font-family: myHeader;}");
                builder.append("h2{font-size:1.30em; text-align: center; font-weight: normal; font-family: myHeader;}");
                builder.append("h3{font-size:1.15em; text-align: center; font-weight: normal; font-family: myHeader;}");
                builder.append("h4{font-size:1.00em; text-align: center; font-weight: normal; font-family: myHeader;}");
                builder.append("h5{font-size:0.80em; text-align: center; font-weight: normal; font-family: myHeader;}");
                builder.append("h6{font-size:0.60em; text-align: center; font-weight: normal; font-family: myHeader;}");

                builder.append("title,title>p,title>p>strong  {font-size:1.2em;  font-weight: normal; font-family: myHeader;}");
                builder.append(/*                 */ "subtitle{font-size:1.0em; font-weight: normal; font-family: myHeader;}");

            } else {
                builder.append("h1{font-size:1.50em; text-align: center; font-weight: bold; font-family: " + headersFont + ";}");
                builder.append("h2{font-size:1.30em; text-align: center; font-weight: bold; font-family: " + headersFont + ";}");
                builder.append("h3{font-size:1.15em; text-align: center; font-weight: bold; font-family: " + headersFont + ";}");
                builder.append("h4{font-size:1.00em; text-align: center; font-weight: bold; font-family: " + headersFont + ";}");
                builder.append("h5{font-size:0.80em; text-align: center; font-weight: bold; font-family: " + headersFont + ";}");
                builder.append("h6{font-size:0.60em; text-align: center; font-weight: bold; font-family: " + headersFont + ";}");

                builder.append("title   {font-size:1.2em; font-weight: bold; font-family: " + headersFont + ";}");
                builder.append("subtitle{    font-size:1.0em; font-weight: bold; font-family: " + headersFont + ";}");
            }

            // FONTS END

            // BODY BEGIN

            // BODY END

            builder.append("body,p{");

            if (isFontFileName(normalFont)) {
                builder.append("font-family: my !important;");
            } else {
                builder.append("font-family:" + normalFont + " !important; font-weight:normal;");
            }

            builder.append(String.format("text-indent:%s;", em(textIndent)));
            builder.append(String.format("text-align:%s !important;", getTextAlignConst(textAlign)));
            builder.append("}");

            if (!isFontFileName(boldFont)) {
                builder.append("b{font-family:" + boldFont + ";font-weight: bold;}");
            }

            if (!isFontFileName(italicFont)) {
                builder.append("i{font-family:" + italicFont + "; font-style: italic, oblique;}");
            }
            builder.append("body,p,b,i,em{font-size:medium !important;}");

        }

        String result = builder.toString();
        return result;

    }

    public String getFontWeight(String fontName) {
        return isFontFileName(fontName) ? "bold" : "bold";
    }

    public String getHeaderFontFamily(String fontName) {
        return isFontFileName(fontName) ? "myHeader" : fontName;
    }

    public static Typeface getTypeFaceForFont(String fontName) {
        try {
            if (FONT_NAMES.contains(fontName)) {
                return Typeface.DEFAULT;
            }
            return Typeface.createFromFile(BookCSS.get().getFontPath(fontName));
        } catch (Exception e) {
            return Typeface.DEFAULT;
        }
    }

    public static Typeface getNormalTypeFace() {
        return getTypeFaceForFont(BookCSS.get().normalFont);
    }

    public String getFontPath(String name) {
        return fontFolder + "/" + name;
    }

}
