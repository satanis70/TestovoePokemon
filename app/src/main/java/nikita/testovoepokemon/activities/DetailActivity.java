package nikita.testovoepokemon.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nikita.testovoepokemon.R;
import nikita.testovoepokemon.models.Pokemon;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity {
    TextView textViewName, textViewHeight, textViewWeight, textViewType,
            textViewHp, textViewDefense, textViewAttack;
    String name, height, weight, type;
    private static final String TAG = "TestovoePokemonDetail";
    private Retrofit retrofit;
    Pokemon p;
    private RequestQueue mQueue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        textViewName = findViewById(R.id.pokemonName_detail);
        textViewHeight = findViewById(R.id.pokemonHeight_detail);
        textViewWeight = findViewById(R.id.pokemonWeight_detail);
        textViewType = findViewById(R.id.pokemonType_detail);
        textViewHp = findViewById(R.id.HpTextView);
        textViewDefense = findViewById(R.id.DefenseTextView);
        textViewAttack = findViewById(R.id.AttackTextView);
        name = getIntent().getStringExtra("pokemonName");
        textViewName.setText(name);
        mQueue = Volley.newRequestQueue(this);

        final String Stringjson = "https://pokeapi.co/api/v2/pokemon/"+name+"/";
        final JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, Stringjson, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    height = response.getString("height");
                    weight = response.getString("weight");
                    JSONArray mJsonArraytype = response.getJSONArray("types");
                    for (int i = 0; i < mJsonArraytype.length(); i++) {
                        JSONObject mJsonObjecttype = mJsonArraytype.getJSONObject(i);
                        String type = mJsonObjecttype.getJSONObject("type").getString("name");
                        textViewType.append(type+System.getProperty ("line.separator"));
                    }

                    JSONArray jsonArraystats = response.getJSONArray("stats");
                    JSONObject jsonObjectHp = jsonArraystats.getJSONObject(0);
                    String Hp = jsonObjectHp.getString("base_stat");
                    textViewHp.append("Hp "+Hp);
                    JSONObject jsonObjectAttack = jsonArraystats.getJSONObject(1);
                    String Attack = jsonObjectAttack.getString("base_stat");
                    textViewAttack.append("Attack "+Attack);
                    JSONObject jsonObjectDefense = jsonArraystats.getJSONObject(2);
                    String Defense = jsonObjectDefense.getString("base_stat");
                    textViewDefense.append("Defense "+Defense);
                    textViewHeight.setText("Height:" + height);
                    textViewWeight.setText("Weight:" + weight);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(jsonObject);

    }



}
