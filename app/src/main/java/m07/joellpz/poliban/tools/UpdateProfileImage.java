package m07.joellpz.poliban.tools;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.UUID;

/**
 * Utility class for updating user profile image.
 */
public class UpdateProfileImage {

    /**
     * Uploads and saves the profile image in Firebase Storage and updates the user's profilePhoto field in Firestore.
     *
     * @param mediaUri The URI of the profile image.
     * @param user     The FirebaseUser object representing the current user.
     */
    public static void pujaGuardarEnFirestore(final Uri mediaUri, FirebaseUser user) {
        FirebaseStorage.getInstance().getReference("usersPhoto/" + UUID.randomUUID())
                .putFile(mediaUri)
                .continueWithTask(task -> task.getResult().getStorage().getDownloadUrl())
                .addOnSuccessListener(url -> FirebaseFirestore.getInstance().collection("users")
                        .document(user.getUid()).update("profilePhoto", url.toString()));
    }
}
