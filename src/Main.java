import dao.Iimpl.AgentDao;
import dao.Iimpl.DepartementDao;
import dao.Iimpl.PaiementDao;
import service.Iimpl.DirecteurServiceImpl;
import model.Agent;
import view.MenuDirecteur;
import utils.DBConnection;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            DBConnection dbConnection = DBConnection.getInstance();
            Connection connection = dbConnection.getConnection();
            if (connection == null) {
                System.err.println("Impossible de se connecter à la base de données.");
                return;
            }
            AgentDao agentDao = new AgentDao(connection);
            DepartementDao departementDao = new DepartementDao(connection);
            PaiementDao paiementDao = new PaiementDao(connection);
            DirecteurServiceImpl directeurService = new DirecteurServiceImpl(agentDao, paiementDao, departementDao);
            Agent directeur = agentDao.lireParId(1);
            MenuDirecteur menuDirecteur = new MenuDirecteur(directeur, directeurService);
            menuDirecteur.afficherMenu();
        } catch (Exception e) {
            System.err.println("Erreur lors du démarrage de l'application :");
            e.printStackTrace();
        } finally {
            DBConnection.getInstance().closeConnection();
            System.out.println("Fermeture de l'application. À bientôt !");
        }
    }
}