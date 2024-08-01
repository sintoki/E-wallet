package Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.demo.UserService.Repository.UserRepository;
import org.demo.UserService.Request.CreateUserRequest;
import org.demo.UserService.model.User;
import org.demo.utils.CommonConstants;

import org.jose4j.json.internal.json_simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KafkaTemplate kafkaTemplate;


    @Override
    public UserDetails loadUserByUsername(String contact) throws UsernameNotFoundException {
        return userRepository.findByContact(contact);
    }
@Value("${user.authority}")
private String userAuthority;


    public User create(CreateUserRequest createUserRequest) throws JsonProcessingException {
        User user = createUserRequest.toUser();
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setAuthorities(userAuthority);
        user= userRepository.save(user);

        // publish data of user and consumers will be consuming it
        JSONObject jsonObject =new JSONObject();
        jsonObject.put(CommonConstants.USER_CREATION_TOPIC_USER_ID, user.getId());
        jsonObject.put(CommonConstants.USER_CREATION_TOPIC_CONTACT , user.getContact());
        jsonObject.put(CommonConstants.USER_CREATION_TOPIC_EMAIL , user.getEmail());
        jsonObject.put(CommonConstants.USER_CREATION_TOPIC_USERIDENTIFIER, user.getUserIdentifier());
        jsonObject.put(CommonConstants.USER_CREATION_TOPIC_NAME , user.getName());
        jsonObject.put(CommonConstants.USER_CREATION_TOPIC_USERIDENTIFIER_VALUE , user.getUserIdentifierValue());
        kafkaTemplate.send(CommonConstants.USER_CREATION_TOPIC, objectMapper.writeValueAsString(jsonObject));
        return user;
    }
}

