package game.AccesoADatos;

import com.google.gson.Gson; //importar Gson
import com.google.gson.reflect.TypeToken;

import game.Morty; //Importar personajes
import game.Rick;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class DatabaseHandler {
    private static final String FILE_PATH = "src/game/Persistencia/characters.json"; //conectar a Json

    public static Characters loadCharacters() throws IOException { //Metodo para cargar los personajes y pasarlos a objetos
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(FILE_PATH)) { //try abre y lee el archivo
            Type type = new TypeToken<Characters>() {}.getType(); // se usa typeToken para usar tipos genéricos
            return gson.fromJson(reader, type); //retorna el objeto creado 
        }
    }

    public static class Characters { //clasifica los personajes en listas de cada tipo
        private List<Rick> ricks; 
        private List<Morty> mortys;

        public List<Rick> getRicks() { //acceso a las listas
            return ricks;
        }

        public List<Morty> getMortys() {
            return mortys;
        }
    }
}
