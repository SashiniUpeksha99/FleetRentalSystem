package lk.ijse.fleet;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static List<SystemUser> usersArr = new ArrayList<SystemUser>();

    public  SystemUser registerUser(String username, String password, String fullName){
        SystemUser user = findByUserName(username);

        if(usersArr.isEmpty() || user == null){
            SystemUser newUser = new SystemUser(username,password,fullName);
            usersArr.add(newUser);
            System.out.println("New User successfully added.");
            return newUser;
        }else if(user.getUsername().equalsIgnoreCase(username) ){
            System.out.println("Username already taken!");
            return null;
        }
        return null;
    }

    public SystemUser login(String username, String password){
        if (!usersArr.isEmpty()) {
            for(SystemUser user : usersArr){
                if(user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)){
                    return user;
                }
            }
        }
        return null;
    }

    public SystemUser findByUserName(String username){
        if (!usersArr.isEmpty()) {
            for (SystemUser user : usersArr) {
                if (user.getUsername().equalsIgnoreCase(username)) {
                    return user;
                }
            }
        }
        return null;
    }
}
