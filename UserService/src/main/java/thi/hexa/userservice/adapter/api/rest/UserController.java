package thi.hexa.userservice.adapter.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import thi.hexa.userservice.adapter.api.rest.dto.*;
import thi.hexa.userservice.adapter.api.rest.exception.BadRequestException;
import thi.hexa.userservice.adapter.api.rest.exception.ResourceNotFoundException;
import thi.hexa.userservice.domain.User;
import thi.hexa.userservice.domain.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{user_id}")
    public UserResponse findUser(@PathVariable int user_id){
        User u = userService.getUser(user_id);
        if(u != null){
            return new UserResponse(u.getUser_id(), u.getUsername());
        }
        throw new ResourceNotFoundException("User with userid " + user_id + " not found");
    }

    @GetMapping("username/{username}")
    public UserResponse findUserByUsername(@PathVariable String username){
        User u = userService.getUserByUsername(username);
        if(u != null){
            return new UserResponse(u.getUser_id(), u.getUsername());
        }
        throw new ResourceNotFoundException("User with username " + username + " not found");
    }

    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest createUserRequest){
        User u = userService.createUser(createUserRequest.getUsername(), createUserRequest.getPassword());
        if (u != null){return new UserResponse(u.getUser_id(), u.getUsername());}
        throw new BadRequestException("User with username " + createUserRequest.getUsername() + " allready exists");
    }

    @PostMapping("/checkpassword")
    public CheckPasswordResponse checkPassword(@RequestBody CheckPasswordRequest checkPasswordRequest){
        User u = userService.getUserByUsername(checkPasswordRequest.getUsername());
        if(u == null){
           return  new CheckPasswordResponse(false);
        }
        boolean b = userService.verifyPassword(u, checkPasswordRequest.getPassword());
        return new CheckPasswordResponse(b);

    }

    @PostMapping("/{user_id}/changepw")
    public ChangePasswordResponse changePassword(@PathVariable int user_id, @RequestBody ChangePasswordRequest changePasswordRequest){
        boolean b = userService.changePassword(user_id, changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
        if(b){
            return new ChangePasswordResponse(b,"success");
        }
        return new ChangePasswordResponse(b,"Password not changed");
    }
    @DeleteMapping("/{user_id}")
    public void deleteUser(@PathVariable int user_id){
        userService.deleteUser(user_id);
    }

    @GetMapping("/userids")
    public UserIdsResponse getUserIds(){
        List<User> ul = userService.getAllUsers();
        List<Integer> il = new ArrayList<>();
        for (User u : ul) {
            il.add(u.getUser_id());
        }
        return new UserIdsResponse(il);
    }
}
