/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ssterrain;

/**
 *
 * @author John Goettsche
 * CS 383 Software Engineering
 */
import Units.*;
import sshexmap.MapHex;
public class HERoad extends EdgeElement {
    @Override
    public HexEdgeType getEdgeType() {
        return HexEdgeType.Road;
    }
    
    /*
    @Override
    public double getMovementCost(MoveableUnit unit) {

    }

    @Override
    public double getCombatMultiplier(MoveableUnit unit) {

    }

    @Override
    public String getCombatEffect(MoveableUnit unit) {

    }
    */
}
