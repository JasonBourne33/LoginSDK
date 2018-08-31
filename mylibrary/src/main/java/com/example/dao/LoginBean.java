package com.example.dao;

public class LoginBean {


    /**
     * ret : 200
     * data : {"id":550,"username":"980x_236971","email":null,"tel":null,"temp":null,"access_token":"RvAKF9nADx5uO0D0TRIQ6Th17h-7a0Bg","is_certification":0,"has_change_username":0,"point":0}
     * msg : 1234567891234567
     */

    private String ret;
    private DataBean data;
    private String msg;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * id : 550
         * username : 980x_236971
         * email : null
         * tel : null
         * temp : null
         * access_token : RvAKF9nADx5uO0D0TRIQ6Th17h-7a0Bg
         * is_certification : 0
         * has_change_username : 0
         * point : 0
         */

        private int id;
        private String username;
        private Object email;
        private Object tel;
        private Object temp;
        private String access_token;
        private int is_certification;
        private int has_change_username;
        private int point;

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

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Object getTel() {
            return tel;
        }

        public void setTel(Object tel) {
            this.tel = tel;
        }

        public Object getTemp() {
            return temp;
        }

        public void setTemp(Object temp) {
            this.temp = temp;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getIs_certification() {
            return is_certification;
        }

        public void setIs_certification(int is_certification) {
            this.is_certification = is_certification;
        }

        public int getHas_change_username() {
            return has_change_username;
        }

        public void setHas_change_username(int has_change_username) {
            this.has_change_username = has_change_username;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }
    }
}
