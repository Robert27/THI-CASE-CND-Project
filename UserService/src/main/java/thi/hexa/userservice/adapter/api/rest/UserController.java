package thi.hexa.userservice.adapter.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import thi.hexa.userservice.adapter.api.rest.dto.*;
import thi.hexa.userservice.adapter.api.rest.exception.ResourceNotFoundException;
import thi.hexa.userservice.domain.User;
import thi.hexa.userservice.domain.UserService;

@RestController
@RequestMapping("user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public UserResponse findUser(@PathVariable String username){
        User u = userService.getUser(username);
        if(u != null){
            return new UserResponse(u.getUsername());
        }
        throw new ResourceNotFoundException("User with username " + username + " not found");
    }

    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest createUserRequest){
        User u = userService.createUser(createUserRequest.getUsername(), createUserRequest.getPassword());
        if (u != null){return new UserResponse(u.getUsername());}
        throw new ResourceNotFoundException("User with username " + createUserRequest.getUsername() + " allready exists");
    }

    @PostMapping("/checkpassword")
    public CheckPasswordResponse checkPassword(@RequestBody CheckPasswordRequest checkPasswordRequest){
        User u = userService.getUser(checkPasswordRequest.getUsername());
        if(u == null){
            throw new ResourceNotFoundException("User with username " + checkPasswordRequest.getUsername() + " not found");
        }
        boolean b = userService.verifyPassword(u, checkPasswordRequest.getPassword());
        return new CheckPasswordResponse(b);

    }

    @PostMapping("/{username}/changepw")
    public ChangePasswordResponse changePassword(@PathVariable String username, @RequestBody ChangePasswordRequest changePasswordRequest){
        boolean b = userService.changePassword(username, changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
        if(b){
            return new ChangePasswordResponse(b,"success");
        }
        return new ChangePasswordResponse(b,"Password not changed");
    }
    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username){
        userService.deleteUser(username);
    }
}
