Movie Theater Seating Challenge - 2020

Programming Language: Java

Overview:
Implement an algorithm in order to assign movie theater seats according to the 
reservation requests while maximizing both customer satisfaction and public safety.
The program takes one input file containing information of all reservation requests.
And output a file with the seats numbers for every request if possible.

Assumptions:
1. Every reservation request comes with an identifier in the format of "R###", 
where "###" is from "001" to "999".

2. When the number of available seats is less than the required number of seats in the
reservation request, it will skip the request and print "No enough seats for R###"

Goals:
1. Customer Satisfaction
In order to maintain customer satisfaction, by the order the reservation requests are 
received, allocate all seats together for the request if possible. If not, split the
request number equally and try to maintain all seats together separately. Recursion is used.

2. Customer Safety.
In order to maintain public safety, make a buffer update of three seats and one row
for every group of requests. If one group of request is needed to be split into multiple
sub-groups, it is acceptable to allocate seats with rows next to each other.

Approaches:
1. Read the input file and process the request line by line.

2. Check if there is enough seats for the request.

3. Allocate seats while maintaining customer satisfaction.

4. Update the buffer to maintain public safety.

5. Write the results in a new file.

Optimizations:
1. The BufferedBookedSeats method could be improved since it only needs to update the
surroundings of the allocated seats intervals. Checking for every seat would result in
checking the same seats multiple times.

2. The situation when we need to divide customers into groups, there are three ways could be
implemented:

(1) Use greedy algorithm, for example, if the number of people is greater than 20, we could
separate those 20 people as a group.

(2) Equally divide customers into two groups if division is needed (the method I use here).

(3) (Improvement) We could equally divide customers into three groups when not being able to allocate
continuous seats in one row for customers. If the number of seats needed is greater than 5
(Except the cases when one group has only 1 person.) We divide the groups into 3, otherwise,
we divide the groups into 2. The reason we could divide the groups into 3 is that the maximum
number of seats needed could be 60 rather than 40, in the case when we only need one division.

3. For the Customer Satisfaction, it could be improved when starting allocating the middle
seats first, and then the ones at the side. Since for every row we start allocating seats
from the middle, we could construct an ArrayList of integer array with length 2,left_index
and right_index. For every row, the seats from 0 to left_index and from right_index to 
(theater_col-1) are the available seats.

4. We could see every allocated seats as intervals. For every row there is a initial 
interval [0, 19], which represents availability from seat 0 to seat 19. If an interval
of seats in one row is allocated, then store that interval inside the ArrayList for that row,
and every time we only need to check the boundary of intervals.

Steps for running:

Method One: From eclipse:

1. Copy paste the input file to reservations.txt

2. run the Main method.

Method Two: From CMD:

1. Give input in the reservations.txt file in the src folder.

2. javac the .java file to create .class file.

4. moves back to the package folder and run java seatAllocation.Main