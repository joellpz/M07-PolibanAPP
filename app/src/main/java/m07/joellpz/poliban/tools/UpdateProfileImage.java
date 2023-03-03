package m07.joellpz.poliban.tools;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class UpdateProfileImage {

    public static void pujaIguardarEnFirestore(final Uri mediaUri,FirebaseUser user) {
        FirebaseStorage.getInstance().getReference("usersPhoto/" +
                        UUID.randomUUID())
                .putFile(mediaUri)
                .continueWithTask(task ->
                        task.getResult().getStorage().getDownloadUrl())
                .addOnSuccessListener(url ->
                        FirebaseFirestore.getInstance().collection("users")
                                .document(user.getUid()).update("profilePhoto", url.toString())
                );
    }

    public static Map<String, Object> getUserData(FirebaseUser user){
        System.out.println(user.getUid());
        Map<String, Object> userData = new HashMap<>();
        FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot docSnap = task.getResult();
                            if (docSnap.exists()) {
                                userData.put("profileName",docSnap.get("profileName"));
                                userData.put("profilePhone",docSnap.get("profilePhone"));
                                userData.put("profileDirection",docSnap.get("profileDirection"));
                                userData.put("profileCP",docSnap.get("profileCP"));
                                userData.put("profilePhoto",docSnap.get("profilePhoto"));
                                System.out.println("ENCOTRADO");
                            } else {
                                System.out.println("Documento no encontrado");
                            }
                        } else {
                            System.out.println(task.getException().getMessage());
                        }
                    }
                });
        System.out.println("HOLA");
        return null;
    }



}
