package nikita.testovoepokemon.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nikita.testovoepokemon.ListPokemonAdapter;
import nikita.testovoepokemon.R;

import nikita.testovoepokemon.models.Pokemon;
import nikita.testovoepokemon.pokeapi.PokemonService;
import nikita.testovoepokemon.models.PokemonResponse;


import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private static final String TAG = "TestovoePokemon";
    private RecyclerView recyclerView;
    private ListPokemonAdapter listPokemonAdapter;
    private int offset;
    private boolean suitableforcharging;
    private RequestQueue mQueue;
    private CheckBox checkBoxHp, checkBoxAttack, checkBoxDefense;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        listPokemonAdapter = new ListPokemonAdapter(this);
        recyclerView.setAdapter(listPokemonAdapter);
        recyclerView.setHasFixedSize(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        mQueue = Volley.newRequestQueue(this);
        checkBoxHp = findViewById(R.id.checkBoxHp);
        checkBoxAttack = findViewById(R.id.checkBoxAttack);
        checkBoxDefense = findViewById(R.id.checkBoxDefense);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0){

                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCpount = gridLayoutManager.getItemCount();
                    int pasteVisibleItams = gridLayoutManager.findFirstVisibleItemPosition();

                    if (suitableforcharging){

                        if ((visibleItemCount + pasteVisibleItams) >= totalItemCpount){

                            Log.i(TAG, "end");
                            suitableforcharging = false;
                            offset += 20;
                            obtanerData(offset);

                        }

                    }

                }

            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        suitableforcharging = true;
        offset = 0;
        obtanerData(offset);



    }

    private void obtanerData(int offset){
        PokemonService service = retrofit.create(PokemonService.class);

        Call<PokemonResponse> pokemonResponseCall = service.obtainerListPokemon(30,offset);

        pokemonResponseCall.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                suitableforcharging = true;

                if (response.isSuccessful()){

                    PokemonResponse pokemonResponse = response.body();
                    final ArrayList<Pokemon> pokemonArrayList = pokemonResponse.getResults();
                    final HashMap<Pokemon, String> hashMap = new HashMap<>();
                    listPokemonAdapter.addListPokemon(pokemonArrayList);

                    for (int i =0; i<pokemonArrayList.size(); i++){
                         final Pokemon p = pokemonArrayList.get(i);
                        final String Stringjson = "https://pokeapi.co/api/v2/pokemon/"+p.getName()+"/";
                        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Stringjson, null, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArraystats = response.getJSONArray("stats");
                                    JSONObject jsonObjectHp = jsonArraystats.getJSONObject(0);
                                    String hp = jsonObjectHp.getString("base_stat");
                                    JSONObject jsonObjectAttack = jsonArraystats.getJSONObject(1);
                                    String attack = jsonObjectAttack.getString("base_stat");
                                    JSONObject jsonObjectDefense = jsonArraystats.getJSONObject(2);
                                    String defense = jsonObjectDefense.getString("base_stat");

                                    hashMap.put(p,hp);
                                    //Log.i(TAG, hashMap.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });



                        mQueue.add(jsonObjectRequest);
                        checkBoxHp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            ArrayList<Pokemon> arrayList = new ArrayList<>();
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    Set<Map.Entry<Pokemon, String>> set = hashMap.entrySet();
                                    List<Map.Entry<Pokemon, String>> list = new ArrayList<Map.Entry<Pokemon, String>>(set);
                                    Collections.sort(list, new Comparator<Map.Entry<Pokemon, String>>() {
                                        @Override
                                        public int compare(Map.Entry<Pokemon, String> o1, Map.Entry<Pokemon, String> o2) {
                                            return o2.getValue().compareTo(o1.getValue());
                                        }
                                    });
                                    Map<Pokemon, String> sortedMap = new LinkedHashMap<>();
                                    for (Map.Entry<Pokemon, String> entry : list)
                                    {
                                        sortedMap.put(entry.getKey(), entry.getValue());

                                        arrayList.add(entry.getKey());

                                    }


                                    Toast.makeText(MainActivity.this, "Флажок выбран", Toast.LENGTH_SHORT).show();
                                    Log.i(TAG, arrayList.toString());
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Флажок не выбран", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                    Log.i(TAG,pokemonArrayList.toString());



                } else {
                    Log.e(TAG, "on response " + response.errorBody());
                }



            }


            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {

                suitableforcharging = true;
                Log.e(TAG, "failure " + t.getMessage());

            }
        });



    }


}
