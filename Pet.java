import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.Period;

public class Pet implements Serializable {
   private String nome;
   private String tipo;
   private LocalDate dataNascimento;
   
   public String getNome() {
      return nome;
   }
   
   public String getTipo() {
      return tipo;
   }
   
   public LocalDate getDataNascimento() {
      return dataNascimento;
   }
   
   public int getIdade() {
      return Period.between(this.dataNascimento, LocalDate.now()).getYears();
   }
   
   public Pet(String nome, String tipo, int dia, int mes, int ano) {
      this.nome = nome;
      this.tipo = tipo;
      this.dataNascimento = LocalDate.of(ano, mes, dia);
   }
   
   public String toString() { 
      StringBuffer ts = new StringBuffer();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
      ts.append(String.format("%-35s", nome));
      ts.append(String.format("%s", dataNascimento.format(formatter)));
      ts.append(String.format("%7d", getIdade()));
      return ts.toString();
   }
}
