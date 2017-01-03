package cat.olivadevelop.myprojectorganizer.managers;

import android.app.Activity;

import cat.olivadevelop.myprojectorganizer.entities.User;
import cat.olivadevelop.myprojectorganizer.manageData.CreateUser;
import okhttp3.FormBody;

import static cat.olivadevelop.myprojectorganizer.tools.Tools.HOSTNAME;

/**
 * Created by Oliva on 29/12/2016.
 */

public class UserManager {

    // create
    // delete
    // update
    // login
    // logout

    public static void create(Activity activity, User user) {
        new CreateUser(activity, HOSTNAME + "/php/user_manager/create.php").execute(new FormBody.Builder()
                .add("id_usuario", user.getId_usuario())
                .add("correo", user.getCorreo())
                .add("user_pass", user.getPassword())
                .add("nif",user.getNif())
                .add("phone",user.getPhone())
                .add("pais", String.valueOf(user.getPais().getId()))
                .add("poblacion",user.getPoblacion())
                .build());
    }
}
