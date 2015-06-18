/*
* Copyright 2015 Gemma Samantha Lara Savill
* FiPL My First Programming Language
* Proyecto de Fin de Grado en Ingeniería en Tecnologías de la Información
* en la Universidad Nacional de Educación a Distancia UNED España
*
* Final project for my Bachelor of Science in Information Technology Engineering
* At the Spanish National Distance University UNED
* Project manager Dr. Anselmo Peñas Padilla

* Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
* You may not use this work except in compliance with the Licence.
* You may obtain a copy of the Licence at:
*
* http://ec.europa.eu/idabc/eupl
*
* Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the Licence for the specific language governing permissions and limitations under the Licence.
*/
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
