package com.stackroute.vehiclerouting.solution;

import java.util.Random;

public class VRP {

	public static void main(String[] args) {

		Random ran = new Random(151190);

		// Problem Parameters
		int NoOfCustomers = 30;
		int NoOfVehicles = 10;
		int VehicleCap = 50;

		// Depot Coordinates
		int Depot_x = 50;
		int Depot_y = 50;

		// Tabu Parameter
		int TABU_Horizon = 10;

		// Initialise
		// Create Random Customers
		Node[] Nodes = new Node[NoOfCustomers + 1];
		Node depot = new Node(Depot_x, Depot_y);

		Nodes[0] = depot;
		for (int i = 1; i <= NoOfCustomers; i++) {
			Nodes[i] = new Node(i, // Id ) is reserved for depot
					ran.nextInt(100), // Random Cordinates
					ran.nextInt(100), 4 + ran.nextInt(7) // Random Demand
			);
		}

		double[][] distanceMatrix = new double[NoOfCustomers + 1][NoOfCustomers + 1];
		double Delta_x, Delta_y;
		for (int i = 0; i <= NoOfCustomers; i++) {
			for (int j = i + 1; j <= NoOfCustomers; j++) // The table is summetric to the first diagonal
			{ // Use this to compute distances in O(n/2)

				Delta_x = (Nodes[i].Node_X - Nodes[j].Node_X);
				Delta_y = (Nodes[i].Node_Y - Nodes[j].Node_Y);

				double distance = Math.sqrt((Delta_x * Delta_x) + (Delta_y * Delta_y));

				distance = Math.round(distance); // Distance is Casted in Integer
				// distance = Math.round(distance*100.0)/100.0; //Distance in double

				distanceMatrix[i][j] = distance;
				distanceMatrix[j][i] = distance;
			}
		}
		int printMatrix = 0; // If we want to print diastance matrix

		if (printMatrix == 1) {
			for (int i = 0; i <= NoOfCustomers; i++) {
				for (int j = 0; j <= NoOfCustomers; j++) {
					System.out.print(distanceMatrix[i][j] + "  ");
				}
				System.out.println();
			}
		}

		// Compute the greedy Solution
		System.out.println("Attempting to resolve Vehicle Routing Problem (VRP) for " + NoOfCustomers
				+ " Customers and " + NoOfVehicles + " Vehicles" + " with " + VehicleCap + " units of capacity\n");

		Solution s = new Solution(NoOfCustomers, NoOfVehicles, VehicleCap);

		s.GreedySolution(Nodes, distanceMatrix);

		s.SolutionPrint("Greedy Solution");

		s.IntraRouteLocalSearch(Nodes, distanceMatrix);

		s.SolutionPrint("Solution after Intra-Route Heuristic Neighborhood Search");

		s.GreedySolution(Nodes, distanceMatrix);

		s.InterRouteLocalSearch(Nodes, distanceMatrix);

		s.SolutionPrint("Solution after Inter-Route Heuristic Neighborhood Search");

		s.GreedySolution(Nodes, distanceMatrix);

		s.TabuSearch(TABU_Horizon, distanceMatrix);

		s.SolutionPrint("Solution After Tabu Search");

	}
}
