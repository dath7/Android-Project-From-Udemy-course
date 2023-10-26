package com.example.top10dowloaded;

import android.nfc.Tag;
import android.util.Log;
import android.util.Xml;
import android.widget.Switch;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

// parse XML
public class ParseApplication {
    private static final String TAG = "ParseApplication";
    private ArrayList<FeedEntry> applications;

    public ParseApplication() {
        this.applications = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    public boolean parse(String xmlData) {
        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            // END_DOCUMENT: no more events available
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                       // Log.d(TAG, "parse: Starting tag for" + tagName);
                        if ("entry".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                      //  Log.d(TAG,"parse: Ending tag for " + tagName);
                        // if in tag entry
                        if (inEntry) {
                            // getName func can be NULL so can be thrown NullPointerException if call method on it: tagName.equalsIgnoreCase("entry")
                            if ("entry".equalsIgnoreCase(tagName)) {
                                // end of data of current record
                                applications.add(currentRecord);
                                inEntry = false;
                            }
                            else if ("name".equalsIgnoreCase(tagName)) {
                                currentRecord.setName(textValue);
                            }
                            else if ("artist".equalsIgnoreCase(tagName)) {
                                currentRecord.setArtist(textValue);
                            }
                            else if ("releaseDate".equalsIgnoreCase(tagName)) {
                                currentRecord.setReleaseDate(textValue);
                            }
                            else if ("summary".equalsIgnoreCase(tagName)) {
                                currentRecord.setSummary(textValue);
                            }
                            else if("image".equalsIgnoreCase(tagName)) {

                                currentRecord.setImageURL(textValue);
                            }
                        }
                        break;
                }
                // parse to next event, next() method return int value determines the current state
                eventType = xpp.next();

             /*   for(FeedEntry app:applications) {
                    Log.d(TAG,"************");
                    Log.d(TAG,app.toString());
                }*/
            }

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }

    ;
}
