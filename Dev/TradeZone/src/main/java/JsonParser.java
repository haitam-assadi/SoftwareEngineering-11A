import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
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