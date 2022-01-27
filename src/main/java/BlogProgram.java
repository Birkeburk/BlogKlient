import java.util.Scanner;

public class BlogProgram {
    private ApiClient myApiClient;

    public BlogProgram() {
        myApiClient = new ApiClient("http://127.0.0.1:8080/api/v1/blog");
    }

    public void start() {
        boolean programRunning = true;

        while (programRunning) {
            System.out.println("[1] Make a new post");
            System.out.println("[2] Delete a post");
            System.out.println("[3] Update a post");
            System.out.println("[4] View all posts");
            System.out.println("[5] View specific post");
            System.out.println("[6] Exit program");
            System.out.println("[?]> ");

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

    public void viewAllPosts() {
        BlogPost[] blogPosts = myApiClient.listBlogPosts();

        if (blogPosts.length > 0) {
            for (int i = 0; i < blogPosts.length; i++) {
                String title = blogPosts[i].title;
                String body = blogPosts[i].body;
                System.out.println(title + "\n");
                System.out.println(body + "\n\n");
            }
        } else {
            System.out.println("You dont have any blog posts");
        }
    }
    public void viewAPost() {
        BlogPost[] blogPosts = myApiClient.listBlogPosts();

        System.out.println("Enter which post you would like to view");
        System.out.print("[id]> ");

        int id = getUserInt();

        if (blogPosts.length > 0) {
            for (int i = 0; i < blogPosts.length; i++) {
                if (id == blogPosts[i].getID()) {
                    String title = blogPosts[i].title;
                    String body = blogPosts[i].body;
                    System.out.println("Title: " + title + "\n");
                    System.out.println("Body: " + body + "\n\n");
                    break;
                }
            }
        }
    }
    public void updateAPost() {
        BlogPost[] blogPosts = myApiClient.listBlogPosts();

        System.out.println("Enter which post you would like to edit!");
        System.out.print("[id]> ");

        int userChoice = getUserInt();

        for (int i = 0; i < blogPosts.length; i++){
            if(userChoice == blogPosts[i].getID()){
                System.out.println("Enter your post title!");
                System.out.print("[...]> ");
                String title = getUserString();

                System.out.println("Enter your new post body!");
                System.out.println("[...]> ");
                String body = getUserString();

                BlogPost updatedPost = new BlogPost(userChoice, title, body);

                boolean successful = myApiClient.updateBlogPost(updatedPost, userChoice);

                if(successful) {
                    System.out.println("Your post has been updated!");
                } else {
                    System.out.println("Something went wrong trying to update your post!");
                }

            }
        }
    }
    public void deleteAPost() {
        System.out.println("=========================================");
        System.out.println("Which blog post would you like to delete?");
        System.out.print("[id]> ");

        int userChoice = getUserInt();

        boolean successful = myApiClient.deleteBlogPost(userChoice);

        if(successful) {
            System.out.println("Your post has been deleted!");
        } else {
            System.out.println("Something went wrong trying to delete your post!");
        }
    }
    public void createAPost() {
        System.out.println("===========");
        System.out.println("Enter title");
        System.out.print("[...]> ");
        String title = getUserString();

        System.out.println("Enter body");
        System.out.println("[...]> ");
        String body = getUserString();

        BlogPost newBlogPost = new BlogPost(title, body);

        boolean successful = myApiClient.createBlogPost(newBlogPost);

        if (successful) {
            System.out.println("Your post has been successfully uploaded!");
        } else {
            System.out.println("Something went wrong trying to upload your post!");
        }
    }
    public String getUserString() {
        Scanner myScanner = new Scanner(System.in);

        String myString;

        while (true) {
            try {
                System.out.print("> ");
                myString = myScanner.nextLine();
                break;
            } catch (Exception e) {
                //System.out.println("Exception: " + e);
                System.out.println("Felaktig indata");
            }
        }

        return myString;
    }
    public int getUserInt() {
        Scanner myScanner = new Scanner(System.in);

        int myInteger;

        while (true) {
            try {
                System.out.print("> ");
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

