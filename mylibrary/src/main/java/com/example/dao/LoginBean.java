package com.example.dao;

public class LoginBean {


    /**
     * erroe_code : 1
     * message : 登录成功
     * data : {"id":1183,"username":"980x_288906","nickname":"","sex":"","message":"","gold_coin":0,"level":0,"diamonds":0,"login_days":0,"last_login_time":"","gift_status":1}
     */

    private int erroe_code;
    private String message;
    private DataBean data;

    public int getErroe_code() {
        return erroe_code;
    }

    public void setErroe_code(int erroe_code) {
        this.erroe_code = erroe_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1183
         * username : 980x_288906
         * nickname :
         * sex :
         * message :
         * gold_coin : 0
         * level : 0
         * diamonds : 0
         * login_days : 0
         * last_login_time :
         * gift_status : 1
         */

        private int id;
        private String username;
        private String nickname;
        private String sex;
        private String message;
        private int gold_coin;
        private int level;
        private int diamonds;
        private int login_days;
        private String last_login_time;
        private int gift_status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getGold_coin() {
            return gold_coin;
        }

        public void setGold_coin(int gold_coin) {
            this.gold_coin = gold_coin;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getDiamonds() {
            return diamonds;
        }

        public void setDiamonds(int diamonds) {
            this.diamonds = diamonds;
        }

        public int getLogin_days() {
            return login_days;
        }

        public void setLogin_days(int login_days) {
            this.login_days = login_days;
        }

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
        }

        public int getGift_status() {
            return gift_status;
        }

        public void setGift_status(int gift_status) {
            this.gift_status = gift_status;
        }
    }
}
