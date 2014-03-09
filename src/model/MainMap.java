/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MainMapBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
//import ssterrain.*;
import Units.*;

/**
 * 
 *   Email me at klin7456@vandals.uidaho.edu if you hove questions or need help
 * 
 *   Keith's pull - adding movement calculator prelim. 
 * 
 * @author David Klingenberg: 
 */
public class MainMap {
    public HashMap <String, MapHex> mainMap = new HashMap();
    private String hexNumber, northHexNumber, northEastHexNumber, 
            southEastHexNumber, southHexNumber, southWestHexNumber,
            northWestHexNumber, terrainKey, providenceName, hexName;
    private boolean cityHex, vortexHex, castleHex, capitalHex, townHex;
    private int portalHex;
    private DocumentBuilderFactory factory; 
    private DocumentBuilder builder;
    private Document doc;
    private File file = new File("resources/MainMap.xml");
    private HashMap<String, ArrayList<String>> edgeDirectionList = new HashMap<>();
    

    
    private static MainMap INSTANCE;
    
    private MainMap(){
         SetFactory();
        try {
            SetBuilder();
            SetValidateFactory();
            SetDoc();
        
        } catch (ParserConfigurationException|SAXException ex) {
            Logger.getLogger(MainMap.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex){
            Logger.getLogger(MainMap.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("You need a resource folder in your projects root directory containing the MainMap.xml.");
        }
        
        BuildmainMap();
        
    }

   private void CleanHex(){
        hexNumber = "";
        northHexNumber = "";
        northEastHexNumber= "";
        southEastHexNumber = "";
        southHexNumber = ""; 
        southWestHexNumber = "";
        northWestHexNumber = "";
        terrainKey = ""; 
        providenceName = "";
        hexName = "";
        cityHex = false;
        vortexHex = false;
        castleHex = false;
        capitalHex = false; 
        townHex = false;
        portalHex = 0;
        HashMap<String, ArrayList<String>> edgeDirectionList = new HashMap<>();
   }
    
    private void BuildmainMap(){
        
        NodeList listOfHex = GetDoc();
       
        //System.out.println("Total number of hexs : " + listOfHex.getLength());
        
        for(int s = 0; s < listOfHex.getLength(); s++){
            CleanHex();
           Node hex = listOfHex.item(s);
           
            //if(hex.getNodeType() == Node.ELEMENT_NODE )
              //  System.out.println("\nName " + hex.getNodeName()+ s);
        
            NodeList hexList = hex.getChildNodes();
            //System.out.println("Total number of cells : " + hexList.getLength());
            
            for (int i = 0; i < hexList.getLength(); i++){
                Node hexItem = hexList.item(i);
                
                if(hexItem.getNodeType() == Node.ELEMENT_NODE){
                                        
                   if ("hexNumber".equals(hexItem.getNodeName())) 
                       SetHexNumber(hexItem);
                   if ("northHexNumber".equals(hexItem.getNodeName())) 
                       SetNorthHexNumber(hexItem);
                   if ("northEastHexNumber".equals(hexItem.getNodeName())) 
                       SetNorthEastHexNumber(hexItem);
                   if ("southEastHexNumber".equals(hexItem.getNodeName())) 
                       SetSouthEastHexNumber(hexItem);                       
                   if ("southHexNumber".equals(hexItem.getNodeName())) 
                       SetSouthHexNumber(hexItem);
                   if ("southWestHexNumber".equals(hexItem.getNodeName())) 
                       SetSouthWestHexNumber(hexItem);
                   if ("northWestHexNumber".equals(hexItem.getNodeName())) 
                       SetNorthWestHexNumber(hexItem);
                   if ("terrainKey".equals(hexItem.getNodeName())) 
                       SetTerrainKey(hexItem);
                   if ("cityHex".equals(hexItem.getNodeName())) 
                       if ("true".equals(hexItem.getTextContent()))
                       SetCityHex();
                   if ("hexName".equals(hexItem.getNodeName()))
                       SetHexName(hexItem);
                   if ("vortexHex".equals(hexItem.getNodeName())) 
                       if ("true".equals(hexItem.getTextContent()))
                       SetVortexHex();
                   if ("portalHex".equals(hexItem.getNodeName()))
                       SetPortalHex(hexItem);
                   if ("providenceName".equals(hexItem.getNodeName()))
                       this.providenceName = hexItem.getTextContent();
                   if ("castleHex".equals(hexItem.getNodeName()))
                       this.castleHex = true;
                   if ("townHex".equals(hexItem.getNodeName()))
                       this.townHex = true;
                   
                   if ("hexEdgeMap".equals(hexItem.getNodeName())){
                       NodeList listOfEdges = hexItem.getChildNodes();
                       
                       for (int j = 0; j < listOfEdges.getLength(); j ++){
                           Node edgeDerection = listOfEdges.item(j);
                           
                           if(edgeDerection.getNodeType() == Node.ELEMENT_NODE){
                               NodeList edgeItems = edgeDerection.getChildNodes();
                               ArrayList<String> edgeList = new ArrayList<>();
                               
                               for (int h = 0; h < edgeItems.getLength(); h++){
                                   Node edgeAttribute = edgeItems.item(h);
                                   
                                   if(edgeAttribute.getNodeType() == Node.ELEMENT_NODE)
                                       edgeList.add(edgeAttribute.getTextContent());
                               }//for (int h = 0; h < edgeItems.getLength(); h++)
                               
                                SetEdgeDirectionList(edgeDerection, edgeList);

                           }
                                          
                       }//for (int j = 0; j < listOfEdges.getLength(); j ++)
                       
                   }//if ("hexEdgeMap".equals(hexItem.getNodeName()))
                
                }//if(hexItem.getNodeType() == Node.ELEMENT_NODE)
            
            }//for(int i = 0; s < listOfHex.getLength(); s++)         
            if (GetPortalHex() >0 ){    
            
            MapHex hexObject = new MapHex(hexNumber, northHexNumber, northEastHexNumber,
                    southEastHexNumber, southHexNumber, southWestHexNumber, northWestHexNumber, terrainKey, this.providenceName,edgeDirectionList, portalHex);
                SetMainMap(hexObject);
                
            }
            else
                if (this.vortexHex){
                    MapHex hexObject = new MapHex(hexNumber, northHexNumber, northEastHexNumber,
                            southEastHexNumber, southHexNumber, southWestHexNumber, northWestHexNumber, terrainKey, providenceName, edgeDirectionList, vortexHex);
                    SetMainMap(hexObject);
                
                
                }
                else
                    if (cityHex || capitalHex || castleHex || townHex){
                        MapHex hexObject = new MapHex(hexNumber, northHexNumber,
                                northEastHexNumber, southEastHexNumber, southHexNumber,
                                southWestHexNumber, northWestHexNumber, terrainKey,
                                providenceName, edgeDirectionList, cityHex, capitalHex,
                                castleHex, townHex, this.hexName);
                        SetMainMap(hexObject);
                    }
                    else
                        if (this.hexName != null){
                            MapHex hexObject = new MapHex(hexNumber, northHexNumber,
                                    northEastHexNumber, southEastHexNumber, southHexNumber,
                                    southWestHexNumber, northWestHexNumber, terrainKey, providenceName, edgeDirectionList, this.hexName);
                            SetMainMap(hexObject);
                        }
                        else {
                            MapHex hexObject = new MapHex(hexNumber, northHexNumber, northEastHexNumber,
                                    southEastHexNumber, southHexNumber, southWestHexNumber, northWestHexNumber, terrainKey, providenceName, edgeDirectionList);
                            SetMainMap(hexObject);
                        }
        }//for(int s = 0; s < listOfHex.getLength(); s++)
        
    }//private void BuildmainMap(){

    private void SetMainMap(MapHex hexObject) {
        mainMap.put(hexObject.GetID(), hexObject);
    }


    private int GetPortalHex() {
        return portalHex;
    }

    private void SetEdgeDirectionList(Node edgeDerection, ArrayList<String> edgeList) {
        edgeDirectionList.put(edgeDerection.getNodeName(), edgeList);
    }

    private void SetPortalHex(Node hexItem) throws DOMException, NumberFormatException {
        portalHex = Integer.parseInt(hexItem.getTextContent());
    }

    private void SetVortexHex() {
        vortexHex = true;
    }

    private void SetHexName(Node hexItem) throws DOMException {
        this.hexName = hexItem.getTextContent();
    }

    private void SetCityHex() {
        cityHex = true;
    }

    private void SetTerrainKey(Node hexItem) throws DOMException {
        terrainKey = hexItem.getTextContent();
    }

    private void SetNorthWestHexNumber(Node hexItem) throws DOMException {
        northWestHexNumber = hexItem.getTextContent();
    }

    private void SetSouthWestHexNumber(Node hexItem) throws DOMException {
        southWestHexNumber = hexItem.getTextContent();
    }

    private void SetSouthHexNumber(Node hexItem) throws DOMException {
        southHexNumber = hexItem.getTextContent();
    }

    private void SetSouthEastHexNumber(Node hexItem) throws DOMException {
        southEastHexNumber = hexItem.getTextContent();
    }

    private void SetNorthEastHexNumber(Node hexItem) throws DOMException {
        northEastHexNumber = hexItem.getTextContent();
    }

    private void SetNorthHexNumber(Node hexItem) throws DOMException {
        northHexNumber = hexItem.getTextContent();
    }

    private void SetHexNumber(Node hexItem) throws DOMException {
        hexNumber =  hexItem.getTextContent();
    }


    private NodeList GetDoc() {
        return doc.getElementsByTagName("hex");
    }
    
    private void SetDoc() throws IOException, SAXException {
        doc = builder.parse(file);
    }

    private void SetValidateFactory() {
        factory.setValidating(true);
    }

    private void SetBuilder() throws ParserConfigurationException {
        builder = factory.newDocumentBuilder();
    }

    private void SetFactory() {
        factory = DocumentBuilderFactory.newInstance();
    }
    
    public static MainMap GetMainMap(){
      if (INSTANCE == null)
          INSTANCE =  new  MainMap();
      return INSTANCE;
    }
    
    public static MapHex GetMapHex (String id){
        return mainMap.get(id);
    }

    public static void MovementCalculator( MapHex sourceHex, Unit movingUnit,
                                       int moveAllowance, ArrayList validHexes )
    {
        if( moveAllowance == 0 )
        {
            return;
        }
        else
        {
            int hexId = Integer.parseInt( sourceHex.GetHexName() );
            // If the 2nd most significant digit of the hex id is even...
            if( (hexId/100)%2 == 0 )
            {
                if( !validHexes.contains( GetMapHex( Integer.toString(hexId -1))))
                {
                    // isValidEdge() and isValidHex() need to be written.
                    if( isValidEdge() && isValidHex() )
                    {
                        validHexes.add(GetMapHex( Integer.toString(hexId-1)));
                        MovementCalculator(GetMapHex(Integer.toString(hexId-1)),
                                movingUnit, 
                                moveAllowance, /*- function call for move modification */
                                validHexes);
                    }
                }
                
            }
            // If the 2nd most significant digit of the hex id is odd...
            else if( (hexId/100)%2 == 1 )
            {
                
            }
        }
        
    }// End MovementCalculator
        
    
    
    
}// End MainMap class


