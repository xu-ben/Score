import java.io.*;
import java.sql.*;
import java.util.*;

public class Problems2011 {
	static Connection connAccess = null;
	static Statement stmtAccess = null;
	static Connection connMySQL = null;
	static Statement stmtMySQL = null;

	public static void main(String[] args) {
		connectMySQL();
//		for (int i = 10; i < 19; i++) {
//			getACCodeOfAContestToDir(i);
//			System.gc();
//		}
		//getCodeToFilesFromAContest(24);
		getACCodeOfAContestToDir(23);
	}

	/**
	 * 将一次竞赛中所有用户Accepted的代码按用户分文件存放在指定的目录下
	 * 
	 * @param id
	 */
	private static void getACCodeOfAContestToDir(int id) {

		String[] namelist = getNameListOfAContest(id);
		String root = "H:\\tt";
		String dir;
		int k;

		Integer[] problemlist = getProblemList(id);
		String solution_id;
		ArrayList<String> ids;

		for (int i = 0; i < namelist.length; i++) {
			for (int j = 0; j < problemlist.length; j++) {
				dir = new String(root);
				if (problemlist.length == 1) {

				} else if (j == 0) {
					dir += "\\A题";
				} else if (j == 1) {
					dir += "\\B题";
				} else {
					dir += "\\C题";
				}
				File f = new File(dir);
				if (!f.exists()) {
					f.mkdir();
				}

				dir += "\\";

				k = 1;
				ids = getSolutionId(namelist[i], problemlist[j], k, id);
				if (ids == null || ids.size() == 0) {
					continue;
				}
				for (int ii = 0; ii < ids.size(); ii++) {
					solution_id = ids.get(ii);
					String code = getACodeBySolutionId(solution_id);
					String date = getDateOfASolution(solution_id);
					setCodeFile(dir, problemlist[j], date, "Accepted", code,
							namelist[i]);
				}

			}

			// solution_id = getFailedSolutionId(namelist[i], problemlist[j],
			// id);
		}
	}

	private static void getCodeToFilesFromAContest(int id) {

		String[] RESULTS = { "waiting", "Accepted", "presenting error",
				"Time Limit Exceed", "Memory Limit Exceed", "Wrong Answer",
				"Compile Error", "Runtime Error", "Judging",
				"System Error(Judge)", "Judge Delay", "Judge Error",
				"Output Limit Exceed", "Restrict Function",
				"System Error(File)", "保留",
				"RunTime Error(ARRAY_BOUNDS_EXCEEDED)" };
		String[] namelist = getNameListOfAContest(id);
		String root = "H:\\临时文件夹\\tt\\问题求解实验所有提交代码\\实验3\\";
		String dir;

		Integer[] problemlist = getProblemList(id);
		String solution_id;
		ArrayList<String> ids;

		for (int i = 0; i < namelist.length; i++) {
			dir = new String(root + namelist[i]);
			File f = new File(dir);
			if (!f.exists()) {
				f.mkdir();
			}

			dir += "\\";

			for (int j = 0; j < problemlist.length; j++) {
				for (int k = 0; k < RESULTS.length; k++) {
					ids = getSolutionId(namelist[i], problemlist[j], k, id);
					if (ids == null || ids.size() == 0) {
						continue;
					}
					for (int ii = 0; ii < ids.size(); ii++) {
						solution_id = ids.get(ii);
						String code = getACodeBySolutionId(solution_id);
						String date = getDateOfASolution(solution_id);
						setCodeFile(dir, problemlist[j], date, RESULTS[k],
								code, namelist[i]);
					}
				}

			}

			// solution_id = getFailedSolutionId(namelist[i], problemlist[j],
			// id);
		}
	}

