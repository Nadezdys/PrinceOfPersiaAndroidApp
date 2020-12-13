package com.example.tamz2_pop_projekt;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LevelBuilder {

    private List<List<GameObject>> objects;
    private Bitmap[][] blockBitmap;
    private Bitmap[][] spikeBitmap;
    private Bitmap[][] charBitmap;
    private Bitmap[][] gateBitmap;
    private GameSurface gameSurface;
    private Context context;

    private List<MainCharacter> character;
    private Gate gate = null;

    private String objectType = new String();

    public LevelBuilder(Context context , Bitmap[][] blockBitmap, Bitmap[][] spikeBitmap,  Bitmap[][] charBitmap, Bitmap[][] gateBitmap, GameSurface gameSurface){
        this.objects = new ArrayList<>();

        this.context = context;

        this.blockBitmap = blockBitmap;
        this.spikeBitmap = spikeBitmap;
        this.charBitmap = charBitmap;
        this.gateBitmap = gateBitmap;

        this.gameSurface = gameSurface;

    }
    public List<List<GameObject>> XmlParseLevel(String name){

        AssetManager assetManager = this.context.getAssets();
        InputStream input;

        try
        {
            input = assetManager.open(name);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(input);
            doc.getDocumentElement().normalize();


            NodeList nodeList = doc.getElementsByTagName("object");

            //int screens = nodeList.

            this.objects.add(new ArrayList<GameObject>());
            this.objects.add(new ArrayList<GameObject>());
            this.objects.add(new ArrayList<GameObject>());

            this.character = new ArrayList<>();

            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);

                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;

                    this.objectType = eElement.getAttribute("typ").toString();
                    if(this.objectType.equals("block")){
                        this.objects.get(Integer.parseInt(eElement.getElementsByTagName("screen").item(0).getTextContent()) - 1).
                                add(new Block(this.gameSurface, this.blockBitmap,
                                        Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent()),
                                        Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent()),
                                        Integer.parseInt(eElement.getElementsByTagName("type").item(0).getTextContent())
                                ));
                    }else if(this.objectType.equals("spike")){
                        this.objects.get(Integer.parseInt(eElement.getElementsByTagName("screen").item(0).getTextContent()) - 1).
                                add(new Spike(this.gameSurface, this.spikeBitmap,
                                        Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent()),
                                        Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent()),
                                        Integer.parseInt(eElement.getElementsByTagName("type").item(0).getTextContent()
                                )));
                    }else if(this.objectType.equals("character")){
                        this.character.
                                add(new MainCharacter(this.gameSurface, this.charBitmap,
                                        Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent()),
                                        Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent())
                                ));
                    }else if(this.objectType.equals("gate")){
                        this.gate = new Gate(this.gameSurface, this.gateBitmap,
                                        Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent()),
                                        Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent()));
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return this.objects;
    }

    public List<MainCharacter> getCharacter(){
        return this.character;
    }

    public Gate getGate(){
        return this.gate;
    }
}

