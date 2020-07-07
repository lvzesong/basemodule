package com.base.basemodule.data;

public class BaseResp<T> {

    private T data;

    private StatusBean status;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static class StatusBean {
        /**
         * code : 0
         * status_des : success
         */

        private int code;
        private String status_des;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStatus_des() {
            return status_des;
        }

        public void setStatus_des(String status_des) {
            this.status_des = status_des;
        }
    }



    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }
}