	public static String getDateOfASolution(String solution_id) {
		try {
			String sql = new String(
					"select in_date from solution where solution_id = ?");
			PreparedStatement ps = connMySQL.prepareStatement(sql);
			ps.setString(1, solution_id);

			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			String ans = null;
			if (rs.next()) {
				ans = rs.getString(1);
				return ans;
			}
		} catch (SQLException se) {
			System.out.println("main sql" + se.toString());
			return null;
		}
		return null;
	}

	private static ArrayList<String> getSolutionId(String name, int pid, int k,
			int id) {
		ArrayList<String> ids = new ArrayList<String>();
		try {
			String sql = new String(
					"select solution_id from solution where result = ? and contest_id = ? and user_name = ? and problem_id = ?");
			PreparedStatement ps = connMySQL.prepareStatement(sql);
			ps.setInt(1, k);
			ps.setInt(2, id);
			ps.setString(3, name);
			ps.setInt(4, pid);

			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			String ans = null;
			while (rs.next()) {
				ans = rs.getString(1).trim();
				ids.add(ans);
			}
			return ids;
		} catch (SQLException se) {
			System.out.println("main sql" + se.toString());
			return null;
		}
	}

	public static void main_old(String[] args) {
		connectMySQL();
		Integer id = 14;
		/*
		 * String strusername = ""; String str = new String(""); str +=
		 * "*****************************************************************\r\n"
		 * ; str += "*******************Java程序设计实验" + (id - 9) +
		 * "代码存档*******************\r\n"; str +=
		 * "*****************************************************************\r\n\r\n"
		 * ; strusername = "090824210"; str += getErrorUser(strusername); str +=
		 * "\r\n"; str +=
		 * "\r\n*****************************************************************\r\n"
		 * ; str += "*******************Java程序设计实验" + (id - 9) +
		 * "代码存档*******************\r\n"; str +=
		 * "*****************************************************************\r\n"
		 * ; DBOperator.setFileText("G:\\t\\Java程序设计实验" + (id - 9) +
		 * "开小号存档.doc", str);
		 */

		String s = getCodeFromAContest(id);
		// System.out.println(s);
		Problems2011.setFileText("G:\\t\\Java程序设计实验" + (id - 9) + "代码存档.doc", s);

	}

	/**
	 * 利用用户名提取用户信息
	 */

