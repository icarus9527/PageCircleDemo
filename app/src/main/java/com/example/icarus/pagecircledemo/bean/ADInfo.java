package com.example.icarus.pagecircledemo.bean;

/**
 * Created by icarus9527 on 2017/5/3.
 */
public class ADInfo {

    private String url;
    private int res;
    private String id;
    private String content;

    public int getRes(){
        return res;
    }

    public void setRes(int res){
        this.res = res;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl(){
        return url;
    }
}
