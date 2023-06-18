import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class JsonParser {
    public static String getJSONFromFile(String filename) {
        String jsonText = "";
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new FileReader(filename));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonText += line + "\n";
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonText;
    }

    public static String getJSONFromURL(String strUrl) {
        String jsonText = "";

        try {
            URL url = new URL(strUrl);
            InputStream is = url.openStream();

            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonText += line + "\n";
            }

            is.close();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return jsonText;
    }

//    public void initMarketParsing(){
//        HashMap memberName_guesName = new HashMap();
//        String strJson = getJSONFromFile("Dev/TradeZone/initFiles/init_1.json");
//        ObjectMapper objectMapper = new ObjectMapper();
//        try{
//            JsonNode jsonNode = objectMapper.readTree(strJson);
//            Iterator keys = jsonNode.fieldNames();
//            while(keys.hasNext()){
//                String current = (String) keys.next();
//                ArrayNode arrayNode;
//                switch(current) {
//                    case "register":
//                        String guest = "";
//                        arrayNode = (ArrayNode) jsonNode.get("register");
//                        System.out.println("arrayNode: " + arrayNode);
//                        for(JsonNode node: arrayNode){
//                            System.out.println("node: " + node);
//                            if(node.isArray()){
//                                guest = enterMarket();
//                                String member_name = node.get(0).asText();
//                                String member_pass = node.get(1).asText();
//                                memberName_guesName.put(member_name, guest);
//                                register(guest, member_name, member_pass);
//                            }
//                        }
//                        break;
//                    case "login":
//                        arrayNode = (ArrayNode) jsonNode.get("login");
//                        System.out.println("arrayNode: " + arrayNode);
//                        for(JsonNode node: arrayNode){
//                            System.out.println("node: " + node);
//                            if(node.isArray()){
//                                String member_name = node.get(0).asText();
//                                String member_pass = node.get(1).asText();
//                                login(memberName_guesName.get(member_name), member_name, member_pass);
//                            }
//                        }
//                        break;
//                    case "create_store":
//                        arrayNode = (ArrayNode) jsonNode.get("create_store");
//                        System.out.println("arrayNode: " + arrayNode);
//                        for(JsonNode node: arrayNode){
//                            System.out.println("node: " + node);
//                            if(node.isArray()){
//                                String member_name = node.get(0).asText();
//                                String store_name = node.get(1).asText();
//                                createStore(member_name, store_name);
//                            }
//                        }
//                        break;
//                    case "appoint_as_store_owner":
//                        arrayNode = (ArrayNode) jsonNode.get("appoint_as_store_owner");
//                        System.out.println("arrayNode: " + arrayNode);
//                        for(JsonNode node: arrayNode){
//                            System.out.println("node: " + node);
//                            if(node.isArray()){
//                                String owner_name = node.get(0).asText();
//                                String store_name = node.get(1).asText();
//                                String new_owner_name = node.get(2).asText();
//                                appointOtherMemberAsStoreOwner(owner_name, store_name, new_owner_name);
//                            }
//                        }
//                        break;
//                    case "logout":
//                        arrayNode = (ArrayNode) jsonNode.get("logout");
//                        System.out.println("arrayNode: " + arrayNode);
//                        for(JsonNode node: arrayNode){
//                            System.out.println("node: " + node);
//                            if(node.isArray()){
//                                String member_name = node.get(0).asText();
//                                memberLogOut(member_name);
//                            }
//                        }
//                        break;
//                    case "system_manager":
//                        break;
//                    case "add_product":
//                        arrayNode = (ArrayNode) jsonNode.get("add_product");
//                        System.out.println("arrayNode: " + arrayNode);
//                        for(JsonNode node: arrayNode){
//                            System.out.println("node: " + node);
//                            if(node.isArray()){
//                                String member_name = node.get(0).asText();
//                                String store_name = node.get(1).asText();
//                                String product_name = node.get(2).asText();
//                                String category_name = node.get(3).asText();
//                                Double price = node.get(4).asDouble();
//                                String description = node.get(5).asText();
//                                int amount = node.get(6).asInt();
//                                addNewProductToStock(member_name, store_name, product_name, category_name, price, description, amount);
//                            }
//                        }
//                        break;
//                    case "appoint_as_store_manager":
//                        arrayNode = (ArrayNode) jsonNode.get("appoint_as_store_manager");
//                        System.out.println("arrayNode: " + arrayNode);
//                        for(JsonNode node: arrayNode){
//                            System.out.println("node: " + node);
//                            if(node.isArray()){
//                                String member_name = node.get(0).asText();
//                                String store_name = node.get(1).asText();
//                                String new_manager_name = node.get(2).asText();
//                                appointOtherMemberAsStoreManager(member_name, store_name, new_manager_name);                            }
//                        }
//                        break;
//                    case "add_permissions":
//                        //    public boolean updateManagerPermissionsForStore(String ownerUserName, String storeName,
//                        //    String managerUserName, List<Integer> newPermissions) throws Exception {
//                        arrayNode = (ArrayNode) jsonNode.get("add_permissions");
//                        System.out.println("arrayNode: " + arrayNode);
//                        for(JsonNode node: arrayNode){
//                            System.out.println("node: " + node);
//                            if(node.isArray()){
//                                String member_name = node.get(0).asText();
//                                String store_name = node.get(1).asText();
//                                String manager_name = node.get(2).asText();
//                                ArrayList<Integer> permissions = new ArrayList<Integer>();
//                                if(node.get(3).isArray()){
//                                    Iterator iter  = node.get(3).iterator();
//                                    while(iter.hasNext()){
//                                        JsonNode curr = (JsonNode) iter.next();
//                                        permissions.add(curr.intValue());
//                                    }
//                                }
//                                updateManagerPermissionsForStore(member_name, store_name, manager_name, permissions);
//                            }
//                        }
//                        break;
//                    default:
//                        System.out.println("Tag is not supported");
//                }
//            }
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        String strJson = getJSONFromFile("Dev/TradeZone/JsonFiles/externalSystemsData.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse the JSON string
            JsonNode jsonNode = objectMapper.readTree(strJson);


            Iterator names = jsonNode.fieldNames();
            System.out.println("Names: ");
            while(names.hasNext()){
                System.out.println(names.next());

            }


            // Retrieve the data
            String data = jsonNode.get("dataBaseUrl").asText();
            int port = jsonNode.get("port").asInt();


            ArrayNode arrayNode = (ArrayNode) jsonNode.get("login");
            System.out.println("arrayNode: " + arrayNode);
            for(JsonNode node: arrayNode){
                System.out.println("node: " + node);
                if(node.isArray()){
                    for(JsonNode elem : node){
                        System.out.println(elem.asText());
                    }
                }
            }
            if(arrayNode.isArray()) {
                for(JsonNode node : arrayNode) {
                    String elem = node.asText();
                    System.out.println(elem);
                }
            }

            // Print the retrieved data
            System.out.println("dataBaseUrl: " + data);
            System.out.println("port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}