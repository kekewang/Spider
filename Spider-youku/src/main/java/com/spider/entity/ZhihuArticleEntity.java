package com.spider.entity;

import java.io.Serializable;
import java.util.List;

public class ZhihuArticleEntity implements Serializable {

    private String id;

    private String title;

    private String content;

    private String fork;

    private String watch;

    private List<AnswerItem> answerItemList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFork() {
        return fork;
    }

    public void setFork(String fork) {
        this.fork = fork;
    }

    public String getWatch() {
        return watch;
    }

    public void setWatch(String watch) {
        this.watch = watch;
    }

    public List<AnswerItem> getAnswerItemList() {
        return answerItemList;
    }

    public void setAnswerItemList(List<AnswerItem> answerItemList) {
        this.answerItemList = answerItemList;
    }

    class AnswerItem {

        private String authorName;
        private String itemId;
        private String title;
        private String type;
        private String voters;
        private String content;
        private String date;

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVoters() {
            return voters;
        }

        public void setVoters(String voters) {
            this.voters = voters;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
