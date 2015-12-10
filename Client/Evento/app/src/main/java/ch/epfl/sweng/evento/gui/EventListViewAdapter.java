package ch.epfl.sweng.evento.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.gui.event_activity.EventActivity;

/**
 * List view adapter that can be used to display the list of event given in the constructor
 */
public class EventListViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {


    Context mContext;
    List<Event> mEvents;
    Activity mActivity;


    /**
     * Constructor of the class
     *
     * @param context        the context where the list adapter is used
     * @param events         the events that compose the list
     * @param parentActivity the activity where the list adapter is used
     */
    public EventListViewAdapter(Context context, List<Event> events, Activity parentActivity) {
        super();

        mContext = context;
        mEvents = events;
        mActivity = parentActivity;
    }


    @Override
    public int getCount() {
        if (mEvents != null) {
            return mEvents.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mEvents.get(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;

        if (rootView == null) {
            LayoutInflater inflater = (LayoutInflater.from(mContext));
            rootView = inflater.inflate(R.layout.list_event, parent, false);
        }

        if (mEvents != null) {
            // setting the title of the event as the text in the list
            TextView titleText = (TextView) rootView.findViewById(R.id.list_event_title);
            titleText.setText(mEvents.get(position).getTitle());

            // setting a default image as the image in the list
            ImageView imageView = (ImageView) rootView.findViewById(R.id.list_event_image);

            int dimension = mActivity.getResources().getDimensionPixelSize(R.dimen.list_event_height);
            Bitmap bitmap = ThumbnailUtils.extractThumbnail(mEvents.get(position).getPicture(),
                    dimension, dimension);

            imageView.setImageBitmap(bitmap);
        }

        return rootView;
    }


    /**
     * Setter for the events
     *
     * @param events the events display in the list adapter
     */
    public void setEvents(List<Event> events) {
        mEvents = events;
        notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, EventActivity.class);
        intent.putExtra(EventActivity.CURRENT_EVENT_KEY, ((Long) id).intValue());
        mActivity.startActivity(intent);
    }
}
