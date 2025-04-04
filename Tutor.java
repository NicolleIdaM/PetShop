import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;
import java.io.Serializable;

public class Tutor implements Serializable{
   private String nome;
   private LocalDate data;
   private String endereco;
   private int cod;
   private ArrayList<Pet> pets = new ArrayList<Pet>();

   public Tutor(int cod, String nome, String endereco, int dia, int mes, int ano) {
      this.cod = cod;
      this.nome = nome;
      this.endereco = endereco;
      this.data = LocalDate.of(ano, mes, dia);
   }

   public String getNome() {
      return nome;
   }

   public LocalDate getData() {
      return data;
   }

   public String getEndereco() {
      return endereco;
   }

   public int getIdade() {
      return Period.between(this.data, LocalDate.now()).getYears();
   }

   public int getCod() {
      return cod;
   }

   public ArrayList<Pet> getPets() {
      return pets;
   }

   public void adicionarPet(Pet pet) {
      this.pets.add(pet);
   }
       
   public String toString(){ // Formato para impressao do cadastro.
      StringBuffer ts = new StringBuffer();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
      ts.append(String.format("%3d. ",cod));
      ts.append(String.format("%-15s",nome));
      ts.append(String.format("%s",data.format(formatter)));
      ts.append(String.format("%9d",getIdade()));
      ts.append("      ");
      ts.append(String.format("%-15s",endereco));
      for (Pet pet : pets) {
         ts.append(String.format("\n     Pet: %-10s",pet.getNome()));
         ts.append(String.format("%-17s",pet.getDataNascimento().format(formatter)));
         ts.append(String.format("%-8s", pet.getIdade()));
         ts.append(String.format("Tipo: %s",pet.getTipo()));
      }
      ts.append("\n");
      return ts.toString();
   }
   
   public String toString1(){ // Formato para resultado da busca.
      StringBuffer ts = new StringBuffer();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
      for (Pet pet : pets) {
         ts.append(String.format("\n  %-15s",pet.getNome()));
         ts.append(String.format("%20s",pet.getDataNascimento().format(formatter)));
         ts.append(String.format("%25s", pet.getTipo()));
      }
      ts.append("\n");
      return ts.toString();
   }
}