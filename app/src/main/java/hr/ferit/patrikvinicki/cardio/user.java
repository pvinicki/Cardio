package hr.ferit.patrikvinicki.cardio;

public class user {
    private int userID;
    private String username;
    private String email;
    private String password;

    public user(){};

    public user(String username, String password){
        this.username = username;
        this.password = password;
        this.email = null;
    }

    public user(String username, String email, String password){
        this.username = username;
        this.email    = email;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
