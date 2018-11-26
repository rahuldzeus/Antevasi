package bhaiya.sid.com.antevasi;


class BioResponse {

    private String name,biography,image;
    BioResponse(String name, String biography, String image){
        this.name=name;
        this.biography=biography;
        this.image=image;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
