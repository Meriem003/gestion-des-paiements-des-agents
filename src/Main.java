import controller.AuthController;

public class Main {
    public static void main(String[] args) {
        try {
            AuthController authController = new AuthController();            
            authController.afficherEcranLogin();
            
        } catch (Exception e) {
            System.err.println("Erreur critique lors du démarrage de l'application :");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}