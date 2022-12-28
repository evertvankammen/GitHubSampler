package nl.evertlevert;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class ApiRequesters {
    public ApiRequesters(String bearerToken){
        this.bearerToken = bearerToken;
    }


    private final String bearerToken;

    public List<RepoModel> QueryOnUserName(String userName) throws URISyntaxException, IOException, InterruptedException {
        String parameters = "q=user:" + userName;
        String uri = "https://api.github.com/search/repositories?" + parameters;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(uri))
                .version(HttpClient.Version.HTTP_2)
                .header("Authorization", "Bearer " + bearerToken)
                .header("Accept", "application/vnd.github.text-match+json")
                .header("Content-Type", "text/plain;charset=UTF-8")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        int i = response.statusCode();
        List<RepoModel> repoModels = new ArrayList<>();
        if (i > 299) {
            return repoModels;
        }

        JsonObject jsonObject = new JsonParser().parse(response.body()).getAsJsonObject();

        if (jsonObject == null) {
            return repoModels;
        }

        JsonElement jsonArrayItems = jsonObject.get("items");

        if (jsonArrayItems == null || jsonArrayItems.isJsonNull() || jsonArrayItems.getAsJsonArray().size() == 0) {
            return repoModels;
        }


        for (JsonElement element : jsonArrayItems.getAsJsonArray()) {
            JsonObject elementJsonObject = element.getAsJsonObject();
            RepoModel repoModel = new RepoModel();

            Integer size = elementJsonObject.get("size").isJsonNull() ? 0 : elementJsonObject.get("size").getAsInt();
            repoModel.setSize(size);

            String fullName = elementJsonObject.get("full_name").isJsonNull() ? "" : elementJsonObject.get("full_name").getAsString();
            repoModel.setFull_name(fullName);

            String language = elementJsonObject.get("language").isJsonNull() ? "" : elementJsonObject.get("language").getAsString();
            repoModel.setLanguage(language);

            String ssh_url = elementJsonObject.get("ssh_url").isJsonNull() ? "" : elementJsonObject.get("ssh_url").getAsString();
            repoModel.setSsh_url(ssh_url);

            String description = elementJsonObject.get("description").isJsonNull() ? "" : elementJsonObject.get("description").getAsString();
            repoModel.setDescription(description);

            repoModel.setCreated_at(LocalDateTime.parse(elementJsonObject.get("created_at").getAsString(), DateTimeFormatter.ISO_DATE_TIME));
            repoModel.setUpdated_at(LocalDateTime.parse(elementJsonObject.get("updated_at").getAsString(), DateTimeFormatter.ISO_DATE_TIME));

            repoModel.setIsFork(elementJsonObject.get("fork").getAsBoolean());
            repoModel.setIsPrivate(elementJsonObject.get("private").getAsBoolean());

            if (!repoModel.getIsPrivate() && repoModel.getLanguage().length() > 0 && repoModel.getDescription().length() > 0) {
                repoModels.add(repoModel);
            }
        }

        return repoModels;
    }


    public String RandomUserName() throws URISyntaxException, IOException, InterruptedException {
        int min = 1;
        int max = 121600000;
        JsonArray jsonArray;

        int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
        String uri = "https://api.github.com/users?since=" + random_int; //+ parameters;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(uri))
                .version(HttpClient.Version.HTTP_2)
                .header("Authorization", "Bearer " + bearerToken)
                .header("Accept", "application/vnd.github.text-match+json")
                .header("Content-Type", "text/plain;charset=UTF-8")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        jsonArray = new JsonParser().parse(response.body()).getAsJsonArray();
        if (jsonArray.size() == 0) {
            return null;
        }

        JsonElement element = jsonArray.get(0);
        JsonObject elementJsonObject = element.getAsJsonObject();

        return elementJsonObject.get("login").isJsonNull() ? "" : elementJsonObject.get("login").getAsString();
    }
}
