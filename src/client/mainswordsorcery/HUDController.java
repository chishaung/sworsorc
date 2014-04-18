/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mainswordsorcery;

/**
 *
 * @author Joe Higley
 */
import MoveCalculator.MovementCalculator;
import Units.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Arrays;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import sshexmap.MainMap;
import sshexmap.MapHex;
import sshexmap.MapView;
 
public class HUDController {
    @FXML private TabPane Units;
    @FXML private TabPane Targets;
    @FXML private ImageView SunImage;
    @FXML private Text RedState;
    @FXML private Text BlueState;
    @FXML private MenuBar menuBar;
    @FXML private TextField message_box;
    @FXML private TextArea chat_box;
    @FXML private ScrollPane map_view;
    @FXML private ScrollPane mini_map;
    @FXML private SwingNode hex_map;
    @FXML private Button phaseButton;
    
    ArmyUnit bow = new Bow();
    ArmyUnit lightsword = new LightSword();
    ArmyUnit pike = new PikeMan();
    SwingNode hdip = new SwingNode();

    MainMap mainmap = MainMap.GetInstance();
    MapView mapview = MapView.getMapView();
    MoveableUnit selected_unit;
        

    /** 
     * initialize() is used to connect GUI view elements with model elements. 
     * example code in HUDController.java
     * 
     * @author Jay Drage        
     */
    public void initialize(){
        MapView m = MapView.getMapView();
        
        //temporary code for movemment demo
        UnitPool pool = UnitPool.getInstance();
        ArmyUnit u = new LightSword();
        u.setRace(Race.Elves);
        pool.addUnit(0, u, "0606");
        
        hdip.setContent(MapView.getDipView());
        
        //Display map in map_view
        SwingNode hmap = new SwingNode();
        hmap.setContent(m);
        map_view.setContent(hmap);
        
        //this adds mouse support to map_view, just a placeholder for now
        map_view.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
		public void handle (MouseEvent mouseEvent) {
                        String hexID = mapview.hexAt((int)mouseEvent.getX(),
                                                     (int)mouseEvent.getY());
                        System.out.println("clicked hex " + hexID);
                        MapHex hex = mainmap.GetHex(hexID);
                        System.out.println("Got hex " + hex);
                        if(hex == null) {
                            System.out.println("No hex at " + hexID + "???");
                        }
                        
                        if(selected_unit == null) {
                            System.out.println("Checking for unit");
                            ArrayList<MoveableUnit> units = hex.getUnits();
                            if(units == null || units.size() == 0) {
                                return;
                            }
                            System.out.println("Found unit " + units.get(0));
                            selected_unit = units.get(0);

                            if(selected_unit == null) {
                                System.out.println("Something went horribly wrong");
                            }
                                                        
                            ArrayList<MapHex> canMoveTo = new ArrayList<MapHex>();
                            
                            MovementCalculator.getValidMoves(selected_unit, hex, 5, canMoveTo );
                            System.out.println("Found " + canMoveTo.size() + " reachable hexes");
                            mapview.highlight(canMoveTo);
                        }
                        else {
                            mapview.clearHighlights();
                            UnitPool pool = UnitPool.getInstance();
                            pool.addMove(selected_unit, hexID);
                            selected_unit = null;
                        }
                        
                        
		}
	});
       //this adds mouse support to mini_map
        mini_map.setOnMousePressed(new EventHandler<MouseEvent>() {
		public void handle (MouseEvent mouseEvent) {
			System.out.println("X: " + mouseEvent.getX() + " Y: " + mouseEvent.getY());
		}
	});
    }
   
    
    //called when clicking a friendly hex
    @FXML protected void DisplayUnits(ActionEvent event) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List <ArmyUnit> AU = new ArrayList <>();
        AU.add(bow);
        AU.add(lightsword);
        AU.add(pike);
        DisplayStack(Units, AU);
    }
    
    //called when clicking an enemy hex
    @FXML protected void DisplayTargets(ActionEvent event) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List <ArmyUnit> AU = new ArrayList <>();
        AU.add(lightsword);
        AU.add(pike);
        DisplayStack(Targets, AU);
    }
    
    /** 
     * constructs the tab pane for the selected hex
     * @author Joe Higley      
     */
    protected void DisplayStack(TabPane tp, List <ArmyUnit> AU) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ClearTitles(tp); //empty the tab pane
        int x = AU.size();
