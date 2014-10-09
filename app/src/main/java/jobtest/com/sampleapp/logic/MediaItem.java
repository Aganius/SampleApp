package jobtest.com.sampleapp.logic;

/**
 * Created by Aganius on 09/10/2014.
 */

public class MediaItem {
    private String type;
    private String text;
    private Boolean value;
    private String location;

    public MediaItem() {
        type = new String();
        text = new String();
        value = null;
        location = new String();
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Boolean getValue() {
        return value;
    }
    public void setValue(Boolean value) {
        this.value = value;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }



}