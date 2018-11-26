package bhaiya.sid.com.antevasi;

import java.io.Serializable;

public class HomeResponse implements Serializable {
    private String pic_name;
    private static final long serialVersionUID = 1L;
    HomeResponse(String pic_name){
        this.pic_name=pic_name;
    }

    public String getPicName() {
        return pic_name;
    }

    public void setPicName(String pic_name) {
        this.pic_name = pic_name;
    }
    

}


