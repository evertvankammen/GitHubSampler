package nl.evertlevert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final CsvSchema schemaHeader = CsvSchema.builder().setUseHeader(true)
            .addColumn("language")
            .addColumn("full_name")
            .addColumn("isFork")
            .addColumn("created_at_string")
            .addColumn("updated_at_string")
            .addColumn("ssh_url")
            .addColumn("size")
            .addColumn("description")
            .build();

    private static final CsvSchema schema = CsvSchema.builder().setUseHeader(false)
            .addColumn("language")
            .addColumn("full_name")
            .addColumn("isFork")
            .addColumn("created_at_string")
            .addColumn("updated_at_string")
            .addColumn("ssh_url")
            .addColumn("size")
            .addColumn("description")
            .build();


    public static void Save(RepoModel repoModel) {
        try {
            File csvOutputFile = new File("myObjects.csv");
            CsvSchema csvSchema = schema;
            if (!csvOutputFile.exists()) {
                csvOutputFile.createNewFile();
                csvSchema = schemaHeader;
            }

            CsvMapper mapper = new CsvMapper();
            mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
            ObjectWriter writer = mapper.writerFor(RepoModel.class).with(csvSchema);
            OutputStream outputStream = new FileOutputStream(csvOutputFile, true);
            writer.writeValue(outputStream, repoModel);

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

        getSecret();
        getMeSomeRepos();
    }

    private static String bearerToken;
    public static void getSecret(){
        try {
            String path = System.getProperty("user.dir") + "\\git_hub_sampler.properties";
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.startsWith("bearer_token")){
                    data = data.trim();
                    int start = "bearer_token=".length();
                    int end = data.length();
                    bearerToken = data.substring(start,end).trim();
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }



    private static void getMeSomeRepos() throws URISyntaxException, IOException, InterruptedException {
        ApiRequesters apiRequesters = new ApiRequesters(bearerToken);
        for (int i = 0; i < 100; i++) {
            String username = apiRequesters.RandomUserName();

            if (username != null) {
                System.out.println("GitHub info found for user: " + username);

                List<RepoModel> repoModels = apiRequesters.QueryOnUserName(username);

                for (RepoModel r : repoModels) {
                    System.out.println("---");
                    System.out.println(r.getLanguage() + " | " + r.getDescription() + " | " + r.getSize());
                        Save(r);
                    }
                }
            }

            TimeUnit.SECONDS.sleep(4);
        }

}
