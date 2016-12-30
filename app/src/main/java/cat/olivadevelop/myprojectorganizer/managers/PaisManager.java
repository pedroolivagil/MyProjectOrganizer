package cat.olivadevelop.myprojectorganizer.managers;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cat.olivadevelop.myprojectorganizer.tools.Tools;

import static cat.olivadevelop.myprojectorganizer.tools.Tools.HOSTNAME;

/**
 * Created by Oliva on 30/12/2016.
 */

public class PaisManager {
    public static final String PAISES_LIST = "paises";
    public static final String PAISES_ID = "id";
    public static final String PAISES_ISO = "iso";
    public static final String PAISES_NOMBRE = "nombre";

    public static List<Pais> downloadCountryList(Activity a) throws ExecutionException, InterruptedException {
        if (Tools.isNetworkAvailable(a)) {
            CountryLoader cl = new CountryLoader(a, HOSTNAME + "/php/project_manager/paises.php");
            cl.execute();
            return cl.get();
        } else {
            return new ArrayList<Pais>();
        }
    }
}
