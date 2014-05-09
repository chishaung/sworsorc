/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Spells.PL_5;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Tao Zhang & Cameron Simon
 */
public final class Planar_Return {
    JFrame frame;
    
    public Planar_Return(){
        prepareGUI();
    }
    
    public void prepareGUI(){
        frame = new JFrame("Planar_Return");
        frame.setSize(100,100);
        frame.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing( WindowEvent e )
            {  //System.exit(0); 
                frame.dispose();
            }
        });
        
        JLabel notice = new JLabel("This is Planar_Return");
        
        frame.add(notice);
        frame.setVisible(true);
    }   
    
    //check mana available
    public boolean checkMana(){
        boolean mana = false;
        
        //if( enough mana ){
          //  mana = true;
        //}
        //else{ print message that not enough mana};
        
        return mana;
    }
    
    public void performSpellEffects(){
        // this function is used to perform the spell effects
        // like cost mana, or the real effects described in rules
        if(checkMana() == true){
            // perform
            /*
                -Decrease mana by 6
                -Caster immediately returns to his home hex
                    -however if he rolls a 1 on the die, he is eliminated and 
                        removed to another plane and may never see the Valley again.
                -Spell may be cast at any point in the game (which makes it an 
                    exception to the sequence of play).        
            */
            
            // what I am thinking about performing some data effects
            // is that we can make a tmp data file that stores all the
            // char or unit info, 
            // then we can just go into that file and change the data
            // then we read the file again for refresh the game data
            
            
        }else{
            // show warning that it desn't fit all the limitations
        }
        
        
        
    }
        
}