//        int x = Game.getInstance().getNum(); //this is just a random number of units, will be replaced later
        //create array of tabs
        Tab[] tabs = new Tab[x];
        for(int i=0; i < x; i++) {
            tabs[i] = new Tab();
            tabs[i].setText(AU.get(i).toString());
            FillStats(tabs[i], AU.get(i)); //add contents of the tab
        }
        //add tabs to tab pane
        tp.getTabs().addAll(tabs);
    }

    /** 
     * fills the contents of a tab pane
     * with unit stats.
     * @author Joe Higley      
     */
    public void FillStats(Tab tp, ArmyUnit AU) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        //stats are kept in a GridPane inside a ScrollPane in case of more stats than space
        ScrollPane scroll = new ScrollPane();
        GridPane grid = new GridPane();
        //set grid formats (may be replaced by css file later)
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(8);
        grid.setHgap(15);
        grid.setPadding(new Insets(5, 5, 5, 5));
        
        //set column1 format
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.LEFT);
        column1.setPercentWidth(65);
        grid.getColumnConstraints().add(column1); 
        //set column2 format
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHalignment(HPos.RIGHT);
        column2.setPercentWidth(35);
        grid.getColumnConstraints().add(column2);
        
        //labels and function names
        List <String> stats = new ArrayList<>(Arrays.asList("Race: ", "Demoralized: ", "Strength: ", "Movement: "));
        List <String> funcs = new ArrayList<>(Arrays.asList("getRace", "isDemoralized", "getStrength", "getMovement"));
        //adds stats to GridPane (will be replaced with call to unit stats)
        for(int i=0; i < stats.size(); i++){
            Label l = new Label(stats.get(i));
            l.setFont(Font.font(null, FontWeight.BOLD, 12));
            //"get" method to be called
            Method method = AU.getClass().getMethod(funcs.get(i));
            Object b = method.invoke(AU); //get desired data
            TextField tf = new TextField(String.valueOf(b));
            tf.setAlignment(Pos.CENTER_RIGHT);
            
            //add stats to new row of GridPane
            grid.add(l, 0, i);
            grid.add(tf, 1, i);
        }
        
        //add GridPane to ScrollPane
        scroll.setFitToWidth(true);
        scroll.setContent(grid);
        //add ScrollPane to Tab
        tp.setContent(scroll);
        
    }
    
    /** 
     * 
     * @author Joe Higley      
     */    
    protected void ClearTitles(TabPane tp) {
        tp.getTabs().clear();
    }
    
    /** 
     * Displays user text in chat_box
     * 
     * @author Joe Higley      
     */    
    @FXML protected void SubmitToChat(ActionEvent event) {
        if(!"".equals(message_box.getText())) {
            chat_box.appendText("<username> " + message_box.getText() + "\n");
            message_box.clear();
        }
    }
    
    /**
     * Undo the last move made
     * Not functional need to talk to whoever coded Unit.UnitPool.undoMove(String)
     * @param event 
     */
    @FXML protected void undoMove(ActionEvent event) {
        UnitPool.getInstance().undoMove(null);
    }
    
    /** 
     * Changes current scene to main menu and shows main menu     * 
     * @author Joe Higley      
     */
    @FXML protected void Quit(ActionEvent event) {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.setScene(Game.getInstance().getMainScene());
        stage.setFullScreen(true);
        stage.show();
    }
    
    /** 
     * SolarDisplay code goes here
     * 
     * @author Joe Higley      
     */   
    @FXML protected void ChangePhase(ActionEvent event) {
        switch(phaseButton.getText()){
            case "End Movement Phase":
                phaseButton.setText("End Spell Phase");
                break;
        
            case "End Spell Phase":
                phaseButton.setText("End Combat Phase");
                break;
            
            case "End Combat Phase":
                phaseButton.setText("End Turn");
                break;
                
            case "End Turn":
                phaseButton.setText("End Movement Phase");
                
                SolarDisplay.SunCalc();
                Image Sun = new Image(SolarDisplay.GetSunImage());
                SunImage.setImage(Sun);
        
                RedState.setText(SolarDisplay.GetRedState());
                BlueState.setText(SolarDisplay.GetBlueState());
                break;
        }
        
    }
    
    @FXML protected void DisplayDiplomacy(ActionEvent event) {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.setScene(Game.getInstance().getDiploScene());
        stage.show();
    }
}
