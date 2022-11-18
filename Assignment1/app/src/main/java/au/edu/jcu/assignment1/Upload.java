package au.edu.jcu.assignment1;

public class Upload {
    private String imageName;
    private String imageUri;

    public Upload() {
        // empty constructor needed!
    }

    public Upload (String name, String url) {
        if (name.trim().equals("")){
            name = "No Name";
        }
        imageName = name;
        imageUri = url;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
