package wl;

import java.io.*;

 import java.util.ArrayList;

import java.util.Scanner;

//Ceci importe toutes les classes du package java.util

import java.util.*;

/**

 * Simulation de la commande ls de linux
   en definissant des arguments
   * 
   * obtenirfichier(String chaine) chaine de type:
   *  caractere echappement: ' (pour le -) 
   * 	ex1: /home/user/dossier
   *    ex2: /home/user/dossier -name a.java -perm r -hidden
   *	ex3: /home/user/dossier !-name b.java !-perm w 
	retourne une array liste de File avec comme dernier element la longeur a enlevr pour avoir le chemin relatif 
 */

public class LsRep
 {
	static ArrayList<File> liste = new ArrayList<File>();//liste de dossier
	static ArrayList<File> liste1 = new ArrayList<File>();//liste de fichier
	static String[] ListeArgs;
	static long[] tablongargs = new long[15];
	static boolean checkarg;
	static int choixmethode;
	static int niveau;
	static int[] TabTypeArg = {0,0,0,0,0,0,0};//7 possibilites d arg


    public static File trouveAbsolu(String chemin ) {
		File Ftmp = new File(chemin);
		String fin;
		if (!Ftmp.isAbsolute()){// si chemin est relatif le mettre en absolu
							fin= ""+System.getProperty("user.dir") + File.separator +chemin;
							Ftmp= new File(fin);
		}
		return Ftmp;
	}
	


    public static boolean existe(File file) {
		return 	file.exists() ;
	}
	
	
	
	public static int compteurChar(String str, char ch) 
	{
  	int compteur = 0;                  
  	for (int i = 0; i < str.length(); i++) 
    if (str.charAt(i) == ch)             
       compteur++;                         
  	return compteur;   
	}	
	
	
	
    public static ArrayList<File> trier(ArrayList<File> ListeaTrier) {
		File Ftmp;
		//int lgListeaTrier=ListeaTrier.size();
		ArrayList<String> listetrier2 = new ArrayList<String>() ;
		ArrayList<String> tmp = new ArrayList<String>() ;
		
		//
		for (File f : ListeaTrier){
			listetrier2.add(f.getPath());
		}	
		ListeaTrier.clear();
		Set<String> mySet = new HashSet<String>(listetrier2);
 
		// Creer une Nouvelle ArrayList a partir de Set
		ArrayList<String> listetrier3 = new ArrayList<String>(mySet);
		Collections.sort(listetrier3);
		for (String s : listetrier3){
			Ftmp= new File(s);
			ListeaTrier.add(Ftmp);
		}	
	return ListeaTrier;
	}






    static boolean verifargs(File f) {// controle les arguments
			boolean bon=true;
			for (int i=0;i<TabTypeArg.length;i++){//ici 7	
				if (TabTypeArg[i]==1){
					bon=VerifArg(i,ListeArgs[i],f);
				}
				if (TabTypeArg[i]==2){
					bon=!VerifArg(i,ListeArgs[i],f);
				}	
				
				if (TabTypeArg[i]==0){
					//pas d arg
				}
				
				
			}
		return bon;		
	}
		
	static boolean VerifArg(int type,String valeur,File f){
		//ex: 1( 1 -> nom),fichier.txt	
		switch (type){
			case 0:
				return VerifieNom(valeur,f.getName());
			case 1:
				return VerifiePerm(valeur,f);
			case 2:
				return VerifieHide(f);			
			case 3:
				return VerifieSize(valeur,f);	
			case 4:
				return VerifieCtime(valeur,f);
																				
		}
		return false;
	}
		
		
		
	
	static boolean VerifieNom(String nomfichier,String nomfichierentre) {
		boolean bool=false;
		////boolean prevalide=true;
		//int position;	int position2=900;

		//System.out.println("nom:"+nomfichier+"voulu"+nomfichierentre);	
		if (nomfichier.equals(nomfichierentre)){bool=true;}
		//else{
			//if (nomfichierentre.indexOf('*')!=-1){
				//Tabsplit=nomfichierentre.split("*");
				//for (String j : Tabsplit){
					//position=nomfichier.indexOf(j);
					//if (position==-1 || (position2< position) ){return false;}
					//position2=position;
					
				//}
			//}
		//}
			
		return bool;
	}	
	
	
	
	static boolean VerifiePerm(String valeur,File f){
		boolean bon=true;
		switch (valeur){
			case "r":
				return f.canRead();
			case "w":
				return f.canWrite();
			case "x":
				return f.canExecute();
		
		}
		return true;
	}
		
	static boolean VerifieHide(File f){
		return f.isHidden();
	}

	static boolean VerifieSize(String valeur, File f){
		String liste[] = {valeur,"-1"};
		if (valeur.indexOf(",")!= -1){
			liste=valeur.split(",");
		}
		
		if (f.length() >= Long.parseLong(liste[0])){
			if (Long.parseLong(liste[1])!=-1){
				if (f.length() <= Long.parseLong(liste[1])){		
					if (Long.parseLong(liste[0])<=Long.parseLong(liste[1])){return true;}
				}
			}
			else{
					return true;
			}		
		}
		return false;
	}


	static boolean VerifieCtime(String valeur, File f){
		String liste[] = {valeur,"-1"};
		if (valeur.indexOf(",")!= -1){
			liste=valeur.split(",");
		}
		
		if (f.lastModified()> Long.parseLong(liste[0])){
			if (liste[1]!="-1"){
				if (f.lastModified()  < Long.parseLong(liste[1])){		
					if (Long.parseLong(liste[0])<Long.parseLong(liste[1])){return true;}
				}
			}
			else{
					return true;
			}		
		}
		return false;
	}
	
	
	

    static int walk( String path ) {// fonction de recursivite
        File root = new File( path );
        if (root.isFile()){
			liste1.add(root.getAbsoluteFile());
		}
        File[] list = root.listFiles();

        if (list == null) return 1 ;

        for ( File f : list ) {

			try{
            if ( f.isDirectory() ) {
                walk( f.getAbsolutePath() );

                if (choixmethode==0){
                liste.add(f.getAbsoluteFile() );
				}	
            }
            else {
				if (checkarg){
					if (verifargs(f)){
						if (choixmethode==1){
							liste1.add(f.getAbsoluteFile() );
						}
					}
				}
				else{liste1.add(f.getAbsoluteFile() );}
            }
				}catch(Exception e){System.out.println(e.getMessage());}
            
        }return 0;
    }
    
    
    
    static ArrayList<String> tabToArray(String[] tab){
		ArrayList<String> arrayTab = new ArrayList<String>() ;

		for (String s: tab){
			arrayTab.add(s);
		}
		return arrayTab;
	}
    
    
        
    
    
    
    static ArrayList<String> splitChaine(String chaine){
		String[] chaineSplitter = new String[1] ;
		ArrayList<String> chaineSplitterFin = new ArrayList<String>() ;

		int positionDernierQuote;
		String chaineTmp;
		String chaineTmp1;
		
	if (chaine.indexOf("-")==-1){//ex "tt"
		if (chaine.charAt(0)=='\''){
			positionDernierQuote=chaine.lastIndexOf('\'');
			if (positionDernierQuote!=-1){
				if (chaine.charAt(0)=='\''){
					if (positionDernierQuote>2){
						chaine.substring(1,positionDernierQuote);// pour transformer 'tt' en tt
					}
				}
			}else{System.out.println("' de fin manque");}
		}
		chaineSplitter[0]=chaine;
		return tabToArray(chaineSplitter);
	}
	else{
		chaineTmp=chaine;
		if (chaine.indexOf("\'")==-1){//pas protege
			int debutArgs=chaine.indexOf("-");
			int debutArgs2=chaine.indexOf("!");
			if (debutArgs2>debutArgs){
				debutArgs=debutArgs2;
			}
			String nom= chaine.substring(debutArgs);
			boolean dernierCharEspace=true;
			int positionDernierEspace;
			while (dernierCharEspace){
				positionDernierEspace=nom.lastIndexOf(' ');
				if (positionDernierEspace==nom.length()){
					nom.substring(nom.length());
				}
				else{
					dernierCharEspace=false;
				}
			}
			chaineTmp=chaine.substring(debutArgs-1);
			chaineSplitter=chaineTmp.split(" ");
			chaineSplitterFin = tabToArray(chaineSplitter);
			chaineSplitterFin.add(0,nom);
			return chaineSplitterFin;
			
			
			
		}	
		else{
			positionDernierQuote=chaine.lastIndexOf('\'');
			String nom=chaine.substring(1,positionDernierQuote);
			chaineTmp=chaine.substring(positionDernierQuote);
			chaineSplitter=chaineTmp.split(" ");
			chaineSplitterFin=tabToArray(chaineSplitter);
			chaineSplitterFin.add(0,nom);
			return chaineSplitterFin;
		}
	}
				
		
	
    
}
    
    
    
    
    

    public static ArrayList<File> obtenirdossier(String chaine) {
		liste= new ArrayList<File>();
		ArrayList<String> ChaineDecoupe = new ArrayList<String>();
		ChaineDecoupe=splitChaine(chaine);
		String nom=ChaineDecoupe.get(0);
		
		liste.clear();
		choixmethode=0;
		niveau=compteurChar(nom,File.pathSeparatorChar);
		try{
		File Ftmp;
		int lgparentchemin=2;
		String tmp;
		int lg;

		File origine=trouveAbsolu(nom);
		if (!existe(origine) || nom.length() <1){
			System.out.println("erreur,fichier introuvable");
			return liste;
		}
		

        LsRep fw = new LsRep();
        if (origine.isDirectory() ){
			liste.add(origine.getAbsoluteFile() );
		}
        fw.walk(origine.getCanonicalPath() );
        
        for (int k=0;k<=niveau;k++){     
			origine=trouveAbsolu(origine.getParent());
        }
		lg=origine.getCanonicalPath().length();
		for (int i=0;i<liste.size();i++){
			tmp=liste.get(i).getCanonicalPath();//.substring(lg+1);
			Ftmp= new File(tmp);
			liste.set(i,Ftmp);
		}
		liste=trier(liste);
		String str= Integer.toString(lg);
		File lg2 = new File(str);
		liste.add(lg2);
		
		 }catch(Exception e){System.out.println(e.getMessage());}

		return liste;
	}




    public static Long obtenirlongarg(String chaine) {
	Long MonLong=(long) 0;
	try { MonLong=Long.parseLong(chaine.substring(1));}catch(Exception e){System.out.println("Argument faux ?");}
	return MonLong;
	}




    public static ArrayList<File> obtenirfichier(String chaine) {
		
		if (chaine.indexOf("file:///")!=-1){
			chaine=chaine.substring(7);
		}
		ArrayList<String> chaineDecoupe = new ArrayList<String>();
		chaineDecoupe=splitChaine(chaine);
		ArrayList<File> lfin = new ArrayList<File>();;

		lfin=obtenirfichiersuite(chaineDecoupe);

	return lfin;
	}





    public static ArrayList<File> obtenirfichiersuite(ArrayList<String> chaine) {
		//for (String j : chaine){System.out.println(j+":");};
		ArrayList<File> erreur =new ArrayList<File>();
		liste1 = new ArrayList<File>();
		liste1.clear();
		int nbArg=chaine.size()-1;
		choixmethode=1;
		int numarg;
		String nom;//emplacement ou chercher
		//String tab[] = new String [chaine.size()];
		//String[] ssListeSpliter= new String [2];
		//String s2;
		//String stmp="!";
		//boolean commut=false;
		
		nom=chaine.get(0);

		File origine=trouveAbsolu(nom);
		
		if (!existe(origine) || nom.length() <1){
			System.out.println("erreur NOT FOUND :(");
			return liste1;
		}
		
		
	
		if (nbArg>=1){checkarg=true;}else{checkarg=false;}//verifie si il y a des arguments
		if (checkarg){
			String arg1="";
			String arg2="";
			String arg3="";
			String arg4="";
			String arg5="";
			String arg6="";
			String arg7="";			
			//long arg6bis=0;
			//long arg7=0;
			//long arg8=0;
			//long arg9=0;
			int k=-1;
			for (int j=0;j<nbArg;j++){
					k++;
					switch (chaine.get(j)){
						 case "-name" :
							arg1=chaine.get(j+1);
							TabTypeArg[0]=1;
							break;  
						 case "!-name" :
							arg1=chaine.get(j+1);
							TabTypeArg[0]=2;
							break;  
 
						 case "-perm":
							arg2=chaine.get(j+1);
							TabTypeArg[1]=1;
							
							break; 					 
						 case "!-perm":
							arg2=chaine.get(j+1);
							TabTypeArg[1]=2;
							
							break;   
						 
						 case "-hidden":
							arg3=chaine.get(j+1);
							TabTypeArg[2]=1;
							
							break; 						 
						 case "!-hidden":	
							TabTypeArg[2]=2;						 
							arg3=chaine.get(j+1);
							break; 
						 				 					 
						 case "-size":
							TabTypeArg[3]=1;						 
							arg4=chaine.get(j+1);
							break; 											 					       
						 case "!-size":		
							arg4=chaine.get(j+1);
							TabTypeArg[3]=2;						 
							break; 						 
						 
						 case "!-ctime":
							arg5=chaine.get(j+1);	
							TabTypeArg[4]=2;						 	
							break; 						 
						 case "-ctime":
							arg5=chaine.get(j+1);
							TabTypeArg[4]=1;						 
							break; 						 

						 //case "!-mtime":
							//arg6=chaine.get(j+1);
							//TabTypeArg[5]=2;						 
								
							//break; 						 
						 //case "-mtime":
							//TabTypeArg[4]=1;						 
							//arg6=chaine.get(j+1);
							//break; 									   		
						   		
						 //case "!-atime":
							//TabTypeArg[5]=2;						 
							//arg7=chaine.get(j+1);	
							//break; 						 
						 //case "-atime":
							//TabTypeArg[5]=1;						 					 
							//arg7=chaine.get(j+1);
							//break; 								   		
						   		

						   		
					}
					
					
					
					
			}
			
			ListeArgs= new String[7];
			ListeArgs[0] = arg1;
			ListeArgs[1] = arg2;
			ListeArgs[2] = arg3;
			ListeArgs[3] = arg4;
			ListeArgs[4] = arg5;
			ListeArgs[5] = arg6;			
			ListeArgs[6] = arg7;


	}
		try{
		
		File Ftmp;
		String tmp;
		int lg;	
		

		
        LsRep fw = new LsRep();
        int b=fw.walk(origine.getCanonicalPath() );	
        for (int k=0;k<=niveau;k++){     
			origine=trouveAbsolu(origine.getParent());
        }
		lg=origine.getCanonicalPath().length();
		for (int i=0;i<liste1.size();i++){
			tmp=liste1.get(i).getPath();//.substring(lg+1);
			Ftmp= new File(tmp);
			liste1.set(i,Ftmp);
		}
		liste1=trier(liste1);
		String str= Integer.toString(lg);
		File lg2 = new File(str);
		liste1.add(lg2);
		 }catch(Exception e){System.out.println(e.getMessage());}

		return liste1;
	}






	 public static void main(String[] argv) throws IOException
  {
	//boolean choix=true;//fichier
	ArrayList<File> entre = new ArrayList<File>() ;
	Scanner sc = new Scanner(System.in);
	System.out.println("Veuillez saisir un dossier/fichier :");
	String str ;//= sc.nextLine();
	str ="ss/a";
	//entre.add(str);entre.add(str);entre.add("cc");
	//for (int i=0;i<8;i++){entre.add(str);}

	System.out.println("Vous avez saisi : " + str);
	boolean absolu =false;
	boolean choix = true;
	int lg;
	String tmp;
	if (choix){
		entre=obtenirfichier(str);
		for (File j : entre){
			System.out.println("fichier: "+j);
		}
	}

	if (choix){
		liste=obtenirdossier(str);
		for (File j : liste){
			System.out.println("dossiers: "+j);
		}
	}


}

}


