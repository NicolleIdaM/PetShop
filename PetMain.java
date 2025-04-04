import javax.swing.JFrame;
public class PetMain{
   public static void main(String[] args){
      PetInterface pi=new PetInterface(500,350);
      pi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      pi.setLocationRelativeTo(null);
      pi.setVisible(true);
   }
}