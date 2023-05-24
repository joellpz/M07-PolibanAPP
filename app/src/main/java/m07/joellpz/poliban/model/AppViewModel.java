package m07.joellpz.poliban.model;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel class for the application.
 */
public class AppViewModel extends AndroidViewModel {

    /**
     * Inner class representing media information.
     */
    public static class Media {
        public final Uri uri;
        public final String tipo;

        /**
         * Constructor for Media class.
         *
         * @param uri  The URI of the media.
         * @param tipo The type of the media.
         */
        public Media(Uri uri, String tipo) {
            this.uri = uri;
            this.tipo = tipo;
        }
    }

    /**
     * LiveData object for the selected media.
     */
    public final MutableLiveData<Media> mediaSeleccionado = new MutableLiveData<>();

    /**
     * Constructor for AppViewModel class.
     *
     * @param application The application instance.
     */
    public AppViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Sets the selected media.
     *
     * @param uri  The URI of the selected media.
     * @param type The type of the selected media.
     */
    public void setMediaSeleccionado(Uri uri, String type) {
        mediaSeleccionado.setValue(new Media(uri, type));
    }
}
