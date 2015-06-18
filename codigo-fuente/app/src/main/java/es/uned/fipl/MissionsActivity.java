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
package es.uned.fipl;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gemma Lara Savill on 07/03/2015
 * Ventana que muestra información sobre las misiones
 */
public class MissionsActivity extends Activity {

    private int selectedMission = 0;
    private ArrayList<String> missionTitles; // titulos de las misiones
    private ArrayList<String> missionSubTitles; // subtítulos de las misiones
    private ArrayList<String> missionTexts; // textos de las misiones

    private TextView missionTitle; // donde mostraremos el titulo de la mision
    private TextView missionSubtitle; // donde mostraremos el subtitulo de la mision
    private TextView missionText; // // donde mostraremos el texto de la mision


    /**
     * Al crearse la ventanda
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);

        // Configuro barra de menú
        Context context = this;
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // flechita volver
        actionBar.setSubtitle(R.string.action_missionScreen);

        // lista de indice lateral
        final ListView missionList = (ListView) findViewById(R.id.missionList);

        // cajas de textos
        missionTitle = (TextView)findViewById(R.id.missionTitle);
        missionSubtitle = (TextView)findViewById(R.id.missionSubtitle);
        missionText = (TextView)findViewById(R.id.missionText);
        // botoón de cargar
        final Button loadMissionBtn = (Button) findViewById(R.id.loadMissionBtn);

        // cargo lista de títulos
        missionTitles = new ArrayList<String>();
        missionTitles.add(context.getString(R.string.mission1_title));
        missionTitles.add(context.getString(R.string.mission2_title));
        missionTitles.add(context.getString(R.string.mission3_title));
        missionTitles.add(context.getString(R.string.mission4_title));
        missionTitles.add(context.getString(R.string.mission0_title));

        // cargo lista de subtítulos
        missionSubTitles = new ArrayList<String>();
        missionSubTitles.add(context.getString(R.string.mission1_subtitle));
        missionSubTitles.add(context.getString(R.string.mission2_subtitle));
        missionSubTitles.add(context.getString(R.string.mission3_subtitle));
        missionSubTitles.add(context.getString(R.string.mission4_subtitle));
        missionSubTitles.add(context.getString(R.string.mission0_subtitle));

        // cargo lista de instrucciones
        missionTexts = new ArrayList<String>();
        missionTexts.add(context.getString(R.string.mission1_description));
        missionTexts.add(context.getString(R.string.mission2_description));
        missionTexts.add(context.getString(R.string.mission3_description));
        missionTexts.add(context.getString(R.string.mission4_description));
        missionTexts.add(context.getString(R.string.mission0_description));

        // cargo la primera misión
        loadMissionInfo(selectedMission);

        ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.mission_list_item, missionTitles);
        missionList.setAdapter(adapter);

        /**
         * Acción a ejecutar cuando se pulsa sobre una posición de la lista
         */
        missionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                // cargo la información de la misión de la posición pulsada
                loadMissionInfo(position);
            }

        });


        /**
         * Acción que se ejecuta al pulsar el botón de cargar misión
         */
        loadMissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMission();
            }
        });

    }

    /**
     * Carga la información de la misión seleccionada
     * @param missionSelected el nº de la misión
     */
    private void loadMissionInfo(int missionSelected) {
        missionTitle.setText(missionTitles.get(missionSelected));
        missionSubtitle.setText(missionSubTitles.get(missionSelected));
        missionText.setText(missionTexts.get(missionSelected));
        selectedMission = missionSelected;
    }

    /**
     * Vuelve a la pantalla de inicio con el
     * nivel de misión que se desea cargar
     */
    private void loadMission() {
        int missionToLoad = selectedMission;
        if (selectedMission<4) {
            missionToLoad++;
        } else {
            missionToLoad = 0;
        }
        Intent data = new Intent();
        data.putExtra("mission", missionToLoad);
        setResult(RESULT_OK, data);
       finish();
    }




}
