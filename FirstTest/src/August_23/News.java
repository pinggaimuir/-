package August_23;

/**
 * Created by tarena on 2016/8/23.
 */
public class News {
    private int id;
    private String title;
    private String content;
    private int  type;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public News() {
    }

    public News(String content, int id, String title, int  type) {
        this.content = content;
        this.id = id;
        this.title = title;
        this.type = type;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int  getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
