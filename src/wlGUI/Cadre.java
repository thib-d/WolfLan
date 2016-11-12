package wlGUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import SecureLan.*;
;public class Cadre extends JFrame {

	/**
	 * @param args
	 */
	JPanel panneauDuCentre;
	static JFrame cadre;
	
    public Cadre(String titre) {
        super(titre);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(panneauDeContenu());
        pack();
    }
	
	
	public static void main(String[] args) {
        cadre = new Cadre("Wolflan");
        cadre.setLocationRelativeTo(null);
        cadre.setVisible(true);
        cadre.validate();
	}
	
	
    private JPanel panneauDeContenu() {
    	JPanel grandPanel = new JPanel();
    	JPanel panneauDuBas = panneauDuBas();
    	panneauDuCentre = panneauDuCentre();
    	JPanel panneauDuHaut = panneauDuHaut();
    	grandPanel.setLayout(new BorderLayout());
    	grandPanel.add(panneauDuHaut);
    	grandPanel.add(panneauDuCentre);
    	grandPanel.add(panneauDuBas);
    	BorderLayout monLayout = new BorderLayout();
    	monLayout.addLayoutComponent(panneauDuHaut,BorderLayout.NORTH);
    	monLayout.addLayoutComponent(panneauDuCentre, BorderLayout.CENTER);
    	monLayout.addLayoutComponent(panneauDuBas, BorderLayout.SOUTH);
    	grandPanel.setLayout(monLayout);
		return grandPanel;
    	
    }
	
    private JPanel panneauDuBas(){
    	JPanel panel = new JPanel();
    	JButton boutonRechercher = new JButton("Recherche d'appareil");
    	boutonRechercher.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("recherche d'appareils");
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
    	panel.setLayout(new BorderLayout());
    	panel.add(boutonRechercher);
		return panel;
    }	
    
    private JPanel panneauDuCentre(){
    	JPanel panel = new JPanel();
    	BorderLayout monLayout = new BorderLayout();
    	panel.setLayout(monLayout);
    	JLabel text= new JLabel("Bienvenue, appuyer sur on pour commencer");
    	panel.add(text);
    	//panel.add(afficherScan());
		return panel;
    }
    private JPanel panneauDuCentre(Component compo){
    	JPanel panel = new JPanel();
    	BorderLayout monLayout = new BorderLayout();
    	panel.setLayout(monLayout);
    	JLabel text= new JLabel("cc");
    	panel.add(text);
    	//panel.add(afficherScan());
		return panel;
    }    
    
    private JPanel panneauDuHaut(){
    	JPanel panel = new JPanel();
    	panel.setLayout(new BorderLayout());
      	JButton boutonOnOff = new JButton("ON/OFF");
      	boutonOnOff.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("on/off");
				// TODO Auto-generated method stub
				panneauDuCentre.removeAll();
				//detection.lancerRecherche();
				//resultatScan= detection.getScan();
				//detection.stoperRecherche();				panneauDuCentre.add(afficherScan());
				//cadre.repaint();
				cadre.validate();

				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
      	panel.add(boutonOnOff);
		return panel;
    }	
    
    
    private JComboBox afficherList(String elements[]){

    	//Create the combo box, select item at index 4.
    	//Indices start at 0, so 4 specifies the pig.
    	JComboBox listBox = new JComboBox(elements);
    	listBox.setSelectedIndex(4);
    	listBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	return listBox;
    	
    }
    
}
