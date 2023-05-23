package m07.joellpz.poliban.model;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AppViewModel extends AndroidViewModel {
    public List<BankAccount> bankAccountList = new ArrayList<>();

    public interface petitionCallback {
        void onCallback(List<BankAccount> value);
    }

    public static class Media {
        public final Uri uri;
        public final String tipo;

        public Media(Uri uri, String tipo) {
            this.uri = uri;
            this.tipo = tipo;
        }
    }


    public void getUserAccounts(FirebaseUser user, final petitionCallback myCallback) {
        bankAccountList = new ArrayList<>();
        new Thread(() -> {
            final List<BankAccount> bankAccountList = new ArrayList<>();

            try {
                Tasks.await(FirebaseFirestore.getInstance().collection("bankAccount")
                        .whereEqualTo("userId", user.getUid())
                        .get()
                        .addOnSuccessListener(docSnap -> {
                            docSnap.forEach(doc -> bankAccountList.add(doc.toObject(BankAccount.class)));

                            // Ejecutar el callback en el hilo secundario
                            myCallback.onCallback(bankAccountList);
                        }));
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Aquí puedes realizar cualquier otra acción posterior a la consulta en el hilo secundario
        }).start();
    }

    private void addToBankAccount(BankAccount account) {
        bankAccountList.add(account);
    }

    public void getBankAccountsFirebase() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    }


    public void setBankAccountList(List<BankAccount> bankAccountList) {
        this.bankAccountList = bankAccountList;
    }

    public List<BankAccount> getBankAccountList() {
        return bankAccountList;
    }

    public BankAccount getBankAccountElement(int position) {
        return bankAccountList.get(position);
    }

    //public MutableLiveData<Post> postSeleccionado = new MutableLiveData<>();
    public final MutableLiveData<Media> mediaSeleccionado = new MutableLiveData<>();

    public AppViewModel(@NonNull Application application) {
        super(application);
    }

    public void setMediaSeleccionado(Uri uri, String type) {
        mediaSeleccionado.setValue(new Media(uri, type));
    }
}