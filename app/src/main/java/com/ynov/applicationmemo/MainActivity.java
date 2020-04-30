package com.ynov.applicationmemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ynov.applicationmemo.adapter.MemoAdapter;
import com.ynov.applicationmemo.database.AppDatabaseHelper;
import com.ynov.applicationmemo.dto.MemoDTO;
import com.ynov.applicationmemo.entity.Memo;
import com.ynov.applicationmemo.fragment.DetailFragment;
import com.ynov.applicationmemo.mapper.MemoMapper;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    //Vues
    private RecyclerView recyclerView = null;
    private EditText editTextMemo = null;
    private FrameLayout frameLayoutConteneurDetail = null;

    // Adapter :
    private MemoAdapter memoAdapter = null;

    // Gesture detector :
    private GestureDetector gestureDetector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int lastPosition = preferences.getInt("lastPosition", 0);

        if(lastPosition != 0) {
            Toast.makeText(this, "la dernière position était :"+lastPosition, Toast.LENGTH_SHORT).show();
        }

        AppDatabaseHelper.getDatabase(this);

        editTextMemo = findViewById(R.id.saisie_memo);
        recyclerView = findViewById(R.id.liste_memos);
        frameLayoutConteneurDetail = findViewById(R.id.conteneur_detail);

        // à ajouter pour de meilleures performances :
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // contenu d'exemple :
        List<MemoDTO> listeCourseDTO = AppDatabaseHelper.getDatabase(this).memoDAO().getListeMemos();
        memoAdapter = new MemoAdapter(listeCourseDTO);
        recyclerView.setAdapter(memoAdapter);

        // listener :
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener()
                {
                    @Override
                    public boolean onSingleTapUp(MotionEvent event)
                    {
                        return true;
                    }
                });
    }

    /**
     * Listener clic bouton valider.
     * @param view Bouton valider
     */
    public void onClickBoutonValider(View view)
    {
        // ajout du mémo :
        MemoDTO memoDTO = new MemoDTO(editTextMemo.getText().toString());
        memoAdapter.ajouterMemo(memoDTO);

        AppDatabaseHelper.getDatabase(this).memoDAO().insert(memoDTO);
        // animation de repositionnement de la liste (sinon on ne voit pas l'item ajouté) :
        recyclerView.smoothScrollToPosition(0);

        // on efface le contenu de la zone de saisie :
        editTextMemo.setText("");
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent motionEvent) {
        if (gestureDetector.onTouchEvent(motionEvent))
        {
            // récupération de l'item cliqué :
            View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (child != null)
            {
                // position dans la liste d'objets métier (position à partir de zéro !) :
                int position = recyclerView.getChildAdapterPosition(child);

                // récupération du mémo à cette position :
                MemoDTO memo = memoAdapter.getItemParPosition(position);

                Toast.makeText(this,"La position du mémo est:"+(position +1),Toast.LENGTH_SHORT).show();

                // client HTTP :
                AsyncHttpClient client = new AsyncHttpClient();

                // paramètres :
                RequestParams requestParams = new RequestParams();
                requestParams.put("memo", memo.getIntitule());
                if(estConnecte(this)) {
                    // appel :
                    client.post("http://httpbin.org/post",requestParams, new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            // retour du webservice :
                            String retour = new String(responseBody);

                            // conversion en un objet Java (à faire!) ayant le même format que le JSON :
                            Gson gson = new Gson();
                            MemoMapper mapperMemo = gson.fromJson(retour, MemoMapper.class);
                            // affichage d'un attribut :
                            Toast.makeText(MainActivity.this, "l'intitule est :" +mapperMemo.getMemoDTO().getIntitule(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                }

                // affichage du détail :
                if (frameLayoutConteneurDetail != null)
                {
                    // fragment :
                    DetailFragment fragment = new DetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(DetailFragment.EXTRA_MEMO, memo.getIntitule());
                    fragment.setArguments(bundle);

                    // le conteneur de la partie détail est disponible, on est donc en mode "tablette" :
                    getSupportFragmentManager().beginTransaction().replace(R.id.conteneur_detail, fragment).commit();
                }
                else
                {
                    // le conteneur de la partie détail n'est pas disponible, on est donc en mode "smartphone" :
                    Intent intent = new Intent(this, DetailActivity.class);
                    intent.putExtra(DetailFragment.EXTRA_MEMO, memo.getIntitule());
                    startActivity(intent);
                }

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("lastPosition", position+1);
                editor.apply();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
    public static boolean estConnecte(Context context) {
        // récupération du manager :
        ConnectivityManager connectivityManager =      (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // récupération de l'état de la connexion :
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null)   {
            return networkInfo.isConnected();
        }
        return false;
    }
}
