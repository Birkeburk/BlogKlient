import java.util.Scanner;

public class BlogProgram {
    private ApiClient myApiClient;

    //Konstruktor som skapar ett objekt av ApiClient classen med vår url
    public BlogProgram() {
        myApiClient = new ApiClient("http://127.0.0.1:8080/api/v1/blog");
    }

    //Metod för att starta programmet
    public void start() {
        boolean programRunning = true;

        while (programRunning) {
            System.out.println(ConsoleColors.BLACK_BOLD + "======================" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.GREEN_BOLD + "[1] Make a new post");
            System.out.println("[2] Delete a post");
            System.out.println("[3] Update a post");
            System.out.println("[4] View all posts");
            System.out.println("[5] View specific post");
            System.out.println("[6] Exit program" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.BLACK_BOLD + "======================");
            System.out.print(ConsoleColors.GREEN_BOLD + "[?]> " + ConsoleColors.RESET);

            int userChoice = getUserInt();

            switch (userChoice) {
                case 1:
                    createAPost();
                    break;
                case 2:
                    deleteAPost();
                    break;
                case 3:
                    updateAPost();
                    break;
                case 4:
                    viewAllPosts();
                    break;
                case 5:
                    viewAPost();
                    break;
                case 6:
                    programRunning = false;

            }
        }
    }

    //Metod för att visa alla blog inlägg
    public void viewAllPosts() {
        BlogPost[] blogPosts = myApiClient.listBlogPosts();

        if (blogPosts.length > 0) {
            for (int i = 0; i < blogPosts.length; i++) {
                System.out.println(ConsoleColors.BLACK_BOLD + "==================================" + ConsoleColors.RESET);
                System.out.printf(ConsoleColors.GREEN_BOLD + "[ID:%d]\n%s\n\n%s\n", blogPosts[i].id, blogPosts[i].title, blogPosts[i].body + ConsoleColors.RESET);
            }
        } else {
            System.out.println(ConsoleColors.BLACK_BOLD + "===============================================" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.RED_BOLD + "You don't have any posts at this point in time!" + ConsoleColors.RESET);
        }
    }

    //Metod för att visa ett blog inlägg
    public void viewAPost() {
        System.out.println(ConsoleColors.BLACK_BOLD + "========================================" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN_BOLD + "Enter which post you would like to view!");
        System.out.print("[id]> " + ConsoleColors.RESET);

        int id = getUserInt();

        BlogPost fetchedPost = myApiClient.viewBlogPost(id);

        if (fetchedPost != null) {
            System.out.println(ConsoleColors.BLACK_BOLD + "==================================" + ConsoleColors.RESET);
            System.out.printf(ConsoleColors.GREEN_BOLD + "[ID:%d]\n%s\n\n%s\n", fetchedPost.getID(), fetchedPost.getTitle(), fetchedPost.getBody() + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.BLACK_BOLD + "===================================================================" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.RED_BOLD + "Couldn't display post with ID[" + id + "] because it doesn't exist!" + ConsoleColors.RESET);
        }

    }

    //Metod för att uppdatera ett blog inlägg
    public void updateAPost() {
        System.out.println(ConsoleColors.BLACK_BOLD + "========================================" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN_BOLD + "Enter which post you would like to edit!");
        System.out.print("[id]> " + ConsoleColors.RESET);

        int userChoice = getUserInt();

        System.out.println(ConsoleColors.GREEN_BOLD + "=====================" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN_BOLD + "Enter your new title!");
        System.out.print("[...]> " + ConsoleColors.RESET);
        String title = getUserString();

        System.out.println(ConsoleColors.BLACK_BOLD + "====================" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN_BOLD + "Enter your new body!");
        System.out.print("[...]> " + ConsoleColors.RESET);
        String body = getUserString();

        BlogPost updatedPost = new BlogPost(userChoice, title, body);

        myApiClient.updateBlogPost(updatedPost, userChoice);
    }

    //Metod för att radera ett blog inlägg
    public void deleteAPost() {
        System.out.println(ConsoleColors.BLACK_BOLD + "=========================================" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN_BOLD + "Which blog post would you like to delete?");
        System.out.print("[id]> " + ConsoleColors.RESET);

        int userChoice = getUserInt();

        myApiClient.deleteBlogPost(userChoice);

    }

    //Metod för att skapa ett blog inlägg
    public void createAPost() {
        System.out.println(ConsoleColors.BLACK_BOLD + "===========" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN_BOLD + "Enter title");
        System.out.print("[...]> " + ConsoleColors.RESET);

        String title = getUserString();

        System.out.println(ConsoleColors.BLACK_BOLD + "==========" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN_BOLD + "Enter body");
        System.out.print("[...]> " + ConsoleColors.RESET);

        String body = getUserString();

        BlogPost newBlogPost = new BlogPost(title, body);

        myApiClient.createBlogPost(newBlogPost);

    }

    //Metod för att at emot en string av användaren
    public String getUserString() {
        Scanner myScanner = new Scanner(System.in);

        String myString;

        while (true) {
            try {
                myString = myScanner.nextLine();
                break;
            } catch (Exception e) {
                //System.out.println("Exception: " + e);
                System.out.println("Felaktig indata");
            }
        }

        return myString;
    }

    //Metod för att ta emot en integer av användaren
    public int getUserInt() {
        Scanner myScanner = new Scanner(System.in);

        int myInteger;

        while (true) {
            try {
                myInteger = Integer.parseInt(myScanner.nextLine());
                break;
            } catch (Exception e) {
                //System.out.println("Exception: " + e);
                System.out.println("Felaktig indata");
            }
        }

        return myInteger;
    }
}

