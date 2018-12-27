package ah1.com.advertisements_v1;

/**
 * Created by A on 7/17/2018.
 */

public class Ads {

    private String title;
    private String descrption;
    private String creationDate;

    private String image;


    private String username;



    private String location;
    private String phone;



    private String imageuser;


    public Ads() {

    }

    public Ads(String title, String descrption, String creationDate, String image,String username,String phone,String location,String imageuser) {
        this.title = title;
        this.descrption = descrption;
        this.creationDate = creationDate;
        this.image = image;
        this.username = username;
        this.phone = phone;
        this.location = location;
        this.imageuser = imageuser;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageuser() {
        return imageuser;
    }

    public void setImageuser(String imageuser) {
        this.imageuser = imageuser;
    }

}
