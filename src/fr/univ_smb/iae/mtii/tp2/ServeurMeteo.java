package fr.univ_smb.iae.mtii.tp2;

import java.io.PrintWriter;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class ServeurMeteo {
	private String bulletin_precedent, bulletin_courant;
	private int port = 9090;
	
	public void setPort(int port) {
		if (port >= 8500 && port <= 9500) {// Intervalle de valeur autorisee du
											// port
			this.port = port;
			System.out.println("Le port a ete modifie. Il faudrait relancer le serveur !\n");
		} else
			System.out.println("Valeur non autorisee: le port n'a pas ete modifie.\n");
	}
	// Méthode pour ouvrir une connexion
	public ServerSocket ouvrirConnexion() throws IOException {
	//	this.setPort();
		ServerSocket listener = new ServerSocket(this.getPort());
		return listener;
	}

	// On utilise desormais les sockets pour avoir une application client-
	// serveur
	public void donnerMeteo(ServerSocket listener) throws IOException {
		System.out.println("Le serveur est prêt à donner la météo. "
				+ "Il est en attente de demandes de client(s) sur le port " + this.getPort() + ".\n");
		try {
			while (true) {
				Socket socket = listener.accept();
				try {
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					if (this.getBulletin_precedent() == null)
						if (this.getBulletin_courant() == null)
							this.setBulletin_precedent("aucun bulletin");
						else
							this.setBulletin_precedent(this.bulletin_courant);
					this.setBulletin_courant(
							"Aujourd'hui, le " + new Date().toString() + " - " + this.demanderSaisieAvisMeteo());
					String messageDuServeur = getBulletin_courant();
							//this.demanderSaisieAvisMeteo();
							//"Il fait tout le temps beau,c'est trop beau !\n";
					out.println(messageDuServeur);
				} finally {
					socket.close();
				}
			}

		} finally {
			listener.close();
		}
	}

	// Utilisation des accesseurs, modificateurs !!!!
	public void afficherBulletinCourant() {
		System.out.println("Bulletin actuel:\t" + this.getBulletin_courant() + "\n");
	}

	// Utilisation des accesseurs, modificateurs !!!!
	public void afficherBulletins() {
		System.out.println("===== Bulletins meteo =====\n");
		this.afficherBulletinCourant();
		System.out.println("Bulletin precedent:\t" + this.getBulletin_precedent() + "\n");
	}

	public String demanderSaisieAvisMeteo() {
		Scanner clavier = new Scanner(System.in);
		System.out.println("Saisir l'avis meteo: ");
		String avis = clavier.nextLine();
		return avis;
		
	}

	public String getBulletin_precedent() {
		return bulletin_precedent;
	}

	public void setBulletin_precedent(String bulletin_precedent) {
		this.bulletin_precedent = bulletin_precedent;
	}

	public String getBulletin_courant() {
		return bulletin_courant;
	}

	public void setBulletin_courant(String bulletin_courant) {
		this.bulletin_courant = bulletin_courant;
	}

	public ServeurMeteo(String bulletin_prec) {
		this.setBulletin_precedent(bulletin_prec);
	}

	public int getPort() {

		return port;
	}

	

	public void afficherPort() {
		System.out.println("La valeur du port est : " + this.getPort() + "\n");
	}

	// Illustration des modifications
	public static void main(String[] args) {
		ServeurMeteo serveur = new ServeurMeteo("Il pleut toute la journee.");
		try {
			ServerSocket socket = serveur.ouvrirConnexion();
			serveur.donnerMeteo(socket);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
