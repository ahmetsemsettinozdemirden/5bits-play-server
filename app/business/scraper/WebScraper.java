package business.scraper;

import db.models.Events;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebScraper {

    public List<Events> scrapeTopic() throws IOException {
        Document document = Jsoup.connect("http://ceng.iyte.edu.tr/news-announcements/").get();
        Elements pngs = document.select("img[src$=.png]");

        List<Events> eventsList = new ArrayList<>();
        for (Element png: pngs) {
            if (png.attr("width").contentEquals("350") && png.attr("height").contentEquals("240")) {
                StringBuilder stringBuilder = new StringBuilder();
                String link = png.parentNode().attr("href");
                Document doc = Jsoup.connect(link).get();
                Elements paragraphs = doc.select("p");
                for (Element p: paragraphs) {
                    stringBuilder.append(p);
                }
                Events event = new Events(doc.title(), stringBuilder.toString());
                eventsList.add(event);
            }
        }

        return eventsList;
    }

}