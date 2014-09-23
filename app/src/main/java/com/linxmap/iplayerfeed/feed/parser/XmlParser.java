package com.linxmap.iplayerfeed.feed.parser;

import com.linxmap.iplayerfeed.list.FeedEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class XmlParser {
    private static final String LOG_TAG = "XmlParserManager";
    private static final boolean LOG_ENABLED = true;

    private final String NAMESPACE = null;
    private final String ENTRY_TAG = "entry";
    private final String TITLE_TAG = "title";
    private final String LINK_TAG = "link";
    private final String MEDIA_THUMBNAIL_TAG = "thumbnail";
    private final String URL_ATTRIBUTE_TAG = "url";


    public XmlParser() {}

    public ArrayList<FeedEntry> parse(String httpResponseBody) throws XmlPullParserException, IOException {
//        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            StringReader stringReader = new StringReader(httpResponseBody);
            parser.setInput(stringReader);
            return readFeed(parser);
//        } finally {
//            stringReader.  .close();
//        }
    }

    private ArrayList<FeedEntry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<FeedEntry> entries = new ArrayList<FeedEntry>();

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals(ENTRY_TAG)) {
                    entries.add(readEntry(parser));
                }
            }
            eventType = parser.next();
        }
        return entries;
    }

    private FeedEntry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, ENTRY_TAG);
        String title = "";
        String imageUrl = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(TITLE_TAG)) {
                title = readTitleTag(parser);
            } else if (name.equals(LINK_TAG)) {
                imageUrl = readImageUrl(parser);
            } else {
                skip(parser);
            }
        }
       /// int rating = feedManager.getRating(title);
        return new FeedEntry(title, imageUrl, -1);
    }

    private String readTitleTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TITLE_TAG);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, TITLE_TAG);
        return title;
    }

    private String readImageUrl(XmlPullParser parser) throws XmlPullParserException, IOException {
        String imageUrl = "";
        imageUrl = readAttribute(parser, MEDIA_THUMBNAIL_TAG, URL_ATTRIBUTE_TAG);
        return imageUrl;

    }

    private String readAttribute(XmlPullParser parser,String tag,String attributeTag) throws IOException, XmlPullParserException {
        String attribute = "";
        int depth = parser.getDepth();
        for (int x = 0; x < depth; x++) {
            if (parser.getEventType() == parser.START_TAG) {
                String name = parser.getName();
                if (name.equals(tag)) {
                    attribute = parser.getAttributeValue(null, attributeTag);
                }
            }
            parser.nextTag();
        }
        return attribute;
     }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
