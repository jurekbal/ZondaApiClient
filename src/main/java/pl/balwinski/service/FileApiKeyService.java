package pl.balwinski.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Reads api keys from local text file.
 * Keep file safe. Do not push the file to public repository (set exception in .gitignore)
 * This handy but is unsafe. Only for testing. Keep key privileges low.
 */
public class FileApiKeyService implements ApiKeyService {

    private static final String EMPTY = "";

    private final String publicKey;
    private final String privateKey;


    public FileApiKeyService() {
        List<String> credentials;
        try {
            credentials = Files.readAllLines(Paths.get("./local_credentials/apiKeys.txt"));
        } catch (IOException e) {
            System.out.println("Error. Unable to read apiKeys file. " + e.getMessage());
            publicKey = EMPTY;
            privateKey = EMPTY;
            return;
        }
        if (credentials.isEmpty()) {
            System.out.println("Error. apiKeys file is empty");
            publicKey = EMPTY;
            privateKey = EMPTY;
            return;
        }

        publicKey = credentials.get(0);
        if (credentials.size() > 1) {
            privateKey = credentials.get(1);
        } else {
            privateKey = EMPTY;
        }
    }

    @Override
    public String getPublicApiKey() {
        return publicKey;
    }

    @Override
    public String getPrivateApiKey() {
        return privateKey;
    }
}
