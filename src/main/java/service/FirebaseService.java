package service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import model.User;
import model.Transaction;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@ApplicationScoped
public class FirebaseService {

    private static final String USER_COLLECTION = "users";
    private static final String TRANSACTION_COLLECTION = "transactions";

    public Firestore getDb() {
        return FirestoreClient.getFirestore();
    }

    public void saveUser(User user) throws ExecutionException, InterruptedException {
        getDb().collection(USER_COLLECTION).document(user.getId()).set(user).get();
    }

    public User getUserByEmail(String email) throws ExecutionException, InterruptedException {
        CollectionReference users = getDb().collection(USER_COLLECTION);
        Query query = users.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();
        if (!docs.isEmpty()) {
            return docs.get(0).toObject(User.class);
        }
        return null;
    }

    public void saveTransaction(Transaction tx) throws ExecutionException, InterruptedException {
        getDb().collection(TRANSACTION_COLLECTION).document(tx.getId()).set(tx).get();
    }

    public List<Transaction> getUserTransactions(String userId, String year, String month)
            throws ExecutionException, InterruptedException {
        CollectionReference txRef = getDb().collection(TRANSACTION_COLLECTION);
        Query query = txRef.whereEqualTo("userId", userId)
                           .whereEqualTo("year", year)
                           .whereEqualTo("month", month);
        List<QueryDocumentSnapshot> docs = query.get().get().getDocuments();
        return docs.stream().map(doc -> doc.toObject(Transaction.class)).collect(Collectors.toList());
    }

    public void deleteTransaction(String id) throws ExecutionException, InterruptedException {
        getDb().collection(TRANSACTION_COLLECTION).document(id).delete().get();
    }
}