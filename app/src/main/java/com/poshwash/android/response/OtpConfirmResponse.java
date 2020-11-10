package com.poshwash.android.response;

import java.util.List;

public class OtpConfirmResponse
{

    /**
     * response : {"status":true,"message":"Mobile no verified successfully.
     * ","data":[{"user_id":"6","login_type":"Manual","first_name":"Abhinav",
     * "last_name":"Agrawal","country_code":"+91","mobile":"9509844700","email":"abhi@gmail.com"}]}
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
         * message : Mobile no verified successfully.
         * data : [{"user_id":"6","login_type":"Manual","first_name":"Abhinav",
         * "last_name":"Agrawal","country_code":"+91","mobile":"9509844700","email":"abhi@gmail.com"}]
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
             * user_id : 6
             * login_type : Manual
             * first_name : Abhinav
             * notification_count : Abhinav
             * last_name : Agrawal
             * country_code : +91
             * mobile : 9509844700
             * email : abhi@gmail.com
             */

            private String user_id;
            private String login_type;
            private String first_name;
            private String last_name;
            private String country_code;
            private String mobile;
            private String email;
            private String img;
            private String referral_code;
            private String notification_count;

            public String getNotification_count() {
                return notification_count;
            }

            public void setNotification_count(String notification_count) {
                this.notification_count = notification_count;
            }

            public String getReferral_code() {
                return referral_code;
            }

            public void setReferral_code(String referral_code) {
                this.referral_code = referral_code;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

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

            public String getCountry_code() {
                return country_code;
            }

            public void setCountry_code(String country_code) {
                this.country_code = country_code;
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
        }
    }
}
