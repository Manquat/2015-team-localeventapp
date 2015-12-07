package ch.epfl.sweng.evento.list_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.evento.R;

/**
 * Created by thomas on 03/12/15.
 * inspired from http://sunil-android.blogspot.ch
 */
public class ListEntryAdapter extends ArrayAdapter<ListEntryAdapter.Item> {
    private ArrayList<Item> mListItems;
    private LayoutInflater mInflater;

    public ListEntryAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.mListItems = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;

        Item item = mListItems.get(position);
        if (item != null) {
            if (item.isSection()) {
                Section section = (Section) item;
                mView = mInflater.inflate(R.layout.list_item_manage_section, null);

                mView.setOnClickListener(null);
                mView.setOnLongClickListener(null);
                mView.setLongClickable(false);

                TextView sectionView = (TextView) mView.findViewById(R.id.list_item_section_text);
                sectionView.setText(section.getTitle());

            } else {
                Entry entry = (Entry) item;
                mView = mInflater.inflate(R.layout.list_item_manage_entry, null);
                TextView title = (TextView) mView.findViewById(R.id.list_item_entry_title);
                TextView subtitle = (TextView) mView.findViewById(R.id.list_item_entry_summary);
                title.setText(entry.getTitle());
                subtitle.setText(entry.getEntry());
            }
        }
        return mView;
    }

    public static class Entry implements Item {
        private final String mTitle;
        private final String mEntry;

        public Entry(String title, String entry) {
            mTitle = title;
            mEntry = entry;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getEntry() {
            return mEntry;
        }

        @Override
        public boolean isSection() {
            return false;
        }

    }

    public static class Section implements Item {
        private final String mTitle;

        public Section(String title) {
            mTitle = title;
        }

        public String getTitle() {
            return mTitle;
        }

        @Override
        public boolean isSection() {
            return true;
        }
    }

    public interface Item {
        boolean isSection();
    }

}
