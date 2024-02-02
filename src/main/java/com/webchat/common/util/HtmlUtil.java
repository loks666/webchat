package com.webchat.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {

    private static final String REGXP_FOR_HTML = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签
    private static final String PICTURE = "[图片]";

    /**
     * @param fString
     *
     * @return
     */
    public static String htmlEncode(String fString) {
        fString = fString.replaceAll(" <", "&lt;");
        fString = fString.replaceAll(">", "&gt;");
        fString = fString.replaceAll(new String(new char[] {32}), "&nbsp;");
        fString = fString.replaceAll(new String(new char[] {9}), "&nbsp;");
        fString = fString.replaceAll(new String(new char[] {34}), "&quot;");
        fString = fString.replaceAll(new String(new char[] {39}), "&#39;");
        fString = fString.replaceAll(new String(new char[] {13}), "");
        fString = fString.replaceAll(new String(new char[] {10, 10}), " </p> <p>");
        fString = fString.replaceAll(new String(new char[] {10}), " <br>");
        return fString;
    }

    /**
     * xss escape
     */
    public static String xssEscape(String input) {
        return input == null ? null : input.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }

    /**
     * 除指定标签之外的html标签编码
     *
     * @param str
     * @param tag
     *
     * @return
     */
    public static String xssEscapeExceptTag(String str, String tag) {
        String replaceTag = "@" + tag + "@";
        str = str.replaceAll("<" + tag, replaceTag);
        str = xssEscape(str);
        str = str.replaceAll(replaceTag, "<" + tag);

        return str;
    }

    /**
     * 过滤一下字符串，连同前后< xxx >yyy< / xxx >全部消除。
     * 不区分大小写、空格可识别
     * <br>"function", "window\\.", "javascript:", "script",
     * <br>"js:", "about:", "file:", "document\\.", "vbs:", "frame",
     * <br>"cookie", "onclick", "onfinish", "onmouse", "onexit=",
     * <br>"onerror", "onclick", "onkey", "onload", "onfocus", "onblur"
     *
     * @param htmlStr
     *
     * @return
     */
    public static String filterSafe(String htmlStr) {
        Pattern p = null; // 正则表达式
        Matcher m = null; // 操作的字符串
        StringBuffer tmp = null;
        String str = "";
        boolean isHave = false;
        String[] rstr = {"meta", "script", "object", "embed"};
        if (htmlStr == null || !(htmlStr.length() > 0)) {
            return "";
        }
        str = htmlStr.toLowerCase();
        for (int i = 0; i < rstr.length; i++) {
            p = Pattern.compile("<" + rstr[i] + "(.[^>])*>");
            m = p.matcher(str);
            tmp = new StringBuffer();
            if (m.find()) {
                m.appendReplacement(tmp, "<" + rstr[i] + ">");
                while (m.find()) {
                    m.appendReplacement(tmp, "<" + rstr[i] + ">");
                }
                isHave = true;
            }

            m.appendTail(tmp);
            str = tmp.toString();

            p = Pattern.compile("</" + rstr[i] + "(.[^>])*>");
            m = p.matcher(str);
            tmp = new StringBuffer();
            if (m.find()) {
                m.appendReplacement(tmp, "</" + rstr[i] + ">");
                while (m.find()) {
                    m.appendReplacement(tmp, "</" + rstr[i] + ">");
                }
                isHave = true;
            }
            m.appendTail(tmp);
            str = tmp.toString();

        }

        String[] rstr1 = {"function", "window\\.", "javascript:", "script",
                "js:", "about:", "file:", "document\\.", "vbs:", "frame",
                "cookie", "onclick", "onfinish", "onmouse", "onexit=",
                "onerror", "onclick", "onkey", "onload", "onfocus", "onblur"};

        for (int i = 0; i < rstr1.length; i++) {
            p = Pattern.compile("<([^<>])*" + rstr1[i] + "([^<>])*>([^<>])*</([^<>])*>");

            m = p.matcher(str);
            tmp = new StringBuffer();
            if (m.find()) {
                m.appendReplacement(tmp, "");
                while (m.find()) {
                    m.appendReplacement(tmp, "");
                }
                isHave = true;
            }
            m.appendTail(tmp);
            str = tmp.toString();
        }

        if (isHave) {
            htmlStr = str;
        }

        htmlStr = htmlStr.replaceAll("%3C", "<");
        htmlStr = htmlStr.replaceAll("%3E", ">");
        htmlStr = htmlStr.replaceAll("%2F", "");
        htmlStr = htmlStr.replaceAll("&#", "&ltb>&#</b>");
        return htmlStr;
    }

    /**
     * @param input
     *
     * @return
     */
    public static String filter(String input) {
        if (!hasSpecialChars(input)) {
            return input;
        }
        StringBuffer filtered = new StringBuffer(input.length());
        char c;
        for (int i = 0; i <= input.length() - 1; i++) {
            c = input.charAt(i);
            switch (c) {
                case '<':
                    filtered.append("&lt;");
                    break;
                case '>':
                    filtered.append("&gt;");
                    break;
                case '"':
                    filtered.append("&uot;");
                    break;
                case '&':
                    filtered.append("&amp;");
                    break;
                default:
                    filtered.append(c);
                    break;
            }
        }
        return (filtered.toString());
    }

    public static boolean hasSpecialChars(String input) {
        boolean flag = false;
        if ((input != null) && (input.length() > 0)) {
            char c;
            for (int i = 0; i <= input.length() - 1; i++) {
                c = input.charAt(i);
                switch (c) {
                    case '>':
                        flag = true;
                        break;
                    case '<':
                        flag = true;
                        break;
                    case '"':
                        flag = true;
                        break;
                    case '&':
                        flag = true;
                        break;
                    default:
                        flag = false;
                        break;

                }
            }
        }
        return flag;
    }

    /**
     * 基本功能：过滤所有以"<"开头以">"结尾的标签
     * <p>
     *
     * @param str
     *
     * @return String
     */
    public static String filterHtml(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        Pattern pattern = Pattern.compile(REGXP_FOR_HTML);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 过滤除指定tag之外的html标签
     *
     * @param str
     * @param tag
     *
     * @return
     */
    public static String filterHtmlExceptTag(String str, String tag) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        String replaceTag = "@" + tag + "@";
        str = str.replaceAll("<" + tag, replaceTag);
        str = filterHtml(str);
        str = str.replaceAll(replaceTag, "<" + tag);

        return str;
    }

    /***
     * 删除指定标签及标签的内容
     * 如：<tag>value</tag>， 连同value内容也会清理掉
     * @param content
     * @param tag
     * @return
     */
    public static String deleteHtmlTagAndTagValue(String content, String tag) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        Pattern imgPattern = Pattern.compile("<" + tag + ".*?>.*</" + tag + ">");
        Matcher tagMatcher = imgPattern.matcher(content);
        while (tagMatcher.find()) {
            String matchGroup = tagMatcher.group();
            content = content.replace(matchGroup, "");
        }
        return content;
    }

    /***
     * 清理动态、评论内的@结构
     * 如：<a>@value</a>， 连同value内容也会清理掉
     * @param content
     * @return
     */
    public static String deleteAtTagAndValue(String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        Pattern imgPattern = Pattern.compile("<a.*?>@.*</a>");
        Matcher tagMatcher = imgPattern.matcher(content);
        while (tagMatcher.find()) {
            String matchGroup = tagMatcher.group();
            content = content.replace(matchGroup, "");
        }
        return content;
    }

    /**
     * 基本功能：过滤指定标签
     * <p>
     *
     * @param str
     * @param tag 指定标签
     *
     * @return String
     */
    public static String fiterHtmlTag(String str, String tag) {
        String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
        Pattern pattern = Pattern.compile(regxp);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 基本功能：替换指定的标签
     *
     * @param str
     * @param beforeTag 要替换的标签
     * @param tagAttrib 要替换的标签属性值
     * @param startTag  新标签开始标记
     * @param endTag    新标签结束标记
     *
     * @return String
     *
     * @如：替换img标签的src属性值为[img]属性值[/img]
     */
    public static String replaceHtmlTag(String str, String beforeTag,
                                        String tagAttrib, String startTag, String endTag) {
        String regxpForTag = "<\\s*" + beforeTag + "\\s+([^>]*)\\s*>";
        String regxpForTagAttrib = tagAttrib + "=\"([^\"]+)\"";
        Pattern patternForTag = Pattern.compile(regxpForTag);
        Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);
        Matcher matcherForTag = patternForTag.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result = matcherForTag.find();
        while (result) {
            StringBuffer sbreplace = new StringBuffer();
            Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));
            if (matcherForAttrib.find()) {
                matcherForAttrib.appendReplacement(sbreplace, startTag + matcherForAttrib.group(1) + endTag);
            }
            matcherForTag.appendReplacement(sb, sbreplace.toString());
            result = matcherForTag.find();
        }
        matcherForTag.appendTail(sb);
        return sb.toString();
    }

    /**
     * html标签替换为指定字符
     *
     * @param str
     * @param tag
     * @param text
     *
     * @return
     */
    public static String replaceHtmlTagOfText(String str, String tag, String text) {
        String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
        Pattern pattern = Pattern.compile(regxp);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, text);
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取指定HTML标签的指定属性的值
     *
     * @param source  要匹配的源文本
     * @param element 标签名称
     * @param attr    标签的属性名称
     *
     * @return 属性值列表
     */
    public static List<String> match(String source, String element, String attr) {
        List<String> result = new ArrayList<>();
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?\\s.*?>";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            String r = m.group(1);
            result.add(r);
        }
        return result;
    }

    /**
     * @param html
     *
     * @return
     */
    public static List<String> getImgHTML(String html) {
        List<String> resultList = new ArrayList<>();
        Pattern p = Pattern.compile("<img ([^>]*)"); // <img开头>结尾
        Matcher m = p.matcher(html); // 开始编译
        while (m.find()) {
            resultList.add("<img " + m.group(1) + ">"); // 获取匹配的部分
        }

        return resultList;
    }

    public static List<String> getImgSrc(String htmlStr) {
        String img = "";
        Pattern pImage;
        Matcher mImage;
        List<String> pics = new ArrayList<>();
        String regExImg = "<img.*src=(.*?)[^>]*?>"; // 图片链接地址
        pImage = Pattern.compile(regExImg, Pattern.CASE_INSENSITIVE);
        mImage = pImage.matcher(htmlStr);
        while (mImage.find()) {
            img = mImage.group();
            Matcher m = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); // 匹配src
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

    public static List<String> getImgAlt(String htmlStr) {
        String img = "";
        Pattern pImage;
        Matcher mImage;
        List<String> alts = new ArrayList<>();

        String regExImg = "<img.*src=(.*?)[^>]*?>"; // 图片链接地址
        pImage = Pattern.compile(regExImg, Pattern.CASE_INSENSITIVE);
        mImage = pImage.matcher(htmlStr);
        while (mImage.find()) {
            img = mImage.group();
            Matcher m = Pattern.compile("alt=\"?(.*?)(\"|>|\\s+)").matcher(img); // 匹配src
            while (m.find()) {
                alts.add(m.group(1));
            }
        }
        return alts;
    }

    /**
     * 基本功能：过滤所有以"<"开头以">"结尾的标签,但是替换为空格
     * <p>
     *
     * @param str
     *
     * @return String
     */
    public static String filterHtmlWithSapce(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        Pattern pattern = Pattern.compile(REGXP_FOR_HTML);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, " ");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取纯文本内容
     *
     * @param content
     *
     * @return
     */
    public static String getTxtContent(String content) {
        String txtcontent = "";
        if (StringUtils.isNotBlank(content)) {
            txtcontent = content.replaceAll("</?[^>]+>", ""); // 剔出<html>的标签
            txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");
            txtcontent = txtcontent.replaceAll("\\s*|\t|\r|\n", "");
        }
        return txtcontent;
    }

    /**
     * 清楚文本中的HTML等标签
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {
        if (StringUtils.isBlank(htmlStr)) {
            return null;
        }
        // 定义script的正则表达式
        String regExScript = "<script[^>]*?>[\\s\\S]*?<\\/script>";
        // 定义style的正则表达式
        String regExStyle = "<style[^>]*?>[\\s\\S]*?<\\/style>";
        // 定义HTML标签的正则表达式
        String regExHtml = "<[^>]+>";
        // 过滤script标签
        Pattern pScript = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
        Matcher mScript = pScript.matcher(htmlStr);
        htmlStr = mScript.replaceAll("");
        // 过滤style标签
        Pattern pStyle = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
        Matcher mStyle = pStyle.matcher(htmlStr);
        htmlStr = mStyle.replaceAll("");
        // 过滤html标签
        Pattern pHtml = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
        Matcher mHtml = pHtml.matcher(htmlStr);
        htmlStr = mHtml.replaceAll("");
        return htmlStr.trim();
    }

    public static List<String> getATagLink(String html) {
        if (StringUtils.isBlank(html)) {
            return null;
        }
        List<String> aLinkList = new ArrayList<>();
        Pattern aLinkPattern;
        Matcher aLinkMatcher;
        // A标签
        String regATag = "<a.*href=(.*?)[^>]*?>";
        aLinkPattern = Pattern.compile(regATag, Pattern.CASE_INSENSITIVE);
        aLinkMatcher = aLinkPattern.matcher(html);
        while (aLinkMatcher.find()) {
            // 匹配A标签href
            Matcher m = Pattern.compile("href=\"?(.*?)(\"|>|\\s+)").matcher(aLinkMatcher.group());
            while (m.find()) {
                aLinkList.add(m.group(1));
            }
        }
        return aLinkList;
    }

    /**
     * xss escape back
     */
    public static String xssEscapeBack(String input) {
        return input == null ? null : input.replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">");
    }
}
