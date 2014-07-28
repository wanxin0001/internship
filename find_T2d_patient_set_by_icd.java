/*
Date: July, 27, 2014
In this file, it process /cac/dc/xw205/problem_after_2001_output.txt and ~/T2D_problem_key_by_icd_code.txt and /cac/dc/xw205/patient_interset.csv to get the list of patients who has T2D and find the first date to get t2d. 
The output file is /cac/dc/xw205/t2d_patient_set_by_icd.csv
the first col: patient_key;
the second col: the first date to get T2D
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


public class find_T2d_patient_set_by_icd {
	public static void main(String[] args) throws IOException, ParseException {
		BufferedReader brProb = new BufferedReader(new FileReader("/cac/dc/xw205/problem_after_2001_output.txt"));
		BufferedReader brPatientSet = new BufferedReader(new FileReader("/cac/dc/xw205/patient_interset.csv"));
		BufferedReader brIcd = new BufferedReader(new FileReader("../T2D_problem_key_by_icd_code.txt"));

		BufferedWriter bw = new BufferedWriter(new FileWriter("/cac/dc/xw205/t2d_patient_set_by_icd.csv"));

		HashSet<Integer> patients = new HashSet<Integer>();
		HashSet<Integer> icd = new HashSet<Integer>();
		Hashtable<Integer, Date> table = new Hashtable<Integer, Date>();
		
		String line ="";
		while((line = brPatientSet.readLine()) != null){
			patients.add(Integer.parseInt(line));
		}
		System.out.println("The patient num is: " + patients.size());

		while((line = brIcd.readLine()) != null) {
			icd.add(Integer.parseInt(line));
		}
		System.out.println("The T2D icd num is: " + icd.size());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		while((line = brProb.readLine()) != null) {
			String[] items = line.split("\t");
			int len = items.length;
			int patientKey = Integer.parseInt(items[1]);

			if(patients.contains(patientKey)) {
				int problemKey = Integer.parseInt(items[len - 1]);

				if(icd.contains(problemKey)) {
					
					Date date = sdf.parse(items[2]);
					if(table.containsKey(patientKey)) {
						if(table.get(patientKey).compareTo(date) > 0) {
							table.put(patientKey, date);
							//System.out.println(patientKey);
							//break;

						}
					}
					else {
						table.put(patientKey, date);

					}
				}

			}
		}

		for(Integer patient : table.keySet()) {
			bw.write(patient + "\t" + sdf.format(table.get(patient)) + "\n");
		}
		System.out.println("the t2d patient num: " + table.size());
		brProb.close();
		brIcd.close();
		brPatientSet.close();
		bw.close();


	}

}