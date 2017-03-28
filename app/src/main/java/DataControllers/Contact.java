package DataControllers;

public class Contact extends DatabaseObject {

    private String name = "";
    private String primaryPhone = "";
    private String secondaryPhone = "";
    private String streetAddress = "";
    private String city = "";
    private String state = "";
    private String zip = "";
    private String photo = "";

    public Contact(String id){
        this.setKey(id);
    }

    public Contact(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (!name.equals(contact.name)) return false;
        if (!primaryPhone.equals(contact.primaryPhone)) return false;
        if (!secondaryPhone.equals(contact.secondaryPhone)) return false;
        if (!streetAddress.equals(contact.streetAddress)) return false;
        if (!city.equals(contact.city)) return false;
        if (!state.equals(contact.state)) return false;
        if (!zip.equals(contact.zip)) return false;
        if (!photo.equals(contact.photo)) return false;
        return photo.equals(contact.photo);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + primaryPhone.hashCode();
        result = 31 * result + secondaryPhone.hashCode();
        result = 31 * result + streetAddress.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + state.hashCode();
        result = 31 * result + zip.hashCode();
        result = 31 * result + photo.hashCode();
        return result;
    }

    public String getPhoto(){
        return photo;
    }
    public void setPhoto(String photoInput){
        this.photo = photoInput;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
