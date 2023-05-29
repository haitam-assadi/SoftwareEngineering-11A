package DomainLayer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class ExternalService {

    protected String url;
    protected boolean demo;
    protected int demoTransactionIds=1;
    protected int serviceResTimeInMin = 3;
    String serviceType;

    public ExternalService(){

    }

    public boolean handshake() throws Exception {
        if(demo)
            return true;
        HashMap<String, String> action_params = new HashMap<>();
        action_params.put("action_type","handshake");

        String serviceAnswer = sendPostRequest(action_params, serviceResTimeInMin).strip();

        if(serviceAnswer != "OK")
            throw new Exception(serviceType+ " service is not available");

        return true;
    }

    public String sendPostRequest(HashMap<String, String> action_params, int timeOut) throws Exception {
        String serviceResponse;
        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<Object> task = new Callable<Object>() {
            public Object call() throws IOException {
                return sendPostRequest(action_params);
            }
        };

        Future<Object> future = executor.submit(task);
        try {
            serviceResponse = (String)future.get(timeOut, TimeUnit.MINUTES);
        } catch (TimeoutException ex) {
            throw new Exception(serviceType+" service is not available");
        } catch (Exception e) {
            throw new Exception("transaction has failed");
        }finally {
            executor.shutdownNow();
        }
        return serviceResponse;
    }

    public String sendPostRequest(HashMap<String, String> action_params) throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setDoOutput(true);
        String request = this.buildRequest(action_params);

        byte[] requestInBytes = request.getBytes("UTF-8");
        OutputStream outputStream = httpConnection.getOutputStream();
        outputStream.write(requestInBytes);
        outputStream.flush();


        InputStream inputStream = httpConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String ret = bufferedReader.readLine();

        httpConnection.disconnect();
        return ret;
    }
    public String buildRequest(HashMap<String, String> action_params){
        String request = "";
        List<String> keys = action_params.keySet().stream().toList();
        for(int i=0; i< keys.size(); i++){
            request= request+keys.get(i)+"=" + action_params.get(keys.get(i));
            if(i+1 != keys.size())
                request= request+"&";
        }
        return request;
    }

    public boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }





}
