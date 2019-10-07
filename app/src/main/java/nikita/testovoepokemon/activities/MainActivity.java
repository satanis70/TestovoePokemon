package nikita.testovoepokemon.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        listPokemonAdapter = new ListPokemonAdapter(this);
        recyclerView.setAdapter(listPokemonAdapter);
        recyclerView.setHasFixedSize(true);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

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

        Call<PokemonResponse> pokemonResponseCall = service.obtainerListPokemon(20,offset);

        pokemonResponseCall.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {

                suitableforcharging = true;

                if (response.isSuccessful()){

                    PokemonResponse pokemonResponse = response.body();
                    ArrayList<Pokemon> pokemonArrayList = pokemonResponse.getResults();

                    listPokemonAdapter.addListPokemon(pokemonArrayList);

                    for (int i =0; i<pokemonArrayList.size(); i++){
                        Pokemon p = pokemonArrayList.get(i);
                        Log.i(TAG,"pokemon" + p.getName());
                    }

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
