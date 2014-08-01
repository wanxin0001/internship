/*
	Copyright 2014 (C) Optimal Solution Inc 
 	Created on : July 27, 2014
 	Author     : Xin Wan
 	In order to implement my idea, I will first extract the records in different time period. And then I create a matrix. The row represents all T2D patients. The column represents that how many times they measure this feature in this time period. In this way, we get two matrix and give us the observation stats information for two period.
	
	One time period is from 2 months before their first date that marked T2D to 2 months after their first date that marked T2D. In this period, all record values will respresent that they are T2D patients.
	
	The second time period is from 14 months before their first date that marked T2D to 10 months before their first date marked T2D. In this period, all record values will show the sistuation of these patients one year ago.
 	
 	The input file is /emerald/xw205/patient_table.csv, /emerald/xw205/feature_table.csv.
	The output file is /emerald/xw205/features_stats_before_1Y_4M.csv, features_stats_Now_4M.csv

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


public class feature_stats_for_t2d {

 	public static void main(String[] args) throws IOException, ParseException{
		//BufferedReader brT2D = new BufferedReader(new FileReader("/emerald/xw205/obs_for_T2D_all.csv"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("/emerald/xw205/features_stats_before_2Y_4M.csv"));
		BufferedReader brT2D = new BufferedReader(new FileReader("/emerald/xw205/patient_table.csv"));
		BufferedReader brFeature = new BufferedReader(new FileReader("/emerald/xw205/feature_table.csv"));
		
		//BufferedWriter bwPatient = new BufferedWriter(new FileWriter("/emerald/xw205/patient_table.csv"));
		//BufferedWriter bwFeature = new BufferedWriter(new FileWriter("/emerald/xw205/feature_table.csv"));
		Hashtable<Integer, Integer> featureSet = new Hashtable<Integer, Integer>();
		Hashtable<Integer, Integer> patientsMap = new Hashtable<Integer, Integer>();


		String line = "";
		int index = 0;
		int num = 0;
		/*
		while((line = brT2D.readLine()) != null) {
			String[] items = line.split("\t");
			//System.out.println(items[2]+ ",");
			if(!featureSet.containsKey(Integer.parseInt(items[2]))){
				System.out.println(index + "\t" + items[2]);

				featureSet.put(Integer.parseInt(items[2]), index);
				bwFeature.write(index + "\t" + items[2] + "\n");
				index++;
				
			}
			
			if(!patientsMap.containsKey(Integer.parseInt(items[1]))){
				//System.out.println(items[2]+ ",,,");
				patientsMap.put(Integer.parseInt(items[1]), num);
				bwPatient.write(num + "\t" + items[1] + "\n");
				num++;
				//break;
			}
		}*/
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
		
		int[][] matrix = new int[patientNum][featureNum + 1];
		for(Integer patient : patientsMap.keySet()) {
			matrix[patientsMap.get(patient)][0] = patient;
		}

		brT2D = new BufferedReader(new FileReader("/emerald/xw205/obs_for_T2D_all.csv"));
		int i = 0;
		while((line = brT2D.readLine()) != null) {
			String[] items = line.split("\t");
			if(Integer.parseInt(items[6]) >= 670 && Integer.parseInt(items[6]) <= 790){
				if(i==0){
					System.out.println(items[6]);
				}
				matrix[patientsMap.get(Integer.parseInt(items[1]))][featureSet.get(Integer.parseInt(items[2])) + 1]++;
				i++;
			}
		}
		System.out.println("Add records num: " + i);
		
		for(int j = 0; j < patientNum; j++) {
			for(int k = 0; k < featureNum; k++) {
				bw.write(matrix[j][k] + "\t");
			}

			bw.write(matrix[j][matrix[0].length - 1] + "\n");
		}

		brT2D.close();
		bw.close();
		//bwPatient.close();
		//bwFeature.close();

		brFeature.close();


	}
}