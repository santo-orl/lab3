package it.polito.lab4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

import it.polito.lab4.User
import it.polito.lab4.skills.Skill
import kotlinx.android.synthetic.main.activity_login.*
import java.util.HashMap

//DA PASSARE A FRAMMENTO
class LoginActivity : AppCompatActivity() {
    private companion object LoginActivity {
        private const val TAG = "LoginActivity"
        private const val RC_GOOGLE_SIGN_IN = 4926
    }
    private var email = ""
    private lateinit var name: String
    private lateinit var auth: FirebaseAuth

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("TEST", "LOGIN ")
        setContentView(R.layout.activity_login)
        setContentView(R.layout.activity_login)
        // Initialize Firebase Auth
        auth = Firebase.auth

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client))
                .requestEmail()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        btnSignIn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent,
                 RC_GOOGLE_SIGN_IN
            )
        }
        super.onCreate(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                email = account.email.toString()
                Log.i("test_login",email)
                name = account.displayName.toString()
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                val map: MutableMap<String, Any> = HashMap()
                map["email"] = email
                map["name"] = name

                db.collection("users").document(email).set(map as Map<String, Any>, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast
                            .makeText(this,"user logged in",Toast.LENGTH_SHORT)
                            .show()
                        Toast
                            .makeText(this,"Two hours-credit have been assigned to your profile",
                                Toast.LENGTH_LONG)
                            .show()

                    }
                    .addOnFailureListener{
                        Toast
                            .makeText(this,"user not created",Toast.LENGTH_SHORT)
                            .show()
                    }



                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }

        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Log.i("uid", uid.toString())
        if(currentUser!=null){
            println("utente già loggato "+currentUser.email.toString())
            email = currentUser.email.toString()
            name = currentUser.displayName.toString()
            val map: MutableMap<String, String> = HashMap()
            map["email"] = email
            map["name"] = name
            db.collection("users").document(email).update(map as Map<String, Any>)

        }
        updateUI(currentUser)
        super.onStart()
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            Log.w(TAG, "user not signed in..")
           return
        }
        val i = Intent(this, MainActivity::class.java)
        Log.i("test_login",email)
        i.putExtra("id",email)
        i.putExtra("name",name)
        startActivity(i)
        finish()
        // Navigate to MainActivity
    }

}