package com.hotusm.fastcrawl.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 */
public class TestAbstractParser extends AbstractParser{

    @Override
    public boolean isMatch(String html) {

       // return true;
        Document document= Jsoup.parse(html);
        return document.select(".vcard-names > .vcard-fullname")!=null;
    }
}
