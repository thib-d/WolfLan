package wl;

import java.net.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.net.*;

import SecureLan.*;
import wl.module.fichieretdossier.*;

import wl.divers.Clavier;
import wl.module.fichieretdossier.Envoi;
import wl.module.fichieretdossier.Reception;
 
public class WolfLan /*implements Runnable*/{
	protected static wl.divers.Clavier sc2 ;
	static Hashtable<String, String> mapCode = new Hashtable<String,String>();

	String nomT;
	String rep;
	int interfaceReseau,port;
	String id,dossier;
	protected static CryptSocket connexion;

/*/
	public WolfLan(String nom,String rep,int interfaceReseau,int port,String id,String dossier)
	{

		nomT=nom;
		this.rep=rep;
		this.interfaceReseau = interfaceReseau;
		this.port = port;
		this.id = id;
		this.dossier = dossier;
		mapCode= new Hashtable<String,String>();
		

	}
	
/*/

public static void main(String[] args) throws Exception
	{
	
		/*///permet de mettre les argument dans Clavier
		try{
		String value,key;
		for (String s: args){
			key=s.split(":")[0];
			System.out.print("cle:"+key);
			value=s.split(":")[1];
			System.out.println(",value:"+value);
			mapCode.put(key,value);
			
		}
		}catch(Exception E){System.out.println("erreur dans les arguments, les arguments doivent etre de la forme code:reponse,pour voir les codes voir wolflan.fr.nf ");
		}
		
		/*/
		try{
		wl.divers.Clavier sc2 = new wl.divers.Clavier(mapCode);
		/*///recherche des mise a jour
		String Version = "V5.0.4" ;
		 System.out.println("recherche de mise a jour ...");
		 URL oracle = new URL("wolflan.fr.nf/vscript.txt");
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));
        String inputLine;
        
        while ((inputLine = in.readLine()) != null){
			if (inputLine.equals(Version))
			{System.out.println("Version : "+Version);}
			else 
				{System.out.println("Une nouvelle version est disponible, nous vous conseillons fortement de télécharger la nouvelle version !");}
				
			        in.close();
		}
		/*/
		 }catch(Exception e){}
		System.out.println("@author: Charles THIEBAUD & Thibault DACCORD");
		
		
		
		//String fin="n";
		//int reponseclient=0;
		System.out.println("bienvenue dans wolflan");
		System.out.println("E=enable ( accepter connexion) lan,J=jumelage (creer connexion)");
		if (Clavier.nextLine(44).equals("J")){//partie client
					boolean ipOk=false;
					Detecteur detection = new Detecteur();
					int repUser;
					int limite =0;
					while (!ipOk && limite<10){
						try{
							limite++;
							//partie serveur
							detection.lancerRecherche();
							detection.actualiserScan();
							ArrayList<Socket> scan =detection.getConnexionsPossibles();
							detection.stoperRecherche();
							if (scan.size()>0){
							System.out.println("Merci de choisir le numero de l'ip voulue");
							for (int i=0;i<scan.size();i++){
								System.out.println("num "+i+"avec l adresse "+scan.get(i).getInetAddress());
							}
							repUser= Clavier.nextInt(111);
							
						    //connexion = new CryptSocket(scan,scan.get(repUser).getInetAddress());
							connexion = new CryptSocket(scan);
							connexion.makeLink();
						    System.out.println("test de la co");
						    //connexion.writeUTF("cc yy");
						    System.out.println("fin test ");
							Envoi envoi =new Envoi(connexion,sc2);
							envoi.envoyer();
							}
						}catch(Exception e){System.out.println("erreur de connection"+e);}
					}
		}else{//partie serveur
			System.out.println("lancement du serveur");
			Detecteur detecteur= new Detecteur();
			detecteur.lancerRepondeur();// cree un nouveau socket 
			detecteur.actualiserScan();
			ArrayList<Socket> coPossible = detecteur.getConnexionsPossibles();
			System.out.println("le serveur a trouver "+coPossible.size()+ " serveur");
			CryptSocket cryptSocket = new CryptSocket(coPossible);
			cryptSocket.acceptLink();
			Reception reception = new Reception(cryptSocket,sc2);
			reception.recevoir();
		}
		//connexion.fermer();
		System.out.println("bye");
	}






/*/

public static ArrayList<String[]> getId() throws Exception
	{
                Enumeration<NetworkInterface> m = NetworkInterface.getNetworkInterfaces();
                ArrayList<String[]> liste = new ArrayList<String[]>();

                while(m.hasMoreElements())
                {
                        NetworkInterface e = m.nextElement();
                        Enumeration<InetAddress> a = e.getInetAddresses();

                       while(a.hasMoreElements())
                        {
                                InetAddress addr = a.nextElement();
				String[] tab ={e.getName(),addr.getHostAddress()};
				liste.add(tab);

                        }

		   }

          return liste;

	}/*/
	
}

//idee utiliser zlib
