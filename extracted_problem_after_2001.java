/*
Date: July, 24, 2014
In this file, it process problem_f and extract the records after 2001.
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

public class extracted_problem_after_2001 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("/cac/u01/xw205/problem_f_all/input.txt"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("/cac/u01/xw205/problem_f_all/paitents_after_2001_output.txt"));

		String line;
		while((line = br.readLine()) != null) {
			String[] items = line.split("\001");
			if(!items[5].equals("null") && Integer.parseInt(items[5].substring(0,4)) > 2000) {
				String line1 = items[1] + "\t" + items[2] + "\t"  + items[5] + "\t"  + items[6] + "\t"  + items[7] + "\t"  + items[8] + "\t"  + items[10] + "\t"  + items[11] + "\t" +  items[12] + "\t"  + items[13] + "\t"  + items[14] + "\t"  + items[3] + "\n";
				bw.write(line1);
			}
			
		}

		br.close();
		bw.close();
	}
}