	public static String getErrorUser(String username) {
		String str = "用户:\t\t" + username + "\r\n";
		try {
			String sql = new String(
					"select ip,time from login_log where user_name = ? and time between '2010-10-28 17:00:00.0' and '2010-10-28 22:00:00.0'");
			PreparedStatement ps = connMySQL.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				str = str + "登陆IP：\t\t" + rs.getString(1) + "\r\n";
				str = str + "登陆时间：\t\t" + rs.getString(2) + "\r\n";
			}
		} catch (Exception se) {
			System.out.println(se.toString());
		}
		return str;
	}

	public static void main1(String[] args) {
		connectMySQL();
		Scanner cin = new Scanner(System.in);
		String username = null, nickname = null;
		while (cin.hasNextLine()) {
			username = cin.nextLine();
			nickname = cin.nextLine();
			username = username.trim();
			nickname = nickname.trim();
			if (username == null || nickname == null) {
				break;
			}
			if (!nickname.equals("null")) {
				updateNickname(username, nickname);
			}
		}
	}

	public static void main3(String[] args) {
		connectMySQL();

		getNicknameOfUser();
	}

	public static void main2(String[] args) {
		boolean flagAccess = false;
		boolean flagMySQL = true;
		if (flagAccess) {
			connectAccess("F:\\problemsolveandprogramming.mdb");
			try {
				/*
				 * ResultSet rs = stmtAccess.executeQuery("select * from t1");
				 * rs.beforeFirst(); for (int i=0; i<3; i++){ rs.next(); String
				 * s = rs.getString(1); System.out.println(s); }
				 */
				String comment = "abcdefg";
				for (int i = 0; i < 1; i++) {
					comment += "abcdefg\r\n";
				}
				stmtAccess.executeUpdate("insert into t1 values(8, 'kkk', '"
						+ comment + "')");
				stmtAccess
						.executeUpdate("insert into t1 values(3, 'dddd', 'xyz')");
				stmtAccess
						.executeUpdate("insert into t1 values(4, 'eeee', 'uvw')");
				stmtAccess.close();

				System.out.println("OK");
			} catch (SQLException se) {
				System.out.println("main sql" + se.toString());
			}
		}

		if (flagMySQL) {
			connectMySQL();
			String s = getACodeBySolutionId("f6642fb628c2bcb60128c48879920004");
			System.out.println(s);
			// select * from solution_code where solution_id =
			// 'f6642fb628c2bcb60128c48879920004'
			// ResultSet rs =
			// stmtMySQL.executeQuery("select user_name from users");
			// ResultSet rs = stmtMySQL.executeQuery(command);
			// ResultSet rs =
			// stmtMySQL.executeQuery("select * from solution_code");
		}
	}

	public static void connectAccess(String accessPath) {
		String strAccess = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ="
				+ accessPath;
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			connAccess = DriverManager.getConnection(strAccess);
			stmtAccess = connAccess.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException sqle) {
			System.out.println("Acesss SQLException " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Access SQLException " + cnfe.getMessage());
		}
	}

	public static void connectMySQL() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Connection connection =
			// DriverManager.getConnection("jdbc:mysql://localhost:3306/acmhome",
			// "root", "root");
			connMySQL = DriverManager.getConnection(
					"jdbc:mysql://202.204.125.14:3306/acmhome", "root", "root");
			stmtMySQL = connMySQL.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static String getACodeBySolutionId(String solution_id) {
		try {
			ResultSet rs = stmtMySQL
					.executeQuery("select source_code from solution_code where solution_id = '"
							+ solution_id + "'");
			rs.beforeFirst();
			if (rs.next()) {
				String s;
				try {
					s = new String(rs.getBytes(1), "GBK");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return null;
				}
				return s;
			}
		} catch (SQLException se) {
			System.out.println("main sql" + se.toString());
			return null;
		}
		return null;
	}

	/**
	 * 根据指定竞赛号获得代码存档
	 * 
	 * @param id
	 * @return
	 */
	public static String getCodeFromAContest(Integer id) {
		String s = new String("");
		s += "*****************************************************************\r\n";
		s += "*******************Java程序设计实验" + (id - 9)
				+ "代码存档*******************\r\n";
		s += "*****************************************************************\r\n\r\n";

		String[] namelist = getNameListOfAContest(id);
		for (int i = 0; i < namelist.length; i++) {
			System.out.println(namelist[i] + "登陆IP" + getUserip(namelist[i]));
		}
		System.exit(0);
		// if (null == namelist) {
		// return "出错!!!";
		// }
		Integer[] problemlist = getProblemList(id);
		for (int i = 0; i < namelist.length; i++) {
			if (namelist[i].equals("090824412")) {
				continue;
			}
			if (namelist[i].equals("090824429")) {
				continue;
			}
			s += "\r\n----------------------------------------------------------------------\r\n";
			s += "用户:\t\t" + namelist[i] + "\r\n";
			String loginip = getUserip(namelist[i]);
			s += "登陆IP:\t\t" + loginip + "\r\n";
			int acnum = getAcNum(id.toString(), namelist[i]);
			s += "解决了 " + acnum + " 道题!\r\n\r\n";
			// 登录的IP

			// System.out.println(namelist[i]);
			for (int j = 0; j < problemlist.length; j++) {
				String solution_id = getAcSolutionId(namelist[i],
						problemlist[j], id);
				if (null != solution_id) {
					s += "\r\n解决的第" + (j + 1) + "道题的代码为:\r\n";
					s += getACodeBySolutionId(solution_id) + "\r\n";
					continue;
				}
				solution_id = getFailedSolutionId(namelist[i], problemlist[j],
						id);
				if (null != solution_id) {
					s += "\r\n第" + (j + 1) + "道题未成功解决!\r\n最后一次提交的错误代码为:\r\n";
					s += getACodeBySolutionId(solution_id) + "\r\n";
					continue;
				}
				s += "\r\n未曾提交解决第" + (j + 1) + "道题的代码!\r\n";
			}
			s += "\r\n----------------------------------------------------------------------\r\n";
		}
		// System.out.println(ss);
		// ;= getCodeFromAContest(4);
		s += "\r\n*****************************************************************\r\n";
		s += "*******************Java程序设计实验" + (id - 9)
				+ "代码存档*******************\r\n";
		s += "*****************************************************************\r\n";
		// select * from solution where contest_id = '4' and time != '-1'
		return s;
	}

	/**
	 * 利用用户名提取用户登陆IP地址
	 */

	public static String getUserip(String username) {
		String userip = null;
		try {
			String sql = new String(
					"select ip from login_log where user_name = ? and time between '2010-11-04 17:00:00.0' and '2010-11-04 22:00:00.0'");
			PreparedStatement ps = connMySQL.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception se) {
			System.out.println(se.toString());
		}
		return userip;
	}

	public static Integer[] getProblemList(Integer id) {
		try {
			String sql = new String(
					"select problem_id from contest_problem where contest_id = ? order by problem_id");
			PreparedStatement ps = connMySQL.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			ArrayList<Integer> list = new ArrayList<Integer>();
			while (rs.next()) {
				list.add(rs.getInt(1));
			}
			int len = list.size();
			Integer[] ans = new Integer[len];
			for (int i = 0; i < len; i++) {
				ans[i] = list.get(i);
			}
			return ans;
		} catch (Exception se) {
			System.out.println(se.toString());
			return null;
		}
	}

	/**
	 * 特定的用户在一场比赛中AC的题数
	 * 
	 * @param contest_id
	 * @param user_id
	 * @return
	 */
	public static int getAcNum(String contest_id, String user_id) {
		try {
			String sql = new String(
					"select accepts from contest_status where contest_id = ? and user_id = ?");
			PreparedStatement ps = connMySQL.prepareStatement(sql);
			ps.setString(1, contest_id);
			ps.setString(2, user_id);
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception se) {
			System.out.println(se.toString());
			return 0;
		}
		return 0;
	}

	public static String getFailedSolutionId(String user_name,
			Integer problem_id, Integer contest_id) {
		try {
			String sql = new String(
					"select solution_id from solution where contest_id = ? and user_name = ? and problem_id = ? order by in_date desc");
			PreparedStatement ps = connMySQL.prepareStatement(sql);
			ps.setInt(1, contest_id);
			ps.setString(2, user_name);
			ps.setInt(3, problem_id);
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			if (rs.next()) {
				return rs.getString(1).trim();
			}
		} catch (SQLException se) {
			System.out.println("main sql" + se.toString());
			return null;
		}
		return null;
	}

	/**
	 * 根据给定的用户名和题号和竞赛号，得到正确解决方案号
	 * 
	 * @param user_name
	 * @param problem_id
	 * @param contest_id
	 * @return
	 */
	public static String getAcSolutionId(String user_name, Integer problem_id,
			Integer contest_id) {
		try {
			String sql = new String(
					"select solution_id from solution where result = 1 and contest_id = ? and user_name = ? and problem_id = ?");
			PreparedStatement ps = connMySQL.prepareStatement(sql);
			ps.setInt(1, contest_id);
			ps.setString(2, user_name);
			ps.setInt(3, problem_id);

			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			String ans = null;
			if (rs.next()) {
				ans = rs.getString(1).trim();
				return ans;
			}
		} catch (SQLException se) {
			System.out.println("main sql" + se.toString());
			return null;
		}
		return null;
	}

	/**
	 * 根据竞赛号得到用户名序列(无重复)
	 * 
	 * @param i
	 * @return
	 */
	public static String[] getNameListOfAContest(Integer id) {

		ArrayList<String> namelist = new ArrayList<String>();
		try {
			String sql = new String(
					"select user_id from contest_status where contest_id = ? order by user_id");
			PreparedStatement ps = connMySQL.prepareStatement(sql);
			ps.setString(1, id.toString());
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				String name = rs.getString(1).trim();
				namelist.add(name);
			}
		} catch (SQLException se) {
			System.out.println("main sql" + se.toString());
			return null;
		}
		String[] rets = new String[namelist.size()];
		for (int index = 0; index < namelist.size(); index++) {
			rets[index] = namelist.get(index);
		}
		return rets;
	}

	/**
	 * 写磁盘文件(替换文件内容)的函数
	 * 
	 * @param filePath
	 * @param text
	 * @return
	 */
	public static boolean setFileText(String filePath, String text) {
		File f = new File(filePath.trim());
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				return false;
			}
		}
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(text);
			fw.flush();
			fw.close();
		} catch (IOException ioe) {
			return false;
		}
		return true;
	}

	public static String getMyTime(String date) {
		int index = date.indexOf(' ');
		if (index == -1) {
			return null;
		}
		date = date.substring(index + 1);
		date = date.replaceAll(":", "-");
		// System.out.println(date);
		// System.exit(0);
		return date.trim();
	}

	/**
	 * 将代码(按文件)存到磁盘中
	 * 
	 * @param filePath
	 * @param text
	 * @return
	 */
	public static boolean setCodeFile(String dir, int problemid, String date,
			String result, String code, String username) {
		String filePath = new String(dir + username + "_");
		switch (problemid) {
		case 1095:
			filePath += "A题_";
			break;
		case 1096:
			filePath += "B题_";
			break;
		case 1076:
			filePath += "C题_";
			break;
		}
		filePath += getMyTime(date);
		filePath += "_" + result + ".cpp";
		File f = new File(filePath.trim());
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.out.println(filePath + "\n竹仔");
				System.exit(0);
				// System.out.println(code);
				return false;
			}
		}
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(code);
			fw.flush();
			fw.close();
		} catch (IOException ioe) {
			return false;
		}
		return true;
	}

	public static String latin1ToGBK(String str) {
		try {
			String temp_p = str;
			byte[] temp_t = temp_p.getBytes("ISO-8859-1");
			String temp = new String(temp_t, "GBK");
			return temp;
		} catch (UnsupportedEncodingException ex) {
			System.out.println(ex);
			return "";
		}

	}

	public static String GBKToLatin1(String str) {
		if (str == null) {
			str = "";
		} else {
			try {
				str = new String(str.getBytes("GBK"), "ISO-8859-1");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * 根据指定竞赛号获得代码存档
	 * 
	 * @param id
	 * @return
	 */
	public static String getNicknameOfUser() {
		String s = null;
		try {
			ResultSet rs = stmtMySQL
					.executeQuery("select user_name,nickname from users");
			rs.beforeFirst();
			while (rs.next()) {
				System.out.println(rs.getString(1));
				s = rs.getString(2);
				if (s == null || s.trim().equals("")) {
					System.out.println("null");
				} else {
					System.out.println(s);
				}
			}
		} catch (SQLException se) {
			// System.out.println("main sql" + se.toString());
			return null;
		}
		return s;
	}

	public static boolean updateNickname(String username, String nickname) {
		String sql = new String("update users set nickname = '");
		sql += nickname + "' ";
		sql += "where user_name = '";
		sql += username + "'";
		// System.out.println(sql);
		try {
			stmtMySQL.executeUpdate(sql);
		} catch (SQLException se) {
			System.out.println("main sql" + se.toString());
			System.out.println(sql);
			return false;
		}
		return true;
	}

}
