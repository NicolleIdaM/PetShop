import java.util.ArrayList;
import java.time.LocalDate;
import java.io.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;

public class PetInterface extends JFrame{
   private int largura,altura;
   private JButton button;
   private static JTextArea logArea=new JTextArea("",10,66);
   static int sizeLog=0;
   private static String arquivo = "Dados.dat";
   private static ArrayList<Tutor> tut=new ArrayList<Tutor>();
   private JTextField nome, endereco, dia, mes, ano;
   private static final String[] option={"Cadastrar", "Imprimir", "Buscar",  "Excluir", "Encerrar"};
   int cad = recuperarDados();
   static Font f=new Font("Consolas", Font.PLAIN, 13);
   
   public static int geraCod(){
      if (tut.size() == 0){
         return 1;
      }else{
         return tut.get(tut.size() - 1).getCod() + 1;
      }
   }
    
   //INTERFACE
   public PetInterface(int largura, int altura){
      super("CADASTRO DE PESSOAS");

      System.out.println("Cadastros: "+cad);
      this.largura=largura;
      this.altura=altura;
      
      setLayout(new FlowLayout());
      JPanel painel=new JPanel(); 
      painel.setPreferredSize(new Dimension(largura,altura));
      add(painel);
      pack();
      BufferedImage imgDecor=null;
      
      try{
         File file = new File("pets.jpg"); 
         FileInputStream fis = new FileInputStream(file);
         imgDecor=ImageIO.read(fis);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
      
      // QUANTIADE DE BOTÕES  
      ButtonHandler handler=new ButtonHandler();
      for(int k=0;k<5;k++){ 
         button=new JButton(option[k]);
         painel.add(button);
         if(k!=4)
            button.addActionListener(handler); 
      }
      
      // BOTÃO ENCERRAR
      button.addActionListener(e->{
         PetInterface.this.dispose();
         System.exit(0);
      });
      
      painel.add(new JLabel("Log de acões realizadas na sessão."));
      Box box = Box.createHorizontalBox();
      logArea.setFont(f);
      logArea.setEditable(false);
      box.add(new JScrollPane(logArea));
      painel.add(box);
      
      // PAINEL DECORATIVO
      JPanel decor=new JPanel();
      decor.setLayout(new FlowLayout());
      JLabel imgLabel=new JLabel(new ImageIcon(imgDecor));
      decor.add(imgLabel);
      painel.add(decor);
   }
   
   // AÇÕES DOS BOTÕES
   private class ButtonHandler implements ActionListener{
      public void actionPerformed(ActionEvent event){
         if(event.getActionCommand()==option[0]){ 
            cadastrar();
         } 
         if(event.getActionCommand()==option[1]){
            imprimirCadastro();
         }          
         if(event.getActionCommand()==option[2]){
            buscarPet();
         }
         if(event.getActionCommand()==option[3]){
            excluirPet();
         }
      }
   }
   
   public static void writeLog(String s){ 
      if (sizeLog==0)
         logArea.append("LOG CRIADO...");
      else {
         logArea.append("\n");
         logArea.append("- "+s);
      }    
      sizeLog++;    
   }
   
   // RECUPERA DADOS DO ARQUIVO "Dados"
   public static int recuperarDados() {
      int count = 0;
      try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(arquivo))) {
         if (!tut.isEmpty()) tut.clear();
         Object obj;
         while ((obj = inputStream.readObject()) != null) {
            if (obj instanceof Tutor) {
               tut.add((Tutor) obj);
               count++;
            }
         }
      } catch (EOFException ex) {
         writeLog("Fim da leitura do arquivo " + arquivo + ".");
      } catch (FileNotFoundException ex) {
         writeLog("Arquivo de dados inexistente.");
      } catch (Exception ex) {
         writeLog(ex.getMessage());
      }
         return count;
   }

   // GRAVA DADOS NO ARQUIVO "Dados"
   public static void gravarDados() {
      try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(arquivo))) {
         for (Tutor p : tut) {
            outputStream.writeObject(p);
         }
      } catch (FileNotFoundException ex) {
         writeLog("Arquivo de dados não encontrado.");
      } catch (IOException ex) {
         writeLog(ex.getMessage());
      }
   }
   
   // CADASTRO DE TUTOR E PET
   private void cadastrar() {
      cadastrarFrame cf = new cadastrarFrame();
      cf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      cf.setSize(largura - 45, 400);
      cf.setLocationRelativeTo(null);
      cf.setVisible(true);
   }
   
   private class cadastrarFrame extends JFrame {
      private JButton ok;
      private JTextField nomePet, diaPet, mesPet, anoPet, tipoPet, numPet;
      private JTabbedPane abas;

      public cadastrarFrame() {
      super("CADASTRO TUTOR E PET");
      setLayout(new FlowLayout(FlowLayout.LEADING));

      // CADASTRO DO TUTOR
      add(new JLabel("Nome Tutor"));
      nome = new JTextField("", 35);
      add(nome);

      add(new JLabel("Endereco Tutor"));
      endereco = new JTextField("", 20);
      add(endereco);

      add(new JLabel("Data Nasc. Tutor (dd/mm/aaaa)"));
      dia = new JTextField(4);
      add(dia);
      add(new JLabel("/"));
      mes = new JTextField(4);
      add(mes);
      add(new JLabel("/"));
      ano = new JTextField(4);
      add(ano);

      add(new JLabel("Digite o número de pets para cadastrar"));
      numPet = new JTextField(3);
      add(numPet);
      
      JButton qntdPet = new JButton("Confirmar");
      qntdPet.addActionListener(e -> {
         int qntd = 0;
         try {
            qntd = Integer.parseInt(numPet.getText());
         } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(PetInterface.this, "Digite um número válido para a quantidade de pets.");
            return;
         }

         // CRIA ABAS PARA O CADASTRO DO(S) PET(S)
         for (int i = 0; i < qntd; i++) {
            JPanel petPanel = new JPanel();
            petPanel.setLayout(new BoxLayout(petPanel, BoxLayout.Y_AXIS));

            JPanel nomePetPanel = new JPanel();
            nomePetPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            nomePetPanel.add(new JLabel("Nome Pet " + (i + 1)));
            nomePet = new JTextField(20);
            nomePetPanel.add(nomePet);
            petPanel.add(nomePetPanel);

            JPanel dataPetPanel = new JPanel();
            dataPetPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            dataPetPanel.add(new JLabel("Data Nasc. Pet " + (i + 1) + " (dd/mm/aaaa)"));
            diaPet = new JTextField(4);
            dataPetPanel.add(diaPet);
            dataPetPanel.add(new JLabel("/"));
            mesPet = new JTextField(4);
            dataPetPanel.add(mesPet);
            dataPetPanel.add(new JLabel("/"));
            anoPet = new JTextField(4);
            dataPetPanel.add(anoPet);
            petPanel.add(dataPetPanel);

            JPanel tipoPetPanel = new JPanel();
            tipoPetPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            tipoPetPanel.add(new JLabel("Tipo de Pet " + (i + 1)));
            tipoPet = new JTextField(10);
            tipoPetPanel.add(tipoPet);
            petPanel.add(tipoPetPanel);

            abas.addTab("Pet " + (i + 1), petPanel);
         }

         // REMOVE BOTÃO CONFIRMAR APÓS CONFIRMAÇÃO DO USUÁRIO
         qntdPet.setVisible(false);
      });
      add(qntdPet);
      
      abas = new JTabbedPane();
      add(abas);

      ok = new JButton("Cadastrar");
      add(ok);

      ok.addActionListener(e -> {
         String n = nome.getText(), ed = endereco.getText(), d = dia.getText(), m = mes.getText(), a = ano.getText();
         String np, tp, dp, mp, ap;
         int D = 0, M = 0, A = 0;

         if (n.isEmpty() || ed.isEmpty() || d.isEmpty() || m.isEmpty() || a.isEmpty()) {
            JOptionPane.showMessageDialog(PetInterface.this, "Todos os campos do tutor devem ser preenchidos!");
            return;
         }

         try {
            D = Integer.parseInt(d);
            M = Integer.parseInt(m);
            A = Integer.parseInt(a);
         } catch (Exception ex) {
            JOptionPane.showMessageDialog(PetInterface.this, "Todos os campos de data do tutor devem ser numéricos!");
            return;
         }

         try {
            LocalDate dataTutor = LocalDate.of(A, M, D);
            Tutor tutor = new Tutor(geraCod(), n, ed, D, M, A);

            int qntd = abas.getTabCount();
            int petCad = 0;

            for (int i = 0; i < qntd; i++) {
               JPanel petPanel = (JPanel) abas.getComponentAt(i);
               nomePet = (JTextField) ((JPanel) petPanel.getComponent(0)).getComponent(1);
               tipoPet = (JTextField) ((JPanel) petPanel.getComponent(2)).getComponent(1);
               diaPet = (JTextField) ((JPanel) petPanel.getComponent(1)).getComponent(1);
               mesPet = (JTextField) ((JPanel) petPanel.getComponent(1)).getComponent(3);
               anoPet = (JTextField) ((JPanel) petPanel.getComponent(1)).getComponent(5);

               np = nomePet.getText();
               tp = tipoPet.getText();
               dp = diaPet.getText();
               mp = mesPet.getText();
               ap = anoPet.getText();

               int Dp = 0, Mp = 0, Ap = 0;
               if (!np.isEmpty() && !dp.isEmpty() && !mp.isEmpty() && !ap.isEmpty() && !tp.isEmpty()) {
                  try {
                     Dp = Integer.parseInt(dp);
                     Mp = Integer.parseInt(mp);
                     Ap = Integer.parseInt(ap);
                     LocalDate dataPet = LocalDate.of(Ap, Mp, Dp);
                     Pet pet = new Pet(np, tp, Dp, Mp, Ap);
                     tutor.adicionarPet(pet);
                     petCad++;
                  } catch (Exception ex) {
                     JOptionPane.showMessageDialog(PetInterface.this, "Todos os campos de data do pet devem ser numéricos!");
                     return;
                  }
               }
            }

            int qntdP = Integer.parseInt(numPet.getText());
            if (petCad < qntdP) {
               JOptionPane.showMessageDialog(PetInterface.this, "Você informou que queria cadastrar " + qntdP + " pets. Não se esqueça de completar o cadastro!");
               return;
            }

            tut.add(tutor);
            gravarDados();

            String msg = String.format("Cadastrado Tutor: %s", n);
            if (!nomePet.getText().isEmpty()) {
               msg += String.format(" - Pet: %s", nomePet.getText());
            }
            JOptionPane.showMessageDialog(PetInterface.this, msg);
            writeLog(msg);
            
            nome.setText(""); endereco.setText(""); dia.setText(""); mes.setText(""); ano.setText("");
            numPet.setText("");
            abas.removeAll();
            dispose();
            } catch (Exception ex) {
               JOptionPane.showMessageDialog(PetInterface.this, ex.toString());
            }
         });
      }
   }

   
   // IMPRIME DADOS GRAVADOS
   public void imprimirCadastro(){
      cadTutorPet ctp=new cadTutorPet();
      ctp.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      ctp.setSize(largura-25,215);
      ctp.setLocationRelativeTo(null);
      ctp.setVisible(true);
   }
   
   //CADASTRO DE TUTOR E PET
   private class cadTutorPet extends JFrame {
      private static final JTextArea cadArea = new JTextArea(10, 64);
   
      public cadTutorPet() {
         super("CADASTRO IMPRESSO");
         cadArea.setEditable(false);
         cadArea.setFont(f);
           
         setLayout(new FlowLayout(FlowLayout.LEADING));
         add(new JScrollPane(cadArea));
           
         // LIMPA ÁREA DE TEXTO ANTES DE ADICIONAR NOVAS INFORMAÇÕES
         cadArea.setText("");
   
         if (!tut.isEmpty()) {
            cadArea.append("  #  Nome           Dt.nasc.      Idade         Endereco    \n");
            cadArea.append("--------------------------------------------------------------\n");
            tut.forEach(p -> cadArea.append(p.toString() + "\n"));
            writeLog("Cadastro impresso.");
         } else {
            cadArea.append("<Cadastro vazio>");
            writeLog("Impressão: <cadastro vazio>");
         }
      }
   }
   
   //BUSCAR PET POR CODIGO DO TUTOR
   public void buscarPet() {
      JFrame jf = new JFrame("BUSCA PET POR CÓDIGO DO TUTOR");
      jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      jf.setSize(largura - 1, 215);
      jf.setLocationRelativeTo(null);
      jf.setLayout(new FlowLayout(FlowLayout.LEADING));
   
      JTextArea resBusca = new JTextArea("", 8, 67);
      resBusca.setFont(f);
      resBusca.setText("");
      resBusca.setEditable(false);
   
      JTextField codBus = new JTextField(3);
      Box box = Box.createHorizontalBox();
      box.add(new JScrollPane(resBusca));
   
      JButton buscar = new JButton("Buscar");
      buscar.addActionListener(e -> {
         int c = Integer.parseInt(codBus.getText());
         Tutor pbusca = null;
         boolean localizado = false;
           
         for (Tutor p : tut) {
            if (p.getCod() == c) {
               localizado = true;
               pbusca = p;
               break;
            }
         }
   
         if (localizado) {
            if (pbusca.getPets().isEmpty()) {
               resBusca.setText("O Tutor com código " + c + " não possui pets.");
            } else {
               String msg = "  Nome                     Dt.nasc.                    Tipo\n";
               msg += "-----------------------------------------------------------------\n";
               msg += pbusca.toString1();
               resBusca.setText(msg);
            }
               writeLog("Busca de pets do tutor com código " + c);
            } else {
               String msg = "Pessoa com código " + c + " inexistente.";
               resBusca.setText(msg);
               writeLog(msg);
           }
       });
   
      jf.add(new JLabel("Código do Tutor: "));
      jf.add(codBus);
      jf.add(buscar);
      jf.add(box);
      jf.setVisible(true);
   }

   
   // EXCLUIR PETS PELO CÓDIGO DO TUTOR
   private void excluirPet() {
      JFrame jf = new JFrame("EXCLUIR PET(S) POR CÓDIGO DO TUTOR");
      jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      jf.setSize(largura - 1, 215);
      jf.setLocationRelativeTo(null);
      jf.setLayout(new FlowLayout(FlowLayout.LEADING));
   
      JTextArea resBusca = new JTextArea("", 5, 67);
      resBusca.setFont(f);
      resBusca.setEditable(false);
      resBusca.setLineWrap(true);
      resBusca.setWrapStyleWord(true);
   
      JTextField codBus = new JTextField(3);
   
      Box box = Box.createHorizontalBox();
      box.add(new JScrollPane(resBusca));
   
      JButton excluir = new JButton("Excluir");
      excluir.addActionListener(e -> {
         int codTutor = Integer.parseInt(codBus.getText());
         Tutor tutorEncontrado = null;
         boolean tutorLocalizado = false;
   
         for (Tutor tutor : tut) {
            if (tutor.getCod() == codTutor) {
               tutorLocalizado = true;
               tutorEncontrado = tutor;
               break;
            }
         }
   
         if (tutorLocalizado) {
            if (!tutorEncontrado.getPets().isEmpty()) {
               tutorEncontrado.getPets().clear();
               gravarDados();
               String msg = "Todos os pets do tutor " + tutorEncontrado.getNome() + " foram excluídos com sucesso.";
               resBusca.setText(msg);
               writeLog(msg);
            } else {
               String msg = "O Tutor com código " + codTutor + " não tem pets para excluir.";
               resBusca.setText(msg);
               writeLog(msg);
            }
         } else {
            String msg = "Tutor com código " + codTutor + " não encontrado.";
            resBusca.setText(msg);
            writeLog(msg);
         }
      });
   
      jf.add(new JLabel("Código do Tutor: "));
      jf.add(codBus);
      jf.add(excluir);
      jf.add(box);
      jf.setVisible(true);
   }
}