package e.tamen.newapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.BaseKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Ref;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public enum ENUM_Gamer
    {
        correct, attention, addict, none
    }

    private String m_mail = "";
    private String m_name = "";
    private String m_firstName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Affichage du nom de l'application dans la console
        Log.i("Log info : ", getResources().getString(R.string.app_name));

        //Récupération de la date
        Date currentDate = Calendar.getInstance().getTime();

        //Affichage de la date
        TextView txt_dateRef = (TextView)findViewById(R.id.TXT_DATE);
        txt_dateRef.setText(currentDate.toString());

        //Référencements des champs Nom, Prénom, et Mail
        final TextView  txt_nameRef = (TextView)findViewById(R.id.TXT_NAME),
                        txt_firstNameRef = (TextView)findViewById(R.id.TXT_FIRSTNAME),
                        txt_mailRef = (TextView)findViewById(R.id.TXT_MAIL);


        //Opérations de création du mail


        txt_nameRef.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && txt_nameRef.getText().length() > 0)
                {
                    m_name = txt_nameRef.getText().toString().toLowerCase();
                }
                return false;
            }
        });

        //Après mise à jour du prénom, le mail s'actualise dans le champ txt_mailRef
        txt_firstNameRef.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && txt_firstNameRef.getText().length() > 0)
                {
                    m_firstName = txt_firstNameRef.getText().toString().toLowerCase();

                    //Mise à jour du champ "Mail"
                    m_mail = m_firstName.toLowerCase().charAt(0) + "." + m_name + "@.ludus-academie.fr";
                    txt_mailRef.setText(m_mail);
                }
                return false;
            }
        });

        //Référencement des checkbox
        final CheckBox[] chkRefs = new CheckBox[] {
                                            (CheckBox)findViewById(R.id.CHK_C),
                                            (CheckBox)findViewById(R.id.CHK_CPP),
                                            (CheckBox)findViewById(R.id.CHK_Csharp)
                                            };

        //Référencement des radios
        final RadioGroup rad_genderRef = (RadioGroup)findViewById(R.id.RADGRP_GENDER);

        //Affichage du toast en fonction du sexe de la personne
        rad_genderRef.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String welcomeTxt = (group.getCheckedRadioButtonId() == 0) ?  "Bienvenue Madame" : "Bienvenue Monsieur";
                Context currentContext = getApplicationContext();

                Toast newToast = Toast.makeText(currentContext,welcomeTxt,Toast.LENGTH_SHORT);
                newToast.show();
            }
        });

        //Référencement du champ contenant le nombre d'heures par sem
        final TextView nb_hoursRef = (TextView)findViewById(R.id.NB_HOURS);

        nb_hoursRef.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && nb_hoursRef.getText().length() > 0)
                {
                    //Définit l'état (correct, attention, addict) d'un joueur en fonction de son nombre d'heures via les opérateurs ternaires
                    ENUM_Gamer gamerState = Integer.parseInt(nb_hoursRef.getText().toString()) > 48 ?
                            ENUM_Gamer.addict
                            : (Integer.parseInt(nb_hoursRef.getText().toString()) > 24) ?
                            ENUM_Gamer.attention
                            : (Integer.parseInt(nb_hoursRef.getText().toString()) > 0) ?
                            ENUM_Gamer.correct : ENUM_Gamer.correct;

                    String toastText = gamerState.toString();
                    Context currentContext = getApplicationContext();

                    Toast toast = Toast.makeText(currentContext, toastText, Toast.LENGTH_SHORT);
                    //Référencement du toast
                    TextView toastRef = (TextView) toast.getView().findViewById(android.R.id.message);

                    switch(gamerState)
                    {
                        case correct:
                            toastRef.setTextColor(Color.GREEN);
                            break;
                        case attention:
                            toastRef.setTextColor(getResources().getColor(R.color.orange));
                            break;
                        case addict:
                            toastRef.setTextColor(Color.RED);
                            toast.setDuration(Toast.LENGTH_LONG);
                            break;
                            default:
                                break;
                    }
                    toast.show();
                }
                return false;
            }
        });



                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Bouton RAZ
        Button resetRef = (Button)findViewById(R.id.BT_RESET);

        resetRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_nameRef.setText("");
                txt_firstNameRef.setText("");
                txt_mailRef.setText("");
                for(int i = 0; i < chkRefs.length; i++)
                {
                    chkRefs[i].setChecked(false);
                }
                //Les
                nb_hoursRef.setText("");
                Log.i("Log info : ", "RAZ");
            }
        });


        //Bouton Validation
        Button confirmRef = (Button)findViewById(R.id.BT_CONFIRM);

        confirmRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String languages = "";

                for(int i = 0 ; i < chkRefs.length; i++)
                {
                    if(chkRefs[i].isChecked())
                    {
                        languages = languages.concat(chkRefs[i].getText().toString() + ", ");
                    }
                }

                if(languages == "")
                    languages = "none";
                else
                    languages = languages.substring(0, languages.length()-2);
                //Concaténation des informations
                Log.i("Log info : ",
                                        "Informations : Name : " + txt_nameRef.getText().toString() +
                                                ". First name : " + txt_firstNameRef.getText().toString() +
                                                ". Email : " + txt_mailRef.getText().toString() +
                                                ". Languages : " + languages +
                                                ". Gender : " + ((rad_genderRef.getCheckedRadioButtonId() == 0) ?  "Female" : "Male") +
                                                ". Gaming hours per week : " + nb_hoursRef.getText().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
