package pl.balwinski.service;

import java.util.Scanner;

public class ConsoleInputApiKeyService implements ApiKeyService {

    @Override
    public String getPublicApiKey() {
        System.out.println("Enter public API key:");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    @Override
    public String getPrivateApiKey() {
        System.out.println("Enter private API key:");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
}
