/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Units;
import Character.Character;

/**
 *
 * @author matt
 */
public class WyvernAirtroops extends Conjured{

    public WyvernAirtroops(Character c, double LifeCost) {
        super(c, LifeCost);
        strength = 3;
        movement = 10;
        demoralizedStrength = 1;
    }
    
    
}
