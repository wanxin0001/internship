/*
Date: July 27th, 2014
In this file, it process /emerald/xw205/paitents_after_2001_output.txt and /cac/u01/xw205/age.csv and t2d_patient_set_by_icd.csv.
The purpose of this file is to get all observation records after the first date that the patients find T2D. It also gives the specific num of days after the first date and transfer age_key to age.
The output file is obs_for_T2D_before_first_date.csv 
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
import org.joda.time.DateTime;
import org.joda.time.Days;

public class find_all_t2d_records_in_obs_after_first_date {

 	public static void main(String[] args) throws IOException, ParseException{
		//read files
		BufferedReader brObs = new BufferedReader(new FileReader("/emerald/xw205/paitents_after_2001_output.txt"));
		BufferedReader brAge = new BufferedReader(new FileReader("/cac/u01/xw205/age.csv"));
		BufferedReader brIcd = new BufferedReader(new FileReader("/cac/u01/xw205/t2d_patient_set_by_icd.csv"));

		BufferedWriter bw = new BufferedWriter(new FileWriter("/emerald/xw205/obs_for_T2D_after_first_date.csv"));

		//save firstdate to hashtable
		Hashtable<Integer, Date> t2dTable = new Hashtable<Integer, Date>();
		Hashtable<Integer, Integer> ageTable = new Hashtable<Integer, Integer>();

		String line = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		while((line = brIcd.readLine()) != null) {
			String[] items = line.split("\t");
			Date date = sdf.parse(items[1]);
			t2dTable.put(Integer.parseInt(items[0]), date);
		}
		System.out.println("T2D patient num: " + t2dTable.size());

		while((line = brAge.readLine()) != null) {
			String[] items = line.split("\t");
			ageTable.put(Integer.parseInt(items[0]), Integer.parseInt(items[1]));
		}
		System.out.println("Age num: " + ageTable.size());

		//process observation_f record
		int num = 0;
		while((line = brObs.readLine()) != null) {
			String[] items = line.split("\t");
			num++;
			int patientKey = Integer.parseInt(items[1]);
			if (t2dTable.containsKey(patientKey)) { 
				//System.out.println("111");
				Date recordDate = sdf.parse(items[4]);
				if(t2dTable.get(patientKey).compareTo(recordDate) <= 0) {
					//System.out.println("222");
					int days = Days.daysBetween(new DateTime(recordDate), new DateTime(t2dTable.get(patientKey))).getDays();
					bw.write(items[0] + "\t" + items[1] + "\t" + items[2] + "\t" + items[3] + "\t" + items[4] + "\t" + ageTable.get(Integer.parseInt(items[5])) + "\t" + days + "\n");
					
					
					
				}
			} 
			if(num % 500000 == 0) {
						System.out.println("finish: " + num );
			}
		}

		//System.out.println("observation num before first date: " + num);


		brIcd.close();
		brObs.close();
		brAge.close();
		bw.close();
	}
}