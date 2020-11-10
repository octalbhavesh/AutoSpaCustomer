package com.poshwash.android.response;

import java.util.List;

public class NotificationResponse
{


    /**
     * response : {"status":true,"message":"Successfully listed.","data":[{"id":"21","message":{"msg":"Dhiraj initiated for car wash by admin.","type":10,"booking_address":"401- A, Tirupati Balaji Nagar, Sanganer, Jaipur, Rajasthan 302029, India","request_type":"1","customer_name":"Dhiraj","first_name":"Dhiraj","last_name":"Kumawat","rating":0,"user_id":"1","customer_profile":"img/profile/profile_image_1556196406.8064.png","latitude":"26.815588","longitude":"75.808022","booking_id":"22","Booking_driver_id":"5"},"is_read":"0","created":"2019-07-01 05:37:09"},{"id":"19","message":{"msg":"Dhiraj initiated for car wash by admin.","type":10,"booking_address":"401- A, Tirupati Balaji Nagar, Sanganer, Jaipur, Rajasthan 302029, India","request_type":"1","customer_name":"Dhiraj","first_name":"Dhiraj","last_name":"Kumawat","rating":0,"user_id":"1","customer_profile":"img/profile/profile_image_1556196406.8064.png","latitude":"26.815588","longitude":"75.808022","booking_id":"25","Booking_driver_id":"4"},"is_read":"0","created":"2019-07-01 05:35:22"}]}
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
         * message : Successfully listed.
         * data : [{"id":"21","message":{"msg":"Dhiraj initiated for car wash by admin.","type":10,"booking_address":"401- A, Tirupati Balaji Nagar, Sanganer, Jaipur, Rajasthan 302029, India","request_type":"1","customer_name":"Dhiraj","first_name":"Dhiraj","last_name":"Kumawat","rating":0,"user_id":"1","customer_profile":"img/profile/profile_image_1556196406.8064.png","latitude":"26.815588","longitude":"75.808022","booking_id":"22","Booking_driver_id":"5"},"is_read":"0","created":"2019-07-01 05:37:09"},{"id":"19","message":{"msg":"Dhiraj initiated for car wash by admin.","type":10,"booking_address":"401- A, Tirupati Balaji Nagar, Sanganer, Jaipur, Rajasthan 302029, India","request_type":"1","customer_name":"Dhiraj","first_name":"Dhiraj","last_name":"Kumawat","rating":0,"user_id":"1","customer_profile":"img/profile/profile_image_1556196406.8064.png","latitude":"26.815588","longitude":"75.808022","booking_id":"25","Booking_driver_id":"4"},"is_read":"0","created":"2019-07-01 05:35:22"}]
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
             * id : 21
             * message : {"msg":"Dhiraj initiated for car wash by admin.","type":10,"booking_address":"401- A, Tirupati Balaji Nagar, Sanganer, Jaipur, Rajasthan 302029, India","request_type":"1","customer_name":"Dhiraj","first_name":"Dhiraj","last_name":"Kumawat","rating":0,"user_id":"1","customer_profile":"img/profile/profile_image_1556196406.8064.png","latitude":"26.815588","longitude":"75.808022","booking_id":"22","Booking_driver_id":"5"}
             * is_read : 0
             * created : 2019-07-01 05:37:09
             */

            private String id;
            private MessageBean message;
            private String is_read;
            private String created;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public MessageBean getMessage() {
                return message;
            }

            public void setMessage(MessageBean message) {
                this.message = message;
            }

            public String getIs_read() {
                return is_read;
            }

            public void setIs_read(String is_read) {
                this.is_read = is_read;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public static class MessageBean {
                /**
                 * msg : Dhiraj initiated for car wash by admin.
                 * type : 10
                 * booking_address : 401- A, Tirupati Balaji Nagar, Sanganer, Jaipur, Rajasthan 302029, India
                 * request_type : 1
                 * customer_name : Dhiraj
                 * first_name : Dhiraj
                 * last_name : Kumawat
                 * rating : 0
                 * user_id : 1
                 * customer_profile : img/profile/profile_image_1556196406.8064.png
                 * latitude : 26.815588
                 * longitude : 75.808022
                 * booking_id : 22
                 * Booking_driver_id : 5
                 */

                private String msg;
                private int type;
                private String booking_address;
                private String request_type;
                private String customer_name;
                private String first_name;
                private String last_name;
                private int rating;
                private String user_id;
                private String customer_profile;
                private String latitude;
                private String longitude;
                private String booking_id;
                private String Booking_driver_id;

                public String getMsg() {
                    return msg;
                }

                public void setMsg(String msg) {
                    this.msg = msg;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getBooking_address() {
                    return booking_address;
                }

                public void setBooking_address(String booking_address) {
                    this.booking_address = booking_address;
                }

                public String getRequest_type() {
                    return request_type;
                }

                public void setRequest_type(String request_type) {
                    this.request_type = request_type;
                }

                public String getCustomer_name() {
                    return customer_name;
                }

                public void setCustomer_name(String customer_name) {
                    this.customer_name = customer_name;
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

                public int getRating() {
                    return rating;
                }

                public void setRating(int rating) {
                    this.rating = rating;
                }

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getCustomer_profile() {
                    return customer_profile;
                }

                public void setCustomer_profile(String customer_profile) {
                    this.customer_profile = customer_profile;
                }

                public String getLatitude() {
                    return latitude;
                }

                public void setLatitude(String latitude) {
                    this.latitude = latitude;
                }

                public String getLongitude() {
                    return longitude;
                }

                public void setLongitude(String longitude) {
                    this.longitude = longitude;
                }

                public String getBooking_id() {
                    return booking_id;
                }

                public void setBooking_id(String booking_id) {
                    this.booking_id = booking_id;
                }

                public String getBooking_driver_id() {
                    return Booking_driver_id;
                }

                public void setBooking_driver_id(String Booking_driver_id) {
                    this.Booking_driver_id = Booking_driver_id;
                }
            }
        }
    }
}
