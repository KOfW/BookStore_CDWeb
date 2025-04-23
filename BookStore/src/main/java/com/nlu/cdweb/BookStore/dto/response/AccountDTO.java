

    package com.nlu.cdweb.BookStore.dto.response;

    import java.util.List;

    public class AccountDTO {
        private Long id;
        private String email;
        private String username;
        private String password;
        private List<String> roles;

        public AccountDTO() {
        }

        public AccountDTO(Long id, String email, String username, String password, List<String> roles) {
            this.id = id;
            this.email = email;
            this.username = username;
            this.password = password;
            this.roles = roles;
        }

        // Getters và Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

