import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileService {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path path;

    public FileService(String path) {
        this.path = Paths.get(path);
    }

    public List<Cat> readFile() {
        String json;
        Type listType = new TypeToken<List<Cat>>() {
        }.getType();
        try {
            json = Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return GSON.fromJson(json, listType);
    }

    public void writeFile(List<Cat> cats) {
        String json = GSON.toJson(cats);

        byte[] arr = json.getBytes();
        try {
            Files.write(path, arr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
