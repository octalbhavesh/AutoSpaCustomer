package com.poshwash.android.Beans;

import java.util.List;

public class PackagesData {


    /**
     * response : {"status":true,"message":"Package details list.","data":[{"id":"9","name":"Basic Package","amount":"25.00","status":"1","created":"2019-07-24 09:59:05","modified":"2019-07-24 09:59:05","is_purchase":1,"vehicle_type_id":"2","vehicle_type_name":"Sedan"}],"isactive":true}
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
         * message : Package details list.
         * data : [{"id":"9","name":"Basic Package","amount":"25.00",
         * "status":"1","created":"2019-07-24 09:59:05","modified":"2019-07-24 09:59:05",
         * "is_purchase":1,"vehicle_type_id":"2","vehicle_type_name":"Sedan"}]
         * isactive : true
         */

        private boolean status;
        private String message;
        private boolean isactive;
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

        public boolean isIsactive() {
            return isactive;
        }

        public void setIsactive(boolean isactive) {
            this.isactive = isactive;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 9
             * name : Basic Package
             * amount : 25.00
             * refer_amount : 25.00
             * status : 1
             * created : 2019-07-24 09:59:05
             * modified : 2019-07-24 09:59:05
             * is_purchase : 1
             * vehicle_type_id : 2
             * description : description
             * vehicle_type_name : Sedan
             */

            private String id;
            private String name;
            private String amount;
            private String status;
            private String created;
            private String modified;
            private int is_purchase;
            private String vehicle_type_id;
            private String vehicle_type_name;
            private String description;
            private String refer_amount;

            public String getRefer_amount() {
                return refer_amount;
            }

            public void setRefer_amount(String refer_amount) {
                this.refer_amount = refer_amount;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public String getModified() {
                return modified;
            }

            public void setModified(String modified) {
                this.modified = modified;
            }

            public int getIs_purchase() {
                return is_purchase;
            }

            public void setIs_purchase(int is_purchase) {
                this.is_purchase = is_purchase;
            }

            public String getVehicle_type_id() {
                return vehicle_type_id;
            }

            public void setVehicle_type_id(String vehicle_type_id) {
                this.vehicle_type_id = vehicle_type_id;
            }

            public String getVehicle_type_name() {
                return vehicle_type_name;
            }

            public void setVehicle_type_name(String vehicle_type_name) {
                this.vehicle_type_name = vehicle_type_name;
            }
        }
    }
}