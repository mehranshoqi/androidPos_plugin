package ir.pigi.android_pos;

import android.content.res.Resources;

import ir.pigi.android_pos.ks8223.PosCommKS8223;
import ir.pigi.android_pos.p1000.PosCommP1000;
import android.util.Log;

public class PosCommFactory {
    IPosComm posCommP1000 = null;
    IPosComm posCommKS8223 = null;
    public IPosComm getPosComm(){
        if(Utility.isPosP1000()){
            if(posCommP1000 == null) {
                Log.d("P1000", "getPosComm: called");
                posCommP1000 = new PosCommP1000();
            }

                return posCommP1000;
        }
        else if(Utility.isPosKS8223()){
            Log.d("KS8223", "getPosComm: instantiating");
            if(posCommKS8223 == null){
                Log.d("KS8223", "getPosComm: called");
                posCommKS8223 = new PosCommKS8223();
            }
            return posCommKS8223;
        }
        else
            throw new Resources.NotFoundException();
    }
}
