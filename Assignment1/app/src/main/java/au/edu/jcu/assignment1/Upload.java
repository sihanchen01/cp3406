package au.edu.jcu.assignment1;

// For storing book image as an Upload object in Firebase Database
public class Upload {
    private String imageName;
    private String imageUrl;

    // Empty constructor
    public Upload () {

    }

    // Constructor with given book image name and url
    public Upload (String name, String url) {
        if (name.trim().equals("")){
            name = "No Name";
        }
        this.imageName = name;
        this.imageUrl = url;
    }

    // Getter and Setter
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
