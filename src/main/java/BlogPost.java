import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class BlogPost implements Jsonable {
    public int id;
    public String title;
    public String body;

    enum keys implements JsonKey {
        ID("id"),
        TITLE("title"),
        BODY("body");

        private final Object value;

        keys(final Object value) {
            this.value = value;
        }

        @Override
        public String getKey() {
            return this.name().toLowerCase();
        }

        @Override
        public Object getValue() {
            return this.value;
        }
    }

    public BlogPost() {

    }

    public BlogPost(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public BlogPost(int id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return this.title;
    }

    public String getBody() {
        return this.body;
    }

    public int getID() {
        return this.id;
    }

    @Override
    public String toJson() {
        final StringWriter writable = new StringWriter();
        try {
            this.toJson(writable);
        } catch (final Exception e) {

        }
        return writable.toString();
    }

    @Override
    public void toJson(final Writer writable) throws IOException {
        try {
            final JsonObject json = new JsonObject();
            json.put(keys.TITLE.getKey(), this.getTitle());
            json.put(keys.BODY.getKey(), this.getBody());
            json.put(keys.ID.getKey(), this.getID());
            json.toJson(writable);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    @Override
    public String toString() {
        return "BlogPost{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
