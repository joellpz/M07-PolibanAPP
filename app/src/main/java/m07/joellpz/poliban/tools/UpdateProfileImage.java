package m07.joellpz.poliban.tools;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

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


}
