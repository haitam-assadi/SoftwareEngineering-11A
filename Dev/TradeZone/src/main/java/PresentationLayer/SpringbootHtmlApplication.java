package PresentationLayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

@SpringBootApplication
public class SpringbootHtmlApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringbootHtmlApplication.class, args);





		HashMap<String, String> action_params = new HashMap<>();
		action_params.put("action_type","cancel_pay");
		action_params.put("transaction_id",Integer.toString(20123));

		String serviceAnswer = sendPostRequest(action_params, 3).strip();

		if(!isNumeric(serviceAnswer))
			throw new Exception("canceling transaction has failed");

		Integer numericServiceAnswer = Integer.parseInt(serviceAnswer);

		if(numericServiceAnswer != 1)
			throw new Exception("canceling transaction has failed");

		System.out.println("hereeeeee");

	}




	public static String sendPostRequest(HashMap<String, String> action_params, int timeOut) throws Exception {
		String serviceResponse;
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<Object> task = new Callable<Object>() {
			public Object call() throws IOException{
				return sendPostRequest(action_params);
			}
		};

		Future<Object> future = executor.submit(task);
		try {
			serviceResponse = (String)future.get(timeOut, TimeUnit.MINUTES);
		} catch (TimeoutException ex) {
			System.out.println("58");
			throw new Exception("service is not available");
		} catch (Exception e) {
			System.out.println("60");
			throw new Exception("transaction has failed");
		}finally {
			System.out.println("64");
			executor.shutdownNow();
		}
		return serviceResponse;
	}

	public static String sendPostRequest(HashMap<String, String> action_params) throws IOException {
		URL url = new URL("https://php-server-try.000webhostapp.com/");
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.setDoOutput(true);
		String request = buildRequest(action_params);

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
	public static String buildRequest(HashMap<String, String> action_params){
		String request = "";
		List<String> keys = action_params.keySet().stream().toList();
		for(int i=0; i< keys.size(); i++){
			request= request+keys.get(i)+"=" + action_params.get(keys.get(i));
			if(i+1 != keys.size())
				request= request+"&";
		}
		return request;
	}


	public static boolean isNumeric(String str) {
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
