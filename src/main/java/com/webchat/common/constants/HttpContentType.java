package com.webchat.common.constants;

import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

public class HttpContentType {

    public static final ContentType UTF8_TEXT_PLAIN = ContentType.TEXT_PLAIN.withCharset(Consts.UTF_8);

    public static final ContentType UTF8_APPLICATION_FORM_URLENCODED =
            ContentType.APPLICATION_FORM_URLENCODED.withCharset(Consts.UTF_8);

    public static final ContentType UTF8_APPLICATION_JSON = ContentType.APPLICATION_JSON.withCharset(Consts.UTF_8);

    public static final ContentType UTF8_APPLICATION_XML = ContentType.APPLICATION_XML.withCharset(Consts.UTF_8);

    public static final ContentType UTF8_MULTIPART_FORM_DATA =
            ContentType.MULTIPART_FORM_DATA.withCharset(Consts.UTF_8);

    public static final ContentType UTF8_TEXT_HTML = ContentType.TEXT_HTML.withCharset(Consts.UTF_8);

    public static final ContentType UTF8_TEXT_XML = ContentType.TEXT_XML.withCharset(Consts.UTF_8);

    public static boolean equals(ContentType type1, ContentType type2) {
        return equalsMimeType(type1, type2) && type1.getCharset().equals(type2.getCharset());
    }

    public static boolean equalsMimeType(ContentType type1, ContentType type2) {
        if (null == type1 || null == type2) {
            return false;
        }
        return type1.getMimeType().equals(type2.getMimeType());
    }

    public static List<MediaType> getSupportedMediaTypes() {
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        return supportedMediaTypes;
    }
}
