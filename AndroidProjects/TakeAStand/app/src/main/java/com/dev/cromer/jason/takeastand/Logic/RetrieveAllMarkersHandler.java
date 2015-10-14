package com.dev.cromer.jason.takeastand.Logic;



import com.dev.cromer.jason.takeastand.networking.GenericHttpGetRequest;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class RetrieveAllMarkersHandler {

    private String url;
    private GenericHttpGetRequest httpGetRequest;
    private String result;
    private String[] markerInfoList;
    private float markerLatitude;
    private float markerLongitude;
    private int religionTypeMappedInteger;
    private MarkerOptions currentMarker;

    //Religion value constants
    private static final int INT_CHRISTIAN = 1;
    private static final int INT_ISLAM = 2;
    private static final int INT_CATHOLIC = 3;
    private static final int INT_HINDU = 4;
    private static final int INT_BUDDHIST = 5;
    private static final int INT_AGNOSTIC = 6;
    private static final int INT_ATHIEST = 7;



    public RetrieveAllMarkersHandler(String url, GenericHttpGetRequest request){
        this.url = url;
        this.httpGetRequest = request;
    }

    public String fetchMarkers(){

        //Attempt GET request
        try{
            result = httpGetRequest.execute(url).get();
        }
        catch(ExecutionException | InterruptedException e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public String[] parseMarkerResult(String input){
        if(input != null){
            //Remove jsonified componenets from retrieved list
            input = input.replace("[","").replace("]","").replace("\"", "");

            //Convert the input to an Array
            markerInfoList = input.split(",");
        }
        else{
            return null;
        }
        return markerInfoList;
    }

    public HashMap<MarkerOptions, Integer> mapMarkerItems(String[] stringInfoArray){
        /*
            The list passed in is composed of 3 items i.e. [latitude, longitude, integer,....]
            We will use the lat/lng to create a marker and map it to the integer
         */

        if(stringInfoArray != null){
            for(int i = 0; i < stringInfoArray.length; i+=3) {
                //Lat/lng values must be floats
                markerLatitude = Float.valueOf(stringInfoArray[i]);
                markerLongitude = Float.valueOf(stringInfoArray[i + 1]);
                religionTypeMappedInteger = Integer.valueOf(stringInfoArray[i + 2]);

                currentMarker = new MarkerOptions().position(new LatLng(markerLatitude, markerLongitude))
                        .icon()

            }
        }



        return null;
    }

    private float getMarkerHue(int religionType){
        //return hue depending on religion type
        switch(religionType){
            case INT_CHRISTIAN:
                return BitmapDescriptorFactory.HUE_AZURE;
            case

        }

        
    }
}