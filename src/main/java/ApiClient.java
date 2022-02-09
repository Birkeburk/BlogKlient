//Dessa paket används för att skriva från och till Http-anslutningar

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

//Dessa paket används för att konvertera information från Json till java objekt och tvärtom
import com.fasterxml.jackson.databind.*;

//Detta paket används för att kunna skicka data med UTF-8, låter oss använda ÅÄÖ
import java.nio.charset.StandardCharsets;

//Dessa paket används för att skapa Http-anslutningar
import java.net.URL;
import java.net.HttpURLConnection;

public class ApiClient {
    private String apiAdress;
    HttpURLConnection connection;

    //Konstruktor för att kunna skapa ett objekt av denna klassen
    public ApiClient(String apiAdress) {
        this.apiAdress = apiAdress;
    }

    //Metod för att radera ett befintligt blog inlägg
    public boolean deleteBlogPost(int id) {
        //Sträng för att kunna slutföra "url"
        String target = "/delete/" + id;

        boolean success = false;

        try {
            //En url skapas
            URL url = new URL(apiAdress + target);
            //Öppnar en connection mellan vår klient och server med hjälp av vår url
            connection = (HttpURLConnection) url.openConnection();
            //Sätter RequestMethod till "DELETE" för att kunna ta bort från servern
            connection.setRequestMethod("DELETE");

            //Tar emot en statuskod från servern och sparar den i status
            int status = connection.getResponseCode();

            //Kollar om statuskoden är OK, om ja så returneras true annars false
            if (status == 200) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return success;
    }

    //Metod för att visa lista på alla blog inlägg
    public BlogPost[] listBlogPosts() {
        BlogPost[] blogPosts = {};

        //Sträng för att kunna slutföra "url"
        String target = "/list";

        BufferedReader reader;
        String line;
        StringBuilder responseContent = new StringBuilder();

        try {
            //Skapar en url
            URL url = new URL(apiAdress + target);
            //Öppnar en connection mellan vår klient och server med hjälp av vår url
            connection = (HttpURLConnection) url.openConnection();
            //Sätter RequestMethod till "GET" för att kunna ta emot information från servern
            connection.setRequestMethod("GET");
            //Bestämmer att det är json data som ska tas emot
            connection.setRequestProperty("accept", "application/json");

            //Används en int för att ta emot en response-code som bestämmer om allt gick bra eller om ett fel inträffade.
            int status = connection.getResponseCode();

            if (status >= 300) {
                if (connection.getErrorStream() != null) {
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                        responseContent.append(line);
                    }
                    reader.close();
                }
            } else {
                //Läser in information från vår connection och skriver över det till responseContent
                if (connection.getInputStream() != null) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        responseContent.append(line);
                    }
                    reader.close();
                }
            }
            //Skriver om json data i responseContent till vanlig data
            String jsonStr = responseContent.toString();
            //Skapar en mapper
            ObjectMapper mapper = new ObjectMapper();
            //Använder vår mapper för att göra om json data till ett java objekt
            blogPosts = mapper.readValue(jsonStr, BlogPost[].class);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return blogPosts;
    }

    //Metod för att visa ett specifikt blog inlägg
    public BlogPost viewBlogPost(int id) {
        BlogPost fetchedPost = null;

        //Sträng för att kunna slutföra "url"
        String target = "/view/" + id;

        BufferedReader reader;
        String line;
        StringBuilder responseContent = new StringBuilder();

        try {
            //Skapar en url
            URL url = new URL(apiAdress + target);
            //Öppnar en connection mellan vår klient och server med hjälp av vår url
            connection = (HttpURLConnection) url.openConnection();
            //Sätter RequestMethod till "GET" för att kunna ta emot information från servern
            connection.setRequestMethod("GET");
            //Bestämmer att det är json data som ska tas emot
            connection.setRequestProperty("accept", "application/json");

            //Används en int för att ta emot en response-code som bestämmer om allt gick bra eller om ett fel inträffade.
            int status = connection.getResponseCode();

            if (status == 200) {
                //Läser in information från vår connection och skriver över det till responseContent
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                return null;
            }
            //Skriver om json data i responseContent till vanlig data
            String jsonStr = responseContent.toString();
            //Skapar en mapper
            ObjectMapper mapper = new ObjectMapper();
            //Använder vår mapper för att göra om json data till ett java objekt
            fetchedPost = mapper.readValue(jsonStr, BlogPost.class);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return fetchedPost;
    }

    //Metod för att skapa nya blog inlägg
    public boolean createBlogPost(BlogPost newBlogPost) {
        //Sträng för att kunna slutföra "url"
        String target = "/create";

        boolean success = false;

        try {
            //Skapar en url
            URL url = new URL(apiAdress + target);
            //Öppnar en connection mellan vår klient och server med hjälp av vår url
            connection = (HttpURLConnection) url.openConnection();
            //Sätter RequestMethod till "POST" för att kunna skapa ett inlägg med ändringar i meddelande bodyn
            connection.setRequestMethod("POST");
            //Bestämmer att det är json data som ska skickas
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            //Bestämmer att vi ska ha en output på vår connection
            connection.setDoOutput(true);

            //Skapar en output stream till vår connection, gör om vår post till Json sen Bytes och sparar det i en Array av bytes
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = newBlogPost.toJson().getBytes(StandardCharsets.UTF_8);
                // Skriver vår input till vår connection
                os.write(input, 0, input.length);
            }

            //Tar emot en statuskod från servern och sparar den i status
            int status = connection.getResponseCode();

            //Kollar om statuskoden är CREATED, om ja så returneras true annars false
            if (status == 201) {
                success = true;
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.BLACK_BOLD + "=================================================" + ConsoleColors.RESET);
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return success;
    }

    //Metod för att uppdatera befintliga blog inlägg
    public boolean updateBlogPost(BlogPost updatedBlogPost, int id) {
        //Sträng för att kunna slutföra "url"
        String target = "/update/" + id;

        boolean success = false;

        try {
            //Skapar en url
            URL url = new URL(apiAdress + target);
            //Öppnar en connection mellan vår klient och server med hjälp av vår url
            connection = (HttpURLConnection) url.openConnection();
            //Sätter RequestMethod till "POST" för att kunna ändra i ett befintligt blog inlägg med ändringar i meddelande bodyn.
            connection.setRequestMethod("POST");
            //Bestämmer att det är json data som ska skickas
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            //Bestämmer att vi ska ha en output på vår connection
            connection.setDoOutput(true);

            //Skapar en output stream till vår connection, gör om vår post till Json sen Bytes och sparar det i en Array av bytes
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = updatedBlogPost.toJson().getBytes(StandardCharsets.UTF_8);
                // Skriver vår input till vår connection
                os.write(input, 0, input.length);
            }

            //Tar emot en statuskod från servern och sparar den i status
            int status = connection.getResponseCode();

            //Kollar om statuskoden är OK, om ja så returneras true annars false
            //Ska undersöka customException för att kunna ge ett svar på varje statuskod.
            if (status == 200) {
                success = true;
            } else if (status == 400) {
                System.out.println(ConsoleColors.BLACK_BOLD + "=========================================================" + ConsoleColors.RESET);
                System.out.println(ConsoleColors.RED_BOLD + "Couldn't update this post, missing mandatory information!" + ConsoleColors.RESET);
            } else if (status == 404) {
                System.out.println(ConsoleColors.BLACK_BOLD + "==========================================================" + ConsoleColors.RESET);
                System.out.println(ConsoleColors.RED_BOLD + "Couldn't update this post, because this post doesn't exist" + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return success;
    }
}
