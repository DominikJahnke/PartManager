package start;

import com.alee.laf.WebLookAndFeel;

import view.OrderDialog;
import view.FrameView;

/**
 * PartManager is a program to manage electronic parts
 * 
 * TODO List:
 * 
 * - Add Amazon.de to shops (ok)
 * - Suche mit Artikelnummer erweitern (ok)
 * - Preise aktualisieren
 * 
 * 
 * @author Dominik Jahnke
 *
 */
public class Start {

    public static void main(String args[]) {
	WebLookAndFeel.install();
	new FrameView();

    }

}
