    package com.nlu.cdweb.BookStore.dto;

    import com.fasterxml.jackson.annotation.JsonInclude;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    public class ApiResponse {
        private String success;
        private String message;
        public Object data;

        public ApiResponse(String success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
