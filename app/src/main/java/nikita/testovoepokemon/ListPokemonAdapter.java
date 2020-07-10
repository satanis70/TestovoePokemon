package nikita.testovoepokemon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import nikita.testovoepokemon.activities.DetailActivity;
import nikita.testovoepokemon.activities.MainActivity;
import nikita.testovoepokemon.models.Pokemon;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class ListPokemonAdapter extends RecyclerView.Adapter<ListPokemonAdapter.ViewHolder> {

    private ArrayList<Pokemon> dataset;
    private ArrayList<Pokemon>  datasetHp;
    private Context context;
    private Pokemon pok, pok2;
    String namePoke;
    MainActivity mainActivity;

    public ListPokemonAdapter(Context context){

        this.context = context;
        dataset = new ArrayList<>();
        datasetHp = new ArrayList<>();

    }

    public void addListPokemon(ArrayList<Pokemon> pokemonArrayList){

        dataset.addAll(pokemonArrayList);
        notifyDataSetChanged();

    }





    @NonNull
    @Override
    public ListPokemonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pokemon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPokemonAdapter.ViewHolder holder, final int position) {
        namePoke = dataset.get(position).getName();
        pok = dataset.get(position);
        holder.pokenameTextView.setText(pok.getName());
        final String height = pok.getHeight();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("pokemonName",dataset.get(position).getName());
                intent.putExtra("Height", dataset.get(position).getHeight());
                v.getContext().startActivity(intent);
                Toast.makeText(context, height, Toast.LENGTH_SHORT).show();
            }
        });
        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/" + pok.getNumber() + ".png")
                .centerCrop()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.pokeImageView);

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView pokeImageView;
        private TextView pokenameTextView;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            pokeImageView =  itemView.findViewById(R.id.pokeImageView);
            pokenameTextView =  itemView.findViewById(R.id.nametextView);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
