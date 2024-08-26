// package com.example.safeplaces.components;

// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.jms.core.JmsTemplate;
// import org.springframework.stereotype.Component;

// @Component

// public class safecopmo {

//     @Autowired
//     private JmsTemplate jmsTemplate;
//     //@JmsListener(destination = "safeplaces")

//     public void sendTestMessage() {
//         Map<String, Object> details = new HashMap<>();
//         details.put("latitude", 17.43498);
//         details.put("longitude", 78.3831);

//         Map<String, Map<String, Object>> testMap = new HashMap<>();
//         testMap.put("Test Place", details);

//         jmsTemplate.convertAndSend("alert", testMap);
//         System.out.println("Test message sent");
//     }
// }

