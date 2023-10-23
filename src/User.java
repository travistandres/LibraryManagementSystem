//DW 10/23/2023
public class User {
    private String name;
    private String email;
    User(String name, String email){
        setName(name);
        setEmail(email);
    }
    //#region Accessors
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    //#endregion
    //#region Mutators
    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    //#endregion
}