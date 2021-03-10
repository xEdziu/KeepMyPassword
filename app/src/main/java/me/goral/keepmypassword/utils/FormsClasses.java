package me.goral.keepmypassword.utils;

public class FormsClasses {
    public static class LoginParams {
        public String username;
        public String password;
        public String androidID;

        public LoginParams(String username, String password, String androidID){
            this.username = username;
            this.password = password;
            this.androidID = androidID;
        }
    }

    public static class RegisterParams {
        public String username;
        public String email;
        public String password;
        public String password2;
        public String androidID;

        public RegisterParams(String username, String email, String password, String password2 ,String androidID){
            this.username = username;
            this.email = email;
            this.password = password;
            this.password2 = password2;
            this.androidID = androidID;
        }
    }
}


