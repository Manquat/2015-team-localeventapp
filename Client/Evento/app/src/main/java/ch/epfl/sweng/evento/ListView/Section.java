package ch.epfl.sweng.evento.ListView;

/**
 * Created by thomas on 03/12/15.
 */
public class Section implements Item {
    private final String mTitle;

    public Section(String title){
        mTitle = title;
    }

    public String getTitle(){
        return mTitle;
    }
    @Override
    public boolean isSection(){
        return true;
    }
}
