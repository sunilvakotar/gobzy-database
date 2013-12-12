package com.ruby.rkandro.pojo;


public class Story {
	private Integer id;
	private Integer storyCategoryId;
	private String storyName;
	private String storyDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStoryCategoryId() {
        return storyCategoryId;
    }

    public void setStoryCategoryId(Integer storyCategoryId) {
        this.storyCategoryId = storyCategoryId;
    }

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public String getStoryDesc() {
        return storyDesc;
    }

    public void setStoryDesc(String storyDesc) {
        this.storyDesc = storyDesc;
    }

    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", storyCategoryId=" + storyCategoryId +
                ", storyName='" + storyName + '\'' +
                ", storyDesc='" + storyDesc + '\'' +
                '}';
    }
}
