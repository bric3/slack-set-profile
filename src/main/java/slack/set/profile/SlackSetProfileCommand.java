package slack.set.profile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Scanner;

public class SlackSetProfileCommand implements Runnable {

    private final HttpClient httpClient = HttpClient.newBuilder().build();


    String token;

    public static void main(String[] args) {
        System.exit(new SlackSetProfileCommand().execute(args));
    }

    private int execute(String[] argArray) {
        final Deque<String> args = new ArrayDeque<>(Arrays.asList(argArray));

        boolean tokenOption = false;
        Runnable command = this::run;
        String arg;

        while ((arg = args.poll()) != null) {
            switch (arg) {
                case "-t":
                case "--token":
                    tokenOption = true;
                    break;
                case "read-profile":
                    command = this::readUserProfile;
                    break;
                case "write-profile":
                    command = this::writeUserProfile;
                    break;
                default:
                    token = arg;
                    break;
            }
        }

        if (!tokenOption) {
            System.out.println("Missing required option: --token");
            return 1;
        }

        if (token == null) {
            if (System.console() != null) {
                token = new String(System.console().readPassword("Slack Oauth token"));
            } else {
                System.out.print("Slack Oauth token: ");
                Scanner in = new Scanner(System.in);
                token = in.nextLine();
            }
        }

        command.run();
        return 0;
    }

    public void run() {
    }


    void readUserProfile() {
        try {
            final HttpResponse<String> httpResponse = httpClient.send(
                    HttpRequest.newBuilder()
                               .GET()
                               .uri(URI.create("https://slack.com/api/users.profile.get?pretty=1"))
                               .header("Authorization", "Bearer " + token)
                               .build(),
                    BodyHandlers.ofString()
            );

            System.out.printf("%s%n", httpResponse);
            System.out.printf("%s%n", httpResponse.headers());
            System.out.printf("%s%n", httpResponse.body());
        } catch (IOException | InterruptedException e) {
            System.err.printf("Error while reaching slack.com/api: %s%n", e);
        }
    }

    void writeUserProfile() {
        try {
            final HttpResponse<String> httpResponse = httpClient.send(
                    HttpRequest.newBuilder()
                               .POST(BodyPublishers.ofString(
                                       "{\n" +
                                       "    \"profile\": {\n" +
                                       "        \"real_name\": \"Ɛɔıɹq\",\n" +
                                       "        \"status_text\": \"graalvm\",\n" +
                                       "        \"status_emoji\": \":allthethings:\",\n" +
                                       "        \"status_expiration\": " + Instant.now().plus(10, ChronoUnit.MINUTES).getEpochSecond() + "\n" +
                                       "    }\n" +
                                       "}"))
                               .uri(URI.create("https://slack.com/api/users.profile.set?pretty=1"))
                               .header("Authorization", "Bearer " + token)
                               .header("Content-Type", "application/json; charset=utf-8")
                               .build(),
                    BodyHandlers.ofString()
            );

            System.out.printf("%s%n", httpResponse);
            System.out.printf("%s%n", httpResponse.headers());
            System.out.printf("%s%n", httpResponse.body());
        } catch (IOException | InterruptedException e) {
            System.err.printf("Error while reaching slack.com/api: %s%n", e);
        }
    }
}
