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

    public static User read(String idUsuario){
        // obtenemos los datos del usuario mediante el id
        User user = new User();
        return user;
    }

    public static void create(Activity activity, User user) {
        new CreateUser(activity, HOSTNAME + "/php/user_manager/create.php").execute(new FormBody.Builder()
                .add("id_usuario", user.getId_usuario())
                .add("correo", user.getCorreo())
                .add("user_pass", user.getPassword())
                .add("nif", user.getNif())
                .add("phone", user.getPhone())
                .add("pais", String.valueOf(user.getPais().getId()))
                .add("poblacion", user.getPoblacion())
                .build());
    }

    public static void update(Activity activity, User user) {
        /*new UpdateUser(activity, HOSTNAME + "/php/user_manager/create.php").execute(new FormBody.Builder()
                .add("id_usuario", user.getId_usuario())
                .add("correo", user.getCorreo())
                .add("user_pass", user.getPassword())
                .add("nif",user.getNif())
                .add("phone",user.getPhone())
                .add("pais", String.valueOf(user.getPais().getId()))
                .add("poblacion",user.getPoblacion())
                .build());*/
    }

    public static void delete(Activity activity, User user) {
        /*new DeleteUser(activity, HOSTNAME + "/php/user_manager/create.php").execute(new FormBody.Builder()
                .add("id_usuario", user.getId_usuario())
                .add("flag_activo",false)
                .build());*/
    }

    public static void login() {

    }

    public static void logout() {

    }

}
