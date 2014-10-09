package jobtest.com.sampleapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.app.ActionBar.LayoutParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jobtest.com.sampleapp.R;
import jobtest.com.sampleapp.logic.MediaItem;


public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity"; // default TAG for log messages
    private LinearLayout defaultLayout; // default layout for the main activity
    private ArrayList<MediaItem> mediaItem; // ArrayList used to store the XML media items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defaultLayout = (LinearLayout) findViewById(R.id.DefaultLayout);


        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream is = getApplicationContext().getAssets().open("sample2.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            parseXML(parser);

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        ArrayList<MediaItem> mediaItems = null;
        int eventType = parser.getEventType();
        MediaItem currentMediaItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    mediaItems = new ArrayList<MediaItem>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("MediaItem")){
                        currentMediaItem = new MediaItem();
                    } else if (currentMediaItem != null){
                        if (name.equals("text")){
                            currentMediaItem.setType("text");
                            currentMediaItem.setText(parser.nextText());
                        } else if (name.equals("location")){
                            currentMediaItem.setLocation(parser.nextText());
                        } else if (name.equals("radiobutton")){
                            currentMediaItem.setType("radiobutton");
                            currentMediaItem.setText(parser.nextText());
                        } else if (name.equals("checkbox")){
                            currentMediaItem.setType("checkbox");
                            currentMediaItem.setText(parser.nextText());
                        } else if (name.equals("value")){
                            currentMediaItem.setValue(Boolean.valueOf(parser.nextText()));
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("mediaitem") && currentMediaItem != null){
                        mediaItems.add(currentMediaItem);
                    }
            }
            eventType = parser.next();
        }

        printMediaItems(mediaItems);
    }

    private void printMediaItems(ArrayList<MediaItem> mediaItems)
    {
        Iterator<MediaItem> it = mediaItems.iterator();
        while(it.hasNext())
        {
            MediaItem currMediaItem  = it.next();

            if (currMediaItem.getType().equals("text")) {
                TextView txtDisplay = new TextView(MainActivity.this);
                txtDisplay.setText(currMediaItem.getText());
                List<String> location = Arrays.asList(currMediaItem.getLocation().split(","));


                txtDisplay.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                defaultLayout.addView(txtDisplay);
            } else if (currMediaItem.getType().equals("radiobutton")) {
                RadioButton rbDisplay = new RadioButton(MainActivity.this);
                rbDisplay.setText(currMediaItem.getText());
                rbDisplay.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                defaultLayout.addView(rbDisplay);
            } else if (currMediaItem.getType().equals("checkbox")) {
                CheckBox cbDisplay = new CheckBox(MainActivity.this);
                cbDisplay.setText(currMediaItem.getText());
                cbDisplay.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                defaultLayout.addView(cbDisplay);
            }
        }

        Button btnSubmit = new Button(MainActivity.this);
        btnSubmit.setText("Submit Value");
        btnSubmit.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        defaultLayout.addView(btnSubmit);

        btnSubmit.setOnClickListener(this);
        mediaItem = new ArrayList<MediaItem>(mediaItems);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View arg0) {
        // Method designed for the button click listener on this activity
        Iterator<MediaItem> it = mediaItem.iterator();
        String content = "";
        while(it.hasNext())
        {

            MediaItem currMediaItem  = it.next();

            if (currMediaItem.getType().equals("radiobutton") || currMediaItem.getType().equals("checkbox")) {
                if (currMediaItem.getValue()) {
                    content = content + currMediaItem.getText() + "\n";
                }
            }
        }
//		Log.e(TAG, "content: " + content);
//		Toast.makeText(getApplicationContext(),
//                content, Toast.LENGTH_LONG).show();

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

        dlgAlert.setMessage(content);
        dlgAlert.setTitle("Selected Values");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

    }
}
