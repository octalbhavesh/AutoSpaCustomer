package com.poshwash.android.response;

import java.util.List;

public class LoginResponse
{

    /**
     * response : {"status":true,"message":"Login successfully.","data":[{"user_id":"9","login_type":"Manual","first_name":"Anand","last_name":"Jain","mobile":"9635488464","email":"anand@mailinator.com","mobile_otp":"123344","notification":"1","notification_count":0,"img":"http://http://192.168.1.122/sweater/app/webroot/img/Admin/avatar5.png"}]}
     */

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * status : true
         * message : Login successfully.
         * data : [{"user_id":"9","login_type":"Manual","first_name":"Anand","last_name":"Jain","mobile":"9635488464","email":"anand@mailinator.com","mobile_otp":"123344","notification":"1","notification_count":0,"img":"http://http://192.168.1.122/sweater/app/webroot/img/Admin/avatar5.png"}]
         */

        private boolean status;
        private String message;
        private List<DataBean> data;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * user_id : 9
             * login_type : Manual
             * first_name : Anand
             * last_name : Jain
             * mobile : 9635488464
             * email : anand@mailinator.com
             * mobile_otp : 123344
             * notification : 1
             * notification_count : 0
             * img : http://http://192.168.1.122/sweater/app/webroot/img/Admin/avatar5.png
             */

            private String user_id;
            private String login_type;
            private String first_name;
            private String last_name;
            private String mobile;
            private String email;
            private String mobile_otp;
            private String notification;
            private int notification_count;
            private String img;

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getLogin_type() {
                return login_type;
            }

            public void setLogin_type(String login_type) {
                this.login_type = login_type;
            }

            public String getFirst_name() {
                return first_name;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public String getLast_name() {
                return last_name;
            }

            public void setLast_name(String last_name) {
                this.last_name = last_name;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getMobile_otp() {
                return mobile_otp;
            }

            public void setMobile_otp(String mobile_otp) {
                this.mobile_otp = mobile_otp;
            }

            public String getNotification() {
                return notification;
            }

            public void setNotification(String notification) {
                this.notification = notification;
            }

            public int getNotification_count() {
                return notification_count;
            }

            public void setNotification_count(int notification_count) {
                this.notification_count = notification_count;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }
    }
}
