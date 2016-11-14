package wl.module.fichieretdossier;
import SecureLan.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import wl.*;

import wl.divers.*;

public class Reception {
	static //static DataInputStream in = null;	
	LsRep ls = new LsRep();
	private static String separator = File.separator;
	private static String verifTaille="";
	private static RandomAccessFile outFile = null;	
	static String rep;
	static CryptSocket connexion;
	static wl.divers.Clavier sc2;
	private static String emplacementChoisi="";
	private static int buffer;
	static int OSClient;
	static long tailleFichier;

	public Reception(CryptSocket pconnexion,wl.divers.Clavier psc2){
		if (pconnexion==null)
			System.out.println("connection null");
		connexion = pconnexion;
		sc2 = psc2;
	}
	
	/**
	 * @param chaine
	 * @return
	 * changer le / en \ si windows
	 */
	public static String changerseparator(String chaine){		
		if (OSClient==wl.divers.OSValidator.recupNumOs()){return chaine;}
		if (OSClient==0 && wl.divers.OSValidator.recupNumOs()==1){
			chaine.replaceAll("/","\\\\");//chaine.replaceAll("/",separator);
		}
		if (OSClient==1 && wl.divers.OSValidator.recupNumOs()==0){
			chaine=remplacerLeMal(chaine);
		}
		
	return chaine;
	}
	/**
	 * @param chaine
	 * @return
	 * changer le / en \ si windows
	 */
	public static String remplacerLeMal(String chaine){
	String[] tab;
	tab=chaine.split("");
	String tmp="";
	for (String s : tab){
		if (!s.equals("\\")){
		tmp=tmp+s;
		}
		else{tmp=tmp+"/";}
		
	}
	return tmp;
	}




	/**
	 * @throws Exception
	 * demande le type de reception
	 */
	public static void recevoir() throws Exception{
		System.out.println(connexion.readUTF()); 
		connexion.writeUTF("pret ! ");
		String repValider;
		boolean ok=false;
		System.out.println("Choisir un dossier/fichier de reception:");
		emplacementChoisi= sc2.nextLine(200);
		if(emplacementChoisi.length()<1){
			emplacementChoisi=".";
		}
		System.out.println("en attente de connexion");
		String mode =connexion.readUTF();
		if(mode.equals("E")){
			System.out.println("reception de(s) fichier(s)/dossiers(s)");
			receptionSimple(emplacementChoisi,0);
		}
		else{
			System.out.println("synchronisation d'un fichier/dossier syncronisé");
			System.out.println("valider ? (y/n)");
				if(sc2.nextLine(200).equals("y")){
					synchronisation(emplacementChoisi);
				}
			}
	}
		
	

	
	
	/**
	 * @param fichier
	 * @throws Exception
	 * repete la reception
	 */
	private static void synchronisation(String fichier) throws Exception{//qui peut aussi etre un dossier
		boolean fin=false;
		while(!fin){
			System.out.println("synchronisation en cours,");	//warnong le fichier est recopier  a l infini
			receptionSimple(fichier,1);
			TimeUnit.MILLISECONDS.sleep(200);	
		}
	}	
	
	
	
	/**
	 * @param emplacementChoisi
	 * @param mode
	 * @throws Exception
	 * receptionner un fichier
	 */
	private static void receptionSimple(String emplacementChoisi,int mode) throws Exception{
	int nbDossiers;
	int nbFichiers;
	String fileName;
	File file;
	//envoi du nombre de dossier
	nbDossiers=connexion.readInt();
	System.out.println("il y a "+nbDossiers+ " dossiers");
	//envoi du nombre de fichier
	nbFichiers=connexion.readInt();

	//envoi de l'os
	OSClient=connexion.readInt();	
	// envoi du buffer

	buffer=connexion.readInt();		
	File fTmp;
	for(int i=0;i<nbDossiers;i++)// cree tout les dossier
	{
		fileName=connexion.readUTF();
		fileName=changerseparator(fileName);
		file = new File(emplacementChoisi+separator+fileName);
		file.mkdir();
	}

	boolean sortir =false;
	String fileName2;
		for(int j=0;j<nbFichiers;j++){
			sortir=false;
		
		fileName2=connexion.readUTF();
		System.out.println("nom recu "+fileName2);
		fileName2=changerseparator(fileName2);
		//ListeNomFichier.add(fileName);
		//System.out.println("nom recu: "+fileName);
		tailleFichier=connexion.readInt();
		
		if(mode==1){
			fTmp=new File(fileName2);
			if(!fTmp.exists() ){
				
				System.out.println("je n ai pas"+fTmp.getPath());
				connexion.writeUTF("yes ok send");
			}
			else{
				connexion.writeUTF("j ai deja");
				sortir=true;

			}
		}
		if(!sortir){// si il n y a pas deja le fichier
		System.out.println("ecriture du fichier");
		file = new File(emplacementChoisi+separator+fileName2);
		System.out.println("creation du fichier "+file.getAbsolutePath());
		outFile=new RandomAccessFile(file,"rw");
		receptionContenu();
		}
	}
	}
		
		
	/**
	 * @throws Exception
	 * reception des données du fichier
	 */
	@SuppressWarnings("static-access")
	private static void receptionContenu() throws Exception{
		byte[] data;
		int typeEnvoi;
		long nbTabEntier;
		long recu;
	   typeEnvoi= connexion.readInt();
		tailleFichier=connexion.readInt();
		System.out.println("taille de "+tailleFichier);

		if (typeEnvoi==0){
			System.out.println("petit fichier");
			System.out.println("taille de "+tailleFichier);
			data= new byte[(int) tailleFichier];
			 try {
				data=connexion.readFully();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			 outFile.write(data);
		}
		
		else{
			 recu=0;
			 System.out.println("gros fichier");
			data = new byte[(int) buffer];
			nbTabEntier=connexion.readInt();

			for (int i=0;i<nbTabEntier;i++){
				try {
					data=connexion.readFully();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				outFile.write(data);
				recu=recu+buffer;
			}	    
			data = new byte[(int) ((int) tailleFichier-recu)];
			try {
				data=connexion.readFully();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			outFile.write(data);
		}
		
		
		
		
		
	}
		
		
		
}
