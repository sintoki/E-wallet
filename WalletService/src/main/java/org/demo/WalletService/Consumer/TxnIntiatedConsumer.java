package org.demo.WalletService.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.demo.utils.CommonConstants;
import org.demo.WalletService.Model.Wallet;
import org.demo.WalletService.Repository.WalletRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TxnIntiatedConsumer {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private KafkaTemplate kafkaTemplate;


    @KafkaListener(topics = CommonConstants.TXN_INITIATED_TOPIC, groupId ="wallet-group")
    public void updateWallet(String msg) throws JsonProcessingException {
        JSONObject object = objectMapper.readValue(msg, JSONObject.class);

        String sender = (String) object.get("sender");
        Double amount = (Double) object.get("amount");
        String receiver = (String) object.get("receiver");
        String txnId = (String) object.get("txnid");
        Wallet senderWallet = walletRepository.findByContact(sender);
        Wallet receiverWallet  = walletRepository.findByContact(receiver);
        String message = "txn is in inititaed";
        String status = "pending";


        if(senderWallet ==null){
            message =  "sender wallet is not associated with us ";
            status = "failure";
        }else if(receiverWallet == null){
            message =  "receiver wallet is not associated with us ";
            status = "failure";
        }else if(amount > senderWallet.getBalance()){
            message =  "sender amount is less than the amount he want to make a txn for. ";
            status = "failure";

        }else{
            walletRepository.updateWallet(sender, -amount);
            walletRepository.updateWallet(receiver, amount);
            message =  "txn is success";
            status = "SUCCESS";
        }
//        JSONObject resp = new JSONObject();
//        resp.put("message", message);
//        resp.put("status", status);
//        resp.put("txnid" , txnId);
//        // kafka published
//        kafkaTemplate.send(CommonConstants.TXN_UPDATE_TOPIC,resp);
        JSONObject json = new JSONObject();
        json.put("message", message);
        json.put("status", status);
        json.put("txnid", txnId);

        // Convert JSONObject to String using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(json);

        // Publish to Kafka
        kafkaTemplate.send(CommonConstants.TXN_UPDATE_TOPIC, jsonString);
    }
}
