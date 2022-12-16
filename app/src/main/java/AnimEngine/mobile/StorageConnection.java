package AnimEngine.mobile;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class StorageConnection implements storageInterface{

    final private StorageReference storageRef; //create refernce object

    public StorageConnection(String folder){
        FirebaseStorage storage = FirebaseStorage.getInstance(); //the fire-base's singelton
        this.storageRef = storage.getReference(folder);  // Create a storage reference to specific folder using singelton
    }

    public void requestFile(String name, OnSuccessListener<byte[]> lambda){
        byte[] ret;
        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference imageRef = this.storageRef.child(name); //get access to the specific string name
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(lambda); //when the DB found the image, the lambda begin
    }

    public byte[][] requestImages(String[] names){return null;}


    public void uploadImage(String name, byte[] img){
        StorageReference imgRef = this.storageRef.child(name); //create ref to the string name in the storage

        UploadTask uploadTask = imgRef.putBytes(img);//upload the byte[] (in the storage: from byte[] to image)

        uploadTask.addOnFailureListener(new OnFailureListener() { //if didn't success the upload
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() { //if success the upload
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }


    public void uploadImages(String[] names, byte[][] imgs){}
}
