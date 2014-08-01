/*
	Copyright 2014 (C) Optimal Solution Inc 
 	Created on : July 27, 2014
 	Author     : Xin Wan

	In this file, it process /emerald/xw205/paitent_after_2001_output.txt(observation_f_after_2001) and problem_after_2001_output.txt (problem_f_after_2001_tmp table). It will generate a file contains the patient interset list. 
	The output file is patient_interset.csv

	Result:
	patient num in observation_f_after_2001 is: 30406867
	patient num in problem_after_2001 is: 31162609
	The interSet num is: 29013106

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

public class extracted_patients_set {
	public static void main(String[] args) throws IOException {
		BufferedReader brObs = new BufferedReader(new FileReader("/emerald/xw205/paitents_after_2001_output.txt"));
		BufferedReader brProb = new BufferedReader(new FileReader("/emerald/xw205/problem_after_2001_output.txt"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("/emerald/xw205/patient_interset.csv"));


		HashSet<Integer> obsSet = new HashSet<Integer>();
		HashSet<Integer> probSet = new HashSet<Integer>();
		Hashtable<Integer,Integer> interSet = new Hashtable<Integer, Integer>();

		String line;
		int patientKey = 0;

		while((line = brObs.readLine()) != null) {
			String[] items = line.split("\t");
			patientKey = Integer.parseInt(items[1]);
			obsSet.add(patientKey);
		}
		System.out.println("patient num in observation_f_after_2001 is: " + obsSet.size());
		
		while((line = brProb.readLine()) != null) {
			String[] items = line.split("\t");
			patientKey = Integer.parseInt(items[1]);
			probSet.add(patientKey);
		}
		System.out.println("patient num in problem_after_2001 is: " + probSet.size());

		for(Integer item : probSet) {
			interSet.put(item, 0);
		}

		for(Integer item : obsSet) {
			if(interSet.containsKey(item)) {
				interSet.put(item, 1);
			}
		}

		int num = 0;
		Set<Integer> set = interSet.keySet();
    	for(int item : set) {
    		if(interSet.get(item) == 1) {
    			num++;
    			bw.write(item + "\n");
    		}
    	}

    	System.out.println("The interSet num is: " + num);

    	brProb.close();
    	brObs.close();
    	bw.close();


	}
}

