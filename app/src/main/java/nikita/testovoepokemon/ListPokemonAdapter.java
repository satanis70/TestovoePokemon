package nikita.testovoepokemon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import nikita.testovoepokemon.activities.DetailActivity;
import nikita.testovoepokemon.models.Pokemon;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class ListPokemonAdapter extends RecyclerView.Adapter<ListPokemonAdapter.ViewHolder> {

    private ArrayList<Pokemon> dataset;
    private Context context;
    private Pokemon p;

    public ListPokemonAdapter(Context context){

        this.context = context;
        dataset = new ArrayList<>();

    }

    @NonNull
    @Override
    public ListPokemonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pokemon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPokemonAdapter.ViewHolder holder, int position) {

        p = dataset.get(position);
        holder.pokenameTextView.setText(p.getName());
        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/" + p.getNumber() + ".png")
                .centerCrop()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.pokeImageView);

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void addListPokemon(ArrayList<Pokemon> pokemonArrayList){

        dataset.addAll(pokemonArrayList);
        notifyDataSetChanged();

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

            switch (view.getId()){

                case R.id.cardView:
                    String pokemon = p.getName();
                    Intent i = new Intent(view.getContext(), DetailActivity.class);
                    view.getContext().startActivity(i);

                    Snackbar.make(view, pokemon, Snackbar.LENGTH_LONG).show();
                    break;

            }

        }
    }
}
