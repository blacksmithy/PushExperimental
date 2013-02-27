package pushgame.gui.windows;


import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JTextPane;



@SuppressWarnings("serial")
public class HelpWindow extends JFrame
{

	public HelpWindow()
	{
		super("Pomoc");
		setSize(850,270);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JTextPane pane1 = new JTextPane();
		pane1.setEditable(false);
		String text1="";
		text1+="Autorzy:\tMichał Kowalski,\tMichał El Fartas\n"+
				"Projekt zaliczeniowy laboratorium przedmiotu Sztuczna Inteligencja.\n"+
				"Politechnika Poznańska 2013";
		pane1.setText(text1);
		tabbedPane.addTab("Autorzy", pane1);

		
		JTextPane pane2 = new JTextPane();
		pane2.setEditable(false);
		String text2="";
		text2+="Gra planszowa dla dwóch graczy.\n" +
				"Gra rozgrywa się na szachownicy o wymiarach 8x8, na które każdy gracz posiada 16 pionów.\n" +
				"Na początku rozgrywki gracze mają piony ułożone w dwóch rzędach po przeciwnych stronach planszy.\n" +
				"Rozgrywka polega na naprzemiennym wykonywaniu ruchów przesuwających piony.\n" +
				"Gracz wygrywa w momencie jak uda mu się umieścić wszystkie piony na pozycjach początkowych przeciwnika.\n" +
				"\n\nDopuszczalne ruchy:\n" +
				"   # dozwolone są ruchy pionkiem w każdym kierunku, aczkolwiek z preferencją ruchu w przód\n" +
				"   # maksymalna odległość przesunięcia pionka to 3 pola\n" +
				"   # jeżeli kilka pionów gracza znajduje się w jednej linii jeden za drugim, to możemy przesunąć całą linię, aczkolwiek tylko zgodnie z jej kierunkiem\n";
		pane2.setText(text2);
		tabbedPane.addTab("Zasady gry", pane2);
		
		JTextPane pane3 = new JTextPane();
		pane3.setEditable(false);
		String text3="";
		text3+= "Nowa gra – powoduje utworzenie nowego okna gry, zgodnie z ustawieniami określonymi w zakładce „wybór graczy”.\n\n" +
				"Załaduj grę – pozwala na otwarcie pliku z wcześniej zapisaną grą.\n\n" +
				"Ustaw planszę – pozwala na ręczne ustawienie planszy oraz zapisanie jej do pliku w celu późniejszego odczytania.\n\n" +
				"Wybór graczy – pozwala wybrać typ graczy (algorytm w przypadku AI), kolor pionów oraz opóźnienie z jakim wykonywane jest polecenie.";
		pane3.setText(text3);
		tabbedPane.addTab("Korzystanie z aplikacji", pane3);
		

		setEnabled(true);
		setVisible(true);
	}
}
