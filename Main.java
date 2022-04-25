package seatAllocation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		//Time Complexity: O(mn^2)
		//Space Complexity: O(mn)
		// m is the number of requests, n is the number of needed reservations in one request.
		
		TheaterSeating ts = new TheaterSeating();
		
		try {
			
			// read the input file.
			FileReader fr = new FileReader(new File("C:\\Users\\wuzhekun\\eclipse-workspace\\Seat_Allocation\\src\\seatAllocation\\reservations.txt"));
			BufferedReader br = new BufferedReader(fr);
			
			String reservation;
			
			// process the reservation input line by line.
			while((reservation = br.readLine()) != null) {
				
				System.out.println(reservation);
				
				// call the initial method of TheaterSeating class.
				boolean enoughSeats = ts.CheckEnoughSeats(reservation);
				
				// situation when there is no enough seats.
				if(enoughSeats == false) {
					System.out.println("Sorry! No enough seats for " + reservation.split(" ")[0]);
				}
			}
			
			br.close();
			
			ts.printResult();
			
			// output the results into the output file.
			LinkedHashMap<String, ArrayList<String>> res = ts.getLinkedHashMap();
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\wuzhekun\\eclipse-workspace\\Seat_Allocation\\src\\seatAllocation\\output.txt"));
			for(Map.Entry<String, ArrayList<String>> entry: res.entrySet()) {
				String one_reservation = entry.getKey() + " " + entry.getValue();
				bw.write(one_reservation + "\n");
			}
			
			bw.close();
			
		}catch(FileNotFoundException er) {
			System.out.println("File is not valid");
			er.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
