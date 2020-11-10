package com.poshwash.android.response;

import java.util.List;

public class StaticPageResponse
{

    /**
     * response : {"status":true,"message":"Page data.","data":[{"page_name":"Privacy Policy","contents":"<p>Lorem Ipsum .<\/p>\r\n"}]}
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
         * message : Page data.
         * data : [{"page_name":"Privacy Policy","contents":"<p>Lorem Ipsum .<\/p>\r\n"}]
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
             * page_name : Privacy Policy
             * contents : <p>Lorem Ipsum .</p>

             */

            private String page_name;
            private String contents;

            public String getPage_name() {
                return page_name;
            }

            public void setPage_name(String page_name) {
                this.page_name = page_name;
            }

            public String getContents() {
                return contents;
            }

            public void setContents(String contents) {
                this.contents = contents;
            }
        }
    }
}
