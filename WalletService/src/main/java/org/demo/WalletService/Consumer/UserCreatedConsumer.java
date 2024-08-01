package org.demo.WalletService.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.kafka.common.protocol.types.Field;
import org.demo.WalletService.Model.Wallet;
import org.demo.WalletService.Repository.WalletRepository;
import org.demo.utils.CommonConstants;
import org.demo.utils.UserIdentifier;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserCreatedConsumer {

@Autowired
    private WalletRepository walletRepository;
@Autowired
    private ObjectMapper objectMapper;

        @KafkaListener(topics= CommonConstants.USER_CREATION_TOPIC,groupId = "wallet-group")
    public void crateWallet(String msg) throws JsonProcessingException {
            JSONObject jsonObject=objectMapper.readValue(msg,JSONObject.class);
            String contact = (String) jsonObject.get(CommonConstants.USER_CREATION_TOPIC_CONTACT);
            Integer userId = (Integer) jsonObject.get(CommonConstants.USER_CREATION_TOPIC_USER_ID);
            String userIdentifier = (String) jsonObject.get(CommonConstants.USER_CREATION_TOPIC_USERIDENTIFIER);
            String userIdentifierValue = (String) jsonObject.get(CommonConstants.USER_CREATION_TOPIC_USERIDENTIFIER_VALUE);

            Wallet wallet = Wallet.builder().
                    userId(userId).
                    contact(contact).
                    userIdentifier(UserIdentifier.valueOf(userIdentifier)).
                    userIdentifierValue(userIdentifierValue).
                    balance(20.0).build();

            walletRepository.save(wallet);


        }
}


