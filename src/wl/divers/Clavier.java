package wl.divers;
import java.util.Hashtable;
import java.util.Scanner;
public class Clavier {
	static Scanner sc = new Scanner(System.in);
	static Hashtable<String, String> mapCode;

	public Clavier(Hashtable<String, String> pMapCode){
		mapCode=pMapCode;
	}
	
	public static String nextLine(int code){
		String msg="";
		if (mapCode.containsKey(""+code)){
			msg=mapCode.get(code);
			System.out.println("code "+code+" pre-rentré, sa valeur est "+mapCode.get(""+code));
			return mapCode.get(""+code);
		}
		else{msg=sc.nextLine();}
		return msg;
	}
	
	
	public static String next(int code){
		String msg="";
		if (mapCode.containsKey(""+code)){
			msg=mapCode.get(code);
			System.out.println("code "+code+" pre-rentré, sa valeur est "+mapCode.get(""+code));
			return mapCode.get(""+code);
		}
		else{msg=sc.nextLine();}
		return msg;
	}
	
	public static int nextInt(int code){
		int msg;
		if (mapCode.containsKey(""+code)){
			System.out.println("code "+code+" pre-rentré, sa valeur est "+mapCode.get(""+code));
			return Integer.parseInt(mapCode.get(""+code));
		}
		else{msg=sc.nextInt();
		return msg;
		}
	}
	
	public static void GetCodeEtReponse(Hashtable<String, String> pMapCode){
		mapCode=pMapCode;

	}	
	
}



//code
/*
1:136 client
2: 189 cient
3 :115 receivers
4 : 96 wolflan
5 : 141 wolflan



*/