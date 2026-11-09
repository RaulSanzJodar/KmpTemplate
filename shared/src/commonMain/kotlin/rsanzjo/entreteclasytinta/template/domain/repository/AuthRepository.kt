package rsanzjo.entreteclasytinta.template.domain.repository

interface AuthRepository {
    suspend fun signIn(email: String, password: String)
    suspend fun signOut()
    suspend fun isSignedIn(): Boolean
}