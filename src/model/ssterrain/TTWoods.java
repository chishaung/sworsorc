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
import Units.Race;
import Units.*;
public class TTWoods extends TerrainType{
    public TTWoods(){
        
    }
    
    @Override
    public double getMovementCost(MoveableUnit unit) {
        if(unit.getRace() == null || !unit.getRace().equals(Race.Elves))
            return 2;
        else
            return 1;
    }

    @Override
    public double getCombatMultiplier(MoveableUnit unit) {
        return 2;
    }

    @Override
    public String getCombatEffect(MoveableUnit unit) {
        return "";
    }
    
    @Override
    public String toString(){
        return "Woods";
    }
}
