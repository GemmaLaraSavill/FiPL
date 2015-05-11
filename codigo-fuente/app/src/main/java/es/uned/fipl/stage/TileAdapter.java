package es.uned.fipl.stage;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Gemma Lara Savill on 30/04/2015.
 * An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
 * The Adapter provides access to the data items.
 * The Adapter is also responsible for making a View for each item in the data set.
 */
public class TileAdapter extends BaseAdapter {

    private int[] tileColors;
    private Context context;

    public TileAdapter(Context context, int[] tileColors) {
        this.context = context;
        this.tileColors = tileColors;
    }

    @Override
    public int getCount() {
        return tileColors.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup grid) {

        ImageView tile;
        int tileWidth = Stage.getTileWidth(context);

        if (view == null) {
            tile = new ImageView(context);
            tile.setLayoutParams( new GridView.LayoutParams(tileWidth, tileWidth) );
            tile.setPadding(1, 1, 1, 1);
            tile.setImageDrawable(new ColorDrawable(tileColors[position]));
        } else {
            tile = (ImageView) view;
        }

        return tile;
    }
}
