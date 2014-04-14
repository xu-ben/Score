import java.util.*;

public class Quaternion implements Comparable<Object>{
	String userName;
	int rank;	
	int count;
	int score;
	public String toString(){
		return userName + ", 排名:" + rank + ", 做对:" + count + ", 分数:" + score;
	}
	public void show(){
		System.out.println(this.toString());
	}
	Quaternion(){
	}
	Quaternion(String userName, int rank, int count, int score){
		this.userName = userName;
		this.rank = rank;
		this.count = count;
		this.score = score;
	}
	Quaternion(String line){
		line = line.trim();
		String[] sub = line.split(" ");
		rank = Integer.parseInt(sub[0].trim());
		userName = sub[1].trim();
		count = Integer.parseInt(sub[2].trim());
		score = 0;
	}
	public int compareTo(Object obj){
		Quaternion that = (Quaternion)obj;
		if (this.rank > that.rank)
			return 1;
		if (this.rank < that.rank)
			return -1;
		return 0;
	}
	public static Quaternion findQuaternion(String pile, String user){//user e.g., n080824117
		String userName = user;
		int nameLen = userName.length();
		int pos = pile.indexOf(userName);
		if (pos < 0)
			return null;
		int from = pos - 3;
		int to = pos + nameLen + 2;
		String line = pile.substring(from, to+1);
		Quaternion result = new Quaternion(line);
		return result;
	}
	public static Quaternion[] findQuaternionGroup(String pile, String[] users){
		int amount = users.length;
		Quaternion[] aQuaternion = new Quaternion[amount];
		for (int i=0; i<amount; i++)
			aQuaternion[i] = new Quaternion(users[i], 1000000, 0, 0);
		for (int i=0; i<amount; i++){
			Quaternion temp = Quaternion.findQuaternion(pile, users[i]);
			if (temp == null)
				continue;
			aQuaternion[i].rank = temp.rank;
			aQuaternion[i].count = temp.count;
			aQuaternion[i].score = temp.score;
		}
		return aQuaternion;
	}
	public static int[][] parseStandardLine(String standardLine){
		standardLine = standardLine.trim();
		String[] aSub = standardLine.split("#");
		int standardCount = aSub.length;
		int[][] result = new int[2][standardCount];
		for (int i=0; i<standardCount; i++){
			aSub[i] = aSub[i].trim();
			aSub[i] = aSub[i].substring(1, aSub[i].length()-1);
			String[] astrNum = aSub[i].split(",");
			result[0][i] = Integer.parseInt(astrNum[0].trim());
			result[1][i] = Integer.parseInt(astrNum[1].trim());
		}
		return result;
	}
	
	public static void dealWithQuaternion(Quaternion[] aQuaternion, String standardLine){
		int userCount = aQuaternion.length;
		Arrays.sort(aQuaternion);
		for (int i=0; i<aQuaternion.length; i++)
			aQuaternion[i].rank = i;
		int[][] boundary = Quaternion.parseStandardLine(standardLine);
		int problemCount = boundary[0].length;
		for (int i=0; i<problemCount; i++){
			int low = boundary[0][i];
			int high = boundary[1][i];
			int from = -1;
			for (int j=0; j<userCount; j++){
				if (aQuaternion[j].count == i){
					from = j;
					break;
				}
			}
			int to = 1000000;
			for (int j=userCount-1; j>=0; j--){
				if (aQuaternion[j].count == i){
					to = j;
					break;
				}
			}
			if (from < 0)
				continue;
			aQuaternion[from].score = high;
			for (int j=from+1; j<=to; j++){
				aQuaternion[j].score = (int)Math.round(1.0 * high - 1.0 * (high - low) * (j-from) / (to - from));
			}
		}
	}
	public static String createStandardSingle(int which, String standardLine){
		String result = "第 " +  which + " 次实验共有 ";
		int[][] boundary = Quaternion.parseStandardLine(standardLine);
		int problemCount = boundary[0].length - 1;
		result += problemCount + " 道题目\r\n";
		for (int i=0; i<problemCount+1; i++){
			result += "  做对 " + i + " 道题目的同学按照排名先后均匀分布在得分区间 [" + boundary[0][i] + ", " + boundary[1][i] + "] 之间。\r\n";
		}
		return result;
	}
	public static void main(String[] args){
		String result = Quaternion.createStandardSingle(2, "[0, 0] # [60, 63] # [64, 69] # [ 70, 71] # [72, 100]");
		System.out.println(result);
	}
}