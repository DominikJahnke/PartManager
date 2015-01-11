package im.jahnke.partmanager.start;

import com.alee.laf.WebLookAndFeel;

import view.OrderDialog;
import view.FrameView;

/**
 * PartManager is a program to manage electronic parts
 * 
 * TODO List:
 * 
 * - Preise aktualisieren
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
