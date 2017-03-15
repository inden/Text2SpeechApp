package com.in.den.android.text2speechapp;

/**
 * Created by harumi on 24/02/2017.
 */

import android.speech.tts.TextToSpeech;

import com.in.den.android.text2speechapp.data.Recepie;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by harumi on 24/02/2017.
 */

public class RecetteParser {
    private static String TAG = "RecetteParser";
    private XmlPullParser xpp = null;
    private static HashMap<String, String> tagMap = new HashMap<>();

    static {
        tagMap.put("titre", "H1");
        tagMap.put("item", "li");
        tagMap.put("etape", "li");
    }

    private static HashMap<String, String> specialtag = new HashMap<>();

    static {
        specialtag.put("preparation", "H2-OL");
        specialtag.put("ingredients", "H2-UL");
    }


    private static String css = "" +
            "<head>" +
            "<style>" +

         "body {\n" +
            " background-color:#FFF9C4;\n" +
            "border-style: dashed;\n" +
            "margin: 6px" +
            "}\n" +
            "h1 {\n" +
            "font-family: verdana;\n" +
            "text-align: center;\n" +
            "margin-top: 32px;\n" +
            "font_size: 32px;\n" +
            "}\n" +
            "h2 {\n" +
            "font-family: verdana;\n" +
            "padding-left:26px;\n" +
            "}\n" +
            "ul {    font-family: verdana;\n" +
            "    font-size: 16px;\n" +
            "}ol {    font-family: verdana;\n" +
            "    font-size: 16px;\n" +
            "}" +

            "</style>" +
            "</head>";


    public void initParser() throws XmlPullParserException {

        xpp = XmlPullParserFactory.newInstance().newPullParser();

    }

    public void readRecette(String html, final TextToSpeech textToSpeech) throws XmlPullParserException, IOException {

        if (xpp == null) initParser();
        xpp.setInput(new StringReader(html));

        int eventType = xpp.getEventType();

        boolean body = false;
        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_DOCUMENT) {
            } else if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equalsIgnoreCase("body")) {
                    body = true;
                }

            } else if (eventType == XmlPullParser.END_TAG) {
                if (body) {
                    String tagname = xpp.getName();
                    int time = 1000;
                    if (tagname.equalsIgnoreCase("H1")) time = 3000;
                    else if (tagname.equalsIgnoreCase("H2")) time = 2000;

                    textToSpeech.playSilence(time, TextToSpeech.QUEUE_ADD, null);
                }
            } else if (eventType == XmlPullParser.TEXT) {
                if (body) {
                    final String s = xpp.getText();
                    if (!s.isEmpty()) {
                        textToSpeech.speak(s, TextToSpeech.QUEUE_ADD, null);
                    }
                }
            }

            eventType = xpp.next();
        }

    }

    public void simpleSpeak(TextToSpeech textToSpeech) {
        textToSpeech.speak("bonjour", TextToSpeech.QUEUE_ADD, null);
    }

    public String getHtmlRecette(InputStreamReader input) throws XmlPullParserException, IOException {
        return getHtmlRecette(input, 1);
    }

    public String getHtmlRecette(InputStreamReader input, int order) throws XmlPullParserException, IOException {
        StringBuffer sb = new StringBuffer();

        if (xpp == null) initParser();

        xpp.setInput(input);
        int eventType = xpp.getEventType();

        int count = 0;
        boolean bRecette = false;
        boolean bstar = false;

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_DOCUMENT) {
                //System.out.println("Start document");
                sb.append("<html>");
                sb.append(css);
                sb.append("<body>");

            } /*else if(eventType == XmlPullParser.END_DOCUMENT) {
                //System.out.println("End document");
                sb.append("</html>");

            } */ else if (eventType == XmlPullParser.START_TAG) {

                String tag = xpp.getName();
                if (tag.equals("recette")) {
                    count++;

                    if (count == order) {
                        bRecette = true;
                    }
                }

                if (bRecette) {

                    if(tag.equals("star")) {
                        bstar = true;
                    }

                    String htmltag = tagMap.get(tag);

                    if (htmltag != null) {
                        sb.append("<" + htmltag + ">");
                    }

                    String sptag = specialtag.get(tag);
                    if (sptag != null) {
                        String[] taglist = sptag.split("-");
                        sb.append("<" + taglist[0] + ">" + tag2String(tag) +
                                "</" + taglist[0] + ">");
                        sb.append("<" + taglist[1] + ">");
                    }
                }

                //System.out.println(xpp.getAttributeValue(null,"silence"));

            } else if (eventType == XmlPullParser.END_TAG) {
                //System.out.println("End tag "+xpp.getName());

                if (bRecette) {
                    String tag = xpp.getName();
                    if (tag.equals("recette")) {
                        break;
                    }

                    if(tag.equals("star")) {
                        bstar = false;
                    }

                    String htmltag = tagMap.get(tag);

                    String sptag = specialtag.get(tag);
                    if (sptag != null) {
                        String[] taglist = sptag.split("-");
                        sb.append("</" + taglist[1] + ">");
                    }

                    if (htmltag != null) {
                        sb.append("</" + htmltag + ">");
                    }
                }
            } else if (eventType == XmlPullParser.TEXT) {
                //System.out.println("Text "+xpp.getText());
                if (bRecette && !bstar) {
                    sb.append(xpp.getText());
                }
            }

            eventType = xpp.next();
        }

        sb.append("</body></html>");

        return sb.toString();
    }

    private String tag2String(String tag) {
        String s = "";
        if ("ingredients".equals(tag)) {
            String personnes = xpp.getAttributeValue(null, "personnes");

            s = "Ingrédients ";
            if (personnes != null && !personnes.isEmpty()) {
                s += "pour " + personnes + " personnes";
            }
        } else if ("preparation".equals(tag)) {
            s = "Préparation";
        }

        return s;
    }

    public List<Recepie> getRecipies(InputStreamReader input, String filename) throws XmlPullParserException, IOException {
        ArrayList<Recepie> list = new ArrayList<Recepie>();

        if (xpp == null) initParser();

        xpp.setInput(input);
        int eventType = xpp.getEventType();

        Recepie recepie = null;
        String currentTag = "";
        StringBuffer sb = new StringBuffer();
        int order = 0;

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_DOCUMENT) {


            } /*else if(eventType == XmlPullParser.END_DOCUMENT) {
                //System.out.println("End document");
                sb.append("</html>");

            } */ else if (eventType == XmlPullParser.START_TAG) {

                String tag = xpp.getName();
                if (tag.equals("recette")) {
                    order++;
                    recepie = new Recepie();
                    recepie.filename = filename;
                    recepie.order = order;
                }

                currentTag = tag;

                //System.out.println(xpp.getAttributeValue(null,"silence"));

            } else if (eventType == XmlPullParser.END_TAG) {
                //System.out.println("End tag "+xpp.getName());
                String tag = xpp.getName();
                if (tag.equals("recette")) {
                    list.add(recepie);
                }

                if(currentTag.equals("titre")) {
                    recepie.name = sb.toString().trim();

                    sb = new StringBuffer();
                }
                else if (currentTag.equals("star")) {
                    String star =  sb.toString().trim();
                    if(star.isEmpty()) {
                        star = "3.0";
                    }
                    recepie.star = Float.valueOf(star);
                    sb = new StringBuffer();
                }

            } else if (eventType == XmlPullParser.TEXT) {
                //System.out.println("Text "+xpp.getText());
               if(currentTag.equals("titre") || currentTag.equals("star")) {
                  sb.append(xpp.getText());
               }

            }

            eventType = xpp.next();
        }



        return list;
    }


}
