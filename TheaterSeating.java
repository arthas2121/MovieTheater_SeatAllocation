package seatAllocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class TheaterSeating {
	//TIME COMPLEXITY: O(n^2)
	//SPACE COMPLEXITY: O(n)
	// n is the number of required reservations.

	// global class variables for the theater.
	int theater_row = 10, theater_col = 20;
	int total_seats = theater_row * theater_col;
	int num_seats_booked = 0, num_customers = 0, curr_customers = 0;
	boolean[][] is_occupied = new boolean[theater_row][theater_col]; // all the seats that are not available.
	boolean[][] is_booked = new boolean[theater_row][theater_col]; // the seats that are booked by customers.
	int[] empty_seats = new int[] {theater_col,theater_col,theater_col,theater_col,theater_col,theater_col,theater_col,theater_col,theater_col,theater_col};
	//reservation_id -> ArrayList of all seat numbers
	LinkedHashMap<String, ArrayList<String>> output_pair = new LinkedHashMap<String, ArrayList<String>>();
	
	
	// constructor for TheaterSeating.
	public TheaterSeating() {
		for(int i = 0; i < theater_row; i++) {
			for(int j = 0; j < theater_col; j++) {
				is_occupied[i][j] = false;
				is_booked[i][j] = false;
			}
		}
	}
	
	
	/**
	 * Check if there are enough seats for the reservation
	 * 
	 * @param a string parameter called reservation, which represents the current reservation.
	 * @return boolean, true means valid, false means no enough seats.
	 * */
	public boolean CheckEnoughSeats(String reservation) {
		// Time Complexity: O(1)
		
		// process the reservation data
		String[] curr_reservation = reservation.split(" "); 
		String reservation_id = curr_reservation[0];
		int num_reservation = Integer.parseInt(curr_reservation[1]);
		
		// Continue to allocate seats if only the number of required reservations is less than the available seats.
		if(num_reservation <= total_seats) {
			num_customers += num_reservation;
			curr_customers = num_reservation;
			AllocateSeats(reservation_id, num_reservation, new ArrayList<int[]>());
		}else {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Allocate seats if checkEnoughSeats returns true.
	 * 
	 * @param string parameter reservation_id represents the identifier of the reservation,
	 * 		  and integer parameter num_reservation represents the number of seats needed for current reservation.
	 * 		  and ArrayList of integer array stores all the current seat numbers
	 * @return void method, no return.
	 * */
	public void AllocateSeats(String reservation_id, int num_reservation, ArrayList<int[]> curr_res) {
		//Time Complexity: O(n), n is the number of num_reservation.
		
		// situation when the number of reservation is greater than 20.
		if(num_reservation > theater_col) {
			int group_one = (num_reservation+1) / 2;
			int group_two = num_reservation - group_one;
			AllocateSeats(reservation_id, group_one, curr_res);
			AllocateSeats(reservation_id, group_two, curr_res);

		}else if(num_reservation > 0){
			
			// situation when the number of reservation is less than or equal to 20.
			int[] res = new int[] {-1, -1};
			
			// loop every row from the end to the start to find the row with empty seats more than required.
			for(int row_index = theater_row-1; row_index >= 0; row_index--) { //O(1)
				
				// if we found that row, call SeatsTogether method.
				if(empty_seats[row_index] >= num_reservation) {
					res = SeatsTogether(num_reservation, row_index);
					
					//  situation when the reservations could be allocated together.
					if(res[0] != -1) {
						
						//all_set = true;
						
						// convert each seat to string and added to the output_pair LinkedHashMap.
						for(int i = res[0]; i <= res[1]; i++) { // Since res[1] - res[0] <= 20, we could see it as O(1)
							String curr_seat = (char)(row_index + 65) + Integer.toString(i+1);
							
							// set the seat to true if booked.
							is_occupied[row_index][i] = true;
							is_booked[row_index][i] = true;
							if(!output_pair.containsKey(reservation_id)) {
								output_pair.put(reservation_id, new ArrayList<String>());
							}
							output_pair.get(reservation_id).add(curr_seat);
							
							// use curr_res to keep the current booking seats for current reservation.
							// and update curr_res every time we run the recursion and reach a sub-solution.
							curr_res.add(new int[] {row_index, i});
						}
						
						// update the number of available empty_seats and total_seats.
						empty_seats[row_index] -= num_reservation;
						total_seats -= num_reservation;
						num_seats_booked += num_reservation;
						
						break;
					}
				}
			}
			
			// if there is no row that could maintain all reservations to be seated together.
			// do the same recursion to split the number of reservations.
			if(res[0] == -1) {
				int sub_group_one = (num_reservation+1) / 2;
				int sub_group_two = num_reservation - sub_group_one;
				AllocateSeats(reservation_id, sub_group_one, curr_res);
				AllocateSeats(reservation_id, sub_group_two, curr_res);
			} 
		}
		
		// finally we call the BufferBookedSeats method to update the buffer
		// after we allocate all required seats for the reservation.
		if(curr_res.size() == curr_customers) {
			BufferBookedSeats(curr_res);
		}
				
	}
	
	
	/**
	 * Check if the current row could allocate all seats together.
	 * 
	 * @param an integer number represents the required number of reservations,
	 * 		  and an integer number represent the index of the current row to be checked.
	 * @return an integer array, if solution can be found, return an array storing the start and ending index
	 * 		   if no solution can be found, return {-1, -1}.
	 * */
	public int[] SeatsTogether(int num_reservation, int row_index) {
		//Time Complexity: O(1)
		
		int[] res = new int[] {-1, -1};
		
		int num_required = num_reservation;
		
		for(int i = 0; i < theater_col; i++) {
			if(!is_occupied[row_index][i]) {
				num_required--;
			}else {
				num_required = num_reservation;
			}
			
			if(num_required == 0) {
				res[1] = i;
				res[0] = i - num_reservation + 1;
				break;
			}
		}
		
		return res;
	}
	
	
	/**
	 * Update the current available seats to maintain the buffer condition.
	 * 
	 * @param an ArrayList contains arrays of integers with length 2, which
	 * 		  represents the row and the column for all booked seats.
	 * @return void method, no return.
	 * */
	public void BufferBookedSeats(ArrayList<int[]> booked_seats) {
		//Time Complexity: O(n)
		
		// all situations to update the buffer.
		int[] row_change = new int[] {0, 0, 0, 1, -1, 0, 0, 0};
		int[] col_change = new int[] {-3, -2, -1, 0, 0, 1, 2, 3};
		
		// check all buffered situation for every booked seat.
		// (Could be improved here)
		for(int[] booked_seat: booked_seats) {
			
			int temp_row = booked_seat[0];
			int temp_col = booked_seat[1];
			
			for(int i = 0; i < col_change.length; i++) {
				if(canReach(temp_row, temp_col, row_change[i], col_change[i]) && 
						is_occupied[temp_row + row_change[i]][temp_col + col_change[i]] == false) {
					is_occupied[temp_row + row_change[i]][temp_col + col_change[i]] = true;
					total_seats -= 1;
				}
			}
		}
	}
	
	
	/**
	 * Helper method for the BufferBookedSeats method to check the boundary.
	 * 
	 * @param integers temp_row, temp_col represents the row and column of the current seat,
	 * 		  integers temp_horizontal, temp_vertical represents the places needed to check.
	 * @return boolean, return true if the place could be reached, false if can not.
	 * */
	public boolean canReach(int temp_row, int temp_col, int row_change, int col_change) {
		//Time Complexity: O(1)
		
		if(temp_row + row_change < 0 || temp_row + row_change >= theater_row || temp_col + col_change < 0 || temp_col + col_change >= theater_col) {
			return false;
		}
		
		return true;
	}
	
	// get the stored LinkedHashMap.
	public LinkedHashMap<String, ArrayList<String>> getLinkedHashMap(){
		return output_pair;
	}
	
	// print the results
	public void printResult() {
		System.out.println("-----------------------------");
		System.out.println("There are " + output_pair.size() + " reservations successfully fulfilled in total.");
		System.out.println("There are " + num_customers + " customers need seats in total.");
		
		System.out.println("There are " + num_seats_booked + " seats booked in total.");
		System.out.println("There are " + total_seats + " seats available in total.");

		System.out.println("---------------------------------------");

		int booked_occupied_num = 0;
		
		for(int i = 0; i < theater_row; i++) {
			for(int j = 0; j < theater_col; j++) {
				if(is_booked[i][j] == true) {
					booked_occupied_num++;
					System.out.print("reserved, ");
				}else if(is_occupied[i][j] == true) {
					booked_occupied_num++;
					System.out.print("occupied, ");
				}else {
					System.out.print("        , ");
				}
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println("The efficiency of the theater seats usage is: " + (float)(num_seats_booked) / (float)(booked_occupied_num));
		
		System.out.println("---------------------------------------");
		String[][] seat_result = new String[theater_row][theater_col];
		for(int i = 0; i < theater_row; i++) {
			for(int j = 0; j < theater_col; j++) {
				seat_result[i][j] = "unused";
			}
		}
		
		for(Map.Entry<String, ArrayList<String>> entry: output_pair.entrySet()) {
			String key = entry.getKey();
			ArrayList<String> all_seats = entry.getValue();
			
			for(String s: all_seats) {
				int row_index = s.charAt(0) - 'A';
				int col_index = Integer.valueOf(s.substring(1)) - 1;
				seat_result[row_index][col_index] = key;
			}
		}
		
		for(int i = 0; i < theater_row; i++) {
			for(int j = 0; j < theater_col; j++) {
				System.out.print(seat_result[i][j] + "\t");
			}
			System.out.println();
		}
	}
	
}
