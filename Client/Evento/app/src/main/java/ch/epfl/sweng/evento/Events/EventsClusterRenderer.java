package ch.epfl.sweng.evento.Events;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.evento.R;

/**
 * Created by Gautier on 25/10/2015.
 */
public class EventsClusterRenderer extends DefaultClusterRenderer<Event>
{
    private final int MAX_NUMBER_OF_IMAGE = 4;

    // Icon generator used to create the custom icon for the events and cluster of events on the map
    private final IconGenerator mEventIconGenerator;
    private final IconGenerator mClusterEventIconGenerator;

    // View used to create the icon of the events and cluster of events on the map
    private final ImageView     mIconEventView;
    private final ImageView     mIconClusterView;

    // context used for all the creation of content
    private final Context       mContext;

    // dimension of the icon in the map
    private final int           mDimension;

    public EventsClusterRenderer(Context context, GoogleMap map, ClusterManager<Event> clusterManager,
                                 ViewGroup container)
    {
        super(context, map, clusterManager);

        mContext = context;
        mDimension = (int) mContext.getResources().getDimension(R.dimen.icon_map_size);

        // initialize the icon generator with the context
        mEventIconGenerator = new IconGenerator(mContext);
        mClusterEventIconGenerator = new IconGenerator(mContext);

        // using the layout to create the icon of the clustered events
        View multiEvents = View.inflate(context, R.layout.icon_cluster, container);
        mClusterEventIconGenerator.setContentView(multiEvents);

        // keep a reference to the image view inside the layout
        mIconClusterView = (ImageView) multiEvents.findViewById(R.id.image);

        // creating an image view and using it for the icon of the event
        mIconEventView = new ImageView(mContext);
        mIconEventView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
        mEventIconGenerator.setContentView(mIconEventView);
    }

    /**
     * Function call just before the icon is rendered, modify the view using the property
     * of the events
     * @param event the event that would be rendered
     * @param markerOptions the options of the marker that would be rendered
     */
    @Override
    protected void onBeforeClusterItemRendered(Event event, MarkerOptions markerOptions)
    {
        // Draw a single event icon
        mIconEventView.setImageResource(R.drawable.football); // TODO replace the mock image by the real one of the event

        Bitmap icon = mEventIconGenerator.makeIcon();

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(event.Title());
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Event> cluster, MarkerOptions markerOptions)
    {
        // Draw the clustered event icon
        // at most MAX_NUMBER_OF_IMAGE image
        List<Drawable> eventsImage = new ArrayList<Drawable>(Math.min(MAX_NUMBER_OF_IMAGE, cluster.getSize()));

        for (Event event : cluster.getItems())
        {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.football); // TODO replace the mock image by the real one of the event
            drawable.setBounds(0, 0, mDimension, mDimension);
            eventsImage.add(drawable);
        }

        MultiDrawable multiDrawable = new MultiDrawable(eventsImage);
        multiDrawable.setBounds(0, 0, mDimension, mDimension);

        mIconClusterView.setImageDrawable(multiDrawable);
        Bitmap icon = mClusterEventIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster)
    {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
}
