/*
Date: July 28th, 2014
In this file, it processes /emerald/xw205/features_stats_before_1Y_4M.csv, patient_table.csv, feature_table.csv. We will build 100 trees. For each tree, we will randomly choose 5 features and check how many patients have these records for both dates.
*/

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class random_feature_selection {
	public static void main(String[] args) throws IOException {
		
		BufferedReader brT2D = new BufferedReader(new FileReader("/emerald/xw205/patient_table.csv"));
		BufferedReader brFeature = new BufferedReader(new FileReader("/emerald/xw205/feature_table.csv"));
		BufferedReader brMatrix = new BufferedReader(new FileReader("/emerald/xw205/features_stats_before_1Y_4M.csv"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("/emerald/xw205/random_feature.csv"));

		Hashtable<Integer, Integer> featureSet = new Hashtable<Integer, Integer>();
		Hashtable<Integer, Integer> patientsMap = new Hashtable<Integer, Integer>();

		String line = "";
		while((line = brFeature.readLine()) != null) {
			String[] items = line.split("\t");
			//System.out.println(items[2]+ ",");
			featureSet.put(Integer.parseInt(items[1]), Integer.parseInt(items[0]));
		}
		while((line = brT2D.readLine()) != null) {
			String[] items = line.split("\t");
			//System.out.println(items[2]+ ",");
			patientsMap.put(Integer.parseInt(items[1]), Integer.parseInt(items[0]));
		}

		int featureNum = featureSet.size();
		int patientNum = patientsMap.size();
		System.out.println("The total feature num: " + featureNum);
		System.out.println("The total patient num: " + patientNum);

		//read Matrix
		int row = 0;
		int[][] matrix = new int[patientNum][featureNum + 1];
		while((line = brMatrix.readLine()) != null) {
			String[] items = line.split("\t");
			int len = items.length;
			for( int loc = 0; loc < len; loc++) {
				matrix[row][loc] = Integer.parseInt(items[loc]);
				//System.out.print(matrix[row][loc] + " ");
			}
			row++;
			//System.out.print("\n");
		}

		int fieldNum = featureAvailable(matrix);
		System.out.println("field num: " + fieldNum);
		//Generate the random features
		int treeNum = 100;
		int featuresForEachTree = 3;

		for (int i = 0; i < treeNum; i++) {
			Random randomGenerator = new Random();
			ArrayList<Integer> chosenFeature = new ArrayList<Integer>();
		    for (int idx = 0; idx < featuresForEachTree; idx++){
		      int randomInt = randomGenerator.nextInt(featureSet.size());
		      chosenFeature.add(randomInt);
		      
		    }
		    //chosenFeature.add(-1);
		    //System.out.println(matrix[1][0]);
		    int patientNumForTree = featureSelection(chosenFeature, matrix);
		    bw.write("Tree " + i + ": " + "\t");
	      	for(int j = 0; j < chosenFeature.size(); j++) {
	      		bw.write(chosenFeature.get(j) + "\t");
	      	}
	      	bw.write(patientNumForTree + "\n");
		}
		
		brMatrix.close();
		brFeature.close();
		brT2D.close();
		bw.close();


	}

	public static int featureSelection(ArrayList<Integer> chosenFeature, int[][] matrix) {
		//System.out.println(matrix[1][0]);
		//System.out.println(chosenFeature.get(0));
		int num = 0;
		int patientsNum = matrix.length;
		int featureSize = chosenFeature.size();
		for(int pos = 0; pos < patientsNum; pos++) {
			boolean flag = true;
			for(int index = 0; index < featureSize; index++) {
				//System.out.println(matrix[pos][1 + chosenFeature.get(index)]);
				if(matrix[pos][1 + chosenFeature.get(index)] == 0) {
					flag = false;
					break;
				}
			}
			if(flag) {
				num++;
			}
		}

		return num;
	}

	public static int featureAvailable(int[][] matrix) {
		int patientsNum = matrix.length;
		int featuresNum = matrix[0].length;
		int num = 0;
		for(int i = 1; i < featuresNum; i++) {
			boolean flag = false;
			for(int j = 0; j< patientsNum; j++) {
				if(matrix[j][i] > 0) {
					flag = true;
					break;
				}
			}

			if(flag) {
				num++;
			}
		}
		return num;
	}
}