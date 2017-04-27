package DataControllers;

public class User extends DatabaseObject {

    private String name;
    private String type;
    private String address;
    private String primaryPhone;
    private String secondaryPhone;
    private String image;




    public User(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (type != null ? !type.equals(user.type) : user.type != null) return false;
        if (address != null ? !address.equals(user.address) : user.address != null) return false;
        if (primaryPhone != null ? !primaryPhone.equals(user.primaryPhone) : user.primaryPhone != null)
            return false;
        if (secondaryPhone != null ? !secondaryPhone.equals(user.secondaryPhone) : user.secondaryPhone != null)
            return false;
        return image != null ? image.equals(user.image) : user.image == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (primaryPhone != null ? primaryPhone.hashCode() : 0);
        result = 31 * result + (secondaryPhone != null ? secondaryPhone.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }

    //automatically generated getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPrimaryPhone() {
        return primaryPhone;
    }
    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }
    public String getSecondaryPhone() {
        return secondaryPhone;
    }
    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
