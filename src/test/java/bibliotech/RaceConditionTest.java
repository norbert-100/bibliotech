package bibliotech;
import com.bibliotech.dao.EmpruntDAO;

public class RaceConditionTest {

    public static void main(String[] args) {

        EmpruntDAO dao1 = new EmpruntDAO();
        EmpruntDAO dao2 = new EmpruntDAO();

        Thread t1 = new Thread(() -> {
            try {
                dao1.enregistrerEmprunt(1, 1, 14);
                System.out.println("Emprunt 1 réussi");
            } catch (Exception e) {
                System.out.println("Emprunt 1 échoué : " + e.getMessage());
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                dao2.enregistrerEmprunt(1, 1, 14);
                System.out.println("Emprunt 2 réussi");
            } catch (Exception e) {
                System.out.println("Emprunt 2 échoué : " + e.getMessage());
            }
        });

        t1.start();
        t2.start();
    }
}