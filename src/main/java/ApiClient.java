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
            //En "url" skapas för att skapa en connection mellan klienten och servern för att kunna radera ett blog inlägg
            URL url = new URL(apiAdress + target);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");


            //Används en int för att ta emot en response-code som bestämmer om allt gick bra eller om ett fel inträffade
            int status = connection.getResponseCode();

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
            //En "url" skapas för att skapa en connection mellan klienten och servern för att kunna lista alla blog inlägg
            //Sätter RequestProperty till att ta emot json data med utf-8 som teckenuppsättning
            URL url = new URL(apiAdress + target);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
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
                //Läser in information från vår connection och skriver över det
                if (connection.getInputStream() != null) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        responseContent.append(line);
                    }
                    reader.close();
                }
            }
            String jsonStr = responseContent.toString();

            ObjectMapper mapper = new ObjectMapper();

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
            //En "url" skapas för att skapa en connection mellan klienten och servern för att kunna lista ett blog inlägg
            //Sätter RequestProperty till att ta emot json data med utf-8 som teckenuppsättning
            URL url = new URL(apiAdress + target);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");

            //Används en int för att ta emot en response-code som bestämmer om allt gick bra eller om ett fel inträffade.
            int status = connection.getResponseCode();

            if (status == 200) {
                //Läser in information från vår connection och skriver över det och skapar ett objekt
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                return null;
            }
            String jsonStr = responseContent.toString();

            ObjectMapper mapper = new ObjectMapper();
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
            //En "url" skapas för att skapa en connection mellan klienten och servern för att kunna skapa ett blogg inlägg
            //Sätter RequestProperty till json data med utf-8 som teckenuppsättning, och säger att på vår connection ska vi ha en output
            URL url = new URL(apiAdress + target);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);

            //Skapar en output stream, gör om vår post till Json sen Bytes och skriver det till vår connection
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = newBlogPost.toJson().getBytes(StandardCharsets.UTF_8);

                os.write(input, 0, input.length);
            }

            //Används en int för att ta emot en response-code som bestämmer om allt gick bra eller om ett fel inträffade.
            int status = connection.getResponseCode();

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
            //En "url" skapas för att skapa en connection mellan klienten och servern för att kunna uppdatera ett blog inlägg
            //Sätter RequestProperty till json data med utf-8 som teckenuppsättning, och säger att på vår connection ska vi ha en output
            URL url = new URL(apiAdress + target);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);


            //Skapa en output stream som skriver till vår connection, och skriver om vårt objekt till Json data och bytes med angiven teckenuppsättning
            try (OutputStream os = connection.getOutputStream()) {

                byte[] input = updatedBlogPost.toJson().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            //Används en int för att ta emot en response-code som bestämmer om allt gick bra eller om ett fel inträffade.
            int status = connection.getResponseCode();

            if (status == 200) {
                success = true;
                //Ska undersöka customException för att kunna ge ett svar på varje statuskod.
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
