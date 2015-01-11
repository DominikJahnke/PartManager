package im.jahnke.partmanager.start;

import im.jahnke.partmanager.view.FrameView;
import im.jahnke.partmanager.view.OrderDialog;

import com.alee.laf.WebLookAndFeel;

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
