package ch.epfl.sweng.evento.ListView;

/**
 * Created by thomas on 03/12/15.
 */
public class Entry implements Item {
    private final String mTitle;
    private final String mEntry;

    public Entry(String title, String entry){
        mTitle = title;
        mEntry = entry;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getEntry(){
        return mEntry;
    }

    @Override
    public boolean isSection(){
        return false;
    }

}
