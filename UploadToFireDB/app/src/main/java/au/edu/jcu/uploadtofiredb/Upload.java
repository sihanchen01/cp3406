package au.edu.jcu.uploadtofiredb;

public class Upload {
    private String imageName;
    private String imageUrl;
    public Upload () {

    }

    public Upload (String name, String url) {
        if (name.trim().equals("")){
            name = "No Name";
        }
        this.imageName = name;
        this.imageUrl = url;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
