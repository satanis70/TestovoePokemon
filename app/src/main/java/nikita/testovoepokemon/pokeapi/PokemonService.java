package nikita.testovoepokemon.pokeapi;

import nikita.testovoepokemon.models.PokemonResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.Call;

public interface PokemonService {
    @GET("pokemon")
    Call<PokemonResponse> obtainerListPokemon(@Query("limit") int limit, @Query("offset") int offset);

}
