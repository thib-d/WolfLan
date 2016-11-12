package wl.module.fichieretdossier;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import SecureLan.*;

import wl.*;
import wl.divers.Clavier;

public class Envoi{

	private static CryptSocket connexion;
	private static Clavier clavier;
	private String rep;//reponse du user
	int buffer;
	static int tailleBuffer= 9999;
	/*/ partie d envoi de fichier
	 * 
	 */
	public Envoi(CryptSocket pconnextion,Clavier pclavier){
		connexion=pconnextion;
		clavier=pclavier;
	}
	
	/*Code clavier 
	 * 100 le fichier a envoyer 
	 * 101 E/P  envoi ou partage
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	
	
	public static void envoyer() throws Exception{
		connexion.writeUTF("pres a l envoi?");
		System.out.println(connexion.readUTF());
		boolean exists= false;
		boolean firstPassage = true;
		boolean ok;
		File testFile;
		String fileName;
		String rep="E";
		do{
				if(!firstPassage)
				{
					System.out.println("le fichier (ou dossier) n'existe pas ");
				}
				System.out.println("fichier ou dossier a envoyer :");
				fileName = clavier.nextLine(100);
				System.out.println("fichier:"+fileName);
				testFile = new File(fileName);
				System.out.println(testFile);
				exists = testFile.exists();
				firstPassage = false;
		}while(!exists);		
		
		ok=false;
		while(!ok){
			System.out.println("Pour envoyer un fichier dossier: E, pour faire un dossier partager: P");
			rep=clavier.nextLine(101);
			rep.toUpperCase();
			if(rep.equals("E") || rep.equals("P")){ok=true;}	
		}
		connexion.writeUTF(rep);
		
		if (rep.equals("E")){
			envoiSimple(testFile.getAbsolutePath(),0);
			
		}
		if (rep.equals("P")){
			synchronisation(testFile.getAbsolutePath());
			
		}
			
	}
			
	
	/**
	 * @param fichier
	 * @throws Exception
	 * permet de synchoniser un fichier ou dossier, (propose d envoyer en permanence)
	 */
	private static void synchronisation(String fichier) throws Exception{//qui peut aussi etre un dossier
		boolean fin=false;
		while(!fin){
			System.out.println("synchronisation en cours,");
			envoiSimple(fichier,1);
			
			
			TimeUnit.MILLISECONDS.sleep(2000);
		
		}
	}	
	
	
			
			
	/**
	 * @param fichier
	 * @param mode
	 * @throws Exception
	 * envoi un fichier
	 */
	private static void envoiSimple(String fichier,int mode) throws Exception{
	 ArrayList<File> listeFichiers;
	 ArrayList<File> listeDossiers;
	 int lgrelatif;
	 String cheminRelatif;//pour reduire la taille de chemin absolue en relatif
	 File FcheminRelatif;//pour reduire la taille de chemin absolue en relatif
	 LsRep lsRep;
	 int receptionOk =0;
	 lsRep = new  LsRep();
	 listeDossiers = LsRep.obtenirdossier(fichier);//liste tout les dossiers du dossier "fichier" (qui peut etre un dossier)
	 listeFichiers = LsRep.obtenirfichier(fichier);//liste tout les fichiers du dossier "fichier" (qui peut etre un dossier)
	lgrelatif=Integer.parseInt(listeFichiers.get(listeFichiers.size()-1).getPath());//-1 car le dernier element est le chemin relatif
	listeDossiers.remove(listeDossiers.size()-1);
	listeFichiers.remove(listeFichiers.size()-1);		
	//envoi du nombre de dossier
	connexion.writeInt(listeDossiers.size());
	//envoi du nombre de fichier
	connexion.writeInt(listeFichiers.size());
	//envoi de l'os
	connexion.writeInt(wl.divers.OSValidator.recupNumOs());		
	//envoi buffer
	connexion.writeInt(tailleBuffer);
		
		
	for(int i=0;i<listeDossiers.size();i++)
		{
			cheminRelatif=listeDossiers.get(i).getCanonicalPath().substring(lgrelatif+1);

			connexion.writeUTF(cheminRelatif);//envoi tout les dossier
		}	


	for(int i=0;i<listeFichiers.size();i++)
		{
		System.out.println("envoi du nom "+listeFichiers.get(i).getCanonicalPath().substring(lgrelatif+1));
		connexion.writeUTF(listeFichiers.get(i).getCanonicalPath().substring(lgrelatif+1));
		connexion.writeInt((int)listeFichiers.get(i).length());
		if(mode==1 ){// si syncronisation
			if(connexion.readUTF().equals("yes ok send")){//si il ne l a pas
				sendContenu(listeFichiers.get(i));
			}
		}
		else{
		sendContenu(listeFichiers.get(i));
		}
		}
			
			
		
		
	}
	/*/
	 * envoi le contenu d un fichier
	 */
	private static void sendContenu(File file) throws IOException{
		 RandomAccessFile inFile;	
		 inFile = new RandomAccessFile(file,"r");
		 int tailleFichier=(int) file.length();
		 boolean big;
		 byte[] data;//data lu dans le fichier

		 if(tailleFichier < tailleBuffer){
			 connexion.writeInt(0);
			 big=false;
		 }
		 else{
			 connexion.writeInt(1);
			big=true;
		 }
		 connexion.writeInt(tailleFichier);

		 if(!big){
			 data= new byte[(int) tailleFichier];
			 inFile.read(data);
			 connexion.write(data);
		}
		else{
			data = new byte[tailleBuffer];
			int nbTabEntier=tailleFichier/tailleBuffer;
			nbTabEntier--;
			connexion.writeInt(nbTabEntier);
			Long envoyer = (long) 0;
			for (int i=0;i<nbTabEntier;i++){
				inFile.read(data);
				connexion.write(data);
				envoyer=envoyer+tailleBuffer;
			}	    

			data = new byte[(int) ((int) tailleFichier-envoyer)];
			inFile.read(data);
			connexion.write(data);
			
		}

		
		
	}
		
		

		
	}		
			
	
