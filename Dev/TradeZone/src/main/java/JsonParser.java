import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

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
        String strJson = getJSONFromFile("Dev/TradeZone/JsonFiles/configurationFile.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse the JSON string
            JsonNode jsonNode = objectMapper.readTree(strJson);

            // Retrieve the data
            String data = jsonNode.get("dataBaseUrl").asText();
            int port = jsonNode.get("port").asInt();
            String credit = jsonNode.get("creditCardUrl").asText();

            // Print the retrieved data
            System.out.println("dataBaseUrl: " + data);
            System.out.println("port: " + port);
            System.out.println("creditCardUrl: " + credit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}