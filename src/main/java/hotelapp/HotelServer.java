package hotelapp;


import jettyServer.JettyServer;

import java.util.HashMap;
import java.util.Map;

public class HotelServer {
	public static void main(String[] args) {

		Map<String, String> arg_map = getArgument();  // arguments handled
		int threads = Integer.parseInt(arg_map.get("-threads"));

		ThreadSafeHotelHandler hotelHandler = new ThreadSafeHotelHandler();
		ThreadSafeReviewHandler reviewHandler = new ThreadSafeReviewHandler();

		FileProcessor fp = new FileProcessor(reviewHandler, threads);

		Hotel[] hotels = fp.parseHotels(arg_map.get("-hotels"));
		hotelHandler.insertHotels(hotels);

		fp.initiateReviewInsertion(arg_map.get("-reviews"));

		runJettyServer(hotelHandler, reviewHandler);
	}
	public static void runJettyServer(ThreadSafeHotelHandler tsHotelHandler, ThreadSafeReviewHandler tsReviewHandler){
		try{
			JettyServer server = new JettyServer(tsHotelHandler, tsReviewHandler);
			server.start();
		} catch (Exception e) {
			System.out.println("Error in starting server: " + e.getMessage());
			e.printStackTrace();
		}
	}
	static Map<String, String> getArgument(){

		HashMap<String, String> arg_map = new HashMap<>();

				arg_map.put("-threads", "1");
				arg_map.put("-reviews", "input/reviews");
				arg_map.put("-hotels", "input/hotels/hotels.json");

			return arg_map;
	}
}